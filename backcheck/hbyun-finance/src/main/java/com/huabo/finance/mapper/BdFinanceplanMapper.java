package com.huabo.finance.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.mappersql.BdFinanceplanMapperSqlConfig;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vr.BdFinanceplanVr;

/**
 * <p>
 * 公司采集配置方案信息表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
public interface BdFinanceplanMapper extends BaseMapper<BdFinanceplan> {

	@Select("SELECT BFP.*,BFD.FINTEXT AS DBCONFIGNAME,BFV.HANDTEXT AS FVERSIONNAME,CORG.ORGNAME AS FINANCEORGNAME,CTS.REALNAME AS CREATORNAME FROM BD_FINANCEPLAN BFP LEFT JOIN BD_FINANCEDATE BFD ON BFP.DBCONFIGID = BFD.FID " + 
			" LEFT JOIN BD_FINVERSION BFV ON BFP.FVERSIONID = BFV.FID LEFT JOIN TBL_ORGANIZATION CORG ON BFP.FINANCEORGID = CORG.ORGID LEFT JOIN TBL_STAFF CTS ON BFP.CREATOR = CTS.STAFFID"
			+ " WHERE BFP.FID = #{fid}")
	BdFinanceplanVr selectUniqueById(@Param("fid")String fid) throws Exception;

	@SelectProvider(type = BdFinanceplanMapperSqlConfig.class , method = "selectPageInfo")
	IPage<BdFinanceplanVr> selectPageInfo(IPage<BdFinanceplanVr> page, BdFinanceplanVo vo) throws Exception;

	@SelectProvider(type = BdFinanceplanMapperSqlConfig.class , method = "selectListForFinance")
	@Results({
		@Result(property = "fid",column = "FID"),
		@Result(property = "fname",column = "FNAME"),
		@Result(property = "record.recordid",column = "RECORDID"),
		@Result(property = "record.recordname",column = "RECORDNAME"),
		@Result(property = "record.iscompleted",column = "ISCOMPLETED"),
		
	})
	List<BdFinanceplanVr> selectListForFinance(BigDecimal orgid, String fname, Integer fstatus) throws Exception;

	@Update("UPDATE BD_FINANCEPLAN SET FSTATUS = #{fid} WHERE FID = #{fstatus}")
	void updateStatus(@Param("fid")String fid,@Param("fstatus") Integer fstatus) throws Exception;

}
