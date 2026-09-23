package com.financial.sharing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 数据库配置类
 * 项目统一使用Oracle/达梦数据库
 */
@Slf4j
@Component
public class DateBaseConfig {

	// 数据库类型 Oracle MySql SqlServer
	public final static String DATABASETYPE = "Oracle";

	@Value("${database.type:Oracle}")
	private String databaseType;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 初始化验证
	 */
	@PostConstruct
	public void init() {
		try {
			log.info("DateBaseConfig初始化开始，数据库类型: {}", databaseType);

			// 验证关键Mapper Bean是否存在
			getOracleAccountSubjectMapper();
			getOracleAuxiliaryItemMapper();

			log.info("DateBaseConfig初始化成功，数据库类型: {}", databaseType);
		} catch (Exception e) {
			log.error("DateBaseConfig初始化失败", e);
			throw new RuntimeException("数据库配置初始化失败: " + e.getMessage(), e);
		}
	}

	/**
	 * 判断是否为Oracle/达梦数据库
	 * @return true-Oracle/达梦, false-其他
	 */
	public boolean isOracle() {
		return "Oracle".equalsIgnoreCase(databaseType) || "Oracle".equals(DATABASETYPE);
	}

	/**
	 * 获取Oracle/达梦版本的InfluenceFactorMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InfluenceFactorMapper getOracleInfluenceFactorMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InfluenceFactorMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的AccountSubjectMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.AccountSubjectMapper getOracleAccountSubjectMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.AccountSubjectMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的AuxiliaryItemMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.AuxiliaryItemMapper getOracleAuxiliaryItemMapper() {
		try {
			return applicationContext.getBean(com.financial.sharing.oracle.mapper.AuxiliaryItemMapper.class);
		} catch (Exception e) {
			log.error("获取Oracle AuxiliaryItemMapper失败", e);
			throw new RuntimeException("数据库配置异常：无法获取Oracle AuxiliaryItemMapper", e);
		}
	}

	/**
	 * 获取Oracle/达梦版本的CurrencyRateMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.CurrencyRateMapper getOracleCurrencyRateMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.CurrencyRateMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的BusinessTransactionMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.BusinessTransactionMapper getOracleBusinessTransactionMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.BusinessTransactionMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的TransactionEntryMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.TransactionEntryMapper getOracleTransactionEntryMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.TransactionEntryMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的AccountingRuleMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.AccountingRuleMapper getOracleAccountingRuleMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.AccountingRuleMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的RuleVoucherMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.RuleVoucherMapper getOracleRuleVoucherMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.RuleVoucherMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的AccountingVoucherMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.AccountingVoucherMapper getOracleAccountingVoucherMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.AccountingVoucherMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的VoucherEntryMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.VoucherEntryMapper getOracleVoucherEntryMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.VoucherEntryMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的RevenueContractMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.RevenueContractMapper getOracleRevenueContractMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.RevenueContractMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的RevenueAllocationMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.RevenueAllocationMapper getOracleRevenueAllocationMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.RevenueAllocationMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InternalSettlementMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InternalSettlementMapper getInternalSettlementMapperOracle() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InternalSettlementMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryValuationMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryValuationMapper getOracleInventoryValuationMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryValuationMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryCategoryMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryCategoryMapper getOracleInventoryCategoryMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryCategoryMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryMasterMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryMasterMapper getOracleInventoryMasterMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryMasterMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryCheckMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryCheckMapper getOracleInventoryCheckMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryCheckMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryCheckResultMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryCheckResultMapper getOracleInventoryCheckResultMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryCheckResultMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryAlertRecordMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryAlertRecordMapper getOracleInventoryAlertRecordMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryAlertRecordMapper.class);
	}

	/**
	 * 获取Oracle/达梦版本的InventoryAlertRuleMapper
	 * @return Oracle/达梦版本的Mapper
	 */
	public com.financial.sharing.oracle.mapper.InventoryAlertRuleMapper getOracleInventoryAlertRuleMapper() {
		return applicationContext.getBean(com.financial.sharing.oracle.mapper.InventoryAlertRuleMapper.class);
	}
}

