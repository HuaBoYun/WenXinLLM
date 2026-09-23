package com.huabo.system.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.system.entity.ProcessSetting;


public interface ProcessSettingService {


    Map<String, Object> findByList(Integer pageNumber, Integer pageSize, String token, String staffId);

    ProcessSetting get(BigDecimal settingid);

    void update(ProcessSetting processSetting);

    Map<String, Object> findByLi(Integer pageNumber, Integer pageSize, String token);


    String deleteProcessInstance(String[] ids);


    void savemerge(ProcessSetting setting);

    void delete(BigDecimal settingId);

    List<String> getButtonsForTransition(String tid);

}
