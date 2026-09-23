package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsFgldhz;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wystan
 * @description 针对表【TBL_YQNS_FGLDHZ(分管领导汇总表)】的数据库操作Mapper
 * @Entity TblYqnsFgldhz
 */
public interface TblYqnsFgldhzMapper extends BaseMapper<TblYqnsFgldhz> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsFgldhzMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsFgldhz> pageInfo, TblYqnsFgldhz vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsFgldhzMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsFgldhz> selectListByPageInfo(PageInfo<TblYqnsFgldhz> pageInfo, TblYqnsFgldhz vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_FGLDHZ_ATT " +
            " WHERE fgldhzid = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_FGLDHZ_ATT WHERE fgldhzid = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_FGLDHZ_ATT(fgldhzid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
}




