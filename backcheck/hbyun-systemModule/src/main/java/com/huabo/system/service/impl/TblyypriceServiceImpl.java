package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.Tblyyprice;
import com.huabo.system.mapper.TblyypriceMapper;
import com.huabo.system.service.TblyypriceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblyypriceServiceImpl implements TblyypriceService {

    @Resource
    private TblyypriceMapper tblyypriceMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findListPage(Find find, String token, String staffId, Integer pageNumber, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            PageInfo<Tblyyprice> pageInfo = new PageInfo<Tblyyprice>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<Tblyyprice> page = new Page<Tblyyprice>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<Tblyyprice> pageList = tblyypriceMapper.selectListByPageInfo(page, find, orgid);
            
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
