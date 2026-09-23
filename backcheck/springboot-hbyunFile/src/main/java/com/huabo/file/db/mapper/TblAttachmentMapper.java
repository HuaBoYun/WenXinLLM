package com.huabo.file.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.file.db.entity.TblAttachment;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {
	
	@Select("SELECT LEVELID FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 4 AND LEVELNAME =#{attachmentLevel}")
    BigDecimal selectattachmentLevel(@io.lettuce.core.dynamic.annotation.Param("attachmentLevel")String attachmentLevel) throws Exception;

    @Select("SELECT COUNT(*) FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 2 AND LEVELNAME = #{formlevel} AND  SECRECYMENUSCOPE LIKE concat('%',#{attachmentLevelId},'%')")
    Integer selectattachmentList(@io.lettuce.core.dynamic.annotation.Param("formlevel")String formlevel, @io.lettuce.core.dynamic.annotation.Param("attachmentLevelId")String attachmentLevelId) throws Exception;
	
}
