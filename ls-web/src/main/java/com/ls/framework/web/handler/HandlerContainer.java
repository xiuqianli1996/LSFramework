package com.ls.framework.web.handler;

import com.ls.framework.web.exception.DuplicateMappingException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HandlerContainer {

    private static final List<ActionHandler> actionHandlerList = new LinkedList<>();

    public static void add(ActionHandler element) {
        ActionHandler existHandler = get(element);
        if (existHandler != null) {
            throw new DuplicateMappingException(element, existHandler);
        }
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

    public static ActionHandler get(ActionHandler actionHandler) {
        for (ActionHandler a2 : actionHandlerList) {
            if (a2.equals(actionHandler)) {
                return a2;
            }
        }
        return null;
    }
}
