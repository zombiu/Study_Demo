package com.example.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;

class MyTestPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("custom plugin -->>this is a plugin in the file 'build.gradle'...")
        //定义 扩展，并且与 ExtConfig 进行绑定。
        ExtConfig extConfig = project.extensions.create('extConfig', ExtConfig.class)
        // 创建了一个 task  printExtConfig
        // 执行对应task 使用命令 gradlew printExtConfig  后面是task名
        Task task = project.tasks.create('printExtConfig') {
            //通过 extConfig 属性，获取外部传递进来的 message 的具体内容。
            println("DemoPlugin message =" + project.extConfig.message)
            // task 执行完后 会去执行 doLast 这里可以拿到最新的 extension设置的值
            doLast {
                println("custom plugin -->> doLast message=${extConfig.message} count=${extConfig.count}")
            }
            doFirst {
                println("custom plugin -->> doFirst message=${extConfig.message} count=${extConfig.count}")
            }
        }
        // 使用命令 gradlew CustomTask
        project.tasks.register("CustomTask", CustomTask) { task1 ->
            taskName = "我是传入的Task Name"
            doLast {
                println("custom task -->> doLast taskName=${task1.taskName}")
            }
        }
    }
}