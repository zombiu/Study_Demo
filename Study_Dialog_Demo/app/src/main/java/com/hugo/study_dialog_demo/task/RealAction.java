package com.hugo.study_dialog_demo.task;

import androidx.core.util.Consumer;

import java.util.Objects;

public class RealAction implements Action {
    private ActionChain chain;
    private final String tag;
    private int priority;
    private Consumer<Action> consumer;

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
    public void run() {
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setConsumer(Consumer<Action> consumer) {
        this.consumer = consumer;
    }

    // 从小到大排序
    @Override
    public int compareTo(Action o) {
        if (priority > o.priority()) {
            return 1;
        }else if (priority < o.priority()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RealAction)) return false;
        RealAction that = (RealAction) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return "RealAction{" +
                "tag='" + tag + '\'' +
                ", priority=" + priority +
                '}';
    }
}
