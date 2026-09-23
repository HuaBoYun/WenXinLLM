package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.huabo.cybermonitor.mapper.MonitorSolutionRuleMapper;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-26
 */
@Service
public class MonitorSolutionRuleServiceImpl extends ServiceImpl<MonitorSolutionRuleMapper, MonitorSolutionRule> implements IMonitorSolutionRuleService {

    @Resource
    MonitorSolutionRuleMapper  monitorSolutionRuleMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public List<MonitorRule> queryRuleBySoluid(BigDecimal solutionid) {
        return monitorSolutionRuleMapper.queryRuleBySoluid(solutionid);
    }
    @Override
	public JsonBean getList(String token, Integer pageNumber, Integer pageSize,String sql) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		
		pageInfo.setTlist(monitorSolutionRuleMapper.selectListPageInfo(pageInfo, sql));
		pageInfo.setTotalRecord(monitorSolutionRuleMapper.selectListPageInfocount(sql));
		pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
}
