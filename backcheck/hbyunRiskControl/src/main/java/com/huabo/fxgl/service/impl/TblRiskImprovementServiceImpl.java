package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.TblRiskImprovementDetailsMapper;
import com.huabo.fxgl.mapper.TblRiskImprovementMapper;
import com.huabo.fxgl.service.TblRiskImprovementService;
import com.huabo.fxgl.util.PageResult;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TblRiskImprovementServiceImpl extends ServiceImpl<TblRiskImprovementMapper, TblRiskImprovementEntiry> implements TblRiskImprovementService {

    @Resource
    private TblRiskImprovementMapper tblRiskImprovementMapper;
    
    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private TblRiskImprovementDetailsMapper tblRiskImprovementDetailsMapper;
    
    @Resource
    private UserProvider userProvider;
    /**
     * 下发后在总公司列表（风险监督改进）显示下发详情
     *  @param improvementEntiry
     */
    @Override
    public void insertImprovement(TblRiskImprovementEntiry improvementEntiry) {
        try {
            tblRiskImprovementMapper.insert(improvementEntiry);
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
    public JsonBean riskImprovementList(String token, Integer pageNumber, Integer pageSize,String name,String orgname) {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            final  List<Map<BigDecimal, Object>>  orgList = tblRiskImprovementMapper.getOrgGroupList(name);
            List<TblRiskImprovementDetailsEntiry> riskList=new ArrayList<TblRiskImprovementDetailsEntiry>();
            String groupCompanyID = SysConfig.get("groupCompanyID");
            int i=1;
            for(Map<BigDecimal, Object> s:orgList){  
            	BigDecimal id=(BigDecimal) s.get("BRANCH_ID");
            	if(id.compareTo(new BigDecimal(groupCompanyID))!=0){ //总公司得用其他方法
            	TblRiskImprovementDetailsEntiry e=tblRiskImprovementDetailsMapper.getScore(id,orgname);
                if(e!=null){
                e.setId(new BigDecimal(i));
            	riskList.add(e);
            	i++;
                }
            	}else{
            		List<TblRiskImprovementDetailsEntiry> entitys=tblRiskImprovementDetailsMapper.getScoreHeadOffice(id, orgname);
            		for(TblRiskImprovementDetailsEntiry e:entitys){
            			e.setId(new BigDecimal(i));
            			i++;
            		}
            		riskList.addAll(entitys);
            	}
            }
            // 计算开始索引和结束索引
            int startIndex = (pageNumber - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, riskList.size());
     
            // 获取当前页的数据
            List<TblRiskImprovementDetailsEntiry> pageData = riskList.subList(startIndex, endIndex);
            com.github.pagehelper.PageInfo<TblRiskImprovementDetailsEntiry> pageInfo=new PageInfo<>(pageData) ;
            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
            pageInfo.setTotal(riskList.size());
            PageResult<TblRiskImprovementDetailsEntiry> build = new PageResult<TblRiskImprovementDetailsEntiry>().build(pageInfo);
            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }
	@Override
	public    List<TblRiskImprovementDetailsEntiry> reportList(String token, String name,String orgname) {
		// TODO Auto-generated method stub
		    final  List<Map<BigDecimal, Object>>  orgList = tblRiskImprovementMapper.getOrgGroupList(name);
            List<TblRiskImprovementDetailsEntiry> riskList=new ArrayList<TblRiskImprovementDetailsEntiry>();
            String groupCompanyID = SysConfig.get("groupCompanyID");
            for(Map<BigDecimal, Object> s:orgList){  
            	BigDecimal id=(BigDecimal) s.get("BRANCH_ID");
            	if(id.compareTo(new BigDecimal(groupCompanyID))!=0){ //总公司得用其他方法
            	TblRiskImprovementDetailsEntiry e=tblRiskImprovementDetailsMapper.getScore(id,orgname);
                riskList.add(e);
            	}else{
            		List<TblRiskImprovementDetailsEntiry> e=tblRiskImprovementDetailsMapper.getScoreHeadOffice(id, orgname);
            		riskList.addAll(e);
            	}
            }
          return riskList;
	}
    
   
}
