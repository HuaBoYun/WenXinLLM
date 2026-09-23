package com.huabo.audit.oracle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.dto.FundAuditProjectAttDto;
import com.huabo.audit.oracle.dto.TblYqnsFundAuditProjectDto;
import com.huabo.audit.oracle.entity.FundAuditProjectAttEntity;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundtb;
import com.huabo.audit.oracle.entity.TblYqnsOperate;
import com.huabo.audit.oracle.mapper.FundAuditProjectAttMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.LeaveAudit3LMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblYqnsFundtbMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.oracle.service.FundAuditProjectService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.impl.ReservePropertyService;
import com.huabo.audit.util.PageResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 00:07
 **/
@Service
@Slf4j
public class FundAuditProjectServiceImpl implements FundAuditProjectService {
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    @Resource
    private FundAuditProjectAttMapper fundAuditProjectAttMapper;
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblYqnsFundtbMapper tblYqnsFundtbMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Resource
	private LeaveAudit3LMapper leaveAudit3LMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveOrUpdate(TblYqnsFundAuditProjectDto param) {
        log.info("财务审计项目安排新增/更新入参:{}", JSONObject.toJSONString(param));
        if(param.getAuditUnit()!=null && param.getAuditUnitId()==null) {
        	Long orgid = tblOrganizationMapper.findNameByname(param.getAuditUnit());
        	param.setAuditUnitId(orgid);
        }
//        if(param.getQftype()!=null && param.getQftype()==1) {
//        	param.setXfstatus(2);
//        }
        JsonBean jsonBean = null;
        if (Objects.isNull(param.getId())) {
            TblYqnsFundAuditProjectEntity fundAuditProjectEntity = new TblYqnsFundAuditProjectEntity();
            BeanUtil.copyProperties(param, fundAuditProjectEntity);
//            fundAuditProjectEntity.setId(fundAuditProjectMapper.getNextSequenceValue());
            fundAuditProjectEntity.setId(RandomUtil.uuLongId());
            if(param.getIds()!=null && param.getIds().length()>0) {
            	String[] split = param.getIds().split(",");
            	fundAuditProjectEntity.setXmsl(split.length);
            }
            fundAuditProjectMapper.insert(fundAuditProjectEntity);
            if (CollectionUtils.isNotEmpty(param.getFundAuditProjectAttEntityList())) {
                for (FundAuditProjectAttEntity entity : param.getFundAuditProjectAttEntityList()) {
                    FundAuditProjectAttEntity fundAuditProjectAttEntity = new FundAuditProjectAttEntity();
                    BeanUtil.copyProperties(entity, fundAuditProjectAttEntity);
                    fundAuditProjectAttEntity.setId(fundAuditProjectAttMapper.getNextSequenceValue());
                    fundAuditProjectAttEntity.setFundAuditProjectId(fundAuditProjectEntity.getId());
                    fundAuditProjectAttMapper.insert(fundAuditProjectAttEntity);
                }
            }
            if(param.getIds()!=null && param.getIds().length()>0) {
            	String[] split = param.getIds().split(",");
            	fundAuditProjectAttMapper.deletegl(fundAuditProjectEntity.getId());
            	for (String glid : split) {
            		fundAuditProjectAttMapper.insertgl(fundAuditProjectEntity.getId(), glid);
				}
            }
//            jsonBean = ResponseFormat.retParam(1, "添加成功");
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("data", fundAuditProjectEntity);
            jsonBean= ResponseFormat.retParam(1,200,resultMap);
        } else {
            TblYqnsFundAuditProjectEntity fundAuditProjectEntity = new TblYqnsFundAuditProjectEntity();
            BeanUtil.copyProperties(param, fundAuditProjectEntity);
            if(param.getIds()!=null && param.getIds().length()>0) {
            	String[] split = param.getIds().split(",");
            	fundAuditProjectEntity.setXmsl(split.length);
            }
            fundAuditProjectMapper.updateById(fundAuditProjectEntity);
            fundAuditProjectAttMapper.delete(new QueryWrapper<FundAuditProjectAttEntity>()
                    .eq("FUND_AUDIT_PROJECT_ID", fundAuditProjectEntity.getId()));
            if (CollectionUtils.isNotEmpty(param.getFundAuditProjectAttEntityList())) {
                for (FundAuditProjectAttEntity entity : param.getFundAuditProjectAttEntityList()) {
                    FundAuditProjectAttEntity fundAuditProjectAttEntity = new FundAuditProjectAttEntity();
                    BeanUtil.copyProperties(entity, fundAuditProjectAttEntity);
                    fundAuditProjectAttEntity.setId(fundAuditProjectAttMapper.getNextSequenceValue());
                    fundAuditProjectAttEntity.setFundAuditProjectId(fundAuditProjectEntity.getId());
                    fundAuditProjectAttMapper.insert(fundAuditProjectAttEntity);
                }
            }
            if(param.getIds()!=null && param.getIds().length()>0) {
            	String[] split = param.getIds().split(",");
            	fundAuditProjectAttMapper.deletegl(fundAuditProjectEntity.getId());
            	for (String glid : split) {
            		fundAuditProjectAttMapper.insertgl(fundAuditProjectEntity.getId(), glid);
				}
            }
//            jsonBean = ResponseFormat.retParam(1, "添加成功");
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("data", fundAuditProjectEntity);
            jsonBean= ResponseFormat.retParam(1,200,resultMap);
        }
        return jsonBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean delete(Long id) {
        TblYqnsFundAuditProjectEntity tblYqnsFundAuditProjectEntity = new TblYqnsFundAuditProjectEntity();
        tblYqnsFundAuditProjectEntity.setDeleted(BigDecimal.ONE);
        tblYqnsFundAuditProjectEntity.setId(Long.valueOf(id));
        fundAuditProjectAttMapper.delete(new QueryWrapper<FundAuditProjectAttEntity>()
                .eq("FUND_AUDIT_PROJECT_ID", id));
        fundAuditProjectMapper.deleteone(id);
        return ResponseFormat.retParam(1, "删除成功", "");
    }

    @Override
    public JsonBean findById(Long id) {
        TblYqnsFundAuditProjectEntity tblYqnsFundAuditProjectEntity = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
                .eq("DELETED", 0)
                .eq("ID", id));
        if (tblYqnsFundAuditProjectEntity == null) {
            return ResponseFormat.retParam(0, "暂无此数据", null);
        }
        log.info("财务审计项目内容:{}", JSONObject.toJSONString(tblYqnsFundAuditProjectEntity));
//        TblYqnsFundAuditProjectDetailDto fundAuditProjectDetailDto = new TblYqnsFundAuditProjectDetailDto();
//        BeanUtil.copyProperties(tblYqnsFundAuditProjectEntity, fundAuditProjectDetailDto);
        List<FundAuditProjectAttEntity> fundAuditProjectAttEntityList = fundAuditProjectAttMapper.selectList(new QueryWrapper<FundAuditProjectAttEntity>()
                .eq("FUND_AUDIT_PROJECT_ID", id));
        if (CollectionUtils.isEmpty(fundAuditProjectAttEntityList)) {
            return ResponseFormat.retParam(1, "查询成功", tblYqnsFundAuditProjectEntity);
        }
        List<FundAuditProjectAttDto> fundAuditProjectAttDtoList = new ArrayList<>();
        for (FundAuditProjectAttEntity projectAttEntity : fundAuditProjectAttEntityList) {
            FundAuditProjectAttDto fundAuditProjectAttDto = new FundAuditProjectAttDto();
            BeanUtil.copyProperties(projectAttEntity, fundAuditProjectAttDto);
            try {
                TblAttachment tblAttachment = tblAttachmentMapper.selectEntityById(projectAttEntity.getAttachmentid());
                fundAuditProjectAttDto.setAttname(tblAttachment.getAttname());
                fundAuditProjectAttDto.setAttsize(tblAttachment.getAttsize());
                fundAuditProjectAttDto.setUploader(tblAttachment.getUploader());
                fundAuditProjectAttDto.setAttid(tblAttachment.getAttid());
            } catch (Exception e) {
                log.error("查询附件信息异常:", e);
            }
            fundAuditProjectAttDtoList.add(fundAuditProjectAttDto);
        }
        tblYqnsFundAuditProjectEntity.setFundAuditProjectAttDtoList(fundAuditProjectAttDtoList );
        return ResponseFormat.retParam(1, "查询成功", tblYqnsFundAuditProjectEntity);
    }

