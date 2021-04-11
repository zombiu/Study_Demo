package com.hugo.study_dialog_demo.task;

public interface IAction extends Runnable,Comparable{

    /**
     * 绑定到要执行的chain上
     * @param chain
     */
    void bind(ActionChain chain);
    /**
     * 每次action执行完，要显式的调用一下next
     * 执行下一个任务
     */
    void next();

    /**
     * 优先级,值越小，优先级越高
     */
    int priority();

    /**
     * 需要等待异步回调结果到来时，再执行
     * @return
     */
    boolean isReady();

    /**
     * 标记唯一
     * @return
     */
    String tag();
}
