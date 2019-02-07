package com.hiczp.ts3translate

import kotlin.system.exitProcess

fun generateOriginalTemplates() {
    if (!translateTemplatesDirectory.exists()) {
        println("${translateTemplatesDirectory.absolutePath} not exists!")
        exitProcess(-1)
    }

    val versionedTranslateTemplatesDirectory = translateTemplatesDirectory
        .resolve("translation_templates_${commandLineArguments.version}")
        .also {
            if (!it.exists()) {
                println("${it.absolutePath} not exists")
                exitProcess(-1)
            }
        }

    originalTemplatesDirectory.mkdirs()

    //拷贝文件
    allFiles.forEach {
        versionedTranslateTemplatesDirectory.resolve("${it}_xx$templateFileSuffix")
            .copyTo(
                originalTemplatesDirectory.resolve("${it}_${commandLineArguments.language}$templateFileSuffix"),
                true
            )
    }

    //额外拷贝
    separatedFiles.map {
        "$it$templateFileSuffix"
    }.forEach {
        versionedTranslateTemplatesDirectory.resolve(it).copyTo(
            originalTemplatesDirectory.resolve(it),
            true
        )
    }
}
