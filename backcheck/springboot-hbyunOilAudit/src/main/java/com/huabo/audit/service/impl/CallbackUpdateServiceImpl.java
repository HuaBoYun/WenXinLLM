package com.huabo.audit.service.impl;


import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblNbsjFactbookEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAdvicenoteMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBorrowRecordMapper;
import com.huabo.audit.oracle.mapper.TblNbsjFactbookMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSheetMapper;
import com.huabo.audit.oracle.mapper.TblReportMapper;
import com.huabo.audit.service.CallbackUpdateService;


/**
 * Created by taoyb on 2017-02-15.
 * 工作流审批最后一步  更新状态
 */
@Service
public class CallbackUpdateServiceImpl implements CallbackUpdateService {
	@Autowired
	private TblCirculationMapper cirmapper;
	
	@Autowired
	private TblNbsjAuditplanMapper planmapper; 
	
	@Autowired
	private TblNbsjAdvicenoteMapper notemapper;
	
	@Autowired
	private TblNbsjProjectMapper projectmapper;
	
	@Autowired
	private TblReportMapper reportmapper;
	
	@Autowired
	private TblNbsjSheetMapper sheetmapper;
	
	@Autowired
	private TblNbsjBorrowRecordMapper recordmapper;
	
	@Autowired
	private TblNbsjFactbookMapper bookmapper;
	
	private TblNbsjQuestionMapper questionmapper;
	
	
	
	/*
	 * @Resource TblNbsjPlanFormService tblNbsjPlanFormService;
	 * 
	 * @Resource TblnbsjauditPlanService tblnbsjauditPlanService;
	 * 
	 * @Resource TblNbsjAdvicenoteService tblNbsjAdvicenoteService;
	 * 
	 * @Resource TblnbsjProjectService tblnbsjProjectService;
	 * 
	 * @Resource TblReportService tblReportService;
	 * 
	 * @Resource TBlNbsjSheetService tBlNbsjSheetService;
	 * 
	 * @Resource TblNbsjFactbookService tblNbsjFactbookService;
	 * 
	 * @Resource TblNbsjQuestionService tblNbsjQuestionService;
	 * 
	 * @Resource public TblNbsjBorrowRecordService tblNbsjBorrowRecordService;
	 * 
	 * @Resource public TblWorksheetService tblWorksheetService;
	 * 
	 * @Resource public TblNbkzFactbookService tblNbkzFactbookService;
	 * 
	 * @Resource public TblBugService tblBugService;
	 * 
	 * @Resource public TblNbsjProjectDataProService dataProService;
	 */
	
