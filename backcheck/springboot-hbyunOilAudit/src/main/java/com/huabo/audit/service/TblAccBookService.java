package com.huabo.audit.service;




import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAccBook;


public interface TblAccBookService {




    TblAccBook findByBookIdOne(String connectionstrings);

    
    JsonBean findstaffid(String token)throws Exception;

}
