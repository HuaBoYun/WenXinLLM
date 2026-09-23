package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblYyXdfTeam;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-08
 */
public interface TblYyXdfTeamMapper extends BaseMapper<TblYyXdfTeam> {

    @Insert("INSERT INTO TBL_YY_XDF_TEAM(TEAMID,TEAMNAME,CREATEDATE,STAFFID,COMPANYID) VALUES(#{teamid},#{teamname},#{createdate},#{staffid},#{companyid})" )
    @Options(useGeneratedKeys=true, keyProperty="teamid", keyColumn="TEAMID")
    void insertTeam(TblYyXdfTeam team);

    @Update("UPDATE TBL_YY_XDF_TEAM SET TEAMNAME = #{teamname} WHERE TEAMID = #{teamid}")
    void updateTeam(TblYyXdfTeam team);

    @Select("select * from TBL_YY_XDF_TEAM where STAFFID= #{staffid} and COMPANYID = #{orgid}")
    List<TblYyXdfTeam> listByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid);

    @Select("SELECT YXT.*,(SELECT COUNT(*) FROM TBL_YY_COMPANY WHERE TEAMID = YXT.TEAMID) FROM TBL_YY_XDF_TEAM YXT where STAFFID= #{staffid} and COMPANYID = #{orgid}")
	List<TblYyXdfTeam> countlistByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid);
}
