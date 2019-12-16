package libs

// Управляет возможностью запуска РЗ в ИБ
//
// Параметры:
//  server1c - сервер 1c
//  port1c - порт сервера 1с
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pass - пароль админа 1С базы
//  action - lock/unlock - заблокировать/разблокировать РЗ
//  permCode - код блокирования ИБ
//
def lockIBTask(server1c, port1c, infobase, user, pass, action, permCode) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/lock_ib.vbs \"${server1c}\" \"${port1c}\" \"${infobase}\" \"${user}\" \"${pass}\" \"${action}\" \"${permCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при изменении возможности запуска РЗ для  ${infobase} на ${action}")
    }
}

// Выбрасывает пользователей из ИБ
//
// Параметры:
//  server1c - сервер 1c
//  port1c - порт сервера 1с
//  infobase - имя базы на сервере 1c и sql
//  user - имя админа 1С базы
//  pass - пароль админа 1С базы
//
def kickUsers(server1c, port1c, infobase, user, pass) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/kick_users.vbs \"${server1c}\" \"${port1c}\" \"${infobase}\" \"${user}\" \"${pass}\"")
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
//  pass - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permCode - код блокирования ИБ
//
def backupConf(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/backup_conf.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pass}\" \"${backupDir}\" \"${permCode}\"")
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
//  pass - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permCode - код блокирования ИБ
//
def backupBase(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/backup_ib.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pass}\" \"${backupDir}\" \"${permCode}\"")
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
//  repUser1c - пользователь базы 1с, подключенной к хранилищу
//  repPass1c - пароль пользователя базы 1с, подключенной к хранилищу
//  repPath - адрес хранилища
//  repUser - пользователь хранилища
//  repPass - пароль пользователя хранилища
//
def prepareUpdate(platform1c, backupDir, repServer1c, repInfobase, repUser1c, repPass1c, repPath, repUser, repPass) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/prepare_update.vbs \"${platform1c}\" \"${backupDir}\" \"${repServer1c}\" \"${repInfobase}\" \"${repUser1c}\" \"${repPass1c}\" \"${repPath}\" \"${repUser}\" \"${repPass}\"")
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
//  pass - пароль админа 1С базы
//  backupDir - директория для сохранения бэкапа конфигурации
//  permCode - код блокирования ИБ
//
def updateBase(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    utils = new Utils()
    returnCode = utils.cmd("C:\\Windows\\SysWOW64\\cscript tools/update_ib.vbs \"${platform1c}\" \"${server1c}\" \"${infobase}\" \"${user}\" \"${pass}\" \"${backupDir}\" \"${permCode}\"")
    if (returnCode != 0) {
        utils.raiseError("Возникла ошибка при обновлении информационной базы ${infobase}")
    }
}