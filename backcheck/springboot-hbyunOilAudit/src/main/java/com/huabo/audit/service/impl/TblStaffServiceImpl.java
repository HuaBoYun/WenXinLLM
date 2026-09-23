package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.TblStaffService;
import com.huabo.audit.util.PageInfo;
import tk.mybatis.mapper.entity.Example;

@Service
public class TblStaffServiceImpl implements TblStaffService {

    @Resource
    public TblOrganizationMapper tblOrganizationMapper;

    @Resource
    public TblStaffMapper tblStaffMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findAllPageBeanPid(String username, String ralename, String pid, Integer pageNumber, Integer pageSize, String token,
                                                  String staffId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                TblStaffUtil user = userProvider.get();
                if (user == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                TblOrganization attribute = new TblOrganization();
                if (pid != null && !pid.equals("")) {
                    attribute = tblOrganizationMapper.findById(new BigDecimal(pid));
                } else {
                    attribute.setOrgid(user.getCurrentOrg().getOrgid());
                    attribute.setOrgtype(user.getCurrentOrg().getOrgtype());
                }
                PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setTlist(tblStaffMapper.findAllPageBeanPid(pageInfo, username, ralename, attribute));
                pageInfo.setTotalRecord(tblStaffMapper.findAllCountPageBeanPid(pageInfo, username, ralename, attribute));
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("attribute", attribute);
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("pageInfo", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                TblStaffUtil user = userProvider.get();
                if (user == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                TblOrganization attribute = new TblOrganization();
                if (pid != null && !pid.equals("")) {
                    attribute = tblOrganizationMapper.findById(new BigDecimal(pid));
                } else {
                    attribute.setOrgid(user.getCurrentOrg().getOrgid());
                    attribute.setOrgtype(user.getCurrentOrg().getOrgtype());
                }
                PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setTlist(tblStaffMapper.findAllPageBeanPid(pageInfo, username, ralename, attribute));
                pageInfo.setTotalRecord(tblStaffMapper.findAllCountPageBeanPid(pageInfo, username, ralename, attribute));
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("attribute", attribute);
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("pageInfo", dataMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public JsonBean findByAllPageBean(String useranme, Integer pageNumber, Integer pageSize, String token, TblStaff staff) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTlist(tblStaffMapper.selectByOrgidListPageInfo(pageInfo, loginStaff.getCurrentOrg().getOrgid(), useranme, staff));
        pageInfo.setTotalRecord(tblStaffMapper.selectByOrgidCountPageInfo(loginStaff.getCurrentOrg().getOrgid(), useranme, staff));
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);

    }

    @Override
    public JsonBean findUsrDetail(String token, Integer staffid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaff plan = this.tblStaffMapper.selectById(staffid);
        try {
            TblOrganization linkOrg = this.tblOrganizationMapper.selectCompanyInfoByDeptId(plan.getOrgid());
            TblOrganization linkdept = this.tblOrganizationMapper.findById(plan.getOrgid());
            plan.setLinkDetp(linkdept);
            plan.setLinkOrg(linkOrg);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        resultMap.put("usrInfo", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public com.github.pagehelper.PageInfo<TblStaff> queryAuditObjectStaffAll(Integer pageNum, Integer pageSize) {
        //Page<TblStaff> page = new Page<>(pageNum, pageSize);p
        PageHelper.startPage(pageNum, pageSize);
        List<TblStaff> tblStaffs = tblStaffMapper.queryAuditObjectStaffAll(1);
        com.github.pagehelper.PageInfo<TblStaff> page = new com.github.pagehelper.PageInfo<TblStaff>(tblStaffs);
        return page;
    }


   public List<TblStaff> selectTblStaffByIds(String staffids) {
        return tblStaffMapper.selectTblStaffByIds(staffids);
    }
}
