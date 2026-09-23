package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.LeaveAudit3LMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjZjbMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJhglJhcgGLMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJsxmTzwcqkMapper;
import com.huabo.audit.service.TblYqnsJhglJhcgGLService;
import com.huabo.audit.util.PageInfoUtil;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿) 关联 】的数据库操作Service实现
 */
@Service
public class TblYqnsJhglJhcgGLServiceImpl extends ServiceImpl<TblYqnsJhglJhcgGLMapper, TblYqnsJhglJhcgGL>
        implements TblYqnsJhglJhcgGLService {
	
	@Resource
	private TblYqnsJhglJhcgGLMapper tblYqnsJhglJhcgGLMapper;
	
	@Resource
	private TblYqnsGcxmzjZjbMapper tblYqnsGcxmzjZjbMapper;
	
	@Resource
	private TblYqnsJsxmTzwcqkMapper tblYqnsJsxmTzwcqkMapper;
	
	@Resource
	private LeaveAudit3LMapper leaveAudit3LMapper;
	
	@Resource
	private EnginAuditProjectMapper enginAuditProjectMapper;
	    
	@Resource
	private  ImplementPlanMapper implementPlanMapper;
	
	 @Resource
	 TblYqnsGcxmzjMapper tblYqnsGcxmzjMapper;
	
	 @Resource
	    private UserProvider userProvider;
	
    /**
     * 新增修改批量处理
     *
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdateList(String token, long jhcgid, List<TblYqnsJhglJhcgGL> voList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != voList && voList.size() != 0  ){
            voList.stream().forEach(vo -> {
            	vo.setId(RandomUtil.uuBigDecimalId());
                vo.setJhcgid(jhcgid);
                vo.setGxsj(new Date());
                vo.setGxr(loginStaff.getStaffid().toString());
            });
            boolean ret = this.saveBatch(voList);
            
            for (TblYqnsJhglJhcgGL gl : voList) {
				if("23".equals(gl.getGltype())) {
					this.tblYqnsJhglJhcgGLMapper.insertSjdwlrsjSbGl(gl.getId(),jhcgid,gl.getRelaOrgId());
				}
				if("31".equals(gl.getGltype())) {
					this.tblYqnsJhglJhcgGLMapper.insertGcxmjshzGl(gl.getId(),jhcgid,gl.getRelaOrgName());
				}
				if("32".equals(gl.getGltype())) {
					this.tblYqnsJhglJhcgGLMapper.insertJsxmtzGl(gl.getId(),jhcgid,gl.getRelaOrgName());
				}
			}
            if (!ret) {
                return ResponseFormat.retParam(0, -1, Boolean.FALSE);
            }
        }
        
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



    /**
     * 通过jhcgid查询明细
     *
     * @param jhcgid
     * @return
     */
    @Override
    public List<TblYqnsJhglJhcgGL> findListByJHCGID(String token,String jhcgid) {
        // 进行数据获取和查询
        QueryWrapper<TblYqnsJhglJhcgGL> query = new QueryWrapper<TblYqnsJhglJhcgGL>();
        query.eq("JHCGID", jhcgid);
        List<TblYqnsJhglJhcgGL>  list= this.baseMapper.selectList(query);
        return list;
    }


    /**
     * 修改
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsJhglJhcgGL entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (null != entity.getId()) {
            entity.setGxsj(new Date());
            entity.setGxr(loginStaff.getStaffid().toString());
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteGL(String token, String id) throws Exception {
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
            this.tblYqnsJhglJhcgGLMapper.deleteGlRela(id);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



	@Override
	public JsonBean deleteGLByIds(String token, BigDecimal jhcgid, BigDecimal formid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        QueryWrapper<TblYqnsJhglJhcgGL> wrapper = new QueryWrapper<TblYqnsJhglJhcgGL>();
        wrapper.eq("JHCGID", jhcgid);
        wrapper.eq("GLID", formid);
		this.remove(wrapper);
		return ResponseFormat.retParam(1, 200, null);
	}



	@Override
	public JsonBean getGcxmjshzList(String token, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<TblYqnsJhglJhcgGL> list = this.tblYqnsJhglJhcgGLMapper.selectGcxmjshzPage();
		return ResponseFormat.retParam(1, 200, list);
	}



	@Override
	public JsonBean getJsxmtzList(String token, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<TblYqnsJhglJhcgGL> list = this.tblYqnsJhglJhcgGLMapper.selectJsxmtzPage();
		return ResponseFormat.retParam(1, 200, list);
	}



	@Override
	public JsonBean getSjdwlrsjSbList(String token, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        List<TblYqnsJhglJhcgGL> list =  this.tblYqnsJhglJhcgGLMapper.selectSjdwlrsjSbList();
		
		return ResponseFormat.retParam(1, 200, list);
	}



	@Override
	public JsonBean getGcxmjsListByhz(String token, BigDecimal id, String jsdw, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<TblYqnsGcxmzjZjb> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.tblYqnsGcxmzjZjbMapper.selectListByjhcgGlRela(id,jsdw));
        PageInfo<TblYqnsGcxmzjZjb> pageInfo = new PageInfoUtil<TblYqnsGcxmzjZjb>().parsePageInfo(page);
        return ResponseFormat.retParam(1, 200, pageInfo);
	}



	@Override
	public JsonBean getJsxmtzListByhz(String token, BigDecimal id, String tbdwName, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) { 
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<TblYqnsJsxmTzwcqk> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.tblYqnsJsxmTzwcqkMapper.selectListByjhcgGlId(id,tbdwName));
        PageInfo<TblYqnsJsxmTzwcqk> pageInfo = new PageInfoUtil<TblYqnsJsxmTzwcqk>().parsePageInfo(page);
        return ResponseFormat.retParam(1, 200, pageInfo);
	}



	@Override
	public JsonBean getSjdwlrsjSbList(String token, BigDecimal id, BigDecimal orgId, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //List<LeaveAudit3LEntity> list =   this.leaveAudit3LMapper.selectListByjhcgGlId(id,orgId);
        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.leaveAudit3LMapper.selectListByjhcgGlId(id,orgId));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1, 200, pageInfo);
	}


	
	@Override
	public JsonBean selectListByRwall(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsGcxmzjZjb> list =   this.tblYqnsGcxmzjZjbMapper.selectListByRwall(id);
        return ResponseFormat.retParam(1, 200, list); 
	}
	
	
	@Override
	public JsonBean selectListByRwcfll(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsGcxmzjZjb> list =   this.tblYqnsGcxmzjZjbMapper.selectListByRwallcf(id);
        return ResponseFormat.retParam(1, 200, list); 
	}
	
	@Override
	public  List<TblYqnsGcxmzjZjb>  selectListByRwalllist(String token, BigDecimal id) throws Exception {
        return  this.tblYqnsGcxmzjZjbMapper.selectListByRwall(id);
	}
	
	
	
	
	@Override
	public JsonBean selectListByIdall(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) { 
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsJsxmTzwcqk> list =   this.tblYqnsJsxmTzwcqkMapper.selectListByIdall(id);
        return ResponseFormat.retParam(1, 200, list);
	}
	
	
	@Override
	public JsonBean selectListByIdallcf(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) { 
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsJsxmTzwcqk> list =   this.tblYqnsJsxmTzwcqkMapper.selectListByIdallcf(id);
        return ResponseFormat.retParam(1, 200, list);
	}
	
	
	
	@Override
	public List<TblYqnsJsxmTzwcqk> selectListByIdalllist(String token, BigDecimal id) throws Exception {
		return  this.tblYqnsJsxmTzwcqkMapper.selectListByIdall(id);
	}
	
	
	
	
	@Override
	public JsonBean rwfpry(String token, String ids, String ryids,String rynames,String xmtype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if(xmtype!=null && xmtype.equals("31")) {
        	String[] idlist=ids.split(",");
        	for (String id : idlist) {
        		tblYqnsGcxmzjZjbMapper.rwfpry(id, ryids, rynames);
        		TblYqnsGcxmzjZjb zjb = tblYqnsGcxmzjZjbMapper.getByRwid(id);
        		if(ryids.split(",").length>1) {
        			String[] split = ryids.split(",");
        			String[] name = rynames.split(",");
        			for (int i = 0; i < split.length; i++) {
            			if(zjb!=null && zjb.getTblYqnsGcxmzj()!=null) {
            				if(i==0) {
            					TblYqnsGcxmzj zj=new TblYqnsGcxmzj();
                    			zj.setGcxmzjid(RandomUtil.uuBigDecimalId());  
                    			zj.setHtbh(zjb.getTblYqnsGcxmzj().getHtbh()+"-0"+(i+1));
                    			zj.setGcmc(zjb.getTblYqnsGcxmzj().getGcmc());
                    			zj.setJsdw(zjb.getTblYqnsGcxmzj().getJsdw());
                    			zj.setSgdw(zjb.getTblYqnsGcxmzj().getSgdw());
                    			zj.setLxr(zjb.getTblYqnsGcxmzj().getLxr());
                    			zj.setLxdh(zjb.getTblYqnsGcxmzj().getLxdh());
                    			zj.setEsscje(zjb.getTblYqnsGcxmzj().getEsscje());
                    			zj.setEsscjewy(zjb.getTblYqnsGcxmzj().getEsscjewy());
                    			zj.setParentid(zjb.getGcxmzjid());
                    			zj.setFzstatus(0);
                    			
                    			tblYqnsGcxmzjMapper.insert(zj);
                    			TblYqnsGcxmzjZjb newzjb = new TblYqnsGcxmzjZjb();
                    			newzjb.setGcxmzjzjbid(RandomUtil.uuBigDecimalId());
                    			newzjb.setParentid(zjb.getGcxmzjzjbid());
                    			newzjb.setCjr(zjb.getCjr());
                    			newzjb.setEdje(zjb.getEdje());
                    			newzjb.setNwb(zjb.getNwb());
                    			newzjb.setFzstatus(0);
                    			newzjb.setRwids(split[i]);
                    			newzjb.setRwnames(name[i]);
                    			newzjb.setGcxmzjid(zj.getGcxmzjid());
                    			tblYqnsGcxmzjZjbMapper.insert(newzjb);
            				}else {
            					TblYqnsGcxmzj zj=new TblYqnsGcxmzj();
                    			zj.setGcxmzjid(RandomUtil.uuBigDecimalId());  
                    			zj.setHtbh(zjb.getTblYqnsGcxmzj().getHtbh()+"-0"+(i+1));
                    			zj.setGcmc(zjb.getTblYqnsGcxmzj().getGcmc());
                    			zj.setJsdw(zjb.getTblYqnsGcxmzj().getJsdw());
                    			zj.setSgdw(zjb.getTblYqnsGcxmzj().getSgdw());
                    			zj.setLxr(zjb.getTblYqnsGcxmzj().getLxr());
                    			zj.setLxdh(zjb.getTblYqnsGcxmzj().getLxdh());
                    			zj.setEsscje(new BigDecimal(0));
                    			zj.setEsscjewy(new BigDecimal(0));
                    			zj.setParentid(zjb.getGcxmzjid());
                    			zj.setFzstatus(0);
                    			
                    			tblYqnsGcxmzjMapper.insert(zj);
                    			TblYqnsGcxmzjZjb newzjb = new TblYqnsGcxmzjZjb();
                    			newzjb.setGcxmzjzjbid(RandomUtil.uuBigDecimalId());
                    			newzjb.setParentid(zjb.getGcxmzjzjbid());
                    			newzjb.setCjr(zjb.getCjr());
                    			newzjb.setEdje("0");
                    			newzjb.setNwb(zjb.getNwb());
                    			newzjb.setFzstatus(0);
                    			newzjb.setRwids(split[i]);
                    			newzjb.setRwnames(name[i]);
                    			newzjb.setGcxmzjid(zj.getGcxmzjid());
                    			tblYqnsGcxmzjZjbMapper.insert(newzjb);
            				}
            				
            			}
            			
					}
        			TblYqnsGcxmzj gcxmzj = tblYqnsGcxmzjMapper.selectById(zjb.getGcxmzjid());
        			gcxmzj.setFzstatus(1);
        			tblYqnsGcxmzjMapper.updateById(gcxmzj);
        			
        			zjb.setFzstatus(1);
        			tblYqnsGcxmzjZjbMapper.updateById(zjb);
        			
        		}else {
        			
        			tblYqnsGcxmzjZjbMapper.deleteparentid(id);
        			
        			TblYqnsGcxmzj gcxmzj = tblYqnsGcxmzjMapper.selectById(zjb.getGcxmzjid());
        			gcxmzj.setFzstatus(0);
        			tblYqnsGcxmzjMapper.updateById(gcxmzj);
        			
        			zjb.setFzstatus(0);
        			tblYqnsGcxmzjZjbMapper.updateById(zjb);
        		}
			}
        	
        }else {
        	String[] idlist=ids.split(",");
        	for (String id : idlist) {
        		TblYqnsJsxmTzwcqk qk = tblYqnsJsxmTzwcqkMapper.selectById(id);
        		qk.setRwids(ryids);
        		qk.setRwnames(rynames);
//        		tblYqnsJsxmTzwcqkMapper.rwfpry(id, ryids, rynames);
        		if(ryids.split(",").length>1) {
        			String[] split = ryids.split(",");
        			String[] name = rynames.split(",");
        			for (int i = 0; i < split.length; i++) {
        				TblYqnsJsxmTzwcqk newwc =new TblYqnsJsxmTzwcqk();
        				newwc.setJsxmtzwcqkid(RandomUtil.uuLongId());
        				newwc.setParentid(qk.getJsxmtzwcqkid());
        				newwc.setRwids(split[i]);
        				newwc.setRwnames(name[i]);
        				newwc.setFzstatus(0);
        				newwc.setHtbh(qk.getHtbh()+"-0"+(i+1));
        				newwc.setJhwh(qk.getJhwh());
        				newwc.setGchfymc(qk.getGchfymc());
        				newwc.setSsdw(qk.getSsdw());
        				newwc.setHtje(qk.getHtje());
        				newwc.setTzjc(qk.getTzjc());
        				newwc.setPfgstzje(qk.getPfgstzje());
        				newwc.setJsje(qk.getJsje());
        				tblYqnsJsxmTzwcqkMapper.insert(newwc);
        				
        			}
        			
        			qk.setFzstatus(1);
        			tblYqnsJsxmTzwcqkMapper.updateById(qk);

        		}else {
        			tblYqnsJsxmTzwcqkMapper.deleteparentid(id);
        			qk.setFzstatus(0);
        			tblYqnsJsxmTzwcqkMapper.updateById(qk);
        		}
			}
        }
        return ResponseFormat.retParam(1, 200, null);
	}
	

	
	
	@Override
	public JsonBean selectListBymtRw(String token, BigDecimal id,BigDecimal projectId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }  
        if(projectId!=null) {
        	ImplementPlanEntity plan = implementPlanMapper.selectById(projectId.toString());
	        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", plan.getXmapbid()));
	        	 if(endin!=null) {
	        		 id=endin.getGljhxmid();
	        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
			         	xmqd.setGljhxmid(endin.getGljhxmid());
			         	xmqd.setPlanid(endin.getPlanid()); 
			         	xmqd.setPlanname(endin.getPlanname());
			         	xmqd.setGljhxmlx(endin.getGljhxmlx());
			         	xmqd.setZsstaffid(endin.getApproverId());
			         	xmqd.setZsname(endin.getApprover());
			         	xmqd.setSiteEndTime(endin.getXcendtime());
			         	xmqd.setXmname(endin.getName());
			         	xmqd.setAssistApprover(endin.getAssistApprover());
			         	xmqd.setAssistApproverId(endin.getAssistApproverId());
			         	xmqd.setFzzStafffId(endin.getFzzStafffId());
			         	xmqd.setFzzName(endin.getFzzName());
			         	xmqd.setSsorgname(endin.getExePhraseUnit());
			         	xmqd.setSsorgid(endin.getExePhraseUnitId());
			         	plan.setXmqd(xmqd);
	        	 }
	        	 
	        }
        }
        
        List<TblYqnsGcxmzjZjb> list =   this.tblYqnsGcxmzjZjbMapper.selectListBymyRw(id, loginStaff.getStaffid());
        return ResponseFormat.retParam(1, 200, list);
	}
	
	
	@Override
	public JsonBean selectListByIdmyrw(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        } 
        List<TblYqnsJsxmTzwcqk> list =   this.tblYqnsJsxmTzwcqkMapper.selectListByIdmyrw(id, loginStaff.getStaffid());
        return ResponseFormat.retParam(1, 200, list);
	}



	@Override
	public List<TblYqnsJhglJhcgGL> findListByJHCGIDByCaogao(String token, String jhcgid) throws Exception {
		 QueryWrapper<TblYqnsJhglJhcgGL> query = new QueryWrapper<TblYqnsJhglJhcgGL>();
	     query.eq("JHCGID", jhcgid);
	     query.notInSql("ID", "(SELECT RELAID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE RELAID IS NOT NULL)");
	     List<TblYqnsJhglJhcgGL>  list= this.baseMapper.selectList(query);
	     return list;
	}

	@Override
	public JsonBean getJhChuGHuizongList(String token, String jhcgid, String relaid,String glType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsJhglJhcgGL> list =  this.tblYqnsJhglJhcgGLMapper.selectSjdwlrsjSbListByJhcgId(jhcgid,relaid,glType);
		return ResponseFormat.retParam(1, 200, list);
	}
	
	
	@Override
	public JsonBean rwfpddry(String token, String ids, String ryids,String rynames,String xmtype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if(xmtype!=null && xmtype.equals("31")) {
        	String[] idlist=ids.split(",");
        	for (String id : idlist) {
        		tblYqnsGcxmzjZjbMapper.rwfpddry(id, ryids, rynames);
        	}
        	
        }else {
        	String[] idlist=ids.split(",");
        	for (String id : idlist) {
        		TblYqnsJsxmTzwcqk qk = tblYqnsJsxmTzwcqkMapper.selectById(id);
        		qk.setFpddryid(ryids);
        		qk.setFpddryname(rynames);
        		tblYqnsJsxmTzwcqkMapper.updateById(qk);
			}
        }
        return ResponseFormat.retParam(1, 200, null);
	}

}




