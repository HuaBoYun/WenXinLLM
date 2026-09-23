package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.ProcessSettingMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface ProcessSettingMySqlMapper extends BaseMapper<ProcessSettingMySql> {

    @SelectProvider(type = ProcessSettingMapperSqlMySqlConfig.class, method = "selectListByPageInfo")
    List<ProcessSettingMySql> selectListByPageInfo(PageInfo<ProcessSettingMySql> pageInfo, BigDecimal orgid, BigDecimal companyid);

//    @Select("SELECT * FROM TBL_PROCESS_SETTING WHERE ORGID = #{orgid}")
//    PageBean selectOrgid(BigDecimal orgid);

    @Select("SELECT COUNT(*) from TBL_PROCESS_SETTING t where COMPANYID= #{companyid} or ORGID=#{orgid}")
    Integer selectCountByPageInfo(PageInfo<ProcessSettingMySql> pageInfo, BigDecimal orgid, BigDecimal companyid);

    @SelectProvider(method = "selctOrgid", type = ProcessSettingMapperSqlMySqlConfig.class)
    @Results({
            @Result(column = "ORGNAME", property = "tblOrganization.orgname")
    })
    List<ProcessSettingMySql> selctOrgid(PageInfo<ProcessSettingMySql> pageInfo, BigDecimal orgid);

    //@SelectProvider(method="select",type=ProcessSettingMapperSqlConfig.class)
    @Select("SELECT COUNT(*) from TBL_PROCESS_SETTING WHERE COMPANYID = #{orgid} ")
    Integer select(BigDecimal orgid);

    @Select("SELECT * FROM TBL_PROCESS_SETTING WHERE SETTINGID = #{settingid}")
    ProcessSettingMySql selectBySettingId(BigDecimal settingid);

    @UpdateProvider(type = ProcessSettingMapperSqlMySqlConfig.class, method = "updateByProcessSetting")
    void updateByProcessSetting(ProcessSettingMySql processSetting);

    @InsertProvider(type = ProcessSettingMapperSqlMySqlConfig.class, method = "savemerge")
    void savemerge(ProcessSettingMySql setting);

    @Delete("DELETE FROM TBL_PROCESS_SETTING WHERE SETTINGID = #{settingId}")
    void deleteBySettingId(BigDecimal settingId);

    @Select("SELECT * from TBL_PROCESS_SETTING t where t.module = #{definitionName}  order by SETTINGID desc")
    List<ProcessSettingMySql> getByModuel(String definitionName);

    @Select("SELECT * from TBL_PROCESS_SETTING WHERE SETTINGID = #{tid}")
    List<String> getButtonsForTransition(String tid);

}
