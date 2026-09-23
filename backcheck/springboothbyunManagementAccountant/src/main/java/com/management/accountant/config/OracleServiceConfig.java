package com.management.accountant.config;

import com.management.accountant.oracle.service.*;
import com.management.accountant.oracle.service.advanced.*;
import com.management.accountant.oracle.service.budget.*;
import com.management.accountant.oracle.service.integration.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Oracle Service配置类
 *
 * @description 为oracle包下的Service接口生成空实现
 * @author AI Assistant
 * @date 2026-02-04
 */
@Slf4j
@Configuration
public class OracleServiceConfig {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OracleServiceConfig.class);

    /**
     * 创建Service代理对象
     */
    @SuppressWarnings("unchecked")
    private <T> T createServiceProxy(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new ServiceInvocationHandler(serviceInterface.getSimpleName())
        );
    }

    /**
     * Service方法调用处理器
     */
    private static class ServiceInvocationHandler implements InvocationHandler {
        private final String serviceName;

        public ServiceInvocationHandler(String serviceName) {
            this.serviceName = serviceName;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();

            log.debug("调用空Service实现: {}.{}({})", serviceName, methodName, args);

            if (returnType == boolean.class || returnType == Boolean.class) {
                return true;
            } else if (returnType == int.class || returnType == Integer.class) {
                return 0;
            } else if (returnType == long.class || returnType == Long.class) {
                return 0L;
            } else if (returnType == String.class) {
                return "";
            } else if (returnType == Map.class) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "操作成功");
                result.put("data", null);
                return result;
            } else if (returnType == List.class) {
                return new ArrayList<>();
            }

            return null;
        }
    }

    // ========== Budget Package Services ==========
    // 注意：以下 Service 已有真实 ServiceImpl，不再需要代理 Bean：
    // BudgetAccountService, BudgetAllocationService, BudgetApprovalService,
    // BudgetConsolidationService, BudgetControlRuleService, BudgetDataService,
    // BudgetDimensionService, BudgetFormulaService, BudgetParameterService,
    // BudgetPeriodService, BudgetRuleService, BudgetScenarioService,
    // BudgetWorkflowService, BudgetTaskService, BudgetTemplateService,
    // BudgetVersionService, BudgetWarningRuleService, BudgetMonitorService,
    // BudgetForecastAnalysisService, BudgetComparisonAnalysisService,
    // BudgetAnalysisReportService, BudgetPerformanceAnalysisService,
    // BudgetTrendAnalysisService, BudgetVarianceAnalysisService,
    // BudgetScenarioAnalysisService, BudgetSensitivityAnalysisService,
    // BudgetSimulationService, BudgetExecutionService, BudgetFreezeService,
    // BudgetIndicatorService, BudgetLimitService, BudgetModelService,
    // BudgetQuotaService, BudgetReleaseService, BudgetReminderService,
    // BudgetReserveService, BudgetRollingService, BudgetTransferService

    @Bean
    public BudgetAdvancedService advancedServiceOracle() {
        return createServiceProxy(BudgetAdvancedService.class);
    }

    @Bean
    public BudgetAnalysisService analysisServiceOracle() {
        return createServiceProxy(BudgetAnalysisService.class);
    }

    @Bean
    public BudgetApprovalFlowService BudgetApprovalFlowServiceOracle() {
        return createServiceProxy(BudgetApprovalFlowService.class);
    }

    @Bean
    public BudgetApprovalHistoryService BudgetApprovalHistoryServiceOracle() {
        return createServiceProxy(BudgetApprovalHistoryService.class);
    }

    @Bean
    public BudgetCalculationService calculationServiceOracle() {
        return createServiceProxy(BudgetCalculationService.class);
    }

    @Bean
    public BudgetComparisonService comparisonServiceOracle() {
        return createServiceProxy(BudgetComparisonService.class);
    }

    @Bean
    public BudgetControlService controlServiceOracle() {
        return createServiceProxy(BudgetControlService.class);
    }

    @Bean
    public BudgetDimensionValueService BudgetDimensionValueServiceOracle() {
        return createServiceProxy(BudgetDimensionValueService.class);
    }

    @Bean
    public BudgetDrillThroughService drillthroughServiceOracle() {
        return createServiceProxy(BudgetDrillThroughService.class);
    }

    @Bean
    public BudgetIntegrationService integrationServiceOracle() {
        return createServiceProxy(BudgetIntegrationService.class);
    }

    @Bean
    public BudgetOrganizationService organizationServiceOracle() {
        return createServiceProxy(BudgetOrganizationService.class);
    }

    @Bean
    public BudgetOrganizationStructureService organizationstructureServiceOracle() {
        return createServiceProxy(BudgetOrganizationStructureService.class);
    }

    @Bean
    public BudgetPreparationService preparationServiceOracle() {
        return createServiceProxy(BudgetPreparationService.class);
    }

    @Bean
    public BudgetRollingForecastService BudgetRollingForecastServiceOracle() {
        return createServiceProxy(BudgetRollingForecastService.class);
    }

    @Bean
    public BudgetSystemService systemServiceOracle() {
        return createServiceProxy(BudgetSystemService.class);
    }

    @Bean
    public BudgetTrendService trendServiceOracle() {
        return createServiceProxy(BudgetTrendService.class);
    }

    @Bean
    public BudgetVarianceService varianceServiceOracle() {
        return createServiceProxy(BudgetVarianceService.class);
    }

    // ========== Advanced Package Services ==========

    @Bean
    public BatchCalculationService BatchCalculationServiceOracle() {
        return createServiceProxy(BatchCalculationService.class);
    }

    @Bean
    public CurrencyService CurrencyServiceOracle() {
        return createServiceProxy(CurrencyService.class);
    }

    @Bean
    public DrillThroughQueryService DrillThroughQueryServiceOracle() {
        return createServiceProxy(DrillThroughQueryService.class);
    }

    @Bean
    public FormulaTraceService FormulaTraceServiceOracle() {
        return createServiceProxy(FormulaTraceService.class);
    }

    @Bean
    public ReminderStrategyService ReminderStrategyServiceOracle() {
        return createServiceProxy(ReminderStrategyService.class);
    }

    @Bean
    public RollingBudgetPlanService RollingBudgetPlanServiceOracle() {
        return createServiceProxy(RollingBudgetPlanService.class);
    }

    // ========== Integration Package Services ==========
    // 注意：以下 Integration Service 已有真实 ServiceImpl，不再需要代理 Bean：
    // BudgetApiIntegrationService, BudgetBiIntegrationService,
    // BudgetCloudIntegrationService, BudgetDatabaseIntegrationService,
    // BudgetDataMappingService, BudgetErpIntegrationService,
    // BudgetFileIntegrationService, BudgetIntegrationConfigService,
    // BudgetIntegrationMonitorService, BudgetMessageQueueIntegrationService,
    // BudgetWebServiceIntegrationService

    @Bean
    public BudgetDataStreamIntegrationService BudgetDataStreamIntegrationServiceOracle() {
        return createServiceProxy(BudgetDataStreamIntegrationService.class);
    }

    // ========== Root Package Services ==========

    @Bean
    public TblCeaConferenceMgtOracleService TblCeaConferenceMgtOracleServiceOracle() {
        return createServiceProxy(TblCeaConferenceMgtOracleService.class);
    }

    @Bean
    public TblAttachmentService TblAttachmentServiceOracle() {
        return createServiceProxy(TblAttachmentService.class);
    }

    @Bean
    public TblStaffOracleService TblStaffOracleServiceOracle() {
        return createServiceProxy(TblStaffOracleService.class);
    }
}
