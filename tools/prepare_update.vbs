' Скрипт создания выгрузки ИБ

Dim Platform1C, BackupDir, RepServer1c, RepInfobase, RepIBUser, RepIBPassw, RepAddress, RepUser, RepPassw ' Входные параметры
Dim ObjShell
Dim BackupName

Platform1C = Wscript.Arguments(0)
BackupDir = Wscript.Arguments(1)
RepServer1c = Wscript.Arguments(2)
RepInfobase = Wscript.Arguments(3)
RepIBUser = Wscript.Arguments(4)
RepIBPassw = Wscript.Arguments(5)
RepAddress = Wscript.Arguments(6)
RepUser = Wscript.Arguments(7)
RepPassw = Wscript.Arguments(8)

If RepPassw = "default" Then
    RepPassw = """"""
End If

Set ObjShell = CreateObject("WScript.Shell")

'Создание файла обновления
ObjShell.Run """" & Platform1C & """ DESIGNER" & _
" /S""" & RepServer1c & "\" & RepInfobase & """" & _
" /N""" & RepIBUser & """ /P""" & RepIBPassw & """" & _
" /Out""" & BackupDir & "\log_Step2_CreateDistribFiles.txt""" & _
" /ConfigurationRepositoryF " & RepAddress & _
" /ConfigurationRepositoryN " & RepUser & _
" /ConfigurationRepositoryP " & RepPassw & _
" /ConfigurationRepositoryDumpCfg """ & BackupDir & "\update.cf""",, True

Set ObjShell = Nothing