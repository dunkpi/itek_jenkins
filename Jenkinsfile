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
        string(defaultValue: "${env.server1c}", description: 'Имя сервера 1с, по умолчанию localhost', name: 'server1c')
        string(defaultValue: "${env.server1cPort}", description: 'Порт рабочего сервера 1с. По умолчанию 1540. Не путать с портом агента кластера (1541)', name: 'server1cPort')
        string(defaultValue: "${env.platform1c}", description: 'Версия платформы 1с, например 8.3.14.1694. По умолчанию будет использована последня версия среди установленных', name: 'platform1c')
        string(defaultValue: "${env.admin1cUser}", description: 'Имя администратора с правом открытия вншних обработок (!) для базы тестирования 1с Должен быть одинаковым для всех баз', name: 'admin1cUser')
        string(defaultValue: "${env.admin1cPwd}", description: 'Пароль администратора базы тестирования 1C. Должен быть одинаковым для всех баз', name: 'admin1cPwd')
        string(defaultValue: "${env.templatebases}", description: 'Список баз для тестирования через запятую. Например work_erp,work_upp', name: 'templatebases')
        string(defaultValue: "${env.backupDir}", description: 'Пусть для сохранения бэкапов конфигурации и ИБ', name: 'backupDir')
        string(defaultValue: "${env.permissionCode}", description: 'Необязательный. Код блокировки ИБ при обновлениию. По умолчанию 0000', name: 'permissionCode')
        string(defaultValue: "${env.storages1cPath}", description: 'Необязательный. Пути к хранилищам 1С для обновления копий баз тестирования через запятую. Число хранилищ (если указаны), должно соответствовать числу баз тестирования. Например D:/temp/storage1c/erp,D:/temp/storage1c/upp', name: 'storages1cPath')
        string(defaultValue: "${env.storageUser}", description: 'Необязательный. Администратор хранилищ  1C. Должен быть одинаковым для всех хранилищ', name: 'storageUser')
        string(defaultValue: "${env.storagePwd}", description: 'Необязательный. Пароль администратора хранилищ 1c', name: 'storagePwd')
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
                        templatebasesList = utils.lineToArray(templatebases.toLowerCase())
                        storages1cPathList = utils.lineToArray(storages1cPath.toLowerCase())
                        if (storages1cPathList.size() != 0) {
                            assert storages1cPathList.size() == templatebasesList.size()
                        }
                        server1c = server1c.isEmpty() ? "localhost" : server1c
                        serverSql = serverSql.isEmpty() ? "localhost" : serverSql
                        server1cPort = server1cPort.isEmpty() ? "1540" : server1cPort
                        permissionCode = permissionCode.isEmpty() ? "0000" : permissionCode
                        storagePwd = storagePwd.isEmpty() ? "default" : storagePwd
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
                        for (i = 0;  i < templatebasesList.size(); i++) {
                            templateDb = templatebasesList[i]
                            // 1. Блокирум запуск соединений и РЗ
                            lockIBTasks["lockIBTask_${templateDb}"] = lockIBTask(
                                server1c, 
                                server1cPort, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd,
                                "lock",
                                permissionCode
                            )
                            // 2. Выбрасываем пользователей из базы 1С
                            kickUsersTasks["kickUsersTask_${templateDb}"] = kickUsersTask(
                                server1c, 
                                server1cPort, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd
                            )
                            // 3. Создаём бэкап конфигурации
                            backupConfTasks["backupConfTask_${templateDb}"] = backupConfTask(
                                platform1c,
                                server1c, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd,
                                backupDir,
                                permissionCode
                            )
                            // 4. Создаём бэкап ИБ
                            backupBaseTasks["backupBaseTask_${templateDb}"] = backupBaseTask(
                                platform1c,
                                server1c, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd,
                                backupDir,
                                permissionCode
                            )
                            // 5. Создаем файл обновления
                            prepareUpdateTasks["prepareUpdateTask_${templateDb}"] = prepareUpdateTask(
                                platform1c, 
                                backupDir, 
                                repServer1c, 
                                repInfobase, 
                                repIBUser, 
                                repIBPassw, 
                                repAddress, 
                                repUser, 
                                repPassw
                            )
                            // 6. Обновляем ИБ
                            updateIBTasks["updateIBTask_${templateDb}"] = updateIBTask(
                                platform1c,
                                server1c, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd,
                                backupDir,
                                permissionCode
                            )
                            // 7. Разблокирум запуск соединений и РЗ
                            lockIBTasks["lockIBTask_${templateDb}"] = lockIBTask(
                                server1c, 
                                server1cPort, 
                                templateDb,
                                admin1cUser,
                                admin1cPwd,
                                "unlock",
                                permissionCode
                            )
                        }
                        parallel lockJobsStartTasks
                        parallel kickUsersTasks
                        parallel backupConfTasks
                        parallel backupBaseTasks
                        parallel unlockJobsStartTasks
                        parallel prepareUpdateTasks
                        parallel updateIBTasks
                    }
                }
            }
        }
    }   
    post {
        always {
            script {
                if (currentBuild.result == "ABORTED") {
                    return
                }
                dir ('build/out/allure') {
                    writeFile file:'environment.properties', text:"Build=${env.BUILD_URL}"
                }
                allure includeProperties: false, jdk: '', results: [[path: 'build/out/allure']]
            }
        }
    }
}

def lockIBTask(server1c, server1cPort, infobase, user, pwd, action, permissionCode) {
    return {
        stage("Изменение разрешения на запуск РЗ в ${infobase}: ${action}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.lockIBTask(server1c, server1cPort, infobase, user, pwd, action, permissionCode)
            }
        }
    }
}

def kickUsersTask(server1c, server1cPort, infobase, user, pwd) {
    return {
        stage("Выбрасывание пользователей из 1С ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.kickUsers(server1c, server1cPort, infobase, user, pwd)
            }
        }
    }
}

def backupConfTask(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    return {
        stage("Создание выгрузки конфигурации базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.backupConf(platform1c, server1c, infobase, user, owd, backupDir, permissionCode)
            }
        }
    }
}

def backupBaseTask(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    return {
        stage("Создание выгрузки информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.backupBase(platform1c, server1c, infobase, user, owd, backupDir, permissionCode)
            }
        }
    }
}

def prepareUpdateTask(platform1c, backupDir, repServer1c, repInfobase, repIBUser, repIBPassw, repAddress, repUser, repPassw) {
    return {
        stage("Подготовка файла обновления информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.prepareUpdate(platform1c, backupDir, repServer1c, repInfobase, repIBUser, repIBPassw, repAddress, repUser, repPassw)
            }
        }
    }
}

def updateIBTask(platform1c, server1c, infobase, user, owd, backupDir, permissionCode) {
    return {
        stage("Обновление информационной базы ${infobase}") {
            timestamps {
                def projectHelpers = new ProjectHelpers()
                projectHelpers.updateBase(platform1c, server1c, infobase, user, owd, backupDir, permissionCode)
            }
        }
    }
}