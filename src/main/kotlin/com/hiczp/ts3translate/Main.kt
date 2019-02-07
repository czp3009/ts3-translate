package com.hiczp.ts3translate

import com.xenomachina.argparser.ArgParser
import java.io.File

//普通文件
val normalFiles = arrayOf(
    "error_report",
    "lagos",
    "package_installer",
    "update"
)

//source 不为英文原文的文件
val separatedFiles = arrayOf(
    "permissions",
    "ts_core_errors"
)

val allFiles = normalFiles + separatedFiles

//模板文件后缀
const val templateFileSuffix = ".ts"
//编译后的模板文件后缀
const val compiledTemplateFileSuffix = ".qm"

val translateTemplatesDirectory = File("translate_templates")
val originalTemplatesDirectory = File("original_templates")
val pureTextDirectory = File("pure_text")
val translatedPureTextDirectory = File("translated_pure_text")
val googleTranslatedTemplatesDirectory = File("google_translated_templates")
val distDirectory = File("dist")

lateinit var commandLineArguments: CommandLineArguments

fun main(args: Array<String>) {
    commandLineArguments = ArgParser(args).parseInto(::CommandLineArguments)

    //生成对应语言的待翻译源文件
    generateOriginalTemplates()
    println("Original templates generated: ${originalTemplatesDirectory.absolutePath}")

    //生成谷歌翻译需要使用的 txt 文件
    generatePureTextFiles()
    println("Pure text files generated: ${pureTextDirectory.absolutePath}")

    //创建存放机翻后的文件的文件夹
    translatedPureTextDirectory.mkdirs()
    print("Please put translated pure text files in ${translatedPureTextDirectory.absolutePath} then press ENTER")

    //等待换行符
    readLine()

    //将文本充填回 xml
    generateGoogleTranslatedTemplates()
    println("Translated files generated in ${googleTranslatedTemplatesDirectory.absolutePath}")

    //编译 ts 文件
    compileTsFiles()
    println("Files compiled: ${distDirectory.absolutePath}")

    println("DONE!")
}
