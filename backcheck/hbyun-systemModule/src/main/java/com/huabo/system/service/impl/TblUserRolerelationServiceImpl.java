package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblUserRolerelation;
import com.huabo.system.mapper.TblUserRolerelationMapper;
import com.huabo.system.service.TblUserRolerelationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TblUserRolerelationServiceImpl implements TblUserRolerelationService {
	
	@Resource
	private TblUserRolerelationMapper tblUserRolerelationMapper;
	
	@Override
	public void setEndRoleDateByStaffIds(String roleid, String staffids) throws Exception {
		QueryWrapper<TblUserRolerelation> wrapper = new QueryWrapper<TblUserRolerelation>();
		
		wrapper.eq("ROLEID", roleid);
		wrapper.inSql("STAFFID", staffids);
		wrapper.isNull("ENDTIME");
		
		TblUserRolerelation rela = new TblUserRolerelation();
		rela.setEndtime(new Date());
		rela.setStatus(0);
		
		this.tblUserRolerelationMapper.update(rela, wrapper);
	}


	@Override
	public void saveEntity(TblUserRolerelation rela) throws Exception {
		this.tblUserRolerelationMapper.insert(rela);
	}


	@Override
	public void dealUserRoleRelation(BigDecimal staffId, String roleIdStrs, String iscanpre, TblStaffUtil loginStaff, String preRoleStrs)
			throws Exception {
		String[] roleIds = roleIdStrs.split(",");
		String[] iscapres = iscanpre.split(",");
		TblUserRolerelation rela = null;
		
		
		if(StringUtils.isBlank(preRoleStrs)) {
			//preRoleStrs 为空新增用户直接保存用户和角色关系
			for (int i = 0; i < roleIds.length; i++) {
				rela = new TblUserRolerelation();
				rela.setRelaid(RandomUtil.uuBigDecimalId());
				rela.setCreatestaffid(loginStaff.getStaffid());
				rela.setCreatestaffname(loginStaff.getRealname());
				rela.setIscanpre(Integer.parseInt(iscapres[i]));
				rela.setRoleid(new BigDecimal(roleIds[i]));
				rela.setStaffid(staffId);
				rela.setCreatetime(new Date());
				rela.setStatus(1);
				this.tblUserRolerelationMapper.insert(rela);
			}
		}else {
			//不为空是修改
			//获取当前用户在角色关系表中已有的角色主键
			List<String> roleList = this.tblUserRolerelationMapper.selectRoleIdsByStaffIsUse(staffId);
			
			//剔除已有的角色不予处理
			List<String> noRoleList = new ArrayList<String>(0); //未有的角色主键集合
			List<String> iscanpreList = new ArrayList<String>(0); //未有的角色是否查看所有
			boolean flag = true;
			for (int i = 0; i < roleIds.length; i++) {
				flag = true;
				for (String roleId : roleList) {
					if(roleId.equals(roleIds[i])) {
						flag = false;
					}
				}
				//没有相同的角色需要新增;
				if(flag) {
					noRoleList.add(roleIds[i]);
					iscanpreList.add(iscapres[i]);
				}
			}
			for (int i = 0; i < noRoleList.size(); i++) {
				rela = new TblUserRolerelation();
				rela.setRelaid(RandomUtil.uuBigDecimalId());
				rela.setCreatestaffid(loginStaff.getStaffid());
				rela.setCreatestaffname(loginStaff.getRealname());
				rela.setIscanpre(Integer.parseInt(iscanpreList.get(i)));
				rela.setRoleid(new BigDecimal(noRoleList.get(i)));
				rela.setStaffid(staffId);
				rela.setStatus(1);
				rela.setCreatetime(new Date());
				this.tblUserRolerelationMapper.insert(rela);
			}
			//针对修改取消的角色 修改结束时间
			String cancelRoleIdStrs = "";
			String[] preRoles = preRoleStrs.split(",");
			for (String preRoleId : preRoles) {
				flag = true;
				for (String roleId : roleIds) {
					if(roleId.equals(preRoleId)) {
						flag = false;
					}
				}
				if(flag) {
					cancelRoleIdStrs += preRoleId + ",";
				}
			}
			
			//修改取消角色结束授权时间
			cancelRoleIdStrs = cancelRoleIdStrs.substring(0, cancelRoleIdStrs.length()-1);
			
			QueryWrapper<TblUserRolerelation> wrapper = new QueryWrapper<TblUserRolerelation>();
			
			wrapper.eq("STAFFID", staffId);
			wrapper.inSql("ROLEID", cancelRoleIdStrs);
			wrapper.isNull("ENDTIME");
			
			TblUserRolerelation uprela = new TblUserRolerelation();
			uprela.setEndtime(new Date());
			uprela.setStatus(0);
			
			this.tblUserRolerelationMapper.update(uprela, wrapper);
			
		}
	}

	
	/**
	 * SELECT * FROM TBL_USER_ROLERELATION;


CREATE TABLE TBL_USER_ROLERELATION
(
	RELAID NUMBER PRIMARY KEY,
	STAFFID NUMBER,
	ROLEID NUMBER,
	PRESTAFFID NUMBER,
	CREATETIME DATE,
	ENDTIME DATE,
	ISCANPRE NUMBER,
	STATUS NUMBER,
	CREATESTAFFID NUMBER,
	CREATESTAFFNAME VARCHAR2(300)
);
COMMENT ON TABLE TBL_USER_ROLERELATION IS '用户角色关系表';
COMMENT ON COLUMN TBL_USER_ROLERELATION.RELAID IS '主键';
COMMENT ON COLUMN TBL_USER_ROLERELATION.STAFFID IS '员工主键';
COMMENT ON COLUMN TBL_USER_ROLERELATION.PRESTAFFID IS '上一任员工主键';
COMMENT ON COLUMN TBL_USER_ROLERELATION.CREATETIME IS '创建时间';
COMMENT ON COLUMN TBL_USER_ROLERELATION.ENDTIME IS '取消授权时间';
COMMENT ON COLUMN TBL_USER_ROLERELATION.ISCANPRE IS '是否可以操作之前授权角色用户的数据，1-是，0否 默认1';
COMMENT ON COLUMN TBL_USER_ROLERELATION.STATUS IS '启用弃用状态 1-启用 0-弃用 默认1';
COMMENT ON COLUMN TBL_USER_ROLERELATION.CREATESTAFFID IS '创建人主键';
COMMENT ON COLUMN TBL_USER_ROLERELATION.CREATESTAFFNAME IS '创建人姓名';



SELECT * FROM TBL_USER_ROLERELATION WHERE ROLEID =12 AND STAFFID = 839815
	 */

}
