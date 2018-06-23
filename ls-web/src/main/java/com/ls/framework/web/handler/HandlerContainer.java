package com.ls.framework.web.handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerContainer {

    private static final List<ActionHandler> actionHandlerList = new ArrayList<>();

    public static void add(ActionHandler element) {
        actionHandlerList.add(element);
    }

    public static ActionHandler get(String url) {
        for (ActionHandler actionHandler : actionHandlerList) {
            if (actionHandler.match(url)) {
                return actionHandler;
            }
        }
        return null;
    }
}
