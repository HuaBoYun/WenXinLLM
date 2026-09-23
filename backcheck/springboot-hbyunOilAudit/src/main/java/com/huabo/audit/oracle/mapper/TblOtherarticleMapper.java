package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblOtherarticle;
import com.huabo.audit.oracle.vo.TblOtherarticleVo;
import com.huabo.audit.util.PageInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-06-30
 */
public interface TblOtherarticleMapper extends BaseMapper<TblOtherarticle> {
    @SelectProvider(method="selectOtherarticlePageInfo",type=TblOtherarticleMapperSqlConfig.class)
	List<TblOtherarticle> selectOtherarticlePageInfo(PageInfo<TblOtherarticle> pageInfo,TblOtherarticleVo tblOtherarticleVo);
    
    @SelectProvider(method="selectOtherarticleCountPageInfo",type=TblOtherarticleMapperSqlConfig.class)
	Integer selectOtherarticleCountPageInfo(PageInfo<TblOtherarticle> pageInfo,TblOtherarticleVo tblOtherarticleVo);
    
    @InsertProvider(method="insertEntity",type=TblOtherarticleMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="othartid", keyColumn="OTHARTID")
	void insertEntity(TblOtherarticle tblOtherarticle);
    
    @UpdateProvider(method="updateEntity",type=TblOtherarticleMapperSqlConfig.class)
	void updateEntity(TblOtherarticle tblOtherarticle);
    
    @Select("SELECT * FROM TBL_OTHERARTICLE WHERE othartid=#{othartid}")
	TblOtherarticle findInfobyid(String othartid);
    
    @Delete("Delete from TBL_OTHERARTICLE WHERE othartid=#{othartid}")
	void deleteInfoById(String othartid);

}
