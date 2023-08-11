package com.example.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;

class CopyTask extends DefaultTask{
    // 指定输入
    @InputFiles
    FileCollection from

    // 指定输出
    @OutputDirectory
    Directory to

    @TaskAction
    void copy() {
        File file = from.getSingleFile()
        if (file.isDirectory()) {
            from.getAsFileTree().each {
                copyFileToDir(it, to)
            }
        } else {
            copyFileToDir(from, to)
        }
    }

    /**
     * 复制文件到文件夹
     * @param src 要复制的文件
     * @param dir 接收的文件夹
     * @return
     */
    private static def copyFileToDir(File src, Directory dir) {
        File dest = new File("${dir.getAsFile().path}/${src.name}")

        if (!dest.exists()) {
            dest.createNewFile()
        }

        dest.withOutputStream {
            it.write(new FileInputStream(src).getBytes())
        }
    }

}
