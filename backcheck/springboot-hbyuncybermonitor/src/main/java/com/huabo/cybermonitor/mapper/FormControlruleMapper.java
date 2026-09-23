package com.huabo.cybermonitor.mapper;
import com.huabo.cybermonitor.mapper.provider.ForoContrlruleProvider;

import com.huabo.cybermonitor.entity.FormControlrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.SelectProvider;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface FormControlruleMapper extends BaseMapper<FormControlrule> {


    @SelectProvider(type=ForoContrlruleProvider.class,method = "calculateFormula")
    BigDecimal calculateFormula(String calFormula);

    @Update("UPDATE TBL_FORM_CONTROLRULE SET RULESTATUS = #{status} WHERE RULEID = #{ruleid}")
    void Update_CONTROLRULE(@Param("ruleid") String ruleid,@Param("status") String status);

    @Select("${ruleIdsSql}")
    String executeSql(@Param("ruleIdsSql") String ruleIdsSql);

    List<FormControlrule> findListByIds(String ruleListHql);

    @Select("select * from tbl_rule_dept c left join TBL_FORM_CONTROLRULE a on c.ruleid = a.ruleid" +
            "left join  TBL_ORGANIZATION b on b.orgid = a.orgid")
    List<Map<String, Object>> getLeftById(String ruleid);
}
