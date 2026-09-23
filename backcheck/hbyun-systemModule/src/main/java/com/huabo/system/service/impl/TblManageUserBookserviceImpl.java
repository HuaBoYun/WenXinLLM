package com.huabo.system.service.impl;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblManageUserBook;
import com.huabo.system.mapper.TblManageUserBookMapper;
import com.huabo.system.service.TblManageUserBookservice;

@Service
public class TblManageUserBookserviceImpl implements TblManageUserBookservice {

    @Resource
    private TblManageUserBookMapper tblManageUserBookMapper;

    @Override
    public void updateUserBook(Set<TblManageUserBook> userBookList, String orgid, String staffid) {
        staffid.substring(0, staffid.length() - 1);

        try {
            Integer count = 0;
            String sql = "";
            Iterator var6 = userBookList.iterator();

            while (var6.hasNext()) {
                TblManageUserBook manage = (TblManageUserBook) var6.next();
                //sql = "SELECT COUNT(0) FROM TBL_MANAGE_USER_BOOK where STAFFID = '" + manage.getStaffid() + "' AND BOOKID = '" + manage.getBookid() + "'";
                count = this.tblManageUserBookMapper.findCount(manage.getStaffid(), manage.getBookid());
                if (count == 0) {
                    this.tblManageUserBookMapper.saveEntity(manage);
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    @Override
    public void delUserBook(String sid, String acctid) {
            String[] staffs = sid.split(",");
            String[] acctids = acctid.split(",");

            for (int i = 0; i < acctids.length; ++i) {
                for (int j = 0; j < staffs.length; ++j) {
                    tblManageUserBookMapper.deleteSidAndAcctid(staffs[j], acctids[i]);
                }
            }
    }
}
