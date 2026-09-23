package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.dto.TblYqnsWtzgAuditResultsStatisticsDto;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_WTZG(问题整改表)】的数据库操作Mapper
 * @createDate 2023-09-27 16:46:40
 * @Entity TblYqnsSjzgWtzg
 */
public interface TblYqnsSjzgWtzgMapper extends BaseMapper<TblYqnsSjzgWtzg> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjzgWtzg> pageInfo, TblYqnsSjzgWtzg vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
            @Result(column = "wtzgid", property = "wtzgid"),
            @Result(column = "ysnrid", property = "ysnrid"),
            @Result(column = "reportnum", property = "reportnum"),
            @Result(column = "unitname", property = "unitname"),
            @Result(column = "wtdx", property = "wtdx"),
            @Result(column = "ssms", property = "ssms"),
            @Result(column = "clyj", property = "clyj"),
            @Result(column = "wtje", property = "wtje"),
            @Result(column = "gldg", property = "gldg"),
            @Result(column = "dx", property = "dx"),
            @Result(column = "dxfgyj", property = "dxfgyj"),
            @Result(column = "zgsx", property = "zgsx"),
            @Result(column = "zgfl", property = "zgfl"),
            @Result(column = "zcss", property = "zcss"),
            @Result(column = "fxcd", property = "fxcd"),
            @Result(column = "ljzgje", property = "ljzgje"),
            @Result(column = "ljjjcg", property = "ljjjcg"),
            @Result(column = "dqzgzt", property = "dqzgzt"),
            @Result(column = "dqzgje", property = "dqzgje"),
            @Result(column = "dqzjjjcgtype", property = "dqzjjjcgtype"),
            @Result(column = "dqzjjjcg", property = "dqzjjjcg"),
            @Result(column = "dqqtjjcgtype", property = "dqqtjjcgtype"),
            @Result(column = "dqqtjjcg", property = "dqqtjjcg"),
            @Result(column = "dqzgms", property = "dqzgms"),
            @Result(column = "sfys", property = "sfys"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "zrr", property = "zrr"),
            @Result(column = "zgqk", property = "zgqk"),
            @Result(column = "zgzt", property = "zgzt"),
            @Result(column = "cjsj", property = "cjsj"),
    })
    List<TblYqnsSjzgWtzg> selectListByPageInfo(PageInfo<TblYqnsSjzgWtzg> pageInfo, TblYqnsSjzgWtzg vo) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_WTZG_ATTACH " +
            " WHERE WTZGID = #{id})")
    List<TblAttachment> selectAttachmentListByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE WTZGID = #{id}")
    void deleteAttByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_SJZG_WTZG_ATTACH(wtzgid,attid) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);

    @SelectProvider(method = "selectPageInfoList", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
    		 @Result(column = "ID", property = "tblIssueEntity.id",id = true),
    		 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
    		 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),
    		 @Result(column = "UNITORGID", property = "tblIssueEntity.unitOrgId"),
    		 @Result(column = "QUALITATIVE", property = "tblIssueEntity.qualitative"),
    		 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
    		 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),
    		 @Result(column = "MONEY", property = "tblIssueEntity.money"),
    		 @Result(column = "QUALITATIVERULE", property = "tblIssueEntity.qualitativeRule"),
    		 @Result(column = "PROBLEMQUALITATIVE", property = "tblIssueEntity.problemQualitative"),
    		 @Result(column = "HEAD", property = "tblIssueEntity.head"),
    		 @Result(column = "TIMELIMIT", property = "tblIssueEntity.timeLimit"),
    		 @Result(column = "WTZGID", property = "wtzgid"), 
    		 @Result(column = "WTZGFL", property = "wtzgfl"),
    		 @Result(column = "ZGZT", property = "zgzt"),
    		 @Result(column = "DQZGZT", property = "dqzgzt"),
    		 @Result(column = "ZGSTATUS", property = "status"),
    		 @Result(column = "DQZGJE", property = "dqzgje"),
    		 @Result(column = "DQZJJJCG", property = "dqzjjjcg"), 
    })
    List<TblYqnsSjzgWtzg> selectPageInfoList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @SelectProvider(method = "selectGzhfList", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
    		 @Result(column = "ID", property = "tblIssueEntity.id",id = true),
    		 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
    		 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),//立项单位
    		 @Result(column = "UNITORGID", property = "tblIssueEntity.unitOrgId"),
    		 @Result(column = "QUALITATIVE", property = "tblIssueEntity.qualitative"),
    		 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
    		 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),//项目名称
    		 @Result(column = "QUALITATIVERULE", property = "tblIssueEntity.qualitativeRule"),
    		 @Result(column = "PROBLEMQUALITATIVE", property = "tblIssueEntity.problemQualitative"),
    		 @Result(column = "MONEY", property = "tblIssueEntity.money"),
    		 @Result(column = "HEAD", property = "tblIssueEntity.head"),
    		 @Result(column = "TIMELIMIT", property = "tblIssueEntity.timeLimit"),
    		 @Result(column = "WTZGID", property = "wtzgid"),
    		 @Result(column = "WTZGFL", property = "wtzgfl"),
    		 @Result(column = "ZGZT", property = "zgzt"),
    		 @Result(column = "DQZGZT", property = "dqzgzt"),
    		 @Result(column = "ZGSTATUS", property = "status"),
    		 @Result(column = "HFSPSTATUS", property = "hfspstatus"),
    })
    List<TblYqnsSjzgWtzg> selectGzhfList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @Update("UPDATE TBL_YQNS_SJZG_WTZG SET STATUS = #{status} WHERE WTZGID = #{wtzgid}")
	void updateStatus(@Param("wtzgid")BigDecimal wtzgid,@Param("status") int status) throws Exception;

    @Select("INSERT INTO TBL_YQNS_SJZG_WTZG_ATTACH(WTZGID,ATTID) SELECT #{wtzgid},ATTID FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE WTZGID = #{oldWtzgId}  ")
	void copyFileRelation(@Param("wtzgid") BigDecimal wtzgid,@Param("oldWtzgId") BigDecimal oldWtzgId);

    @Select("SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE ATTID = #{attid}")
	Integer selectFileRelationCount(@Param("attid")String attid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE WTZGID = #{wtzgid} AND ATTID = #{attid}")
	void deleteFileRelation(@Param("attid")String attid,@Param("wtzgid") BigDecimal wtzgid) throws Exception;

    @Select("INSERT INTO TBL_YQNS_SJYAATT(WTZGID,ATTID) VALUES(#{wtzgid},#{attId}) ")
	void insertSjyqbcRealtion(@Param("wtzgid")BigDecimal wtzgid,@Param("attId") BigDecimal attId) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJYAATT WHERE WTZGID = #{wtzgid} AND ATTID = #{attId}")
	void deleteSjyqbcRealtion(@Param("wtzgid")BigDecimal wtzgid,@Param("attId") BigDecimal attId) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_YSNRATT " +
            " WHERE WTZGID = #{wtzgid})")
	List<TblAttachment> selectSjQyAttachmentListByPk(@Param("wtzgid") BigDecimal wtzgid);

    @Select("SELECT COUNT(0) FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE WTZGID = #{wtzgid} ")
	Integer selectSjyqAttCount(@Param("wtzgid") BigDecimal wtzgid) throws Exception;

    @Select("SELECT FLOWTYPE FROM TBL_YQNS_XMQD WHERE XMDQID = (SELECT XMDQID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID = (SELECT PROJECTID FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID = #{wtzgid}))")
	String selectFlowType(@Param("wtzgid")BigDecimal wtzgid) throws Exception;

    @Insert("INSERT INTO TBL_YQNS_WTZG_SJTZS (WTZGID,SJTZSID) VALUES (#{wtzgid},#{sjtzsid})")
	void insertWtzgSjtzs(@Param("wtzgid")BigDecimal wtzgid,@Param("sjtzsid") BigDecimal sjtzsid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_WTZG_SJTZS WHERE WTZGID = #{wtzgid} AND SJTZSID = #{sjtzsid}")
	void removeWtzgSjtzs(@Param("wtzgid")BigDecimal wtzgid,@Param("sjtzsid") BigDecimal sjtzsid) throws Exception;

    @Insert("INSERT INTO TBL_YQNS_WTZG_SJYJJDS (WTZGID,SJYJJDID) VALUES (#{wtzgid},#{sjtzsid})")
	void insertWtzgSjyjjds(@Param("wtzgid")BigDecimal wtzgid,@Param("sjtzsid") BigDecimal sjyjjdId);

    @Insert("INSERT INTO TBL_YQNS_WTZG_JJZRSJJGBG (WTZGID,SJJGBGID) VALUES (#{wtzgid},#{sjjgbgid})")
	void insertWtzgJjzrsjjgbg(@Param("wtzgid")BigDecimal wtzgid,@Param("sjjgbgid") BigDecimal sjjgbgid) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_WTZG_SJYJJDS WHERE WTZGID = #{wtzgid} AND SJYJJDID = #{sjyjjdId}")
	void removetWtzgSjyjjds(@Param("wtzgid")BigDecimal wtzgid,@Param("sjyjjdId") BigDecimal sjyjjdId) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_WTZG_JJZRSJJGBG WHERE WTZGID = #{wtzgid} AND SJJGBGID = #{sjjgbgid}")
	void removeWtzgJjzrsjjgbg(@Param("wtzgid")BigDecimal wtzgid,@Param("sjjgbgid") BigDecimal sjjgbgid) throws Exception;

    @SelectProvider(method = "selectHxzgList", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
    		 @Result(column = "ID", property = "tblIssueEntity.id",id = true),
    		 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
    		 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),//被审计单位
    		 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
    		 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),//项目名称
    		 @Result(column = "RECTPERNAME", property = "tblIssueEntity.rectPerName"),//当前处理人
    		 @Result(column = "WTZGID", property = "wtzgid"),
    		 @Result(column = "STATUS", property = "status"),//审批状态
    		 @Result(column = "ZGZT", property = "zgzt"), //整改状态
    		 @Result(column = "WZGCOUNT", property = "wzgCount"),//后续整改设计问题发现数
    		 @Result(column = "YWCCOUNT", property = "ywcCount"),//已完成整改数
    		 @Result(column = "BZGCOUNT", property = "bzgCount"),//无法整改数
    })
    List<TblYqnsSjzgWtzg> selectHxzgList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @SelectProvider(method = "selectHgzgFillInListList", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
	    	@Result(column = "ID", property = "tblIssueEntity.id",id = true),
			 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
			 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),
			 @Result(column = "UNITORGID", property = "tblIssueEntity.unitOrgId"),
			 @Result(column = "QUALITATIVE", property = "tblIssueEntity.qualitative"),
			 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
			 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),
			 @Result(column = "MONEY", property = "tblIssueEntity.money"),
			 @Result(column = "QUALITATIVERULE", property = "tblIssueEntity.qualitativeRule"),
			 @Result(column = "PROBLEMQUALITATIVE", property = "tblIssueEntity.problemQualitative"),
			 @Result(column = "HEAD", property = "tblIssueEntity.head"),
			 @Result(column = "TIMELIMIT", property = "tblIssueEntity.timeLimit"),
			 @Result(column = "WTZGID", property = "wtzgid"),
			 @Result(column = "WTZGFL", property = "wtzgfl"),
			 @Result(column = "ZGZT", property = "zgzt"),
			 @Result(column = "DQZGZT", property = "dqzgzt"),
			 @Result(column = "ZGSTATUS", property = "status"),
			 @Result(column = "DQZGJE", property = "dqzgje"),
			 @Result(column = "DQZJJJCG", property = "dqzjjjcg"),
			 @Result(column = "HFSPSTATUS", property = "hfspstatus"),
			 @Result(column = "HXSPSTATUS", property = "hxspstatus"),
    		 @Result(column = "DQZJJJCGTYPE", property = "dqzjjjcgtype"),
    })
    List<TblYqnsSjzgWtzg> selectHgzgFillInListList(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @Select("SELECT SUM(DQZGJE) AS DQZGJE,SUM(DQZJJJCG) AS DQZJJJCG , SUM(DQQTJJCG) AS DQQTJJCG FROM TBL_YQNS_SJZG_WTZG WHERE WTZGID IN (" + 
    		" SELECT TYIR.WTZGID FROM TBL_YQNS_ISSUESRECORD TYIR JOIN ( SELECT ISSUESID,MAX(VERSION) AS VERSION FROM TBL_YQNS_ISSUESRECORD GROUP BY ISSUESID ) TYIRV ON TYIR.ISSUESID = TYIRV.ISSUESID AND TYIR.VERSION = TYIRV.VERSION WHERE WTZGID IN (SELECT WTZGID FROM TBL_YQNS_SJZG_WTZG WHERE PROJECTID = #{projectId}))")
    @Results({
    	@Result(column = "DQZGJE", property = "dqzgje"),
		 @Result(column = "DQZJJJCG", property = "dqzjjjcg"),
		 @Result(column = "DQQTJJCG", property = "dqqtjjcg"),
    })
	TblYqnsSjzgWtzg selectTotalMoney(@Param("projectId")BigDecimal projectId) throws Exception;

    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
            " FROM TBL_ATTACHMENT WHERE" +
            " ATTID IN (SELECT ATTID FROM TBL_YQNS_SJYAATT " +
            " WHERE WTZGID = #{id})")
	List<TblAttachment> selectAttachmentListYsByPk(@Param("id") String id);

    @SelectProvider(method = "selectRectificationLedger", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
    		 @Result(column = "ID", property = "tblIssueEntity.id",id = true),
    		 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
    		 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),//被审计单位
    		 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
    		 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),//项目名称
    		 @Result(column = "RECTPERNAME", property = "tblIssueEntity.rectPerName"),//当前处理人
    		 @Result(column = "WTZGID", property = "wtzgid"),
    		 @Result(column = "STATUS", property = "status"),//审批状态
    		 @Result(column = "ZGZT", property = "zgzt"), //整改状态
    		 @Result(column = "WZGCOUNT", property = "wzgCount"),//后续整改设计问题发现数
    		 @Result(column = "YWCCOUNT", property = "ywcCount"),//已完成整改数
    		 @Result(column = "BZGCOUNT", property = "bzgCount"),//无法整改数
    })
    List<TblYqnsSjzgWtzg> selectRectificationLedger(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @SelectProvider(method = "rectificationLedgerFillin", type = TblYqnsSjzgWtzgMapperSqlConfig.class)
    @Results({
	    	@Result(column = "ID", property = "tblIssueEntity.id",id = true),
			 @Result(column = "ISSUENUMBER", property = "tblIssueEntity.issueNumber"),
			 @Result(column = "UNITNAME", property = "tblIssueEntity.unitName"),
			 @Result(column = "UNITORGID", property = "tblIssueEntity.unitOrgId"),
			 @Result(column = "QUALITATIVE", property = "tblIssueEntity.qualitative"),
			 @Result(column = "PROJECTID", property = "tblIssueEntity.projectId"),
			 @Result(column = "PROJECTNAME", property = "tblIssueEntity.projectName"),
			 @Result(column = "MONEY", property = "tblIssueEntity.money"),
			 @Result(column = "QUALITATIVERULE", property = "tblIssueEntity.qualitativeRule"),
			 @Result(column = "PROBLEMQUALITATIVE", property = "tblIssueEntity.problemQualitative"),
			 @Result(column = "HEAD", property = "tblIssueEntity.head"),
			 @Result(column = "TIMELIMIT", property = "tblIssueEntity.timeLimit"),
			 @Result(column = "WTZGID", property = "wtzgid"), 
			 @Result(column = "WTZGFL", property = "wtzgfl"),
			 @Result(column = "ZGZT", property = "zgzt"),
			 @Result(column = "DQZGZT", property = "dqzgzt"),
			 @Result(column = "ZGSTATUS", property = "status"),
			 @Result(column = "DQZGJE", property = "dqzgje"),
			 @Result(column = "DQZJJJCG", property = "dqzjjjcg"),
			 @Result(column = "HFSPSTATUS", property = "hfspstatus"),
    		 @Result(column = "DQZJJJCGTYPE", property = "dqzjjjcgtype"),
    })
    List<TblYqnsSjzgWtzg> rectificationLedgerFillin(TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues);

    @Select("SELECT TYSW.*,TYIR.VERSION,TYIR.RECTPERSON,TYIR.RECTPERSONNAME FROM TBL_YQNS_SJZG_WTZG TYSW LEFT JOIN TBL_YQNS_ISSUESRECORD TYIR ON TYSW.WTZGID = TYIR.WTZGID WHERE TYIR.ISSUESID = #{issuesId} ORDER BY TYIR.VERSION ASC ")
	List<TblYqnsSjzgWtzg> getHistoryVersion(@Param("issuesId")BigDecimal issuesId,@Param("wtzgid") BigDecimal wtzgid) throws Exception;


	/**
	 * 问题整改-审计成果统计
	 * @param entity
	 * @return
	 */
	List<TblYqnsWtzgAuditResultsStatisticsDto> 	selectWtzgAuditResultsStatistics(TblYqnsWtzgAuditResultsStatisticsDto entity);

}




