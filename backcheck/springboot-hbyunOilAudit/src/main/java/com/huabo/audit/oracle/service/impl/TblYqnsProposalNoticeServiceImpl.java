package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsFgldhz;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSlbg;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProposalNoticeMapper;
import com.huabo.audit.oracle.service.TblYqnsProposalNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeNoticeServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企模块-计划编制-审计立项建议通知 serviceImpl
 */
@Service
public class TblYqnsProposalNoticeServiceImpl extends ServiceImpl<TblYqnsProposalNoticeMapper, TblYqnsProposalNoticeEntity>
        implements TblYqnsProposalNoticeService {


    @Resource
    private TblYqnsProposalNoticeMapper tblYqnsProposalNoticeMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 获取分页的审计立项建议通知列表
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getNoticeList(String token, Integer pageNumber, Integer pageSize, TblYqnsProposalNoticeEntity entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        /*entity.setCreateUser(loginStaff.getStaffid().toString());
        PageInfo<TblYqnsProposalNoticeEntity> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, entity));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, entity));

        result.put("pageInfo", pageInfo);*/
        //创建分页对象
        IPage<TblYqnsProposalNoticeEntity> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsProposalNoticeEntity> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(entity.getDocument())) {
            wrapper.lambda().like(TblYqnsProposalNoticeEntity::getDocument, entity.getDocument());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(entity.getTitle())) {
            wrapper.lambda().like(TblYqnsProposalNoticeEntity::getTitle, entity.getTitle());
        }
        //倒序
        wrapper.orderByDesc(true, "CREATEUSER");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        IPage<TblYqnsProposalNoticeEntity> page = baseMapper.selectPage(query, wrapper);
        return ResponseFormat.retParam(1, "查询成功", result);
    }



    /**
     * 获取单独一个审计立项建议通知
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getNoticeById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsProposalNoticeEntity bean = this.getById(id);
        if (bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString());
        bean.setAttachments(attachments);
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 审计立项建议通知-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsProposalNoticeEntity entity) throws Exception {
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
            entity.setCreateUser(loginStaff.getStaffid().toString());
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
     * 删除审计立项建议通知(直接删除)
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
        // check id is null
        this.baseMapper.deleteAttById(attId);
        BigDecimal attIdDecimal  = new BigDecimal(attId);
        tblAttachmentMapper.deleteEntity(attIdDecimal);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

}
