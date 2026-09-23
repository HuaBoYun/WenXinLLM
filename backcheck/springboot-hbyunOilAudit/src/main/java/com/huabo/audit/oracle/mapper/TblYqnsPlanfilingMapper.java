package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsPlanfiling;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsPlanfilingMapper extends BaseMapper<TblYqnsPlanfiling> {


    @Delete("DELETE FROM TBL_YQNS_PLANFILING_ATT WHERE PLANFILINGID =#{planfilingid}")
    void deleteAttById(BigDecimal planfilingid);

    @Insert("INSERT INTO TBL_YQNS_PLANFILING_ATT (ATTID,PLANFILINGID) VALUES (#{attId},#{planfilingid})")
    void saveAtt(String planfilingid, String attId);

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_PLANFILING_ATT " +
            " WHERE PLANFILINGID = #{planfilingid})")
    List<TblAttachment> selectAttachmentListByPk(BigDecimal planfilingid);
}
