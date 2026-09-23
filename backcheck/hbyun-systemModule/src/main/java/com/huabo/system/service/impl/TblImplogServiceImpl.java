package com.huabo.system.service.impl;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblImplog;
import com.huabo.system.mapper.TblImplogMapper;
import com.huabo.system.service.TblImplogService;

@Service
public class TblImplogServiceImpl implements TblImplogService {

    @Resource
    private TblImplogMapper tblImplogMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findByTblImplogList(String token, String staffId, String type, Integer pageNumber, Integer pageSize) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                PageInfo<TblImplog> pageInfo = new PageInfo<TblImplog>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblImplog> page = new Page<TblImplog>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblImplog> pageList = tblImplogMapper.findByTblImplogList(page, staff.getUsername(), type);
                
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
