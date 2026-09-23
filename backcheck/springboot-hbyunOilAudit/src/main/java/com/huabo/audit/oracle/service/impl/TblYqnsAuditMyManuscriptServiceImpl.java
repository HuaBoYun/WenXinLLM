package com.huabo.audit.oracle.service.impl;

import cn.hutool.core.io.resource.ClassPathResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.oracle.service.*;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.impl.ReservePropertyService;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.result.FlowTaskInfo;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿 serviceImpl
 */
@Service
public class TblYqnsAuditMyManuscriptServiceImpl extends ServiceImpl<TblYqnsAuditMyManuscriptMapper, TblYqnsAuditMyManuscriptEntity>
        implements TblYqnsAuditMyManuscriptService {


    @Resource
    private TblYqnsAuditMyManuscriptMapper tblYqnsAuditMyManuscriptMapper;
    @Resource
    private TblYqnsAuditMyManuVerifyService tblYqnsAuditMyManuVerifyService;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;

    @Resource
    private TblYqnsAuditWorkRecordsMapper tblYqnsAuditWorkRecordsMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private TblYqnsAuditMyManuVerifyMapper tblYqnsAuditMyManuVerifyMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
    
    

    /**
     * 获取分页的我的底稿-分页
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMyManuscriptPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity entity, BigDecimal staffId, Integer xmnd) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (staffId == null && xmnd == null && entity.getProjectId() == null) {
            //==查询当前实施的项目！
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            BigDecimal projectId = tnp.getId();
            if (null == projectId) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            entity.setProjectId(projectId);


        }
        if (entity.getIsall() != null && entity.getIsall().equals("1")) {
            entity.setProjectId(null);
        } else if (entity.getIsall() != null && entity.getIsall().equals("2")) {
            entity.setCreateUser(staffId.toString());
            entity.setProjectId(null);
        } else {
            entity.setCreateUser(loginStaff.getStaffid().toString());
//        	  entity.setStatus(6);
        }

        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectMyManuscriptList(entity, staffId, xmnd));

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
     * 获取分页的底稿列表-分页
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean dggl_list(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (entity.getProjectId() == null) {
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            BigDecimal projectId = tnp.getId();
            if (null == projectId) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            entity.setProjectId(projectId);
        }

//        entity.setCreateUser(loginStaff.getStaffid().toString());
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selecManuscriptList(entity, null));
        // 构建返回值条件
        if(pageInfo.getList()!=null && pageInfo.getList().size()>0) {
        	for (TblYqnsAuditMyManuscriptEntity dg : pageInfo.getList()) {
        		TblStaff staff = tblStaffMapper.selectById(new BigDecimal(dg.getCreateUser()));
        		dg.setCreateUsername(staff.getRealname());
				
			}
        }
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }


    /**
     * 获取分页的审计发现列表-分页
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean question_store_list(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        if (entity.getProjectId() == null) {
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            BigDecimal projectId = tnp.getId();
            if (null == projectId) {
                return ResponseFormat.retParam(0, 30003, null);
            }
            entity.setProjectId(projectId);
        }

        entity.setCreateUser(loginStaff.getStaffid().toString());
        entity.setStatus(6); //审批状态为已完成；
        entity.setProbleMdraft("1"); //审计发现为是；
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.question_store_list(entity));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }

    /**
     * 我的底稿--获取我的底稿列表 通过TypeId
     *
     * @param token
     * @param typeId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMyManuscriptListByTypeId(String token, String typeId, String templateId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();

        ImplementPlanEntity pj = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        TblYqnsAuditMyManuscriptEntity entity = new TblYqnsAuditMyManuscriptEntity();
        entity.setTypeId(typeId);
        entity.setProjectId(pj.getId());
        entity.setTemplateId(templateId);
        List<TblYqnsAuditMyManuscriptEntity> MyManuscriptList = this.selectMyManuscriptList(entity, null, null);
        result.put("MyManuscriptList", MyManuscriptList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 获取单独一个我的底稿记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMyManuscriptById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditMyManuscriptEntity bean = this.getMyManuscriptById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = tblYqnsAuditMyManuVerifyService.getListByMyManuscriptId(bean.getId().toString());
        bean.setAuditMyManuVerifyEntityList(tblYqnsAuditMyManuVerifyEntityList);
        List<TblAttachment> attList = this.tblYqnsAuditMyManuscriptMapper.findAttachmentListByMuManuAtt(id.toString());
        bean.setAttList(attList);

        List<TblYqnsAuditWorkRecordsEntity> workList = this.tblYqnsAuditWorkRecordsMapper.selectListByRealMyDraft(bean.getId());
        bean.setWorkList(workList);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);

        return ResponseFormat.retParam(1, 200, bean);
    }


    /**
     * 获取单独一个我的底稿记录(获取模板)
     *
     * @param id
     * @throws Exception
     */
    @Override
    public TblYqnsAuditMyManuscriptEntity getMyManuscriptById(Long id) {
        LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity>()
                .eq(TblYqnsAuditMyManuscriptEntity::getId, id);
        return tblYqnsAuditMyManuscriptMapper.selectOne(query);
    }


    /**
     * 我的底稿-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsAuditMyManuscriptEntity entity,String type) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
//            return ResponseFormat.retParam(0,30003,null);
        } else {
        	if(entity.getId()==null) {
        		entity.setProjectId(tnp.getId());
        	}
        }

        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getRealname());
        } else {
        	entity.setId(RandomUtil.uuLongId());
//            // 生成底稿编号
//            entity.setDraftNumber(this.generateDraftNumber());
            entity.setCreateUser(loginStaff.getStaffid().toString());
        }
        boolean ret = this.saveOrUpdate(entity);

        //===============
        String attIds = entity.getAttIds();
        if (attIds != null && attIds.length() > 0) {
            tblYqnsAuditMyManuscriptMapper.delFileRelation(entity.getId().toString());
            String[] stins = attIds.split(",");
            for (String attid : stins) {
                tblYqnsAuditMyManuscriptMapper.insetFileRelation(attid, entity.getId().toString());
            }
        }
        //========================


        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        if (null != entity.getAuditMyManuVerifyEntityList() && entity.getAuditMyManuVerifyEntityList().size()>0) {
            entity.getAuditMyManuVerifyEntityList().stream().forEach(vo -> {
            	
                vo.setMyManuscriptId(entity.getId().toString());
//                vo.setProjectId(projectId);
                if(entity.getType()!=null && entity.getType().equals("1")) {
                	vo.setId(RandomUtil.uuLongId());
                } 
                if(vo.getId()==null) { 
                	vo.setId(RandomUtil.uuLongId());
                }
            });
            if(entity.getType()!=null && entity.getType().equals("1")) {
            	tblYqnsAuditMyManuVerifyMapper.updateByxmner(entity.getId().toString());
            }
            tblYqnsAuditMyManuVerifyService.saveOrUpdateList(token, entity.getAuditMyManuVerifyEntityList());
        }

        if (StringUtils.isNotBlank(entity.getReportIds())) {
            String[] reportIdarr = entity.getReportIds().split(",");
            for (String rid : reportIdarr) {
                this.tblYqnsAuditMyManuscriptMapper.insertRealRpoertDraft(rid, entity.getId());
            }
        }

        return ResponseFormat.retParam(1, 200, entity);
    }

    /**
     * 删除我的底稿(直接删除)
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
            this.tblYqnsAuditMyManuscriptMapper.deleteRelaWorkRpeort(id);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询我的底稿
     *
     * @param entity
     * @param xmnd
     * @param staffId
     * @return
     */
    public List<TblYqnsAuditMyManuscriptEntity> selectMyManuscriptList(TblYqnsAuditMyManuscriptEntity entity, BigDecimal staffId, Integer xmnd) {
        List<TblYqnsAuditMyManuscriptEntity> list = null;
        try {
            // 进行数据获取和查询
            LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity>();

            if (StringUtil.isNotEmpty(entity.getTypeId())) {
                query.eq(TblYqnsAuditMyManuscriptEntity::getTypeId, entity.getTypeId());
            }
            if (StringUtil.isNotEmpty(entity.getProjectName())) {
                query.like(TblYqnsAuditMyManuscriptEntity::getProjectName, entity.getProjectName());
            }


            if (StringUtil.isNotEmpty(entity.getTemplateId())) {
                query.eq(TblYqnsAuditMyManuscriptEntity::getTemplateId, entity.getTemplateId());
            }
            if (entity.getProjectId() != null) {
                query.eq(TblYqnsAuditMyManuscriptEntity::getProjectId, entity.getProjectId());
            }
            if (StringUtil.isNotEmpty(entity.getProbleMdraft())) {
                query.eq(TblYqnsAuditMyManuscriptEntity::getProbleMdraft, entity.getProbleMdraft());
            }
            if (StringUtil.isNotEmpty(entity.getDraftNumber())) {
                query.like(TblYqnsAuditMyManuscriptEntity::getDraftNumber, entity.getDraftNumber());
            }
            if (entity.getStatus() != null) {
                query.eq(TblYqnsAuditMyManuscriptEntity::getStatus, entity.getStatus());
            }

            if (xmnd != null) {
                query.ge(TblYqnsAuditMyManuscriptEntity::getCreateTime, DateUtil.getYearStartDate(xmnd)).le(TblYqnsAuditMyManuscriptEntity::getCreateTime, DateUtil.getYearEndDate(xmnd));
            }
            if (staffId != null) {
                query.and(wq -> wq.eq(TblYqnsAuditMyManuscriptEntity::getCreateUser, staffId).or().eq(TblYqnsAuditMyManuscriptEntity::getThedeptstaffid, staffId));
            } else {
                if (StringUtil.isNotEmpty(entity.getCreateUser())) {
                    query.eq(TblYqnsAuditMyManuscriptEntity::getCreateUser, entity.getCreateUser());
                }
            }

            list = tblYqnsAuditMyManuscriptMapper.selectList(query);
            list.stream().forEach(bean -> {
                List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = tblYqnsAuditMyManuVerifyService.getListByMyManuscriptId(bean.getId().toString());
                bean.setAuditMyManuVerifyEntityList(tblYqnsAuditMyManuVerifyEntityList);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询审计发现
     *
     * @param entity
     * @return
     */
    public List<TblYqnsAuditMyManuscriptEntity> question_store_list(TblYqnsAuditMyManuscriptEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity>()
                .like(StringUtil.isNotEmpty(entity.getProjectName()), TblYqnsAuditMyManuscriptEntity::getProjectName, entity.getProjectName())  //项目名称
                .eq(StringUtil.isNotEmpty(entity.getTypeId()), TblYqnsAuditMyManuscriptEntity::getTypeId, entity.getTypeId()) //基础设置类型
                .eq(StringUtil.isNotEmpty(entity.getProbleMdraft()), TblYqnsAuditMyManuscriptEntity::getProbleMdraft, entity.getProbleMdraft()) //审计发现
                .eq(StringUtil.isNotEmpty(entity.getCreateUser()), TblYqnsAuditMyManuscriptEntity::getCreateUser, entity.getCreateUser()) //创建人
                .eq(StringUtil.isNotEmpty(entity.getStatus().toString()), TblYqnsAuditMyManuscriptEntity::getStatus, entity.getStatus()) //审批状态
                .eq(StringUtil.isNotEmpty(entity.getProjectId().toString()), TblYqnsAuditMyManuscriptEntity::getProjectId, entity.getProjectId());  //关联的项目id
        List<TblYqnsAuditMyManuscriptEntity> list = tblYqnsAuditMyManuscriptMapper.selectList(query);
        list.stream().forEach(bean -> {
            List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = tblYqnsAuditMyManuVerifyService.getListByMyManuscriptId(bean.getId().toString());
            bean.setAuditMyManuVerifyEntityList(tblYqnsAuditMyManuVerifyEntityList);
        });
        return list;
    }

    /**
     * 查询底稿管理
     *
     * @param entity
     * @param idList 我的底稿id集合
     * @return
     */
    public List<TblYqnsAuditMyManuscriptEntity> selecManuscriptList(TblYqnsAuditMyManuscriptEntity entity, List<String> idList) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity>()
                .like(StringUtil.isNotEmpty(entity.getProjectName()), TblYqnsAuditMyManuscriptEntity::getProjectName, entity.getProjectName())  //项目名称
                .like(StringUtil.isNotEmpty(entity.getDraftNumber()), TblYqnsAuditMyManuscriptEntity::getDraftNumber, entity.getDraftNumber())  //编号
                .eq(StringUtil.isNotEmpty(entity.getTypeId()), TblYqnsAuditMyManuscriptEntity::getTypeId, entity.getTypeId()) //基础设置类型
                .eq(StringUtil.isNotEmpty(entity.getCreateUser()), TblYqnsAuditMyManuscriptEntity::getCreateUser, entity.getCreateUser()) //创建人
                .eq(entity.getStatus()!=null, TblYqnsAuditMyManuscriptEntity::getStatus, entity.getStatus()) //状态
                .in(CollectionUtils.isNotEmpty(idList), TblYqnsAuditMyManuscriptEntity::getId, idList)
                .eq(entity.getProjectId() != null, TblYqnsAuditMyManuscriptEntity::getProjectId, entity.getProjectId());  //关联的项目id
        List<TblYqnsAuditMyManuscriptEntity> list = tblYqnsAuditMyManuscriptMapper.selectList(query);

        if (list == null || list.size() == 0) {
            return list;
        }

        List<List<String>> oneList = new ArrayList<List<String>>(0);
        List<String> twoList = new ArrayList<String>(0);

        int i = 0;
        for (TblYqnsAuditMyManuscriptEntity bean : list) {
            List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = tblYqnsAuditMyManuVerifyService.getListByMyManuscriptId(bean.getId().toString());
            bean.setAuditMyManuVerifyEntityList(tblYqnsAuditMyManuVerifyEntityList);
            i = i + 1;
            twoList.add(bean.getId().toString());
            if (i > 998) {
                oneList.add(twoList);
                twoList = new ArrayList<String>(0);
            }
        }
        oneList.add(twoList);
        String dgIds = "";

        //专业科室审核信息
        List<FlowTaskInfo> zyksShList = new ArrayList<FlowTaskInfo>(0);

        //发起人审批修改信息
        List<FlowTaskInfo> fqrShList = new ArrayList<FlowTaskInfo>(0);

        //专业科室复核信息
        List<FlowTaskInfo> zyksFhList = new ArrayList<FlowTaskInfo>(0);

        for (List<String> one : oneList) {
            dgIds = String.join(",", one);
            //当前信息专业科室审核意见
            List<FlowTaskInfo> dqShList = this.tblYqnsAuditMyManuscriptMapper.selectApprovalInfoList(TblYqnsAuditMyManuscriptEntity.tableId, dgIds, "专业科室审核");
            zyksShList.addAll(dqShList);
            //当前信息发起人复核意见
            List<FlowTaskInfo> fqrSh = this.tblYqnsAuditMyManuscriptMapper.selectApprovalInfoList(TblYqnsAuditMyManuscriptEntity.tableId, dgIds, "发起人复核");
            fqrShList.addAll(fqrSh);
            //当前信息专业科室复核意见
            List<FlowTaskInfo> zyksFh = this.tblYqnsAuditMyManuscriptMapper.selectApprovalInfoList(TblYqnsAuditMyManuscriptEntity.tableId, dgIds, "专业科室复核");
            zyksFhList.addAll(zyksFh);
        }

        for (TblYqnsAuditMyManuscriptEntity bean : list) {
            for (FlowTaskInfo shInfo : zyksShList) {
                if (bean.getId().toString().equals(shInfo.getFromId())) {
                    bean.setZyksshDate(shInfo.getCreateTime());
                    bean.setZyksshPeople(shInfo.getCurrentStaffName());
                }
            }
            for (FlowTaskInfo fqr : fqrShList) {
                if (bean.getId().toString().equals(fqr.getFromId())) {
                    bean.setFqrShDate(fqr.getCreateTime());
                }
            }
            for (FlowTaskInfo fhInfo : zyksFhList) {
                if (bean.getId().toString().equals(fhInfo.getFromId())) {
                    bean.setZyksfhDate(fhInfo.getCreateTime());
                    bean.setZyksfhCommont(fhInfo.getCommont());
                }
            }
        }


        return list;
    }


    /**
     * 生成底稿标识
     *
     * @return
     */
    @Override
    public String generateDraftNumber() {
        //获取当前月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String montrStr = "";
        if (month < 10) {
            montrStr = "0" + month;
        } else {
            montrStr = month + "";
        }

        //组合自动编号:年度-月-模块-自增数
        String draftNumber = year + "-" + montrStr + "-WDDG-";

        //自增数查询
        String no = "0";
        try {
            no = this.tblYqnsAuditMyManuscriptMapper.selectMaxDraftNumber();
        } catch (Exception e) {
            no = "0";
        }
        if (no != null) {
//			 no = no.replace(sheetCode, "");
        } else {
            no = "0";
        }
        draftNumber += (Integer.parseInt(no) + 1);
        return draftNumber;
    }

    /**
     * 获取单独一个底稿管理记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getManuscriptById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditMyManuscriptEntity bean = this.getMyManuscriptById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = tblYqnsAuditMyManuVerifyService.getListByMyManuscriptId(bean.getId().toString());
        bean.setAuditMyManuVerifyEntityList(tblYqnsAuditMyManuVerifyEntityList);
        return ResponseFormat.retParam(1, 200, bean);
    }


    /**
     * 底稿管理-导出
     */
    @Override
    public JsonBean excelUtils(HttpServletResponse response, String token, TblYqnsAuditMyManuscriptEntity vo,List<String> idList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
            return ResponseFormat.retParam(0, 30003, null);
        }

        String[] titles = {"序号", "底稿编号", "审计查证事实", "审计结论及依据", "审计处理意见及建议", "编制人",
                "审计证据是否准确、真实、合法", "审计查证事实描述是否详尽、充分", "审计结论是否客观、公正", "审计处理意见及建议是否正确、具有可操作性和建设性", "基础工作",
                "审计查证事实", "审计结论及依据", "审计处理意见及建议",
                "审计组与被审计单位交换意见后修改审计查证事实描述", "备注"};
        PageInfo<TblYqnsAuditMyManuVerifyEntity> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);
        TblYqnsAuditMyManuscriptEntity entity = new TblYqnsAuditMyManuscriptEntity();
        entity.setProjectId(tnp.getId());
        entity.setProjectName(vo.getProjectName());
        entity.setDraftNumber(vo.getDraftNumber());
        entity.setCreateUser(vo.getCreateUser());
        entity.setStatus(vo.getStatus());
        List<TblYqnsAuditMyManuscriptEntity> query = this.selecManuscriptList(entity, idList);
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsAuditMyManuscriptEntity bean : query) {

            List<TblYqnsAuditMyManuVerifyEntity> listVerify = bean.getAuditMyManuVerifyEntityList();

            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getDraftNumber(); //底稿编号
            if (null != listVerify && listVerify.size() > 0) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(0);
                obj[2] = verify1.getVerificationDescription();
                obj[3] = verify1.getAuditConclusion();
                obj[4] = verify1.getHandlingOpinions();
            } else {
                obj[2] = "";
                obj[3] = "";
                obj[4] = "";
            }
            obj[5] = tblYqnsAuditMyManuscriptMapper.selectRealnameById(bean.getCreateUser()); //创建人
            obj[6] = bean.getEvidenceAccurate(); //审计证据是否准确、真实、合法
            obj[7] = bean.getDgverificationDescription(); //审计查证事实描述是否详尽、充分
            obj[8] = bean.getDgauditConclusion(); //审计结论是否客观、公正
            obj[9] = bean.getDghandlingOpinions(); //审计处理意见及建议是否正确、具有可操作性和建设性
            obj[10] = bean.getBasicWork(); //基础工作
            if (null != listVerify && listVerify.size() > 1) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(1);
                obj[11] = verify1.getVerificationDescription();
                obj[12] = verify1.getAuditConclusion();
                obj[13] = verify1.getHandlingOpinions();
            } else {
                obj[11] = "";
                obj[12] = "";
                obj[13] = "";
            }
            if (null != listVerify && listVerify.size() > 2) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(2);
                obj[14] = verify1.getVerificationDescription();
                obj[15] = verify1.getRemarks();
            } else {
                obj[14] = "";
                obj[15] = "";
            }

            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("底稿".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);


    }


    @Override
    public JsonBean delMyDraftWorkReportRela(String token, Long workReprotId, Long myDraftId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsAuditMyManuscriptMapper.deleteMyDraftWorkReportRela(workReprotId, myDraftId);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    public JsonBean getwtqdManuscriptPage(String token, Integer pageNumber, Integer pageSize,TblYqnsAuditMyManuscriptEntity entity) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity> query = new LambdaQueryWrapper<TblYqnsAuditMyManuscriptEntity>();

        if (StringUtil.isNotEmpty(entity.getTypeId())) {
            query.eq(TblYqnsAuditMyManuscriptEntity::getTypeId, entity.getTypeId());
        }
        if (StringUtil.isNotEmpty(entity.getProjectName())) {
            query.like(TblYqnsAuditMyManuscriptEntity::getProjectName, entity.getProjectName());
        }


        if (StringUtil.isNotEmpty(entity.getTemplateId())) {
            query.eq(TblYqnsAuditMyManuscriptEntity::getTemplateId, entity.getTemplateId());
        }
        if (entity.getProjectId() != null) {
            query.eq(TblYqnsAuditMyManuscriptEntity::getProjectId, entity.getProjectId());
        }
        if (StringUtil.isNotEmpty(entity.getProbleMdraft())) {
            query.eq(TblYqnsAuditMyManuscriptEntity::getProbleMdraft, entity.getProbleMdraft());
        }
        if (StringUtil.isNotEmpty(entity.getDraftNumber())) {
            query.like(TblYqnsAuditMyManuscriptEntity::getDraftNumber, entity.getDraftNumber());
        }
        if (entity.getStatus() != null) {
            query.eq(TblYqnsAuditMyManuscriptEntity::getStatus, entity.getStatus());
        }

        //倒序 
        query.orderByDesc(TblYqnsAuditMyManuscriptEntity::getId);
        
        
       
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo =  PageMethod.startPage(pageNumber,pageSize)
				.doSelectPageInfo(() -> tblYqnsAuditMyManuscriptMapper.selectList(query));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		//构建预留字段返回
		 com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
		 // 构建返回值条件
	        info.setCurrentPage(pageInfo.getPageNum());
	        info.setPageSize(pageInfo.getPageSize());
	        info.setTotalRecord((int) pageInfo.getTotal());
	        info.setTlist(pageInfo.getList());
	        resultMap.put("pageInfo", info);
		return ResponseFormat.retParam(1,200,resultMap);
	}
    
    
    @Override
    public JsonBean getManuscriptPage(String token, Integer pageNumber, Integer pageSize,
                                      TblYqnsAuditMyManuscriptEntity vo) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectMyManuscriptList(vo, null, null));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }


    /**
     * 我的底稿--底稿清单--汇总;汇总查询数据-按照底稿导出
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getDraftListPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMyManuscriptEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
//        //==查询当前实施的项目！
//        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//        if(tnp == null) {
//            return ResponseFormat.retParam(0,30003,null);
//        }
//        entity.setProjectId(tnp.getId());
//        entity.setCreateUser(loginStaff.getStaffid().toString());
//        List<TblYqnsAuditMyManuscriptEntity> query = this.selecManuscriptList(entity);
        com.huabo.audit.util.PageInfo<TblYqnsAuditMyManuscriptEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selecManuscriptList(entity, null));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        HashMap<String, Object> result = new HashMap<>();
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }


    /**
     * 底稿清单-导出
     */
    @Override
    public JsonBean qdexcelUtils(HttpServletResponse response, String token, TblYqnsAuditMyManuscriptEntity entity, List<String> idList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
//        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//        if(tnp == null) {
//            return ResponseFormat.retParam(0,30003,null);
//        }

        String[] titles = {"序号", "填报时间", "项目名称", "被审计单位", "底稿编号", "审计查证事实", "审计结论及依据", "审计处理意见及建议", "编制人",
                "审计证据是否准确、真实、合法", "审计查证事实描述是否详尽、充分", "审计结论是否客观、公正", "审计处理意见及建议是否正确、具有可操作性和建设性", "基础工作",
                "审计查证事实", "审计结论及依据", "审计处理意见及建议",
                "审计组与被审计单位交换意见后修改审计查证事实描述", "备注"};
        PageInfo<TblYqnsAuditMyManuVerifyEntity> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);
