package com.huabo.audit.service;




import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAccBook;

import java.math.BigDecimal;


public interface TblAccBookService {




    TblAccBook findByBookIdOne(BigDecimal connectionstrings);

    
    JsonBean findstaffid(String token)throws Exception;

}
