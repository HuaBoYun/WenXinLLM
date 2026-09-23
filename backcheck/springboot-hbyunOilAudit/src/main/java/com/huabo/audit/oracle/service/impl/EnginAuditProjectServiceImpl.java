package com.huabo.audit.oracle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.dto.TblYqnsEnginAuditProjectDto;
import com.huabo.audit.oracle.dto.TblYqnsEnginProjectAttDto;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginProjectAttEntity;
import com.huabo.audit.oracle.entity.TblYqnsEngintb;
import com.huabo.audit.oracle.entity.TblYqnsOperate;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.EnginProjectAttMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblYqnsEngintbMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.oracle.service.EnginAuditProjectService;
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
 * @create: 2023-10-14 10:41
 **/
@Service
@Slf4j
public class EnginAuditProjectServiceImpl implements EnginAuditProjectService {

    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;
    @Resource
    private EnginProjectAttMapper enginProjectAttMapper;
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblYqnsEngintbMapper tblYqnsEngintbMapper;
    
    @Resource  
    private TblOrganizationMapper tblOrganizationMapper;
	@Resource
	private ReservePropertyService reservePropertyService;
	
	
	@Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
	
	@Resource
    private UserProvider userProvider;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveOrUpdate(TblYqnsEnginAuditProjectDto param) {
        log.info("工程审计项目新增/修改入参:{}", JSONObject.toJSONString(param));
        if(param.getAuditUnit()!=null && param.getAuditUnitId()==null) {
        	Long orgid = tblOrganizationMapper.findNameByname(param.getAuditUnit());
        	param.setAuditUnitId(orgid);
        }
        JsonBean jsonBean = null;
        if (Objects.isNull(param.getId())) {
            TblYqnsEnginAuditProjectEntity enginAuditProjectEntity = new TblYqnsEnginAuditProjectEntity();
            BeanUtil.copyProperties(param, enginAuditProjectEntity);
//            enginAuditProjectEntity.setId(enginAuditProjectMapper.getNextSequenceValue());
            enginAuditProjectEntity.setId(RandomUtil.uuLongId());
            enginAuditProjectMapper.insert(enginAuditProjectEntity);
            if (CollectionUtils.isNotEmpty(param.getEnginProjectAttEntityList())) {
                for (TblYqnsEnginProjectAttEntity enginProjectAttEntity : param.getEnginProjectAttEntityList()) {
                    enginProjectAttEntity.setId(enginProjectAttMapper.getNextSequenceValue());
                    enginProjectAttEntity.setEnginAuditProjectId(enginAuditProjectEntity.getId());
                    enginProjectAttMapper.insert(enginProjectAttEntity);
                }
            }
//            jsonBean = ResponseFormat.retParam(1, "添加成功");
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("data", enginAuditProjectEntity);
            jsonBean= ResponseFormat.retParam(1,200,resultMap);
        } else {
            TblYqnsEnginAuditProjectEntity enginAuditProjectEntity = new TblYqnsEnginAuditProjectEntity();
            BeanUtil.copyProperties(param, enginAuditProjectEntity);
            enginAuditProjectMapper.updateById(enginAuditProjectEntity);
            enginProjectAttMapper.delete(new QueryWrapper<TblYqnsEnginProjectAttEntity>()
                    .eq("ENGIN_AUDIT_PROJECT_ID", enginAuditProjectEntity.getId()));
            if (CollectionUtils.isNotEmpty(param.getEnginProjectAttEntityList())) {
                for (TblYqnsEnginProjectAttEntity enginProjectAttEntity : param.getEnginProjectAttEntityList()) {
                    enginProjectAttEntity.setId(enginProjectAttMapper.getNextSequenceValue());
                    enginProjectAttEntity.setEnginAuditProjectId(enginAuditProjectEntity.getId());
                    enginProjectAttMapper.insert(enginProjectAttEntity);
                }
            }
//            jsonBean = ResponseFormat.retParam(1, "成功");
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("data", enginAuditProjectEntity);
            jsonBean= ResponseFormat.retParam(1,200,resultMap);
        }
        return jsonBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean delete(Long id) {
        TblYqnsEnginAuditProjectEntity enginAuditProjectEntity = new TblYqnsEnginAuditProjectEntity();
        enginAuditProjectEntity.setDeleted(BigDecimal.ONE);
        enginAuditProjectEntity.setId(Long.valueOf(id));
        enginProjectAttMapper.delete(new QueryWrapper<TblYqnsEnginProjectAttEntity>()
                .eq("ENGIN_AUDIT_PROJECT_ID", id));
        enginAuditProjectMapper.deleteone(id);
        return ResponseFormat.retParam(1, "删除成功", "");
    }

    @Override
    public JsonBean findById(Long id) {
        TblYqnsEnginAuditProjectEntity enginAuditProjectEntity = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
                .eq("DELETED", 0)
                .eq("ID", id));
        if (enginAuditProjectEntity == null) {
            return ResponseFormat.retParam(0, "暂无此数据", null);
        }
        log.info("工程审计项目内容:{}", JSONObject.toJSONString(enginAuditProjectEntity));
//        TblYqnsEnginAuditProjectDetailDto enginAuditProjectDetailDto = new TblYqnsEnginAuditProjectDetailDto();
//        BeanUtil.copyProperties(enginAuditProjectEntity, enginAuditProjectDetailDto);
        List<TblYqnsEnginProjectAttEntity> enginProjectAttEntityList = enginProjectAttMapper.selectList(new QueryWrapper<TblYqnsEnginProjectAttEntity>()
                .eq("ENGIN_AUDIT_PROJECT_ID", id));
        if (CollectionUtils.isEmpty(enginProjectAttEntityList)) {
            return ResponseFormat.retParam(1, "查询成功", enginAuditProjectEntity);
        }
        List<TblYqnsEnginProjectAttDto> enginProjectAttDtoList = new ArrayList<>();
        for (TblYqnsEnginProjectAttEntity enginProjectAttEntity : enginProjectAttEntityList) {
            TblYqnsEnginProjectAttDto enginProjectAttDto = new TblYqnsEnginProjectAttDto();
            BeanUtil.copyProperties(enginProjectAttEntity, enginProjectAttDto);
            try {
                TblAttachment tblAttachment = tblAttachmentMapper.selectEntityById(enginProjectAttEntity.getAttachmentId());
                enginProjectAttDto.setAttname(tblAttachment.getAttname());
                enginProjectAttDto.setAttsize(tblAttachment.getAttsize());
                enginProjectAttDto.setUploader(tblAttachment.getUploader());
                enginProjectAttDto.setAttid(tblAttachment.getAttid());
            } catch (Exception e) {
                log.error("查询附件信息异常:", e);
            }
            enginProjectAttDtoList.add(enginProjectAttDto);
        }
        enginAuditProjectEntity.setEnginProjectAttDtoList(enginProjectAttDtoList);
        return ResponseFormat.retParam(1, "查询成功", enginAuditProjectEntity);
    }

