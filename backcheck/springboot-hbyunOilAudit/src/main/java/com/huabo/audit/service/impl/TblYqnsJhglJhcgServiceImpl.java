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
import com.huabo.audit.oracle.mapper.*;
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
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿)】的数据库操作Service实现
 */
@Service
public class TblYqnsJhglJhcgServiceImpl extends ServiceImpl<TblYqnsJhglJhcgMapper, TblYqnsJhglJhcg>
        implements TblYqnsJhglJhcgService {

    TblStaffUtil loginStaff;

    @Resource
    TblYqnsJhglJhcgGLService tblYqnsJhglJhcgGLService;
    @Resource
    ProjectProposalEvaluationService projectProposalEvaluationService;
    
    @Resource
    private TblYqnsOtherAuditMapper tblYqnsOtherAuditMapper;
    
    @Resource
	private TblYqnsJhglJhcgGLMapper tblYqnsJhglJhcgGLMapper;
    
    @Autowired
    private InterimAuditDetailService interimAuditDetailService;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
	 @Autowired
	 private TblStaffMapper tblStaffMapper;


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
    private UserProvider userProvider;


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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhglJhcg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
       /* PageInfo<TblYqnsJhglJhcg> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsJhglJhcg> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getJhmc())) {
            wrapper.lambda().like(TblYqnsJhglJhcg::getJhmc, vo.getJhmc());
        }
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsJhglJhcg::getCjr, vo.getCjr());
        }
        if (StringUtils.isNotBlank(vo.getSjxmmc())) {
            wrapper.lambda().like(TblYqnsJhglJhcg::getSjxmmc, vo.getSjxmmc());
        }
        if (vo.getCjsj() != null) {
            wrapper.lambda().ge(TblYqnsJhglJhcg::getCjsj, vo.getCjsj());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate() != null) {
            wrapper.lambda().ge(TblYqnsJhglJhcg::getStartDate, vo.getStartDate());
        }
        
       
        
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJhglJhcg::getEndDate, vo.getEndDate());
        }
        
        if(vo.getSpzt() != null) {
        	wrapper.lambda().eq(TblYqnsJhglJhcg::getSpzt, vo.getSpzt());
        }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJhglJhcg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJhglJhcg> page = new PageResult<TblYqnsJhglJhcg>().build(pageInfo);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(page.getTlist());
        return ResponseFormat.retParam(1, 200, page);
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
    public JsonBean saveOrUpdate(String token, TblYqnsJhglJhcg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJhcgid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getJhcgid()==null) { 
        	vo.setJhcgid(RandomUtil.uuBigDecimalId());
        	 List<TblYqnsOperate> list1 = tblYqnsOperateMapper.findbyByformid(loginStaff.getStaffid().toString(), "1519");
             if(list1==null || list1.size()<=0 ) {
             	TblStaffUtil staff = userProvider.get();
                 List<String> list = tblStaffMapper.getByroleIds("计划科科长");
                 if(list!=null && list.size()>0) {
         			for (String string : list) {
         				List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
         				if(list2!=null && list2.size()>0) {
         					for (TblStaff user : list2) {
         						TblYqnsOperate newoper= new TblYqnsOperate();
         						newoper.setSsmkid("1520");
         						newoper.setSsmk("计划初稿");
         						newoper.setRwmc("计划初稿编制");
         						newoper.setFormid(vo.getJhcgid());
         						newoper.setFormname(vo.getSjxmmc());
         						newoper.setOperid(RandomUtil.uuBigDecimalId());
//         					    newoper.setParentid(tb.getOperid());
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
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
       // this.baseMapper.deleteAttByPk(vo.getJhcgid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJhcgid().toString(), attId);
            }
        }
        tblYqnsJhglJhcgGLService.saveOrUpdateList(token, vo.getJhcgid().longValue(), vo.getTblYqnsJhglJhcgGLList());
        
        if(org.apache.commons.lang.StringUtils.isNotBlank(vo.getOtherAuditIdsStrs())) {
        	 this.tblYqnsOtherAuditMapper.updateJhcgIdByIds(vo.getJhcgid(),vo.getOtherAuditIdsStrs());
        }
       
        
       
        return ResponseFormat.retParam(1, 200, vo);
    }

    
    @Override
	public JsonBean copyUnique(String token, String jhcgid) throws Exception {
    	JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJhcg bean = this.getById(jhcgid);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        // 获取关联id数据
        List<TblYqnsJhglJhcgGL> glList = tblYqnsJhglJhcgGLService.findListByJHCGID(token, jhcgid);

        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByDraftPlanId(bean.getJhcgid());
        
        //------------- 开始复制
        bean.setParentId(bean.getJhcgid());
        bean.setSpzt((long) 0);
        bean.setJhcgid(RandomUtil.uuBigDecimalId());
        
        this.save(bean);
        
        for (TblYqnsOtherAudit oau : oauList) {
        	oau.setAuditId(RandomUtil.uuBigDecimalId());
        	oau.setDraftPlanId(bean.getJhcgid());
        	oau.setCreateTime(new Date());
        	oau.setCreateStaffId(loginStaff.getStaffid());
			this.tblYqnsOtherAuditMapper.insert(oau);
		}
        
        BigDecimal newGlId = null;
        for (TblYqnsJhglJhcgGL gl : glList) {
        	newGlId = RandomUtil.uuBigDecimalId();
        	if(gl.getGltype().equals("23") || gl.getGltype().equals("31") || gl.getGltype().equals("32")) {
        		this.tblYqnsJhglJhcgGLMapper.insertGlRealDataAllInfo(bean.getJhcgid(),gl.getId(),newGlId);
        	}
        	gl.setId(newGlId);
        	gl.setJhcgid(bean.getJhcgid().longValue());
        	gl.setGxsj(new Date());
        	gl.setGxr(loginStaff.getStaffid().toString());
		}
        this.tblYqnsJhglJhcgGLService.saveBatch(glList);
        return ResponseFormat.retParam(1, 200, null);
	}
    

//    /**
//     *  改造之前得
//     *  保存 修改
//     *
//     * @param token
//     * @param vo
//     * @return
//     * @throws Exception
//     */
//    @Override
//    @Transactional
//    public JsonBean saveOrUpdate(String token, TblYqnsJhglJhcg vo) throws Exception {
//        JsonBean retParam = validToken(token);
//        if (retParam != null) return retParam;
//        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
//        vo.setExt1(loginStaff.getStaffid().toString());
//        if (vo.getJhcgid() != null) {
//            vo.setGxr(loginStaff.getRealname());
//            vo.setGxsj(new Date());
//        } else {
//            vo.setCjr(loginStaff.getRealname());
//            vo.setCjsj(new Date());
//        }
//        boolean ret = this.saveOrUpdate(vo);
//        if (!ret) {
//            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
//        }
//        this.baseMapper.deleteAttByPk(vo.getJhcgid().toString());
//        List<String> attIds = vo.getAttIds();
//        if (attIds != null && attIds.size() > 0) {
//            for (String attId : attIds) {
//                this.baseMapper.saveAtt(vo.getJhcgid().toString(), attId);
//            }
//        }
//
//        List<String> mxIds = vo.getMxIds();
//        if (mxIds != null && mxIds.size() > 0) {
//            TblYqnsJhglJhcgMx sjlxmx = new TblYqnsJhglJhcgMx();
//            for (String sjlxmxId : mxIds) {
//                sjlxmx.setJhcgmxid(new BigDecimal(sjlxmxId));
//                sjlxmx.setJhcgid(vo.getJhcgid().longValue());
//                this.mxService.updateById(sjlxmx);
//            }
//        } else {
//            Map<String, Object> qryMap = new HashMap<>();
//            qryMap.put("jhcgid", vo.getJhcgid());
//            this.mxService.getBaseMapper().deleteByMap(qryMap);
//        }
//
//        return ResponseFormat.retParam(1, 200, vo);
//    }

    /**
     * 详情
     *
     * @param token
     * @param jhcgid
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, String jhcgid) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJhcg bean = this.getById(jhcgid);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhcgid().toString());
        bean.setAttachments(attachments);
        this.dealJhglJhcgGL(token, bean);
        
        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByDraftPlanId(bean.getJhcgid());
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

    @Override
	public JsonBean detailJhCgByChugao(String token, String jhcgid) throws Exception {
    	JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJhcg bean = this.getById(jhcgid);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhcgid().toString());
        bean.setAttachments(attachments);
        this.dealJhglJhcgGLByCaoGao(token, bean);
        
        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByFirstDraftPlanId(bean.getJhcgid());
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
    
    
    private void dealJhglJhcgGLByCaoGao(String token, TblYqnsJhglJhcg vo) throws Exception {
    	// 获取关联id数据
        List<TblYqnsJhglJhcgGL> tblYqnsJhglJhcgGLList = tblYqnsJhglJhcgGLService.findListByJHCGIDByCaogao(token, vo.getJhcgid().toString());

        if (null == tblYqnsJhglJhcgGLList) {
            return;
        }
        
        Map<String,BigDecimal> map = null;
        
        // 11生产经营管理专项审计
        List<TblYqnsJhglJhcgGL> sjlx11List = tblYqnsJhglJhcgGLList.stream().filter(v -> "11".equals(v.getGltype())).collect(Collectors.toList());
        //12 基建与投资专项审计
        List<TblYqnsJhglJhcgGL> sjlx12List = tblYqnsJhglJhcgGLList.stream().filter(v -> "12".equals(v.getGltype())).collect(Collectors.toList());
        // 21 二级单位及所属成员单位离任经济责任审计
        List<TblYqnsJhglJhcgGL> sjlx21List = tblYqnsJhglJhcgGLList.stream().filter(v -> "21".equals(v.getGltype())).collect(Collectors.toList());
        // 22 二级单位任中经济责任审计
        List<TblYqnsJhglJhcgGL> sjlx22List = tblYqnsJhglJhcgGLList.stream().filter(v -> "22".equals(v.getGltype())).collect(Collectors.toList());

        if (sjlx11List != null && sjlx11List.size() >0) {
            // 11生产经营管理专项审计
        	 try {
        		 List<String> sjlx11 = sjlx11List.stream().map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        		 List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx11);
        		 map = new HashMap<String, BigDecimal>(0);
        		 for (TblYqnsJhglJhcgGL gl : sjlx11List) {
						map.put(gl.getGlId(), gl.getId());
        		 }
        		 for (ProjectProposalEvaluationEntity eva : evaluationList) {
					eva.setRelaid(map.get(eva.getId().toString()));
				 }
        		 vo.setMx11(evaluationList);
        	 }catch (Exception e){
                 e.printStackTrace();
             }
        }
        
        if (sjlx12List != null && sjlx12List.size() >0) {
            // 12基建与投资专项审计
        	try {
        		List<String> sjlx12 = sjlx12List.stream().map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
	            List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx12);
	            map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhcgGL gl : sjlx12List) {
							map.put(gl.getGlId(), gl.getId());
	       		 }
	       		 for (ProjectProposalEvaluationEntity eva : evaluationList) {
						eva.setRelaid(map.get(eva.getId().toString()));
				 }
	            vo.setMx12(evaluationList);
	        }catch (Exception e){
	            e.printStackTrace();
	        }
        }
        

        if (sjlx21List != null && sjlx21List.size() >0) {
            // 21 二级单位及所属成员单位离任经济责任审计
        	try {
        		List<String> sjlx21 = sjlx21List.stream().map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        		List<LeaveAudit2LEntity> leaveAudit2LEntityList = leaveAudit2LService.findByIds(String.join(",", sjlx21));
        		map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhcgGL gl : sjlx21List) {
	       			 map.put(gl.getGlId(), gl.getId());
	       		 }
	       		for (LeaveAudit2LEntity au : leaveAudit2LEntityList) {
	       			au.setRelaid(map.get(au.getId().toString()));
	       		}
        		
        		vo.setLeaveAudit2LEntityList(leaveAudit2LEntityList);
        	} catch (Exception e) {
        		e.printStackTrace();
			}
        }

        if (sjlx22List != null && sjlx22List.size() >0) {
            // 22 二级单位任中经济责任审计
        	try {
        		List<String> sjlx22 = sjlx22List.stream().map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        		List<InterimAuditDetailEntity> auditSuggestion2LEntityList = interimAuditDetailService.findByIds(String.join(",", sjlx22));
        		map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhcgGL gl : sjlx22List) {
	       			 map.put(gl.getGlId(), gl.getId());
	       		 }
	       		for (InterimAuditDetailEntity su : auditSuggestion2LEntityList) {
	       			su.setRelaid(map.get(su.getId().toString()));
	       		}
        		
        		vo.setAuditSuggestion2LEntityList(auditSuggestion2LEntityList);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        }

            try {
                // 23 三级单位任中经济责任审计
                List<TblYqnsJhglJhcgGL> leaveAudit3LEntityList = tblYqnsJhglJhcgGLList.stream().filter(v -> "23".equals(v.getGltype())).collect(Collectors.toList());
                vo.setLeaveAudit3LEntityList(leaveAudit3LEntityList);
            }catch (Exception e){
                e.printStackTrace();
            }


            // 31 工程建设项目结算审计
        	try {
            List<TblYqnsJhglJhcgGL> tblYqnsJsxmJbqkList = tblYqnsJhglJhcgGLList.stream().filter(v -> "31".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsJsxmJbqkList(tblYqnsJsxmJbqkList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

            // 31 工程建设项目竣工决算审计
        	try {
            List<TblYqnsJhglJhcgGL> tblYqnsGcxmjgYsjhList = tblYqnsJhglJhcgGLList.stream().filter(v -> "32".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsGcxmjgYsjhList(tblYqnsGcxmjgYsjhList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

	}


	private void dealJhglJhcgGL(String token, TblYqnsJhglJhcg vo) throws Exception {
        // 获取关联id数据
        List<TblYqnsJhglJhcgGL> tblYqnsJhglJhcgGLList = tblYqnsJhglJhcgGLService.findListByJHCGID(token, vo.getJhcgid().toString());

        if (null == tblYqnsJhglJhcgGLList) {
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
        // 11生产经营管理专项审计
        List<String> sjlx11 = tblYqnsJhglJhcgGLList.stream().filter(v -> "11".equals(v.getGltype())).map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        //12 基建与投资专项审计
        List<String> sjlx12 = tblYqnsJhglJhcgGLList.stream().filter(v -> "12".equals(v.getGltype())).map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        // 21 二级单位及所属成员单位离任经济责任审计
        List<String> sjlx21 = tblYqnsJhglJhcgGLList.stream().filter(v -> "21".equals(v.getGltype())).map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());
        // 22 二级单位任中经济责任审计
        List<String> sjlx22 = tblYqnsJhglJhcgGLList.stream().filter(v -> "22".equals(v.getGltype())).map(TblYqnsJhglJhcgGL::getGlId).collect(Collectors.toList());

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
        

        if (sjlx21 != null && sjlx21.size() >0) {
            // 21 二级单位及所属成员单位离任经济责任审计
        	try {
            List<LeaveAudit2LEntity> leaveAudit2LEntityList = leaveAudit2LService.findByIds(String.join(",", sjlx21));
            vo.setLeaveAudit2LEntityList(leaveAudit2LEntityList);
        	} catch (Exception e) {
        		e.printStackTrace();
			}
        }

        if (sjlx22 != null && sjlx22.size() >0) {
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
                List<TblYqnsJhglJhcgGL> leaveAudit3LEntityList = tblYqnsJhglJhcgGLList.stream().filter(v -> "23".equals(v.getGltype())).collect(Collectors.toList());
                vo.setLeaveAudit3LEntityList(leaveAudit3LEntityList);
            }catch (Exception e){
                e.printStackTrace();
            }


            // 31 工程建设项目结算审计
        	try {
            List<TblYqnsJhglJhcgGL> tblYqnsJsxmJbqkList = tblYqnsJhglJhcgGLList.stream().filter(v -> "31".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsJsxmJbqkList(tblYqnsJsxmJbqkList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

            // 31 工程建设项目竣工决算审计
        	try {
            List<TblYqnsJhglJhcgGL> tblYqnsGcxmjgYsjhList = tblYqnsJhglJhcgGLList.stream().filter(v -> "32".equals(v.getGltype())).collect(Collectors.toList());
            vo.setTblYqnsGcxmjgYsjhList(tblYqnsGcxmjgYsjhList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

    }

//
//    private void dealSjlxmx(TblYqnsJhglJhcg vo) {
//        Map<String, Object> qryMap = new HashMap<>();
//        qryMap.put("jhcgid", vo.getJhcgid());
//        List<TblYqnsJhglJhcgMx> sjlxmxList = this.mxService.getBaseMapper().selectByMap(qryMap);
//        List<TblYqnsJhglJhcgMx> sjlx11 = sjlxmxList.stream().filter(v -> "11".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx12 = sjlxmxList.stream().filter(v -> "12".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx21 = sjlxmxList.stream().filter(v -> "21".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx22 = sjlxmxList.stream().filter(v -> "22".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx23 = sjlxmxList.stream().filter(v -> "23".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx31 = sjlxmxList.stream().filter(v -> "31".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhcgMx> sjlx32 = sjlxmxList.stream().filter(v -> "32".equals(v.getSjlx())).collect(Collectors.toList());
//        vo.setMx11(sjlx11);
//        vo.setMx12(sjlx12);
//        vo.setMx21(sjlx21);
//        vo.setMx22(sjlx22);
//        vo.setMx23(sjlx23);
//        vo.setMx31(sjlx31);
//        vo.setMx32(sjlx32);
//    }

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
    public JsonBean delete(String token, TblYqnsJhglJhcg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsJhglJhcg> query = new LambdaQueryWrapper<TblYqnsJhglJhcg>()
                .eq(TblYqnsJhglJhcg::getJhcgid, vo.getJhcgid());
        int ret =  this.baseMapper.delete(query);
        if (ret<1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }

        List<TblYqnsJhglJhcgGL> tblYqnsJhglJhcgGLList = tblYqnsJhglJhcgGLService.findListByJHCGID(token, vo.getJhcgid().toString());
        if (tblYqnsJhglJhcgGLList != null) {
            tblYqnsJhglJhcgGLList.forEach(v -> {
                try {
                    tblYqnsJhglJhcgGLService.deleteGL(token, v.getId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        this.tblYqnsOtherAuditMapper.deleteByDraftPladId(vo.getJhcgid());
        TblStaffUtil loginStaff = userProvider.get();
        List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyByformidparentid(loginStaff.getStaffid().toString(), "1519");
        if(list!=null && list.size()>0) {
        	  tblYqnsOperateMapper.deleteoneByUserId(loginStaff.getStaffid().toString(), vo.getJhcgid().toString());
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhglJhcg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "计划名称"};
        List<TblYqnsJhglJhcg> list;
        PageInfo<TblYqnsJhglJhcg> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJhglJhcg bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getJhmc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("计划草稿".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsJhglJhcg o = new TblYqnsJhglJhcg();
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




