package com.huabo.audit.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.entity.TblProjectDataPreEntity;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjProjectDataProService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblNbsjProjectDataProServiceImpl implements TblNbsjProjectDataProService {

    @Autowired
    private TblNbsjProjectDataMapper tblNbsjProjectDataMapper;

    @Autowired
    private ActivityPluginsService activityPluginsService;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private TblNbsjProjectService tblNbsjProjectService;

    @Resource
    private TblNbsjProjectMapper tblNbsjProjectMapper;

    @Resource
    private TblNbsjMbMapper tblNbsjMbMapper;

    @Resource
    private TblNbsjSjjykMapper tblNbsjSjjykMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public void saveOrUpdate(TblProjectDataPreEntity dataPre) {
        // TODO Auto-generated method stub

    }

    @Override
    public void proDel(TblProjectDataPreEntity dataPre) {
        // TODO Auto-generated method stub

    }

    @Override
    public TblProjectDataPreEntity proDataById(BigDecimal id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addList(List<TblProjectDataPreEntity> tblProjectDataPres, String username, BigDecimal projectid,
                        String projectname, String orgid) {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer proDataById(String projectDataPreId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer selectProjectNumber(String projectDataPreId, String projectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateTblProjectDataPre(TblProjectDataPreEntity dataPre) {
        // TODO Auto-generated method stub

    }


    //==
    @Override
    public JsonBean dataproPageList(String token, Integer pageNumber, Integer pageSize, DataProVo dataProVo, Integer orgid, Integer projectId) throws Exception {
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
//    	if(null == orgid) {
//    		orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
//    	}
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblNbsjProjectDataEntity> pageInfo = new PageInfo<TblNbsjProjectDataEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        // 判断是否为项目组成员 是项目组成员可以获取该项目下的项目资料列表
        boolean ifTeam = false;
        List<TblNbsjProject> pro = tblNbsjProjectMapper.selectAuditItems(loginStaff.getStaffid());
        if (pro.size() > 0) {

            if (projectId == null) {
                //==查询当前实施的项目！
                TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
                if (tnp == null) {
                    return ResponseFormat.retParam(0, 30003, resultMap);
                }
                projectId = tnp.getProjectId();
            }

            if (null == projectId) {
                return ResponseFormat.retParam(0, 30003, resultMap);
            }
            Integer finalProjectId = projectId;
            if (pro != null) {
                ifTeam = true;
                com.github.pagehelper.PageInfo<TblNbsjProjectDataEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                        .doSelectPageInfo(() -> {
                            try {
                                this.tblNbsjProjectDataMapper.selectListByPageInfo(pageInfo, dataProVo, orgid, finalProjectId, null);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                pageInfo.setTlist(pageInfo2.getList());
                pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
                pageInfo.getTotalPage();
            }
        } else { //如果不是项目成员 查看是否为下发人员
            com.github.pagehelper.PageInfo<TblNbsjProjectDataEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() -> {
                        try {
                            this.tblNbsjProjectDataMapper.selectListByPageInfo(pageInfo, dataProVo, orgid, null, loginStaff.getStaffid());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
            pageInfo.setTlist(pageInfo2.getList());
            pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
            pageInfo.getTotalPage();
        }
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        resultMap.put("ifTeam", ifTeam);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean dataproAdd(TblNbsjProjectDataEntity pd, String token, String attids, String mbids, String jykids) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Integer count = this.tblNbsjProjectDataMapper.selectPlanCodeByOrgid(pd);
        if (count > 0) {
            return ResponseFormat.retParam(0, 202, null);
        }

        //==查询当前实施的项目！
        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, null);
        }
        Integer projectId = tnp.getProjectId();
        if (null == projectId) {
            return ResponseFormat.retParam(0, 30003, null);
        }
        pd.setProjectid(projectId);

        BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();

//		notice.setTblCreater(loginStaff);
        pd.setDataDate(new Date());
        pd.setOrgid(orgid + "");
        pd.setUsername(loginStaff.getRealname());

        //根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；

        if (pd.getId() != null) {
            //修改；
            //this.tblNbsjProjectDataMapper.updateEntity(pd);
            this.tblNbsjProjectDataMapper.updateByPrimaryKeySelective(pd);
            //==附件，先删除 再重新添加
            this.tblAttachmentMapper.deleteAttmentRelationDataPj(pd.getId());
            if (attids != null && !"".equals(attids)) {
                String[] ids = attids.split(",");
                for (String id : ids) {
                    this.tblAttachmentMapper.insertAttmentRelationDataPj(id, pd.getId());
                }
            }
            //模板库
            if (mbids != null && !"".equals(mbids)) {
                String[] ids = mbids.split(",");
                for (String mbid : ids) {
                    tblNbsjMbMapper.insertMbdatapre(mbid, pd.getId());
                }
            }
            //经验库
            if (jykids != null && !"".equals(jykids)) {
                String[] ids = jykids.split(",");
                for (String jykid : ids) {
                    tblNbsjSjjykMapper.insertjykdatapre(jykid, pd.getId());
                }
            }
        } else {
            //新增；
            //this.tblNbsjProjectDataMapper.insertEntity(pd);
            this.tblNbsjProjectDataMapper.insertSelective(pd);
            //==附件
            if (attids != null && !"".equals(attids)) {
                String[] ids = attids.split(",");
                for (String id : ids) {
                    this.tblAttachmentMapper.insertAttmentRelationDataPj(id, pd.getId());
                }
            }
            //模板库
            if (mbids != null && !"".equals(mbids)) {
                String[] ids = mbids.split(",");
                for (String mbid : ids) {
                    tblNbsjMbMapper.insertMbdatapre(mbid, pd.getId());
                }
            }
            //经验库
            if (jykids != null && !"".equals(jykids)) {
                String[] ids = jykids.split(",");
                for (String jykid : ids) {
                    tblNbsjSjjykMapper.insertjykdatapre(jykid, pd.getId());
                }
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("Doubtfulpoint", pd);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean dataproAddFile(Integer dataId, BigDecimal attid) throws Exception {
        TblNbsjProjectDataEntity pd = this.tblNbsjProjectDataMapper.selectById(dataId);
        if (pd.getId() != null && attid != null) {
            this.tblAttachmentMapper.insertAttmentRelationDataPj(attid.toString(), pd.getId());
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean dataproDelete(Integer dataId, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjProjectDataEntity plan = this.tblNbsjProjectDataMapper.selectById(dataId);

        if (plan == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }

//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjProjectDataMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
        tblNbsjMbMapper.deletetMbdatapre(dataId);
        tblNbsjSjjykMapper.deletetjykdatapre(dataId);
        this.tblNbsjProjectDataMapper.deleteById(dataId);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean findDataProDetail(String token, Integer dataId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblNbsjProjectDataEntity plan = this.tblNbsjProjectDataMapper.selectById(dataId);
        resultMap.put("Doubtfulpoint", plan);
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
        this.tblNbsjProjectDataMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());

        return R.success();
    }

    @Override
    public JsonBean issueProject(String token, String staffId, String dataId, String projectid) throws Exception {
        // TODO Auto-generated method stub
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (StringUtils.isBlank(projectid) && tnp != null) {
            projectid = tnp.getProjectId().toString();
        }
        String[] dataIds = dataId.split(",");
        String[] roleids = staffId.split(",");
        try {
            TblNbsjProjectDataEntity dataPre = null;
            for (String pid : dataIds) {
                dataPre = this.tblNbsjProjectDataMapper.selectById(Integer.valueOf(pid));
                for (String rid : roleids) {
                    if (tblNbsjProjectDataMapper.checkIssue(Integer.valueOf(pid), Integer.valueOf(rid)) == 0) {
                        if (pid != null && pid.length() > 0) {
                            dataPre.setFristuserid(rid);
                            tblNbsjProjectDataMapper.updateEntity(dataPre);
                        }
                        tblNbsjProjectDataMapper.saveIssue(projectid, rid, pid);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean savexmfj(String token, Integer dataId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjProjectDataEntity pd = this.tblNbsjProjectDataMapper.selectById(dataId);
        if (pd.getId() != null) {
            List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByProjectid(pd.getProjectid());
            if (attList == null || attList.size() <= 0) {
                return ResponseFormat.retParam(0, "项目无附件", null);
            }
            for (TblAttachment tblAttachment : attList) {
                TblAttachment tblAttachmentEntity = new TblAttachment();
                tblAttachmentEntity.setAttpath(tblAttachment.getAttpath());
                tblAttachmentEntity.setAttsize(tblAttachment.getAttsize());
                tblAttachmentEntity.setUploadtime(tblAttachment.getUploadtime());
                tblAttachmentEntity.setUploader(tblAttachment.getUploader());
                tblAttachmentEntity.setAttname(tblAttachment.getFilename());
                tblAttachmentMapper.insertEntity(tblAttachmentEntity);
                this.tblAttachmentMapper.insertAttmentRelationDataPj(tblAttachmentEntity.getAttid().toString(), pd.getId());
            }

        }
        return ResponseFormat.retParam(1, 200, null);
    }

}
