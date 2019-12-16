' Скрипт создания выгрузки ИБ

Dim Platform1C, Server1c, Infobase, User, Passw, BackupDir, PermissionCode ' Входные параметры
Dim ObjShell
Dim BackupName

Platform1C = Wscript.Arguments(0)
Server1c = Wscript.Arguments(1)
Infobase = Wscript.Arguments(2)
User = Wscript.Arguments(3)
Passw = Wscript.Arguments(4)
BackupDir = Wscript.Arguments(5)
PermissionCode = Wscript.Arguments(6)

If RepPassw = "default" Then
    RepPassw = """"""
End If

Set ObjShell = CreateObject("WScript.Shell")

'Обновление конфигурации
ObjShell.Run """C:\Program Files (x86)\1cv8\" & Platform1C & "\bin\1cv8.exe"" DESIGNER" & _
" /S""" & Server1c & "\" & Infobase & """" & _
" /N""" & User & """ /P""" & Passw & """" & _
" /Out""" & BackupDir & "\log_Step3_UpdateCfg.txt""" & _
" /DisableStartupMessages /LoadCfg """ & BackupDir & "\update.cf""",, True

'Обновление конфигурации БД
ObjShell.Run """C:\Program Files (x86)\1cv8\" & Platform1C & "\bin\1cv8.exe"" DESIGNER" & _
" /S""" & Server1c & "\" & Infobase & """" & _
" /N""" & User & """ /P""" & Passw & """" & _
" /Out""" & BackupDir & "\log_Step4_UpdateDBCfg.txt""" & _
" /DisableStartupMessages /UpdateDBCfg -Server -Dynamic-",, True

Set ObjShell = Nothing