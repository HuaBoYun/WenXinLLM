package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjbgdg;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJBG_SJBGDG(审计报告定稿表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjbgSjbgdg
 */
public interface TblYqnsSjbgSjbgdgMapper extends BaseMapper<TblYqnsSjbgSjbgdg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjbgSjbgdgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjbgSjbgdg> pageInfo, TblYqnsSjbgSjbgdg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjbgSjbgdgMapperSqlConfig.class)
    @Results({
            @Result(column = "sjbgdgid", property = "sjbgdgid"),
            @Result(column = "document", property = "document"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjbgSjbgdg> selectListByPageInfo(PageInfo<TblYqnsSjbgSjbgdg> pageInfo, TblYqnsSjbgSjbgdg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJBG_SJBGDGATTACH " +
            " WHERE SJBGDGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJBG_SJBGDGATTACH WHERE SJBGDGID = #{id}")
    void deleteAttByPk(String id);

    @Insert("INSERT INTO TBL_YQNS_SJBG_SJBGDGATTACH(sjbgdgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @Select("SELECT AUDIT_ORG_ID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID IN (SELECT PROJECT_ID FROM TBL_YQNS_SJBG_SJBGDG WHERE SJBGDGID = #{sjbgdgid})")
	String selectAuditOrgIdStrs(@Param("sjbgdgid") BigDecimal sjbgdgid) throws Exception;

    @Select("SELECT ORGID,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${auditOrgId})")
	List<TblOrganization> selectAuditOrgListByIds(@Param("auditOrgId")String auditOrgId) throws Exception;
}




