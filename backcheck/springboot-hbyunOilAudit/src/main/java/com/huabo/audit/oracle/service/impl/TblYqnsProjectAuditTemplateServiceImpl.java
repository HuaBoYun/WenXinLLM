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
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTemplateEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeNameEntity;
import com.huabo.audit.oracle.mapper.TblYqnsProjectAuditTemplateMapper;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTemplateService;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeService;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTemplateServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计模板 serviceImpl
 */
@Service
public class TblYqnsProjectAuditTemplateServiceImpl extends ServiceImpl<TblYqnsProjectAuditTemplateMapper, TblYqnsProjectAuditTemplateEntity>
        implements TblYqnsProjectAuditTemplateService {


    @Resource
    private TblYqnsProjectAuditTemplateMapper tblYqnsProjectAuditTemplateMapper;

    @Resource
    private TblYqnsProjectAuditTypeService tblYqnsProjectAuditTypeService;
    @Autowired
    private ReservePropertyService reservePropertyService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取分页的审计工程审计模板
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTemplatePage(String token, Integer pageNumber, Integer pageSize, TblYqnsProjectAuditTemplateEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsProjectAuditTemplateEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsProjectAuditTemplateEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectTemplateList(entity));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);
        return ResponseFormat.retParam(1, "查询成功", result);
    }


    /**
     * 获取审计工程审计模板明细
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTemplateList(String token, TblYqnsProjectAuditTemplateEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        List<TblYqnsProjectAuditTemplateEntity> entityList = this.selectTemplateList(entity);
        // 构建返回值条件
        result.put("entityList", entityList);
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
    public JsonBean getAuditTemplateById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProjectAuditTemplateEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 复制整套工程审计模板
     *
     * @param token
     * @param templateId 原工程模板id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean cpTemplateByTemplateId(String token, String templateId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProjectAuditTemplateEntity entity = this.getById(templateId);
        entity.setTemplateId(entity.getId().toString());
        entity.setId(null);

        // 获取关联审计类型（获取模板）
        List<TblYqnsProjectAuditTypeEntity> typeEntityList = tblYqnsProjectAuditTypeService.getAuditTypeListByIds(entity.getTypeIds());
        if (null == typeEntityList) {
            typeEntityList = new ArrayList<>();
        }
        // 获取关联审计类型（获取复制）
        List<TblYqnsProjectAuditTypeEntity> typeCpEntityList = tblYqnsProjectAuditTypeService.getAuditTypeListByTemplateId(String.valueOf(entity.getId()));
        if (null == typeCpEntityList) {
            typeCpEntityList = new ArrayList<>();
        }
        // 使用map将typeEntityList 对象转换为id属性 与idCpSet进行去重 模板ID
        List<Long> idSet = typeEntityList.stream()
                .map(TblYqnsProjectAuditTypeEntity::getId)
                .collect(Collectors.toList());
        // 复制类型id的 模板id
        List<Long> idCpSet = typeCpEntityList.stream()
                .map(idCp -> Long.valueOf(idCp.getTypeId()))
                .collect(Collectors.toList());
        // 获取idCpSet不在idSet列表中的id  【没有保存过的Cp数据，需要进行cpAdd】
        List<Long> addDiffIdList = idSet.stream()
                .filter(id -> !idCpSet.contains(id)) // 使用filter过滤出idCpSet不在idSet列表中的元素
                .collect(Collectors.toList()); // 使用collect将Stream转换为List
        // 获取需要cp的模板数据 【diffTypeEntityList ： 需要cp的AuditType数据】
        List<TblYqnsProjectAuditTypeEntity> addDiffTypeEntityList = typeEntityList.stream()
                .filter(addEntity -> addDiffIdList.contains(addEntity.getId()))
                .collect(Collectors.toList());
        // 获取idCpSet不在idSet列表中的id  【没有保存过的Cp数据，需要进行cpAdd】
        List<Long> deleteDiffIdList = idCpSet.stream()
                .filter(id -> !idSet.contains(id)) // 使用filter过滤出idCpSet不在idSet列表中的元素
                .collect(Collectors.toList()); // 使用collect将Stream转换为List
        // 获取需要cp的模板数据 【diffTypeEntityList ： 需要cp的AuditType数据】
        List<TblYqnsProjectAuditTypeEntity> deleteDiffTypeEntityList = typeCpEntityList.stream()
                .filter(deleteEntity -> deleteDiffIdList.contains(Long.valueOf(deleteEntity.getTypeId())))
                .collect(Collectors.toList());
        // 进行保存模板 获取到模板id
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        // 对Cp数据 进行赋值模板id
        addDiffTypeEntityList.stream().forEach(vo -> {
            vo.setTemplateId(entity.getId().toString());
        });
        // 进行关联-复制修改
        tblYqnsProjectAuditTypeService.saveOrUpdateCP(token, addDiffTypeEntityList);
        // 删除无关联-复制信息
        tblYqnsProjectAuditTypeService.deleteCP(token, deleteDiffTypeEntityList);
        // 获取最新cp数据并获取的cpIds
        List<TblYqnsProjectAuditTypeEntity> typeEntityCpNewList = tblYqnsProjectAuditTypeService.getAuditTypeListByTemplateId(String.valueOf(entity.getId()));
        if (null == typeEntityCpNewList) {
            typeEntityCpNewList = new ArrayList<>();
        }
        String typeCpEntityNewIds = typeEntityCpNewList.stream()
                .map(newEntity -> String.valueOf(newEntity.getId()))
                .collect(Collectors.joining(","));
        // 赋值最新的cpIds

        entity.setTypeCpIds(typeCpEntityNewIds);
        entity.setSTATUS(TblYqnsProjectAuditTemplateEntity.STATUS_ONE);
        // 进行保存模板
        boolean ret1 = this.saveOrUpdate(entity);
        if (!ret1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 工程审计模板-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsProjectAuditTemplateEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getRealname());
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }
        entity.setSTATUS(TblYqnsProjectAuditTemplateEntity.STATUS_ZERO);
        // 进行保存模板
        boolean ret1 = this.saveOrUpdate(entity);
        if (!ret1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除工程审计模板(直接删除)
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
     * 查询工程审计模板 ZERO
     *
     * @param entity
     * @return
     */
    public List<TblYqnsProjectAuditTemplateEntity> selectTemplateList(TblYqnsProjectAuditTemplateEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsProjectAuditTemplateEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTemplateEntity>()
                .like(StringUtil.isNotEmpty(entity.getTemplateName()), TblYqnsProjectAuditTemplateEntity::getTemplateName, "%" + entity.getTemplateName() + "%")
                .eq(TblYqnsProjectAuditTemplateEntity::getSTATUS, TblYqnsProjectAuditTemplateEntity.STATUS_ZERO);
        List<TblYqnsProjectAuditTemplateEntity> entityList = tblYqnsProjectAuditTemplateMapper.selectList(query);
        entityList.forEach(dto -> {
            try {
                List<TblYqnsProjectAuditTypeEntity> typeEntityList = tblYqnsProjectAuditTypeService.getAuditTypeListByIds(dto.getTypeIds());
                //构建预留字段返回
                reservePropertyService.buildReserveProperty(typeEntityList);
                dto.setAuditTypeList(typeEntityList);
            } catch (Exception e) {
                e.printStackTrace();
                new RuntimeException("模板-获取工程类型错误");
            }

            //构建预留字段返回
            reservePropertyService.buildReserveProperty(dto);
        });
        return entityList;
    }


    /**
     * 获取单一工程审计模板下的所有子集 获取复制的
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean previewTemplateById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProjectAuditTemplateEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblYqnsProjectAuditTypeEntity> typeEntityList = tblYqnsProjectAuditTypeService.getAuditTypeListByIds(bean.getTypeCpIds());
        if(typeEntityList==null) {
        	typeEntityList = tblYqnsProjectAuditTypeService.getAuditTypeListByIds(bean.getTypeIds());
        }
        bean.setAuditTypeList(typeEntityList);
        return ResponseFormat.retParam(1, 200, bean);
    }
    
    
    
    /**
     * 获取单一工程审计模板下的所有子集 获取复制的
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean mytaskTemplateById(String token,Long id,Long templateId,BigDecimal projectId)throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProjectAuditTemplateEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblYqnsProjectAuditTypeEntity> typeEntityList = tblYqnsProjectAuditTypeService.gettaskListByIds(bean.getTypeCpIds(),templateId,projectId);
        if(typeEntityList==null) {
        	typeEntityList = tblYqnsProjectAuditTypeService.gettaskListByIds(bean.getTypeIds(),templateId,projectId);
        }
        bean.setAuditTypeList(typeEntityList);
        return ResponseFormat.retParam(1, 200, bean);
    }
    
    
    
    /**
     * 督导任务-获取模版内容
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean ddtaskTemplateById(String token,Long id,BigDecimal projectId)throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProjectAuditTemplateEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblYqnsProjectAuditTypeEntity> typeEntityList = tblYqnsProjectAuditTypeService.getddtaskListByIds(bean.getTypeCpIds(),projectId);
        if(typeEntityList==null) {
        	typeEntityList = tblYqnsProjectAuditTypeService.getddtaskListByIds(bean.getTypeIds(),projectId);
        }
        bean.setAuditTypeList(typeEntityList);
        return ResponseFormat.retParam(1, 200, bean);
    }
    
    
    
}
