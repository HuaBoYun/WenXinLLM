package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.Header;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Table.Cell;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblYqnsGcxmjgYsjh;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmjgYsjhMapper;
import com.huabo.audit.service.TblYqnsGcxmjgYsjhService;
import com.huabo.audit.util.AutoNo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.ss.usermodel.DataFormatter;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMJG_YSJH(工程项目竣工验收计划)】的数据库操作Service实现
 */
@Service
public class TblYqnsGcxmjgYsjhServiceImpl extends ServiceImpl<TblYqnsGcxmjgYsjhMapper, TblYqnsGcxmjgYsjh>
        implements TblYqnsGcxmjgYsjhService {

    TblStaffUtil loginStaff;
    @Resource
    TblAttachmentMapper attachmentMapper;
    
    @Resource
    TblYqnsGcxmjgYsjhMapper tblYqnsGcxmjgYsjhMapper;

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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo, Integer queryYear) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        PageInfo<TblYqnsGcxmjgYsjh> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        
        if(queryYear != null) {
        	vo.setQueryYear(queryYear);
        }
        
        vo.setCurrentStaffId(user.getStaffid());
        vo.setQueryDeptIds(user.getDeptIds());

        List<TblYqnsGcxmjgYsjh> tblYqnsGcxmjgYsjhs = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(tblYqnsGcxmjgYsjhs);
        pageInfo.setTlist(tblYqnsGcxmjgYsjhs);
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
        return ResponseFormat.retParam(1, 200, pageInfo);
    }

    @Override
	public JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo,
			Integer queryYear) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        PageInfo<TblYqnsGcxmjgYsjh> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        
        if(queryYear != null) {
        	vo.setQueryYear(queryYear);
        }
        
        vo.setCurrentStaffId(user.getStaffid());
        vo.setQueryDeptIds(user.getDeptIds());

        List<TblYqnsGcxmjgYsjh> tblYqnsGcxmjgYsjhs = this.baseMapper.selectListByPageInfoDraftPlan(pageInfo, vo);
        //构建预留字段返回
        reservePropertyService.buildReserveProperty(tblYqnsGcxmjgYsjhs);
        pageInfo.setTlist(tblYqnsGcxmjgYsjhs);
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfoDraftPlan(pageInfo, vo));
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
    public JsonBean saveOrUpdate(String token, TblYqnsGcxmjgYsjh vo, int type) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        if (vo.getGcxmjgysjhid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
            this.updateById(vo);
        } else {
        	vo.setExt1(loginStaff.getStaffid().toString());
            vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
            vo.setGcxmjgysjhid(RandomUtil.uuLongId());
            
            if((type == 1 && vo.getGcxmjgysjhNo() == null) || type == 0) {
            	Integer currentYear = LocalDate.now().getYear();
                BigDecimal maxNo = this.tblYqnsGcxmjgYsjhMapper.selectMaxAutoNo(currentYear);
                BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
                vo.setGcxmjgysjhNo(autoNo);
            }
            
            this.save(vo);
        }
        /*boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }*/
        //this.baseMapper.deleteAttByPk(vo.getGcxmjgysjhid());
        List<String> attIds = vo.getAttIds();
        if(attIds != null){
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getGcxmjgysjhid().toString(), attId);
            }
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
    public JsonBean detail(String token, TblYqnsGcxmjgYsjh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsGcxmjgYsjh bean = this.getById(vo.getGcxmjgysjhid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getGcxmjgysjhid());
        bean.setAttachments(attachments);
        //构建预留字段查询返回
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
    public JsonBean delete(String token, TblYqnsGcxmjgYsjh vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            this.baseMapper.deleteAttByPk(Long.valueOf(id));
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmjgYsjh vo) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        String[] titles = {"序号", "项目名称", "建设单位", "项目类别", "项目总投资(万元)"
                ,"项目投产时间","生产考核完成时间","消防设施验收-计划完成时间","环境保护验收-计划完成时间",
                "安全设施验收-计划完成时间","职业病防护设施验收-计划完成时间"
                ,"水土保持设施验收-计划完成时间","土地利用验收-计划完成时间"
        ,"节能验收-计划完成时间","安全防范系统验收-计划完成时间","雷电防护装置验收-计划完成时间"
        ,"档案验收-计划完成时间","竣工决算验收-上报审计时间","项目结算验收-计划完成时间","初步验收-计划完成时间"
        ,"竣工验收-计划完成时间","备注"};
        vo.setCurrentStaffId(user.getStaffid());
        vo.setQueryDeptIds(user.getDeptIds());
        List<TblYqnsGcxmjgYsjh> list = this.baseMapper.selectListByExport(vo);
        List<Object[]> objs = new ArrayList<Object[]>();
        for (TblYqnsGcxmjgYsjh bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = bean.getGcxmjgysjhNo();
            //项目名称
            obj[1] = bean.getXmmc();
            //建设单位
            obj[2] = bean.getJsdw();
            //项目类别 二类项目|三类项目
            obj[3] = bean.getXmlb();
            //项目总投资金额
            obj[4] = bean.getXmztzje();
            obj[5] = this.yyyyMMdd2Str(bean.getXmtcsj());
            obj[6] = this.yyyyMMdd2Str(bean.getSckhwcsj());
            obj[7] = this.yyyyMMdd2Str(bean.getXfssysjhwcsj());
            obj[8] = this.yyyyMMdd2Str(bean.getHjbhysjhwcsj());
            obj[9] = this.yyyyMMdd2Str(bean.getAqssysjhwcsj());
            obj[10] = this.yyyyMMdd2Str(bean.getZybfhssysjhwcsj());
            obj[11] = this.yyyyMMdd2Str(bean.getStbcssysjhwcsj());
            obj[12] = this.yyyyMMdd2Str(bean.getTdlyysjhwcsj());
            obj[13] = this.yyyyMMdd2Str(bean.getJnysjhwcsj());
            obj[14] = this.yyyyMMdd2Str(bean.getAqffxtysjhwcsj());
            obj[15] = this.yyyyMMdd2Str(bean.getLdfhzzysjhwcsj());
            obj[16] = this.yyyyMMdd2Str(bean.getDaysjhwcsj());
            obj[17] = this.yyyyMMdd2Str(bean.getJgjsyssbsjsj());
            obj[18] = this.yyyyMMdd2Str(bean.getXmjsysjhwcsj());
            obj[19] = this.yyyyMMdd2Str(bean.getCbysjhwcsj());
            obj[20] = this.yyyyMMdd2Str(bean.getJgysjhwcsj());
            obj[21] = bean.getBz();
            objs.add(obj);
        }
        response.setContentType(ContentType.APPLICATION_OCTET_STREAM.withCharset(StandardCharsets.UTF_8).toString());
        response.setHeader("Content-Disposition", "attachment; filename=" +  URLEncoder.encode("工程项目竣工验收计划", StandardCharsets.UTF_8.name()) + ".xlsx");
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
    public JsonBean importData(MultipartFile file, String token, Integer isCover) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        List<Object[]> objList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Object[] obj = new Object[lastCellNum];
            for (int k = 0; k < lastCellNum; k++) {
                XSSFCell cell = row.getCell(k);
                if(k > 4) {
                	obj[k] = dataFormatter.formatCellValue(cell);
                }else {
                	cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
                    obj[k] = cell.getStringCellValue();
                }
            }
            objList.add(obj);
        }
        //6/14/24
        TblYqnsGcxmjgYsjh gcxmzj = null;
        TblYqnsGcxmjgYsjh preEntity = null;
        for (Object[] obj : objList) {
            gcxmzj = new TblYqnsGcxmjgYsjh();
            gcxmzj.setGcxmjgysjhNo(obj[0] == null ? null : new BigDecimal(obj[0].toString()));
            gcxmzj.setXmmc(obj[1] == null ? null : obj[1].toString());
            gcxmzj.setJsdw(obj[2] == null ? null : obj[2].toString());
            gcxmzj.setXmlb(obj[3] == null ? null : obj[3].toString());
            gcxmzj.setXmztzje(obj[4] == null ? null : new BigDecimal(obj[4].toString()));
            System.out.println(obj[5].toString());
            gcxmzj.setXmtcsj(obj[5]==null?null:com.hbfk.util.DateUtil.formatDate(obj[5].toString(), "M/dd/yy"));
            gcxmzj.setSckhwcsj(obj[6]==null?null:com.hbfk.util.DateUtil.formatDate(obj[6].toString(), "M/dd/yy"));
            gcxmzj.setXfssysjhwcsj(obj[7]==null?null:com.hbfk.util.DateUtil.formatDate(obj[7].toString(), "M/dd/yy"));
            gcxmzj.setHjbhysjhwcsj(obj[8]==null?null:com.hbfk.util.DateUtil.formatDate(obj[8].toString(), "M/dd/yy"));
            gcxmzj.setAqssysjhwcsj(obj[9]==null?null:com.hbfk.util.DateUtil.formatDate(obj[9].toString(), "M/dd/yy"));
            gcxmzj.setZybfhssysjhwcsj(obj[10]==null?null:com.hbfk.util.DateUtil.formatDate(obj[10].toString(), "M/dd/yy"));
            gcxmzj.setStbcssysjhwcsj(obj[11]==null?null:com.hbfk.util.DateUtil.formatDate(obj[11].toString(), "M/dd/yy"));
            gcxmzj.setTdlyysjhwcsj(obj[12]==null?null:com.hbfk.util.DateUtil.formatDate(obj[12].toString(), "M/dd/yy"));
            gcxmzj.setJnysjhwcsj(obj[13]==null?null:com.hbfk.util.DateUtil.formatDate(obj[13].toString(), "M/dd/yy"));
            gcxmzj.setAqffxtysjhwcsj(obj[14]==null?null:com.hbfk.util.DateUtil.formatDate(obj[14].toString(), "M/dd/yy"));
            gcxmzj.setLdfhzzysjhwcsj(obj[15]==null?null:com.hbfk.util.DateUtil.formatDate(obj[15].toString(), "M/dd/yy"));
            gcxmzj.setDaysjhwcsj(obj[16]==null?null:com.hbfk.util.DateUtil.formatDate(obj[16].toString(), "M/dd/yy"));
            gcxmzj.setJgjsyssbsjsj(obj[17]==null?null:com.hbfk.util.DateUtil.formatDate(obj[17].toString(), "M/dd/yy"));
            gcxmzj.setXmjsysjhwcsj(obj[18]==null?null:com.hbfk.util.DateUtil.formatDate(obj[18].toString(), "M/dd/yy"));
            gcxmzj.setCbysjhwcsj(obj[19]==null?null:com.hbfk.util.DateUtil.formatDate(obj[19].toString(), "M/dd/yy"));
            gcxmzj.setJgysjhwcsj(obj[20]==null?null:com.hbfk.util.DateUtil.formatDate(obj[20].toString(), "M/dd/yy"));
            gcxmzj.setBz(obj[21].toString());
            
            if(gcxmzj.getGcxmjgysjhNo() != null) {
            	preEntity = this.tblYqnsGcxmjgYsjhMapper.selectEntityByNo(gcxmzj.getGcxmjgysjhNo());
            	 if(preEntity != null) {
            		 if(isCover == 0) {
            			 continue;
            		 }
            		 gcxmzj.setGcxmjgysjhid(preEntity.getGcxmjgysjhid());
            	 }
            }
            this.saveOrUpdate(token,gcxmzj,1);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    private String yyyyMMdd2Str(Date date){
        if(date == null){
            return "";
        }
        return DateUtil.formatDate(date);
    }

    private Date yyyyMMdd2Date(Object date){
        if(date == null || date.toString().equals("")){
            return null;
        }
        return DateUtil.parse(date.toString()).toJdkDate();
    }

    @Override
    public List<TblYqnsGcxmjgYsjh> findByIds(String ids)   {
        // 验证token
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        // 查询工程审计类型
        List<TblYqnsGcxmjgYsjh> beanList = this.listByIds(idList);
        return beanList;
    }


	@Override
	public JsonBean getAutoNo(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
		
		//获取今年年份
        Integer currentYear = LocalDate.now().getYear();
        BigDecimal maxNo = this.tblYqnsGcxmjgYsjhMapper.selectMaxAutoNo(currentYear);
        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
        return ResponseFormat.retParam(1,200,autoNo);
	}
	
	
	   @Override
		public JsonBean getxzListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo,
				Integer queryYear) throws Exception {
	    	TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
	        PageInfo<TblYqnsGcxmjgYsjh> pageInfo = new PageInfo<>();
	        pageInfo.setPageSize(pageSize);
	        pageInfo.setCurrentPage(pageNumber);
	        
	        if(queryYear != null) {
	        	vo.setQueryYear(queryYear);
	        }
	        
//	        vo.setCurrentStaffId(user.getStaffid());
//	        vo.setQueryDeptIds(user.getDeptIds());
           List<TblYqnsGcxmjgYsjh> tblYqnsGcxmjgYsjhs = this.baseMapper.selectListByPageInfo(pageInfo, vo);
           //构建预留字段返回
           reservePropertyService.buildReserveProperty(tblYqnsGcxmjgYsjhs);
           pageInfo.setTlist(tblYqnsGcxmjgYsjhs);
           pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
           return ResponseFormat.retParam(1, 200, pageInfo);
		}
	    
}




