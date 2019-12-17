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

BackupName = Infobase & "_" & CurrentDTFormat()

Set ObjShell = CreateObject("WScript.Shell")
ObjShell.Run """" & Platform1C & """ DESIGNER" & _
" /S""" & Server1c & "\" & Infobase & """" & _
" /N""" & User & """ /P""" & Passw & """" & _
" /UC " & PermissionCode & _
" /Out""" & BackupDir & "\" & BackupName & ".txt""" & _
" -NoTruncate" & _
" /DumpIB""" & BackupDir & "\" & BackupName & ".dt""" , , True


Set ObjShell = Nothing

Function CurrentDTFormat()
  m = Month(Date())
  If m < 10 Then
      m = "0" & m
  End If
  d = Day(Date())
  If d < 10 Then
      d = "0" & d
  End If
  h = Hour(Time())
  If h < 10 Then
      h = "0" & h
  End If
  CurrentDTFormat = Year(Date()) & "-" & m & "-" & d & "_" & h
End Function