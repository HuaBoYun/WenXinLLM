package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-26
 */
public interface IMonitorSolutionRuleService extends IService<MonitorSolutionRule> {


      /*
         查询 solutionid 对应的 Rule
      */

      public List<MonitorRule> queryRuleBySoluid(BigDecimal solutionid);
      
      public JsonBean getList(String token,Integer pageNumber,Integer pageSize,String sql) throws Exception;

}
