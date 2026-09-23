package com.huabo.contract.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblBiPage;
import com.huabo.contract.entity.TblYyPrice;
import com.huabo.contract.entity.TblYyXdfTeam;
import com.huabo.contract.mapper.TblBiPageMapper;
import com.huabo.contract.mapper.TblYyPriceMapper;
import com.huabo.contract.mapper.TblYyXdfCompanyMapper;
import com.huabo.contract.mapper.TblYyXdfTeamMapper;
import com.huabo.contract.service.TblYyXdfTeamService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblYyXdfTeamServiceImpl implements TblYyXdfTeamService {
	
	@Autowired
	private UserProvider userProvider;
	
	@Resource
    private TblYyXdfTeamMapper tblYyXdfTeamMapper;
	
	@Resource
	private TblYyPriceMapper tblYyPriceMapper;
	
	@Resource
	private TblYyXdfCompanyMapper tblYyXdfCompanyMapper;
	
	@Resource
	private TblBiPageMapper tblBiPageMapper;

	
	@Override
	public Map<String, Object> saveOrupdateTeam(TblYyXdfTeam team) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
	     try {
	         TblStaffUtil staff = userProvider.get();
	         if (staff == null) {
	             resultMap.put("code", "0");
	             resultMap.put("msg", "用户已失效！");
	             return resultMap;
	         }
	         // 如果团队ID不为空，则更新团队信息
	         if (team.getTeamid() != null) {
	             tblYyXdfTeamMapper.updateTeam(team);
	         } else {
	             // 否则，创建新的团队记录，设置公司ID、创建日期和员工ID
	             team.setCompanyid(staff.getCurrentOrg().getOrgid());
	             team.setCreatedate(new Date());
	             team.setStaffid(staff.getStaffid());
	             team.setTeamid(RandomUtil.uuBigDecimalId());
	             tblYyXdfTeamMapper.insertTeam(team);
	         }
	
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     // 设置返回结果
	     resultMap.put("code", "1");
	     resultMap.put("msg", "成功");
	     resultMap.put("data", team);
	     return resultMap;
	}
	
	@Override
    public Map<String, Object> findBYuseridAndCompanid() throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 Map<String, Object> dataMap = new HashMap<String, Object>(0);
		 // 解析用户令牌
		 TblStaffUtil staff = userProvider.get();
		 if (staff == null) {
		     resultMap.put("code", "0");
		     resultMap.put("msg", "用户已失效！");
		     return resultMap;
		 }
		 // 获取价格列表
		 List<TblYyPrice> pricelist = tblYyPriceMapper.findAll();
		 // 获取页面子节点列表
		 List<TblBiPage> pageChilds = tblBiPageMapper.findByOrgid(staff.getStaffid(), staff.getLinkDetp().getOrgid());
		 if (pageChilds != null && pageChilds.size() > 0) {
		     dataMap.put("pageChilds", pageChilds);
		 }
		 // 获取与用户和公司相关的团队列表
		 List<TblYyXdfTeam> list = this.tblYyXdfTeamMapper.countlistByOrgidAndStaffid(staff.getCurrentOrg().getOrgid(), staff.getStaffid());
		 /*List<TblYyXdfTeam> newlist = new ArrayList();
		 if (list != null && list.size() > 0) {
		     Iterator var6 = list.iterator();
		     TblYyXdfTeam tblyyxdfTeam = null;
		     Integer count = 0;
		     while (var6.hasNext()) {
		         tblyyxdfTeam = (TblYyXdfTeam) var6.next();
		         count = this.tblYyXdfCompanyMapper.listBySqlPageCount(tblyyxdfTeam.getTeamid());
		         // 获取公司数量
		         tblyyxdfTeam.setCopanycount(count);
		         newlist.add(tblyyxdfTeam);
		     }
		 }*/
		
		 if (list != null && list.size() > 0) {
		     dataMap.put("teams", list);
		     dataMap.put("count", list.size());
		 }
		 dataMap.put("pricelist", pricelist);
		 // 设置返回结果码和消息
		 resultMap.put("code", "1");
		 resultMap.put("msg", "成功");
		 resultMap.put("data", dataMap);
		 return resultMap;
    }

}
