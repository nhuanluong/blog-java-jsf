package com.coursevm.core.base.context;

public class TenantContextThreadLocal {

    private static final ThreadLocal<TenantContext> multiTenantCtxThreadLocal = new ThreadLocal<>();

    public static void init() {
        set(new TenantContext());
    }

    public static void set(TenantContext context) {
        multiTenantCtxThreadLocal.set(context);
    }

    public static void unset() {
        multiTenantCtxThreadLocal.remove();
    }

    public static TenantContext get() {
        return multiTenantCtxThreadLocal.get();
    }
}
