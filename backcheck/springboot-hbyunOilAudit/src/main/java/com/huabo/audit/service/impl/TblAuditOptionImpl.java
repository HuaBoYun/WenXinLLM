package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.mapper.TblAuditOptionMapper;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAdvicenoteMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBorrowRecordMapper;
import com.huabo.audit.oracle.mapper.TblNbsjFactbookMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSheetMapper;
import com.huabo.audit.oracle.mapper.TblReportMapper;
import com.huabo.audit.service.TblAuditOptionService;

@Service
public class TblAuditOptionImpl implements TblAuditOptionService {

	@Resource
	private TblAuditOptionMapper tblAuditOptionMapper;
	
	@Resource
	private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
	
	@Resource
	private TblCirculationMapper tblCirculationMapper;
	
	@Autowired
    private TblNbsjAdvicenoteMapper tblNbsjAdvicenoteMapper;
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	@Resource
	private TblReportMapper tblReportMapper;
	@Autowired
    private TblNbsjBorrowRecordMapper tblNbsjBorrowRecordMapper;
	@Autowired
	private TblNbsjSheetMapper tblNbsjSheetMapper;
	@Autowired
    private TblNbsjFactbookMapper tblNbsjFactbookMapper;
	
	
	@Override
	public List<TblAuditOption> findOptionByRelationId(String id, Integer cyId) throws Exception {
    	List<TblAuditOption> aoList = this.tblAuditOptionMapper.selectListByCyId(id,cyId);
    	return aoList;
	}

