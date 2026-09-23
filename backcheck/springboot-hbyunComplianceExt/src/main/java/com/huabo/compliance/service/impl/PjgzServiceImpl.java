package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.compliance.entity.TblAssess;
import com.huabo.compliance.entity.TblAssessMarkVo;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.mapper.TblAssessMapper;
import com.huabo.compliance.mapper.TblAssessMarkMapper;
import com.huabo.compliance.mapper.TblAssessMarkVoMapper;
import com.huabo.compliance.service.PjgzService;
import com.huabo.compliance.util.ConstClass;
import com.huabo.compliance.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@Service
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
}
