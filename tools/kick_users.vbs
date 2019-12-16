' Скрипт выбрасывания пользователей из ИБ

Dim Server1c, AgentPort, Infobase, User, Passw ' Входные параметры
Dim ObjV83Com, ObjServerAgent, ObjCluster, ObjWorkingProcess, ObjCurrentWorkingProcess, ObjBase
Dim Bases, Connections
Dim i

Server1c = Wscript.Arguments(0)
AgentPort = Wscript.Arguments(1)
Infobase = Wscript.Arguments(2)
User = Wscript.Arguments(3)
Passw = Wscript.Arguments(4)

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
Connections = ObjCurrentWorkingProcess.GetInfoBaseConnections(ObjBase)
For Each i In Connections
    If (i.AppID <> "COMConsole") then
        Wscript.echo "Reset connection " & i.AppID & " to infobase " & Infobase
        ObjCurrentWorkingProcess.Disconnect(i)
    End if
Next

Set ObjV83Com = Nothing
Set ObjServerAgent = Nothing
Set ObjCluster = Nothing
Set ObjWorkingProcess = Nothing
Set ObjCurrentWorkingProcess = Nothing
Set ObjBase = Nothing