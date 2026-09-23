package com.huabo.system.mapper;

import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblOrgRight;
import com.huabo.system.entity.TblOrgRightnew;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblOrgRightMapper extends BaseMapper<TblOrgRightnew> {


    @Select("select * from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightname= #{rightname}")
    List<TblOrgRightnew> findByTblOrgRight(BigDecimal orgid, String rightname);

    @Select("select * from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightid= #{rightid}")
    List<TblOrgRightnew> findByTblOrgRightId(BigDecimal orgid, String rightid);

    @Select("select rightname,rightid from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightid= #{rightid}")
    List<Object[]> OBJlistBySql(BigDecimal orgid, String rightid);

    @Update("update TBL_ORG_RIGHT_NEW set INDICATORSTATUS= #{indicatorstatus} where RIGHTID= #{rightid} and ORGID= #{orgid}")
    void updateByOrgright(TblOrgRightnew orgright);

    @Update("update TBL_ORG_RIGHT_NEW SET RIGHTNAME = #{rightname}, INDICATORSTATUS= #{indicatorstatus} where RIGHTID= #{rightid} and ORGID= #{orgid}")
    void updateTblOrgRights(TblOrgRightnew orgright);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) VALUES (#{orgid},#{rightid})")
    void insertOrgRight(TblOrgRight right);
}
