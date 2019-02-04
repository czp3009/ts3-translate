package com.hiczp.ts3translate

import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

fun generatePureTextFiles() {
    pureTextDirectory.mkdirs()

    allFiles.forEach { file ->
        val isSeparatedFile = file in separatedFiles
        val fileNeedToParse = if (isSeparatedFile) {
            "$file$templateFileSuffix"
        } else {
            "${file}_${commandLineArguments.language}$templateFileSuffix"
        }.let {
            originalTemplatesDirectory.resolve(it)
        }
        val outputFileName = "$file.txt"
        val parseField = if (isSeparatedFile) "translation" else "source"

        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileNeedToParse)
        pureTextDirectory.resolve(outputFileName).also {
            if (it.exists()) it.delete()
            it.createNewFile()
        }.bufferedWriter().use { writer ->
            document.getElementsByTagName("context").forEach { context ->
                (context as Element).getElementsByTagName("message").forEach { message ->
                    val source = (message as Element).getElementsByTagName(parseField)
                        .item(0)
                        .textContent
                        .replace("\n", " ")
                    writer.run {
                        write(source)
                        newLine()
                    }
                }
            }
        }
    }
}
