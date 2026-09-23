package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TBLAnnouncementManagement;

import java.math.BigDecimal;
import java.util.Date;

public interface TBLAnnouncementManagementService {
    JsonBean saveOrUpdate(TBLAnnouncementManagement entity) throws Exception;
    JsonBean getHomepage_List(Integer pageNumber, Integer pageSize, String title, String start,String end,String createTimeStart,String createTimeEnd,Integer type) throws Exception;

    JsonBean deleteById(BigDecimal id) throws Exception;
    JsonBean getDetail(BigDecimal id) throws Exception;
    JsonBean getIndexList(Integer pageNumber,Integer pageSize) throws Exception;


}
