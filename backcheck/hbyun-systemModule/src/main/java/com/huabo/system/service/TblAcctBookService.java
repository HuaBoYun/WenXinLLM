package com.huabo.system.service;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.page.PageBean;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblAcctBookService {
    Map<String, Object> findByTypeNewZB(String token, Integer pageNumber, Integer pageSize,BigDecimal pid);

}
