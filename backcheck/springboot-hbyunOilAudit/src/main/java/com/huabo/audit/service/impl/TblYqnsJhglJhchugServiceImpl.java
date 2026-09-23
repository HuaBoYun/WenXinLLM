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
import com.huabo.audit.oracle.mapper.TblYqnsJhglJhchugMapper;
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
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG_MX(计划管理计划初稿明细)】的数据库操作Service实现
 */
@Service
public class TblYqnsJhglJhchugServiceImpl extends ServiceImpl<TblYqnsJhglJhchugMapper, TblYqnsJhglJhchug>
        implements TblYqnsJhglJhchugService {

    TblStaffUtil loginStaff;


    @Resource
    TblYqnsJhglJhchugGLService tblYqnsJhglJhchugGLService;
    
    @Resource
    ProjectProposalEvaluationService projectProposalEvaluationService;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

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
    
    @Autowired
    private InterimAuditDetailService interimAuditDetailService;

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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhglJhchug vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        /*PageInfo<TblYqnsJhglJhchug> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsJhglJhchug> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getJhmc())) {
            wrapper.lambda().like(TblYqnsJhglJhchug::getJhmc, vo.getJhmc());
        }
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsJhglJhchug::getCjr, vo.getCjr());
        }
        if (StringUtils.isNotBlank(vo.getSjxmmc())) {
            wrapper.lambda().like(TblYqnsJhglJhchug::getSjxmmc, vo.getSjxmmc());
        }
        if (vo.getCjsj() != null) {
            wrapper.lambda().ge(TblYqnsJhglJhchug::getCjsj, vo.getCjsj());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsJhglJhchug::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJhglJhchug::getEndDate, vo.getEndDate());
        }
        if(vo.getSpzt() != null) {
        	wrapper.lambda().eq(TblYqnsJhglJhchug::getSpzt, vo.getSpzt());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJhglJhchug> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJhglJhchug> page = new PageResult<TblYqnsJhglJhchug>().build(pageInfo);

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
    public JsonBean saveOrUpdate(String token, TblYqnsJhglJhchug vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJhchugid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
        	vo.setSpzt((long)0);
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getJhchugid()==null) {
        	vo.setJhchugid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }

        //this.baseMapper.deleteAttByPk(vo.getJhchugid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJhchugid().toString(), attId);
            }
        }
        tblYqnsJhglJhchugGLService.saveOrUpdateList(token, vo.getJhchugid().longValue(), vo.getTblYqnsJhglJhchugGLList());

        if(org.apache.commons.lang.StringUtils.isNotBlank(vo.getOtherAuditIdsStrs())) {
        	List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByAuditIds(vo.getOtherAuditIdsStrs());
        	//关联草稿字段为空 or 关联当前实体类字段，直接修改需要关联的字段  
        	List<BigDecimal> uidList = oauList.stream().filter(v -> (v.getDraftPlanId()== null || vo.getJhchugid().compareTo(v.getDraftPlanId()) == 0)).map(TblYqnsOtherAudit::getAuditId).collect(Collectors.toList());
        	if(uidList != null && uidList.size() > 0) {
        		String updateIds = "";
        		for (BigDecimal id : uidList) {
					updateIds += id+",";
				}
        		updateIds = updateIds.substring(0, updateIds.length() - 1);
        		this.tblYqnsOtherAuditMapper.updateJhchugIdByIds(vo.getJhchugid(),updateIds,1);
        	}
        	//获取关联草稿字段不为空，并且关联草稿字段不是当前草稿则复制；
        	List<TblYqnsOtherAudit> copyList = oauList.stream().filter(v -> (v.getDraftPlanId()!= null && vo.getJhchugid().compareTo(v.getDraftPlanId()) != 0)).collect(Collectors.toList());
        	if(copyList != null && copyList.size() > 0) {
        		for (TblYqnsOtherAudit audit : copyList) {
            		audit.setAuditId(RandomUtil.uuBigDecimalId());
            		audit.setDraftPlanId(vo.getJhchugid());
            		audit.setFirstDraftPlanId(BigDecimal.valueOf(1));
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
    public JsonBean detail(String token, TblYqnsJhglJhchug vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJhchug bean = this.getById(vo.getJhchugid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhchugid().toString());
        bean.setAttachments(attachments);
        this.dealJhglJhchugGL(token,bean);
        
        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByDraftPlanId(bean.getJhchugid());
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

//    private void dealSjlxmx(TblYqnsJhglJhchug vo) {
//        Map<String, Object> qryMap = new HashMap<>();
//        qryMap.put("jhchugid", vo.getJhchugid());
//        List<TblYqnsJhglJhchugMx> sjlxmxList = this.mxService.getBaseMapper().selectByMap(qryMap);
//        List<TblYqnsJhglJhchugMx> sjlx11 = sjlxmxList.stream().filter(v -> "11".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx12 = sjlxmxList.stream().filter(v -> "12".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx21 = sjlxmxList.stream().filter(v -> "21".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx22 = sjlxmxList.stream().filter(v -> "22".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx23 = sjlxmxList.stream().filter(v -> "23".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx31 = sjlxmxList.stream().filter(v -> "31".equals(v.getSjlx())).collect(Collectors.toList());
//        List<TblYqnsJhglJhchugMx> sjlx32 = sjlxmxList.stream().filter(v -> "32".equals(v.getSjlx())).collect(Collectors.toList());
//        vo.setMx11(sjlx11);
//        vo.setMx12(sjlx12);
//        vo.setMx21(sjlx21);
//        vo.setMx22(sjlx22);
//        vo.setMx23(sjlx23);
//        vo.setMx31(sjlx31);
//        vo.setMx32(sjlx32);
//    }


    private void dealJhglJhchugGL(String token, TblYqnsJhglJhchug vo) throws Exception {
        // 获取关联id数据
        List<TblYqnsJhglJhchugGL> tblYqnsJhglJhchugGLList = tblYqnsJhglJhchugGLService.findListByJHCHUGID(token, vo.getJhchugid().toString());

        if (null == tblYqnsJhglJhchugGLList) {
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
        List<String> sjlx11 = tblYqnsJhglJhchugGLList.stream().filter(v -> "11".equals(v.getGltype())).map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        List<String> sjlx12 = tblYqnsJhglJhchugGLList.stream().filter(v -> "12".equals(v.getGltype())).map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        // 21 二级单位及所属成员单位离任经济责任审计
        List<String> sjlx21 = tblYqnsJhglJhchugGLList.stream().filter(v -> "21".equals(v.getGltype())).map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        // 22 二级单位任中经济责任审计
        List<String> sjlx22 = tblYqnsJhglJhchugGLList.stream().filter(v -> "22".equals(v.getGltype())).map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());


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
                List<TblYqnsJhglJhchugGL> leaveAudit3LEntityList = tblYqnsJhglJhchugGLList.stream().filter(v -> "23".equals(v.getGltype())).collect(Collectors.toList());
                vo.setLeaveAudit3LEntityList(leaveAudit3LEntityList);
            }catch (Exception e){
                e.printStackTrace();
            }


            // 31 工程建设项目结算审计
        	try {
	            List<TblYqnsJhglJhchugGL> tblYqnsJsxmJbqkList = tblYqnsJhglJhchugGLList.stream().filter(v -> "31".equals(v.getGltype())).collect(Collectors.toList());
	            vo.setTblYqnsJsxmJbqkList(tblYqnsJsxmJbqkList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

            // 32 工程建设项目竣工决算审计
        	try {
	            List<TblYqnsJhglJhchugGL> tblYqnsGcxmjgYsjhList = tblYqnsJhglJhchugGLList.stream().filter(v -> "32".equals(v.getGltype())).collect(Collectors.toList());
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
    public JsonBean delete(String token, TblYqnsJhglJhchug vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsJhglJhchug> query = new LambdaQueryWrapper<TblYqnsJhglJhchug>()
                .eq(TblYqnsJhglJhchug::getJhchugid, vo.getJhchugid());
        int ret =  this.baseMapper.delete(query);
        if (ret<1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        List<TblYqnsJhglJhchugGL> tblYqnsJhglJhchugGLList = tblYqnsJhglJhchugGLService.findListByJHCHUGID(token, vo.getJhchugid().toString());
        if (tblYqnsJhglJhchugGLList != null) {
            tblYqnsJhglJhchugGLList.forEach(v -> {
                try {
                    tblYqnsJhglJhchugGLService.deleteGL(token, v.getId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        this.tblYqnsOtherAuditMapper.deleteByDraftPladId(vo.getJhchugid());
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhglJhchug vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "计划名称"};
        List<TblYqnsJhglJhchug> list;
        PageInfo<TblYqnsJhglJhchug> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJhglJhchug bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getJhmc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("计划初稿".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsJhglJhchug o = new TblYqnsJhglJhchug();
            o.setCjr(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setJhmc(obj[1].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


	@Override
	public JsonBean detailJhCgByChugao(String token, String jhcgid) throws Exception {
		JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJhglJhchug bean = this.getById(jhcgid);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhchugid().toString());
        bean.setAttachments(attachments);
        this.dealJhglJhChugGLByZgao(token, bean);
        
        List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListBySecondDraftPlanId(bean.getJhchugid());
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


	private void dealJhglJhChugGLByZgao(String token, TblYqnsJhglJhchug vo) throws Exception {
		 // 获取关联id数据
        List<TblYqnsJhglJhchugGL> tblYqnsJhglJhchugGLList = tblYqnsJhglJhchugGLService.findListByJHCGIDByChugao(token, vo.getJhchugid().toString());

        if (null == tblYqnsJhglJhchugGLList) {
            return;
        }
        
        Map<String,BigDecimal> map = null;
        
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
        List<TblYqnsJhglJhchugGL> sjlx11List = tblYqnsJhglJhchugGLList.stream().filter(v -> "11".equals(v.getGltype())).collect(Collectors.toList());
        //12 基建与投资专项审计
        List<TblYqnsJhglJhchugGL> sjlx12List = tblYqnsJhglJhchugGLList.stream().filter(v -> "12".equals(v.getGltype())).collect(Collectors.toList());
        // 21 二级单位及所属成员单位离任经济责任审计
        List<TblYqnsJhglJhchugGL> sjlx21List = tblYqnsJhglJhchugGLList.stream().filter(v -> "21".equals(v.getGltype())).collect(Collectors.toList());
        // 22 二级单位任中经济责任审计
        List<TblYqnsJhglJhchugGL> sjlx22List = tblYqnsJhglJhchugGLList.stream().filter(v -> "22".equals(v.getGltype())).collect(Collectors.toList());


        if (sjlx11List != null && sjlx11List.size() >0) {
            // 11生产经营管理专项审计
        	 try {
        		 List<String> sjlx11 = sjlx11List.stream().map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        		 List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx11);
        		 map = new HashMap<String, BigDecimal>(0);
        		 for (TblYqnsJhglJhchugGL gl : sjlx11List) {
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
        		List<String> sjlx12 = sjlx12List.stream().map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
	            List<ProjectProposalEvaluationEntity> evaluationList = projectProposalEvaluationService.findByIds(sjlx12);
	            map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhchugGL gl : sjlx12List) {
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
        		List<String> sjlx21 = sjlx21List.stream().map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        		List<LeaveAudit2LEntity> leaveAudit2LEntityList = leaveAudit2LService.findByIds(String.join(",", sjlx21));
        		map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhchugGL gl : sjlx21List) {
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
        		List<String> sjlx22 = sjlx22List.stream().map(TblYqnsJhglJhchugGL::getGlId).collect(Collectors.toList());
        		List<InterimAuditDetailEntity> auditSuggestion2LEntityList = interimAuditDetailService.findByIds(String.join(",", sjlx22));
        		map = new HashMap<String, BigDecimal>(0);
	       		 for (TblYqnsJhglJhchugGL gl : sjlx22List) {
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
                List<TblYqnsJhglJhchugGL> leaveAudit3LEntityList = tblYqnsJhglJhchugGLList.stream().filter(v -> "23".equals(v.getGltype())).collect(Collectors.toList());
                vo.setLeaveAudit3LEntityList(leaveAudit3LEntityList);
            }catch (Exception e){
                e.printStackTrace();
            }


            // 31 工程建设项目结算审计
        	try {
	            List<TblYqnsJhglJhchugGL> tblYqnsJsxmJbqkList = tblYqnsJhglJhchugGLList.stream().filter(v -> "31".equals(v.getGltype())).collect(Collectors.toList());
	            vo.setTblYqnsJsxmJbqkList(tblYqnsJsxmJbqkList);
        	} catch (Exception e) {
				// TODO: handle exception
			}

            // 32 工程建设项目竣工决算审计
        	try {
	            List<TblYqnsJhglJhchugGL> tblYqnsGcxmjgYsjhList = tblYqnsJhglJhchugGLList.stream().filter(v -> "32".equals(v.getGltype())).collect(Collectors.toList());
	            vo.setTblYqnsGcxmjgYsjhList(tblYqnsGcxmjgYsjhList);
        	} catch (Exception e) {
				// TODO: handle exception
			}
	}

}




