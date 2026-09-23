package com.huabo.audit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import com.huabo.audit.oracle.mapper.TblYqnsGcjsSettlementMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjMapper;
import com.huabo.audit.service.TblYqnsGcxmzjService;
import com.huabo.audit.util.PageResult;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMZJ(工程项目造价表)】的数据库操作Service实现
 * @createDate 2023-09-07 16:46:40
 */
@Service
public class TblYqnsGcxmzjServiceImpl extends ServiceImpl<TblYqnsGcxmzjMapper, TblYqnsGcxmzj> implements TblYqnsGcxmzjService {

    TblStaffUtil loginStaff;
    
    
    @Resource
    private TblYqnsGcjsSettlementMapper tblYqnsGcjsSettlementMapper;
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzj vo, Integer order) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       /* PageInfo<TblYqnsGcxmzj> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
        //假设 当前操作人所属集团ID = 1
        Integer belongGroup = 1;*/

        //查询 当前操作人的所属集团ID以及子集团所有 集团IDS
        //这个集团数组就是替代原sql
        //belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
        //					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+")
       /* List<Integer> belongGroupList = tblStaffOracleService.getTblOrganizationAll(belongGroup);
        queryParam.setBelongGroupList(belongGroupList);*/

        //查询条件
        QueryWrapper<TblYqnsGcxmzj> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 精准查询
        if (vo.getOrgid() != null) {
            wrapper.lambda().eq(TblYqnsGcxmzj::getOrgid, vo.getOrgid());
        }
        //查询 需要判空 在查询 数组查询
        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getHtbh, vo.getHtbh());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getGcmc())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getGcmc, vo.getGcmc());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getJsdw())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getJsdw, vo.getJsdw());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getSgdw())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getSgdw, vo.getSgdw());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getLxr())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getLxr, vo.getLxr());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getLxdh())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getLxdh, vo.getLxdh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (StringUtils.isNotEmpty(vo.getStartDate())) {
            wrapper.lambda().ge(TblYqnsGcxmzj::getCjsj, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (StringUtils.isNotEmpty(vo.getEndDate())) {
            wrapper.lambda().lt(TblYqnsGcxmzj::getCjsj, vo.getEndDate());
        }
        
        if(StringUtils.isNotEmpty(vo.getCreateYear())) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateYear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateYear()+"-12-31", DateTimeFormatter.ISO_DATE);
            if (StringUtils.isNotBlank(vo.getCreateYear())) {
            	wrapper.lambda().ge(TblYqnsGcxmzj::getCjsj, startlocalDate);
            	wrapper.lambda().lt(TblYqnsGcxmzj::getCjsj, endlocalDate);
            }
        }
        
        if (StringUtils.isNotBlank(loginStaff.getDeptIds())) {
        	wrapper.and(q -> q.eq("CJR", loginStaff.getRealname()).or().inSql("CJR", "SELECT DISTINCT REALNAME FROM TBL_USER_ORGRELATION tuo LEFT JOIN TBL_STAFF ts on ts.STAFFID=tuo.STAFFID WHERE DEPTID IN ("+loginStaff.getDeptIds()+") AND tuo.ORGID = "+loginStaff.getCurrentOrg().getOrgid()));
        }else {
        	wrapper.and(q -> q.eq("CJR", loginStaff.getRealname()));
        }
        
        wrapper.isNull("PARENTID");
        
        //倒序
        if(order == 0) {
        	 wrapper.orderByAsc(true, "SERIALNUMBER");
        }else {
        	 wrapper.orderByDesc(true, "SERIALNUMBER");
        }
        
        wrapper.orderByDesc(true, "cjsj");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsGcxmzj> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> this.baseMapper.selectList(wrapper));
        PageResult<TblYqnsGcxmzj> build = new PageResult<TblYqnsGcxmzj>().build(pageInfo);
        //构建预留字段
        reservePropertyService.buildReserveProperty(build.getTlist());
        //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
        return ResponseFormat.retParam(1, 200, build);
    }

    
    @Override
	public JsonBean getChoiceList(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzj vo)
			throws Exception {
    	JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        //查询条件
        QueryWrapper<TblYqnsGcxmzj> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 精准查询
        if (vo.getOrgid() != null) {
            wrapper.lambda().eq(TblYqnsGcxmzj::getOrgid, vo.getOrgid());
        }
        //查询 需要判空 在查询 数组查询
        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getHtbh, vo.getHtbh());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getGcmc())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getGcmc, vo.getGcmc());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getJsdw())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getJsdw, vo.getJsdw());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getSgdw())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getSgdw, vo.getSgdw());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getLxr())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getLxr, vo.getLxr());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotEmpty(vo.getLxdh())) {
            wrapper.lambda().like(TblYqnsGcxmzj::getLxdh, vo.getLxdh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (StringUtils.isNotEmpty(vo.getStartDate())) {
            wrapper.lambda().ge(TblYqnsGcxmzj::getCjsj, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (StringUtils.isNotEmpty(vo.getEndDate())) {
            wrapper.lambda().lt(TblYqnsGcxmzj::getCjsj, vo.getEndDate());
        }
        
        if(StringUtils.isNotEmpty(vo.getCreateYear())) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateYear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateYear()+"-12-31", DateTimeFormatter.ISO_DATE);
            if (StringUtils.isNotBlank(vo.getCreateYear())) {
            	wrapper.lambda().ge(TblYqnsGcxmzj::getCjsj, startlocalDate);
            	wrapper.lambda().lt(TblYqnsGcxmzj::getCjsj, endlocalDate);
            }
        }
        
        wrapper.notInSql("GCXMZJID", "SELECT GCXMZJID FROM TBL_YQNS_GCXMZJ_ZJB where  GCXMZJID is not NULL ");
        //倒序
        wrapper.orderByDesc(true, "cjsj");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsGcxmzj> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> this.baseMapper.selectList(wrapper));
        PageResult<TblYqnsGcxmzj> build = new PageResult<TblYqnsGcxmzj>().build(pageInfo);
        //构建预留字段
        reservePropertyService.buildReserveProperty(build.getTlist());
        //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
        return ResponseFormat.retParam(1, 200, build);
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
    public JsonBean saveOrUpdate(String token, TblYqnsGcxmzj vo) throws Exception {
//        JsonBean retParam = validToken(token);
//        if (retParam != null) return retParam;
//        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
//        vo.setExt1(loginStaff.getStaffid().toString());
//        if(vo.getGcxmzjid() != null){
//            vo.setGxr(loginStaff.getRealname());
//            vo.setGxsj(new Date());
//        }else{
//            vo.setCjr(loginStaff.getRealname());
//            vo.setCjsj(new Date());
//        }
    	Integer htbh = tblYqnsGcjsSettlementMapper.selectbyHtbh(vo.getHtbh());
    	if(htbh>0) {
    		vo.setXmstatus(1);
    		vo.setXmzttype("工程结算审计");
    	}else {
    		vo.setXmstatus(0);
    	}
    	 if(vo.getGcxmzjid()==null) {
         	vo.setGcxmzjid(RandomUtil.uuBigDecimalId());
         }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
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
    public JsonBean detail(String token, TblYqnsGcxmzj vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsGcxmzj bean = this.getById(vo.getGcxmzjid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsGcxmzj vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmzj vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "合同编号", "工程名称", "建设单位", "二审审查金额", "施工单位", "联系人", "联系电话"};
        PageInfo<TblYqnsGcxmzj> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);
        List<TblYqnsGcxmzj> list;
        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<Object[]>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsGcxmzj gcxmzj : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = gcxmzj.getHtbh();
            obj[2] = gcxmzj.getGcmc();
            obj[3] = gcxmzj.getJsdw();
            obj[4] = gcxmzj.getEsscje();
            obj[5] = gcxmzj.getSgdw();
            obj[6] = gcxmzj.getLxr();
            obj[7] = gcxmzj.getLxdh();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("工程项目造价".getBytes(), "UTF-8") + ".xlsx");
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
        String wz = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
       
        List<Object[]> objList = new ArrayList<>();
        if(wz!=null && (wz.equals("xls") || wz.equals("XLS"))) {
        	Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet s = workbook.getSheetAt(0);
            int lastRowNum = s.getLastRowNum()+1;
            
            for (int i = 1; i < lastRowNum; i++) {
            	Row row=s.getRow(i);
            	 Object[] obj = new Object[row.getLastCellNum()];
            	for (int k = 0; k < row.getLastCellNum(); k++) {
            		Cell  cell = row.getCell(k);
                    cell.setCellType(1);
                    obj[k] = cell.getStringCellValue();
                }
                objList.add(obj);
            }
        }else {
        	XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workBook.getSheetAt(0);
            int lastRowNum = sheet.getPhysicalNumberOfRows();
            
            for (int i = 1; i < lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                int lastCellNum = row.getLastCellNum();
                Object[] obj = new Object[lastCellNum];
                for (int k = 0; k < lastCellNum; k++) {
                    XSSFCell cell = row.getCell(k);
                    cell.setCellType(1);
                    obj[k] = cell.getStringCellValue();
                }
                objList.add(obj);
            }
        }
        
        
        for (Object[] obj : objList) {
            TblYqnsGcxmzj gcxmzj = new TblYqnsGcxmzj();
            gcxmzj.setHtbh(obj[1] == null ? null : obj[1].toString());
            gcxmzj.setGcmc(obj[2] == null ? null : obj[2].toString());
            gcxmzj.setJsdw(obj[3] == null ? null : obj[3].toString());
            gcxmzj.setEsscje(StringUtils.isNotEmpty(obj[4].toString()) ? new BigDecimal(obj[4].toString()) : null);
            gcxmzj.setSgdw(obj[5] == null ? null : obj[5].toString());
            gcxmzj.setLxr(obj[6] == null ? null : obj[6].toString());
            gcxmzj.setLxdh(obj[7] == null ? null : obj[7].toString());
            gcxmzj.setCjr(loginStaff.getRealname());
            if(obj[1]!=null) {
            	Integer htbh = tblYqnsGcjsSettlementMapper.selectbyHtbh(obj[1].toString());
            	if(htbh>0) {
            		gcxmzj.setXmstatus(1);
            		gcxmzj.setXmzttype("工程结算审计");
            	}else {
            		gcxmzj.setXmstatus(0);
            	}
            }else {
            	gcxmzj.setXmstatus(0);
            }
            
            this.baseMapper.insert(gcxmzj);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
}




