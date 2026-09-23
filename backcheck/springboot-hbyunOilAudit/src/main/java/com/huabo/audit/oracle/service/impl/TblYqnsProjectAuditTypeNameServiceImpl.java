package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeNameEntity;
import com.huabo.audit.oracle.mapper.TblYqnsProjectAuditTypeMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProjectAuditTypeNameMapper;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeNameService;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeNameServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型-NAME子列表 serviceImpl
 */
@Service
public class TblYqnsProjectAuditTypeNameServiceImpl extends ServiceImpl<TblYqnsProjectAuditTypeNameMapper, TblYqnsProjectAuditTypeNameEntity>
        implements TblYqnsProjectAuditTypeNameService {



    @Resource
    private TblYqnsProjectAuditTypeNameMapper tblYqnsProjectAuditTypeNameMapper;

    @Resource
    private UserProvider userProvider;



    /**
     * 获取单独一个工程审计类型记录
     *
     * @param token
     * @param typeId
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getListByTypeId(String token, String typeId) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsProjectAuditTypeNameEntity>  list= this.getListByTypeId(typeId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        return ResponseFormat.retParam(1, "查询成功", result);
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
    public JsonBean saveOrUpdate(String token, TblYqnsProjectAuditTypeNameEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (null != entity.getId()) {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(loginStaff.getStaffid().toString());
        }else {
        	entity.setId(RandomUtil.uuLongId());
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



    /**
     * 工程审计类型-增加修改
     *
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdateList(String token,  List<TblYqnsProjectAuditTypeNameEntity> voList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        voList.stream().forEach(vo -> {
            // check id is null
            if (null != vo.getId()) {
                vo.setUpdateTime(new Date());
                vo.setUpdateUser(loginStaff.getRealname());
            }else {
            	vo.setCreateTime(new Date());
            	
            	vo.setId(RandomUtil.uuLongId());
            }
            if (StringUtils.isEmpty(vo.getSTATUS())) {
                vo.setSTATUS(TblYqnsProjectAuditTypeNameEntity.STATUS_ZERO);
            }
        });
        boolean ret = this.saveOrUpdateBatch(voList);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
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
     * 查询工程审计类型
     *
     * @param typeId
     * @return
     */
    @Override
    public List<TblYqnsProjectAuditTypeNameEntity> getListByTypeId(String typeId) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsProjectAuditTypeNameEntity> query = new LambdaQueryWrapper<TblYqnsProjectAuditTypeNameEntity>()
                .eq(StringUtil.isNotEmpty(typeId), TblYqnsProjectAuditTypeNameEntity::getTypeId, typeId);
        List<TblYqnsProjectAuditTypeNameEntity>  list= tblYqnsProjectAuditTypeNameMapper.selectList(query);
        return list;
    }
}
