package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Bug;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.Riskcategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Mapper
public interface BugMapper extends BaseMapper<Bug> {
   @Select("SELECT * FROM TBL_BUG bu LEFT JOIN TBL_BUG_CRITERION cir on BU.BUGID=CIR.BUGID where 1=1 and ${ew.sqlSegment}")
   IPage<Bug> findAll(IPage page, @Param("ew") QueryWrapper<Bug> queryWrapper);


   /*通过BugId查询相关的缺陷*/
    @Select("select * from TBL_BUG where 1=1 start with BUGID= #{param1} connect by prior BUGID=FATHERBUGID")
    List<Bug> getChildrenById(String parentId);
//    @Select("SELECT * FROM TBL_BUG bu LEFT JOIN TBL_BUG_CRITERION cir on BU.BUGID=CIR.BUGID where 1=1 and ${ew.sqlSegment}")
//    IPage<Bug> findAll(IPage page, @Param("ew") QueryWrapper<Bug> queryWrapper);

    @Select("select * from TBL_BUG b where b.BUGNUMBER =#{code} AND b.BUGBYSYSTEM = #{type} AND b.BUGDEPARTMENT IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= #{orgid} AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT #{orgid} FROM DUAL)")
    List<Bug> findByCode(@RequestParam("code") String code, @RequestParam("type") String type, @RequestParam("orgid") BigDecimal orgid);

   /* List<Bug> findALL1(@RequestParam("orgid") String orgid, @RequestParam("type") String type);

    List<Bug> findALL2(@RequestParam("orgid") String orgid, @RequestParam("type") String type);*/

    IPage<Bug> selectPage1(@Param("orgid") String orgid, IPage page, @Param("ew") Wrapper<T> queryWrapper);

    IPage<Bug> selectPage2(@Param("orgid") String orgid, IPage page, @Param("ew") Wrapper<T> queryWrapper);

    List<HashMap> qxglExport(@Param("orgid") String orgid, @Param("type") String type);
}
