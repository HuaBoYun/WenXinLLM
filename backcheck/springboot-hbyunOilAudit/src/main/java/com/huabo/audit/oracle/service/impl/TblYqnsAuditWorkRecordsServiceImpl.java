package com.huabo.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblYqnsAuditWorkRecordsMapper;
import com.huabo.audit.oracle.service.TblYqnsAuditWorkRecordsService;
import com.huabo.audit.service.impl.ReservePropertyService;
import com.sun.org.apache.bcel.internal.generic.NEW;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname TblYqnsAuditWorkRecordsServiceImpl
 * @Description TODO  央企内审-审计实施-审计工作记录 serviceImpl
 * @Date 2023/10/8 21:56
 * @Created by GJ.C
 */
@Service
public class TblYqnsAuditWorkRecordsServiceImpl implements TblYqnsAuditWorkRecordsService {

    @Resource
    private TblYqnsAuditWorkRecordsMapper tblYqnsAuditWorkRecordsMapper;

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
     * 获取分页的审计工作记录
     *
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    @Override
    public JsonBean getRecordsList(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditWorkRecordsEntity vo) throws Exception {
//         验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        if(vo.getProjectId()==null) {
            BigDecimal projectId = tnp.getId();
            if(null == projectId) {
                return ResponseFormat.retParam(0,30003,null);
            }
            vo.setProjectId(projectId);
        }
        if(StringUtils.isBlank(loginStaff.getDeptIds()) && tnp.getZsstaffid()!=loginStaff.getStaffid()){
        	vo.setCreateUser(loginStaff.getStaffid().toString());
        }
        
        
        HashMap<String, Object> result = new HashMap<>();
        // 进行分页处理
        com.huabo.audit.util.PageInfo<TblYqnsAuditWorkRecordsEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsAuditWorkRecordsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> this.selectRecordsList(vo));

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
     * @param id
     * @return
     */
    @Override
    public JsonBean getRecordsById(String token, Long id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 获取单独一个审计工作记录
        TblYqnsAuditWorkRecordsEntity tblYqnsAuditWorkRecordsEntity = this.selectRecordsById(id);
        List<TblAttachment> attachments = tblYqnsAuditWorkRecordsMapper.selectAtt(new BigDecimal(tblYqnsAuditWorkRecordsEntity.getId()));
        tblYqnsAuditWorkRecordsEntity.setTblNoteAtts(attachments);
        if (tblYqnsAuditWorkRecordsEntity == null) {
            throw new ServiceException(400, 50001);
        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(tblYqnsAuditWorkRecordsEntity);

        // 进行分页处理
        return ResponseFormat.retParam(1, "查询成功", tblYqnsAuditWorkRecordsEntity);

    }

    /**
     * 审计工作记录-增加修改
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean  saveOrUpdate(String token,TblYqnsAuditWorkRecordsEntity vo,String attids) throws Exception {
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
        vo.setProjectId(projectId);
        JsonBean jsonBean ;
        try {
            Integer result = 0;
            if (null != vo.getId()) {
                vo.setUpdateUser(loginStaff.getStaffid().toString());
                result = this.updateEntity(vo);
                //附件
                this.deleteAttmentRelationByBizId(vo.getId());
                if (attids != null && !"".equals(attids)) {
                    String[] attId = attids.split(",");
                    for (String aid : attId) {
                        this.insertAttInfoAtt(vo.getId(), aid);
                    }
                }
            } else {
                vo.setId(this.getIdSequence());
                vo.setCreateUser(loginStaff.getStaffid().toString());
                result = this.insertEntity(vo);

                if (attids != null && !"".equals(attids)) {
                    String[] attId = attids.split(",");
                    for (String aid : attId) {
                        this.insertAttInfoAtt(vo.getId(), aid);
                    }
                }
            }


            jsonBean = result == 0 ? ResponseFormat.retParam(0, 1000) : ResponseFormat.retParam(1, 200,vo);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    private void deleteAttmentRelationByBizId(Long id) {
        tblYqnsAuditWorkRecordsMapper.deleteATT(id);
    }

    private void insertAttInfoAtt(Long id, String aid) {
         tblYqnsAuditWorkRecordsMapper.insertATT(id,aid);
    }

    /**
     * 删除数据(逻辑删除)
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
        // 判断是否删除成功
        if(id!=null) {
            Integer result = this.deleteEntity(id);
            return result == 0 ? ResponseFormat.retParam(0, 50001,"未删除成功") : ResponseFormat.retParam(1, 200);
        }
        return ResponseFormat.retParam(0,10002,null);
    }



    // ============== ============== ==============

    /**
     * 查询审计工作记录
     *
     * @param entity
     * @return
     */
    public List<TblYqnsAuditWorkRecordsEntity> selectRecordsList(TblYqnsAuditWorkRecordsEntity entity) {
        return tblYqnsAuditWorkRecordsMapper.selectRecordsList(entity);
    }

    /**
     * 获取单独一个审计记录
     *
     * @param id 
     * @return
     */
    private TblYqnsAuditWorkRecordsEntity selectRecordsById(Long id) {
        return tblYqnsAuditWorkRecordsMapper.selectRecordsById(id);
    }


    /**
     * 新增审计记录
     *
     * @param entity
     * @return
     */
    Integer insertEntity(TblYqnsAuditWorkRecordsEntity entity) {
        return tblYqnsAuditWorkRecordsMapper.insertEntity(entity);

    }


    /**
     * 修改审计记录
     *
     * @param entity
     * @return
     */
    Integer updateEntity(TblYqnsAuditWorkRecordsEntity entity) {
        return tblYqnsAuditWorkRecordsMapper.updateEntity(entity);

    }


    /**
     * 删除数据(逻辑删除)
     *
     * @param id 
     * @return
     */
    Integer deleteEntity(Long id) {
        return tblYqnsAuditWorkRecordsMapper.deleteEntity(id);
    }


    /**
     * 获取自增id
     * @return
     */
    Long getIdSequence() {
        return tblYqnsAuditWorkRecordsMapper.getIdSequence();
    }


	@Override
	public JsonBean getRecordsListByMyDraft(String token, Integer pageNumber, Integer pageSize,
			TblYqnsAuditWorkRecordsEntity vo) throws Exception {
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
	        vo.setProjectId(projectId);
	        vo.setStatus("6");
	        if(tnp.getZsstaffid()!=null && !tnp.getZsstaffid().toString().equals(loginStaff.getStaffid().toString())) {
	        	vo.setCreateUser(loginStaff.getStaffid().toString());
	        }
	        HashMap<String, Object> result = new HashMap<>();
	        // 进行分页处理
	        com.huabo.audit.util.PageInfo<TblYqnsAuditWorkRecordsEntity> info = new com.huabo.audit.util.PageInfo<>();
	        PageInfo<TblYqnsAuditWorkRecordsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
	                .doSelectPageInfo(() -> this.selectRecordsList(vo));
	        // 构建返回值条件
	        info.setCurrentPage(pageInfo.getPageNum());
	        info.setPageSize(pageInfo.getPageSize());
	        info.setTotalRecord((int) pageInfo.getTotal());
	        info.setTlist(pageInfo.getList());
	        result.put("pageInfo", info);

	        return ResponseFormat.retParam(1, "查询成功", result);
	}

}
