package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJhglJhMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOtherAuditMapper;
import com.huabo.audit.service.*;
import com.huabo.audit.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Wangys
 * @description 针对表【TBL_YQNS_JHGL_JH(计划管理计划)】的数据库操作Service实现
 */
@Service
public class TblYqnsJhglJhServiceImpl extends ServiceImpl<TblYqnsJhglJhMapper, TblYqnsJhglJh>
        implements TblYqnsJhglJhService {

    TblStaffUtil loginStaff;

    @Resource
    TblYqnsJhglJhGLService tblYqnsJhglJhGLService;

    /**
     * 专项审计_生产经营管理专项审计
     */
//    @Resource
//    LeaveAudit2LService leaveAudit2LService;

    /**
     * 专项审计_基建与投资专项审计
     */
//    @Resource
//    LeaveAudit2LService leaveAudit2LService;

    /**
     * 二级单位及所属成员单位离任经济责任审计
     */
    @Resource
    LeaveAudit2LService leaveAudit2LService;
    /**
     * 二级单位任中经济责任审计
     */
    @Resource
    AuditSuggestion2LService auditSuggestion2LService;
    /**
     * 二级单位及成员单位离任审计
     */
    @Resource
    LeaveAudit3LService leaveAudit3LService;

    /**
     * 工程建设项目审计_工程建设项目结算审计
     */
    @Resource
    TblYqnsJsxmJbqkService tblYqnsJsxmJbqkService;

    /**
     * 工程建设项目审计_工程建设项目竣工决算审计
     */
    @Resource
    TblYqnsGcxmjgYsjhService tblYqnsGcxmjgYsjhService;
    
    @Resource
    private TblYqnsOtherAuditMapper tblYqnsOtherAuditMapper;
    
    @Resource
    ProjectProposalEvaluationService projectProposalEvaluationService;
    
    @Autowired
    private InterimAuditDetailService interimAuditDetailService;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;
    
    @Autowired
	 private TblStaffMapper tblStaffMapper;
    
    @Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;

    @Resource
    TblYqnsJhglJhcgGLService tblYqnsJhglJhcgGLService;

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        /*PageInfo<TblYqnsJhglJh> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsJhglJh> wrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsJhglJh::getCjr, vo.getCjr());
        }
        if (vo.getCjsj() != null) {
            wrapper.lambda().ge(TblYqnsJhglJh::getCjsj, vo.getCjsj());
        }
        
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getJhmc())) {
            wrapper.lambda().like(TblYqnsJhglJh::getJhmc, vo.getJhmc());
        }
        if (StringUtils.isNotBlank(vo.getSjxmmc())) {
            wrapper.lambda().like(TblYqnsJhglJh::getSjxmmc, vo.getSjxmmc());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate() != null) {
            wrapper.lambda().ge(TblYqnsJhglJh::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJhglJh::getEndDate, vo.getEndDate());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJhglJh> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJhglJh> page = new PageResult<TblYqnsJhglJh>().build(pageInfo);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(page.getTlist());
        return ResponseFormat.retParam(1, 200, page);
    }

    /**
     * 通过审计类型获取相对应的项目
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    public JsonBean findListToSsxmlx(String token, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;

        //查询条件
        QueryWrapper<TblYqnsJhglJh> wrapper = new QueryWrapper<>();
        if (vo.getSsxmlx() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJhglJh::getSsxmlx, vo.getSsxmlx());
        }
        List<TblYqnsJhglJh> tblYqnsJhglJhList = baseMapper.selectList(wrapper);

        List<TblYqnsJhglJhprojectName> tblYqnsJhglJhprojectNameList = new ArrayList<>();
        for (int i = 0; i < tblYqnsJhglJhList.size(); i++) {
            TblYqnsJhglJh tblYqnsJhglJh = tblYqnsJhglJhList.get(i);
            detail(token, tblYqnsJhglJh);

            // 21 二级单位及所属成员单位离任经济责任审计
            if (tblYqnsJhglJh.getLeaveAudit2LEntityList() != null && tblYqnsJhglJh.getLeaveAudit2LEntityList().size() > 0) {
                tblYqnsJhglJh.getLeaveAudit2LEntityList().stream().forEach(t -> {
                    TblYqnsJhglJhprojectName tblYqnsJhglJhprojectName = new TblYqnsJhglJhprojectName();
                    tblYqnsJhglJhprojectName.setGltype("21");
                    tblYqnsJhglJhprojectName.setProjectName(t.getProjectName());
                    tblYqnsJhglJhprojectName.setJhid(tblYqnsJhglJh.getJhid().longValue());
                    tblYqnsJhglJhprojectName.setGlId(t.getId().toString());
                    tblYqnsJhglJhprojectNameList.add(tblYqnsJhglJhprojectName);
                });
            }
            // 22 二级单位任中经济责任审计
            if (tblYqnsJhglJh.getAuditSuggestion2LEntityList() != null && tblYqnsJhglJh.getAuditSuggestion2LEntityList().size() > 0) {
                tblYqnsJhglJh.getAuditSuggestion2LEntityList().stream().forEach(t -> {
                    TblYqnsJhglJhprojectName tblYqnsJhglJhprojectName = new TblYqnsJhglJhprojectName();
//                    tblYqnsJhglJhprojectName.setProjectName(t.getName());

                    tblYqnsJhglJhprojectName.setGltype("22");
                    tblYqnsJhglJhprojectName.setJhid(tblYqnsJhglJh.getJhid().longValue());
                    tblYqnsJhglJhprojectName.setGlId(t.getId().toString());
                    tblYqnsJhglJhprojectNameList.add(tblYqnsJhglJhprojectName);
                });
            }
            // 23 三级单位任中经济责任审计
            if (tblYqnsJhglJh.getLeaveAudit3LEntityList() != null && tblYqnsJhglJh.getLeaveAudit3LEntityList().size() > 0) {
                tblYqnsJhglJh.getLeaveAudit3LEntityList().stream().forEach(t -> {
                    TblYqnsJhglJhprojectName tblYqnsJhglJhprojectName = new TblYqnsJhglJhprojectName();
//                    tblYqnsJhglJhprojectName.setProjectName(t.getName());

                    tblYqnsJhglJhprojectName.setGltype("23");
                    tblYqnsJhglJhprojectName.setJhid(tblYqnsJhglJh.getJhid().longValue());
                    tblYqnsJhglJhprojectName.setGlId(t.getId().toString());
                    tblYqnsJhglJhprojectNameList.add(tblYqnsJhglJhprojectName);
                });
            }
            // 31 工程建设项目结算审计
            if (tblYqnsJhglJh.getTblYqnsJsxmJbqkList() != null && tblYqnsJhglJh.getTblYqnsJsxmJbqkList().size() > 0) {
                tblYqnsJhglJh.getTblYqnsJsxmJbqkList().stream().forEach(t -> {
                    TblYqnsJhglJhprojectName tblYqnsJhglJhprojectName = new TblYqnsJhglJhprojectName();
//                    tblYqnsJhglJhprojectName.setProjectName(t.getYsxmmc());

                    tblYqnsJhglJhprojectName.setGltype("31");
                    tblYqnsJhglJhprojectName.setJhid(tblYqnsJhglJh.getJhid().longValue());
//                    tblYqnsJhglJhprojectName.setGlId(t.getJsxmjbqkid().toString());
                    tblYqnsJhglJhprojectNameList.add(tblYqnsJhglJhprojectName);
                });
            }

            // 32 工程建设项目竣工决算审计
            if (tblYqnsJhglJh.getTblYqnsGcxmjgYsjhList() != null && tblYqnsJhglJh.getTblYqnsGcxmjgYsjhList().size() > 0) {
                tblYqnsJhglJh.getTblYqnsGcxmjgYsjhList().stream().forEach(t -> {
                    TblYqnsJhglJhprojectName tblYqnsJhglJhprojectName = new TblYqnsJhglJhprojectName();
//                    tblYqnsJhglJhprojectName.setProjectName(t.getXmmc());
                    tblYqnsJhglJhprojectName.setGltype("32");

                    tblYqnsJhglJhprojectName.setJhid(tblYqnsJhglJh.getJhid().longValue());
//                    tblYqnsJhglJhprojectName.setGlId(t.getGcxmjgysjhid().toString());
                    tblYqnsJhglJhprojectNameList.add(tblYqnsJhglJhprojectName);
                });
            }
        }

        //获得数据
        return ResponseFormat.retParam(1, 200, tblYqnsJhglJhprojectNameList);
    }

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        boolean fa=true;
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJhid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
            fa=false;
        } else {
        	vo.setSpzt((long)0);
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
            
        }
        if(vo.getJhid()==null) {
        	vo.setJhid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) { 
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }

        if(fa) {
        	List<TblYqnsOperate> list1 = tblYqnsOperateMapper.findbyByformid(loginStaff.getStaffid().toString(), "1521");
            if(list1==null || list1.size()<=0 ) {
            
    	        List<String> list = tblStaffMapper.getByroleIds("计划科科长");
    	        if(list!=null && list.size()>0) {
    	        	 TblStaffUtil staff = userProvider.get();
    				for (String string : list) {
    					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
    					if(list2!=null && list2.size()>0) {
    						for (TblStaff user : list2) {
    							TblYqnsOperate newoper= new TblYqnsOperate();
    							newoper.setSsmkid("1465");
    							newoper.setSsmk("工程审计项目安排");
    							newoper.setRwmc("工程审计项目安排");
    							newoper.setFormid(vo.getJhid());
    							newoper.setFormname("工程审计项目安排");
    							newoper.setOperid(RandomUtil.uuBigDecimalId());
    						    newoper.setStatus(0);
    						    newoper.setCreatestaffid(staff.getStaffid());
    						    newoper.setCreatename(staff.getRealname());
    						    newoper.setCreatedate(new Date());
    						    newoper.setRwuserid(user.getStaffid().toString());
    						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
    						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
    						    tblYqnsOperateMapper.insert(newoper);
    						    
    						    
    						    newoper= new TblYqnsOperate();
    							newoper.setSsmkid("1466");
    							newoper.setSsmk("财务审计项目安排");
    							newoper.setRwmc("财务审计项目安排");
    							newoper.setFormid(vo.getJhid());
    							newoper.setFormname("财务审计项目安排");
    							newoper.setOperid(RandomUtil.uuBigDecimalId());
    						    newoper.setStatus(0);
    						    newoper.setCreatestaffid(staff.getStaffid());
    						    newoper.setCreatename(staff.getRealname());
    						    newoper.setCreatedate(new Date());
    						    newoper.setRwuserid(user.getStaffid().toString());
    						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
    						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
    						    tblYqnsOperateMapper.insert(newoper);
    						}
    					}
    					
    				}
    			}
            }
        }
        
        //this.baseMapper.deleteAttByPk(vo.getJhid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJhid().toString(), attId);
            }
        } 
        vo.setJhchugid(vo.getJhid().toString());
        if(vo.getJhid()==null) {
        	vo.setJhid(RandomUtil.uuBigDecimalId());
        }
        boolean retw = this.saveOrUpdate(vo);
        if (!retw) { 
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        
        tblYqnsJhglJhGLService.saveOrUpdateList(token, vo.getJhid().longValue(), vo.getTblYqnsJhglJhGLList());
        
        if(org.apache.commons.lang.StringUtils.isNotBlank(vo.getOtherAuditIdsStrs())) {
        	List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByAuditIds(vo.getOtherAuditIdsStrs());
        	//关联草稿字段为空 or 关联当前实体类字段，直接修改需要关联的字段  
        	List<BigDecimal> uidList = oauList.stream().filter(v -> (v.getDraftPlanId()== null || vo.getJhid().compareTo(v.getDraftPlanId()) == 0)).map(TblYqnsOtherAudit::getAuditId).collect(Collectors.toList());
        	if(uidList != null && uidList.size() > 0) {
        		String updateIds = "";
        		for (BigDecimal id : uidList) {
					updateIds += id+",";
				}
        		updateIds = updateIds.substring(0, updateIds.length() - 1);
        		this.tblYqnsOtherAuditMapper.updateJhzgIdByIds(vo.getJhid(),updateIds,1);
        	}
        	//获取关联草稿字段不为空，并且关联草稿字段不是当前草稿则复制；
        	List<TblYqnsOtherAudit> copyList = oauList.stream().filter(v -> (v.getDraftPlanId()!= null && vo.getJhid().compareTo(v.getDraftPlanId()) != 0)).collect(Collectors.toList());
        	if(copyList != null && copyList.size() > 0) {
        		for (TblYqnsOtherAudit audit : copyList) {
            		audit.setAuditId(RandomUtil.uuBigDecimalId());
            		audit.setDraftPlanId(vo.getJhid());
            		audit.setSecondDraftPlanId(BigDecimal.valueOf(1));
    				this.tblYqnsOtherAuditMapper.insert(audit);
    			}
        	}
        }
       
        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJh bean = this.getById(vo.getJhid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhid().toString());
        bean.setAttachments(attachments);
        this.dealJhglJhGL(token, bean);
        
        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByDraftPlanId(bean.getJhid());
        List<String> orgNameList = null;
        for (TblYqnsOtherAudit oau : oauList) {
        	orgNameList = this.tblYqnsOtherAuditMapper.selectAuditOrgNameList(oau.getAuditOrgidStrs());
        	oau.setAuditOrgNameStrs(String.join(",", orgNameList));
		}
        bean.setOauList(oauList);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }

    private void dealJhglJhGL(String token, TblYqnsJhglJh vo) throws Exception {
        // 获取关联id数据
        List<TblYqnsJhglJhGL> tblYqnsJhglJhGLList = tblYqnsJhglJhGLService.findListByJHID(token, vo.getJhid().toString());

        if (null == tblYqnsJhglJhGLList) {
            return;
        }
        /**
         * 审计类型
         * 1：专项审计
         * 11：生产经营管理专项审计
         * 12：基建与投资专项审计
         * *************************************
         * 2：经济责任审计
         * 21：二级单位及所属成员单位离任经济责任审计
         * 22：二级单位任中经济责任审计
         * 23: 三级单位离任经济责任审计
         * *************************************
         * 3: 工程建设项目审计
         * 31：工程建设项目结算审计
         * 32：工程建设项目竣工决算审计
         */
        // 11 12 暂定
        List<String> sjlx11 = tblYqnsJhglJhGLList.stream().filter(v -> "11".equals(v.getGltype())).map(TblYqnsJhglJhGL::getGlId).collect(Collectors.toList());
        List<String> sjlx12 = tblYqnsJhglJhGLList.stream().filter(v -> "12".equals(v.getGltype())).map(TblYqnsJhglJhGL::getGlId).collect(Collectors.toList());
        // 21 二级单位及所属成员单位离任经济责任审计
        List<String> sjlx21 = tblYqnsJhglJhGLList.stream().filter(v -> "21".equals(v.getGltype())).map(TblYqnsJhglJhGL::getGlId).collect(Collectors.toList());
        // 22 二级单位任中经济责任审计
        List<String> sjlx22 = tblYqnsJhglJhGLList.stream().filter(v -> "22".equals(v.getGltype())).map(TblYqnsJhglJhGL::getGlId).collect(Collectors.toList());
        
        if (sjlx11 != null && sjlx11.size() >0) {
            // 11生产经营管理专项审计
        	 try {
        		 List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx11);
        		 vo.setMx11(evaluationList);
        	 }catch (Exception e){
                 e.printStackTrace();
             }
        }
        
        if (sjlx12 != null && sjlx12.size() >0) {
            // 12基建与投资专项审计
        	try {
	            List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx12);
	            vo.setMx12(evaluationList);
	        }catch (Exception e){
	            e.printStackTrace();
	        }
        }
        
        if (sjlx21 != null && sjlx21.size() > 0) {
            // 21 二级单位及所属成员单位离任经济责任审计
        	try {
                List<LeaveAudit2LEntity> leaveAudit2LEntityList = leaveAudit2LService.findByIds(String.join(",", sjlx21));
                vo.setLeaveAudit2LEntityList(leaveAudit2LEntityList);
            } catch (Exception e) {
            		e.printStackTrace();
    		}
        }

        if (sjlx22 != null && sjlx22.size() > 0) {
            // 22 二级单位任中经济责任审计
        	try {
	            List<InterimAuditDetailEntity> auditSuggestion2LEntityList = interimAuditDetailService.findByIds(String.join(",", sjlx22));
	            vo.setAuditSuggestion2LEntityList(auditSuggestion2LEntityList);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }

        
        try {
            // 23 三级单位任中经济责任审计
            List<TblYqnsJhglJhGL> leaveAudit3LEntityList = tblYqnsJhglJhGLList.stream().filter(v -> "23".equals(v.getGltype())).collect(Collectors.toList());
            vo.setLeaveAudit3LEntityList(leaveAudit3LEntityList);
        }catch (Exception e){
            e.printStackTrace();
        }


        // 31 工程建设项目结算审计
    	try {
            List<TblYqnsJhglJhGL> tblYqnsJsxmJbqkList = tblYqnsJhglJhGLList.stream().filter(v -> "31".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsJsxmJbqkList(tblYqnsJsxmJbqkList);
    	} catch (Exception e) {
			// TODO: handle exception
		}

        // 32 工程建设项目竣工决算审计
    	try {
            List<TblYqnsJhglJhGL> tblYqnsGcxmjgYsjhList = tblYqnsJhglJhGLList.stream().filter(v -> "32".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsGcxmjgYsjhList(tblYqnsGcxmjgYsjhList);
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }


    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsJhglJh> query = new LambdaQueryWrapper<TblYqnsJhglJh>()
                .eq(TblYqnsJhglJh::getJhid, vo.getJhid());
        int ret = this.baseMapper.delete(query);
        if (ret < 1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        List<TblYqnsJhglJhGL> tblYqnsJhglJhGLList = tblYqnsJhglJhGLService.findListByJHID(token, vo.getJhid().toString());
        if (tblYqnsJhglJhGLList != null) {
            tblYqnsJhglJhGLList.forEach(v -> {
                try {
                    tblYqnsJhglJhGLService.deleteGL(token, v.getId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        this.tblYqnsOtherAuditMapper.deleteByDraftPladId(vo.getJhid());
        
        List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyByformidparentid(loginStaff.getStaffid().toString(), "1465");
        List<TblYqnsOperate> list1 = tblYqnsOperateMapper.findbyByformidparentid(loginStaff.getStaffid().toString(), "1466");
        List<TblYqnsOperate> list2 = tblYqnsOperateMapper.findbyByformidpare("'1465','1466'");
        list.addAll(list2);
        list.addAll(list1); 
        if(list!=null && list.size()>0) {
        	  tblYqnsOperateMapper.deleteoneByUserId(loginStaff.getStaffid().toString(), vo.getJhid().toString());
        	  tblYqnsOperateMapper.deleteoneBymkId("'1465','1466'");
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
     *
     * @param token
     * @return null or not null
     * @throws Exception
     */
    private JsonBean validToken(String token) throws Exception {
        loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return null;
    }

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhglJh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "计划名称"};
        List<TblYqnsJhglJh> list;
        PageInfo<TblYqnsJhglJh> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJhglJh bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getJhmc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("计划".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public JsonBean importData(MultipartFile file, String token) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        List<Object[]> objList = new ArrayList<>();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Object[] obj = new Object[lastCellNum];
            for (int k = 0; k < lastCellNum; k++) {
                XSSFCell cell = row.getCell(k);
                obj[k] = cell.getStringCellValue();
            }
            objList.add(obj);
        }
        for (Object[] obj : objList) {
            TblYqnsJhglJh o = new TblYqnsJhglJh();
            o.setCjr(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setJhmc(obj[1].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

}




