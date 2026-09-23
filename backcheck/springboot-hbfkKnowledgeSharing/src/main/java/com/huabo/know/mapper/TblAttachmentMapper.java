package com.huabo.know.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.know.entity.TblAttachment;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(@Param("attid")Long attid) throws Exception;

    //知识共享-文库附件
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_ZSGX_LIBRARY_ATT  WHERE LIBRARYID = #{libraryid})")
	List<com.hbfk.entity.TblAttachment> selectTrainAttListBylibraryid(Long libraryid);
}
