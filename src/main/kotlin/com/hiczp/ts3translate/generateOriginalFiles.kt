package com.hiczp.ts3translate

import java.nio.file.Files
import java.nio.file.StandardCopyOption
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
        val source = versionedTranslateTemplatesDirectory.resolve("${it}_xx$templateFileSuffix")
        val target = originalTemplatesDirectory.resolve("${it}_${commandLineArguments.language}$templateFileSuffix")
        Files.copy(
            source.toPath(),
            target.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    //额外拷贝
    separatedFiles.forEach {
        val fileName = "$it$templateFileSuffix"

        Files.copy(
            versionedTranslateTemplatesDirectory.resolve(fileName).toPath(),
            originalTemplatesDirectory.resolve(fileName).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}
