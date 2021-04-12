package com.hugo.study_dialog_demo.task;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 按顺序执行的任务链
 */
public class ActionChain {

    public String tag;

    /**
     * 线程安全的优先级队列
     */
    private PriorityBlockingQueue<Action> queue = new PriorityBlockingQueue<>();

    public ActionChain(){

    }

    public PriorityBlockingQueue<Action> getQueue() {
        return queue;
    }

    public void register(Action action) {
        action.bind(this);
        queue.put(action);
//        LogUtils.e("-->> " + queue.toString());
    }

    /**
     * 执行action
     */
    public void notifyAction() {
        Action poll = queue.poll();
        if (poll == null) {
            return;
        }
        LogUtils.e("-->>" + poll.toString());
        if (poll.isReady()){
            poll.run();
        }else {
            queue.put(poll);
        }
    }

    public void clear(String tag) {
        RealAction realAction = new RealAction(tag);
       clear(realAction);
    }

    public void clear(RealAction action) {
        queue.remove(action);
    }
}
