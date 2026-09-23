package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblAuditModelExcelExtOracle;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;
import com.huabo.audit.util.PageResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.x509.qualified.TypeOfBiometricData;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjType;
import com.huabo.audit.oracle.entity.TblNbsjTypeOf;
import com.huabo.audit.oracle.entity.TblYqnsFgldhz;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTypeMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTypeOfMapper;
import com.huabo.audit.service.TblNbsjTypeOfService;
import com.huabo.audit.service.TblNbsjTypeService;
@Service
public class TblNbsjTypeOfServiceImpl implements TblNbsjTypeOfService {
	@Resource
	TblNbsjTypeOfMapper tblNbsjTypeOfMapper;
	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public   JsonBean findAllList(String token, Integer typeid,Integer pageNumber,Integer pageSize,String auditType) throws Exception{
		// TODO Auto-generated method stub
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return null;
		}
		List<TblNbsjTypeOf> addAll = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//获取公司下的审计类型树
		if(typeid>0&&pageNumber>0&&pageSize>0){
			 PageInfo<TblNbsjTypeOf> pageInfo= PageMethod.startPage(pageNumber,pageSize)
						.doSelectPageInfo(() -> tblNbsjTypeOfMapper.findAll(typeid,auditType));
				PageResult<TblNbsjTypeOf> build = new PageResult<TblNbsjTypeOf>().build(pageInfo);
				return ResponseFormat.retParam(1, 200, build);
		}else{
		 List<TblNbsjTypeOf> allList = tblNbsjTypeOfMapper.getAllByOrgid(user.getCurrentOrg().getOrgid());
		 List<TblNbsjTypeOf> typeOf = tblNbsjTypeOfMapper.getAllParent(user.getCurrentOrg().getOrgid());
		 doAllList(allList, addAll, typeOf);
		 addAll.addAll(typeOf);
		 resultMap.put("date", addAll);
		}
	
      
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private void doAllList(List<TblNbsjTypeOf> allList, List<TblNbsjTypeOf> addList, List<TblNbsjTypeOf> collect1) {
		collect1.forEach(it1 -> {
			List<TblNbsjTypeOf> collect2 = allList.stream().filter(it2 ->it2.getParentid()!=null&&it2.getParentid().compareTo(it1.getTypeId())==0).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(collect2)) {
				it1.setChildrenList(collect2);
			}
			doAllList(allList, addList, collect2);
		});
	}
 

	@Override
	public JsonBean saveNbsjTypeOf(TblNbsjTypeOf nbsjTypeOf, String token) throws Exception{
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			TblNbsjTypeOf type = null;
			String typeNameStr = "";
			StringBuffer nameSb = new StringBuffer();
			if (nbsjTypeOf!=null&&nbsjTypeOf.getParentid() == null) {//一级父节点
				List<TblNbsjTypeOf> typeOf = tblNbsjTypeOfMapper.findByOrgidAndType(user.getCurrentOrg().getOrgid(), nbsjTypeOf.getAuditType());
				if(typeOf!=null && typeOf.size()>0) {
		               return ResponseFormat.retParam(0,50003,null);			}
				// 新建
				nbsjTypeOf.setVersion("1");
				if(nbsjTypeOf.getStatus()==null) {
					nbsjTypeOf.setStatus(2);
				}
				 
				nbsjTypeOf.setCreatestaff(user.getStaffid());
				nbsjTypeOf.setCreatetime(new Date());
				nbsjTypeOf.setOrgid(user.getCurrentOrg().getOrgid().toString());
				nbsjTypeOf.setAuditCode(1);//1当作1级菜单标识
				tblNbsjTypeOfMapper.insert(nbsjTypeOf);

			} else {//子级
//				List<TblNbsjTypeOf> typeOf = tblNbsjTypeOfMapper.findByOrgidAndId(user.getCurrentOrg().getOrgid(),type.getAuditType(),type.getParentid());
//				if(typeOf!=null && typeOf.size()>0) {
//				    resultMap.put("code", "0");
//					resultMap.put("msg", "审计类型不能重复");
//					return resultMap;	
//				}
				List<TblNbsjTypeOf> typeOf = tblNbsjTypeOfMapper.findByOrgidAndType(user.getCurrentOrg().getOrgid(), nbsjTypeOf.getAuditType());
				if(typeOf!=null && typeOf.size()>0) {
		               return ResponseFormat.retParam(0,50003,null);	
		        }
				nbsjTypeOf.setVersion("1");
				if(nbsjTypeOf.getStatus()==null) {
					nbsjTypeOf.setStatus(2);
				}
					nbsjTypeOf.setOrgid(user.getCurrentOrg().getOrgid().toString());
					nbsjTypeOf.setCreatestaff(user.getStaffid());
					nbsjTypeOf.setCreatetime(new Date());
					tblNbsjTypeOfMapper.insert(nbsjTypeOf);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean  modifyNbsjTypeOf(String token, TblNbsjTypeOf nbsjTypeOf)throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			Integer count = this.tblNbsjTypeOfMapper.selectCountByNbsjType(user.getCurrentOrg().getOrgid(), nbsjTypeOf.getTypeId().toString());
			if (count > 0) {
				resultMap.put("code", "-2");
				resultMap.put("msg", "已被使用无法修改！"); 
				return ResponseFormat.retParam(0,90010,resultMap);
			}
			Integer reCount = this.tblNbsjTypeOfMapper.selectRepeatCount(nbsjTypeOf.getAuditType(), user.getCurrentOrg().getOrgid(), nbsjTypeOf.getTypeId().toString());
			if (reCount > 0) {
				resultMap.put("code", "-1");
				resultMap.put("msg", "审计类型重复！"); 
				return ResponseFormat.retParam(0,50003,resultMap);
			}

			TblNbsjTypeOf type = this.tblNbsjTypeOfMapper.selectById(nbsjTypeOf.getTypeId());
		    Integer varsion = Integer.parseInt(type.getVersion())+1;
		    type.setVersion(varsion.toString());
			type.setAuditType(nbsjTypeOf.getAuditType());
			type.setStatus(nbsjTypeOf.getStatus());
			tblNbsjTypeOfMapper.updateById(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean removeNbsjTypeOf(String typeId, String token)throws Exception {
		// TODO Auto-generated method stub
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			try {
				TblStaffUtil user = userProvider.get();
				if (user == null) {
					return ResponseFormat.retParam(0,20006,null);
				}
				Integer count = this.tblNbsjTypeOfMapper.selectCountByType(user.getCurrentOrg().getOrgid(), typeId);
				if (count > 0) {
					resultMap.put("code", "-1");
					resultMap.put("msg", "已被使用无法删除！");
					return ResponseFormat.retParam(0,201,resultMap);
				}
				this.tblNbsjTypeOfMapper.deleteTypeByParentId(typeId);
				this.tblNbsjTypeOfMapper.deleteTypeByTypeId(typeId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ResponseFormat.retParam(1,70003,resultMap);
	}

	@Override
	public  JsonBean  SelectNbsjType(String type, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjTypeOf nbsjtype=tblNbsjTypeOfMapper.selectById(type);
         resultMap.put("date", nbsjtype);
		return ResponseFormat.retParam(1,200,resultMap);
	}

}
