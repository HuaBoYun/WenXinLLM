package com.huabo.system.service.impl;

import java.math.BigDecimal;
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
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblExternalExpert;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.mapper.TblExternalExpertDao;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.service.TblExternalExpertService;

@Service
public class TblExternalExpertServiceImpl implements TblExternalExpertService {

    @Resource
    private TblExternalExpertDao tblExternalExpertDao;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblExternalExpert getExpert(BigDecimal exterid) {
        return tblExternalExpertDao.get(exterid);
    }

    @Override
    public Map<String, Object> findByOrgId(Find find, Integer pageNumber, Integer pageSize, String token,
                                           String staffId, String company) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            TblOrganization org = tblOrganizationMapper.findByWPZJK(orgid, TblOrganization.WPZJK);
            PageInfo<TblExternalExpert> pageInfo = new PageInfo<TblExternalExpert>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblExternalExpert> page = new Page<TblExternalExpert>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblExternalExpert> pageList = tblExternalExpertDao.selectTblExternal(page, orgid, find, company);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            dataMap.put("org", org);
            dataMap.put("pageInfo", pageInfo);
            resultMap.put("data", dataMap);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;

    }

    @Override
    public void updateTblExternalExpert(TblExternalExpert tee) {
        tblExternalExpertDao.updateTblExternalExpert(tee);
    }

    @Override
    public void saveTblExternalExpert(TblExternalExpert tee) {
        tblExternalExpertDao.saveTblExternalExpert(tee);
    }

}
