package com.huabo.contract.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblYyPrice;
import com.huabo.contract.entity.TblYyReportModel;
import com.huabo.contract.entity.TblYyXdfCompany;
import com.huabo.contract.mapper.TblYyPriceMapper;
import com.huabo.contract.mapper.TblYyReportModelMapper;
import com.huabo.contract.mapper.TblYyXdfCompanyMapper;
import com.huabo.contract.service.TblyyxdfCompanyService;

@Service
public class TblyyxdfCompanyServiceImpl implements TblyyxdfCompanyService {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
    private TblYyXdfCompanyMapper tblYyXdfCompanyMapper;
	
	@Resource
    private TblYyPriceMapper tblYyPriceMapper;
	
	@Resource
    private TblYyReportModelMapper tblYyReportModelMapper;
	
	// 定义一个名为findByCompay的方法，返回一个Map<String, Object>类型的结果
    @Override
    public Map<String, Object> findByCompay(Integer pageNumber, Integer pageSize, String teamid, String fxtype, String companyname) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 TblStaffUtil staff = userProvider.get();
		 if (staff == null) {
		     // 如果用户令牌无效，设置结果码和消息，然后返回结果
		     resultMap.put("code", "0");
		     resultMap.put("msg", "用户已失效！");
		     return resultMap;
		 }
		 // 创建一个PageInfo对象，用于存储分页信息和查询结果
		 PageInfo<TblYyXdfCompany> pageInfo = new PageInfo<TblYyXdfCompany>();
		 if (teamid != null && teamid.trim().length() > 0) {
		     // 创建一个公司对象，设置查询条件
		     TblYyXdfCompany company = new TblYyXdfCompany();
		     company.setCompanyname(companyname);
		     company.setFxtype(fxtype);
		     company.setTeamid(new BigDecimal(teamid));
		     company.setStaffid(staff.getStaffid());
		     // 设置分页信息和查询条件
		     IPage<TblYyXdfCompany> page = new Page<TblYyXdfCompany>(pageNumber,pageSize);
		     IPage<TblYyXdfCompany> pageList = tblYyXdfCompanyMapper.selectListByPageInfo(page,company);
		    
		     // 处理查询结果，为每个公司对象设置价格列表
		     List<TblYyXdfCompany> list = pageList.getRecords();
		     List<TblYyXdfCompany> newlist = new ArrayList<TblYyXdfCompany>();
		     if (list != null && list.size() > 0) {
		    	 String priceIds = "";
		    	 List<TblYyPrice> list2 = new ArrayList<TblYyPrice>(0);
		         for (TblYyXdfCompany tblyyCompany : list) {
		        	 priceIds = tblYyPriceMapper.findByIs(tblyyCompany.getReportid());
		        	 list2 = tblYyPriceMapper.findByPriceIds(priceIds);
		             tblyyCompany.setList(list2);
		             newlist.add(tblyyCompany);
		         }
		        
		     }
		     pageInfo.setCurrentPage(pageNumber);
		     pageInfo.setPageSize(pageSize);
		     pageInfo.setCondition(company);
		     // 执行查询，并将结果设置到pageInfo对象中
		     pageInfo.setTlist(newlist);
		     pageInfo.setTotalRecord((int) pageList.getTotal());
		 } else {
		     // 如果teamid不存在，执行另一种查询逻辑
		     TblYyXdfCompany company = new TblYyXdfCompany();
		     company.setCompanyname(companyname);
		     company.setFxtype(fxtype);
		     company.setStaffid(staff.getStaffid());
		     company.setOrgid(staff.getCurrentOrg().getOrgid());
		     
		     IPage<TblYyXdfCompany> page = new Page<TblYyXdfCompany>(pageNumber,pageSize);
		     IPage<TblYyXdfCompany> pageList = tblYyXdfCompanyMapper.selectListByPageInfo(page,company);
		     
		     List<TblYyXdfCompany> list = pageInfo.getTlist();
		     List<TblYyXdfCompany> newlist = new ArrayList<TblYyXdfCompany>();
		     if (list != null && list.size() > 0) {
		    	 String priceIds = "";
		    	 List<TblYyPrice> list2 = new ArrayList<TblYyPrice>(0);
		         for (TblYyXdfCompany tblyyCompany : list) {
		             priceIds = tblYyPriceMapper.findByIs(tblyyCompany.getReportid());
		        	 list2 = tblYyPriceMapper.findByPriceIds(priceIds);
		             tblyyCompany.setList(list2);
		             newlist.add(tblyyCompany);
		         }
		     }
		     pageInfo.setCurrentPage(pageNumber);
		     pageInfo.setPageSize(pageSize);
		     pageInfo.setCondition(company);
		     pageInfo.setTlist(newlist);
		     pageInfo.setTotalRecord((int) pageList.getTotal());
		 }
		 resultMap.put("code", "1");
		 resultMap.put("msg", "访问接口成功");
		 resultMap.put("data", pageInfo);
		 return resultMap;
    }

	@Override
	public Map<String, Object> saveOrupdateYYCompany(String pageid, String priceid, TblYyXdfCompany company)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
		    resultMap.put("code", "0");
		    resultMap.put("msg", "用户已失效！");
		    return resultMap;
		}
		// 创建报告模型
		TblYyReportModel model = new TblYyReportModel();
		model.setOrgid(staff.getCurrentOrg().getOrgid());
		model.setPriceid(priceid);
		model.setStaffid(staff.getStaffid());
		model.setReportname(company.getCompanyname());
		model.setReportid(RandomUtil.uuBigDecimalId());
		// 保存报告模型
		tblYyReportModelMapper.insert(model);
		
		// 设置公司信息
		company.setReportid(model.getReportid());
		company.setOrgid(staff.getCurrentOrg().getOrgid());
		company.setStaffid(staff.getStaffid());
		company.setCreatedate(new Date());
		// company.setPages(pagess);
		// 保存公司信息
		tblYyXdfCompanyMapper.insert(company);
		resultMap.put("code", "1");
		resultMap.put("msg", "访问接口成功");
		resultMap.put("data", company);
		
		return resultMap;
	}
}
