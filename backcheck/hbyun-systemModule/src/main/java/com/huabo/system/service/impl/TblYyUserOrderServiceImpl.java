package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblYyOrgDeposit;
import com.huabo.system.entity.TblYyUserOrder;
import com.huabo.system.mapper.TblYyOrgDepositMapper;
import com.huabo.system.mapper.TblYyUserOrderMapper;
import com.huabo.system.service.TblYyUserOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblYyUserOrderServiceImpl implements TblYyUserOrderService {

    @Resource
    private TblYyOrgDepositMapper tblYyOrgDepositMapper;

    @Resource
    private TblYyUserOrderMapper tblYyUserOrderMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> selectPageInfoList(String token, String staffId, Integer pageNumber, Integer pageSize, TblYyUserOrder yuo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            TblYyOrgDeposit yod = tblYyOrgDepositMapper.selectOrgDepositByOrgId(staff.getCurrentOrg().getOrgid());
            Double price = 0.0;
            if (yod != null) {
                price = yod.getTotalmoney() - yod.getTotalpaymoney();
            }
            PageInfo<TblYyUserOrder> pageInfo = new PageInfo<TblYyUserOrder>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            yuo.setOrgid(staff.getCurrentOrg().getOrgid());
            pageInfo.setCondition(yuo);
            
            Page<TblYyUserOrder> page = new Page<TblYyUserOrder>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblYyUserOrder> pageList = tblYyUserOrderMapper.selectListByPageInfo(page,yuo);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int) pageList.getTotal());
            Map<String, Object> dataMap = new HashMap<String, Object>(0);
            dataMap.put("money", Float.valueOf(price.toString()));
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", dataMap);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


}

