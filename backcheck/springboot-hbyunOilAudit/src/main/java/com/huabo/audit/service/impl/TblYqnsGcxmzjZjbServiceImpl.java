package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjZjbMapper;
import com.huabo.audit.service.TblYqnsGcxmzjService;
import com.huabo.audit.service.TblYqnsGcxmzjZjbService;
import com.huabo.audit.util.AutoNo;

import org.apache.commons.lang.StringUtils;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_GCXMZJ_ZJB(工程项目造价中间表)】的数据库操作Service实现
 * @createDate 2023-09-10 20:34:27
 */
@Service
public class TblYqnsGcxmzjZjbServiceImpl extends ServiceImpl<TblYqnsGcxmzjZjbMapper, TblYqnsGcxmzjZjb>
        implements TblYqnsGcxmzjZjbService {

    TblStaffUtil loginStaff;

    @Resource
    TblYqnsGcxmzjService tblYqnsGcxmzjService;

    @Resource
    TblYqnsGcxmzjMapper tblYqnsGcxmzjMapper;
    
    @Resource
    TblYqnsGcxmzjZjbMapper tblYqnsGcxmzjZjbMapper;
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmzjZjb vo) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        vo.setExt1(staff.getStaffid().toString());
        vo.setDeips(staff.getDeptIds());
        PageInfo<TblYqnsGcxmzjZjb> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);

        List<TblYqnsGcxmzjZjb> tblYqnsGcxmzjZjbs = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(tblYqnsGcxmzjZjbs);
        pageInfo.setTlist(tblYqnsGcxmzjZjbs);
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
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
    public JsonBean saveOrUpdate(String token, TblYqnsGcxmzjZjb vo,int type) throws Exception {
    	 TblStaffUtil user = userProvider.get();
         if(user == null) {
             return ResponseFormat.retParam(0,20006,null);
         }
    	 vo.setExt1(user.getStaffid().toString());
         vo.setOrgid(user.getCurrentOrg().getOrgid().longValue());
         if(vo.getGcxmzjzjbid() == null){
        	 
             vo.setCjr(user.getRealname());
             vo.setCjsj(new Date());
             vo.setGcxmzjzjbid(RandomUtil.uuBigDecimalId());
             if((type == 1 && vo.getGcxmzjZjbNo() == null) || type == 0) {
             	Integer currentYear = LocalDate.now().getYear();
                 BigDecimal maxNo = this.tblYqnsGcxmzjZjbMapper.selectMaxAutoNo(currentYear);
                 BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
                 vo.setGcxmzjZjbNo(autoNo);
             }
             this.save(vo);
             TblYqnsGcxmzj gcxmzj = tblYqnsGcxmzjMapper.selectById(vo.getGcxmzjid());
        	 gcxmzj.setEsscjewy(vo.getEsscjewy());
        	 tblYqnsGcxmzjMapper.updateById(gcxmzj);
         }else{
             vo.setGxr(user.getRealname());
             vo.setGxsj(new Date());
             this.updateById(vo);
             TblYqnsGcxmzj gcxmzj = tblYqnsGcxmzjMapper.selectById(vo.getGcxmzjid());
        	 gcxmzj.setEsscjewy(vo.getEsscjewy());
        	 tblYqnsGcxmzjMapper.updateById(gcxmzj);
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
    public JsonBean detail(String token, TblYqnsGcxmzjZjb vo) throws Exception {
    	 TblStaffUtil user = userProvider.get();
         if(user == null) {
             return ResponseFormat.retParam(0,20006,null);
         }
        TblYqnsGcxmzjZjb bean = this.getById(vo.getGcxmzjzjbid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        TblYqnsGcxmzj gcxmzj = this.tblYqnsGcxmzjService.getById(bean.getGcxmzjid());
        bean.setTblYqnsGcxmzj(gcxmzj);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
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
    public JsonBean delete(String token, TblYqnsGcxmzjZjb vo) throws Exception {
    	 TblStaffUtil user = userProvider.get();
         if(user == null) {
             return ResponseFormat.retParam(0,20006,null);
         }
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmzjZjb vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "合同编号", "工程名称", "建设单位", "二审审查金额", "额度","内外部","施工单位", "联系人", "联系电话"};
        List<TblYqnsGcxmzjZjb> list = this.baseMapper.selectListByExport(vo);
        
         List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsGcxmzjZjb bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = bean.getGcxmzjZjbNo();
            obj[1] = bean.getTblYqnsGcxmzj().getHtbh();
            obj[2] = bean.getTblYqnsGcxmzj().getGcmc();
            obj[3] = bean.getTblYqnsGcxmzj().getJsdw();
            obj[4] = bean.getTblYqnsGcxmzj().getEsscje();
            obj[5] = bean.getEdje();
            obj[6] = bean.getNwb();
            obj[7] = bean.getTblYqnsGcxmzj().getSgdw();
            obj[8] = bean.getTblYqnsGcxmzj().getLxr();
            obj[9] = bean.getTblYqnsGcxmzj().getLxdh();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("工程项目造价中间表".getBytes(), "UTF-8") + ".xlsx");
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
    @Transactional
    public JsonBean importData(MultipartFile file, String token, Integer isCover) throws Exception {
    	 TblStaffUtil user = userProvider.get();
         if(user == null) {
             return ResponseFormat.retParam(0,20006,null);
         }
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        List<TblYqnsGcxmzjZjb> result = new ArrayList<>();
        
        TblYqnsGcxmzjZjb gcxmzjZjb = null;
        TblYqnsGcxmzj gcxmzj = null;
        
        TblYqnsGcxmzjZjb preEntity = null;
        List<TblYqnsGcxmzj> pregcxmzj = null;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                gcxmzjZjb = new TblYqnsGcxmzjZjb();
                gcxmzj = new TblYqnsGcxmzj();

                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzjZjb.setGcxmzjZjbNo(StringUtils.isBlank(cell.getStringCellValue())?null:new BigDecimal(cell.getStringCellValue()));
                }
                
                cell = row.getCell(1);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setHtbh(cell.getStringCellValue());
                }else {
                	continue;
                }

                cell = row.getCell(2);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setGcmc(cell.getStringCellValue());
                }

                cell = row.getCell(3);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setJsdw(cell.getStringCellValue());
                }

                cell = row.getCell(4);
                if(cell != null){
                    cell.setCellType(1);
                    String value = cell.getStringCellValue();
                    gcxmzj.setEsscje(StringUtil.isNotEmpty(value)?new BigDecimal(value):new BigDecimal(0));
                    gcxmzjZjb.setEsscjewy(StringUtil.isNotEmpty(value)?new BigDecimal(value):new BigDecimal(0));
                    gcxmzj.setEsscjewy(StringUtil.isNotEmpty(value)?new BigDecimal(value):new BigDecimal(0));
                }

                cell = row.getCell(5);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzjZjb.setEdje(cell.getStringCellValue());
                }

                cell = row.getCell(6);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzjZjb.setNwb(cell.getStringCellValue());
                }

                cell = row.getCell(7);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setSgdw(cell.getStringCellValue());
                }

                cell = row.getCell(8);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setLxr(cell.getStringCellValue());
                }

                cell = row.getCell(9);
                if(cell != null){
                    cell.setCellType(1);
                    gcxmzj.setLxdh(cell.getStringCellValue());
                }
                gcxmzj.setCjr(user.getRealname()+"");
                gcxmzjZjb.setTblYqnsGcxmzj(gcxmzj);
                result.add(gcxmzjZjb);
            }
        }
 
        for (TblYqnsGcxmzjZjb item : result) {
        	pregcxmzj = tblYqnsGcxmzjMapper.selectByHtbh(item.getTblYqnsGcxmzj().getHtbh());
        	if(pregcxmzj == null || pregcxmzj.size() == 0) {
        		item.getTblYqnsGcxmzj().setCjsj(new Date());
        		tblYqnsGcxmzjMapper.insetEntity(item.getTblYqnsGcxmzj());
        		item.setGcxmzjid(item.getTblYqnsGcxmzj().getGcxmzjid());
        	}else {
        		item.setGcxmzjid(pregcxmzj.get(0).getGcxmzjid());
        	}
        	if(item.getGcxmzjZjbNo() != null) {
        		Calendar calendar = Calendar.getInstance();
        		Integer year = calendar.get(Calendar.YEAR);
            	preEntity = this.tblYqnsGcxmzjZjbMapper.selectRequirementNoEntity(item.getGcxmzjZjbNo(),year);
            	 if(preEntity != null) {
            		 if(isCover == 0) {
            			 continue;
            		 }
            		 item.setGcxmzjzjbid(preEntity.getGcxmzjzjbid());
            	 }
            }
        	saveOrUpdate(token, item,1);
		}
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


	@Override
	public JsonBean getAutoNo(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
		
		//获取今年年份
        Integer currentYear = LocalDate.now().getYear();
        BigDecimal maxNo = this.tblYqnsGcxmzjZjbMapper.selectMaxAutoNo(currentYear);
        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
        return ResponseFormat.retParam(1,200,autoNo);
	}
}




