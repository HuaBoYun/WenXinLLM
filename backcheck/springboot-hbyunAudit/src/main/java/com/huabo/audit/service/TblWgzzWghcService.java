package com.huabo.audit.service;

import com.hbfk.util.JsonBean;

import java.math.BigDecimal;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:00
 */
public interface TblWgzzWghcService {

    JsonBean selectwghcBy(String token, String creator,Integer pageNumber, Integer pageSize)throws Exception;

    JsonBean selectwghcXQBy(String token, BigDecimal id)throws Exception;

    JsonBean updateStatus(String token, BigDecimal id, Integer status)throws Exception;

}
