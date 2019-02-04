package com.hiczp.ts3translate

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class CommandLineArguments(argParser: ArgParser) {
    val version by argParser.storing(
        "-v", "--version",
        help = "working version"
    ).default(defaultVersion)

    val language by argParser.storing(
        "-l", "--language",
        help = "working language"
    ).default(defaultLanguage)

    companion object {
        //预设的当前工作版本号
        const val defaultVersion = "3.2.3"
        //预设的当前工作语言
        const val defaultLanguage = "zh"
    }
}
