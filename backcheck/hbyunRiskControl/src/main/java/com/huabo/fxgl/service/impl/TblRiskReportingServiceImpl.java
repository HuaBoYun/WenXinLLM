package com.huabo.fxgl.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblRiskReportingMapper;
import com.huabo.fxgl.service.TblRiskReportingService;
import com.huabo.fxgl.util.PageResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TblRiskReportingServiceImpl extends ServiceImpl<TblRiskReportingMapper, TblRiskReportingEntity> implements TblRiskReportingService {

    @Resource
    private TblRiskReportingMapper tblRiskReportingMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private AttachmentMapper attachmentMapper;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 相关风险事件填报列表
     * smf 2024-10-31
     * @param token
     * @param pageNumber
     * @param pageSize
     */
    @Override
    public JsonBean queryRiskReportingAll(String token, Integer pageNumber, Integer pageSize,String entName) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }

            TblRiskReportingEntity queryParam=new TblRiskReportingEntity();
            queryParam.setEntName(entName);
//            QueryWrapper<TblRiskReportingEntity> queryWrapper = new QueryWrapper<TblRiskReportingEntity>();
//            if(StringUtils.isNotBlank(entName)){
//        	   queryWrapper.like("entName", "%"+entName+"%");
//            }
            //使用pagehelper自动分页。
            com.github.pagehelper.PageInfo<TblRiskReportingEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() ->  tblRiskReportingMapper.selectAllList(queryParam));
//            for (TblRiskReportingEntity reportingEntity : pageInfo.getList()) {
//                reportingEntity.setFillDeptName(this.queryTableFieldFalseName(reportingEntity).getFillDeptName());
//                reportingEntity.setFillUnitName(this.queryTableFieldFalseName(reportingEntity).getFillUnitName());
//                reportingEntity.setFillStaffName(this.queryTableFieldFalseName(reportingEntity).getFillStaffName());
//            }

            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
            PageResult<TblRiskReportingEntity> build = new PageResult<TblRiskReportingEntity>().build(pageInfo);

            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 相关风险事件填报新增
     * @param tblRiskReporting
     * @return
     */
    @Override
    public JsonBean insertOrUpdateReporting(TblRiskReportingEntity tblRiskReporting,String token,String attIds) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }

            if (tblRiskReporting.getId() != null ){
                tblRiskReportingMapper.updateById(tblRiskReporting);
            }else {
                tblRiskReporting.setFillCreateDate(new Date());
                tblRiskReporting.setFillStaffid(staffUtil.getStaffid());
                tblRiskReporting.setFillDeptid(staffUtil.getLinkDetp().getOrgid());
                tblRiskReporting.setFillUnit(staffUtil.getLinkOrg().getOrgid());
                tblRiskReporting.setState(new BigDecimal(0));
                tblRiskReporting.setId(RandomUtil.uuBigDecimalId());
                tblRiskReportingMapper.insert(tblRiskReporting);
            }
            if(attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblRiskReportingMapper.insertAttInfoReporting(tblRiskReporting.getId(),aid);
                }
            }
            TblRiskReportingEntity entity = tblRiskReportingMapper.selectById(tblRiskReporting.getId());
            entity.setFillDeptName(this.queryTableFieldFalseName(entity).getFillDeptName());
            entity.setFillUnitName(this.queryTableFieldFalseName(entity).getFillUnitName());
            entity.setFillStaffName(this.queryTableFieldFalseName(entity).getFillStaffName());
            hashMap.put("pageInfo",entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 相关风险事件填报详情
     * @param id
     * @param token
     * @return
     */
    @Override
    public JsonBean riskReportingDetails(String id, String token) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            TblRiskReportingEntity entity = tblRiskReportingMapper.selectById(id);
            if (!Objects.isNull(entity)){
                entity.setFillDeptName(this.queryTableFieldFalseName(entity).getFillDeptName());
                entity.setFillUnitName(this.queryTableFieldFalseName(entity).getFillUnitName());
                entity.setFillStaffName(this.queryTableFieldFalseName(entity).getFillStaffName());
            }
            hashMap.put("pageInfo",entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 相关风险事件填报删除
     * @param id
     * @return
     */
    @Override
    public void riskReportingDelete(String id) {
        try {
            tblRiskReportingMapper.deleteById(id);
            //删除所有附件
            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
            for (BigDecimal attId : attIdList) {
                this.deleteRealtionAttInfo(attId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteRealtionAttInfo(BigDecimal attId) throws Exception {
        try {
            Attachment att = this.attachmentMapper.selectEntityById(attId);
            this.tblRiskReportingMapper.deleteFileInfoByAttId(att.getAttid());
            this.attachmentMapper.deleteEntity(att.getAttid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 相关风险事件填报的附件
     * @param token
     * @param id
     * @return
     */
        @Override
    public JsonBean getRiskReportingAttInfo(String token, BigDecimal id) throws Exception {
            List<Attachment> attList = null;
            try {
                TblStaffUtil user = userProvider.get();
                if(user == null) {
                    return ResponseFormat.retParam(0,20006,null);
                }
                Map<String,Object> resultMap = new HashMap<String,Object>(0);

                attList = this.attachmentMapper.selectAttListByReportin(id);
                resultMap.put("pageInfo", attList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ResponseFormat.retParam(1,200,attList);
    }

    public TblRiskReportingEntity  queryTableFieldFalseName(TblRiskReportingEntity entity){
        try {
            final TblRiskReportingEntity riskReportingEntity = new TblRiskReportingEntity();
            Organization orgDeptName = organizationMapper.selectById(entity.getFillDeptid());
            Organization orgUnitName = organizationMapper.selectById(entity.getFillUnit());
            Staff staffName = staffMapper.selectById(entity.getFillStaffid());
            riskReportingEntity.setFillDeptName(orgDeptName.getOrgname());
            riskReportingEntity.setFillUnitName(orgUnitName.getOrgname());
            riskReportingEntity.setFillStaffName(staffName.getRealname());
            return riskReportingEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public List<TblRiskReportingEntity> exportRiskReport(String entname, String id) throws Exception {
		// TODO Auto-generated method stub
		List<TblRiskReportingEntity> entityList;
		  try {
			  String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				  id=id.substring(0,id.length()-1);
				  ids=id.split(",");
			  }
			  entityList=tblRiskReportingMapper.exportRiskReport(entname,ids);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		  return entityList;
	}
}
