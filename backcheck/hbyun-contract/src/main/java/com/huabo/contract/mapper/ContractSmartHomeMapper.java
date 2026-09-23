package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import com.huabo.contract.mappersql.ContractSmartHomeSqlProvider;

import java.util.List;
import java.util.Map;

/**
 * 智慧合同首页Mapper
 *
 * @author 华博云开发团队
 * @since 2025-06-01
 */
public interface ContractSmartHomeMapper {

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getYearMoneyInt")
    Integer getYearMoneyInt(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getContractCountInt")
    Integer getContractCountInt(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getBreachCountInt")
    Integer getBreachCountInt(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getPendingCount")
    Integer getPendingCount(String orgid, Integer year);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getExecutingCount")
    Integer getExecutingCount(String orgid, Integer year);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getReceivablesInt")
    Integer getReceivablesInt(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getActualCollectionInt")
    Integer getActualCollectionInt(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getContractTypeStats")
    List<Map<String, Object>> getContractTypeStats(String orgid, Integer year, Integer month);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getRiskWarningList")
    List<Map<String, Object>> getRiskWarningList(String orgid, Integer pageSize);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getExpiringList")
    List<Map<String, Object>> getExpiringList(String orgid, Integer days);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getCounterpartRiskList")
    List<Map<String, Object>> getCounterpartRiskList(String orgid);

    @SelectProvider(type = ContractSmartHomeSqlProvider.class, method = "getKeyContractList")
    List<Map<String, Object>> getKeyContractList(String orgid, Integer pageSize);
}
