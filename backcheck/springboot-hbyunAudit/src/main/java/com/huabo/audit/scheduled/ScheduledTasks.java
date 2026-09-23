package com.huabo.audit.scheduled;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.oracle.entity.TblSystemDistribution;
import com.huabo.audit.oracle.mapper.TblRectificationIssuesMapper;
import com.huabo.audit.oracle.mapper.TblSystemDistributionMapper;
import com.huabo.audit.oracle.mapper.TblZgzzIssuesilistMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationplanMapper;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;

import redis.clients.jedis.Jedis;

/**
 * 定时任务类
 * 用于执行系统定时任务
 *
 * @author Augment Code AI
 * @date 2025-01-21
 */
@Slf4j
@Component
public class ScheduledTasks {

	@Resource
	private TblZgzzRectificationplanMapper tblZgzzRectificationplanMapper;
	
	
	@Resource
    private TblSystemDistributionMapper tblSystemDistributionMapper;
	
	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	
	/**
	 * 整改跟踪提醒定时任务
	 * 每天凌晨1点执行
	 */
	@Scheduled(cron = "0 0 1 * * ?") //每天00点执行代码 ；
	//@Scheduled(fixedRate = 600000)
    public void setRectificationTrackingResponer() {
    	log.info("整改跟踪提醒定时任务开始执行");
    	Jedis jedis = null;
    	try {
    		//1.获取所有整改跟踪的整改方案，按照整改责任人和截止时间 排序，
			TblZgzzRectificationplanVo plan = new TblZgzzRectificationplanVo();
			List<TblZgzzRectificationplanVo>  voList = this.tblZgzzRectificationplanMapper.selectPageInfoList(plan,new StringBuffer(" AND TZR.STATUS IN (7,8,9) ").toString(),null);
			log.debug("查询到整改方案数量: {}", voList != null ? voList.size() : 0);
			
			//2.遍历结合处理业务逻辑整改截止日期最后7天要给整改责任人提示消息！过期未进行整改，则变更状态为：到期未整改
			String tipStr = null;
			BigDecimal preResponse = null;
			long diff = 0;
			long diffDays = 0;
			Date newDate = new Date();
			
			TblSystemDistribution dis = null;
			Map<String,List<TblSystemDistribution>> staffMap = new HashMap<String, List<TblSystemDistribution>>(0);
			List<TblSystemDistribution> list = null;
			boolean flag = false;
			for (TblZgzzRectificationplanVo vo : voList) { 
				
				
				if(preResponse == null || vo.getResponse().compareTo(preResponse) != 0) {
					if(preResponse != null && vo.getResponse().compareTo(preResponse) != 0) {
						flag = true;
					}else {
						flag = false;
					}
					preResponse = vo.getResponse();
					list = new ArrayList<TblSystemDistribution>(0);
				}
				diff = vo.getDeadlineTime().getTime() - newDate.getTime();
				
				diffDays = diff/(24*60*60*1000);
				//大于7天不予提示
				if(diffDays > 7) {
					continue;
				}
				
				TblRectificationIssuesVo issues = new TblRectificationIssuesVo();
				issues.setPlanId(vo.getPlanId());
				List<TblRectificationIssuesVo> issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
				if(issuesList!=null && issuesList.size()>0) {
					for (TblRectificationIssuesVo islist : issuesList) {
						dis = new TblSystemDistribution();
						
						dis.setDistributionId(RandomUtil.uuStringId());
						dis.setReciver(islist.getImplementer());
						dis.setCreateTime(new Date());
						dis.setFormId(islist.getRelaId());
						dis.setModuleType("znsj");
						dis.setPageUrl(TblSystemDistribution.ZGTZWTZGURL);
						dis.setDistributionType(TblSystemDistribution.ZGTZWTZGTYPE);
						dis.setCreateStaff(islist.getImplementer());
						
						if(diff < 0) {
						    tipStr = "到期未整改，"+vo.getPlanName()+"整改通知截止至"+ DateUtil.parseDate(vo.getDeadlineTime(), DateUtil.DATE_SMALL_STR) +"未整改 ！";
							//到期未整改 
							//this.tblZgzzRectificationplanMapper.updateStatusById(vo.getPlanId(),12);
						    //this.tblZgzzIssuesilistMapper.updateStatusByClosePlan(new BigDecimal(vo.getPlanId()),0);
						}else {
							 tipStr = vo.getPlanName()+"整改通知，截止日期至"+DateUtil.parseDate(vo.getDeadlineTime(), DateUtil.DATE_SMALL_STR)+"剩余"+((int)diffDays)+"天，即将到期 ！";
						}
						dis.setDistributionTitle(tipStr);
						this.tblSystemDistributionMapper.insertSelective(dis);
						list.add(dis);
					}
					
				}
				
				
				dis = new TblSystemDistribution();
				
				dis.setDistributionId(RandomUtil.uuStringId());
				dis.setReciver(vo.getResponse());
				dis.setCreateTime(new Date());
				dis.setFormId(vo.getPlanId());
				dis.setModuleType("znsj");
				dis.setPageUrl(TblSystemDistribution.ZGFAURL);
				dis.setDistributionType(TblSystemDistribution.ZGFATYPE);
				dis.setCreateStaff(vo.getResponse());
				
				if(diff < 0) {
				    tipStr = "到期未整改，"+vo.getPlanName()+"整改通知截止至"+ DateUtil.parseDate(vo.getDeadlineTime(), DateUtil.DATE_SMALL_STR) +"未整改 ！";
					//到期未整改 
					//this.tblZgzzRectificationplanMapper.updateStatusById(vo.getPlanId(),12);
				    //this.tblZgzzIssuesilistMapper.updateStatusByClosePlan(new BigDecimal(vo.getPlanId()),0);
				}else {
					 tipStr = vo.getPlanName()+"整改通知，截止日期至"+DateUtil.parseDate(vo.getDeadlineTime(), DateUtil.DATE_SMALL_STR)+"剩余"+((int)diffDays)+"天，即将到期 ！";
				}
				dis.setDistributionTitle(tipStr);
				this.tblSystemDistributionMapper.insertSelective(dis);
				list.add(dis);
				
				//将提示信息放入map
				if(flag) {
					staffMap.put(vo.getResponse().toString(), list);
				}
			}
			staffMap.put(preResponse.toString(), list);
			
			jedis = JedisUtil.getJedis();
			for (Map.Entry<String, List<TblSystemDistribution>> entry : staffMap.entrySet()) {
				jedis.set(entry.getKey() + JedisUtil.TIPRECTIFICATIONPLAN,JSONObject.toJSONString(entry.getValue()));
			}
			System.out.println("定时提醒整改通知成功");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JedisUtil.returnResource(jedis);
		}
    }
}
