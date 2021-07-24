package com.coursevm.core.base.context;

public class CurrentTenant {

    public static Long getTenantId() {

        TenantContext tenantContext = TenantContextThreadLocal.get();

        if (tenantContext == null) return 0L;

        return tenantContext.getTenantId();
    }

    public static Long getTenantOrgId() {

        TenantContext tenantContext = TenantContextThreadLocal.get();

        if (tenantContext == null) return 0L;

        return tenantContext.getTenantOrgId();
    }

    public static Long getUserMasterId() {

        TenantContext tenantContext = TenantContextThreadLocal.get();

        if (tenantContext == null) return 0L;

        return tenantContext.getUserMasterId();
    }
}
