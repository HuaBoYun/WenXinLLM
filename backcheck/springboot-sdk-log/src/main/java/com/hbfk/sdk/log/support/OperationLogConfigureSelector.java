package com.hbfk.sdk.log.support;

import com.hbfk.sdk.log.annotation.EnableOperationLog;
import com.hbfk.sdk.log.config.OperationLogConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.lang.Nullable;

/**
* @Description: Enable 查找器
* @Author: 61
*/
public class OperationLogConfigureSelector extends AdviceModeImportSelector<EnableOperationLog> {

    @Override
    @Nullable
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), OperationLogConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{OperationLogConfiguration.class.toString()};
            default:
                return null;
        }
    }
}
