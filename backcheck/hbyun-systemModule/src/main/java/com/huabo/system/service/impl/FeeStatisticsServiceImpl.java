package com.huabo.system.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.dto.FeeDrilldownQueryDTO;
import com.huabo.system.dto.FeeStatisticsQueryDTO;
import com.huabo.system.mapper.TblFeeRecordMapper;
import com.huabo.system.service.FeeStatisticsService;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.vo.FeeStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class FeeStatisticsServiceImpl implements FeeStatisticsService {

    @Resource
    private TblFeeRecordMapper feeRecordMapper;
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean queryStatistics(String token, FeeStatisticsQueryDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        List<Map<String, Object>> statsList;
        String dimension = dto.getDimension() != null ? dto.getDimension() : "personal";

        switch (dimension) {
            case "company":
                BigDecimal companyOrgId = loginStaff.getLinkOrg() != null ? loginStaff.getLinkOrg().getOrgid() : null;
                if (companyOrgId == null) {
                    return ResponseFormat.retParam(0, 10001, null);
                }
                statsList = feeRecordMapper.statsByModuleForCompany(companyOrgId, dto.getStartTime(), dto.getEndTime());
                break;
            case "group":
                BigDecimal groupOrgId = loginStaff.getGroupOrg() != null ? loginStaff.getGroupOrg().getOrgid() : null;
                if (groupOrgId == null) {
                    return ResponseFormat.retParam(0, 10001, null);
                }
                statsList = feeRecordMapper.statsByModuleForGroup(groupOrgId, dto.getStartTime(), dto.getEndTime());
                break;
            default: // personal
                statsList = feeRecordMapper.statsByModuleForPerson(loginStaff.getStaffid().toString(), dto.getStartTime(), dto.getEndTime());
                break;
        }

        // Build result
        FeeStatisticsVO result = new FeeStatisticsVO();
        BigDecimal totalFee = BigDecimal.ZERO;
        long totalCallCount = 0;
        List<FeeStatisticsVO.ModuleStatItem> moduleStats = new ArrayList<>();

        for (Map<String, Object> row : statsList) {
            FeeStatisticsVO.ModuleStatItem item = new FeeStatisticsVO.ModuleStatItem();
            item.setModuleName(row.get("MODULE_NAME") != null ? row.get("MODULE_NAME").toString() : "未知模块");
            BigDecimal fee = row.get("TOTAL_FEE") != null ? new BigDecimal(row.get("TOTAL_FEE").toString()) : BigDecimal.ZERO;
            long count = row.get("CALL_COUNT") != null ? Long.parseLong(row.get("CALL_COUNT").toString()) : 0;
            item.setTotalFee(fee);
            item.setCallCount(count);
            moduleStats.add(item);
            totalFee = totalFee.add(fee);
            totalCallCount += count;
        }

        result.setTotalFee(totalFee);
        result.setTotalCallCount(totalCallCount);
        result.setModuleStats(moduleStats);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statistics", result);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean drilldown(String token, FeeDrilldownQueryDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        BigDecimal companyOrgId = null;
        BigDecimal groupOrgId = null;
        String staffId = null;
        String dimension = dto.getDimension() != null ? dto.getDimension() : "personal";

        switch (dimension) {
            case "company":
                companyOrgId = loginStaff.getLinkOrg() != null ? loginStaff.getLinkOrg().getOrgid() : null;
                break;
            case "group":
                groupOrgId = loginStaff.getGroupOrg() != null ? loginStaff.getGroupOrg().getOrgid() : null;
                break;
            default:
                staffId = loginStaff.getStaffid().toString();
                break;
        }

        List<Map<String, Object>> drilldownList = feeRecordMapper.drilldownByModule(
                dto.getModuleType(), companyOrgId, groupOrgId, staffId,
                dto.getStartTime(), dto.getEndTime());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", drilldownList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
}
