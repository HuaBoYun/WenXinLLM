package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblNbsjBugEntity;
import com.huabo.monitor.entity.TblRepAtt;
import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.mapper.TblNbsjBugMapper;
import com.huabo.monitor.mapper.TblRepAttMapper;
import com.huabo.monitor.mapper.TblReportBugMapper;
import com.huabo.monitor.mapper.TblReportMapper;
import com.huabo.monitor.mapper.YhrPageMapper;
import com.huabo.monitor.service.PjbgService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.PageBean;
import com.huabo.monitor.vo.param.TblReportParam;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
    
    @Resource
    TblNbsjBugMapper tblNbsjBugMapper;
    
    
    @Resource TblReportBugMapper tblReportBugMapper;
    @Override
    public IPage<TblReport> findAll(Integer pageNumber, String name, String startDate, String endDate, String type, BigDecimal orgid, BigDecimal staffid,Integer authorityType) throws Exception{
        IPage<TblReport> page=new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql = "select * from TBL_REPORT where type = '"+type+"' and orgid = '"+orgid+"' ";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        TblReport r=new TblReport();
        r.setType(type);
        r.setOrgid(orgid);
        r.setReportname(name);
        r.setReporttime(sdf.parse(startDate));
        r.setCreatestaffid(staffid);
        if (StringUtils.isNotEmpty(name)) {
            sql += " and REPORTNAME like '%"+name+"%'";
        }
        if (StringUtils.isNotEmpty(startDate)) {
                sql += " and REPORTTIME  >= to_date('"+startDate+"','yyyy-MM-dd')";
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql += " and REPORTTIME  <= to_date('"+endDate+"','yyyy-MM-dd')";
        }
		if (Objects.equals(authorityType,0)){
			sql += " and CREATESTAFFID  ='"+staffid+"'";
		}
        //sql += " order by REPORTTIME DESC, (CASE WHEN REPORTSTATUS is NULL THEN '0' ELSE REPORTSTATUS END) asc";
        sql += " order by REPORTID DESC";
        return this.reportMapper.getPage(page,sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReport(TblReport report, String attids) {
    	if(report.getReportid()!=null&&report.getReportid().compareTo(new BigDecimal("0"))!=0){
    		reportMapper.updateById(report);
    	}else{
    		 report.setReportid(RandomUtil.uuBigDecimalId());
             this.reportMapper.insert(report);
    	}
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
    public void saveRepAtt(TblAttachment a, String reportid,String isDecision) {
    	a.setAttid(RandomUtil.uuBigDecimalId());
        attachmentMapper.insert(a);
        TblRepAtt  repAtt=new TblRepAtt();
        repAtt.setReportid(new BigDecimal(reportid));
        repAtt.setAttid(a.getAttid());
        if(StringUtils.isNotBlank(isDecision)&&isDecision.equals("1")){
        	 repAtt.setIsdecision("1");
        }else{
        	 repAtt.setIsdecision("0");
        }
        this.repAttMapper.insert(repAtt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAttAndRepAtt(BigDecimal attid, String reportid) {

        String sql="delete from TBL_REP_ATT where attid="+attid;
        this.yhrPageMapper.delete(sql);
        attachmentMapper.deleteEntity(attid);

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
               //删除关联内控风险缺陷
                tblReportBugMapper.deleteByReportId(new BigDecimal(reportids[i]));
            }
        }
    }

	@Override
	public PageInfo<TblReport> findAllNewPage(Integer pageNumber, String name, String startDate, String endDate,
			String type, BigDecimal orgid, BigDecimal staffid, Integer authorityType,TblStaffUtil user,String year,String reporttype) throws Exception {
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        TblReportParam r=new TblReportParam();
	        r.setType(type);
	        r.setOrgid(orgid);
	        r.setReportname(name);
	        r.setReporttype(reporttype);
	        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(startDate)){
		        r.setStartDate(startDate);
	        }
	        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(endDate)){
		        r.setEndDate(endDate);
	        }
	        
	        if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(year)){
		        r.setStartDateYear(year+"-01-01");
		        r.setEndDateYear(year+"-12-31");
	        }
	        
	    	String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), authorityType == 0, "orgid", "LINKDEPTID", "createstaffid", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
	        PageInfo<TblReport> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->reportMapper.findList(r,sql));
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
	        
	        return pageInfo;
	}

	@Override
	public List<TblNbsjBugEntity> selectNbsjBugList(BigDecimal reportid) {
		// TODO Auto-generated method stub
		 List<TblNbsjBugEntity> list=null;
		try {
			list=tblNbsjBugMapper.selectNbsjBugList(reportid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
