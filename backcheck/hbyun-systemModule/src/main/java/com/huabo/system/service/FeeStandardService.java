package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.huabo.system.dto.FeeStandardSaveDTO;

/**
 * 费用标准管理服务
 */
public interface FeeStandardService {
    JsonBean getStandardList(String token, String moduleType) throws Exception;
    JsonBean saveStandard(String token, FeeStandardSaveDTO dto) throws Exception;
}
