package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblYyUserQuery;
import com.huabo.system.mapper.TblYyOrgDepositMapper;
import com.huabo.system.service.TblYyOrgDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class TblYyOrgDepositServiceImpl implements TblYyOrgDepositService {

    @Autowired
    private TblYyOrgDepositMapper tblYyOrgDepositMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findCostPircePageInfo(Integer pageNumber, Integer pageSize, TblYyUserQuery yuq, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            PageInfo<TblYyUserQuery> pageInfo = new PageInfo<TblYyUserQuery>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            yuq.setOrgid(staff.getCurrentOrg().getOrgid());
            //yuq.setStaffid(staff.getStaffid());
            pageInfo.setCondition(yuq);
            
            Page<TblYyUserQuery> page = new Page<TblYyUserQuery>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblYyUserQuery> pageList = tblYyOrgDepositMapper.selectListByPageInfo(page,yuq);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

}
