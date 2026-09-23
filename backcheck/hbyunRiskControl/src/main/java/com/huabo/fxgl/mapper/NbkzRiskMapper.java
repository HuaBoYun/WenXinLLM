package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.NbkzRisk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.entity.NbkzRisk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Repository
public interface NbkzRiskMapper extends BaseMapper<NbkzRisk> {
    @Select("SELECT * FROM TBL_BUGCRITERION where 1=1 and ORGID=#{orgid}")
    List<Criterion> findAll(String orgid);

    @Select("SELECT * from TBL_NBKZ_RISK WHERE riskid=#{riskid}")
    List<NbkzRisk> get(@Param("riskid") String riskid);

    @Select("SELECT * from TBL_NBKZ_RISK WHERE risknumber=#{risknumber} and stype like #{type} " +
            "AND ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= #{orgid}" +
            "AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT ${orgid} FROM DUAL)")
    List<NbkzRisk> getBycode(@Param("risknumber")String risknumber,@Param("type")String type,@Param("orgid")String orgid);
//  risknumber=01 type=nb
}
