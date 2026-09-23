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
import com.huabo.audit.oracle.mapper.TblYqnsAuditMethodMaintainMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProjectAuditTypeMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProjectAuditTypeNameMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjddMapper;
import com.huabo.audit.oracle.service.TblYqnsAuditMethodMaintainService;
import com.huabo.audit.oracle.service.TblYqnsMyTaskReviewService;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeNameService;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeService;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型 serviceImpl
 */
@Service
public class TblYqnsProjectAuditTypeServiceImpl extends ServiceImpl<TblYqnsProjectAuditTypeMapper, TblYqnsProjectAuditTypeEntity>
        implements TblYqnsProjectAuditTypeService {


    @Resource
    private TblYqnsProjectAuditTypeMapper tblYqnsProjectAuditTypeMapper;
    @Resource
    private TblYqnsProjectAuditTypeNameService tblYqnsProjectAuditTypeNameService;
    @Resource
    private TblYqnsAuditMethodMaintainService tblYqnsAuditMethodMaintainService;
    @Resource
    private TblYqnsMyTaskReviewService tblYqnsMyTaskReviewService;
    @Resource
    private TblYqnsSjddMapper tblYqnsSjddMapper;
    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 获取分页的审计工程审计类型 获取模板 Status=0
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTypePage(String token, Integer pageNumber, Integer pageSize, TblYqnsProjectAuditTypeEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsProjectAuditTypeEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsProjectAuditTypeEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectAuditTypeListZero(entity));

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
     * 获取的审计工程审计类型
     *  不分页 获取模板 Status=0
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTypeList(String token,  TblYqnsProjectAuditTypeEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        List<TblYqnsProjectAuditTypeEntity> auditTypeList= this.selectAuditTypeListZero(entity);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(auditTypeList);
        // 构建返回值条件
        result.put("auditTypeList", auditTypeList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 获取单独一个工程审计类型记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTypeById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 查询工程审计类型
        TblYqnsProjectAuditTypeEntity bean = this.getById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        this.packageEntity(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }


    /**
     * 获取多个工程审计类型记录
     *
     * @param token
     * @param ids 1,2,3
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getAuditTypeByIds(String token, String ids) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        // 查询工程审计类型
        List<TblYqnsProjectAuditTypeEntity> beanList = this.listByIds(idList);
        if (beanList == null || beanList.size()<1) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(beanList);
        return ResponseFormat.retParam(1, 200, beanList);
    }



    /**
     * 工程审计类型-增加修改
     *
     * @param token
     * @param addDiffTypeEntityList 未复制的 模板
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public List<TblYqnsProjectAuditTypeEntity> saveOrUpdateCP(String token, List<TblYqnsProjectAuditTypeEntity> addDiffTypeEntityList) throws Exception {
        addDiffTypeEntityList.stream().forEach(entity -> {
            try {
                // 进行复制 审计方法
                TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity = entity.getAuditMethodMaintainEntity();
                if (null != auditMethodMaintainEntity) {
                    auditMethodMaintainEntity.setSTATUS(TblYqnsAuditMethodMaintainEntity.STATUS_ONE);
                    auditMethodMaintainEntity.setId(null);
                    // 赋值新的CPid
                    tblYqnsAuditMethodMaintainService.saveOrUpdate(token, auditMethodMaintainEntity);
                    entity.setMethodMaintainId(auditMethodMaintainEntity.getId().toString());
                }

                // 进行复制 审计类型 审计类型-Name
                entity.setSTATUS(TblYqnsProjectAuditTypeEntity.STATUS_ONE);
                entity.setTypeId(entity.getId().toString() );
                entity.setId(null);
                // 给类型-Name list 赋值id
                entity.getAuditTypeNameEntityList().stream().forEach(vo -> {
                    vo.setId(null);
                    vo.setSTATUS(TblYqnsProjectAuditTypeNameEntity.STATUS_ONE);
                });
                // 新增修改 类型-Name list
                this.saveOrUpdate(token, entity);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });
        return addDiffTypeEntityList;
    }


    public JsonBean deleteCP(String token, List<TblYqnsProjectAuditTypeEntity> deleteDiffTypeEntityList) throws Exception {
        deleteDiffTypeEntityList.stream().forEach(entity -> {
            try {
                TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity = entity.getAuditMethodMaintainEntity();
                // 进行删除[无关联CP]审计方法维护关联
                if (null != auditMethodMaintainEntity) {
                    tblYqnsAuditMethodMaintainService.delete(token, auditMethodMaintainEntity.getId());
                }
                // 进行删除[无关联CP]审计类型-Name
                entity.getAuditTypeNameEntityList().stream().forEach(vo -> {
                    try {
                        tblYqnsProjectAuditTypeNameService.delete(token,vo.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                // 新增删除[无关联CP]list
                this.delete(token, entity.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



    /**
     * 工程审计类型-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsProjectAuditTypeEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null updateEntity
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getRealname());
        }else {
        	entity.setId(RandomUtil.uuLongId());
        	entity.setCreateTime(new Date());
        }
        if (StringUtils.isEmpty(entity.getSTATUS())) {
            entity.setSTATUS(TblYqnsProjectAuditTypeEntity.STATUS_ZERO);
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        if (null != entity.getAuditTypeNameEntityList()){
            entity.getAuditTypeNameEntityList().stream().forEach(vo -> {
                vo.setTypeId(entity.getId().toString());
            });
            tblYqnsProjectAuditTypeNameService.saveOrUpdateList(token, entity.getAuditTypeNameEntityList());
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 删除工程审计类型(直接删除)
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
     * 查询工程审计类型 -复制 One
     *
     * @param entity
     * @return
     */
    public List<TblYqnsProjectAuditTypeEntity> selectAuditTypeListOne(TblYqnsProjectAuditTypeEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .eq(TblYqnsProjectAuditTypeEntity::getSTATUS,TblYqnsAuditMethodMaintainEntity.STATUS_ONE)
                .eq(StringUtil.isNotEmpty(entity.getTypeName()), TblYqnsProjectAuditTypeEntity::getTypeName, entity.getTypeName());
        return tblYqnsProjectAuditTypeMapper.selectList(query);
    }

    /**
     * 查询工程审计类型 - 模板 zero
     * @param entity
     * @return
     */
    public List<TblYqnsProjectAuditTypeEntity> selectAuditTypeListZero(TblYqnsProjectAuditTypeEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .eq(TblYqnsProjectAuditTypeEntity::getSTATUS,TblYqnsAuditMethodMaintainEntity.STATUS_ZERO)
                .like(StringUtil.isNotEmpty(entity.getTypeName()), TblYqnsProjectAuditTypeEntity::getTypeName, "%" + entity.getTypeName() + "%");
        return tblYqnsProjectAuditTypeMapper.selectList(query);
    }

    /**
     * 查询工程审计类型 -通过 ids查询
     *
     * @param ids -> List<ids>  [1, 2, 3]
     * @return
     */
    @Override
    public List<TblYqnsProjectAuditTypeEntity> getAuditTypeListByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());

        // 进行数据获取和查询 进行MaintainId排序
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .in(TblYqnsProjectAuditTypeEntity::getId,idList)
                .orderByDesc(TblYqnsProjectAuditTypeEntity::getMethodMaintainId);
        List<TblYqnsProjectAuditTypeEntity> entityList =  tblYqnsProjectAuditTypeMapper.selectList(query);
        entityList.forEach(dto -> {
            this.packageEntity(dto);
        });
        return entityList;
    }

    /**
     * 查询工程审计类型 - 通过模板id
     * 有模板id 肯定是复制Cp 所以是 STATUS_ONE
     * @param templateId 模板id
     * @return
     */
    @Override
    public List<TblYqnsProjectAuditTypeEntity> getAuditTypeListByTemplateId(String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            return null;
        }
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .eq(TblYqnsProjectAuditTypeEntity::getSTATUS, TblYqnsProjectAuditTypeEntity.STATUS_ONE)
                .eq(TblYqnsProjectAuditTypeEntity::getTemplateId, templateId);
        List<TblYqnsProjectAuditTypeEntity> entityList = tblYqnsProjectAuditTypeMapper.selectList(query);
        entityList.forEach(dto -> {
            this.packageEntity(dto);
        });
        return entityList;
    }

    /**
     * 封装工程审计类型-获取子集 【获取模板】
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public TblYqnsProjectAuditTypeEntity packageEntity(TblYqnsProjectAuditTypeEntity entity) {
        if (null != entity.getId()) {
            // 查询工程审计类型-Name
            List<TblYqnsProjectAuditTypeNameEntity> typeNameEntityList = tblYqnsProjectAuditTypeNameService.getListByTypeId(entity.getId().toString());
//            typeNameEntityList.stream().forEach(typeNameEntity->{
//                if (null != typeNameEntity.getId()) {
//                    // 查询我的任务-审查
//                    TblYqnsMyTaskReviewEntity tblYqnsMyTaskReviewEntity = tblYqnsMyTaskReviewService.getReviewByTypeId(Long.valueOf(typeNameEntity.getId()));
//                    typeNameEntity.setTblYqnsMyTaskReviewEntity(tblYqnsMyTaskReviewEntity);
//                }
//            });
            entity.setAuditTypeNameEntityList(typeNameEntityList);
        }
        if (StringUtils.isNotEmpty(entity.getMethodMaintainId())) {
            // 查询审计方法维护
            TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity = tblYqnsAuditMethodMaintainService.getMethodMaintainById(Long.valueOf(entity.getMethodMaintainId()));
            if (auditMethodMaintainEntity != null && StringUtils.isEmpty(auditMethodMaintainEntity.getMethodId())) {
                auditMethodMaintainEntity.setMethodId(auditMethodMaintainEntity.getId().toString());
            }
            entity.setAuditMethodMaintainEntity(auditMethodMaintainEntity);
        }
        return entity;
    }
    
    
    /**
     * 查询工程审计类型 -我的任务
     *
     * @param ids -> List<ids>  [1, 2, 3]
     * @return
     */
    @Override
    public List<TblYqnsProjectAuditTypeEntity> gettaskListByIds(String ids,Long templateId,BigDecimal projectId) {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());

        // 进行数据获取和查询 进行MaintainId排序
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .in(TblYqnsProjectAuditTypeEntity::getId,idList)
                .orderByDesc(TblYqnsProjectAuditTypeEntity::getMethodMaintainId);
        List<TblYqnsProjectAuditTypeEntity> entityList =  tblYqnsProjectAuditTypeMapper.selectList(query);
        entityList.forEach(dto -> {
            this.taskpackageEntity(dto, templateId, projectId);
        });
        return entityList;
    }
    
    
    public TblYqnsProjectAuditTypeEntity taskpackageEntity(TblYqnsProjectAuditTypeEntity entity,Long templateId,BigDecimal projectId) {
        if (null != entity.getId()) {
            // 查询工程审计类型-Name
            List<TblYqnsProjectAuditTypeNameEntity> typeNameEntityList = tblYqnsProjectAuditTypeNameService.getListByTypeId(entity.getId().toString());
            typeNameEntityList.stream().forEach(typeNameEntity->{
                if (null != typeNameEntity.getId() && templateId!=null) {
                    // 查询我的任务-审查
                    TblYqnsMyTaskReviewEntity tblYqnsMyTaskReviewEntity = tblYqnsMyTaskReviewService.gettaskTypeId(Long.valueOf(typeNameEntity.getId()),templateId,projectId);
                    typeNameEntity.setTblYqnsMyTaskReviewEntity(tblYqnsMyTaskReviewEntity);
                }
            });
            entity.setAuditTypeNameEntityList(typeNameEntityList);
        }
        if (StringUtils.isNotEmpty(entity.getMethodMaintainId())) {
            // 查询审计方法维护
            TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity = tblYqnsAuditMethodMaintainService.getMethodMaintainById(Long.valueOf(entity.getMethodMaintainId()));
            if (auditMethodMaintainEntity != null && StringUtils.isEmpty(auditMethodMaintainEntity.getMethodId())) {
                auditMethodMaintainEntity.setMethodId(auditMethodMaintainEntity.getId().toString());
            }
            entity.setAuditMethodMaintainEntity(auditMethodMaintainEntity);
        }
        return entity;
    }
    
    
    
    
    
    /**
     * 查询工程审计类型 -我的任务
     *
     * @param ids -> List<ids>  [1, 2, 3]
     * @return
     */
    @Override
    public List<TblYqnsProjectAuditTypeEntity> getddtaskListByIds(String ids,BigDecimal projectId) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());

        // 进行数据获取和查询 进行MaintainId排序
        LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeEntity>()
                .in(TblYqnsProjectAuditTypeEntity::getId,idList)
                .orderByDesc(TblYqnsProjectAuditTypeEntity::getMethodMaintainId);
        List<TblYqnsProjectAuditTypeEntity> entityList =  tblYqnsProjectAuditTypeMapper.selectList(query);
        entityList.forEach(dto -> {
            this.ddtaskpackageEntity(dto, projectId);
        });
        return entityList;
    }
    
    
    public TblYqnsProjectAuditTypeEntity ddtaskpackageEntity(TblYqnsProjectAuditTypeEntity entity,BigDecimal projectId) {
        if (null != entity.getId()) {
            // 查询工程审计类型-Name
            List<TblYqnsProjectAuditTypeNameEntity> typeNameEntityList = tblYqnsProjectAuditTypeNameService.getListByTypeId(entity.getId().toString());
            typeNameEntityList.stream().forEach(typeNameEntity->{
                if (null != typeNameEntity.getId() && projectId!=null) {
					try {
						 // 根据模板查询督导任务
						List<TblYqnsSjdd> list = tblYqnsSjddMapper.selectByrwId(typeNameEntity.getId(),projectId);
						if(list!=null && list.size()>0) {
	                		typeNameEntity.setSjddrw(list.get(0));
	                	}
					} catch (Exception e) {
						e.printStackTrace();
					}
                	
                }
            });
            entity.setAuditTypeNameEntityList(typeNameEntityList);
        }
        if (StringUtils.isNotEmpty(entity.getMethodMaintainId())) {
            // 查询审计方法维护
            TblYqnsAuditMethodMaintainEntity auditMethodMaintainEntity = tblYqnsAuditMethodMaintainService.getMethodMaintainById(Long.valueOf(entity.getMethodMaintainId()));
            if (auditMethodMaintainEntity != null && StringUtils.isEmpty(auditMethodMaintainEntity.getMethodId())) {
                auditMethodMaintainEntity.setMethodId(auditMethodMaintainEntity.getId().toString());
            }
            entity.setAuditMethodMaintainEntity(auditMethodMaintainEntity);
        }
        return entity;
    }
    
    
    
    

}
