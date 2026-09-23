package com.huabo.system.service;

import java.util.Set;

import com.huabo.system.entity.TblManageUserBook;

public interface TblManageUserBookservice {
	
    void updateUserBook(Set<TblManageUserBook> userBookList, String orgid, String staffid);

    void delUserBook(String sid, String acctid);
}