    @Override
    public JsonBean findList(Integer pageNumber, Integer pageSize, String name) {
        HashMap<String, Object> result = new HashMap<>();

        com.huabo.audit.util.PageInfo<TblYqnsFundAuditProjectEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsFundAuditProjectEntity> pageInfo;
        QueryWrapper<TblYqnsFundAuditProjectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETED", 0);
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("NAME", name);
        }
        pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> fundAuditProjectMapper.selectList(wrapper));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);

        return ResponseFormat.retParam(1, "查询成功", result);
    }
    
    
    
    
    @Override
	public JsonBean saveOrupdate(String token, TblYqnsFundtb jd, String glids,String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        if(jd!=null && jd.getTbid()!=null) {
        	TblYqnsFundtb engintb = tblYqnsFundtbMapper.selectById(jd.getTbid());
        	jd.setStatus(engintb.getStatus());
        	jd.setCreatedate(engintb.getCreatedate());
        	tblYqnsFundtbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsFundtbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	} 
        	
        	
        	
        	
        	List<TblYqnsFundAuditProjectEntity> tbidall = fundAuditProjectMapper.findByTbidall(jd.getTbid().toString());
  			if(tbidall!=null && tbidall.size()>0) {
  				for (TblYqnsFundAuditProjectEntity engin : tbidall) {
  					if(engin.getApproverId()==null) {
 						continue;
 					}
  					
  					Integer rw = tblYqnsOperateMapper.findrwUseridcount(engin.getId().toString(), engin.getApproverId().toString());
  					if(rw>0) {
  						continue;
  					}
  					if(engin.getGljhxmlx().equals("11")) {
  						TblYqnsOperate newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1497");
 						newoper.setSsmk("工作方案");
 						newoper.setRwmc("工作方案编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date());
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
  					}else if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("23")) { 
 						 
						List<LeaveAudit3LEntity> llist = leaveAudit3LMapper.getcwanbList(null,new BigDecimal((engin.getId())));
						if(llist!=null && llist.size()>0) {
							for (LeaveAudit3LEntity lls : llist) {
								rw = tblYqnsOperateMapper.findrwUseridcount(lls.getId().toString(), lls.getZsstaffid().toString());
			  					if(rw>0) {
			  						continue;
			  					}
								TblYqnsOperate newoper= new TblYqnsOperate();
								newoper.setSsmkid("1496"); 
								newoper.setSsmk("审前调查报告");
								newoper.setRwmc("审前调查报告编制");
								newoper.setFormid(lls.getId());
								newoper.setFormname(lls.getProjectname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
//							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid()); 
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(lls.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							    
							    
							    newoper= new TblYqnsOperate();
								newoper.setSsmkid("1363");
								newoper.setSsmk("实施方案");
								newoper.setRwmc("实施方案编制");
								newoper.setFormid(lls.getId());
								newoper.setFormname(lls.getProjectname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
//							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid()); 
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(lls.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}
						}
					
 					}else {
  						TblYqnsOperate newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1496");
      					newoper.setSsmk("审前调查报告");
      					newoper.setRwmc("审前调查报告编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      	//			    newoper.setParentid(tb.getOperid());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date());
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
      				    
      				    
      				    newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1363");
      					newoper.setSsmk("实施方案");
      					newoper.setRwmc("实施方案编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      	//			    newoper.setParentid(tb.getOperid());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date());
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
  					}
  					
  				}
  			}
        	
        	
        	
        }else {
        	jd.setTbid(RandomUtil.uuBigDecimalId());
        	jd.setStatus(0); 
        	 jd.setCreatedate(new Date());
        	tblYqnsFundtbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsFundtbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	}
        	List<TblYqnsFundAuditProjectEntity> tbidall = fundAuditProjectMapper.findByTbidall(jd.getTbid().toString());
        	if(tbidall!=null && tbidall.size()>0) {
  				for (TblYqnsFundAuditProjectEntity engin : tbidall) {
  					if(engin.getApproverId()==null) {
 						continue;
 					}
  					Integer rw = tblYqnsOperateMapper.findrwUseridcount(engin.getId().toString(), engin.getApproverId().toString());
  					if(rw>0) {
  						continue;
  					}
  					if(engin.getGljhxmlx().equals("11")) {
  						TblYqnsOperate newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1497");
 						newoper.setSsmk("工作方案");
 						newoper.setRwmc("工作方案编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date());
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
  					}else if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("23")) { 
  						 
						List<LeaveAudit3LEntity> llist = leaveAudit3LMapper.getcwanbList(null,new BigDecimal((engin.getId())));
						if(llist!=null && llist.size()>0) {
							for (LeaveAudit3LEntity lls : llist) {
								rw = tblYqnsOperateMapper.findrwUseridcount(lls.getId().toString(), lls.getZsstaffid().toString());
			  					if(rw>0) {
			  						continue;
			  					}
								TblYqnsOperate newoper= new TblYqnsOperate();
								newoper.setSsmkid("1496"); 
								newoper.setSsmk("审前调查报告");
								newoper.setRwmc("审前调查报告编制");
								newoper.setFormid(lls.getId());
								newoper.setFormname(lls.getProjectname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
//							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid()); 
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(lls.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							    
							    
							    newoper= new TblYqnsOperate();
								newoper.setSsmkid("1363");
								newoper.setSsmk("实施方案");
								newoper.setRwmc("实施方案编制");
								newoper.setFormid(lls.getId());
								newoper.setFormname(lls.getProjectname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
//							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid()); 
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(lls.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}
						}
					
  					}else {
  						TblYqnsOperate newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1496");
      					newoper.setSsmk("审前调查报告");
      					newoper.setRwmc("审前调查报告编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      	//			    newoper.setParentid(tb.getOperid());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date());
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
      				    
      				    
      				    newoper= new TblYqnsOperate();
      					newoper.setSsmkid("1363");
      					newoper.setSsmk("实施方案");
      					newoper.setRwmc("实施方案编制");
      					newoper.setFormid(new BigDecimal(engin.getId()));
      					newoper.setFormname(engin.getName());
      					newoper.setOperid(RandomUtil.uuBigDecimalId());
      	//			    newoper.setParentid(tb.getOperid());
      				    newoper.setStatus(0);
      				    newoper.setCreatestaffid(staff.getStaffid());
      				    newoper.setCreatename(staff.getRealname());
      				    newoper.setCreatedate(new Date()); 
      				    newoper.setRwuserid(engin.getApproverId().toString());
      				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
      				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
      				    tblYqnsOperateMapper.insert(newoper);
  					}
  					
  				}
  			}
             
        }
        if(attids != null && !"".equals(attids)) {
			String[] attId = attids.split(",");
			for (String aid : attId) {
				tblYqnsFundtbMapper.insertAttInfoForPlan(jd.getTbid(),aid);
			}
		}
       
        resultMap.put("data", jd);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        } 
        TblYqnsFundtb tb = tblYqnsFundtbMapper.selectById(jdid);
        
        QueryWrapper<TblYqnsFundAuditProjectEntity> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("xfksryids", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()).or().like("fpksryids", staff.getStaffid()));
        }
        queryWrapper.and(q -> q.inSql("id", "SELECT FUNID from TBL_YQNS_FUNDTB_GL where TBID="+jdid));
        //倒序 
        queryWrapper.orderByDesc(true, "CREATETIME");
        List<TblYqnsFundAuditProjectEntity> list = fundAuditProjectMapper.selectList(queryWrapper);
        
        tb.setList(list);
        if(staff.getRoleNames().contains("审理")) {
        	tb.setAnstatus(1);
        }else {
        	tb.setAnstatus(0);
        }
        

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(tb);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", tb);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblYqnsFundtb> queryWrapper = new QueryWrapper<>();
        if(vo.getYear()!=null && vo.getYear().length()>0) {
        	queryWrapper.like("ruleyear", vo.getYear());
        }
        
        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
        	queryWrapper.eq("tbid", vo.getTbrgid());
        } 
        
        if(vo.getName()!=null && vo.getName().length()>0) {
        	queryWrapper.like("tbrgname", vo.getName());
        }
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_FUNDTB_GL WHERE FUNID IN (select id from TBL_YQNS_FUND_AUDIT_PROJECT where (xfsmzryids like '%"+staff.getStaffid()+"%' or xfksryids like '%"+staff.getStaffid()+"%')) "));
        }
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsFundtb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsFundtbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsFundtb> build = new PageResult<TblYqnsFundtb>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsFundtbMapper.deleteglnr(jdid);
        tblYqnsFundtbMapper.deleteoneById(jdid);
        List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyByformidparentid(staff.getStaffid().toString(), "1466");
        List<TblYqnsOperate> list2 = tblYqnsOperateMapper.findbyByformidpare("'1496','1363','1497'");
        list.addAll(list2);
        if(list!=null && list.size()>0) {
        	 tblYqnsOperateMapper.deleteoneBymkId("'1465','1363','1497'");
	        List<TblYqnsFundAuditProjectEntity> tbidall = fundAuditProjectMapper.findByTbidall(jdid.toString());
			if(tbidall!=null && tbidall.size()>0) {
				for (TblYqnsFundAuditProjectEntity tblYqnsEnginAuditProjectEntity : tbidall) {
					 tblYqnsOperateMapper.deleteoneByUserId(staff.getStaffid().toString(), tblYqnsEnginAuditProjectEntity.getId().toString());
				}
			    
			}
        }
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	 @Override
	 public JsonBean xfzyksry(String token, String ids,String zyksryids,String names) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	String[] split = ids.split(",");
	     	for (String id : split) {
	     		 fundAuditProjectMapper.xfksry(id, zyksryids, names);
			}
	     	return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	
	 @Override
	 public JsonBean xfxmzry(String token, String ids,String zyksryids,String names) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	String[] split = ids.split(",");
	     	for (String id : split) {
	     		 fundAuditProjectMapper.xfxmzry(id, zyksryids, names);
			}
	     	tblYqnsOperateMapper.updateByxmner(tblYqnsOperateMapper.findXmidcountcw(split[0]),"1466");
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	 
	@Override
	public JsonBean findAlcwddfglList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		    if (staff == null) {
		    	return ResponseFormat.retParam(0, 20006, null);
		    }
		    QueryWrapper<TblYqnsFundtb> queryWrapper = new QueryWrapper<>();
		    if(vo.getYear()!=null && vo.getYear().length()>0) {
		    	queryWrapper.like("ruleyear", vo.getYear());
		    }
		    
		    if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
		    	queryWrapper.eq("tbid", vo.getTbrgid());
		    } 
		    
		    if(vo.getName()!=null && vo.getName().length()>0) {
	        	queryWrapper.like("tbrgname", vo.getName());
	        }
		    
		    if (StringUtils.isNotBlank(staff.getDeptIds())) {
		    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
		    }else {
		    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_FUNDTB_GL WHERE FUNID IN (select id from TBL_YQNS_FUND_AUDIT_PROJECT where (fpksryids like '%"+staff.getStaffid()+"%'  or xfsmzryids like '%"+staff.getStaffid()+"%' or xfksryids like '%"+staff.getStaffid()+"%')) "));
		    }
		    //倒序 
		    queryWrapper.orderByDesc(true, "CREATEDATE");
		    com.github.pagehelper.PageInfo<TblYqnsFundtb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsFundtbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsFundtb> build = new PageResult<TblYqnsFundtb>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());

		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	

	 @Override
	 public JsonBean fpzyksry(String token, String ids,String zyksryids,String names) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	String[] split = ids.split(",");
	     	for (String id : split) {
	     		 fundAuditProjectMapper.fpksry(id, zyksryids, names);
			}
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	

    
	 
	 @Override
	 public JsonBean xzfindList(String token,Integer pageNumber, Integer pageSize, String name) throws Exception {
		  TblStaffUtil staff = userProvider.get();
		     if (staff == null) {
		         return ResponseFormat.retParam(0, 20006, null);
		     }
	     HashMap<String, Object> result = new HashMap<>();

	     com.huabo.audit.util.PageInfo<TblYqnsFundAuditProjectEntity> info = new com.huabo.audit.util.PageInfo<>();
	     PageInfo<TblYqnsFundAuditProjectEntity> pageInfo;
	     QueryWrapper<TblYqnsFundAuditProjectEntity> wrapper = new QueryWrapper<>();
	     wrapper.eq("DELETED", 0);
	     if (StringUtils.isNotEmpty(name)) {
	         wrapper.like("NAME", name);
	     }
	     
	     if (StringUtils.isNotBlank(staff.getDeptIds())) {
	     	wrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	     }else {
	     	wrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("xfksryids", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()).or().like("fpksryids", staff.getStaffid()));
	     }
	     pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
	             .doSelectPageInfo(() -> fundAuditProjectMapper.selectList(wrapper));
	     // 构建返回值条件
	     info.setCurrentPage(pageInfo.getPageNum());
	     info.setPageSize(pageInfo.getPageSize());
	     info.setTotalRecord((int) pageInfo.getTotal());
	     info.setTlist(pageInfo.getList());
	     result.put("pageInfo", info);

	     return ResponseFormat.retParam(1, "查询成功", result);
	 }
	 
	 
	 
	 @Override
		public JsonBean getswsbList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
			TblStaffUtil staff = userProvider.get();
			    if (staff == null) {
			    	return ResponseFormat.retParam(0, 20006, null);
			    }
			    QueryWrapper<TblYqnsFundtb> queryWrapper = new QueryWrapper<>();
			    if(vo.getYear()!=null && vo.getYear().length()>0) {
			    	queryWrapper.like("ruleyear", vo.getYear());
			    }
			    
			    if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
			    	queryWrapper.eq("tbid", vo.getTbrgid());
			    } 
			    
			    if (StringUtils.isNotBlank(staff.getDeptIds())) {
			    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
			    }else {
			    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_FUNDTB_GL WHERE FUNID IN (select id from TBL_YQNS_FUND_AUDIT_PROJECT where  XFSMZRYIDS like '%"+staff.getStaffid()+"%') "));
			    }
			    //倒序 
			    queryWrapper.orderByDesc(true, "CREATEDATE");
			    com.github.pagehelper.PageInfo<TblYqnsFundtb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
					.doSelectPageInfo(() -> tblYqnsFundtbMapper.selectList(queryWrapper));
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			PageResult<TblYqnsFundtb> build = new PageResult<TblYqnsFundtb>().build(info);
			resultMap.put("pageInfo", build);
			return ResponseFormat.retParam(1,200,resultMap);
		}
	 
	 
	 @Override
	 public JsonBean fpkzsrys(String token, String ids,String zyksryids,String names) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	String[] split = ids.split(",");
	     	for (String id : split) {
	     		 fundAuditProjectMapper.fpkzsrys(id, zyksryids, names);
			}
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }

	@Override
	public JsonBean outFileList(String token, String tbid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
		List<TblAttachment> list = tblAttachmentMapper.selectAttListBytbid(tbid);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteattid(String token, String attid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     tblYqnsFundtbMapper.deleteFileInfoByAttId(attid);
	     tblYqnsFundtbMapper.deleteEntity(attid);
	     return ResponseFormat.retParam(1, 200, null);
	}
	
	
	 @Override
	 public JsonBean xmzsb(String token, String ids) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	 fundAuditProjectMapper.xmzsb(ids);
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	 
	 @Override
	 public JsonBean xmzsbth(String token, String ids) throws Exception {
	     TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	 fundAuditProjectMapper.xmzsbth(ids);
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	 @Override
		public List<TblYqnsFundAuditProjectEntity>  findbytbidgetlist(String token, String tbid, List<String> idList) throws Exception {
		 TblStaffUtil staff = userProvider.get();
		 QueryWrapper<TblYqnsFundAuditProjectEntity> queryWrapper = new QueryWrapper<>();
	        
	        if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("xfksryids", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()).or().like("fpksryids", staff.getStaffid()));
	        }
			if (CollectionUtils.isNotEmpty(idList)) {
				queryWrapper.and(q -> q.in("id", idList));
			} else {
				queryWrapper.and(q -> q.inSql("id", "SELECT FUNID from TBL_YQNS_FUNDTB_GL where TBID="+tbid));
			}
	        //倒序 
	        queryWrapper.orderByDesc(true, "CREATETIME");
	        return fundAuditProjectMapper.selectList(queryWrapper);
			
		}

	@Override
	public JsonBean fpslkry(String ids, String fpslkryid, String fpslkryname) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
	     if (loginStaff == null) {
	         return ResponseFormat.retParam(0, 20006, null);
	     }
	     if(ids!=null&& ids.length()>0) {
	     	String[] split = ids.split(",");
	     	for (String id : split) {
	     		 fundAuditProjectMapper.fpslkry(id, fpslkryid, fpslkryname);
			}
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	}
	 
	 
	 
	 
}
