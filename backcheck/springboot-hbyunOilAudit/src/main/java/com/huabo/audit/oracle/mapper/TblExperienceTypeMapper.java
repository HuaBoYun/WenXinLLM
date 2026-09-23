package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblExperienceTypeEntity;

import de.odysseus.el.tree.Tree;

public interface TblExperienceTypeMapper extends BaseMapper<TblExperienceTypeEntity> {

	@Select("SELECT * from TBL_NBSJ_EXPERIENCE WHERE TEMPID= #{tempId} AND URL = #{url}")
	List<Tree> getRoot(String tempId, String url);
	
	@Select("SELECT * from TBL_NBSJ_EXPERIENCE WHERE TEMPID= #{tempId} AND URL = #{url} AND PARENTID = #{parentId}")
	List<Tree> getTree(String parentId,String tempId, String url);
	
	@Select("SELECT COUNT(*) from TBL_NBSJ_EXPERIENCE WHERE TEMPID= #{tempId} AND TARGETID = #{targetId}")
	int getCount(String tempId,String targetId);
	
	@Delete("DELETE from TBL_NBSJ_EXPERIENCE WHERE TEMPID= #{tempId}")
	int deleteByTempId(String tempId);
	
	@Delete("DELETE from TBL_NBSJ_EXPERIENCE WHERE TARGETID= #{targetid}")
	int delete(String targetid);
	
}	

