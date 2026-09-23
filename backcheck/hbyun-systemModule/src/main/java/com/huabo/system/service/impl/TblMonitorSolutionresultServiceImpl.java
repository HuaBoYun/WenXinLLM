package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorSolutionresult;
import com.huabo.system.mapper.TblMonitorSolutionresultMapper;
import com.huabo.system.service.TblMonitorSolutionresultService;

@Service
public class TblMonitorSolutionresultServiceImpl implements TblMonitorSolutionresultService {

    @Resource
    private TblMonitorSolutionresultMapper tblMonitorSolutionresultMapper;

    @Override
    public List<String> find(String table, BigDecimal SoultionId, String ruleid, String zt) {
    	return tblMonitorSolutionresultMapper.findRuleidAndAcctid(SoultionId, ruleid);
    }

    @Override
    public Map<String, Object> findBySoultionIdZKZX(String table, BigDecimal SoultionId, String rulid, String zt, PageInfo<TblMonitorSolutionresult> pageInfo) {
//        String sql = "select *  from \"" + zt + "\"." + table + " where  EXECTIME = (SELECT SIGNID FROM TBL_MONITOR_PREWARNING WHERE SOLUTIONRESULTID = ( select SOLUTIONRESULTID from ( SELECT SOLUTIONRESULTID FROM TBL_MONITOR_SOLUTIONRESULT  WHERE SOURCE = 3 AND SOLUTIONID = " + SoultionId + " order by savetime desc) where rownum=1) and RULEID=" + rulid + ") ";
//        String sqlCount = "select count(*)  from \"" + zt + "\"." + table + " where  EXECTIME = (SELECT SIGNID FROM TBL_MONITOR_PREWARNING WHERE SOLUTIONRESULTID = ( select SOLUTIONRESULTID from ( SELECT SOLUTIONRESULTID  FROM TBL_MONITOR_SOLUTIONRESULT WHERE SOURCE = 3 AND SOLUTIONID = " + SoultionId + " order by savetime desc) where rownum=1) and RULEID=" + rulid + ")";

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        
        Page<TblMonitorSolutionresult> page = new Page<TblMonitorSolutionresult>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblMonitorSolutionresult> pageList = tblMonitorSolutionresultMapper.findBySoultionIdZKZX(zt, table, SoultionId, rulid, page);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findBySoultionIdZK(String table, BigDecimal SoultionId, String rulid, String zt, PageInfo<TblMonitorSolutionresult> pageIn) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Page<TblMonitorSolutionresult> page = new Page<TblMonitorSolutionresult>(pageIn.getCurrentPage(),pageIn.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblMonitorSolutionresult> pageList = tblMonitorSolutionresultMapper.findBySoultionIdZKZX(zt, table, SoultionId, rulid, page);
        pageIn.setTlist(pageList.getRecords());
        pageIn.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageIn);
        return resultMap;
    }

}
