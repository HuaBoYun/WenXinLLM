package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JSXM_JBQK(建设项目基本情况表)】的数据库操作Mapper
 */
public interface TblYqnsJsxmJbqkMapper extends BaseMapper<TblYqnsJsxmJbqk> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJsxmJbqkMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJsxmJbqk> pageInfo, TblYqnsJsxmJbqk vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJsxmJbqkMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJsxmJbqk> selectListByPageInfo(PageInfo<TblYqnsJsxmJbqk> pageInfo, TblYqnsJsxmJbqk vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JSXM_JBQK_ATT " +
            " WHERE jsxmjbqkid = #{jsxmjbqkid})")
    List<TblAttachment> selectAttachmentListByPk(Long jsxmjbqkid);

    @Delete("DELETE FROM TBL_YQNS_JSXM_JBQK_ATT WHERE jsxmjbqkid = #{jsxmjbqkid}")
    void deleteAttByPk(Long jsxmjbqkid);

    @Insert("INSERT INTO TBL_YQNS_JSXM_JBQK_ATT(jsxmjbqkid,attid) VALUES(#{jsxmjbqkid},#{attid})")
    void saveAtt(String jsxmjbqkid, String attid);
    
    
    @Select("SELECT count(*) FROM TBL_YQNS_COMPLETION_SET where COMPLETIONCODE=#{no} ")
    Integer selectbyHtbh(String no);
}




