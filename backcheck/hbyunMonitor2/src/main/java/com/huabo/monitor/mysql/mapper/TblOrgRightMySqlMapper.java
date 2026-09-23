package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblOrgRightMySql;
import com.huabo.monitor.mysql.entity.TblOrgRightnewMySql;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblOrgRightMySqlMapper extends BaseMapper<TblOrgRightnewMySql> {


    @Select("select * from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightname= #{rightname}")
    List<TblOrgRightnewMySql> findByTblOrgRight(BigDecimal orgid, String rightname);

    @Select("select * from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightid= #{rightid}")
    List<TblOrgRightnewMySql> findByTblOrgRightId(BigDecimal orgid, String rightid);

    @Select("select rightname,rightid from TBL_ORG_RIGHT_NEW where ORGID= #{orgid} and rightid= #{rightid}")
    List<Object[]> OBJlistBySql(BigDecimal orgid, String rightid);

    @Update("update TBL_ORG_RIGHT_NEW set  INDICATORSTATUS= #{indicatorstatus} where RIGHTID= #{rightid} and ORGID= #{orgid}")
    void updateByOrgright(TblOrgRightnewMySql orgright);

    @Update("update TBL_ORG_RIGHT_NEW SET RIGHTNAME = #{rightname}, INDICATORSTATUS= #{indicatorstatus} where RIGHTID= #{rightid} and ORGID= #{orgid}")
    void updateTblOrgRights(TblOrgRightnewMySql orgright);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) VALUES (#{orgid},#{rightid})")
    void insertOrgRight(TblOrgRightMySql right);
}
