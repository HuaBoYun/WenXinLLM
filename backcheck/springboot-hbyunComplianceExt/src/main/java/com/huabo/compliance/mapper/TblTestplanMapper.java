package com.huabo.compliance.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTestplanVo;
import com.huabo.compliance.entity.TblTesttempleVo;
import com.huabo.compliance.entity.Tree;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
@Mapper
public interface TblTestplanMapper extends BaseMapper {

	@Select("SELECT COUNT(0) FROM TBL_COM_EXT_TESTPLAN WHERE TESTTEMID = #{templId}")
	Integer findPlanCountByTestTempId(Integer templId) throws Exception;

    @Select("${sql}")
    <p extends IPage<TblTestplan>>  p  getSqlPage(p page, @Param("sql") String sql);

    @Select("select * from TBL_COM_EXT_TESTPLAN where testplanid=#{testplanid}")
    @Results(id="TblTestplanVo",value={
            @Result(property = "testplanid",column = "testplanid",id = true),
            @Result(property = "testtemid",column = "testtemid"),
            @Result(property = "orgid",column = "orgid"),
            @Result(property = "creatid",column = "creatid"),
            @Result(property = "staffid",column = "staffid"),
            @Result(property = "testtemple",column = "testtemid",one=@One(select="com.huabo.compliance.mapper.TblTesttempleMapper.selectById")),

    })
    TblTestplanVo getOneTblTestplanVo(BigDecimal testplanid);

    @Select("select * from TBL_COM_EXT_TESTTEMPLE where testtemid=#{testtemid}")
    @Results(id="TblTesttempleVo",value={
            @Result(property = "staffid",column = "staffid"),
            @Result(property = "chuangjianren",column = "staffid",one=@One(select="com.huabo.compliance.mapper.TblStaffMapper.selectById")),

    })
    TblTesttempleVo getOneTblTesttempleVo(@Param("testtemid") BigDecimal testtemid);



    @Select("${sql}")
    @ResultMap("TblTesttempleVo")
    <p extends IPage<TblTesttempleVo>>  p  getTesttempleVoSqlPage(p page, @Param("sql") String sql);



    @Select("select\n" +
            "    decode(CONNECT_BY_ISLEAF,0,'true','false') AS \"isParent\",\n" +
            "    t.typeid as \"id\",\n" +
            "    t.TYPENAME as \"name\",\n" +
            "    t.PARENTID as \"pId\"\n" +
            "from TBL_COM_EXT_TESTTEMPL_TYPE t\n" +
            "CONNECT BY PRIOR T.TYPEID = T.PARENTID\n" +
            "START WITH t.parentId is null and t.testTempletaId = #{tempid}")
    List<Tree> getTreeListByTemid(BigDecimal tempid);


    /*
        分配右侧列表
     */
    @Select("${sql}")
    IPage<Map<String,Object>>  fingByTree(IPage<Map<String,Object>> page, @Param("sql") String sql);

    @Select("select * from TBL_COM_EXT_TESTPLAN where testplanid=#{testplanid}")
    TblTestplan findById(BigDecimal testplanid);
    
    
    @Delete("DELETE FROM TBL_COM_EXT_TESTPLAN where testplanid=#{testplanid}")
    void deleteTestPlan(@Param("testplanid")BigDecimal testplanid);

    @InsertProvider(type=TblTestplanMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="testplanid", keyColumn="TESTPLANID")
	void insertEntity(TblTestplan tblTestplan) throws Exception;

    @UpdateProvider(type=TblTestplanMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTestplan tblTestplan) throws Exception;
    
    
}