	@Override
	public JsonBean saveAuditOptionInfo(TblCirculation cy, TblAuditOption opt) throws Exception {
        if (opt.getOptState().equals("修改") || opt.getOptState().equals("退回") || opt.getOptState().equals("驳回")){
            cy.setCystate(TblCirculation.STATE_TZ);
            //判断为 从计划审批过来的流程
            if(cy.getCytype().equals(TblCirculation.TYPE_JHSP)){
                List<TblNbsjAuditplan> list = this.tblNbsjAuditplanMapper.selectAuditPlanByIds(opt.getRelationId().toString(),false);
                for (TblNbsjAuditplan plan : list) {
                	plan.setStarttime(null);
                	plan.setEndtime(null);
                	plan.setOpinionstatus(TblNbsjAuditplan.SPTZ);
                    this.tblNbsjAuditplanMapper.updateEntity(plan);
				}
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJTZS)){//审计通知书
                TblNbsjAdvicenoteEntity na = tblNbsjAdvicenoteMapper.getById(opt.getRelationId().toString());
                na.setContent(null);
                na.setStatus(TblNbsjAdvicenoteEntity.XTZ);
                tblNbsjAdvicenoteMapper.updateEntity(na);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_XMSP)){//项目审批
                TblNbsjProject na = tblNbsjProjectMapper.getById(opt.getRelationId().toString());
                na.setExamineType(TblNbsjProject.EXAMINETYPE5);
                tblNbsjProjectMapper.updateEntity(na);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBG)){//审计报告
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setRepdesc(null);
                report.setReportstatus(TblReportEntity.XTZ);
                tblReportMapper.updateEntity(report);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBGFH)){//审计报告复核
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setRepdesc(null);
                report.setReportstatus(TblReportEntity.FHTZ);
                tblReportMapper.updateEntity(report);
            }else if(cy.getCytype().equals(TblNbsjBorrowRecordEntity.TYPE_DAJY)){//审计档案
            	TblNbsjBorrowRecordEntity tblNbsjBorrowRecord = tblNbsjBorrowRecordMapper.getById(opt.getRelationId().toString());
            	tblNbsjBorrowRecord.setStatus(TblNbsjBorrowRecordEntity.SPTZ);//退回
            	tblNbsjBorrowRecordMapper.updateEntity(tblNbsjBorrowRecord);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_DGFH)){//底稿
            	TblNbsjSheetEntity sheet = tblNbsjSheetMapper.getById(opt.getRelationId().toString());
                sheet.setState(TblNbsjSheetEntity.STATE5);
                tblNbsjSheetMapper.updateEntity(sheet);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SSQRS)){
                TblNbsjFactbookEntity book = tblNbsjFactbookMapper.getById(opt.getRelationId().toString());
                book.setStatus(TblNbsjFactbookEntity.STATUS_3);
                tblNbsjFactbookMapper.updateEntity(book);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBGZQYJ)){//审计报告征求意见
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setRepdesc(null);
                report.setReportstatus(TblReportEntity.ZQYJTZ);
                tblReportMapper.updateEntity(report);
            }
            this.tblCirculationMapper.updateCirculationInfoById(cy);
        }
        if (opt.getOptState().equals("提交")&&cy.getCystate().equals(TblCirculation.STATE_TZ)){
        	//重新提交
        	cy.setCystate(TblCirculation.STATE_FQ);
        	//判断为 从计划审批过来的流程
        	if(cy.getCytype().equals(TblCirculation.TYPE_JHSP)){
        		List<TblNbsjAuditplan> list = this.tblNbsjAuditplanMapper.selectAuditPlanByIds(opt.getRelationId().toString(),false);
        		for (TblNbsjAuditplan plan : list) {
        			plan.setOpinionstatus(TblNbsjAuditplan.SPKA);
        			this.tblNbsjAuditplanMapper.updateEntity(plan);
        		}
        	}else if(cy.getCytype().equals(TblCirculation.TYPE_SJTZS)){
        		TblNbsjAdvicenoteEntity na = tblNbsjAdvicenoteMapper.getById(opt.getRelationId().toString());
                na.setContent(null);
        		na.setStatus(TblNbsjAdvicenoteEntity.SPZ);
        		tblNbsjAdvicenoteMapper.updateEntity(na);
        	}else if(cy.getCytype().equals(TblCirculation.TYPE_XMSP)){
                TblNbsjProject na = tblNbsjProjectMapper.getById(opt.getRelationId().toString());
                na.setExamineType(TblNbsjProject.EXAMINETYPE2);
                tblNbsjProjectMapper.updateEntity(na);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBG)){//审计报告
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setReportstatus(TblReportEntity.SPZ);
                tblReportMapper.updateEntity(report);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBGFH)){//审计报告复核
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setReportstatus(TblReportEntity.FHZ);
                tblReportMapper.updateEntity(report);
            }else if(cy.getCytype().equals(TblNbsjBorrowRecordEntity.TYPE_DAJY)){//审计档案
            	TblNbsjBorrowRecordEntity tblNbsjBorrowRecord = tblNbsjBorrowRecordMapper.getById(opt.getRelationId().toString());
            	tblNbsjBorrowRecord.setStatus(TblNbsjBorrowRecordEntity.SPKA);//通过
            	tblNbsjBorrowRecordMapper.updateEntity(tblNbsjBorrowRecord);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_DGFH)){//底稿
            	TblNbsjSheetEntity sheet = tblNbsjSheetMapper.getById(opt.getRelationId().toString());
                sheet.setState(TblNbsjSheetEntity.STATE2);
                tblNbsjSheetMapper.updateEntity(sheet);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SSQRS)){
                TblNbsjFactbookEntity book = tblNbsjFactbookMapper.getById(opt.getRelationId().toString());
                book.setStatus(TblNbsjFactbookEntity.STATUS_2);
                tblNbsjFactbookMapper.updateEntity(book);
            }else if(cy.getCytype().equals(TblCirculation.TYPE_SJBGZQYJ)){//审计报告征求意见
            	TblReportEntity report = tblReportMapper.getById(opt.getRelationId().toString());
                report.setRepdesc(null);
                report.setReportstatus(TblReportEntity.ZQYJ);
                tblReportMapper.updateEntity(report);
            }
        	this.tblCirculationMapper.updateCirculationInfoById(cy);
        }
        //审批意见表
    	opt.setCreateDate(new Date());
    	this.tblAuditOptionMapper.saveEnity(opt);
    	return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public List<TblAuditOption> findOptionByRelationId(String id) {
		
		try {
			List<TblAuditOption> list = tblAuditOptionMapper.getOptionByRelationId(id);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