    @Override
    public JsonBean findList(Integer pageNumber, Integer pageSize, String name) {
        HashMap<String, Object> result = new HashMap<>();

        com.huabo.audit.util.PageInfo<TblYqnsEnginAuditProjectEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsEnginAuditProjectEntity> pageInfo;
        QueryWrapper<TblYqnsEnginAuditProjectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETED", 0);
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("NAME", name);
        }
        pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
                .doSelectPageInfo(() -> enginAuditProjectMapper.selectList(wrapper));
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);

        return ResponseFormat.retParam(1, "查询成功", result);
    }
    
    

    
    @Override
	public JsonBean saveOrupdate(String token, TblYqnsEngintb jd, String glids,String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatedate(new Date());
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        if(jd!=null && jd.getTbid()!=null) {
        	TblYqnsEngintb engintb = tblYqnsEngintbMapper.selectById(jd.getTbid());
        	jd.setStatus(engintb.getStatus());
        	jd.setCreatedate(engintb.getCreatedate());
        	tblYqnsEngintbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsEngintbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	} 
        	
        	
        	 List<TblYqnsEnginAuditProjectEntity> tbidall = enginAuditProjectMapper.findByTbidall(jd.getTbid().toString());
 			if(tbidall!=null && tbidall.size()>0) {
 				for (TblYqnsEnginAuditProjectEntity engin : tbidall) {
 					if(engin.getApproverId()==null) {
 						continue;
 					}
 					Integer rw = tblYqnsOperateMapper.findrwUseridcount(engin.getId().toString(), engin.getApproverId().toString());
  					if(rw>0) {
  						continue;
  					}
 					if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("12")) {
 						TblYqnsOperate newoper= new TblYqnsOperate();
 						newoper.setSsmkid("1497");
							newoper.setSsmk("工作方案");
							newoper.setRwmc("工作方案编制");
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
 						
						}else {
							TblYqnsOperate newoper= new TblYqnsOperate();
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
        	tblYqnsEngintbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsEngintbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	}
        	
        	List<TblYqnsOperate> list1 = tblYqnsOperateMapper.findbyByformid(staff.getStaffid().toString(), "1465");
            if(list1==null || list1.size()<=0 ) {
    	        List<TblYqnsEnginAuditProjectEntity> tbidall = enginAuditProjectMapper.findByTbidall(jd.getTbid().toString());
    			if(tbidall!=null && tbidall.size()>0) {
    				for (TblYqnsEnginAuditProjectEntity engin : tbidall) {
    					if(engin.getApproverId()==null) {
    						continue;
    					}
    					if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("12")) {
    						TblYqnsOperate newoper= new TblYqnsOperate();
    						newoper.setSsmkid("1497");
							newoper.setSsmk("工作方案");
							newoper.setRwmc("工作方案编制");
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
    						
						}else {
							TblYqnsOperate newoper= new TblYqnsOperate();
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
        }
        if(attids != null && !"".equals(attids)) {
			String[] attId = attids.split(",");
			for (String aid : attId) {
				tblYqnsEngintbMapper.insertAttInfoForPlan(jd.getTbid(),aid);
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
        TblYqnsEngintb tb = tblYqnsEngintbMapper.selectById(jdid);
        
        QueryWrapper<TblYqnsEnginAuditProjectEntity> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("XFKSRYIDS", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()));
        }
        queryWrapper.and(q -> q.inSql("id", "SELECT ENGID from TBL_YQNS_ENGINTB_GL where TBID="+jdid));
        //倒序 
        queryWrapper.orderByDesc(true, "CREATETIME");
        List<TblYqnsEnginAuditProjectEntity> list = enginAuditProjectMapper.selectList(queryWrapper);
        
        tb.setList(list);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(tb);
		  if(staff.getRoleNames().contains("审理")) {
	        	tb.setAnstatus(1);
	        }else {
	        	tb.setAnstatus(0);
	        }
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
        QueryWrapper<TblYqnsEngintb> queryWrapper = new QueryWrapper<>();
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
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_ENGINTB_GL WHERE ENGID IN (select id from TBL_YQNS_ENGIN_AUDIT_PROJECT where (xfsmzryids like '%"+staff.getStaffid()+"%' or xfksryids like '%"+staff.getStaffid()+"%')) "));
        }
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsEngintb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsEngintbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsEngintb> build = new PageResult<TblYqnsEngintb>().build(info);

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
        tblYqnsEngintbMapper.deleteglnr(jdid); 
        tblYqnsEngintbMapper.deleteoneById(jdid);
        List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyByformidparentid(staff.getStaffid().toString(), "1465");
        List<TblYqnsOperate> list2 = tblYqnsOperateMapper.findbyByformidpare("'1496','1363','1497'");
        list.addAll(list2);
        if(list!=null && list.size()>0) {
        	 tblYqnsOperateMapper.deleteoneBymkId("'1465','1363','1497'");
	        List<TblYqnsEnginAuditProjectEntity> tbidall = enginAuditProjectMapper.findByTbidall(jdid.toString());
			if(tbidall!=null && tbidall.size()>0) {
				for (TblYqnsEnginAuditProjectEntity tblYqnsEnginAuditProjectEntity : tbidall) {
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
	     		enginAuditProjectMapper.xfksry(id, zyksryids, names);
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
	     		enginAuditProjectMapper.xfxmzry(id, zyksryids, names);
			}
	     	
	     	;
	     	tblYqnsOperateMapper.updateByxmner(tblYqnsOperateMapper.findXmidcount(split[0]),"1465");
	     	return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	     
	 }

	 @Override
		public JsonBean findAllddfgList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
	        QueryWrapper<TblYqnsEngintb> queryWrapper = new QueryWrapper<>();
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
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_ENGINTB_GL WHERE ENGID IN (select id from TBL_YQNS_ENGIN_AUDIT_PROJECT where (fpksryids like '%"+staff.getStaffid()+"%'  or xfsmzryids like '%"+staff.getStaffid()+"%' or xfksryids like '%"+staff.getStaffid()+"%')) "));
	        }
	        //倒序 
	        queryWrapper.orderByDesc(true, "CREATEDATE");
	        com.github.pagehelper.PageInfo<TblYqnsEngintb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
					.doSelectPageInfo(() -> tblYqnsEngintbMapper.selectList(queryWrapper)); 
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			PageResult<TblYqnsEngintb> build = new PageResult<TblYqnsEngintb>().build(info);

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
	     		enginAuditProjectMapper.fpksry(id, zyksryids, names);
			}
	     	 return ResponseFormat.retParam(1, 200, null);
	     }else {
	    	 return ResponseFormat.retParam(0, "未选择数据", null);
	     }
	     
	    
	 }
	 
	 
	 @Override
	    public JsonBean xzfindList(String token,Integer pageNumber, Integer pageSize, String name) throws Exception{
	        HashMap<String, Object> result = new HashMap<>();
	        TblStaffUtil staff = userProvider.get();
		     if (staff == null) {
		         return ResponseFormat.retParam(0, 20006, null);
		     }
	        com.huabo.audit.util.PageInfo<TblYqnsEnginAuditProjectEntity> info = new com.huabo.audit.util.PageInfo<>();
	        PageInfo<TblYqnsEnginAuditProjectEntity> pageInfo;
	        QueryWrapper<TblYqnsEnginAuditProjectEntity> wrapper = new QueryWrapper<>();
	        wrapper.eq("DELETED", 0);
	        if (StringUtils.isNotEmpty(name)) {
	            wrapper.like("NAME", name);
	        }
	        if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	wrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	wrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("XFKSRYIDS", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()));
	        }
	        pageInfo = PageMethod.startPage(pageNumber, pageSize, "id desc ")
	                .doSelectPageInfo(() -> enginAuditProjectMapper.selectList(wrapper));
	        // 构建返回值条件
	        info.setCurrentPage(pageInfo.getPageNum());
	        info.setPageSize(pageInfo.getPageSize());
	        info.setTotalRecord((int) pageInfo.getTotal());
	        info.setTlist(pageInfo.getList());
	        result.put("pageInfo", info);

	        return ResponseFormat.retParam(1, "查询成功", result);
	    }
	    
	 
	 
	 
	 @Override
		public JsonBean getgcsbList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
	        QueryWrapper<TblYqnsEngintb> queryWrapper = new QueryWrapper<>();
	        if(vo.getYear()!=null && vo.getYear().length()>0) {
	        	queryWrapper.like("ruleyear", vo.getYear());
	        }
	        
	        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
	        	queryWrapper.eq("tbid", vo.getTbrgid());
	        } 
	        
	        if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("tbid", "SELECT DISTINCT tbid FROM TBL_YQNS_ENGINTB_GL WHERE ENGID IN (select id from TBL_YQNS_ENGIN_AUDIT_PROJECT where  XFSMZRYIDS like '%"+staff.getStaffid()+"%') "));
	        }
	        //倒序 
	        queryWrapper.orderByDesc(true, "CREATEDATE");
	        com.github.pagehelper.PageInfo<TblYqnsEngintb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
					.doSelectPageInfo(() -> tblYqnsEngintbMapper.selectList(queryWrapper));
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			PageResult<TblYqnsEngintb> build = new PageResult<TblYqnsEngintb>().build(info);
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
	     		enginAuditProjectMapper.fpkzsrys(id, zyksryids, names);
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
			List<TblAttachment> list = tblAttachmentMapper.selectAttListBygctbid(tbid);
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
		     tblYqnsEngintbMapper.deleteFileInfoByAttId(attid);
		     tblYqnsEngintbMapper.deleteEntity(attid);
		     return ResponseFormat.retParam(1, 200, null);
		}
	 
		 @Override
		 public JsonBean xmzsb(String token, String ids) throws Exception {
		     TblStaffUtil loginStaff = userProvider.get();
		     if (loginStaff == null) {
		         return ResponseFormat.retParam(0, 20006, null);
		     }
		     if(ids!=null&& ids.length()>0) {
		    	 enginAuditProjectMapper.xmzsb(ids);
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
		    	 enginAuditProjectMapper.xmzsbth(ids);
		     	 return ResponseFormat.retParam(1, 200, null);
		     }else {
		    	 return ResponseFormat.retParam(0, "未选择数据", null);
		     }
		     
		    
		 }
		 
		 
		 
		 @Override
			public List<TblYqnsEnginAuditProjectEntity>  findbytbidgetlist(String token, String tbid, List<String> idList) throws Exception {
			 TblStaffUtil staff = userProvider.get();
			 QueryWrapper<TblYqnsEnginAuditProjectEntity> queryWrapper = new QueryWrapper<>();
		        
		        if (StringUtils.isNotBlank(staff.getDeptIds())) {
		        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().inSql("CREATERID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
		        }else {
		        	queryWrapper.and(q -> q.eq("CREATERID", staff.getStaffid()).or().like("XFKSRYIDS", staff.getStaffid()).or().like("XFSMZRYIDS", staff.getStaffid()));
		        }

				if (CollectionUtils.isNotEmpty(idList)) {
					queryWrapper.and(q->q.in("id", idList));
				} else {
					queryWrapper.and(q -> q.inSql("id", "SELECT ENGID from TBL_YQNS_ENGINTB_GL where TBID="+tbid));
				}
		        //倒序 
		        queryWrapper.orderByDesc(true, "CREATETIME");
		        return enginAuditProjectMapper.selectList(queryWrapper);
				
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
		     		enginAuditProjectMapper.fpslkry(id, fpslkryid, fpslkryname);
				}
		     	 return ResponseFormat.retParam(1, 200, null);
		     }else {
		    	 return ResponseFormat.retParam(0, "未选择数据", null);
		     }
		}
		
}
