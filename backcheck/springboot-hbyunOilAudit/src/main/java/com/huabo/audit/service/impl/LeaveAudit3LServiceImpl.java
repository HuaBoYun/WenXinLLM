package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.LeaveAudit3LMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.vo.LeaveAudit3LCountVo;
import com.huabo.audit.service.LeaveAudit3LService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rui
 * @ClassName LeaveAudit3LServiceImpl
 * @Description
 * @DATE 2023/9/14
 */
@Service
public class LeaveAudit3LServiceImpl implements LeaveAudit3LService {

    @Autowired
    private LeaveAudit3LMapper leaveAudit3LMapper;
    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity leaveAudit3LEntity, BigDecimal jdid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit3LMapper.selectByEntity(leaveAudit3LEntity, user, jdid));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

    @Override
    public JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize,
                                     LeaveAudit3LEntity leaveAudit3LEntity, BigDecimal jdid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit3LMapper.selectListDraftPlan(leaveAudit3LEntity, user, jdid));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception {

        LeaveAudit3LEntity leaveAudit3LEntity = leaveAudit3LMapper.selectById(id);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(leaveAudit3LEntity);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", leaveAudit3LEntity);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean updateEntity(String token,LeaveAudit3LEntity leaveAudit3LEntity) throws Exception {
    	 TblStaffUtil user = userProvider.get();
         if (user != null) {
             TblStaff tblStaff = new TblStaff(); 
             tblStaff.setStaffid(user.getStaffid());
             leaveAudit3LEntity.setCreateUser(tblStaff);
         }
         if(leaveAudit3LEntity.getName()==null || leaveAudit3LEntity.getName().equals("")) {
        	  LeaveAudit3LEntity sl = leaveAudit3LMapper.selectById(leaveAudit3LEntity.getId().toString());
              leaveAudit3LEntity.setName(sl.getName());
         }
//         if(leaveAudit3LEntity.getProjectname()==null || leaveAudit3LEntity.getProjectname().equals("")) {
//        	 String projectname=leaveAudit3LEntity.getOrgname()+"原"+leaveAudit3LEntity.getOldJob()+leaveAudit3LEntity.getName()+"离任经济责任审计";
//             leaveAudit3LEntity.setProjectname(projectname);
//         }
         
        leaveAudit3LMapper.updateEntity(leaveAudit3LEntity);
        return ResponseFormat.retParam(1, "保存成功", leaveAudit3LEntity);
    }

    @Override
    public JsonBean saveEntity(String token, LeaveAudit3LEntity leaveAudit3LEntity) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            leaveAudit3LEntity.setCreateUser(tblStaff);
        }
        String projectname=leaveAudit3LEntity.getOrgname()+"原"+leaveAudit3LEntity.getOldJob()+leaveAudit3LEntity.getName()+"离任经济责任审计";
        leaveAudit3LEntity.setProjectname(projectname);
        leaveAudit3LMapper.insertEntity(leaveAudit3LEntity);
        return ResponseFormat.retParam(1, "保存成功", leaveAudit3LEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception {
        leaveAudit3LMapper.deleteEntity(ids);
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
    public JsonBean exportData(HttpServletResponse response, String token, LeaveAudit3LEntity vo, BigDecimal jdid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String[] titles = {"序号","姓名", "原职务", "原行政级别", "原单位", "原任职起止日期", "原任职结束时间",
                "现职务", "现行政级别", "现单位", "现任职开始时间", "现任职结束时间"
                , "部门职能分类", "部门历史沿革", "主责主业", "主要权利", "对外业务"
                , "人员构成", "是否独立核算", "资产情况", "主要费用", "收入", "成本"
                , "可控成本", "是否有外雇人员", "非常规性工作", "是否审计"};//"状态",, "季度", "季度类型"
        List<LeaveAudit3LEntity> list = selectByEntity(vo, user, null);
        if (jdid != null) {
        	list=selectByEntity(vo, user, jdid);
        }
        List<Object[]> objs = new ArrayList<Object[]>();
        AtomicLong xh = new AtomicLong(1);
        for (LeaveAudit3LEntity bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            
            //姓名
            obj[1] = bean.getName();
            
            //原职务
            obj[2] = bean.getOldJob();
            //原行政级别
            obj[3] = bean.getOldLevel();
            //原单位
//            obj[4] = bean.getOldOrgId();
            if(bean.getOldOrg()!=null) {
            	obj[4] = bean.getOldOrg().getOrgname();
            }else {
            	 obj[4] = "";
            }
            //原任职起止日期
            obj[5] = this.yyyyMMdd2Str(bean.getOldJobStartTime());
            obj[6] = this.yyyyMMdd2Str(bean.getOldJobEndTime());

            //现职务
            obj[7] = bean.getJob();
            obj[8] = bean.getLevels();
//            obj[9] = bean.getOldOrgId();
            if(bean.getOrg()!=null) {
            	obj[9] = bean.getOrg().getOrgname();
            }else {
            	 obj[9] = "";
            }
           
            obj[10] = this.yyyyMMdd2Str(bean.getJobStartTime());
            obj[11] = this.yyyyMMdd2Str(bean.getJobEndTime());
            //部门职能分类
            obj[12] = bean.getDeptType();
            obj[13] = bean.getDeptHistory();
            obj[14] = bean.getMainDuty();
            obj[15] = bean.getMainPower();
            obj[16] = bean.getBusiness();
            //人员构成
            obj[17] = bean.getPerson();
            obj[18] = bean.getIsSeparateAccount();
            obj[19] = bean.getAssetInfo();
            obj[20] = bean.getMainCost();
            obj[21] = bean.getIncome();
            obj[22] = bean.getCost();
            //可控成本 
            obj[23] = bean.getControllableCost();
            obj[24] = bean.getHasExternalPerson();
            obj[25] = bean.getUnconventJob();
            obj[26] = bean.getIsAudit();
//            obj[27] = bean.getQuarterNum();
//            obj[28] = bean.getQuarterType();
//            obj[28] = bean.getStatus();
           
            objs.add(obj);
        }
//        response.setContentType(ContentType.APPLICATION_OCTET_STREAM.withCharset(StandardCharsets.UTF_8).toString());
//        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("三级单位离任审计表", StandardCharsets.UTF_8.name()) + ".xlsx");
//        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
//        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("三级单位离任审计表".getBytes(), "UTF-8") + ".xlsx");
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
    public JsonBean importData(MultipartFile file, String token, BigDecimal jdid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblStaff tblStaff = new TblStaff();
        tblStaff.setStaffid(user.getStaffid());
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
                obj[k] = ExclUtils.getCellValueForStr(cell);
            }
            objList.add(obj);
        }
        List<LeaveAudit3LEntity> list = new ArrayList<LeaveAudit3LEntity>();
        for (Object[] obj : objList) {
            LeaveAudit3LEntity entity = new LeaveAudit3LEntity();


            entity.setName(toString(obj[1]));
            
            entity.setOldJob(toString(obj[2]));
            entity.setOldLevel(toString(obj[3]));
            if(toString(obj[4])!=null){
            	List<TblOrganization> byname = tblOrganizationMapper.findByname(toString(obj[4]));
            	if(byname!=null && byname.size()>0) {
            		entity.setOldOrgId(byname.get(0).getOrgid().toString());
            		entity.setOldOrg(byname.get(0));
            	}
            }
            
            entity.setOldJobStartTime(this.yyyyMMdd2Date(obj[5]));
            entity.setOldJobEndTime(this.yyyyMMdd2Date(obj[6]));

            entity.setJob(toString(obj[7]));
            entity.setLevels(toString(obj[8]));
//            entity.setOrgId(toString(obj[9]));
            if(toString(obj[9])!=null){
            	List<TblOrganization> byname = tblOrganizationMapper.findByname(toString(obj[9]));
            	if(byname!=null && byname.size()>0) {
            		entity.setOrgId(byname.get(0).getOrgid().toString());
            		entity.setOrg(byname.get(0));
            	}
            }
            entity.setJobStartTime(this.yyyyMMdd2Date(obj[10]));
            entity.setJobEndTime(this.yyyyMMdd2Date(obj[11]));

            entity.setDeptType(toString(obj[12]));
            entity.setDeptHistory(toString(obj[13]));
            entity.setMainDuty(toString(obj[14]));
            entity.setMainPower(toString(obj[15]));
            entity.setBusiness(toString(obj[16]));


            entity.setPerson(toString(obj[17]));
            entity.setIsSeparateAccount(toIntger(obj[18]));
            entity.setAssetInfo(toString(obj[19]));
            entity.setMainCost(toString(obj[20]));
            entity.setIncome(toBigeDecal(obj[21]));
            entity.setCost(toBigeDecal(obj[22]));

            entity.setControllableCost(toBigeDecal(obj[23]));
            entity.setHasExternalPerson(toIntger(obj[24]));
            entity.setUnconventJob(toString(obj[25]));
            entity.setIsAudit(toIntger(obj[26]));
//            entity.setQuarterNum(toIntger(obj[27]));
//            entity.setQuarterType(toIntger(obj[28]));
//            entity.setStatus(toIntger(obj[28]));
           
            entity.setCreateUser(tblStaff);
            entity.setCreateTime(new Date());

            leaveAudit3LMapper.insertEntity(entity);
            list.add(entity);
//            tblYqnsSjdwjdMapper.insertglnr(jdid,  entity.getId().toString());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", list);
        return ResponseFormat.retParam(1, 200,resultMap);
    }


    /**
     * 通过ids查询 三级单位离任审计
     *
     * @param ids
     * @return
     * @throws Exception
     */ 
    @Override
    public List<LeaveAudit3LEntity> findByIds(String ids) {
        // 三级单位离任审计 通过ids
        List<LeaveAudit3LEntity> beanList = leaveAudit3LMapper.findByIds(ids);
        return beanList;
    }


    /**
     * 三级单位离任审计明细
     *
     * @param leaveAudit3LEntity
     * @return
     */
    public List<LeaveAudit3LEntity> selectByEntity(LeaveAudit3LEntity leaveAudit3LEntity, TblStaffUtil user, BigDecimal jdid) {
        // 三级单位离任审计
        List<LeaveAudit3LEntity> leaveAudit3LEntities = leaveAudit3LMapper.selectByEntity(leaveAudit3LEntity, user, jdid);
        return leaveAudit3LEntities;
    }


    /**
     * 获取汇总数据
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean selectLeaveAudit3LSummary(String token, Integer queryYear) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if(queryYear == null) {
        	queryYear = Year.now().getValue();
        }
        List<LeaveAudit3LCountVo> beanList = this.selectLeaveAudit3LSummary(queryYear);
        int sumCounts = beanList.stream()
                .mapToInt(LeaveAudit3LCountVo::getCOUNTS) // 假设存在一个getCOUNTS()方法来访问COUNTS字段
                .sum();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", beanList);
        resultMap.put("sumCounts", sumCounts);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    /**
     * 取三级单位离任审计汇总
     * @param queryYear 
     *
     * @return
     */
    public List<LeaveAudit3LCountVo> selectLeaveAudit3LSummary(Integer queryYear) {
        // 三级单位离任审计 通过ids
        List<LeaveAudit3LCountVo> beanList = leaveAudit3LMapper.selectLeaveAudit3LSummary(queryYear);
        return beanList;
    }


    private String yyyyMMdd2Str(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.formatDate(date);
    }

    private Date yyyyMMdd2Date(Object date) {
        if (date == null || date.toString().equals("")) {
            return null;
        }
        return DateUtil.parse(date.toString()).toJdkDate();
    }

    private String toString(Object object) {
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    private BigDecimal toBigeDecal(Object object) {
        if (object == null) {
            return null;
        }
        if (StringUtils.isEmpty(object.toString())) {
            return null;
        }
        return new BigDecimal(object.toString());
    }

    private Integer toIntger(Object object) {
        if (object == null) {
            return null;
        }
        if (StringUtils.isEmpty(object.toString())) {
            return null;
        }
        if(object.toString().equals("是")) {
        	return 1;
        }
        if(object.toString().equals("否")) {
        	return 0;
        }
        return Integer.parseInt(object.toString());
    }

	@Override
    public JsonBean getDetailDistributeList(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity vo)
            throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit3LMapper.selectDetailDistributeList(vo, user));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

	@Override
	public JsonBean getDistributeReceiveList(String token, Integer pageNumber, Integer pageSize, LeaveAudit3LEntity vo)
			throws Exception {
		TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit3LMapper.selectDistributeReceiveList(vo, user));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean saveDistributionPerson(String token, String idStrs, BigDecimal disFirstPerson,
			BigDecimal disSecondPerson) throws Exception {
		TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        if(disFirstPerson != null) {
        	this.leaveAudit3LMapper.updateDisFirstPersonByIds(disFirstPerson,idStrs);
        }
        
        if(disSecondPerson != null) {
        	this.leaveAudit3LMapper.updateDisSecondPersonByIds(disSecondPerson,idStrs);
        }
        
        
        return ResponseFormat.retParam(1, 200, null);
	}

}
