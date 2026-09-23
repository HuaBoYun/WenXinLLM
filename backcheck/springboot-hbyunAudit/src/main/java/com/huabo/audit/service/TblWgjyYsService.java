package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.util.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:00
 */
public interface TblWgjyYsService {

    JsonBean getByWgjyYsList(String token, Integer pageNumber, Integer pageSize,String clueNaber)throws Exception;

	List<TblWgzzWgjyYs> getByWgjyYsExtList(Integer pageNumber, Integer pageSize);

    JsonBean addlist(String token, TblWgzzWgjyYs tblWgzzWgjyYs) throws Exception;

    JsonBean removeList(String token, BigDecimal id) throws Exception;

    JsonBean detail(String token, BigDecimal wghcid) throws Exception;

	R deleteAttInfoByAttId(String attId) throws Exception;
	
	
    JsonBean getbyiddetail(String token, BigDecimal id) throws Exception;
}
