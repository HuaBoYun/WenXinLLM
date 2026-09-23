package com.huabo.audit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessVariableEnum;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.TblCirculationService;
import com.huabo.audit.service.TblNbsjCertificateService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.HttpClient;
import com.huabo.audit.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class TblNbsjCertificateServiceImpl implements TblNbsjCertificateService {

    @Resource
    private TblNbsjCertificateMapper tblNbsjCertificateMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private TblNbsjProjectService tblNbsjProjectService;

    @Resource
    private TblCirculationMapper tblCirculationMapper;

    @Resource
    private TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;

    @Resource
    private TblProcessAnalysisMapper tblProcessAnalysisMapper;

    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblMyTaskMapper tblMyTaskMapper;

    @Resource
    private TblNbsjTeamstaffMapper tblNbsjTeamstaffMapper;

    @Resource
    private TblNbsjProjectMapper tblNbsjProjectMapper;

    @Resource
    private TblNbsjQuestionMapper questionmapper;

    @Resource
    private TblCirculationService tblCirculationService;

    @Resource
    private TblProcessSettingMapper processSettingMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean getNbsjCertificateListPage(String token, Integer pageNumber, Integer pageSize, String projectName, String auditMatter, String auditAbstract, Integer projectId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }


        if (projectId == null) {
            //==查询当前实施的项目！
            TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            projectId = tnp.getProjectId();
        }

        if (null == projectId) {
            return ResponseFormat.retParam(0, 30003, null);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblNbsjCertificate> pageInfo = new PageInfo<TblNbsjCertificate>();

        Integer finalProjectId = projectId;
        com.github.pagehelper.PageInfo<TblNbsjCertificate> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjCertificateMapper.selectNbsjCertificateListByPageInfo(pageInfo, loginStaff.getCurrentOrg().getOrgid(), projectName, auditMatter, auditAbstract, finalProjectId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(pageInfo2.getList());
        pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean getNbsjCertificateList(String token, TblNbsjCertificate categories, Integer pageNumber, Integer pageSize, String sheetid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        //==查询当前实施的项目！
        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, resultMap);
        }
        Integer projectId = tnp.getProjectId();
        if (null == projectId) {
            return ResponseFormat.retParam(0, 30003, resultMap);
        }
        categories.setProjectId(projectId);
        categories.setOrgId(Integer.valueOf(loginStaff.getCurrentOrg().getOrgid().toString()));
        PageInfo<TblNbsjCertificate> pageInfo = new PageInfo<TblNbsjCertificate>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setCondition(categories);
        //此处审计取证单的列表需要排除已经被勾选的
        pageInfo.setTlist(this.tblNbsjCertificateMapper.selectNbsjCertificateListByPageInfoDg(pageInfo, sheetid));
        pageInfo.setTotalRecord(this.tblNbsjCertificateMapper.selectNbsjCertificateListCountByPageInfoDg(pageInfo, sheetid));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean certificateSave(TblNbsjCertificate certificate, String token, String attids) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

//		Integer count = this.tblNbsjWorkReportMapper.selectPlanCodeByOrgid(certificate);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Integer projectId = null;


        if (certificate != null && certificate.getCertificateId() != null) {
            TblNbsjCertificate plan = this.tblNbsjCertificateMapper.selectById(certificate.getCertificateId());
            projectId = plan.getProjectId();
        } else {
            TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, resultMap);
            }
            projectId = tnp.getProjectId();
        }
        //==查询当前实施的项目！


        if (null == projectId) {
            return ResponseFormat.retParam(0, 30003, resultMap);
        }
        certificate.setProjectId(projectId);

        certificate.setAuditUserId(loginStaff.getStaffid().intValue());
        certificate.setCreateDate(new Date());
        //根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；

        if (certificate.getCertificateId() != null) {
            //修改；
            //this.tblNbsjCertificateMapper.updateEntity(certificate);
            this.tblNbsjCertificateMapper.updateByPrimaryKeySelective(certificate);
            //==附件，先删除 再重新添加
            this.tblAttachmentMapper.deleteAttmentRelationCertificate(certificate.getCertificateId().intValue());
            if (attids != null && !"".equals(attids)) {
                String[] ids = attids.split(",");
                for (String id : ids) {
                    this.tblAttachmentMapper.insertAttmentRelationCertificate(id, certificate.getCertificateId().intValue());
                }
            }
        } else {
            //新增；
            //this.tblNbsjCertificateMapper.insertEntity(certificate);
            certificate.setStatus(0);
            this.tblNbsjCertificateMapper.insertSelective(certificate);
            //==附件
            if (attids != null && !"".equals(attids)) {
                String[] ids = attids.split(",");
                for (String id : ids) {
                    this.tblAttachmentMapper.insertAttmentRelationCertificate(id, certificate.getCertificateId().intValue());
                }
            }
        }
        resultMap.put("data", certificate);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public R removeAttInfoByAttId(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        return this.deleteRealtionAttInfo(attId);
    }

    private R deleteRealtionAttInfo(String attId) throws Exception {
        boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjCertificateMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());

        return R.success();
    }


    @Override
    public JsonBean certificateDelete(Integer certificateId, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjCertificate plan = this.tblNbsjCertificateMapper.selectById(certificateId);

        if (plan == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }

//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjWorkReportMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
        this.tblNbsjCertificateMapper.deleteById(certificateId);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean certificateDetail(String token, Integer certificateId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblNbsjCertificate plan = this.tblNbsjCertificateMapper.selectById(certificateId);
        resultMap.put("certificate", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean getCertificateList(String token, Integer sheetid) throws Exception {
        // TODO Auto-generated method stub
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblNbsjCertificate> list = this.tblNbsjCertificateMapper.selectCertificateList(sheetid);
        resultMap.put("data", list);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean certificateSubmit(String token, Integer certificateId, String examination) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //TblNbsjSheetEntity sheet = tblNbsjSheetMapper.getById(certificateId.toString());
        TblNbsjCertificate certificate = tblNbsjCertificateMapper.selectById(certificateId);
        if (certificate == null) {
            return ResponseFormat.retParam(0, 50002, null);
        }

        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, null);
        }
        TblCirculation cir = null;
        try {

            //获取审计组组长id所在表单对象
            Integer projectId = tnp.getProjectId();
            List<TblNbsjTeamstaffEntity> byProjectid = tblNbsjTeamstaffMapper.getByProjectid(projectId);
            TblNbsjTeamstaffEntity teamstaffEntity = byProjectid.get(0);

            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.getByFlowSetting(ProcessEnum.SJ_QZD.name());
            String busType = ProcessEnum.SJ_QZD.name();
            Integer orgid = Integer.parseInt(user.getLinkOrg().getOrgid().toString());
            List<TblProcessSettingEntity> settings = processSettingMapper.selectByOrgid(busType, orgid);
            String setting = ProcessEnum.SJ_QZD.name();
            if (settings != null && settings.size() > 0) {
                setting = settings.get(0).getModule();
                list = this.tblProcessAnalysisMapper.getByFlowSetting(setting);
            }

            HashMap<String, Object> fields = new HashMap<String, Object>();
            if (list != null && list.size() > 0) {
                for (TblProcessAnalysis tblAnalysis : list) {
                    TblProcessAnalusisUser analysisUser = this.tblProcessAnalusisUserMapper.findOnd(tblAnalysis.getAnalid().toString(), certificateId.toString());

                    if (analysisUser == null) {
                        analysisUser = new TblProcessAnalusisUser();
                        analysisUser.setAnalid(tblAnalysis.getAnalid().toString());
                        analysisUser.setFromid(certificateId.toString());
                        analysisUser.setSpdate(new Date());
                        if (tblAnalysis.getUserid() != null) {
                            analysisUser.setStaffid(user.getRealname());
                        } else {
                            analysisUser.setStaffid(tblAnalysis.getRolename());
                        }

                        //审计取证单流程参数
                        //提交人参数

                        if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                            fields.put("tcuserid", user.getStaffid().toString());
                            analysisUser.setStaffid(tblAnalysis.getRolename());
                            System.out.println("tcuserid:" + user.getStaffid());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zsuserid")) {
                            //审计组主审
                            if (tnp.getPmId() == null) {

                                return ResponseFormat.retParam(0, "审计组主审！", "");
                            }
                            fields.put("zsuserid", tnp.getPmId());
                            System.out.println("zsuserid:" + tnp.getPmId());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zzuserid")) {
                            //审计组组长
                            if (teamstaffEntity.getStaffid() == null) {

                                return ResponseFormat.retParam(0, "审计组组长！", "");
                            }
                            fields.put("zzuserid", teamstaffEntity.getStaffid());
                            System.out.println("zzuserid:" + teamstaffEntity.getStaffid());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zbsjuserid")) {
                            //总部审计部门人员
                            if (certificate.getAssistedzbuserid() == null) {

                                return ResponseFormat.retParam(0, "总部审计部门人员！", "");
                            }
                            fields.put("zbsjuserid", certificate.getAssistedzbuserid());
                            System.out.println("zbsjuserid:" + certificate.getAssistedzbuserid());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("xbfzr")) {

                            //协办部门负责人
                            if (certificate.getAssistedbmfzrid() != null) {

                                fields.put("xbfzr", certificate.getAssistedbmfzrid());
                                System.out.println("xbfzr:" + certificate.getAssistedbmfzrid());
                            }


                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("xbfgld")) {

                            //协办部门分管领导
                            if (certificate.getAssistedfgldid() != null) {
                                fields.put("xbfgld", certificate.getAssistedfgldid());
                                System.out.println("xbfgld:" + certificate.getAssistedfgldid());
                            }


                        } else {
                            //除此之外的参数都设置为当前提交人
                            if (tblAnalysis.getUserid() != null && !tblAnalysis.getUserid().equals("")) {
                                fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                                System.out.println(tblAnalysis.getUserid() + ":" + user.getStaffid().toString());
                            }
                        }
                        if (StringUtils.isNotBlank(certificate.getAssistedbmfzr())) {
                            fields.put("assistedType", "是");
                        } else {
                            fields.put("assistedType", "否");
                        }
                        this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
                    } else {
                        //审计取证单流程参数
                        //提交人参数

                        if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("tcuserid")) {
                            fields.put("tcuserid", user.getStaffid().toString());
                            analysisUser.setStaffid(tblAnalysis.getRolename());
                            System.out.println("tcuserid:" + user.getStaffid());
                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zsuserid")) {
                            //审计组主审
                            if (tnp.getPmId() == null) {

                                return ResponseFormat.retParam(0, "审计组主审！", "");
                            }
                            fields.put("zsuserid", tnp.getPmId());
                            System.out.println("zsuserid:" + tnp.getPmId());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zzuserid")) {
                            //审计组组长
                            if (teamstaffEntity.getStaffid() == null) {

                                return ResponseFormat.retParam(0, "审计组组长！", "");
                            }
                            fields.put("zzuserid", teamstaffEntity.getStaffid());
                            System.out.println("zzuserid:" + teamstaffEntity.getStaffid());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("zbsjuserid")) {
                            //总部审计部门人员
                            if (certificate.getAssistedzbuserid() == null) {

                                return ResponseFormat.retParam(0, "总部审计部门人员！", "");
                            }
                            fields.put("zbsjuserid", certificate.getAssistedzbuserid());
                            System.out.println("zbsjuserid:" + certificate.getAssistedzbuserid());

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("xbfzr")) {
                            //协办部门负责人
                            if (certificate.getAssistedbmfzrid() != null) {
                                fields.put("xbfzr", certificate.getAssistedbmfzrid());
                                System.out.println("xbfzr:" + certificate.getAssistedbmfzrid());
                            }

                        } else if (tblAnalysis.getUserid() != null && tblAnalysis.getUserid().equals("xbfgld")) {
                            //协办部门分管领导
                            if (certificate.getAssistedfgldid() != null) {
                                fields.put("xbfgld", certificate.getAssistedfgldid());
                                System.out.println("xbfgld:" + certificate.getAssistedfgldid());
                            }

                        } else {
                            //除此之外的参数都设置为当前提交人
                            if (tblAnalysis.getUserid() != null && !tblAnalysis.getUserid().equals("")) {
                                fields.put(tblAnalysis.getUserid(), user.getStaffid().toString());
                                System.out.println(tblAnalysis.getUserid() + ":" + user.getStaffid().toString());
                            }
                        }
                        if (StringUtils.isNotBlank(certificate.getAssistedbmfzr())) {
                            fields.put("assistedType", "是");
                        } else {
                            fields.put("assistedType", "否");
                        }
                    }

                }
            }
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(fields);
            Map<String, Object> map = HttpClient.startProcessAll(setting, jsonObject.toString());
            String object = (String) map.get("result");
            String processInstanceId = (String) map.get("processInstanceId");
            String processDefinitionKey = (String) map.get("processDefinitionKey");
            cir = this.tblCirculationService.saveTblCirculationnew(TblCirculation.TYPE_QZD, certificate.getProjectName(), certificate.getAuditMatter(), TblCirculation.URL_QZD + certificateId, user.getStaffid(), processInstanceId, processDefinitionKey, certificateId.toString());
            if (object != null && object.equals("true")) {
                List<TblMyTask> tasks = HttpClient.findByTask("", user.getStaffid().toString(), 1, 10000);
                if (tasks != null && tasks.size() > 0) {
                    for (TblMyTask task : tasks) {
                        if (task.getProcessInstanceId().equals(processInstanceId)) {
                            Map<String, Object> map1 = HttpClient.handleProcessJson(user.getStaffid().toString(), "通过", task.getTaskId());
                            String result = (String) map1.get("result");
                            if (result != null && result.equals("true")) {
                                String blande = "";
                                //查询执行人
                                String nextapprover = HttpClient.nextapprover(cir.getBusinesskey());
                                TblProcessAnalysis analysis = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, setting);
                                Integer number = 1;
                                String usertaskid = analysis.getUsertaskid();
                                Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                                blande = usertaskid.substring(0, usertaskid.length() - 1) + num;
                                TblProcessAnalysis analysis1 = this.tblProcessAnalysisMapper.findOndBytakdidstart(blande, ProcessEnum.SJ_QZD.name());
                                TblProcessAnalusisUser analysisUser = this.tblProcessAnalysisMapper.findOnd(analysis1.getAnalid().toString(), certificate.toString());
                                task.setFromid(certificateId.toString());
                                task.setApprover(user.getRealname());
                                task.setUsrid(user.getStaffid().toString());
                                task.setExamination(examination);
                                task.setProcessName(setting);
                                if (user.getTrole() != null && user.getTrole().getRname() != null) {
                                    task.setApprovalrole(user.getTrole().getRname());
                                }
                                task.setApprovaldate(new Date());
                                task.setResult("通过");
                                task.setCirid(cir.getCyid().toString());
                                if (nextapprover.contains("zsuserid")) {

                                    TblStaff zsuserid = tblStaffMapper.getById(tnp.getPmId().toString());
                                    task.setHandle(zsuserid.getRealname());

                                } else if (nextapprover.contains("zzuserid")) {

                                    TblStaff zzuserid = tblStaffMapper.getById(teamstaffEntity.getStaffid().toString());
                                    task.setHandle(zzuserid.getRealname());

                                } else if (nextapprover.contains("zbsjuserid")) {

                                    task.setHandle(certificate.getAssistedzbuser());

                                } else if (nextapprover.contains("xbfzr")) {

                                    task.setHandle(certificate.getAssistedbmfzr());

                                } else if (nextapprover.contains("xbfgld")) {

                                    task.setHandle(certificate.getAssistedfgld());

                                } else {
                                    task.setHandle(nextapprover);
                                }

                                task.setAnalid(analysisUser.getAnalid().toString());
                                //this.tblMytaskMapper.insertMyTaskSetting(task);
                                this.tblMyTaskMapper.insertMyTaskSetting(task);

                            }
                        }
                    }
                }
                resultMap.put("code", "1");
                resultMap.put("msg", "审批已提交！");
            } else {
                resultMap.put("code", "0");
                resultMap.put("msg", "流程提交失败！");
                log.error("发送审批失败");
            }

            //流程定义Id
