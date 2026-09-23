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
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsTemplateManagerEntity;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsTemplateManagerMapper;
import com.huabo.audit.oracle.service.TblYqnsTemplateManagerService;
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
 * @CLASS_NAME: TblYqnsTemplateManagerServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-模板管理 serviceImpl
 */
@Service
public class TblYqnsTemplateManagerServiceImpl extends ServiceImpl<TblYqnsTemplateManagerMapper, TblYqnsTemplateManagerEntity>
        implements TblYqnsTemplateManagerService {


    @Resource
    private TblYqnsTemplateManagerMapper TblYqnsTemplateManagerMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
    private ReservePropertyService reservePropertyService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取分页的模板管理列表
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getTemplateList(String token, Integer pageNumber, Integer pageSize, TblYqnsTemplateManagerEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditWorkRecordsEntity> info = new com.huabo.audit.util.PageInfo<>();
        com.github.pagehelper.PageInfo<TblYqnsAuditWorkRecordsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectTemplateList(entity));

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
     * 获取单独一个模板管理记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getTemplateById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsTemplateManagerEntity bean = this.getById(id);
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
     * 模板管理-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsTemplateManagerEntity entity) throws Exception {
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
            entity.setCreateUser(loginStaff.getRealname());
        }
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
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 删除模板管理(直接删除)
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
     * 查询模板管理
     *
     * @param entity
     * @return
     */
    public List<TblYqnsTemplateManagerEntity> selectTemplateList(TblYqnsTemplateManagerEntity entity) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsTemplateManagerEntity> query = new LambdaQueryWrapper<TblYqnsTemplateManagerEntity>()
                .like(StringUtil.isNotEmpty(entity.getTemplateName()), TblYqnsTemplateManagerEntity::getTemplateName, entity.getTemplateName());
        return TblYqnsTemplateManagerMapper.selectList(query);
    }
}
