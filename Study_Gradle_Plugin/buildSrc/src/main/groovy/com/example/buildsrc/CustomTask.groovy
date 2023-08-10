package com.example.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction;

class CustomTask extends DefaultTask{
    String message = "this is old value"
    int count = 0

    void count(int count) {
        this.count = count
    }

    @Internal
    def taskName = "default"

    @TaskAction
    def MyAction1() {
        println("$taskName -- MyAction1")
    }

    @TaskAction
    def MyAction2() {
        println("$taskName -- MyAction2")
    }
}
