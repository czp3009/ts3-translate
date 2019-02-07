package com.hiczp.ts3translate

import java.io.File

fun compileTsFiles() {
    allFiles.map {
        "${it}_${commandLineArguments.language}"    //不包含后缀的文件名
    }.forEach { fileNameWithoutSuffix ->
        //调用命令行
        val sourceFileName = "$fileNameWithoutSuffix$templateFileSuffix"
        "${commandLineArguments.lReleasePath} ${googleTranslatedTemplatesDirectory.resolve(sourceFileName).absolutePath}"
            .runCommand(googleTranslatedTemplatesDirectory)

        //移动编译后的文件
        val compiledFileName = "$fileNameWithoutSuffix$compiledTemplateFileSuffix"
        googleTranslatedTemplatesDirectory.resolve(compiledFileName).run {
            copyTo(
                distDirectory.resolve(compiledFileName),
                true
            )
            delete()
        }
    }
}

private fun String.runCommand(workingDir: File) =
    ProcessBuilder(split(" "))
        .directory(workingDir)
        .start()
        .waitFor()
