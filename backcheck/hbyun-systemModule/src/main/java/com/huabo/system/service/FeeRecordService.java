package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.dto.FeeRecordQueryDTO;
import javax.servlet.http.HttpServletResponse;

/**
 * 费用记录服务
 */
public interface FeeRecordService {
    JsonBean getPersonalRecords(String token, FeeRecordQueryDTO dto) throws Exception;
    void exportRecords(String token, FeeRecordQueryDTO dto, HttpServletResponse response) throws Exception;
}
