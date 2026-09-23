package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkFymx;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author Administrator
* @description 针对表【TBL_YQNS_JSXM_TZWCQK_FYMX(建设项目投资完成情况费用明细表)】的数据库操作Mapper
* @Entity TblYqnsJsxmTzwcqkFymx
*/
public interface TblYqnsJsxmTzwcqkFymxMapper extends BaseMapper<TblYqnsJsxmTzwcqkFymx> {
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsJsxmTzwcqkFymxMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsJsxmTzwcqkFymxMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsJsxmTzwcqkFymx> selectListByPageInfo(PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo, TblYqnsJsxmTzwcqkFymx vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_JSXM_TZWCQK_FYMX_ATT " +
            " WHERE jsxmtzwcqkfymxid = #{jsxmtzwcqkfymxid})")
    List<TblAttachment> selectAttachmentListByPk(Long jsxmtzwcqkfymxid);

    @Delete("DELETE FROM TBL_YQNS_JSXM_TZWCQK_FYMX_ATT WHERE jsxmtzwcqkfymxid = #{jsxmtzwcqkfymxid}")
    void deleteAttByPk(Long jsxmtzwcqkfymxid);

    @Insert("INSERT INTO TBL_YQNS_JSXM_TZWCQK_FYMX_ATT(jsxmtzwcqkfymxid,attid) VALUES(#{jsxmtzwcqkfymxid},#{attid})")
    void saveAtt(String jsxmtzwcqkfymxid, String attid);
}




