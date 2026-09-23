package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditprogramMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTargettypeMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTempleteMapper;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;
import com.huabo.audit.service.TblNbsjTempleteService;

import cn.hutool.core.util.StrUtil;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-20
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjTempleteServiceImpl  implements TblNbsjTempleteService {
    @Autowired
    private TblNbsjTempleteMapper tblNbsjTempleteMapper;
    @Autowired
    private TblNbsjTargettypeMapper tblNbsjTargettypeMapper;
    @Autowired
    private TblNbsjAuditprogramMapper tblNbsjAuditprogramMapper;
    
    @Resource
    private UserProvider userProvider;
    
	public JsonBean selectNbsjTempleteListByPageInfo(String token, Integer pageNumber, Integer pageSize,TblNbsjTempleteVo tblNbsjTempleteVo) throws Exception {
    	
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjTempleteEntity> pageInfo = new PageInfo<TblNbsjTempleteEntity>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	List<TblNbsjTempleteEntity> list = this.tblNbsjTempleteMapper.selectNbsjTempleteListByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid(),tblNbsjTempleteVo);
    	for (TblNbsjTempleteEntity tblNbsjTempleteEntity : list) {
    		//根据模板ID查询适用机构
    		tblNbsjTempleteEntity.setTemorgname(tblNbsjTempleteMapper.findOrgNameByTempId(tblNbsjTempleteEntity.getTempleteId()));
		}
    	pageInfo.setTlist(list);
    	
    	
    	pageInfo.setTotalRecord(this.tblNbsjTempleteMapper.selectNbsjTempleteListCountByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid(),tblNbsjTempleteVo));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	public JsonBean mergeNbsjTempleteInfo(TblNbsjTempleteEntity templete, String token,String orgids) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(templete.getTempleteId()!= null) {
			//修改；
			System.out.println(templete.getTempleteId().toString());
			tblNbsjTempleteMapper.deleteTemOrg(templete.getTempleteId().toString());
			templete.setUpdateDate(new Date());
			templete.setUpdateStaffId(loginStaff.getStaffid());
			tblNbsjTempleteMapper.updateByPrimaryKeySelective(templete);
			//修改第一个节点的名称
			TblNbsjTargettypeEntity selectFirstTarget = tblNbsjTargettypeMapper.selectFirstTarget(templete.getTempleteId().toString());
			selectFirstTarget.setTargetName(templete.getTempleteName());
			selectFirstTarget.setTargetDesc(templete.getTempleteName());
			tblNbsjTargettypeMapper.updateById(selectFirstTarget);
		}else {
			//新增；
			templete.setCreateDate(new Date());
			templete.setStatus(1);
			templete.setOrgId(loginStaff.getCurrentOrg().getOrgid());
			templete.setStaffId(loginStaff.getStaffid());
			templete.setTempleteId(RandomUtil.uuBigDecimalId());
			tblNbsjTempleteMapper.insertSelective(templete);
			
			TblNbsjTargettypeEntity target = new TblNbsjTargettypeEntity();
			target.setTempId(templete.getTempleteId());
			target.setCreateTime(new Date());
			target.setParentId(null);
			target.setTargetName(templete.getTempleteName());
			target.setTargetDesc(templete.getTempleteName());
			if(templete.getTempType().equals("0")) {
				target.setStatus(TblNbsjTargettypeEntity.TEMP_NUMBER);
			}
			if(templete.getTempType().equals("1")) {
				target.setStatus(TblNbsjTargettypeEntity.ZY_NUMBER);
			}
			if(templete.getTempType().equals("2")) {
				target.setStatus(TblNbsjTargettypeEntity.COPY_TYPE);
			}
			//外层，目录默认创建1个
			target.setTargetId(RandomUtil.uuBigDecimalId());
			tblNbsjTargettypeMapper.insert(target);
		}
		if(!orgids.equals(null) && !orgids.equals("") && !orgids.equals("null")) {
			String[] split = orgids.split(",");
			for (String string : split) {
				System.out.println(string);
				tblNbsjTempleteMapper.insertTemOrg(templete.getTempleteId().toString(), string);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("templete",templete);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public LambdaQueryWrapper<TblNbsjTempleteEntity> onSelectWhere(TblNbsjTempleteEntity model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveTblNbsjTemplete(TblNbsjTempleteEntity model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTblNbsjTemplete(TblNbsjTempleteEntity model) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(BigDecimal bigDecimal) {
		// TODO Auto-generated method stub
		
	}
	
	public JsonBean updateStatus(String templeteId) throws Exception{
		//查询
		TblNbsjTempleteEntity selectById = tblNbsjTempleteMapper.findbyid(templeteId);
		if(selectById==null) {
			return ResponseFormat.retParam(1,50001,null);
		}
		Integer status=selectById.getStatus();
		if(status==1) {
			tblNbsjTempleteMapper.updateStatus(-1, templeteId);
			return ResponseFormat.retParam(1,200,null);
		}
		if(status==-1||status==0) {
			tblNbsjTempleteMapper.updateStatus(1, templeteId);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(0,50001,null);
	}
	public JsonBean copyTemplete(String templeteId,Integer copytype,String token) throws Exception{
		//查询
		System.out.println(templeteId);
		TblNbsjTempleteEntity oldTempletes = tblNbsjTempleteMapper.findInfobyid(templeteId);
		System.out.println("======0==");
		List<String> oldOrgids = tblNbsjTempleteMapper.selectOrgidByTempleteid(templeteId);
		System.out.println("======1==");
		TblNbsjTargettypeEntity oldFirstTarget = tblNbsjTargettypeMapper.selectFirstTarget(templeteId);
		System.out.println("===2=====");
		if(oldTempletes==null) {
			return ResponseFormat.retParam(1,50001,null);
		}
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	TblNbsjTempleteEntity newTempletes=new  TblNbsjTempleteEntity();
    	if (templeteId!=null) {
    			//新增模板
	 			newTempletes.setCreateDate(new Date());
	 			newTempletes.setStatus(oldTempletes.getStatus());
	 			newTempletes.setOrgId(loginStaff.getCurrentOrg().getOrgid());
	 			newTempletes.setStaffId(loginStaff.getStaffid());
	 			newTempletes.setTempleteCode(oldTempletes.getTempleteCode());
	 			newTempletes.setTempleteName(oldTempletes.getTempleteName());
	 			newTempletes.setTempleteDesc(oldTempletes.getTempleteDesc());
	 			newTempletes.setTempleteType(oldTempletes.getTempleteType());
	 			newTempletes.setTempType(copytype.toString());
				tblNbsjTempleteMapper.insertEntity(newTempletes);
				//涉及组织
				System.out.println("=====4===");
				for (String temorg : oldOrgids) {
					tblNbsjTempleteMapper.insertTemOrg(newTempletes.getTempleteId().toString(),temorg);
				}
				//新增模板目录
				TblNbsjTargettypeEntity fathertarget = new TblNbsjTargettypeEntity();
				fathertarget.setTempId(newTempletes.getTempleteId());
				fathertarget.setCreateTime(new Date());
				fathertarget.setParentId(null);
				fathertarget.setTargetName(newTempletes.getTempleteName());
				fathertarget.setTargetDesc(newTempletes.getTempleteName());
				fathertarget.setStatus(copytype);
				//外层，目录默认创建1个
				tblNbsjTargettypeMapper.insertEntity(fathertarget);//父节点
				System.out.println("====5====");
				capyMb(oldFirstTarget.getTargetId().toString(), newTempletes, fathertarget.getTargetId().toString(),copytype);//递归
				return  ResponseFormat.retParam(1,200,null);
    		}
		return ResponseFormat.retParam(0,10001,null);
	}
	public void capyMb(String parentid, TblNbsjTempleteEntity newtemp, String newparentid,Integer copytype) throws Exception {
		List<TblNbsjTargettypeEntity> list = tblNbsjTargettypeMapper.selectByParentId(parentid);
		if (list != null && list.size() > 0) {
			for (TblNbsjTargettypeEntity tblTargetType : list) {
				TblNbsjTargettypeEntity target = new TblNbsjTargettypeEntity();
				target.setCreateTime(tblTargetType.getCreateTime());
				target.setTempId(newtemp.getTempleteId());
				target.setParentId(new BigDecimal(newparentid));
				target.setStatus(copytype);//
				target.setTargetDesc(tblTargetType.getTargetDesc());
				target.setTargetName(tblTargetType.getTargetName());
				tblNbsjTargettypeMapper.insertEntity(target);
				List<TblNbsjAuditprogramEntity> oldgrams = tblNbsjAuditprogramMapper.selectByTargetid(tblTargetType.getTargetId());
				if (oldgrams != null && oldgrams.size() > 0) {
					for (TblNbsjAuditprogramEntity gram : oldgrams) {
						TblNbsjAuditprogramEntity prog = new TblNbsjAuditprogramEntity();
						prog.setBioData(gram.getBioData());
						prog.setTargetId(target.getTargetId());
						prog.setBusinessType(gram.getBusinessType());
						prog.setStatus(copytype);
						prog.setCreateTime(gram.getCreateTime());
						prog.setControl(gram.getControl());
						prog.setTempId(newtemp.getTempleteId());
						prog.setRiskPoint(gram.getRiskPoint());
						prog.setRiskSource(gram.getRiskSource());
						prog.setSuditProcess(gram.getSuditProcess());
						prog.setUpdateTime(gram.getUpdateTime());
						tblNbsjAuditprogramMapper.insertEntity(prog);
					}
				}
				capyMb(tblTargetType.getTargetId().toString(), newtemp, target.getTargetId().toString(),copytype);
			}
		}
	}
	public JsonBean selectInfo(String templeteId) throws Exception{
		//查询
		TblNbsjTempleteEntity selectById = tblNbsjTempleteMapper.findInfobyid(templeteId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("templete",selectById);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public JsonBean deleteInfo(String templeteId) throws Exception{
		TblNbsjTempleteEntity selectById = tblNbsjTempleteMapper.findInfobyid(templeteId);
		//查询
		if(selectById==null) {
			return ResponseFormat.retParam(1,50001,null);
		}
		tblNbsjTempleteMapper.deleteTemOrg(templeteId);//删除关联组织
		tblNbsjAuditprogramMapper.deleteInfoByTempleteId(templeteId);
		tblNbsjTargettypeMapper.deleteInfoByTempleteId(templeteId);//删除左目录
		tblNbsjTempleteMapper.deleteInfoById(templeteId);//删除模板
		return ResponseFormat.retParam(1,70003,null);
	}
}
