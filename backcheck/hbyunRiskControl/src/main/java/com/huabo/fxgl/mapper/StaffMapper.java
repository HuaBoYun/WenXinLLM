package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.Organization;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
@Repository
public interface StaffMapper extends BaseMapper<Staff> {
    @Select("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,STA.username " +
            "from TBL_STAFF sta INNER JOIN TBL_ORGANIZATION org ON STA.ORGID=ORG.ORGID " +
            "where STA.ORGID in " +
            "(select ORGID from TBL_ORGANIZATION org " +
            "where FATHERORGID=#{param1} AND ORGTYPE=0 )" +
            "AND (STA.STATUS is NULL or STA.STATUS != 0) order by STAFFID desc")
    IPage<Staff> selectStaffByFatherOrgid(BigDecimal orgid,IPage page);

    @Select("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,STA.username " +
            "from tbl_staff sta INNER JOIN TBL_ORGANIZATION org " +
            "ON STA.ORGID=ORG.ORGID " +
            "where STA.ORGID =#{param1} AND (STA.STATUS is NULL or STA.STATUS != 0) order by STAFFID desc")
    IPage<Staff> selectStaffByOrgid(BigDecimal orgid,IPage page);
    
    @Select("select realname from tbl_staff where staffid=#{staffid}")
    String findRealNameById(String staffid);

	List<Staff> getOrgNameByIds(@Param("ids")String[] ids);

	List<Staff> getOrgIdByNames(@Param("ids")String[] ids,@Param("orgid")BigDecimal orgid);
	
	List<Staff> getOrgIdByNames2(@Param("ids")String[] ids,@Param("orgids")List<BigDecimal> orgids);

	

}
