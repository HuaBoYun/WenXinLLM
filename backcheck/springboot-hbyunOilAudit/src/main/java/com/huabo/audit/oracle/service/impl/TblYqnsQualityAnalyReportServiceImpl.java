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
import com.huabo.audit.oracle.entity.TblYqnsQualityAnalyReportEntity;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.service.TblYqnsAuditOverseeRecordsService;
import com.huabo.audit.oracle.service.TblYqnsQualityAnalyReportService;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsQualityAnalyReportServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-审计实施-质量分析报告 serviceImpl
 */
@Service
public class TblYqnsQualityAnalyReportServiceImpl extends ServiceImpl<TblYqnsQualityAnalyReportMapper, TblYqnsQualityAnalyReportEntity>
        implements TblYqnsQualityAnalyReportService {


    @Resource
    private TblYqnsQualityAnalyReportMapper tblYqnsQualityAnalyReportMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;
    @Resource
    private ReservePropertyService reservePropertyService;
    
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


    /**
     * 获取分页的质量分析报告列表
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getReportList(String token, Integer pageNumber, Integer pageSize, TblYqnsQualityAnalyReportEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        if(entity.getProjectId()==null) {
        	 ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
             if(tnp == null) {
                 return ResponseFormat.retParam(0,30003,null);
             }
             BigDecimal projectId = tnp.getId();
             if(null == projectId) {
                 return ResponseFormat.retParam(0,30003,null);
             }
             entity.setProjectId(projectId);
        }
       
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsQualityAnalyReportEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsQualityAnalyReportEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectReportList(entity));

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getList());

        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 获取单独一个审计工作记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getReportById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsQualityAnalyReportEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString());
        bean.setAttachments(attachments);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);

        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 质量分析报告-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsQualityAnalyReportEntity entity) throws Exception {
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
        entity.setProjectId(projectId);
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getRealname());
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }
		entity.setCreateUser(loginStaff.getRealname());
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        // 附件关联操作
        this.baseMapper.deleteAttByPk(entity.getId().toString());
        List<String> attIds = entity.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(entity.getId().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, entity);
    }

    /**
     * 删除质量分析报告(直接删除)
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (id != null) {
            boolean ret = this.removeById(id);
            if (!ret) {
                return ResponseFormat.retParam(0, -1, Boolean.FALSE);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除附件
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
        this.baseMapper.deleteAttById(attId);
        BigDecimal attIdDecimal  = new BigDecimal(attId);
        // 删除附件信息
        tblAttachmentMapper.deleteEntity(attIdDecimal);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询质量分析报告
     *
     * @param entity
     * @return
     */
    public List<TblYqnsQualityAnalyReportEntity> selectReportList(TblYqnsQualityAnalyReportEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsQualityAnalyReportEntity> query = new LambdaQueryWrapper<TblYqnsQualityAnalyReportEntity>()
                .like(StringUtil.isNotEmpty(entity.getTitle()), TblYqnsQualityAnalyReportEntity::getTitle, entity.getTitle())
                .like(StringUtil.isNotEmpty(entity.getProjectId().toString()), TblYqnsQualityAnalyReportEntity::getProjectId, entity.getProjectId())
                .like(StringUtil.isNotEmpty(entity.getDocumentNumber()), TblYqnsQualityAnalyReportEntity::getDocumentNumber, entity.getDocumentNumber());
        return tblYqnsQualityAnalyReportMapper.selectList(query);
    }
}
