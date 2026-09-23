package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.Tree;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;
import com.huabo.audit.oracle.mapper.TblTargetTypeMapper;
import com.huabo.audit.service.TblTargetTypeService;

@Service
public class TblTargetTypeServiceImpl implements TblTargetTypeService {
	
	@Autowired
	private TblTargetTypeMapper tblTargetTypeMapper;

	@Override
	public boolean saveBatch(Collection<TblTargetTypeEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblTargetTypeEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblTargetTypeEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblTargetTypeEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblTargetTypeEntity getOne(Wrapper<TblTargetTypeEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblTargetTypeEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblTargetTypeEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseMapper<TblTargetTypeEntity> getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblTargetTypeEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblTargetTypeEntity> getRoot(BigDecimal tempId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount(BigDecimal tempId, BigDecimal targetId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteByTempId(BigDecimal tempId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblTargetTypeEntity> getNodeListByUser(BigDecimal projectId, BigDecimal staffId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblTargetTypeEntity> getNodeList(BigDecimal projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblTargetTypeEntity> findByAllMBs(String tempid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblTargetTypeEntity> findByALLPatrnt(String parentid) {
		return this.tblTargetTypeMapper.selectListByParentId(parentid);
	}

	@Override
	public void deleteByZY(String tempId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tree> getRootMb(BigDecimal tempId, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tree> getTreeMb(BigDecimal parentId, BigDecimal tempId, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tree> getTreeByUserMb(BigDecimal tempId, BigDecimal projectId, BigDecimal staffId, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tree> getTreeByUserMb(BigDecimal parentId, BigDecimal tempId, BigDecimal projectId, BigDecimal staffId,
			String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge(TblTargetTypeEntity targetType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TblTargetTypeEntity targetType) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public List<Tree> getTree(BigDecimal parentId, BigDecimal tempId, String url) {
		List<TblTargetTypeEntity> children = this.tblTargetTypeMapper.getTree(parentId);
		return getTreeNode(children,tempId,url);
	}
	
	@Override
	public List<Tree> getTreezy(BigDecimal parentId, BigDecimal tempId, String url,String topname) {
		List<TblTargetTypeEntity> children = this.tblTargetTypeMapper.getTree(parentId);
		return getTreeNodezy(children,tempId,url,topname);
	}
	
	
	@Override
	public List<TblTargetTypeEntity> getRoottype(BigDecimal tempId) {
		return tblTargetTypeMapper.getRoot(tempId);
	}
	
	
	private List<Tree> getTreeNodezy(List<TblTargetTypeEntity> nodeList,BigDecimal tempId,String url,String topname){
		List<Tree> treeList = new ArrayList<Tree>();
		for (TblTargetTypeEntity tblTargetType : nodeList) {
			Tree tree = new Tree();
			BigDecimal targetId = tblTargetType.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(tblTargetType.getTargetId());
			tree.setName(tblTargetType.getTargetName());
			tree.setType("0");
			tree.setTopName(topname);
			Integer count = this.tblTargetTypeMapper.getCount(tempId, targetId);
			if(count>0){
				List<TblTargetTypeEntity> children = this.tblTargetTypeMapper.getTree(targetId);
				List<Tree> nodezy = getTreeNodezy(children, tempId, url,topname);
				tree.setChildren(nodezy);
				tree.setIsParent(true);
				tree.setTopName(topname);
				tree.setOpen(true);
			}else{
				tree.setIsParent(false);
			}
			tree.setpId(tblTargetType.getParentId());
			if(null!=tblTargetType.getParentId()){
				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
			}else{
				tree.setUrl(url+"&tempId="+tempId);
			}
			treeList.add(tree);
		}
		return treeList;
	}
	
	
	private List<Tree> getTreeNode(List<TblTargetTypeEntity> nodeList,BigDecimal tempId,String url){
		List<Tree> treeList = new ArrayList<Tree>();
		for (TblTargetTypeEntity tblTargetType : nodeList) {
			Tree tree = new Tree();
			BigDecimal targetId = tblTargetType.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(tblTargetType.getTargetId());
			tree.setName(tblTargetType.getTargetName());
			tree.setType("0");
			Integer count = this.tblTargetTypeMapper.getCount(tempId, targetId);
			if(count>0){
				tree.setIsParent(true);
				tree.setOpen(true);
			}else{
				tree.setIsParent(false);
			}
			tree.setpId(tblTargetType.getParentId());
			if(null!=tblTargetType.getParentId()){
				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
			}else{
				tree.setUrl(url+"&tempId="+tempId);
			}
			treeList.add(tree);
		}
		return treeList;
	}
	
	
	@Override
	public List<Tree> getRoot(BigDecimal tempId, String url) {
		List<TblTargetTypeEntity> root = this.tblTargetTypeMapper.getRoot(tempId);
		return getTreeNode(root,tempId,url);
	}

	@Override
	public List<Tree> getTreeByUser(BigDecimal tempId, BigDecimal projectId, BigDecimal staffId, String url) {
		List<TblTargetTypeEntity> children =this.tblTargetTypeMapper.getChildByUser( projectId, staffId);
		return getTreeNodeByUser(children,tempId,url,projectId,staffId);
	}

	
	
	private List<Tree> getTreeNodeByUser(List<TblTargetTypeEntity> nodeList,BigDecimal tempId,String url,BigDecimal projectId,BigDecimal staffid){
		List<Tree> treeList = new ArrayList<Tree>();
		//for (TblTargetType tblTargetType : nodeList) {
		for (int i = 0;i<nodeList.size();i++) {
			TblTargetTypeEntity tblTargetType = nodeList.get(i);
			Tree tree = new Tree();
			BigDecimal targetId = tblTargetType.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(tblTargetType.getTargetId());
			tree.setName(tblTargetType.getTargetName());
			int count = this.tblTargetTypeMapper.getCount(tempId, targetId);
			List<TblTargetTypeEntity> list = tblTargetTypeMapper.getChildByUsers(tblTargetType.getParentId(), projectId, staffid);
			if(count>0 && list!=null && list.size()>0){
				tree.setIsParent(true);
			}else{
				tree.setIsParent(false);
				boolean falg=addtree(treeList, tblTargetType.getParentId(), tempId, url);
				if(falg){
					continue;
				}
			}
			tree.setpId(tblTargetType.getParentId());
			if(null!=tblTargetType.getParentId()){
				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
			}else{
				tree.setUrl("/nbsj/sjyj/def_cat_list?tempId="+tempId);
			}
			treeList.add(tree);
		}
		return treeList;
	}
	
	public boolean addtree(List<Tree> treeList,BigDecimal patentid,BigDecimal tempId,String url) {
		boolean falg=false;
		if (patentid == null || StringUtils.isEmpty(patentid.toString())){return falg;}
		List<TblTargetTypeEntity> listtrget = tblTargetTypeMapper.findByparent(patentid);
		
		TblTargetTypeEntity tblTargetType = null;
		if(listtrget!=null && listtrget.size()>0){
			tblTargetType = listtrget.get(0);
		}
		
		if(tblTargetType!=null && tblTargetType.getParentId()!=null){
			Tree tree = new Tree();
			tree.setTarget("mainFramex");
			tree.setId(tblTargetType.getTargetId());
			tree.setName(tblTargetType.getTargetName());
			tree.setIsParent(true);
			tree.setpId(tblTargetType.getParentId());
			if(null!=tblTargetType.getParentId()){
				tree.setUrl(url+"&targetId="+tblTargetType.getParentId()+"&tempId="+tempId);
			}else{
				tree.setUrl("/nbsj/sjyj/def_cat_list?tempId="+tempId);
			}
			//todo tyb判断两个同样的tree
			for (int i = 0; i < treeList.size(); i++) {
				Tree tr = treeList.get(i);
				if(tr.getId().equals(tblTargetType.getTargetId()) && tr.getName().equals(tblTargetType.getTargetName())){
					treeList.remove(tr);
				}
			}
			treeList.add(tree);
			if(tblTargetType.getParentId()!=null){
				addtree(treeList, tblTargetType.getParentId(), tempId, url);
			}
			falg=true;
		}
		return falg;
	}
	
	
	@Override
	public List<TblTargetTypeEntity> findByAllMB(String tempid) {
		return tblTargetTypeMapper.findByAllMB(tempid);
		
	}

	@Override
	public List<Tree> getTreeByUser(BigDecimal parentId, BigDecimal tempId, Integer projectId, BigDecimal staffId,
			String url) {
		// TODO Auto-generated method stub
		return null;
	}
}
