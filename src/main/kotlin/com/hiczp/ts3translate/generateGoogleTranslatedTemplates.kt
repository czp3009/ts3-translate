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
                        (message as Element).getElementsByTagName("translation")
                            .item(0)
                            .textContent = scanner.nextLine()
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
