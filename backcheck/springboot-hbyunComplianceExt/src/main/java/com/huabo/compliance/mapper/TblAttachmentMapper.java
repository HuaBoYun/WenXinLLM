package com.huabo.compliance.mapper;
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblAttachment;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {


     @Select("${sql}")
     List<TblAttachment> getListBySql(@Param("sql") String sql);
     
     @SelectProvider(method="selectListByPageInfo",type=TblAttachmentMapperSqlConfig.class)
     @Results({
     	@Result(column="ATTNAME",property="attname"),
     	@Result(column="ATTSIZE",property="attsize"),
     	@Result(column="UPLOADTIME",property="uploadtime"),
     	@Result(column="UPLOADER",property="uploader"),
     })
 	List<TblAttachment> selectListByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,Integer orgid) throws Exception;
     
   //==附件列表
 	@SelectProvider(method="selectCountByPageInfo",type=TblAttachmentMapperSqlConfig.class)
    	Integer selectCountByPageInfo(PageInfo<TblAttachment> pageInfo, String attname, Integer orgid) throws Exception;
 	
 	@UpdateProvider(method = "updateTblBugsInTblAttachment", type = TblAttachmentMapperSqlConfig.class)
    void updateTblBugsInTblAttachment(TblAttachment attachment);


	@InsertProvider(type=TblAttachmentMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblAttachment tblAttachmentEntity) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE attid=#{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid);
	
}
