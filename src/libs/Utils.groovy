package libs

// Выполняет команду в среде ОС Windows (batch) и возвращает статус операции
//
// Параметры:
//  command - строка команды, которую нужно выполнить
//  workDir  - рабочий каталог для команды 
//
// Возвращаемое значение
//  Integer - код выполнения операции
//
def cmd(command, workDir = "") {
    if (!workDir.isEmpty()) {
        command = "${getWorkspaceLine(workDir)} ${command}"
    }
    def returnCode = 0
    returnCode = bat script: "chcp 65001\n${command}", returnStatus: true
    return returnCode
}

// Вызывает ошибку, которая прекращает исполнение кода и прикрепляет текст ошибки архивом к сборке
//
// Параметры:
//
//  errorText - читаемое описание ошибки
//
def raiseError(errorText) {
    setBuildResultMessage(errorText)
    error errorText
}

// Создает и прикрепляет артефакт к сборке в виде текстового файла. Каждый вызов метода перезатирает артефакт.
//
// Параметры:
//  text - текст для помещения в артефакт
//
def setBuildResultMessage(text){
    def fileName = 'BuildResultMessage.txt'
    writeFile(file: fileName, text: text, encoding: "UTF-8")
    step([$class: 'ArtifactArchiver', artifacts: fileName, fingerprint: true])
}

// Конвертирует строку в массив по сплиттеру
//
// Параметры:
//  line - строка с разделителями
//
// Возвращаемое значение
//  Array - массив строк
//
def lineToArray(line, splitter = ",") {
    dirtArray = line.replaceAll("\\s", "").split(",")
    cleanArray = []
    for (item in dirtArray) {
        if (!item.isEmpty()) {
            cleanArray.add(item)
        }
    }
    return cleanArray
}