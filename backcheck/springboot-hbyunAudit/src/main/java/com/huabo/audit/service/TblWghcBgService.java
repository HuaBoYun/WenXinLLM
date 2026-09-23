package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;

import java.math.BigDecimal;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:00
 */
public interface TblWghcBgService {

    JsonBean getByWghcBgList(String token, Integer pageNumber, Integer pageSize,String clueNaber)throws Exception;

    JsonBean addlist(String token, TblWgzzWghcBg tblWgzzWghcBg) throws Exception;

    JsonBean removeList(String token, BigDecimal id) throws Exception;

    JsonBean detail(String token, BigDecimal wghcid) throws Exception;

}
