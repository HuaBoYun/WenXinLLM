package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblExperienceTypeEntity;

import de.odysseus.el.tree.Tree;


public interface TblExperienceTypeService extends IService<TblExperienceTypeEntity>{
	public List<Tree> getRoot(Integer tempId,String url);

	public List<Tree> getTree(Integer parentId,Integer tempId,String url);

	public int getCount(Integer tempId, Integer targetId);

	public void deleteByTempId(BigDecimal bigDecimal);
 
	public void merge(TblExperienceTypeEntity tblExperienceType);
	public void delete(TblExperienceTypeEntity tblExperienceType);
}
