package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Attachment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface AttachmentMapper extends BaseMapper<Attachment> {

    @Select("select * from TBL_ATTACHMENT where ATTID in (select ATTID from TBL_OTHERARTICLE_ATT where OTHARTID = #{selectedruleid} )")
    List<Attachment> findTblAttachmentByOthartid(@Param("selectedruleid") String selectedruleid);
}