//        TblYqnsAuditMyManuscriptEntity entity = new TblYqnsAuditMyManuscriptEntity();
//        entity.setProjectId(tnp.getId());
//        entity.setCreateUser(loginStaff.getStaffid().toString());
        List<TblYqnsAuditMyManuscriptEntity> query = this.selecManuscriptList(entity, idList);
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (TblYqnsAuditMyManuscriptEntity bean : query) {

            List<TblYqnsAuditMyManuVerifyEntity> listVerify = bean.getAuditMyManuVerifyEntityList();

            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getCreateTime() != null ? format.format(bean.getCreateTime()) : "";//填报时间
            obj[2] = bean.getProjectName();//项目名称
            obj[3] = bean.getAuditeeName();//被审计单位
            obj[4] = bean.getDraftNumber(); //底稿编号
            if (null != listVerify && listVerify.size() > 0) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(0);
                obj[5] = verify1.getVerificationDescription();
                obj[6] = verify1.getAuditConclusion();
                obj[7] = verify1.getHandlingOpinions();
            } else {
                obj[5] = "";
                obj[6] = "";
                obj[7] = "";
            }
            obj[8] = tblYqnsAuditMyManuscriptMapper.selectRealnameById(bean.getCreateUser()); //创建人
            obj[9] = bean.getEvidenceAccurate(); //审计证据是否准确、真实、合法
            obj[10] = bean.getDgverificationDescription(); //审计查证事实描述是否详尽、充分
            obj[11] = bean.getDgauditConclusion(); //审计结论是否客观、公正
            obj[12] = bean.getDghandlingOpinions(); //审计处理意见及建议是否正确、具有可操作性和建设性
            obj[13] = bean.getBasicWork(); //基础工作
            if (null != listVerify && listVerify.size() > 1) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(1);
                obj[14] = verify1.getVerificationDescription();
                obj[15] = verify1.getAuditConclusion();
                obj[16] = verify1.getHandlingOpinions();
            } else {
                obj[14] = "";
                obj[15] = "";
                obj[16] = "";
            }
            if (null != listVerify && listVerify.size() > 2) {
                TblYqnsAuditMyManuVerifyEntity verify1 = listVerify.get(2);
                obj[17] = verify1.getVerificationDescription();
                obj[18] = verify1.getRemarks();
            } else {
                obj[17] = "";
                obj[18] = "";
            }

            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("底稿".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);


    }


    /**
     * 底稿清单-导出Word
     */
    @Override
    public JsonBean excelWord(HttpServletResponse response, String token, TblYqnsAuditMyManuscriptEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 获取稿件信息
        JsonBean jsonBean = getMyManuscriptById(token, entity.getId());
        if (jsonBean == null || jsonBean.getData() == null) {
            return ResponseFormat.retParam(0, 500, "获取稿件信息失败");
        }

        TblYqnsAuditMyManuscriptEntity bean = (TblYqnsAuditMyManuscriptEntity) jsonBean.getData();
        TblStaff staff = tblStaffMapper.getById(bean.getCreateUser());
        bean.setCreateUsername(staff.getRealname());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        
        // 解析字符串为 Date，然后格式化为中文日期
        String chineseDate = outputFormat.format(bean.getCreateTime());
        bean.setExportDate(chineseDate);
        
        
        List<TblYqnsAuditMyManuVerifyEntity> tblYqnsAuditMyManuVerifyEntityList = bean.getAuditMyManuVerifyEntityList();
        TblYqnsAuditMyManuVerifyEntity tblYqnsAuditMyManuVerifyEntity = new TblYqnsAuditMyManuVerifyEntity();

        if (tblYqnsAuditMyManuVerifyEntityList != null && !tblYqnsAuditMyManuVerifyEntityList.isEmpty()) {
            tblYqnsAuditMyManuVerifyEntity = Collections.max(tblYqnsAuditMyManuVerifyEntityList, Comparator.comparingLong(TblYqnsAuditMyManuVerifyEntity::getId));
        }

        // 根据 probleMdraft 的值确定模板文件路径
        String templatePath = "1".equals(bean.getProbleMdraft())
                ? "/template/TblYqnsAuditMyManuscriptWordProblem.docx"
                : "/template/TblYqnsAuditMyManuscriptWord.docx";

        try {
            // 加载模板文件
            ClassPathResource resource = new ClassPathResource(templatePath);
            if (resource == null) {
                return ResponseFormat.retParam(0, 500, "模板文件不存在: " + templatePath);
            }

            try (InputStream ins = resource.getStream();
                 OutputStream out = response.getOutputStream()) {

                // 设置响应头以触发文件下载
                String filenameBase = "2-9-1审计工作底稿";
                String filenameSuffix = "1".equals(bean.getProbleMdraft()) ? "(问题型)" : "(复核型)";
                String filename = filenameBase + filenameSuffix + ".docx";
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes(), "UTF-8") );
                // 注册 xdocreport 实例并加载 FreeMarker 模板引擎
                IXDocReport report = XDocReportRegistry.getRegistry().loadReport(ins, TemplateEngineKind.Freemarker);
                // 创建 xdocreport 上下文对象，用于存放具体数据
                IContext context = report.createContext();
                // 将数据放入上下文
                context.put("bean", bean);
                context.put("entity", tblYqnsAuditMyManuVerifyEntity);

                // 处理报告并输出到响应流
                report.process(context, out);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseFormat.retParam(0, 500, "生成word文件时发生异常: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 500, "加载模板文件时发生异常: " + e.getMessage());
        }

        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
    
    
    /**
     * 底稿管理--根据项目底稿自动生成编号
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean autoCode(String token) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(loginStaff.getStaffid());
        List<TblYqnsAuditMyManuscriptEntity> fhlist = tblYqnsAuditMyManuscriptMapper.findbyPorjectid(projectId, "0");
        if(fhlist!=null && fhlist.size()>0) {
        	for (int i = 0; i < fhlist.size(); i++) {
        		String draftNumber="F";
        		TblYqnsAuditMyManuscriptEntity dg=fhlist.get(i);
				if(i<9) {
					draftNumber+="00"+(i+1);
				}else if(i>=9 && i<99) {
					draftNumber+="0"+(i+1);
				}else {
					draftNumber+=(i+1);
				}
				tblYqnsAuditMyManuscriptMapper.updateBycode(dg.getId(), draftNumber);
			}
        }
        List<TblYqnsAuditMyManuscriptEntity> wtlist = tblYqnsAuditMyManuscriptMapper.findbyPorjectid(projectId, "1");
        if(wtlist!=null && wtlist.size()>0) {
        	
        	for (int i = 0; i < wtlist.size(); i++) {
        		 String draftNumber="W";
        		TblYqnsAuditMyManuscriptEntity dg=wtlist.get(i);
				if(i<9) {
					draftNumber+="00"+(i+1);
				}else if(i>=9 && i<99) {
					draftNumber+="0"+(i+1);
				}else {
					draftNumber+=(i+1);
				}
				tblYqnsAuditMyManuscriptMapper.updateBycode(dg.getId(), draftNumber);
			}
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


}