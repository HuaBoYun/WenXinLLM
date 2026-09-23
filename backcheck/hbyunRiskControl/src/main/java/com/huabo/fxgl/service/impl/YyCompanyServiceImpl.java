package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.entity.YyCompany;
import com.huabo.fxgl.entity.YyPrice;
import com.huabo.fxgl.mapper.YyCompanyMapper;
import com.huabo.fxgl.mapper.YyPriceMapper;
import com.huabo.fxgl.mapper.YyReportModelMapper;
import com.huabo.fxgl.service.IYyCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-02
 */
@Service
public class YyCompanyServiceImpl extends ServiceImpl<YyCompanyMapper, YyCompany> implements IYyCompanyService {

    @Autowired
    private YyPriceMapper yyPriceMapper;
    @Autowired
    private YyReportModelMapper reportModelMapper;

    @Override
    public PageInfo<YyCompany> findCompanyListByTeamid(Integer pageNo,Integer pageSize, Find find, Integer teamid,String fxjktype) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamid", teamid);
        queryWrapper.eq("fxjktype", fxjktype);
        if (find != null && find.getStaffid() != null && find.getStaffid().length() > 0) {
            queryWrapper.eq("STAFFID", find.getStaffid());
        }
        if (find != null && find.getFxtype() != null && find.getFxtype().length() > 0) {
            queryWrapper.eq("fxtype", find.getFxtype());
        }
        if (find != null && find.getCompanyname() != null && find.getCompanyname().length() > 0) {
            queryWrapper.like("COMPANYNAME", find.getCompanyname());
        }
        queryWrapper.orderByAsc("companyid");
        com.github.pagehelper.PageInfo<YyCompany> pageInfo = PageMethod.startPage( pageNo, pageSize)
 				.doSelectPageInfo(() -> baseMapper.findCompanyList(queryWrapper));
        for (YyCompany company : pageInfo.getList()) {
            if (company.getReport() != null && StringUtils.isNotBlank(company.getReport().getPriceid())) {
                company.setPriceList(buildPriceList(company.getReport().getPriceid()));
            }
        }
        return pageInfo;
    }

    @Override
    public PageInfo<YyCompany> findCompanyList(Integer pageNo,Integer pageSize, Find find, BigDecimal orgid, BigDecimal staffid,String fxjktype) {
    	QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ORGID", orgid);
        queryWrapper.eq("staffid", staffid);
        queryWrapper.eq("fxjktype", fxjktype);
        if (find != null && find.getFxtype() != null && find.getFxtype().length() > 0) {
            queryWrapper.eq("fxtype", find.getFxtype());
        }
        if (find != null && find.getCompanyname() != null && find.getCompanyname().length() > 0) {
            queryWrapper.like("COMPANYNAME", find.getCompanyname());
        }
        queryWrapper.orderByAsc("companyid");

         com.github.pagehelper.PageInfo<YyCompany> pageInfo = PageMethod.startPage( pageNo, pageSize)
 				.doSelectPageInfo(() -> baseMapper.findCompanyList(queryWrapper));
        for (YyCompany company : pageInfo.getList()) {
            if (company.getReport() != null && StringUtils.isNotBlank(company.getReport().getPriceid())) {
                company.setPriceList(buildPriceList(company.getReport().getPriceid()));
            }
        }
        return pageInfo;
    }

    /**
     * 根据逗号分隔的 priceid 字符串构建价格/监控项列表。
     * - CJBDI 类型（以 "cjbdi_" 开头）：直接构建 YyPrice 对象，不查数据库，
     *   避免达梦数据库将字符串 priceid 与数值类型字段比较时报"字符串转换出错"。
     * - 旧数值类型：查 TBL_YY_PRICE 表。
     */
    private java.util.List<YyPrice> buildPriceList(String priceid) {
        if (priceid == null || priceid.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        String[] parts = priceid.split(",");
        java.util.List<String> legacyIds = new java.util.ArrayList<>();
        java.util.List<YyPrice> result = new java.util.ArrayList<>();

        for (String part : parts) {
            String id = part.trim();
            if (id.isEmpty()) continue;
            if (id.startsWith("cjbdi_")) {
                // CJBDI 监控项：直接构建，不查数据库
                YyPrice p = new YyPrice();
                p.setPriceid(id);
                p.setInterfacename(id); // interfacename 由前端从 CJBDI_MONITOR_ITEMS 配置映射，此处填 id 作为占位
                result.add(p);
            } else {
                legacyIds.add(id);
            }
        }

        // 旧数值类型 priceid 仍走数据库查询
        if (!legacyIds.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < legacyIds.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("'").append(legacyIds.get(i)).append("'");
            }
            result.addAll(yyPriceMapper.selectListByIds(sb.toString()));
        }

        return result;
    }



    @Override
    @Transactional
    public boolean save(YyCompany yyCompany) {
        reportModelMapper.insert(yyCompany.getReport());
        baseMapper.insert(yyCompany);
        baseMapper.insertBiPageSet(yyCompany.getCompanyid(), yyCompany.getBiPageSet());
        return true;
    }

    @Override
    public YyCompany getById(BigDecimal companyId) {
        YyCompany yyCompany = super.getById(companyId);
        //查询对应的Price信息，使用 buildPriceList 避免达梦数据库将 cjbdi_xx 字符串误识别为列名
        if (yyCompany.getReport() != null && StringUtils.isNotBlank(yyCompany.getReport().getPriceid())) {
            yyCompany.setPriceList(buildPriceList(yyCompany.getReport().getPriceid()));
        }
        return yyCompany;
    }
    
    
    @Override
    public void deleteComPageByCompanyId(BigDecimal companyId) {
    	baseMapper.deleteComPageByCompanyId(companyId);
    }
    
    @Override
    public void deleteComPageByTeamId(BigDecimal teamid) {
    	baseMapper.deleteComPageByTeamId(teamid);
    }
    
    
    @Override
    public boolean updateEntity(YyCompany yyCompany) {
    	QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("REPORTID", yyCompany.getReport().getReportid());
        reportModelMapper.update(yyCompany.getReport(),queryWrapper);

        QueryWrapper queryWrapperCompany = new QueryWrapper();
        queryWrapperCompany.eq("COMPANYID", yyCompany.getCompanyid());
        baseMapper.update(yyCompany,queryWrapperCompany);

        //==
        baseMapper.deleteComPageByCompanyId(yyCompany.getCompanyid());
        baseMapper.insertBiPageSet(yyCompany.getCompanyid(), yyCompany.getBiPageSet());
        return true;
    }

    @Override
    public void insertPageIdsByStrList(BigDecimal companyId, List<String> pageIdList) {
        if (pageIdList == null || pageIdList.isEmpty()) {
            return;
        }
        baseMapper.insertPageIdsByStrList(companyId, pageIdList);
    }

}
