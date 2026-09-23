package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;
import com.huabo.fxgl.entity.TblRiskMonthlyEvaluationEntity;
import com.huabo.fxgl.mapper.RiskMapper;
import com.huabo.fxgl.mapper.TblRiskImprovementDetailsMapper;
import com.huabo.fxgl.mapper.TblRiskImprovementMapper;
import com.huabo.fxgl.service.TblRiskImprovementDetailsService;
import com.huabo.fxgl.util.PageResult;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TblRiskImprovementDetailsServiceImpl extends ServiceImpl<TblRiskImprovementDetailsMapper, TblRiskImprovementDetailsEntiry> implements TblRiskImprovementDetailsService {

    @Resource
    private TblRiskImprovementDetailsMapper tblRiskImprovementDetailsMapper;

    @Resource
    private TblRiskImprovementMapper tblRiskImprovementMapper;
    
    @Resource
    private UserProvider userProvider;
    
    
    
    @Resource
    private RiskMapper riskMapper;
    /**
     * 风险事件上报后在 风险监督改进详情中增加汇总数据;
     * @param riskImprovementDetailsEntiry
     */

    @Override
    public void insertTblRiskImprovementDetails(TblRiskImprovementDetailsEntiry riskImprovementDetailsEntiry) {
        try {
        	
            tblRiskImprovementDetailsMapper.insert(riskImprovementDetailsEntiry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 风险监督改进列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean riskImprovementDetailsList(String token, Integer pageNumber, Integer pageSize, BigDecimal riskImplementID,String orgname) {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            //使用pagehelper自动分页。
            com.github.pagehelper.PageInfo<TblRiskImprovementDetailsEntiry> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() ->  tblRiskImprovementDetailsMapper.selectList(riskImplementID,orgname));

            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
            PageResult<TblRiskImprovementDetailsEntiry> build = new PageResult<TblRiskImprovementDetailsEntiry>().build(pageInfo);

            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    
    }
    
//    @Override
//    public JsonBean riskImprovementDetailsList(String token, BigDecimal riskImplementID) {
//        Map<String, Object> hashMap = null;
//        try {
//            hashMap = new HashMap<>();
//            //得到了当前登录的用户信息
//            TblStaffUtil staffUtil = userProvider.get();
//            if (staffUtil == null){
//                return  ResponseFormat.retParam(0, 20006, hashMap);
//            }
//            List<TblRiskImprovementDetailsEntiry>  list= tblRiskImprovementDetailsMapper.selectList(riskImplementID);
//            hashMap.put("pageInfo",list);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseFormat.retParam(1, 200, hashMap);
//    
//    }

    /**
     * 每个月月初，判断下发的单位上月个是否以上报，如上报...
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public void isReport() {
        log.info("定时任务开始！开始时间"+new Date() +"");
        try {
            //获取上月
            LocalDate currentDate = LocalDate.now();
            LocalDate lastMonth = currentDate.minusMonths(1);
            int year = lastMonth.getYear();
            int month = lastMonth.getMonth().getValue();
            //首选查询所有风险监督改进列表，遍历后根据下发表单从风险监督改进详情里查询所有详情列，判断是否有上个月，没有的话视为未上报；
            QueryWrapper<TblRiskImprovementEntiry> queryWrapper = new QueryWrapper<TblRiskImprovementEntiry>();
            final List<TblRiskImprovementEntiry> improvementEntiries = tblRiskImprovementMapper.selectList(queryWrapper);
            for (TblRiskImprovementEntiry entiry:improvementEntiries) {
                final List<TblRiskImprovementDetailsEntiry> entiries = tblRiskImprovementDetailsMapper.selectListImprovement(entiry.getRiskImplementID());
                final List<TblRiskImprovementDetailsEntiry> improvementDetailsEntiries = entiries.stream().filter(o -> o.getYears().equals(new BigDecimal(year))).
                        filter(o -> o.getMonth().equals(new BigDecimal(month))).collect(Collectors.toList());
                if (improvementDetailsEntiries.size() > 0){
                    log.info("上月已上报！");
                }else {
                    log.info("上月未上报！");
                    final TblRiskImprovementDetailsEntiry detailsEntiry = new TblRiskImprovementDetailsEntiry();
                    detailsEntiry.setIsReport("未上报");
                    detailsEntiry.setMonth(new BigDecimal(month));
                    detailsEntiry.setScoreDetails("-0.1");
                    detailsEntiry.setImprovementId(entiry.getRiskImplementID());
                    detailsEntiry.setYears(new BigDecimal(year));
                    detailsEntiry.setBranchName(entiry.getBranchName());
                    detailsEntiry.setBranchId(entiry.getBranchId());
                    detailsEntiry.setId(RandomUtil.uuBigDecimalId());
                    detailsEntiry.setCreatTime(new Date());
                    tblRiskImprovementDetailsMapper.insert(detailsEntiry);
                }
            }
        } catch (Exception e) {
            log.info("定时任务错误信息！错误时间"+new Date() +",错误内容："+e.getLocalizedMessage()+"");
            throw new RuntimeException(e);
        }
        log.info("定时任务结束！结束时间"+new Date() +"");
    }
    
    

 
	@Override
	public TblRiskImprovementDetailsEntiry riskImprovementDetails(String token, BigDecimal id) {
		// TODO Auto-generated method stub
		return  tblRiskImprovementDetailsMapper.selectTblRiskImprovementDetails(id);
	}
	@Override
	public List<TblRiskImprovementDetailsEntiry> exportRiskReport(String id, BigDecimal impId,String orgName) {
		// TODO Auto-generated method stub
		List<TblRiskImprovementDetailsEntiry>  list=null;    
		try {
			  String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				  id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
	              list= tblRiskImprovementDetailsMapper.exportRiskReport(ids,impId,orgName);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		return list;
	}
	
	
//	@Override
//	public JsonBean updateScore(String token, String id, Integer score) {
//		// TODO Auto-generated method stub
//		Map<String, Object> result=null;
//		try {
//			result=new HashMap<String, Object>();
//        	TblRiskImprovementDetailsEntiry e=tblRiskImprovementDetailsMapper.getScore(new BigDecimal(id));
//        	if(e!=null){
//        		Integer oldScore=Integer.valueOf(e.getScoreDetails());
//        		if(score>oldScore){
//        			List<TblRiskImprovementDetailsEntiry> list=tblRiskImprovementDetailsMapper.getDetailsEntiry(new BigDecimal(id));
//        		    for(int i=0;i<=(score-oldScore)-1;i++){
//        		    	TblRiskImprovementDetailsEntiry entity=list.get(i);
//        		    	entity.setScoreDetails("0");
//        		    	tblRiskImprovementDetailsMapper.updateById(entity);
//        		    }	
//        		}
//        	}
//        	result.put("data", "操作成功！");
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		 return ResponseFormat.retParam(1, 200, result);
//	}
	
	@Override
	public JsonBean updateScoreNew(String token, BigDecimal id, String score) {
		// TODO Auto-generated method stub
		Map<String, Object> result=null;
		try {
			result=new HashMap<String, Object>();
        	if(StringUtils.isNotBlank(score)){
        		    	TblRiskImprovementDetailsEntiry entity=tblRiskImprovementDetailsMapper.getOne(id);
        		    	entity.setScoreDetails(score);
        		    	tblRiskImprovementDetailsMapper.updateById(entity);
        		    }	
        	result.put("data", "操作成功！");
		} catch (Exception e) {
			// TODO: handle exception
		}
		 return ResponseFormat.retParam(1, 200, result);
	}
}
