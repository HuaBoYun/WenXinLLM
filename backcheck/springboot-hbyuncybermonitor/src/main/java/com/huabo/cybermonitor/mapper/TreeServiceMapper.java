package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Tree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TreeServiceMapper extends BaseMapper<Tree> {



     @Select("select count(*) from TBL_ORGANIZATION where audittype = '1' and orgid = #{orgid}")
     public long   getORGANIZATIONCountByorgId(@Param("orgid") String orgid);



    @Select("select count(*) from TBL_ORGANIZATION  where orgtype != 100 and orgid != #{orgid} and orgid = #{toOrgid} " +
            "             start with orgid = #{orgid} connect by prior orgid = fatherorgid")
    public long   getORGANIZATIONCountByorgIdAndToOrgID(@Param("toOrgid") String toOrgid,@Param("orgid") String orgid);




    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0" +
            " START WITH o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")

    public List<Organization>  getORGANIZATIONByOrgID(@Param("orgid")String orgid);

}
