package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.mapper.ProcessSettingMapper;
import com.huabo.system.mapper.TblMyTaskMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.service.ProcessSettingService;
import com.huabo.system.service.TblCirculationService;
import com.huabo.system.service.TblStaffService;

@Service
public class ProcessSettingServiceImpl implements ProcessSettingService {

    @Resource
    private ProcessSettingMapper processSettingMapper;
    @Resource
    private TblStaffService tblStaffService;
    @Resource
    private TblCirculationService tblCirculationService;
    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
	private TblMyTaskMapper tblMyTaskMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findByList(Integer pageNumber, Integer pageSize, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
                PageInfo<ProcessSetting> pageInfo = new PageInfo<ProcessSetting>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<ProcessSetting> page = new Page<ProcessSetting>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<ProcessSetting> pageList = processSettingMapper.selectListByPageInfo(page, staff.getLinkDetp().getOrgid(), staff.getCurrentOrg().getOrgid());
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public ProcessSetting get(BigDecimal settingid) {
        return processSettingMapper.selectBySettingId(settingid);

    }

    @Override
    public void update(ProcessSetting processSetting) {
            processSettingMapper.updateByProcessSetting(processSetting);
    }


    @Override
    public Map<String, Object> findByLi(Integer pageNumber, Integer pageSize, String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();

                PageInfo<ProcessSetting> pageInfo = new PageInfo<ProcessSetting>();
                if (pageSize != null) {
                    pageInfo.setPageSize(pageSize);
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<ProcessSetting> page = new Page<ProcessSetting>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<ProcessSetting> pageList = processSettingMapper.selctOrgid(page, orgid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);

            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public String deleteProcessInstance(String[] ids) {
    	return String.valueOf(processSettingMapper.deleteById(ids));
    }

    @Override
    public void savemerge(ProcessSetting setting) {
    	processSettingMapper.savemerge(setting);
    }

    @Override
    public void delete(BigDecimal settingId) {
    	processSettingMapper.deleteBySettingId(settingId);
    }

    @Override
    public List<String> getButtonsForTransition(String tid) {
        return processSettingMapper.getButtonsForTransition(tid);
    }

}
