package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblWgzzWghc;
import com.huabo.audit.oracle.mapper.TblWgzzWghcMapper;
import com.huabo.audit.service.TblWgzzWghcService;
import com.huabo.audit.util.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:02
 */
@Service
public class TblWgzzWghcServiceimpl implements TblWgzzWghcService {

    @Resource
    TblWgzzWghcMapper tblWgzzWghcMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean selectwghcBy(String token, String creator,Integer pageNumber, Integer pageSize) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize==null) {
            pageSize=15;
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<TblWgzzWghc> pageInfo = new PageInfo<TblWgzzWghc>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblWgzzWghcMapper.selectBywghcBy(pageInfo,creator));
        pageInfo.setTotalRecord(this.tblWgzzWghcMapper.getByContWghcList(pageInfo,creator));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean selectwghcXQBy(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        TblWgzzWghc pan = tblWgzzWghcMapper.selectBywghcXQBy(id);
        resultMap.put("pan",pan);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean updateStatus(String token, BigDecimal id, Integer status) throws Exception {
        /*TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }*/
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        tblWgzzWghcMapper.updateStatus(id,status);
        return ResponseFormat.retParam(1,200,resultMap);
    }
}
