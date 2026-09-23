package com.huabo.compliance.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.compliance.oracle.entity.TblhgglAret;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:18:18
 */
public interface TblhgglAretMapper extends Mapper<TblhgglAret> {

    @Select("select * from TBL_HGGL_ARET where ID = #{id}")
    List<TblhgglAret> getListByselectID(@Param("id") Integer id);

    @Delete("delete from TBL_HGGL_ARET where ID = #{id}")
    Integer deleteListByaret(@Param("id") Integer id);
    
    @InsertProvider(method="insertEntity",type=TblhgglAretMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	void insertEntity(TblhgglAret plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblhgglAretMapperSqlConfig.class)
	void updateEntity(TblhgglAret plan) throws Exception;

	@Select("select * from TBL_HGGL_ARET where ID = #{id}")
	TblhgglAret findById(@Param("id")Integer id);
}
