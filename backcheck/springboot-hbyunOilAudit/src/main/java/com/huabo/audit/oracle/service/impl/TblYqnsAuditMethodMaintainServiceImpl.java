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
import com.huabo.audit.oracle.entity.TblYqnsAuditMethodMaintainEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.mapper.TblYqnsAuditMethodMaintainMapper;
import com.huabo.audit.oracle.service.TblYqnsAuditMethodMaintainService;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintainServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-审计方法维护 serviceImpl
 */
@Service
public class TblYqnsAuditMethodMaintainServiceImpl extends ServiceImpl<TblYqnsAuditMethodMaintainMapper, TblYqnsAuditMethodMaintainEntity>
        implements TblYqnsAuditMethodMaintainService {


    @Resource
    private TblYqnsAuditMethodMaintainMapper tblYqnsAuditMethodMaintainMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 获取分页的审计方法维护-分页 模板 - Zero
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMethodMaintainPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMethodMaintainEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditMethodMaintainEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditMethodMaintainEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectMethodMaintainList(entity));

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
     * 获取分页的审计方法维护 模板 - Zero
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMethodMaintainList(String token, TblYqnsAuditMethodMaintainEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        List<TblYqnsAuditMethodMaintainEntity> methodMaintainList = this.selectMethodMaintainList(entity);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(methodMaintainList);

        result.put("methodMaintainList", methodMaintainList);
        return ResponseFormat.retParam(1, "查询成功", result);
    }

    /**
     * 获取单独一个审计方法维护记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getMethodMaintainById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsAuditMethodMaintainEntity bean = this.getMethodMaintainById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }


    /**
     * 获取单独一个审计方法维护记录(获取模板)
     *
     * @param id
     * @throws Exception
     */
    @Override
    public TblYqnsAuditMethodMaintainEntity getMethodMaintainById(Long id) {
        LambdaQueryWrapper<TblYqnsAuditMethodMaintainEntity> query = new LambdaQueryWrapper<TblYqnsAuditMethodMaintainEntity>()
                .eq(TblYqnsAuditMethodMaintainEntity::getId, id);
        return tblYqnsAuditMethodMaintainMapper.selectOne(query);
    }


    /**
     * 审计方法维护-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsAuditMethodMaintainEntity entity) throws Exception {
        // 验证token
//        TblStaffUtil loginStaff = userProvider.get();
//        if (loginStaff == null) {
//            return ResponseFormat.retParam(0, 20006, null);
//        }
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
//            entity.setUpdateUser(loginStaff.getRealname());
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }
        if (StringUtil.isEmpty(entity.getSTATUS())) {
            entity.setSTATUS(TblYqnsAuditMethodMaintainEntity.STATUS_ZERO);
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 删除审计方法维护(直接删除)
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
     * 查询审计方法维护 - 模板 Zero
     *
     * @param entity
     * @return
     */
    public List<TblYqnsAuditMethodMaintainEntity> selectMethodMaintainList(TblYqnsAuditMethodMaintainEntity entity) {
        // 进行数据获取和查询(只查询模板)
        LambdaQueryWrapper<TblYqnsAuditMethodMaintainEntity> query = new LambdaQueryWrapper<TblYqnsAuditMethodMaintainEntity>()
                .eq(TblYqnsAuditMethodMaintainEntity::getSTATUS, TblYqnsAuditMethodMaintainEntity.STATUS_ZERO)
                .like(StringUtil.isNotEmpty(entity.getMethodName()), TblYqnsAuditMethodMaintainEntity::getMethodName, "%"+ entity.getMethodName() + "%");
        return tblYqnsAuditMethodMaintainMapper.selectList(query);
    }
}
