package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.entity.TblRepAtt;
import com.huabo.compliance.entity.TblReport;
import com.huabo.compliance.mapper.TblAttachmentMapper;
import com.huabo.compliance.mapper.TblRepAttMapper;
import com.huabo.compliance.mapper.TblReportMapper;
import com.huabo.compliance.mapper.YhrPageMapper;
import com.huabo.compliance.service.PjbgService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class PjbgServiceImpl implements PjbgService {

    @Resource
    YhrPageMapper yhrPageMapper;

    @Resource
    TblReportMapper  reportMapper;
    @Resource
    TblRepAttMapper  repAttMapper;
    @Resource
    TblAttachmentMapper attachmentMapper;
    @Override
    public IPage<TblReport> findAll(Integer pageNumber, String name, String startDate, String endDate, String type, BigDecimal orgid,Integer pageSize) {
        IPage<TblReport> page=new Page<>(pageNumber, pageSize);

        String sql = "select * from TBL_REPORT where type = '"+type+"' and orgid = '"+orgid+"' ";

        if (StringUtils.isNotEmpty(name)) {
            sql += " and REPORTNAME like '%"+name+"%'";

        }

        if (StringUtils.isNotEmpty(startDate)) {
                sql += " and REPORTTIME  >= to_date('"+startDate+"','yyyy-MM-dd')";

        }



        if (StringUtils.isNotEmpty(endDate)) {
            sql += " and REPORTTIME  <= to_date('"+endDate+"','yyyy-MM-dd')";

        }


        sql += " order by REPORTTIME DESC, (CASE WHEN REPORTSTATUS is NULL THEN '0' ELSE REPORTSTATUS END) asc";


        return this.reportMapper.getPage(page,sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReport(TblReport report, String attids) {
          this.reportMapper.insert(report);
          if (StringUtils.isNotBlank(attids)) {
            String[] ids = attids.split(",");
            TblRepAtt  repAtt=new TblRepAtt();
            for (int i = 0; i < ids.length; i++) {
                repAtt.setReportid(report.getReportid());
                repAtt.setAttid(new BigDecimal(ids[i]));
                this.repAttMapper.insert(repAtt);
            }
         }

    }

    @Override
    public List<TblAttachment> getRepAttByReportId(BigDecimal reportid) {
        String sql="select * from TBL_ATTACHMENT where attid in (select attid from TBL_REP_ATT where reportid="+reportid+"  )";
        return this.attachmentMapper.getListBySql(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRepAtt(TblAttachment a, String reportid) {
        attachmentMapper.insert(a);
        TblRepAtt  repAtt=new TblRepAtt();
        repAtt.setReportid(new BigDecimal(reportid));
        repAtt.setAttid(a.getAttid());
        this.repAttMapper.insert(repAtt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAttAndRepAtt(BigDecimal attid, String reportid) {

        String sql="delete from TBL_REP_ATT where attid="+attid;
        this.yhrPageMapper.delete(sql);

        attachmentMapper.deleteById(attid);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delReport(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] reportids = ids.split(",");
            TblRepAtt  repAtt=new TblRepAtt();
            for (int i = 0; i < reportids.length; i++) {
                // 删除中间表数据
                String sql="delete from TBL_REP_ATT where reportid="+reportids[i];
                yhrPageMapper.delete(sql);
                //  删除报告
                this.reportMapper.deleteById(new BigDecimal(reportids[i]));
            }
        }
    }
}
