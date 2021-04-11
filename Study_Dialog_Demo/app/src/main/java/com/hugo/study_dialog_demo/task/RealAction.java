package com.hugo.study_dialog_demo.task;

import androidx.core.util.Consumer;

import kotlin.Function;

public class RealAction implements IAction {
    private ActionChain chain;
    private final String tag;
    private int priority;
    private Consumer<IAction> consumer;

    public RealAction(String tag) {
        this.tag = tag;
    }

    @Override
    public void bind(ActionChain chain){
        if (this.chain != null) {
          // 不能重复bind
            return;
        }
        this.chain = chain;
    }

    @Override
    public void next() {
        chain.notifyAction();
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public String tag() {
        return tag;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void run() {
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setConsumer(Consumer<IAction> consumer) {
        this.consumer = consumer;
    }
}
