package com.huabo.audit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBorrowRecordMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjxmbMapper;
import com.huabo.audit.service.TblYqnsSjxmbService;
import com.huabo.audit.util.PageResult;

import cn.hutool.core.util.StrUtil;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_SJXMB(审计项目表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjxmbServiceImpl extends ServiceImpl<TblYqnsSjxmbMapper, TblYqnsSjxmb> implements TblYqnsSjxmbService {
	
    TblStaffUtil loginStaff;
    
    @Autowired
    private  ImplementPlanMapper implementPlanMapper;
    
    @Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
    
    @Autowired
    private TblNbsjBorrowRecordMapper tblNbsjBorrowRecordMapper;

    @Autowired
    private ReservePropertyService reservePropertyService;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjxmb vo) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        /*PageInfo<TblYqnsSjxmb> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsSjxmb> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getGcmc())) {
            wrapper.lambda().like(TblYqnsSjxmb::getGcmc, vo.getGcmc());
        }
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsSjxmb::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getSgdw())) {
            wrapper.lambda().like(TblYqnsSjxmb::getSgdw, vo.getSgdw());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjxmb::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjxmb::getEndDate, vo.getEndDate());
        }
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()).or().inSql("EXT1", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()));
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsSjxmb> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjxmb> page = new PageResult<TblYqnsSjxmb>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjxmb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getSjxmbid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getSjxmbid()==null) {
        	vo.setSjxmbid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getSjxmbid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getSjxmbid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsSjxmb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjxmb bean = this.getById(vo.getSjxmbid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getSjxmbid().toString());
        bean.setAttachments(attachments);
        return ResponseFormat.retParam(1, 200, bean);
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
    public JsonBean delete(String token, TblYqnsSjxmb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            this.baseMapper.deleteAttByPk(id);
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsSjxmb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "工程名称", "合同编号", "施工单位", "二审审查金额", "本次审计人员"};
        List<TblYqnsSjxmb> list;
        PageInfo<TblYqnsSjxmb> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsSjxmb bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getGcmc();
            obj[2] = bean.getHtbh();
            obj[3] = bean.getSgdw();
            obj[4] = bean.getEsscje();
            obj[5] = bean.getBcsjry();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("审计项目表".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsSjxmb o = new TblYqnsSjxmb();
            o.setCjr(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setGcmc(obj[1].toString());
            o.setHtbh(obj[2].toString());
            o.setSgdw(obj[3].toString());
            o.setEsscje(obj[4].toString().equals("") ? null : new BigDecimal(obj[4].toString()));
            o.setBcsjry(obj[5].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 一个或多个审计项目
     * 下发到一个或多个员工
     *
     * @param token
     * @param vo
     * @return
     */
    @Override
    public JsonBean xf(String token, TblYqnsSjxmb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getIds() == null || vo.getRyIdsList() == null || vo.getIds().size() == 0 || vo.getRyIdsList().size() == 0){
            return ResponseFormat.retParam(0, "列表未选择或人员未选择", Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            TblYqnsSjxmb sjxmb = this.getById(id);
            String join = StrUtil.join(",", vo.getRyIdsList());
            sjxmb.setRyIds(join);
            this.updateById(sjxmb);
        }
        return ResponseFormat.retParam(1, "操作成功", Boolean.TRUE);
    }
    
    
    @Override
	public JsonBean getLiftMenu(String token,String projectid) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(null==projectid || "".equals(projectid)) {
			projectid = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(loginStaff.getStaffid()).toString();
			//return ResponseFormat.retParam(0,"请确认项目信息",null);
		}
		ImplementPlanEntity project = implementPlanMapper.selectById(projectid);
		resultMap.put("tree", getTree(project, null));
		return ResponseFormat.retParam(1,200,resultMap);
	}
    
    private Tree getTree(ImplementPlanEntity project,String type){ 
    	//是否工程类
    	if("1".equals(project.getIsgc())) {
    		String[][] Menu =  new String[][]{
    			{"审计通知","#"},
    			{"通知变更","#"},
    			{"审计承诺书","#"},
    			{"我的工程任务","#"},
    			{"底稿管理","#"},
    			{"审计项目运行情况表","#"},
    			{"审计督导任务","#"},
    			{"交换意见稿","#"},
    			{"审理报告","#"},
    			{"审计报告定稿","#"}
    		};
    		Tree tree = new Tree();
    		tree.setId(new BigDecimal("-1"));
    		tree.setIsParent(true);
    		tree.setName("实施方案");
    		tree.setOpen(true);
    		tree.setUrl("/nbsj/sjgl/project_look");
    		tree.setTarget("mainFramex");
    		List<Tree> children = new ArrayList<Tree>();
    		for (String[] str : Menu) {
    			Tree childTree = new Tree();
    			childTree.setIsParent(false);
    			childTree.setName(str[0]);
    			childTree.setpId(new BigDecimal("-1"));
    			childTree.setTarget("mainFramex");
    			childTree.setId(new BigDecimal("0"));
    			children.add(childTree); 
    		}
    		tree.getChildren().addAll(children);
    		return tree;
    	}else {
    		String[][] Menu =  new String[][]{
    			{"审计任务清单","#"},
//    			{"项目资料","#"},
    			{"审计通知","#"},
    			{"通知变更","#"},
    			{"审计承诺书","#"},
//    			{"我的任务","#"},
//    			{"我的底稿","#"},
    			{"底稿管理","#"},
    			{"审计结果确认单","#"},
    			{"审计项目追款","#"},
    			{"审计工作记录","#"},
//    			{"审计发现","#"},
    			{"审计督导任务","#"},
    			{"现场审查主要内容","#"},
    			{"审计项目运行情况表","#"},
    			{"质量分析报告","#"},
    			{"交换意见稿","#"},
    			{"审理报告","#"},
    			{"审计报告定稿","#"}
    		};
    		Tree tree = new Tree();
    		tree.setId(new BigDecimal("-1"));
    		tree.setIsParent(true);
    		tree.setName("实施方案");
    		tree.setOpen(true);
    		tree.setUrl("/nbsj/sjgl/project_look");
    		tree.setTarget("mainFramex");
    		List<Tree> children = new ArrayList<Tree>();
    		for (String[] str : Menu) {
    			Tree childTree = new Tree();
    			childTree.setIsParent(false);
    			childTree.setName(str[0]);
    			childTree.setpId(new BigDecimal("-1"));
    			childTree.setTarget("mainFramex");
    			childTree.setId(new BigDecimal("0"));
    			children.add(childTree);
    		}
    		tree.getChildren().addAll(children);
    		return tree;
		}
		
	}
    
    @Override
	public JsonBean projectArchiveList(String token) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//==查询当前实施的项目！
		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		if(null == tnp.getId()) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Tree tree = getTree(tnp, null);
		
		resultMap.put("tree", tree);
		resultMap.put("pj", tnp);
		return ResponseFormat.retParam(1,200,resultMap);
	}
    
    //获取当前实施项目；
  	public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
  		BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
  		if (projectId == null) {
  			return null;
  		}
  		return implementPlanMapper.selectById(projectId.toString());
  	}
  	
  	@Override
    public JsonBean daList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<ImplementPlanEntity> pageInfo = new PageInfo<ImplementPlanEntity>();
		com.github.pagehelper.PageInfo<ImplementPlanEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.implementPlanMapper.daList(pageInfo,projectName,qdcode,staff.getStaffid());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});



    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());

    	resultMap.put("pageInfo", pageInfo);
        
        return ResponseFormat.retParam(1, 200, pageInfo);
    }
  	
  	@Override
    public JsonBean dajyList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<ImplementPlanEntity> pageInfo = new PageInfo<ImplementPlanEntity>();
		com.github.pagehelper.PageInfo<ImplementPlanEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.implementPlanMapper.daList(pageInfo,projectName,qdcode,null);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		//赋值审批状态、借阅有效性
		List<ImplementPlanEntity> listRes = pageInfo2.getList();
		if(null!=listRes) {
			ImplementPlanEntity res = null;
			for (int i = 0; i < listRes.size(); i++) {
				res = listRes.get(i);
				//查询当前用户对该项目的借阅状态，如果已过借阅有效期，状态为0未审批；
				Integer jystatus = this.implementPlanMapper.selectJyStatusByStaff(res.getId(), staff.getStaffid());
				if(null==jystatus) {
					res.setJystatus(0);
				}else {
					res.setJystatus(jystatus);
				}
			}
		}
		
		
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
    	
    	resultMap.put("pageInfo", pageInfo);
        
        return ResponseFormat.retParam(1, 200, pageInfo);
    }
  	
  	@Override
    public JsonBean dajySaveOrUpdate(String token, TblNbsjBorrowRecordEntity vo) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        vo.setStaffid(staff.getStaffid().intValue());
        vo.setStatus(TblNbsjBorrowRecordEntity.SPNO);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		if(vo.getBorrowid() != null) {
			vo.setBackDate(vo.getBackDate());
			vo.setBorrowDate(vo.getBorrowDate());
			//修改；
			this.tblNbsjBorrowRecordMapper.updateEntity(vo);
		}else {
			vo.setBackDate(vo.getBackDate());
			vo.setBorrowDate(vo.getBorrowDate());
			
			//新增；
			this.tblNbsjBorrowRecordMapper.insertEntity(vo);
		}
		resultMap.put("borrowRecord",vo);
        
        return ResponseFormat.retParam(1, 200, resultMap);
    }
  	
  	@Override
    public JsonBean dajyDetail(String token, Integer borrowId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblNbsjBorrowRecordEntity borrowrecord = tblNbsjBorrowRecordMapper.selectByIdBorrw(borrowId);
    	return ResponseFormat.retParam(1,200,borrowrecord);
    }
  	
  	
  	@Override
    public JsonBean jyrzList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<ImplementPlanEntity> pageInfo = new PageInfo<ImplementPlanEntity>();
		com.github.pagehelper.PageInfo<ImplementPlanEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						//档案管理员能够看所有的记录；普通用户只能看自己的借阅记录
				        Integer isadmin = 0;
				        if(null!=staff.getRoleNames() && staff.getRoleNames().indexOf("档案管理员")>=0) {
				        	isadmin = 1;
				        }
				        
						this.implementPlanMapper.jyrzList(pageInfo,projectName,qdcode,staff.getStaffid(),isadmin);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
    	resultMap.put("pageInfo", pageInfo);
        
        return ResponseFormat.retParam(1, 200, pageInfo);
    }
  	
  	@Override
    public JsonBean jyrzxqList(String token, String id) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        
        //档案管理员能够看所有的记录；普通用户只能看自己的借阅记录
        Integer isadmin = 0;
        if(null!=staff.getRoleNames() && staff.getRoleNames().indexOf("档案管理员")>=0) {
        	isadmin = 1;
        }
        
        List<TblNbsjBorrowRecordEntity> list = this.implementPlanMapper.jyrzxqList(id,isadmin,staff.getStaffid());
    	resultMap.put("list", list);
        
        return ResponseFormat.retParam(1, 200, resultMap);
    }
  	
}




