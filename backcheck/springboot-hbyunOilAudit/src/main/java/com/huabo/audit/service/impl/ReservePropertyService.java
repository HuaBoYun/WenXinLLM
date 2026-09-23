package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import com.huabo.audit.oracle.mapper.ExpectLeaveMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.ExpectLeaveService;
import com.huabo.audit.util.PageInfoUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rui
 * @ClassName ExpectLeaveServiceImpl
 * @Description
 * @DATE 2023/9/14
 */
@Service
public class ReservePropertyService{

    @Autowired
    private TblStaffMapper tblStaffMapper;

    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;

    /**
     * 构建预留字段返回
     * @param list
     */
    public void buildReserveProperty(List<? extends BaseReservedProperty> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (BaseReservedProperty entity : list) {
            buildReserveProperty(entity);
        }
    }
    /**
     * 构建预留字段返回
     * @param entity
     */
    public void buildReserveProperty(BaseReservedProperty entity) {
        if (Objects.isNull(entity)) {
            return;
        }

        //预留人员单选
        if(Objects.nonNull(entity.getStaffid1())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid1().toString());
            entity.setRealname1(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid2())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid2().toString());
            entity.setRealname2(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid3())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid3().toString());
            entity.setRealname3(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid4())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid4().toString());
            entity.setRealname4(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid5())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid5().toString());
            entity.setRealname5(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        //预留人员多选
        if(StringUtil.isNotEmpty(entity.getStaffids1())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids2())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids3())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids4())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids5())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames5(result);
        }

        //预留组织单选
        if(Objects.nonNull(entity.getOrgid1())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid1().toString()));
            entity.setOrgname1(realname);
        }
        if(Objects.nonNull(entity.getOrgid2())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid2().toString()));
            entity.setOrgname2(realname);
        }
        if(Objects.nonNull(entity.getOrgid3())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid3().toString()));
            entity.setOrgname3(realname);
        }
        if(Objects.nonNull(entity.getOrgid4())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid4().toString()));
            entity.setOrgname4(realname);
        }
        if(Objects.nonNull(entity.getOrgid5())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid5().toString()));
            entity.setOrgname5(realname);
        }
        //预留组织多选
        if(StringUtil.isNotEmpty(entity.getOrgids1())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids2())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids3())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids4())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids5())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames5(result);
        }
    }
}