    @Override
    public void updateStatus(BigDecimal cId, String status) {
        TblCirculation tblCirculation = cirmapper.findById(cId.toString());
        String fromid = tblCirculation.getCyurl().split("=")[1].split("&")[0];
        if(tblCirculation.getCytype().equals(TblCirculation.TYPE_JHSP)){
            //TblNbsjPlanForm form = tblNbsjPlanFormService.get(fromid);
            //TblnbsjauditPlan
            //form.setPfState(StringUtils.isNotBlank(status) && status.trim().equals(tblCirculation.STATE_TG)?form.PFSTATE3:form.PFSTATE4);
            //tblNbsjPlanFormService.saveNbsjPlanForm(form);
            //List<TblnbsjauditPlan> list = tblnbsjauditPlanService.findCheckJH(form.getPfPlanId(),false);
        	try {
            	TblNbsjAuditplan p =planmapper.selectNbsjAuditPlanEntityById(Integer.parseInt(fromid));
            	//TblnbsjauditPlan p = tblnbsjauditPlanService.get(fromid); 
            	p.setStarttime(null);
            	p.setEndtime(null);
                p.setOpinionstatus(StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_TG)?TblNbsjAuditplan.SPWC:TblNbsjAuditplan.SPZZ);
				planmapper.updateEntity(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else if (tblCirculation.getCytype().equals(TblCirculation.TYPE_SJTZS)){//判断 从审计通知书中过来的审批
        	try {
				TblNbsjAdvicenoteEntity na = notemapper.getById(fromid);
				na.setContent(null);
				na.setStatus(StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_TG)?TblNbsjAdvicenoteEntity.YTG:TblNbsjAdvicenoteEntity.YZZ);
				notemapper.updateEntity(na);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else if (tblCirculation.getCytype().equals(TblCirculation.TYPE_XMSP)){//判断 从项目审批中过来的审批
        	TblNbsjProject pro = projectmapper.getById(fromid);
            Integer type = null;
            if(StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_TG)){
                type = TblNbsjProject.EXAMINETYPE4;
            }else if (StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_ZD)){
                type = TblNbsjProject.EXAMINETYPE6;
            }else {
                type = TblNbsjProject.EXAMINETYPE3;
            }
            pro.setExamineType(type);
            try {
				projectmapper.updateEntity(pro);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_SJBG)){//判断 从审计报告中过来的
        	TblReportEntity report = reportmapper.getById(fromid);
            report.setReportstatus(status.equals(TblCirculation.STATE_TG)?TblReportEntity.YTG:TblReportEntity.YZZ);
            try {
            	report.setRepdesc(null);
				reportmapper.updateEntity(report);
			} catch (Exception e) {
				e.printStackTrace();
			};
        }else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_SJBGFH)){//判断 从审计报告复核中过来的
        	TblReportEntity report = reportmapper.getById(fromid);
            report.setReportstatus(status.equals(TblCirculation.STATE_TG)?TblReportEntity.FHTG:TblReportEntity.FHZZ);
            try {
            	report.setRepdesc(null);
				reportmapper.updateEntity(report);
			} catch (Exception e) {
				e.printStackTrace();
			};
		}else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_SJBGZQYJ)){//判断 从审计报告征求意见过来的
        	TblReportEntity report = reportmapper.getById(fromid);
            report.setReportstatus(status.equals(TblCirculation.STATE_TG)?TblReportEntity.ZQYJTG:TblReportEntity.ZQYJZZ);
            try {
            	report.setRepdesc(null);
				reportmapper.updateEntity(report);
			} catch (Exception e) {
				e.printStackTrace();
			};
		} /*
			 * else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_SJBGZQYJ)){//判断
			 * 从审计报告征求意见中过来的 TblReportEntity report = reportmapper.getById(fromid);
			 * report.setReportstatus(StringUtils.isNotBlank(status) &&
			 * status.trim().equals(tblCirculation.STATE_TG)?TblReportEntity.FHTG.toString()
			 * :TblReportEntity.FHZZ.toString()); try { reportmapper.updateEntity(report); }
			 * catch (Exception e) { e.printStackTrace(); } }
			 */else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_DGFH)){//判断 从审计底稿复核中过来的
        	TblNbsjSheetEntity sheet = sheetmapper.getById(fromid);
            sheet.setState(StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_TG)?TblNbsjSheetEntity.STATE4:TblNbsjSheetEntity.STATE3);
        	try {
				sheetmapper.updateEntity(sheet);
				List<TblNbsjQuestionEntity>  listq = questionmapper.findNbsjQuestionBySheetIdSp(sheet.getSheetId());
                for (TblNbsjQuestionEntity ques : listq) {
                	ques.setStatus(TblNbsjQuestionEntity.STATUSYES);
                    questionmapper.updateEntity(ques);
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }else if(tblCirculation.getCytype().equals(TblNbsjBorrowRecordEntity.TYPE_DAJY)){//判断 借阅管理
        	TblNbsjBorrowRecordEntity tblNbsjBorrowRecord = recordmapper.getById(fromid);
        	tblNbsjBorrowRecord.setStatus(TblNbsjBorrowRecordEntity.SPZZ);//完成
        	recordmapper.updateEntity(tblNbsjBorrowRecord);
        }else if(tblCirculation.getCytype().equals(TblCirculation.TYPE_SSQRS)){//判断 从事实确认书复核中过来的
        	try {
	        	TblNbsjFactbookEntity book = bookmapper.getById(fromid);
	            if(StringUtils.isNotBlank(status) && status.trim().equals(TblCirculation.STATE_TG)) {
	            	book.setStatus(TblNbsjFactbookEntity.STATUS_5);
	            	List<TblNbsjSheetEntity> list = this.sheetmapper.findNbsjSheetByFactBookIdSp(fromid, book.getTblNbsjProject().getProjectId().toString());
	            	TblNbsjSheetEntity sheet = null;
	            	List<TblNbsjQuestionEntity> listq = null;
	                for (int i = 0;i<list.size();i++){
	                    sheet = list.get(i);
	                    listq = questionmapper.findNbsjQuestionBySheetIdSp(sheet.getSheetId());
	                    for (TblNbsjQuestionEntity ques : listq) {
	                    	questionmapper.updateState(ques.getQuestionId(),TblNbsjQuestionEntity.STATUSYES);
						}
	                }
	            }else {
	            	book.setStatus(TblNbsjFactbookEntity.STATUS_4);
	            }
	            
//	            List<TblNbsjQuestionEntity> list = questionmapper.getAuditQuestionByfactidList(book.getFactid().toString());
//	            if(list!=null && list.size()>0) {
//	            	for (TblNbsjQuestionEntity q : list) {
//	            		 q.setStatus(TblNbsjQuestionEntity.STATUSYES);
//	            		 questionmapper.updateEntity(q);
//					}
//	            }
           
           
				bookmapper.updateEntity(book);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}
