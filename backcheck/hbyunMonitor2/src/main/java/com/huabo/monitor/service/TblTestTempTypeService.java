package com.huabo.monitor.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblTesttemplType;

public interface TblTestTempTypeService {

    void deleteBytemplId(BigDecimal testtemid);

	JsonBean save(String token, TblTesttemplType type) throws Exception;

	JsonBean modify(String token, TblTesttemplType type) throws Exception;

	JsonBean remove(String token, BigDecimal typeid) throws Exception;

	JsonBean getInfo(String token, BigDecimal typeid) throws Exception;

	JsonBean getTreeList(String token, BigDecimal parentId, BigDecimal testtempletaid) throws Exception;
}
