package com.huabo.system.service;


import com.huabo.system.entity.Find;
import com.huabo.system.entity.Tblyyprice;
import com.huabo.system.page.PageBean;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblyypriceService {
    Map<String, Object> findListPage(Find find,String token,String staffId, Integer pageNumber, Integer pageSize);

}
