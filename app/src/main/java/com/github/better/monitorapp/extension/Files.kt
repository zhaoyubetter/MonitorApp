package com.github.better.monitorapp.extension

import java.io.File

/**
 * File类扩展
 * @author zhaoyu1  2019/5/16
 **/

/**
 * 文件内容追加
 */
fun File.appendContext(content: String) {
    if (!this.exists()) {
        if (!this.parentFile.exists()) {
            this.parentFile.mkdirs()
        }
        this.createNewFile()
    }
    this.appendText(content)
}