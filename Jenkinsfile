@Library("shared-libraries")
import libs.ProjectHelpers
import libs.Utils

def utils = new Utils()
def projectHelpers = new ProjectHelpers()

def kickUsersTasks = [:]
def lockIBTasks = [:]
def backupConfTasks = [:]
def backupBaseTasks = [:]
def prepareUpdateTasks = [:]
def updateIBTasks = [:]
def unlockIBTasks = [:]

pipeline {
    parameters {
        string(defaultValue: "${env.jenkinsAgent}", description: 'Нода дженкинса, на которой запускать пайплайн. По умолчанию master', name: 'jenkinsAgent')
        string(defaultValue: "${env.platform1c}", description: 'Версия платформы 1с, например 8.3.14.1694', name: 'platform1c')
        string(defaultValue: "${env.server1c}", description: 'Имя сервера 1с, по умолчанию localhost', name: 'server1c')
        string(defaultValue: "${env.port1c}", description: 'Порт сервера 1с. По умолчанию 1540. Не путать с портом агента кластера (1541)', name: 'port1c')
        string(defaultValue: "${env.infobases}", description: 'Список баз для обновления через запятую. Например c83_ack,c83_ato', name: 'infobases')
        string(defaultValue: "${env.user1c}", description: 'Имя администратора базы 1с Должен быть одинаковым для всех баз', name: 'user1c')
        string(defaultValue: "${env.pass1c}", description: 'Пароль администратора базы 1C. Должен быть одинаковым для всех баз', name: 'pass1c')
        string(defaultValue: "${env.backupDir}", description: 'Путь для сохранения бэкапов 1c', name: 'backupDir')
        string(defaultValue: "${env.repServer1c}", description: 'Имя сервера 1с базы, подключенной к хранилищу, по умолчанию localhost', name: 'repServer1c')
        string(defaultValue: "${env.repInfobase}", description: 'База подключенная к хранилищу', name: 'repInfobase')
        string(defaultValue: "${env.repUser1c}", description: 'Имя администратора базы 1с, подключенной к хранилищу', name: 'repUser1c')
        string(defaultValue: "${env.repPass1c}", description: 'Пароль администратора базы 1с, подключенной к хранилищу', name: 'repPass1c')
        string(defaultValue: "${env.repPath}", description: 'Необязательный. Пути к хранилищам 1С для обновления копий баз тестирования через запятую. Число хранилищ (если указаны), должно соответствовать числу баз тестирования. Например D:/temp/storage1c/ack,D:/temp/storage1c/ato', name: 'repPath')
        string(defaultValue: "${env.repUser}", description: 'Необязательный. Администратор хранилищ  1C. Должен быть одинаковым для всех хранилищ', name: 'repUser')
        string(defaultValue: "${env.repPass}", description: 'Необязательный. Пароль администратора хранилищ 1c', name: 'repPass')
        string(defaultValue: "${env.permCode}", description: 'Необязательный. Код блокировки ИБ при обновлениию. По умолчанию 0000', name: 'permCode')
    }
    agent {
        label "${(env.jenkinsAgent == null || env.jenkinsAgent == 'null') ? "master" : env.jenkinsAgent}"
    }
    options {
        timeout(time: 8, unit: 'HOURS') 
        buildDiscarder(logRotator(numToKeepStr:'10'))
    }
    stages {
        stage("Подготовка") {
            steps {
                timestamps {
                    script {
                        infobasesList = utils.lineToArray(infobases.toLowerCase())
                        repPathList = utils.lineToArray(repPath.toLowerCase())
                        if (repPathList.size() != 0) {
                            assert repPathList.size() == infobasesList.size()
                        }
                        server1c = server1c.isEmpty() ? "localhost" : server1c
                        repServer1c = repServer1c.isEmpty() ? "localhost" : repServer1c
                        port1c = port1c.isEmpty() ? "1540" : port1c
                        repPass = repPass.isEmpty() ? "default" : repPass
                        permCode = permCode.isEmpty() ? "0000" : permCode
                        // создаем пустые каталоги
                        dir ('build') {
                            writeFile file:'dummy', text:''
                        }
                    }
                }
            }
        }
        stage("Запуск") {
            steps {
                timestamps {
                    script {
                        for (i = 0;  i < infobasesList.size(); i++) {
                            infobase = infobasesList[i]
                            // 1. Блокирум запуск соединений и РЗ
                            lockIBTasks["lockIBTask_${infobase}"] = lockIBTask(server1c, port1c, infobase, user1c, pass1c, "lock", permCode)
                            // 2. Выбрасываем пользователей из базы 1С
                            kickUsersTasks["kickUsersTask_${infobase}"] = kickUsersTask(server1c, port1c, infobase, user1c, pass1c)
                            // 3. Создаём бэкап конфигурации
                            backupConfTasks["backupConfTask_${infobase}"] = backupConfTask(platform1c, server1c, infobase, user1c, pass1c, backupDir, permCode)
                            // 4. Создаём бэкап ИБ
                            backupBaseTasks["backupBaseTask_${infobase}"] = backupBaseTask(platform1c, server1c, infobase, user1c, pass1c, backupDir, permCode)
                            // 5. Создаем файл обновления
                            prepareUpdateTasks["prepareUpdateTask_${infobase}"] = prepareUpdateTask(platform1c, backupDir, repServer1c, repInfobase, repUser1c, repPass1c, repPath, repUser, repPass)
                            // 6. Обновляем ИБ
                            updateIBTasks["updateIBTask_${infobase}"] = updateIBTask(platform1c, server1c, infobase, user1c, pass1c, backupDir, permCode)
                            // 7. Разблокирум запуск соединений и РЗ
                            unlockIBTasks["unlockIBTask_${infobase}"] = lockIBTask(server1c, port1c, infobase, user1c, pass1c, "unlock", permCode)
                        }
                        parallel lockIBTasks
                        parallel kickUsersTasks
                        // parallel backupConfTasks
                        // parallel backupBaseTasks
                        // parallel prepareUpdateTasks
                        parallel updateIBTasks
                        parallel unlockIBTasks
                    }
                }
            }
        }
    }   
}

def lockIBTask(server1c, port1c, infobase, user, pass, action, permCode) {
    return {
        stage("Изменение разрешения на запуск РЗ в ${infobase}: ${action}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.lockIBTask(server1c, port1c, infobase, user, pass, action, permCode)
            }
        }
    }
}

def kickUsersTask(server1c, port1c, infobase, user, pass) {
    return {
        stage("Выбрасывание пользователей из 1С ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.kickUsers(server1c, port1c, infobase, user, pass)
            }
        }
    }
}

def backupConfTask(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    return {
        stage("Создание выгрузки конфигурации базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.backupConf(platform1c, server1c, infobase, user, pass, backupDir, permCode)
            }
        }
    }
}

def backupBaseTask(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    return {
        stage("Создание выгрузки информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.backupBase(platform1c, server1c, infobase, user, pass, backupDir, permCode)
            }
        }
    }
}

def prepareUpdateTask(platform1c, backupDir, repServer1c, repInfobase, repUser1c, repPass1c, repPath, repUser, repPass) {
    return {
        stage("Подготовка файла обновления информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.prepareUpdate(platform1c, backupDir, repServer1c, repInfobase, repUser1c, repPass1c, repPath, repUser, repPass)
            }
        }
    }
}

def updateIBTask(platform1c, server1c, infobase, user, pass, backupDir, permCode) {
    return {
        stage("Обновление информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.updateBase(platform1c, server1c, infobase, user, pass, backupDir, permCode)
            }
        }
    }
}