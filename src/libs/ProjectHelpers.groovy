package libs

// Управляет возможностью запуска РЗ в ИБ
//
// Параметры:
//  server1c - сервер 1c
//  server1cPort - порт сервера 1с
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pwd - пароль админа 1С базы
//  action - lock/unlock - заблокировать/разблокировать РЗ
//  permissionCode - код блокирования ИБ
//
def lockIBTask(server1c, server1cPort, infobase, user, pwd, action, permissionCode) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/lock_ib.vbs \"${server1c}\" \"${server1cPort}\" \"${infobase}\" \"${user}\" \"${pwd}\" \"${action}\" \"${permissionCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при изменении возможности запуска РЗ для  ${infobase} на ${action}")
    }
}

// Выбрасывает пользователей из ИБ
//
// Параметры:
//  server1c - сервер 1c
//  server1cPort - порт сервера 1с
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pwd - пароль админа 1С базы
//
def kickUsers(server1c, server1cPort, infobase, user, pwd) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/kick_users.vbs \"${server1c}\" \"${server1cPort}\" \"${infobase}\" \"${user}\" \"${pwd}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при выбрасывании пользователей из  ${infobase}")
    }
}

// Создает бэкап конфигурации ИБ
//
// Параметры:
//  platform1c - версия платформы 1С, например 8.3.12.1529
//  server1c - сервер 1c
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pwd - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permissionCode - код блокирования ИБ
//
def backupConf(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/backup_conf.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pwd}\" \"${backupDir}\" \"${permissionCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при сохранении конфигурации базы ${infobase}")
    }
}

// Создает бэкап ИБ
//
// Параметры:
//  platform1c - версия платформы 1С, например 8.3.12.1529
//  server1c - сервер 1c
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pwd - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permissionCode - код блокирования ИБ
//
def backupBase(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/backup_ib.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pwd}\" \"${backupDir}\" \"${permissionCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при сохранении информационной базы ${infobase}")
    }
}

// Создает бэкап ИБ
//
// Параметры:
//  platform1c - версия платформы 1С, например 8.3.12.1529
//  backupDir - директория для сохранения бэкапа конфигурации
//  repServer1c - сервер 1с базы, подлключенной к хранилищу
//  repInfobase - база 1с, подключенная к хранилищу
//  repIBUser - пользователь базы 1с, подключенной к хранилищу
//  repIBPassw - пароль пользователя базы 1с, подключенной к хранилищу
//  repAddress - адрес хранилища
//  repUser - пользователь хранилища
//  repPassw - пароль пользователя хранилища
//
def prepareUpdate(platform1c, backupDir, repServer1c, repInfobase, repIBUser, repIBPassw, repAddress, repUser, repPassw) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/prepare_update.vbs \"${platform1c}\" \"${backupDir}\" \"${repServer1c}\" \"${repInfobase}\" \"${repIBUser}\" \"${repIBPassw}\" \"${repAddress}\" \"${repUser}\" \"${repPassw}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при создании обновления ${infobase}")
    }
}

// Создает бэкап ИБ
//
// Параметры:
//  platform1c - версия платформы 1С, например 8.3.12.1529
//  server1c - сервер 1c
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pwd - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permissionCode - код блокирования ИБ
//
def updateBase(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/update_ib.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pwd}\" \"${backupDir}\" \"${permissionCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при обновлении информационной базы ${infobase}")
    }
}