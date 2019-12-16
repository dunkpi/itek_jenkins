' Скрипт управления доступом к старту РЗ в ИБ

Dim Server1c, AgentPort, Infobase, User, Passw, Action, PermissionCode, ' Входные параметры
Dim ObjV83Com, ObjServerAgent, ObjCluster, ObjWorkingProcess, ObjCurrentWorkingProcess, ObjBase
Dim Bases, Connections
Dim i

Server1c = Wscript.Arguments(0)
AgentPort = Wscript.Arguments(1)
Infobase = Wscript.Arguments(2)
User = Wscript.Arguments(3)
Passw = Wscript.Arguments(4)
Action = Wscript.Arguments(5)
PermissionCode = Wscript.Arguments(6)

Set ObjV83Com = CreateObject("V83.ComConnector")
Set ObjServerAgent = ObjV83Com.ConnectAgent(Server1c & ":" & AgentPort)
Set ObjCluster = ObjServerAgent.GetClusters()(0)
ObjServerAgent.Authenticate ObjCluster, User, Passw
Set ObjWorkingProcess = ObjServerAgent.GetWorkingProcesses(ObjCluster)(0)
Set ObjCurrentWorkingProcess = ObjV83Com.ConnectWorkingProcess(ObjWorkingProcess.HostName & ":" & ObjWorkingProcess.MainPort)
ObjCurrentWorkingProcess.AddAuthentication User, Passw
Bases = ObjCurrentWorkingProcess.GetInfoBases()
For Each i In Bases
    If i.Name = Infobase Then
        Set ObjBase = i
        Exit For
    End If
Next
If Action = "lock" Then
    ObjBase.SessionsDenied = True
    ObjBase.ScheduledJobsDenied = True
    ObjBase.PermissionCode = PermissionCode
    ObjBase.DeniedMessage = "Обновление информационной базы"
    ObjCurrentWorkingProcess.UpdateInfoBase(ObjBase)
ElseIf Action = "unlock" Then
    ObjBase.SessionsDenied = False
    ObjBase.ScheduledJobsDenied = False
    ObjCurrentWorkingProcess.UpdateInfoBase(ObjBase)
End If

Set ObjV83Com = Nothing
Set ObjServerAgent = Nothing
Set ObjCluster = Nothing
Set ObjWorkingProcess = Nothing
Set ObjCurrentWorkingProcess = Nothing
Set ObjBase = Nothing