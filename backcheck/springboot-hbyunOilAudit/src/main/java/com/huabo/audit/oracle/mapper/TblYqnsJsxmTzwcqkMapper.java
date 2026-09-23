package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JSXM_TZWCQK(建设项目投资完成情况)】的数据库操作Mapper
 * @Entity TblYqnsJsxmTzwcqk
 */
public interface TblYqnsJsxmTzwcqkMapper extends BaseMapper<TblYqnsJsxmTzwcqk> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJsxmTzwcqk> pageInfo, TblYqnsJsxmTzwcqk vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJsxmTzwcqk> selectListByPageInfo(PageInfo<TblYqnsJsxmTzwcqk> pageInfo, TblYqnsJsxmTzwcqk vo) throws Exception;
 
    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JSXM_TZWCQK_ATT " +
            " WHERE jsxmtzwcqkid = #{jsxmtzwcqkid})")
    List<TblAttachment> selectAttachmentListByPk(Long jsxmtzwcqkid);

    @Delete("DELETE FROM TBL_YQNS_JSXM_TZWCQK_ATT WHERE jsxmtzwcqkid = #{jsxmtzwcqkid}")
    void deleteAttByPk(Long jsxmjbqkid);

    @Insert("INSERT INTO TBL_YQNS_JSXM_TZWCQK_ATT(jsxmtzwcqkid,attid) VALUES(#{jsxmtzwcqkid},#{attid})")
    void saveAtt(String jsxmtzwcqkid, String attid);

    @SelectProvider(method = "selectListByjhcgGlId", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
	List<TblYqnsJsxmTzwcqk> selectListByjhcgGlId(BigDecimal id, String tbdwName);
    
    @SelectProvider(method = "selectListByIdall", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
   	List<TblYqnsJsxmTzwcqk> selectListByIdall(BigDecimal id) throws Exception;
    
     
    @Update("UPDATE TBL_YQNS_JSXM_TZWCQK SET RWIDS = #{ryids},RWNAMES = #{rynames} WHERE JSXMTZWCQKID = #{jsxmtzwcqkid}")
    void rwfpry(String jsxmtzwcqkid, String ryids,String rynames);
    
    
    @Delete("DELETE FROM TBL_YQNS_JSXM_TZWCQK WHERE PARENTID = #{jsxmtzwcqkid}")
    void deleteparentid(String jsxmjbqkid);
    
    @SelectProvider(method = "selectListByIdmyrw", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class) 
   	List<TblYqnsJsxmTzwcqk> selectListByIdmyrw(BigDecimal id,BigDecimal staffid) throws Exception;

    @SelectProvider(method = "selectListByjhchugGlId", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
    List<TblYqnsJsxmTzwcqk> selectListByjhchugGlId(BigDecimal id, BigDecimal relaId);

    @SelectProvider(method = "selectListByjhzgGlId", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
    List<TblYqnsJsxmTzwcqk> selectListByjhzgGlId(BigDecimal id, BigDecimal relaId);

    @Select("SELECT * FROM TBL_YQNS_JSXM_TZWCQK WHERE JSXMTZWCQKID IN (SELECT FORMID FROM TBL_YQNS_JHZGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JH_GL WHERE ID IN (SELECT GLJHXMID FROM TBL_YQNS_ENGIN_AUDIT_PROJECT))) AND (ISSYNC IS NULL OR ISSYNC != 1)")
	List<TblYqnsJsxmTzwcqk> selectSyncListData() throws Exception;
    
    @Select("SELECT count(*) FROM TBL_YQNS_COMPLETION_SET where COMPLETIONCODE=#{no} ")
    Integer selectbyHtbh(String no);
 
    
    
    @SelectProvider(method = "selectListByIdallcf", type = TblYqnsJsxmTzwcqkMapperSqlConfig.class)
   	List<TblYqnsJsxmTzwcqk> selectListByIdallcf(BigDecimal id) throws Exception;
}




