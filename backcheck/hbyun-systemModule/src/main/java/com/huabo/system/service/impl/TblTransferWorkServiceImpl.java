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
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblTransferWork;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblTransferWorkMapper;
import com.huabo.system.mapper.TblUserOrgRelationMapper;
import com.huabo.system.service.TblTransferWorkService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblTransferWorkServiceImpl implements TblTransferWorkService {
	@Resource
	private TblTransferWorkMapper tblTransferWorkMapper;
	
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblUserOrgRelationMapper tblUserOrgRelationMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean getPageList(String token, Integer pageNumber, Integer pageSize, TblTransferWork work) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblTransferWork> wrapper = new QueryWrapper<TblTransferWork>();
		
		if(StringUtils.isNotBlank(work.getCreatestaffname())) {
			wrapper.like("CREATESTAFFNAME", work.getCreatestaffname());
		}
		if(StringUtils.isNotBlank(work.getTransefrstaffname())) {
			wrapper.like("TRANSEFRSTAFFNAME", work.getTransefrstaffname());
		}
		if(StringUtils.isNotBlank(work.getDockstaffname())) {
			wrapper.like("DOCKSTAFFNAME", work.getDockstaffname());
		}
		
		wrapper.orderByDesc("TRANSFERTIME");
		
		Page<TblTransferWork> pageCondition = new Page<TblTransferWork>(pageNumber,pageSize);
		pageCondition.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblTransferWork> page = this.tblTransferWorkMapper.selectPage(pageCondition, wrapper);
		
		for (TblTransferWork en : page.getRecords()) {
			if(StringUtils.isNotBlank(en.getTranorgidstrs())) {
				en.setTranorgnamestrs(String.join(",", this.tblOrganizationMapper.selectTreeNamesByOrgTreeId(en.getTranorgidstrs())));
			}
		}
		
		return ResponseFormat.retParam(1, 200, page);
	}

	@Override
	public JsonBean mengerEntity(String token, TblTransferWork work) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(work.getTransferid() != null) {
			this.tblTransferWorkMapper.updateById(work);
		}else {
			work.setTransferid(RandomUtil.uuBigDecimalId());
			work.setCreatestaffid(loginStaff.getStaffid());
			work.setCreatestaffname(loginStaff.getRealname());
			work.setLinkdeptid(loginStaff.getLinkDetp().getOrgid());
			work.setLinkorgid(loginStaff.getCurrentOrg().getOrgid());
			work.setTranenablestatus(0);
			work.setTranstatus(0);
			this.tblTransferWorkMapper.insert(work);
		}
		return ResponseFormat.retParam(1, 200, work);
	}

	@Override
	public JsonBean enableStatus(String token, BigDecimal transferid, Integer transtatus) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblTransferWork work = this.tblTransferWorkMapper.selectById(transferid);
		/*if(transtatus == 1) {
			//启用 废除掉之前的人员所属组织   
			//获取移交人
			TblStaff yjstaff = this.tblStaffMapper.findByStaffid(work.getTransferstaffid());
			//获取移交的部门
			String orgIdStrs = work.getTranorgidstrs();
			this.tblUserOrgRelationMapper.deleteRelationByStaffIdDeptId(yjstaff.getStaffid(),orgIdStrs);
		}*/
		
			//弃用直接修改状态
			TblTransferWork update = new TblTransferWork();
			update.setTransferid(transferid);
			update.setTranstatus(transtatus);
			update.setTransfertime(new Date());
			this.tblTransferWorkMapper.updateById(update);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getDetail(String token, BigDecimal transferid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblTransferWork work = this.tblTransferWorkMapper.selectById(transferid);
		if(StringUtils.isNotBlank(work.getTranorgidstrs())) {
			work.setTranorgnamestrs(String.join(",", this.tblOrganizationMapper.selectTreeNamesByOrgTreeId(work.getTranorgidstrs())));
		}
		return ResponseFormat.retParam(1, 200, work);
	}


}