//            String businessKey = processInstance.getBusinessKey();
//            String definitionId = processInstance.getProcessDefinitionId();
            //taskService.complete(task.getId());
            certificate.setStatus(TblNbsjCertificate.STATE2);
            tblNbsjCertificateMapper.updateEntity(certificate);
        } catch (Exception e) {
            e.printStackTrace();
            if (cir != null && cir.getCyid() != null) {
                this.tblCirculationMapper.deleteEntityById(cir.getCyid());
            }
            return ResponseFormat.retParam(0, 30002, null);
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 审计取证单查看办理
     *
     * @param token
     * @param certificateId
     * @param taskId
     * @param cyId
     * @return
     */
    @Override
    public JsonBean getCertificate(String token, Integer certificateId, String taskId, Integer cyId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblNbsjCertificate certificate = this.tblNbsjCertificateMapper.selectById(certificateId);//tblNbsjSheetMapper.getById(sheetid.toString());

        if (certificate == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }

        if (cyId != null) {
            TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());
            resultMap.put("cy", cy);
            List<TblMyTask> list = null;
            if (staff.getTrole() != null && staff.getTrole().getRname() != null) {
                list = HttpClient.findByTask(staff.getTrole().getRname(), staff.getStaffid().toString(), 1, 10000);
            } else {
                list = HttpClient.findByTask("", staff.getStaffid().toString(), 1, 10000);
            }

            if (list != null && list.size() > 0) {
                Map<String, Object> map = null;
                String resultData = null;
                String[] results = null;
                for (TblMyTask myTask : list) {
                    if (cy != null && myTask.getProcessInstanceId().equals(cy.getBusinesskey())) {
                        map = HttpClient.lczxProcessJson(myTask.getTaskId());
                        resultData = (String) map.get("data");
                        results = resultData.replace("\"", "").split(",");
                        if (results.length > 0 && !results[0].equals("")) {
                            List<String> stringB = Arrays.asList(results);
                            resultMap.put("btnList", stringB);
                            resultMap.put("number", stringB.size());
                        } else {
                            resultMap.put("btnList", null);
                            resultMap.put("number", 0);
                        }
                        resultMap.put("task", myTask);
                        break;
                    }
                }
            }
            resultMap.put("url", HttpClient.jkurl + cy.getBusinesskey());
        }

        resultMap.put("taskId", taskId);
        //resultMap.put("aoptionList", ao);

        resultMap.put("cyId", cyId);
        resultMap.put("certificateId", certificateId);
        TblNbsjProject project = tblNbsjProjectMapper.selectPJById(certificate.getProjectId());
        resultMap.put("project", project);

        return ResponseFormat.retParam(1, 200, resultMap);

    }

    /**
     * 审计取证单-办理流程
     *
     * @param token
     * @param cyId
     * @param taskId
     * @param transition
     * @param optDesc
     * @param certificateId
     * @param processDefinitionId
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean dealCertificate(String token, Integer cyId, String taskId, String transition, String optDesc, Integer certificateId, String processDefinitionId, String processInstanceId) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjCertificate certificate = tblNbsjCertificateMapper.selectById(certificateId);
        if (certificate == null) {
            return ResponseFormat.retParam(0, 50002, null);
        }
        JsonBean jsonBean = null;
        TblCirculation cy = this.tblCirculationMapper.findById(cyId.toString());

        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, null);
        }

        if (null != cy) {
            Map<String, Object> variables = new HashMap<>();
            variables.put(ProcessVariableEnum.model.toString(), cy);
            if (StrUtil.isNotBlank(transition)) {
                variables.put(ProcessVariableEnum.transition.toString(), transition);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                if (taskId != null && !taskId.equals("")) {

                    Map<String, Object> map = HttpClient.handleProcessJson(user.getStaffid().toString(), transition, taskId);
                    String object = (String) map.get("result");
                    if (object != null && object.equals("true")) {
                        TblMyTask task = new TblMyTask();
                        List<TblMyTask> myTasks = tblMyTaskMapper.findOndbyFrom(certificateId.toString());
                        TblProcessAnalysis findOnd = null;
                        if (CollectionUtils.isEmpty(myTasks)) {
                            findOnd = tblProcessAnalysisMapper.findOndBytakdid("");
                        } else {
                            findOnd = tblProcessAnalysisMapper.findOndAnalysis(myTasks.get(0).getAnalid());
                        }
                        Integer number = 1;
                        String usertaskid = findOnd.getUsertaskid();
                        Integer num = Integer.parseInt(usertaskid.substring(usertaskid.length() - 1, usertaskid.length())) + number;
                        String blande = usertaskid.substring(0, usertaskid.length() - 1) + num;

                        TblProcessAnalysis analysis = null;
                        if (transition != null && transition.equals("退回")) {
                            analysis = tblProcessAnalysisMapper.findOndBytakdidAnId(blande, findOnd.getProcessname());
                        }

                        //查询执行人
                        String nextapprover = HttpClient.nextapprover(cy.getBusinesskey());
                        task.setApprovaldate(new Date());
                        if (user.getTrole() != null && user.getTrole().getRname() != null) {
                            task.setApprovalrole(user.getTrole().getRname());
                        }
                        task.setApprover(user.getRealname());
                        task.setExamination(optDesc);
                        task.setFromid(certificateId.toString());
                        task.setProcessDefinitionId(processDefinitionId);
                        task.setUsrid(user.getStaffid().toString());
                        task.setCirid(cyId.toString());
                        task.setResult(transition);
                        task.setProcessName(myTasks.get(0).getProcessName());
                        task.setTaskId(taskId);
                        task.setProcessInstanceId(processInstanceId);
                        //task.setImgbasestr(imgBaseStr);
                        task.setAnalid("");
                        if (analysis != null) {
                            task.setAnalid(analysis.getAnalid().toString());
                        }
                        if (StringUtils.isBlank(nextapprover)) {//transitionName.equals("完成")||
                            task.setHandle("无");
                            cy.setCystate("已完成");
                            //审核完成
                            certificate.setStatus(TblNbsjCertificate.STATE4);
                            List<TblNbsjQuestionEntity> listq = questionmapper.findNbsjQuestionBySheetIdSp(certificate.getCertificateId());
                            for (TblNbsjQuestionEntity ques : listq) {
                                ques.setStatus(TblNbsjQuestionEntity.STATUSYES);
                                questionmapper.updateEntity(ques);
                            }
                        } else {

                            //退回or通过
                            TblStaff findById = tblStaffMapper.getById(cy.getCyStaffid());//表单提交人
                            if (nextapprover.contains("zsuserid")) {
                                TblStaff staff = tblStaffMapper.getById(tnp.getPmId().toString());
                                //审计主审
                                task.setHandle(staff.getRealname());
                            } else if (nextapprover.contains("zzuserid")) {
                                //审计组组长
                                //获取审计组组长id所在表单对象
                                Integer projectId = tnp.getProjectId();
                                List<TblNbsjTeamstaffEntity> byProjectid = tblNbsjTeamstaffMapper.getByProjectid(projectId);
                                TblNbsjTeamstaffEntity teamstaffEntity = byProjectid.get(0);
                                TblStaff staff = tblStaffMapper.getById(teamstaffEntity.getStaffid().toString());
                                task.setHandle(staff.getRealname());
                            } else if (nextapprover.contains("zbsjuserid")) {
                                //总部审计负责人
                                task.setHandle(certificate.getAssistedzbuser());
                            } else if (nextapprover.contains("xbfzr")) {
                                //协办部门负责人
                                task.setHandle(certificate.getAssistedbmfzr());
                            } else if (nextapprover.contains("xbfgld")) {
                                //协办部门分管领导
                                task.setHandle(certificate.getAssistedfgld());
                            } else if (nextapprover.contains("tcuserid")) {
                                //退回到创建人
                                task.setHandle(findById.getRealname());
                            } else {
                                //角色
                                task.setHandle(nextapprover);
                            }
                        }
                        if (certificate.getStatus() == 2) {
                            cy.setCystate("审批中");
                            certificate.setStatus(TblNbsjSheetEntity.STATE2);
                        }
                        if (transition != null && transition.equals("退回") || transition != null && transition.equals("驳回")) {
                            cy.setCystate("需调整");
                            certificate.setStatus(TblNbsjSheetEntity.STATE5);
                        }


                        tblCirculationService.upateTblCirculation(cy);

                        tblNbsjCertificateMapper.updateEntity(certificate);

                        tblMyTaskMapper.insertMyTaskSetting(task);
                    }


                    return ResponseFormat.retParam(1, "办理成功！", "");
                }

            } catch (Exception e) {

                e.printStackTrace();
                return ResponseFormat.retParam(0, 30002, null);

            }
        }

        return jsonBean;

    }

}
