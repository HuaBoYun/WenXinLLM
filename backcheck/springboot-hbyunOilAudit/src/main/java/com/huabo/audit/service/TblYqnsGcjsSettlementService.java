package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

public interface TblYqnsGcjsSettlementService  extends IService<TblYqnsGcjsSettlement> {
    JsonBean getSettlementList(String token, Integer pageNumber, Integer pageSize, TblYqnsGcjsSettlement vo) throws Exception;

    JsonBean saveOrUpdate(String token, TblYqnsGcjsSettlement vo) throws Exception;

    JsonBean delete(String token, BigDecimal id) throws Exception;

    JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcjsSettlement vo) throws Exception;

    JsonBean importData(MultipartFile file, String token) throws Exception;

	JsonBean syncConstructionProject(String token) throws Exception;
}
