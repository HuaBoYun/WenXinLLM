package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Worksheet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Repository
public interface WorksheetMapper extends BaseMapper<Worksheet> {
    @Select("select count(*) from TBL_ORGANIZATION where audittype = '1' and orgid = #{param}")
    Integer isSJByOrgId(String userOrgid);

    @Select("select * from TBL_WORKSHEET where 1=1 and orgid= #{orgid} and WORKSHEETBYSYSTEM like '%fxgl%' ")
    IPage<Worksheet> findAllTblWorksheetByorgid(String orgid, String type, IPage page,  QueryWrapper queryWrapper);

    @Select("select * from TBL_WORKSHEET where 1=1  and  orgid in (select ORGID from TBL_ORGANIZATION where  orgid= #{orgid} start with  fatherorgid= #{orgid} connect by prior fatherorgid =ORGID ) and WORKSHEETBYSYSTEM like '%fxgl%' ")
    IPage<Worksheet> findAllTblWorksheetByorgid1(String orgid, String type, IPage page,  QueryWrapper queryWrapper);

    List<Worksheet> findByTblWorkSheetnumber(@Param("num") String num, @Param("type") String type, @Param("orgId") BigDecimal orgId);

    @Select("select * from TBL_WORKSHEET where 1=1 and ORGID in (select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid=1 connect by prior ORGID= fatherorgid) and WORKSHEETBYSYSTEM like '#{param}%' order by WORKSHEETID desc ")
    List<Worksheet> findAllByorgids(String type,String orgid);
//'#{param}%'
    @Select("select * from TBL_WORKSHEET where 1=1 and WORKSHEETBYSYSTEM like #{type}  and  orgid= #{orgid} order by WORKSHEETID desc ")
    List<Worksheet> findAllByorgid(@Param("type") String type,@Param("orgid") String orgid);
}
