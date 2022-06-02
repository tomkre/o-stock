package com.optimagrowth.organization.utils;

import org.springframework.util.Assert;

public class UserContextHolder {

    private static final ThreadLocal<UserContext> userContextContainer = new ThreadLocal<>();

    public static final UserContext getContext() {
        UserContext context = userContextContainer.get();
        if (context == null) {
            context = createEmptyContext();
            userContextContainer.set(context);
        }
        return userContextContainer.get();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContextContainer.set(context);
    }

    public static final UserContext createEmptyContext() {
        return new UserContext();
    }

}
