package com.hugo.study_dialog_demo.task;

import java.util.concurrent.ConcurrentHashMap;

public class ActionManager {
    public static ActionManager actionManager;
    private ConcurrentHashMap<String, ActionChain> chainMap = new ConcurrentHashMap<>();

    private ActionManager() {

    }

    public static ActionManager getInstance() {
        if (actionManager == null) {
            synchronized (ActionManager.class) {
                if (actionManager == null) {
                    actionManager = new ActionManager();
                }
            }
        }
        return actionManager;
    }

    public ActionChain create(String tag) {
        ActionChain actionChain = new ActionChain();
        actionChain.setTag(tag);
        chainMap.put(tag, actionChain);
        return actionChain;
    }

    public void notifyAction(String chainTag) {
        ActionChain actionChain = chainMap.get(chainTag);
        if (actionChain != null) {
            actionChain.notifyAction();
        }
    }
}
