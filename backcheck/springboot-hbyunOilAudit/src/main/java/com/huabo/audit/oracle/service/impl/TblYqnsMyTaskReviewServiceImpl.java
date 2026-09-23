package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsAuditOverseeRecordsMapper;
import com.huabo.audit.oracle.mapper.TblYqnsMyTaskReviewMapper;
import com.huabo.audit.oracle.service.TblYqnsMyTaskReviewService;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.util.PageInfoUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsMyTaskReviewServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/28 14:40
 * @version: V1.0
 * @description: 央企内审-审计实施-我的任务-审查 serviceImpl
 */
@Service
public class TblYqnsMyTaskReviewServiceImpl extends ServiceImpl<TblYqnsMyTaskReviewMapper, TblYqnsMyTaskReviewEntity>
        implements TblYqnsMyTaskReviewService {

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private ImplementPlanService implementPlanService;
    
    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private TblYqnsMyTaskReviewMapper tblYqnsMyTaskReviewMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
	public JsonBean getMyTaskReviewList(String token, TblYqnsMyTaskReviewEntity vo) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
    	
        ImplementPlanEntity tnp = null;
        if(vo.getProjectId() == null) {
        	tnp = this.implementPlanService.getCurrenNbsjProjectByLoginStaff(user.getStaffid());
        }else {
            tnp = implementPlanMapper.selectById(vo.getProjectId().toString());
        }
        if(tnp == null) {
        	 return ResponseFormat.retParam(0, 10001, "未找到当前实施项目");
        }
        
        //是否可以查看其他人创建的数据 true有  false 没有
        boolean totalFlag = false;

        //校验当前人是否有查询全部数据权限
        if(user.getStaffid().compareTo(tnp.getProjectOrderId()) == 0 || user.getStaffid().compareTo(tnp.getFzstaffid()) == 0 || user.getStaffid().compareTo(tnp.getZsstaffid()) == 0 ) {
        	totalFlag = true;
        }
        
        List<TblYqnsMyTaskReviewEntity> voList = this.tblYqnsMyTaskReviewMapper.selectListByVo(vo,user,totalFlag);
        return ResponseFormat.retParam(1,200,voList);
	}
    
    
    

    /**
     * 获取单独一个我的任务-审查记录
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getReviewById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsMyTaskReviewEntity bean = this.getReviewById(id);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 我的任务-审查详情
     * @param id
     * @return
     */
    public TblYqnsMyTaskReviewEntity getReviewById(Long id){
        TblYqnsMyTaskReviewEntity bean = this.getById(id);
        if (bean == null){
            return null;
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString(),null);
        bean.setAttachments(attachments);
        return bean;
    }
    
    
    /**
     * 我的任务-审查详情id 查询附件列表
     * @param id
     * @return
     */
    @Override
    public JsonBean getReviewattlistById(String token,String id,String attids) throws Exception{
    	 TblStaffUtil user = userProvider.get();
         if(user == null) {
             return ResponseFormat.retParam(0,20006,null);
         }
    	   List<TblAttachment> attachments = null;
    	   if(id!=null&&id.length()>0){
    		   attachments= this.baseMapper.selectAttachmentListByPk(id,attids);
    	   }
           Map<String,Object> resultMap = new HashMap<String,Object>(0);
           resultMap.put("data", attachments);
           return ResponseFormat.retParam(1,200,resultMap);
    }
    
    


    /**
     * 使用TypeID进行查询
     * @param typeId
     * @return
     */
    public TblYqnsMyTaskReviewEntity getReviewByTypeId(Long typeId){
        LambdaQueryWrapper<TblYqnsMyTaskReviewEntity> query = new LambdaQueryWrapper<TblYqnsMyTaskReviewEntity>()
                .eq(TblYqnsMyTaskReviewEntity::getTypeNameId, typeId);
        TblYqnsMyTaskReviewEntity bean = this.baseMapper.selectOne(query);
        if (bean == null){
            return null;
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString(),null);
        bean.setAttachments(attachments);
        return bean;
    }


    /**
     * 我的任务-审查-增加修改
     *
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsMyTaskReviewEntity entity) throws Exception {
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
        
       
        if (null == entity.getProjectId()) {
        	ImplementPlanEntity tnp = this.implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 10001, "未找到当前实施项目");
            }
            entity.setProjectId(tnp.getId());
        }
        entity.setCreateUser(loginStaff.getStaffid().toString());
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        // 附件关联操作
        List<String> attIds = entity.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(entity.getId().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 保存多个 我的任务-审查
     * @param token
     * @param entityList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdateList(String token, List<TblYqnsMyTaskReviewEntity> entityList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        
        ImplementPlanEntity tnp = this.implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (tnp == null) {
        	return ResponseFormat.retParam(0, 10001, "未找到当前实施项目");
        }
            
        BigDecimal projcetId = tnp.getId(); 
            
        entityList.stream().forEach(entity->{
        	
        	if(entity.getProjectId() == null) {
        		entity.setProjectId(projcetId);
        	}
        	
            // check id is null
            if (null != entity.getId()) {
                entity.setUpdateTime(new Date());
                entity.setUpdateUser(loginStaff.getRealname());
            }
            boolean ret = this.saveOrUpdate(entity);
            if (!ret) {
                    throw new RuntimeException("我的任务-审查 上传失败");
            }
            // 附件关联操作
            List<String> attIds = entity.getAttIds();
            if (attIds != null && attIds.size() > 0) {
                for (String attId : attIds) {
                    this.baseMapper.saveAtt(entity.getId().toString(), attId);
                }
            }
        });

        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 删除我的任务-审查(直接删除)
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

     
    
    /**
     * 使用TypeID和关联项目查询进行查询
     * @param typeId
     * @return
     */
    public TblYqnsMyTaskReviewEntity gettaskTypeId(Long typeId,Long templateId,BigDecimal projectId){
        LambdaQueryWrapper<TblYqnsMyTaskReviewEntity> query = new LambdaQueryWrapper<TblYqnsMyTaskReviewEntity>()
                .eq(TblYqnsMyTaskReviewEntity::getTypeNameId, typeId)
                .eq(TblYqnsMyTaskReviewEntity::getTemplateId, templateId);
        TblYqnsMyTaskReviewEntity bean = this.baseMapper.selectOne(query);
        if (bean == null){
            return null;
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString(),null);
        bean.setAttachments(attachments);
        return bean;
    }
    
}
