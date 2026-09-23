package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblOaDocumentList;
import com.huabo.contract.mappersql.TblOaDocumentListSqlConfig;

 
@Mapper
public interface TblOaDocumentListMapper extends BaseMapper<TblOaDocumentList> {

	@Select("SELECT * FROM  TBL_OA_DOCUMENTLIST  WHERE CONTRACTID = #{contractid} ")
	List<TblOaDocumentList> selectDocumentList(BigDecimal contractid);

	@Delete("DELETE FROM TBL_OA_DOCUMENTLIST WHERE DOCUMENTID = #{documentId}")
	void deleteDocumentId(BigDecimal documentId);

	@InsertProvider(type = TblOaDocumentListSqlConfig.class, method = "insertDocument")
	@Options(useGeneratedKeys = true, keyProperty = "documentId", keyColumn = "DOCUMENTID")
	void insertDocument(TblOaDocumentList doc);
}
