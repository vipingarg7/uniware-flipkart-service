package com.uniware.integrations.client.utils;

import com.unifier.core.utils.StringUtils;
import com.uniware.integrations.client.context.FlipkartRequestContext;
import com.unicommerce.platform.web.context.TenantRequestContext;

/**
 * Created by vipin on 20/05/22.
 */

public class ThreadContextUtils {

    public static void setThreadMetadata(String threadNamePrefix) {

        StringBuilder threadNameBuilder = new StringBuilder(threadNamePrefix).append("-").append(Thread.currentThread().getId());

        if (TenantRequestContext.current().getSourceCode() != null) {
            threadNameBuilder.append(':').append(TenantRequestContext.current().getSourceCode());
        }
        if (StringUtils.isNotBlank(TenantRequestContext.current().getTenantCode())) {
            threadNameBuilder.append(':').append(TenantRequestContext.current().getTenantCode());
        }
        if (StringUtils.isNotBlank(FlipkartRequestContext.current().getRequestURI())) {
            threadNameBuilder.append(':').append(FlipkartRequestContext.current().getRequestURI());
        }
        Thread.currentThread().setName(threadNameBuilder.toString());
    }

}
