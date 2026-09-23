package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.vo.param.ProjectSettlementCostIntermediateQueryParam;
import com.huabo.audit.vo.result.ProjectSettlementCostIntermediateResult;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_GCXMZJ_ZJB(工程项目造价中间表)】的数据库操作Mapper
 * @createDate 2023-09-10 20:34:27
 * @Entity generator.domain.TblYqnsGcxmzjZjb
 */
public interface TblYqnsGcxmzjZjbMapper extends BaseMapper<TblYqnsGcxmzjZjb> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsGcxmzjZjb> pageInfo, TblYqnsGcxmzjZjb vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
    @Results({
    	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
            @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
            @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
            @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
            @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
            @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
            @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
            @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
            @Result(column = "esscjewy", property = "tblYqnsGcxmzj.esscjewy"),
    })
    List<TblYqnsGcxmzjZjb> selectListByPageInfo(PageInfo<TblYqnsGcxmzjZjb> pageInfo, TblYqnsGcxmzjZjb vo) throws Exception;

	/**
	 * 工程结算审计项目汇总 列表查询
	 * @param param
	 * @return
	 */
	List<ProjectSettlementCostIntermediateResult> findProjectSettlementCostIntermediateList(@Param("param") ProjectSettlementCostIntermediateQueryParam param);

	@Select("SELECT MAX(GCXMZJJZBNO) FROM TBL_YQNS_GCXMZJ_ZJB WHERE GCXMZJJZBNO LIKE '${currentYear}%' ")
	BigDecimal selectMaxAutoNo(Integer currentYear) throws Exception;

	@Select("SELECT * FROM TBL_YQNS_GCXMZJ_ZJB WHERE GCXMZJJZBNO = #{gcxmzjZjbNo} and CJSJ like '${currentYear}%'")
	TblYqnsGcxmzjZjb selectRequirementNoEntity(BigDecimal gcxmzjZjbNo,Integer currentYear) throws Exception;

	@SelectProvider(method = "selectListByExport", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
    @Results({
    		@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
            @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
            @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
            @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
            @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
            @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
            @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
            @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
    })
	List<TblYqnsGcxmzjZjb> selectListByExport(TblYqnsGcxmzjZjb vo) throws Exception;

    @Results({
    	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
            @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
            @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
            @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
            @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
            @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
            @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
            @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
    })
	@SelectProvider(method = "selectListByjhcgGlRela", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
	List<TblYqnsGcxmzjZjb> selectListByjhcgGlRela(BigDecimal id, String jsdw);
    
    
    @Results({
		 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),  
	        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
	        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
	        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
	        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
	        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
	        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
	        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
	        @Result(column = "esscjewy", property = "tblYqnsGcxmzj.esscjewy"),
	})
	@SelectProvider(method = "selectListByRwall", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
	List<TblYqnsGcxmzjZjb> selectListByRwall(BigDecimal id) throws Exception;
    
    
    @Update("UPDATE TBL_YQNS_GCXMZJ_ZJB SET RWIDS = #{ryids},RWNAMES = #{rynames} WHERE GCXMZJZJBID = #{jsxmtzwcqkid}")
    void rwfpry(String jsxmtzwcqkid, String ryids,String rynames);
     
    
    
    @Results({ 
	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
        @Result(column = "esscjewy", property = "tblYqnsGcxmzj.esscjewy"),
	})
	@SelectProvider(method = "selectListBymyRw", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
	List<TblYqnsGcxmzjZjb> selectListBymyRw(BigDecimal id,BigDecimal staffid) throws Exception;

    @Results({
	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"), 
        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
	})
	@SelectProvider(method = "selectListByjhchugGlRela", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
	List<TblYqnsGcxmzjZjb> selectListByjhchugGlRela(BigDecimal id, BigDecimal relaId);

    @Results({
	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
	})
	@SelectProvider(method = "selectListByjhzgGlRela", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
    List<TblYqnsGcxmzjZjb> selectListByjhzgGlRela(BigDecimal id, BigDecimal relaId);

    
    
    @Results({
	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
        @Result(column = "esscjewy", property = "tblYqnsGcxmzj.esscjewy"),
	})
	@SelectProvider(method = "getByRwid", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
	TblYqnsGcxmzjZjb getByRwid(String id) throws Exception;
 
    
    
    @Results({
	 	@Result(column = "GCXMZJJZBNO", property = "gcxmzjZjbNo"),
        @Result(column = "htbh", property = "tblYqnsGcxmzj.htbh"),
        @Result(column = "gcmc", property = "tblYqnsGcxmzj.gcmc"),
        @Result(column = "jsdw", property = "tblYqnsGcxmzj.jsdw"),
        @Result(column = "sgdw", property = "tblYqnsGcxmzj.sgdw"),
        @Result(column = "lxr", property = "tblYqnsGcxmzj.lxr"),
        @Result(column = "lxdh", property = "tblYqnsGcxmzj.lxdh"),
        @Result(column = "esscje", property = "tblYqnsGcxmzj.esscje"),
        @Result(column = "esscjewy", property = "tblYqnsGcxmzj.esscjewy"),
})
@SelectProvider(method = "selectListByRwallcf", type = TblYqnsGcxmzjZjbMapperSqlConfig.class)
List<TblYqnsGcxmzjZjb> selectListByRwallcf(BigDecimal id) throws Exception;
    
    
    @Delete("DELETE FROM TBL_YQNS_GCXMZJ_ZJB WHERE PARENTID = #{id}")
    void deleteparentid(String id);
    
    
    
    @Update("UPDATE TBL_YQNS_GCXMZJ_ZJB SET FPDDRYID = #{fpddryid},FPDDRYNAME = #{fpddryname} WHERE GCXMZJZJBID = #{id}")
    void rwfpddry(String id, String fpddryid,String fpddryname);
    
}




