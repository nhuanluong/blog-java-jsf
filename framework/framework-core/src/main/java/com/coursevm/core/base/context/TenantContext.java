package com.coursevm.core.base.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TenantContext {

    public final static String TENANT_ID_KEY = "X-TenantId";
    public final static String ORGANIZATION_ID_KEY = "X-TenantOrgId";
    public final static String USER_ID_KEY = "X-UserId";
    public final static String USER_MASTER_ID = "X-UserMasterId";

    private Long tenantId;
    private Long tenantOrgId;
    private Long userPortalId;
    private Long userMasterId;
    private String tenantDatabase;
    private Long orgSchedule;

    public TenantContext(Long tenantId, Long tenantOrgId, Long userPortalId, Long userMasterId) {
        this.tenantId = tenantId;
        this.tenantOrgId = tenantOrgId != null ? tenantOrgId : 1 ;
        this.userPortalId = userPortalId;
        this.userMasterId = userMasterId;
    }

    public TenantContext(Long tenantId, Long orgId) {
        this.tenantId = tenantId;
        this.tenantOrgId = orgId;
    }
}
