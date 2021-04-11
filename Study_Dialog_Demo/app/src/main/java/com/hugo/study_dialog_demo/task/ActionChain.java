package com.hugo.study_dialog_demo.task;

import com.blankj.utilcode.util.LogUtils;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 每个任务链
 */
public class ActionChain {

    /**
     * 线程安全的优先级队列
     */
    private PriorityBlockingQueue<IAction> queue = new PriorityBlockingQueue<>();

    public ActionChain(){

    }

    public void register(IAction action) {
        if (action == null) {
            LogUtils.e("-->>action不能为null");
            return;
        }
        action.bind(this);
        queue.put(action);
    }

    /**
     * 执行action
     */
    public void notifyAction() {
        IAction poll = queue.poll();
        if (poll == null) {
            return;
        }
        if (poll.isReady()){
            poll.run();
        }else {
            queue.put(poll);
        }
    }
}
