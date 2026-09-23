package com.huabo.system.service;



import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblAccBook;

public interface TblAccBookService {

    List<TblAccBook> findBookIdByUserAll(BigDecimal staffid, BigDecimal orgid);

    TblAccBook findByBookIdOne(String connectionstrings);

    JsonBean findstaffid(String token)throws Exception;

}
