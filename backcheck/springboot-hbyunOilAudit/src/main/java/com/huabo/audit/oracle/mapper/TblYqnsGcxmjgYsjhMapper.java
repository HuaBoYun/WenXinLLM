package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsGcxmjgYsjh;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMJG_YSJH(工程项目竣工验收计划)】的数据库操作Mapper
 * @Entity TblYqnsGcxmjgYsjh
 */
public interface TblYqnsGcxmjgYsjhMapper extends BaseMapper<TblYqnsGcxmjgYsjh> {
	
    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsGcxmjgYsjhMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsGcxmjgYsjhMapperSqlConfig.class)
    @Results({
            @Result(column = "htbh", property = "htbh"),
            @Result(column = "ITEMCOUNT", property = "itemCount"),
    })
    List<TblYqnsGcxmjgYsjh> selectListByPageInfo(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception;
    
    @SelectProvider(method = "selectListByPageInfoDraftPlan", type = TblYqnsGcxmjgYsjhMapperSqlConfig.class)
    @Results({
            @Result(column = "htbh", property = "htbh"),
            @Result(column = "ITEMCOUNT", property = "itemCount"),
    })
    List<TblYqnsGcxmjgYsjh> selectListByPageInfoDraftPlan(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception;

    @SelectProvider(method = "selectCountByPageInfoDraftPlan", type = TblYqnsGcxmjgYsjhMapperSqlConfig.class)
	Integer selectCountByPageInfoDraftPlan(PageInfo<TblYqnsGcxmjgYsjh> pageInfo, TblYqnsGcxmjgYsjh vo) throws Exception;
    

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_GCXMJG_YSJH_ATT " +
            " WHERE gcxmjgysjhid = #{gcxmjgysjhid})")
    List<TblAttachment> selectAttachmentListByPk(Long gcxmjgysjhid);

    @Delete("DELETE FROM TBL_YQNS_GCXMJG_YSJH_ATT WHERE gcxmjgysjhid = #{gcxmjgysjhid}")
    void deleteAttByPk(Long gcxmjgysjhid);

    @Insert("INSERT INTO TBL_YQNS_GCXMJG_YSJH_ATT(gcxmjgysjhid,attid) VALUES(#{gcxmjgysjhid},#{attid})")
    void saveAtt(String gcxmjgysjhid, String attid);

	@Select("SELECT MAX(GCXMJGYSJHNO) FROM TBL_YQNS_GCXMJG_YSJH WHERE GCXMJGYSJHNO LIKE '${currentYear}%' ")
	BigDecimal selectMaxAutoNo(Integer currentYear) throws Exception;

    @SelectProvider(method = "selectListByExport", type = TblYqnsGcxmjgYsjhMapperSqlConfig.class)
    @Results({
            @Result(column = "htbh", property = "htbh"),
    })
	List<TblYqnsGcxmjgYsjh> selectListByExport(TblYqnsGcxmjgYsjh vo) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_GCXMJG_YSJH WHERE GCXMJGYSJHNO = #{gcxmjgysjhNo}")
	TblYqnsGcxmjgYsjh selectEntityByNo(BigDecimal gcxmjgysjhNo) ;

}




