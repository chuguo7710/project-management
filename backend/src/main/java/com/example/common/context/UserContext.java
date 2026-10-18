package com.example.common.context;

import com.example.modules.entity.ContextData;

public class UserContext {
    private static final ThreadLocal<ContextData> USER_ROLE = new ThreadLocal<>();
    public static void set(ContextData contextData) {
        USER_ROLE.set(contextData);
    }
    public static ContextData get() {
        return USER_ROLE.get();
    }
    public static void remove() {
        USER_ROLE.remove();
    }
}
