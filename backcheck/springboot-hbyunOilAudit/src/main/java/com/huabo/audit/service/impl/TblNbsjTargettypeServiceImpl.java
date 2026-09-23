package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.common.ResultCode;
import com.huabo.audit.exception.CommercialException;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditprogramMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTargettypeMapper;
import com.huabo.audit.service.TblNbsjTargettypeService;

import cn.hutool.core.util.StrUtil;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-21
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjTargettypeServiceImpl extends ServiceImpl<TblNbsjTargettypeMapper, TblNbsjTargettypeEntity> implements TblNbsjTargettypeService {
    @Autowired
    private TblNbsjTargettypeMapper tblNbsjTargettypeMapper;
    @Autowired
    private TblNbsjAuditprogramMapper tblNbsjAuditprogramMapper;
    
    @Resource
    private UserProvider userProvider;

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    @Override
    public LambdaQueryWrapper<TblNbsjTargettypeEntity> onSelectWhere(TblNbsjTargettypeEntity model) {
        LambdaQueryWrapper<TblNbsjTargettypeEntity>  queryWrapper=new QueryWrapper<TblNbsjTargettypeEntity>().lambda();
        if (model == null) {
            return queryWrapper;
        }

        queryWrapper
                .eq(model.getTargetId()!=null && model.getTargetId()!=null,TblNbsjTargettypeEntity::getTargetId, model.getTargetId())
                .eq(model.getTargetName()!=null,TblNbsjTargettypeEntity::getTargetName, model.getTargetName())
                .eq(model.getTargetDesc()!=null,TblNbsjTargettypeEntity::getTargetDesc, model.getTargetDesc())
                .eq(model.getParentId()!=null,TblNbsjTargettypeEntity::getParentId, model.getParentId())
                .eq(model.getCreateTime()!=null,TblNbsjTargettypeEntity::getCreateTime, model.getCreateTime())
                .eq(model.getUpdateTime()!=null,TblNbsjTargettypeEntity::getUpdateTime, model.getUpdateTime())
                .eq(model.getTempId()!=null,TblNbsjTargettypeEntity::getTempId, model.getTempId())
                .eq(model.getStatus()!=null,TblNbsjTargettypeEntity::getStatus, model.getStatus())
        ;

        return queryWrapper;
    }

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    @Override
    public boolean saveTblNbsjTargettype(TblNbsjTargettypeEntity model) {
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
    public boolean updateTblNbsjTargettype(TblNbsjTargettypeEntity model) {
        if(model.getTargetId()==null || model.getTargetId()==null){
            throw new CommercialException(ResultCode.BIZ_ERROR,"teamId不能为空！");
        }
        //在保存和更新之前的操作 同步字段id、name
        beforSaveandUpdate(model);

        return updateById(model);
    }

    @Override
    public List<TblNbsjTargettypeEntity> selectByTeamId(Integer teamId) {
        LambdaQueryWrapper<TblNbsjTargettypeEntity>  queryWrapper=new QueryWrapper<TblNbsjTargettypeEntity>().lambda();
        queryWrapper.eq(teamId!=null,TblNbsjTargettypeEntity::getTempId, teamId);
        return list(queryWrapper);
    }

    @Override
    public List<TblNbsjTargettypeEntity> selectByParentId(Integer parentId) {
        LambdaQueryWrapper<TblNbsjTargettypeEntity>  queryWrapper=new QueryWrapper<TblNbsjTargettypeEntity>().lambda();
        queryWrapper.eq(parentId!=null,TblNbsjTargettypeEntity::getParentId, parentId);
        return list(queryWrapper);
    }

    @Override
    public Integer selectCountByTempIdAndPid(Integer tempId, Integer parentId) {
        LambdaQueryWrapper<TblNbsjTargettypeEntity>  queryWrapper=new QueryWrapper<TblNbsjTargettypeEntity>().lambda();
        queryWrapper.eq(tempId!=null,TblNbsjTargettypeEntity::getTempId, tempId)
        .eq(parentId!=null,TblNbsjTargettypeEntity::getParentId, parentId);
        return (int)count(queryWrapper);
    }

    /**
    * @description: 在保存和更新之前的操作 同步字段id、name
    * @param model:
    * @return: void
    * @author: ziyao
    * @date: 2022-04-21
    */
    private void beforSaveandUpdate(TblNbsjTargettypeEntity model){

    }
	public JsonBean mergeNbsjTargettypInfo(TblNbsjTargettypeEntity target, String token) throws Exception{
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(target.getTargetId()!= null) {
			//修改；
			target.setUpdateTime(new Date());
			tblNbsjTargettypeMapper.updateEntity(target);
		}else {
			//新增；
			target.setCreateTime(new Date());
			target.setStatus(0);
			target.setTempId(target.getTempId());
			target.setUpdateTime(new Date());
			tblNbsjTargettypeMapper.insertEntity(target);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("target",target);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public JsonBean selectInfo(String targetId) throws Exception{
		//查询
		TblNbsjTargettypeEntity selectById = tblNbsjTargettypeMapper.findbyid(targetId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("target",selectById);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public JsonBean deleteInfo(String targetid) throws Exception{
		tblNbsjAuditprogramMapper.deleteInfoByTargetId(targetid);
		tblNbsjTargettypeMapper.deleteInfoById(targetid);
		return ResponseFormat.retParam(1,70003,null);
	}
	private List<Tree> getTreeNode(List<TblNbsjTargettypeEntity> nodeList,BigDecimal tempId,String url) throws Exception{
		List<Tree> treeList = new ArrayList<Tree>();
		for (TblNbsjTargettypeEntity tblTargetType : nodeList) {
			Tree tree = new Tree();
			BigDecimal targetId = tblTargetType.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(new BigDecimal(tblTargetType.getTargetId().toString()));
			tree.setName(tblTargetType.getTargetName());
			tree.setType("0");
			int count = this.tblNbsjTargettypeMapper.getCount(tempId, targetId);
			if(count>0){
				tree.setIsParent(true);
				tree.setOpen(true);
			}else{
				tree.setIsParent(false);
			}
			tree.setpId(null);
			if(tblTargetType.getParentId()!=null) {
				tree.setpId(new BigDecimal(tblTargetType.getParentId().toString()));
			}
//			if(null!=tblTargetType.getParentId()){
//				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
//			}else{
//				tree.setUrl(url+"&tempId="+tempId);
//			}
			treeList.add(tree);
		}
		return treeList;
	}
	@Override
	public JsonBean findNbsjTargetTree(String templeteId,String nodeId) throws Exception {
		BigDecimal tempId = new BigDecimal(templeteId);
		//查询  树结构
		List<Tree> treeNode=null;
		List<TblNbsjTargettypeEntity> root=new ArrayList<TblNbsjTargettypeEntity>();
        if (StrUtil.isBlank(nodeId)) {
        	TblNbsjTargettypeEntity selectFirstTarget = this.tblNbsjTargettypeMapper.selectFirstTarget(templeteId);
    		root.add(selectFirstTarget);
        	//List<TblNbsjTargettypeEntity> selectTargetByTempleteId = tblNbsjTargettypeMapper.selectTargetByTempleteId(templeteId);
        	//root=selectTargetByTempleteId;
    		treeNode = this.getTreeNode(root,tempId,"");
        } else {
    		List<TblNbsjTargettypeEntity> children = this.tblNbsjTargettypeMapper.selectByParentId(nodeId);
    		treeNode = this.getTreeNode(children,tempId,"");
        }
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("targetTree",treeNode);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	private void setNBsjTargetChildernTree(List<TblNbsjTargettypeEntity> tree) throws Exception {
		List<TblNbsjTargettypeEntity> treeChildren = null;
		for (TblNbsjTargettypeEntity target : tree) {
			treeChildren = tblNbsjTargettypeMapper.selectByParentId(target.getTargetId().toString());
			if(treeChildren != null) {
				this.setNBsjTargetChildernTree(treeChildren);
				target.setChildrenList(treeChildren);
			}
		}
	}
	
	
	@Override
	public String getTargetTree(String templeteId) throws Exception {
		String result = null;
       TblNbsjTargettypeEntity firstTargetId = tblNbsjTargettypeMapper.selectFirstTarget(templeteId);//根节点
       List<TblNbsjTargettypeEntity> tree = tblNbsjTargettypeMapper.selectByParentId(firstTargetId.getTargetId().toString());//其他
       Map<String, Object> resultMap = new HashMap<String, Object>(0);
       this.setNBsjTargetChildernTree(tree);
       resultMap.put("tree", tree);
       resultMap.put("targetId",firstTargetId.getTargetId());
       resultMap.put("treeName", firstTargetId.getTargetName());
       resultMap.put("targetFrame", "mainFramex");
       JSONObject json = new JSONObject(resultMap);
       result = json.toString();
       return result;
	}

	
}
