package com.huabo.system.service;



import java.math.BigDecimal;
import java.util.Map;

public interface TblAcquisitionRecordService {


	Map<String, Object> findByPage(Integer pageNumber, Integer pageSize,String token, String staffId);
}
