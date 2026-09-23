package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemUrgentEvents;
import com.huabo.system.entity.TblVirtualOrgInfo;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblVirtualOrgInfoMapper;
import com.huabo.system.service.TblVirtualOrgInfoService;

@Service
public class TblVirtualOrgInfoServiceImpl implements TblVirtualOrgInfoService {
	
    @Resource
    private TblVirtualOrgInfoMapper tblVirtualOrgInfoMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean adds(List<TblVirtualOrgInfo> voiList) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        if(voiList == null || voiList.size() == 0) {
        	return ResponseFormat.retParam(0, 10002, null);
        }
        
        for (TblVirtualOrgInfo voi : voiList) {
        	if(StringUtils.isBlank(voi.getVirtualname())) {
        		continue;
        	}
			voi.setFid(RandomUtil.uuStringId());
			voi.setCreator(loginStaff.getStaffid());
			voi.setCreationtime(new Date());
			voi.setDeleteflag(0);
			this.tblVirtualOrgInfoMapper.insert(voi);
		}
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean modify(TblVirtualOrgInfo voi) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        if(voi == null)  {
        	return ResponseFormat.retParam(0, 10002, null);
        }
        
        if(StringUtils.isBlank(voi.getVirtualname())) {
        	return ResponseFormat.retParam(0, 10002, null);
        }
		voi.setModifier(loginStaff.getStaffid());
		voi.setModifiedtime(new Date());
		this.tblVirtualOrgInfoMapper.updateById(voi);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean remove(String fid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        
        if(StringUtils.isBlank(fid)) {
        	return ResponseFormat.retParam(0, 10002, null);
        }
        
        TblVirtualOrgInfo voi = this.tblVirtualOrgInfoMapper.selectById(fid);
        voi.setDeleteflag(-1);
		voi.setModifier(loginStaff.getStaffid());
		voi.setModifiedtime(new Date());
		this.tblVirtualOrgInfoMapper.updateById(voi);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean list(BigDecimal orgid, String virtualname, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        if(orgid == null) {
        	return ResponseFormat.retParam(0, 10002, null);
        }
        
        QueryWrapper<TblVirtualOrgInfo> wrapper = new QueryWrapper<TblVirtualOrgInfo>();
        wrapper.eq("DELETEFLAG", 0);
        wrapper.eq("ORGID", orgid);
        if(StringUtils.isNotBlank(virtualname)) {
        	wrapper.like("VIRTUALNAME", virtualname);
        }
        Page<TblVirtualOrgInfo> page = new Page<TblVirtualOrgInfo>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblVirtualOrgInfo> pageList = tblVirtualOrgInfoMapper.selectPage(page, wrapper);
        
        TblOrganization orgInfo = this.tblOrganizationMapper.selectById(orgid);
        for (TblVirtualOrgInfo voi : pageList.getRecords()) {
			voi.setOrg(orgInfo);
		}
        return ResponseFormat.retParam(1, 200, pageList);
	}
	
	
	

}
