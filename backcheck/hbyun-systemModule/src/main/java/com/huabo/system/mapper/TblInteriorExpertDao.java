package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblInteriorExpert;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblInteriorExpertDao extends BaseMapper<TblInteriorExpert> {
    @Select("SELECT * FROM TBL_INTERIOR_EXPERT WHERE INTERIORID = #{interior}")
    List<TblInteriorExpert> findInterior(TblInteriorExpert interior);

    @InsertProvider(method = "insertInteriorExpert",type = TblInteriorExpertDaoSqlConfig.class)
    void insertInteriorExpert(TblInteriorExpert tie);

    @UpdateProvider(type=TblInteriorExpertDaoSqlConfig.class,method="updateInteriorExpert")
    void updateInteriorExpert(TblInteriorExpert tblinter);

    @SelectProvider(method = "selectListByPageInfoo",type = TblInteriorExpertDaoSqlConfig.class)
    @Results({
            @Result(column="REALNAME",property="staff.realname"),
            @Result(column="ORGNAME",property="organization.orgname"),
    })
    IPage<TblInteriorExpert> selectListByPageInfoo(IPage<TblInteriorExpert> page, BigDecimal orgid, Find find);

    @SelectProvider(method = "selectListByPageInfoCount",type = TblInteriorExpertDaoSqlConfig.class)
    Integer selectListByPageInfoCount(PageInfo<TblInteriorExpert> pageInfo, BigDecimal orgid, Find find);

    @Select("select * from TBL_INTERIOR_EXPERT where INTERIORID = #{userid}")
    List<TblInteriorExpert> findByRid(BigDecimal userid);

    @Delete("DELETE FROM TBL_INTERIOR_EXPERT WHERE INTERIORID = #{interiorid}")
    void deleteNbzj(BigDecimal interiorid);
}
