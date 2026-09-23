package com.huabo.compliance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.compliance.entity.TblAssesselement;
import com.huabo.compliance.mapper.TblAssesselementMapper;
import com.huabo.compliance.service.ITblAssesselementService;
import com.huabo.compliance.util.ConstClass;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
public class TblAssesselementServiceImpl extends ServiceImpl<TblAssesselementMapper, TblAssesselement> implements ITblAssesselementService {
	
	@Resource
    TblAssesselementMapper tblAssesselementMapper;
	
	@Override
    public IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement,Integer pagesize) {
        IPage<TblAssesselement> page = new Page<>(pageNumber, pagesize);

        List<Object> listparam = new ArrayList<>();
        String hql = "select * from TBL_ASSESSELEMENT t where 1=1  and t.tblComany = "+orgid;
        if (StringUtils.isNotBlank(assesselement.getElementname())) {
            hql += " and t.elementname LIKE '%" + assesselement.getElementname() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getBusinessattribute())) {
            hql += " and t.businessattribute =  "+assesselement.getBusinessattribute();
        }
        if (StringUtils.isNotBlank(assesselement.getBusinesstype())) {
            hql += " and t.businesstype LIKE '%" + assesselement.getBusinesstype() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            hql += " and t.elementNumber LIKE '%" + assesselement.getElementnumber() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getAuditpoint())) {
            hql += " and t.auditpoint LIKE '%" + assesselement.getAuditpoint() + "%'";
        }
        hql += " order by t.elementNumber desc, t.elementname desc";

        return this.tblAssesselementMapper.getSqlPage(page, hql);
    }
	
	@Override
    public TblAssesselement getNumber(String number, String orgid) {
        List<TblAssesselement> list = this.tblAssesselementMapper.getNumber(number, orgid);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
	
	@Override
    public void add(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.insert(tblAssesselement);
    }
	
	@Override
    public void Update(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.updateById(tblAssesselement);
    }
	
	@Override
    public TblAssesselement findById(BigDecimal id) {
        return tblAssesselementMapper.selectById(id);
    }
	
	@Override
    public TblAssesselement get(BigDecimal id) {
        return tblAssesselementMapper.selectById(id);
    }
	
	@Override
    public void delete(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.deleteById(tblAssesselement);
    }
	
	@Override
    public List<TblAssesselement> getAssEssByIn(String assessIds) {
		 String hql = "select * from TBL_ASSESSELEMENT t  where t.ASSELEID   in ("+assessIds+")";
        return tblAssesselementMapper.getAssEssByIn(hql);
    }

    @Override
    public List<TblAssesselement> getComany(String orgid) {
        return tblAssesselementMapper.getComany(orgid);
    }
    
    @Override
    public IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement, String notInStr) {
        IPage<TblAssesselement> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        List<Object> listparam = new ArrayList<>();
        String hql = "select * from TBL_ASSESSELEMENT t where 1=1  and t.tblComany = "+orgid;
        listparam.add(orgid);
        if (StringUtils.isNotBlank(notInStr)) {
            hql += " and t.asseleid not in (" + notInStr + ")";
        }
        if (StringUtils.isNotBlank(assesselement.getElementname())) {
            hql += " and t.elementname like '%" + assesselement.getElementname() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getBusinessattribute())) {
            hql += " and t.businessattribute = " +assesselement.getBusinessattribute();
        }
        if (StringUtils.isNotBlank(assesselement.getBusinesstype())) {
            hql += " and t.businesstype = "+assesselement.getBusinesstype();
        }
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            hql += " and t.elementNumber like '%" + assesselement.getElementnumber() + "%'";
            listparam.add("%" + assesselement.getElementnumber() + "%");
        }
        hql += "order by t.elementNumber , t.elementname desc";
        return this.tblAssesselementMapper.getSqlPage(page, hql);
    }
}
