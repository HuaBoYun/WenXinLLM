package com.huabo.cybermonitor.service;

import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.FormControlrule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.FormControlruleDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IFormControlruleService extends IService<FormControlrule> {




    void findPageInfo(PageInfo<FormControlrule> pageInfo) throws Exception;

    String insertFormControlRule(FormControlrule rule, String[] replacekey,
                                 String[] replacekvalue, String[] replacetype, BigDecimal eleId, String reorg) throws Exception;

    String modifyFormControlRule(FormControlruleDto rule, String[] replacekey,
                                 String[] replacekvalue, String[] replacetype, BigDecimal eleId,
                                 String[] reid, String reorg) throws Exception;

    BigDecimal calculateFormula(String calFormula, String valjson, String eletext) throws Exception;

    void modifyFormStatus(String ruleid, String status) throws Exception;

    String checkFlowFromRule(String jsonArry, String formId, String userName, BigDecimal orgId) throws Exception;

    String checkFlowFromRuleByjsonArray(String jsonArry, String realname, BigDecimal orgid, String ruleNo) throws Exception;


    void findFieldRulePageInfo(PageInfo<FormControlrule> pageInfo) throws Exception;

    String findVideoPlayUrl(Integer videoId) throws Exception;

    String findDocFileName(Integer docId) throws Exception;

    List<Map<String,Object>> getLeftById(String ruleid);

    int remove_fromRule(BigDecimal ruleid);
}
