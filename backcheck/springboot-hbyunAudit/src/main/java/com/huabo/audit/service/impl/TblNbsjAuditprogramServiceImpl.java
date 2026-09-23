package com.huabo.audit.service.impl;

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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.common.ResultCode;
import com.huabo.audit.exception.CommercialException;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditprogramMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTargettypeMapper;
import com.huabo.audit.service.TblNbsjAuditprogramService;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-20
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjAuditprogramServiceImpl extends ServiceImpl<TblNbsjAuditprogramMapper, TblNbsjAuditprogramEntity> implements TblNbsjAuditprogramService {
    @Autowired
    private TblNbsjAuditprogramMapper tblNbsjAuditprogramMapper;
    @Autowired
    private TblNbsjTargettypeMapper tblNbsjTargettypeMapper;
    
    @Resource
    private UserProvider userProvider;

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    @Override
    public LambdaQueryWrapper<TblNbsjAuditprogramEntity> onSelectWhere(TblNbsjAuditprogramEntity model) {
        LambdaQueryWrapper<TblNbsjAuditprogramEntity>  queryWrapper=new QueryWrapper<TblNbsjAuditprogramEntity>().lambda();
        if (model == null) {
            return queryWrapper;
        }

        queryWrapper
                .eq(model.getStatus()!=null,TblNbsjAuditprogramEntity::getStatus, model.getStatus())
                .eq(model.getControl()!=null,TblNbsjAuditprogramEntity::getControl, model.getControl())
        ;

        return queryWrapper;
    }

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    @Override
    public boolean saveTblNbsjAuditprogram(TblNbsjAuditprogramEntity model) {
        //在保存和更新之前的操作 同步字段id、name
        beforSaveandUpdate(model);

        return save(model);
    }

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    @Override
    public boolean updateTblNbsjAuditprogram(TblNbsjAuditprogramEntity model) {
        if(model.getProgramId()==null || model.getProgramId().intValue()==0){
            throw new CommercialException(ResultCode.BIZ_ERROR,"teamId不能为空！");
        }
        //在保存和更新之前的操作 同步字段id、name
        beforSaveandUpdate(model);

        return updateById(model);
    }

    @Override
    public List<TblNbsjAuditprogramEntity> selectByTempId(Integer tempId) {
        LambdaQueryWrapper<TblNbsjAuditprogramEntity>  queryWrapper=new QueryWrapper<TblNbsjAuditprogramEntity>().lambda();
        queryWrapper.eq(tempId!=null,TblNbsjAuditprogramEntity::getTempId, tempId);
        return list(queryWrapper);
    }

    @Override
    public List<TblNbsjAuditprogramEntity> selectByTargetId(Integer targetId) {
        LambdaQueryWrapper<TblNbsjAuditprogramEntity>  queryWrapper=new QueryWrapper<TblNbsjAuditprogramEntity>().lambda();
        queryWrapper.eq(targetId!=null,TblNbsjAuditprogramEntity::getTargetId, targetId);
        return list(queryWrapper);
    }

    /**
    * @description: 在保存和更新之前的操作 同步字段id、name
    * @param model:
    * @return: void
    * @author: ziyao
    * @date: 2022-04-20
    */
    private void beforSaveandUpdate(TblNbsjAuditprogramEntity model){

    }

	@Override
	public JsonBean mergeTblNbsjAuditprogramInfo(TblNbsjAuditprogramEntity program, String token) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(program.getProgramId()!= null) {
			//修改；
			program.setUpdateTime(new Date());
			tblNbsjAuditprogramMapper.updateEntity(program);
		}else {
			//新增；
			program.setCreateTime(new Date());
			//查询模板
			TblNbsjTargettypeEntity target = tblNbsjTargettypeMapper.findbyid(program.getTargetId().toString());
			program.setTempId(target.getTempId());
			program.setStatus(target.getStatus());
			program.setProgramId(RandomUtil.uuBigDecimalId());
			tblNbsjAuditprogramMapper.insert(program);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("program",program);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean selectInfo(String programId) {
		//查询
		TblNbsjAuditprogramEntity program = tblNbsjAuditprogramMapper.findbyid(programId);
		//TblNbsjAuditprogramEntity program = tblNbsjAuditprogramMapper.selectById(programId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("program",program);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteInfo(String programId) {
		//查询
		tblNbsjAuditprogramMapper.deleteInfoById(programId);
		return ResponseFormat.retParam(1,70003,null);
	}

	@Override
	public JsonBean selectTblNbsjAuditprogramListByPageInfo(String token, Integer pageNumber, Integer pageSize,
			String templeteId, String targetId) throws Exception {
    	
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
    	
    	PageInfo<TblNbsjAuditprogramEntity> pageInfo = new PageInfo<TblNbsjAuditprogramEntity>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjAuditprogramMapper.selectTblNbsjAuditprogramListByPageInfo(pageInfo,templeteId,targetId));
    	pageInfo.setTotalRecord(this.tblNbsjAuditprogramMapper.selectTblNbsjAuditprogramCountByPageInfo(pageInfo,templeteId,targetId));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

}
