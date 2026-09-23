package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.mappersql.ProcessSettingMapperSqlConfig;

public interface ProcessSettingMapper extends BaseMapper<ProcessSetting> {

    @SelectProvider(type=ProcessSettingMapperSqlConfig.class,method="selectListByPageInfo")
    IPage<ProcessSetting> selectListByPageInfo(IPage<ProcessSetting> page, BigDecimal orgid, BigDecimal companyid);

    @SelectProvider(method="selctOrgid",type=ProcessSettingMapperSqlConfig.class)
    @Results({
            @Result(column="ORGNAME",property="tblOrganization.orgname")
    })
    IPage<ProcessSetting> selctOrgid(IPage<ProcessSetting> page,BigDecimal orgid);

    @Select("SELECT * FROM TBL_PROCESS_SETTING WHERE SETTINGID = #{settingid}")
    ProcessSetting selectBySettingId(BigDecimal settingid);

    @UpdateProvider(type=ProcessSettingMapperSqlConfig.class,method="updateByProcessSetting")
    void updateByProcessSetting(ProcessSetting processSetting);

    @InsertProvider(type=ProcessSettingMapperSqlConfig.class,method="savemerge")
    void savemerge(ProcessSetting setting);

    @Delete("DELETE FROM TBL_PROCESS_SETTING WHERE SETTINGID = #{settingId}")
    void deleteBySettingId(BigDecimal settingId);

    @Select("SELECT * from TBL_PROCESS_SETTING t where t.module = #{definitionName}  order by SETTINGID desc")
    List<ProcessSetting> getByModuel(String definitionName);

    @Select("SELECT * from TBL_PROCESS_SETTING WHERE SETTINGID = #{tid}")
    List<String> getButtonsForTransition(String tid);

}
