package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessMark;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.mapper.TblAssessMapper;
import com.huabo.monitor.mapper.TblAssessMarkMapper;
import com.huabo.monitor.mapper.TblAssessMarkVoMapper;
import com.huabo.monitor.service.PjgzService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.PageBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PjgzServiceImpl extends ServiceImpl<TblAssessMapper, TblAssess> implements PjgzService {

    @Resource
    private TblAssessMarkMapper tblAssessMarkMapper;

    @Resource
    TblAssessMarkVoMapper markVoMapper;

    
    /**
     * 评价管理-评价跟踪-主页查询
     * @param pageNumber
     * @param assNumnber
     * @param assName
     * @param startDate
     * @param startDates
     * @param endDate
     * @param endDates
     * @param staff
     * @return
     */
    public IPage<TblAssess> initiatePjgl (Integer pageNumber, String assNumnber, String assName, String startDate, String startDates, String endDate, String endDates, TblStaffUtil staff){


        boolean isAudit = false;
        if (staff.getLinkOrg().getAudittype() != null
                && staff.getLinkOrg().getAudittype().equals(TblOrganization.AUDITTYPE)) {
            isAudit = true;
        }
        IPage<TblAssess> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        QueryWrapper<TblAssess> qw = new QueryWrapper<>();
        qw.eq("tblcomany", staff.getCurrentOrg().getOrgid());
        if (!isAudit) {
            qw.and(wr -> wr.eq("asssponsor", staff.getUsername()).or().eq("leaderid", staff.getStaffid()));
        }
        if (StringUtils.isNotBlank(assNumnber)) {
            qw.like("assessid", assNumnber);
        }

        if (StringUtils.isNotBlank(assName)) {
            qw.like("assessname", assName);
        }

        if (!StringUtils.isBlank(startDate)) {
            qw.ge("startdate", DateUtils.parse(startDate));
        }

        if (!StringUtils.isBlank(startDates)) {
            qw.le("startdate", DateUtils.parse(startDates));
        }

        if (!StringUtils.isBlank(endDate)) {
            qw.ge("enddate", DateUtils.parse(endDate));

        }
        if (!StringUtils.isBlank(endDates)) {
            qw.le("enddate",DateUtils.parse(endDates) );

        }
        qw.orderByAsc("assstatus").orderByDesc("assid");

        return this.page(iPage, qw);
    }


    /**
     * 项目跟踪---评价对象列表
     *
     * @param
     * @return
     */

    @Override
    public  IPage<TblAssessMarkVo> findMarkByOrgGroup( IPage<TblAssessMarkVo> iPage, BigDecimal assId) {

        return this.markVoMapper.findMarkByOrgGroup(iPage,assId);
    }

    @Override
    public IPage<Map<String, Object>> findByperson(IPage<Map<String, Object>> page, BigDecimal assid, BigDecimal orgid) {
        return this.markVoMapper.findByperson(page,assid,orgid);
    }


	@Override
	public PageInfo<TblAssessMarkVo> findMarkByOrgGroupNew(Integer pageNumber, BigDecimal assId) {
		// TODO Auto-generated method stub
		return    PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblAssessMarkMapper.findMarkByOrgGroupNew(assId));
	}


	@Override
	public PageInfo<Map<String, Object>> findBypersonNew(Integer pageNumber, BigDecimal assid, BigDecimal orgid) {
		// TODO Auto-generated method stub
		return    PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblAssessMarkMapper.findBypersonNew(assid,orgid));
	}
}
