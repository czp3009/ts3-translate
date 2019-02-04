package com.hiczp.ts3translate

import org.w3c.dom.Node
import org.w3c.dom.NodeList

inline fun NodeList.forEach(block: (Node) -> Unit) {
    for (i in 0 until length) {
        block(item(i))
    }
}
