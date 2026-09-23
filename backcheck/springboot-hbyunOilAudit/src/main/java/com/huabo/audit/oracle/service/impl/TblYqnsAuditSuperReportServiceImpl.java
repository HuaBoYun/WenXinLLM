package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.entity.TblYqnsAuditSuperReport;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.service.TblYqnsAuditOverseeRecordsService;
import com.huabo.audit.oracle.service.TblYqnsAuditSuperReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblYqnsAuditSuperReportServiceImpl extends ServiceImpl<TblYqnsAuditSuperReportMapper, TblYqnsAuditSuperReport>
        implements TblYqnsAuditSuperReportService {

    @Resource
    private TblYqnsAuditSuperReportMapper tblYqnsAuditSuperReportMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private UserProvider userProvider;

    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }

    @Override
    public JsonBean selectReportList(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditSuperReport entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        entity.setProjectId(projectId.toString());
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditWorkRecordsEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditWorkRecordsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectReportList(entity));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    @Override
    public JsonBean selectReportById(Long id) throws Exception {
        TblYqnsAuditSuperReport report = tblYqnsAuditSuperReportMapper.selectReportById(id);
        List<TblAttachment> attachments = this.tblYqnsAuditSuperReportMapper.selectAttachmentListByPk(report.getId().toString());
        report.setAttachments(attachments);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", report);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveReport(String token, TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        if(tblYqnsAuditSuperReport.getId()==null) {
        	tblYqnsAuditSuperReport.setId(RandomUtil.uuLongId());
        }
        tblYqnsAuditSuperReport.setProjectId(projectId.toString());
        tblYqnsAuditSuperReportMapper.saveReport(tblYqnsAuditSuperReport);
        // 附件关联操作
        tblYqnsAuditSuperReportMapper.deleteAttByPk(tblYqnsAuditSuperReport.getId().toString());
        List<String> attIds = tblYqnsAuditSuperReport.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                tblYqnsAuditSuperReportMapper.saveAtt(tblYqnsAuditSuperReport.getId().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean updateReport(String token, TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        tblYqnsAuditSuperReport.setProjectId(projectId.toString());
        tblYqnsAuditSuperReportMapper.updateReport(tblYqnsAuditSuperReport);
        // 附件关联操作
        tblYqnsAuditSuperReportMapper.deleteAttByPk(tblYqnsAuditSuperReport.getId().toString());
        List<String> attIds = tblYqnsAuditSuperReport.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                tblYqnsAuditSuperReportMapper.saveAtt(tblYqnsAuditSuperReport.getId().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteReoprt(String token, String ids) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsAuditSuperReportMapper.deleteReoprt(ids);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除附件
     *
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteFileAttach(String token, String attId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 删除关联附件信息
        tblYqnsAuditSuperReportMapper.deleteAttById(attId);
        BigDecimal attIdDecimal = new BigDecimal(attId);
        // 删除附件信息
        tblAttachmentMapper.deleteEntity(attIdDecimal);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询审计督导记录
     *
     * @param entity
     * @return
     */
    public List<TblYqnsAuditSuperReport> selectReportList(TblYqnsAuditSuperReport entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditSuperReport> query = new LambdaQueryWrapper<TblYqnsAuditSuperReport>()
                .eq(StringUtil.isNotEmpty(entity.getProjectId()), TblYqnsAuditSuperReport::getProjectId, entity.getProjectId())
                .like(StringUtil.isNotEmpty(entity.getReportName()), TblYqnsAuditSuperReport::getReportName, entity.getReportName())
                .apply(StringUtil.isNotEmpty(entity.getStartTime()),
                        "REPORTTIME  >= TO_DATE('" + entity.getStartTime() + "','YYYY-MM-DD')")
                .apply(StringUtil.isNotEmpty(entity.getEndTime()),
                        "REPORTTIME <= TO_DATE('" + entity.getEndTime() + "','YYYY-MM-DD')");
        return tblYqnsAuditSuperReportMapper.selectList(query);
    }
}
