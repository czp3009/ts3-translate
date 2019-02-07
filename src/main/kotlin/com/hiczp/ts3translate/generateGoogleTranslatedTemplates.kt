package com.hiczp.ts3translate

import org.w3c.dom.Element
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun generateGoogleTranslatedTemplates() {
    googleTranslatedTemplatesDirectory.mkdirs()

    allFiles.forEach { file ->
        val translatedPureTextFileName = "$file.txt"
        val originalTemplateFileName = "${file}_${commandLineArguments.language}$templateFileSuffix"

        val document = Scanner(translatedPureTextDirectory.resolve(translatedPureTextFileName)).use { scanner ->
            originalTemplatesDirectory.resolve(originalTemplateFileName).let {
                DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
            }.apply {
                getElementsByTagName("context").forEach { context ->
                    (context as Element).getElementsByTagName("message").forEach { message ->
                        @Suppress("SpellCheckingInspection")
                        //是否复数
                        val numerus = message.attributes.getNamedItem("numerus")?.nodeValue == "yes"

                        val source = (message as Element).getElementsByTagName("source").item(0).textContent
                        val translation = message.getElementsByTagName("translation").item(0) as Element

                        @Suppress("ReplaceSingleLineLet")
                        val processedText = scanner.nextLine().let {
                            processAcceleratorKey(source, it)
                        }.let {
                            processLeadingSpace(source, it)
                        }.let {
                            processTrailingSpace(source, it)
                        }

                        if (!numerus) {
                            translation
                        } else {
                            @Suppress("SpellCheckingInspection")
                            translation.getElementsByTagName("numerusform").item(0)
                        }.textContent = processedText
                    }
                }
            }
        }.apply {
            xmlStandalone = true
        }

        googleTranslatedTemplatesDirectory.resolve(originalTemplateFileName).bufferedWriter().use {
            TransformerFactory.newInstance().newTransformer().transform(
                DOMSource(document),
                StreamResult(it)
            )
            //补充最后一个换行符
            it.newLine()
        }
    }
}

//处理原文中的快捷键
private fun processAcceleratorKey(source: String, translatedText: String) =
    if (source.startsWith("&")) {   //TODO &符号可能不在第一位
        val acceleratorKey = source[1]
        if (translatedText.startsWith("&")) {
            translatedText.substring(1)
        } else {
            translatedText
        } + "(&${acceleratorKey.toUpperCase()})"
    } else {
        translatedText
    }

//处理译文开头的空格与换行
private fun processLeadingSpace(source: String, translatedText: String): String {
    var index = -1
    for (i in 0 until source.length) {
        val char = source[i]
        if (char == ' ' || char == '\n') {
            index = i
        } else {
            break
        }
    }
    return if (index != -1) {
        source.substring(0, index + 1) + translatedText
    } else {
        translatedText
    }
}

//处理末尾的空格
private fun processTrailingSpace(source: String, translatedText: String): String {
    var count = 0
    for (i in source.length - 1 downTo 0) {
        if (source[i] == ' ') {
            count++
        } else {
            break
        }
    }
    return translatedText + " ".repeat(count)
}
