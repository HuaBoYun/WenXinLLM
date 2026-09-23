package com.huabo.finance.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ByteArrayMultipartFile;
import com.hbfk.util.DateUtil;
import com.hbfk.util.FileNameEncoderUtil;
import com.hbfk.util.FinancialDataExport;
import com.hbfk.util.JsonBean;
import com.hbfk.util.NumberParseUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.TblAttachmentUtil;
import com.huabo.finance.entity.caiji.BdFinanceAccass;
import com.huabo.finance.entity.caiji.GlAssBalane;
import com.huabo.finance.entity.caiji.GlBalance;
import com.huabo.finance.entity.caiji.GlDetail;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.BdAccountMapper;
import com.huabo.finance.mapper.BdFinanceAccassMapper;
import com.huabo.finance.mapper.GlAssBalaneMapper;
import com.huabo.finance.mapper.GlBalanceMapper;
import com.huabo.finance.mapper.GlDetailMapper;
import com.huabo.finance.mapper.GlVoucherMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.ExportFileService;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlDetailVo;
import com.huabo.finance.vr.BdAccountVr;
import com.huabo.finance.vr.GlAssBalaneVr;
import com.huabo.finance.vr.GlBalanceVr;
import com.huabo.finance.vr.GlDetailVr;

/**
 * 会计科目表 - 服务接口实现
 *
 * @author lee
 * @version 1.0.0
 **/
@Service
public class ExportFileServiceImpl implements ExportFileService {
	
	@Value("${fileUploadPath}")
    private String fileUploadPath;
	
	@Resource
	private BdAccountMapper bdAccountMapper;
	
	@Resource
	private GlDetailMapper glDetailMapper;
	
	@Resource
	private GlBalanceMapper glBalanceMapper;
	
	@Resource
	private GlVoucherMapper glVoucherMapper;
	
	@Resource
	private BdFinanceAccassMapper bdFinanceAccassMapper;
	
	@Resource
	private GlAssBalaneMapper glAssBalaneMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;
	
	/**
	 * 导出选中信息为Excel
	 * @param token
	 * @param exportRequestVo
	 * @return
	 */
	@Override
	public JsonBean exportFileFunc(String token, ExportRequestVo exportRequestVo, TblStaffUtil staff, HttpServletResponse response, HttpServletRequest request) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		HSSFWorkbook workBook = null;
		String name ;
		
		if(StringUtils.isBlank(exportRequestVo.getPkOrg())) {
			String org = this.orgOrgsMapper.selectFinanceOrgIdByOrgId(staff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(org)) {
				return ResponseFormat.retParam(0, "请选择财务组织！", null);
			}
			exportRequestVo.setPkOrg(org);
		}
		
		//通用参数 1-导出科目表,2-导出日记账,3-凭证库,4-明细分类账、5-科目余额表、6-总分类账、7-辅助信息表、8-辅助余额表、9-辅助总账
		switch (exportRequestVo.getExprotType()) {
			case 1:
				workBook = this.exportAccountFile(exportRequestVo);
				name = "科目表";
				break;
			case 2:
				name = "日记账";
				workBook = this.exportRjzFile(exportRequestVo,bookInfo); 
				break;
			case 3:
				name = "凭证库";
				workBook = this.exportPzkFile(exportRequestVo); 
				break; 
			case 4:
				name = "明细分类账";
				workBook = this.exportMxflzFile(exportRequestVo); 
				break;
			case 5:
				name = "科目余额表";
				workBook = this.exportKmyebFile(exportRequestVo); 
				break; 
			case 6: 
				name = "总分类账";
				workBook = this.exportZflzbFile(exportRequestVo); 
				break; 
			 case 7: 
				 name = "辅助信息表";
				 workBook = this.exportFzxxzFile(exportRequestVo); 
				break; 
			 case 8:
				 name = "辅助余额表";
				 workBook = this.exportFzyebFile(exportRequestVo); 
				break; 
			 case 9:
				 name = "辅助总账";
				 workBook = this.exportFzzzbFile(exportRequestVo); 
				 break;
			default:
				name = "无";
				break;
		}
		name += ".xlsx";
		TblAttachment attachment = new TblAttachment();
		if("1".equals(exportRequestVo.getIsExport())) {
			 //response.setContentType("application/vnd.ms-excel");
	         response.setHeader("Content-Disposition", "attachment; filename=\"" + FileNameEncoderUtil.encodeFileName(name, request) + "\"");
	         response.setCharacterEncoding("UTF-8");
             // 写入响应流
	         workBook.write(response.getOutputStream());
	         response.getOutputStream().flush();
	         workBook.close();
		}else {
			try {
				long timeInMillis = Calendar.getInstance().getTimeInMillis();
				String oldname = name.substring(0,name.lastIndexOf("."));
				String newname=name.replace(oldname, timeInMillis+"");
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workBook.write(outputStream);
	            byte[] bytes = outputStream.toByteArray();
				MultipartFile file = new ByteArrayMultipartFile(bytes,"file", name, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");  
				System.out.println("开始调用附件方法****************");
				attachment=TblAttachmentUtil.fileUpload(file, false, null, null);
				if(attachment!=null){
					attachment.setUploader(staff.getRealname());
					bdAccountMapper.insertAttchment(attachment);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return ResponseFormat.retParam(1, 200, attachment);
	}
	
	
	/**
     * 使用 multipart/form-data 上传文件
     */
    public String uploadFileWithMultipart(byte[] fileBytes, String fileName, String targetUrl,String token) {
        try {
            // 创建 ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };
            
            // 创建 multipart 请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            body.add("fileName", fileName);
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("token", token);
            
            // 创建请求实体
            HttpEntity<MultiValueMap<String, Object>> requestEntity = 
                new HttpEntity<>(body, headers);
            
            // 发送请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(
                targetUrl, requestEntity, String.class);
            
            return response.getBody();
            
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }
	
	
	
	private HSSFWorkbook exportFzzzbFile(ExportRequestVo exportRequestVo) throws Exception {
		List<GlAssBalaneVr> list = this.glAssBalaneMapper.exportAccAssGeneralLedgerList(exportRequestVo);
		
		String[] cNames =new String[]{"科目名称","辅助名称","期间","摘要","借方","贷方","余额-方向","余额"};
		int[] cWidths = new int[]{7000, 9000,3000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"辅助总账","辅助总账"};
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		GlAssBalane yearBalance = null;//年初数
		
		GlAssBalane preBalance = null;//上月余额信息
		
		QueryWrapper<GlAssBalane> wrapper = new QueryWrapper<GlAssBalane>();
		
		BigDecimal balance = null;
		
		BigDecimal beginBalance = null;
		
		String minPeriod = null;//最小会计期间
		
		
		for (GlAssBalaneVr vr : list) {
			minPeriod = this.glAssBalaneMapper.selectMinPeriod(vr);
			
			//查询年初数
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccasoa());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", minPeriod);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			yearBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(yearBalance == null) {
				yearBalance = new GlAssBalane();
				yearBalance.setBeginbalancemount(BigDecimal.valueOf(0));
			}
			
			
			//查询当前记录上一个月的余额信息，可能为null 
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccass());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", StringUtils.isNotBlank(vr.getPeriod())?Integer.parseInt(vr.getPeriod())-1:0);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			preBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(preBalance == null) {
				preBalance = new GlAssBalane();
				preBalance.setYearcreditmount(BigDecimal.valueOf(0));
				preBalance.setYeardebitamount(BigDecimal.valueOf(0));
			}
			
			
			//计算期末方向 根据科目借贷方向  以及辅助余额本年累计 计算
			if(vr.getBalanorient() == 0) {
				//借方
				balance = yearBalance.getBeginbalancemount().add(vr.getYeardebitamount()).subtract(vr.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYeardebitamount()).subtract(preBalance.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方
				balance = yearBalance.getBeginbalancemount().add(vr.getYearcreditmount()).subtract(vr.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYearcreditmount()).subtract(preBalance.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			obj = new Object[24];
			obj[0] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[1] = StringUtils.isNotBlank(vr.getAssname())?vr.getAssname():" ";
			obj[2] = vr.getPeriod();
			obj[3] = "期初余额";
			obj[4] = 0==vr.getBeginBalanorient()?vr.getBeginbalancemount()!=null?NumberParseUtil.getCurrency(vr.getBeginbalancemount().toString()):"0.00":"0.00";
			obj[5] = 1==vr.getBeginBalanorient()?vr.getBeginbalancemount()!=null?NumberParseUtil.getCurrency(vr.getBeginbalancemount().toString()):"0.00":"0.00";
			obj[6] = 0==vr.getBeginBalanorient()?"借":"贷";
			obj[7] = vr.getBeginbalancemount()!=null?NumberParseUtil.getCurrency(vr.getBeginbalancemount().toString()):"0.00";
			obj[8] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[9] = StringUtils.isNotBlank(vr.getAssname())?vr.getAssname():" ";
			obj[10] = vr.getPeriod();
			obj[11] = "本期合计";
			obj[12] = vr.getDebitamount()!=null?NumberParseUtil.getCurrency(vr.getDebitamount().toString()):"0.00";
			obj[13] = vr.getCreditmount()!=null?NumberParseUtil.getCurrency(vr.getCreditmount().toString()):"0.00";
			obj[14] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[15] = vr.getEndbalancemount()!=null?NumberParseUtil.getCurrency(vr.getEndbalancemount().toString()):"0.00";
			obj[16] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[17] = StringUtils.isNotBlank(vr.getAssname())?vr.getAssname():" ";
			obj[18] = vr.getPeriod();
			obj[19] = "本年累计";
			obj[20] = vr.getYeardebitamount()!=null?NumberParseUtil.getCurrency(vr.getYeardebitamount().toString()):"0.00";
			obj[21] = vr.getYearcreditmount()!=null?NumberParseUtil.getCurrency(vr.getYearcreditmount().toString()):"0.00";
			obj[22] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[23] = vr.getEndbalancemount()!=null?NumberParseUtil.getCurrency(vr.getEndbalancemount().toString()):"0.00";
			objList.add(obj);
		}
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,FinancialDataExport.ZONGZHANG);
		return workBook;
	}

	private HSSFWorkbook exportFzyebFile(ExportRequestVo exportRequestVo) throws Exception {
		List<GlAssBalaneVr> list = this.glAssBalaneMapper.exportAccAssBalanceList(exportRequestVo);
		
		
		String[] cNames = new String[]{"辅助编号","辅助名称","期初余额-方向","期初余额-金额","本期发生-借方 ","本期发生-贷方","期末余额-方向","期末余额-金额"};
		int[] cWidths = new int[]{8000, 9000,3500,3500,3500,3500,3500,3500};
		String[] names = new String[]{"辅助余额表","辅助余额表"};
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		
		GlAssBalane yearBalance = null;//年初数
		
		GlAssBalane preBalance = null;//上月余额信息
		
		QueryWrapper<GlAssBalane> wrapper = new QueryWrapper<GlAssBalane>();
		
		BigDecimal balance = null;
		
		BigDecimal beginBalance = null;
		
		String minPeriod = null;//最小会计期间
		
		
		for (GlAssBalaneVr vr : list) {
			minPeriod = this.glAssBalaneMapper.selectMinPeriod(vr);
			
			//查询年初数
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccasoa());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", minPeriod);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			yearBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(yearBalance == null) {
				yearBalance = new GlAssBalane();
				yearBalance.setBeginbalancemount(BigDecimal.valueOf(0));
			}
			
			
			//查询当前记录上一个月的余额信息，可能为null 
			wrapper.clear();
			wrapper.eq("PK_ORG", vr.getPkOrg());
			wrapper.eq("PK_ACCASOA", vr.getPkAccass());
			wrapper.eq("PK_ACCASS", vr.getPkAccass());
			wrapper.eq("YEAR", vr.getYear());
			wrapper.eq("PERIOD", StringUtils.isNotBlank(vr.getPeriod())?Integer.parseInt(vr.getPeriod())-1:0);
			wrapper.eq("DATAORIGINFLAG", -2);
			wrapper.eq("FPLANID", vr.getFplanid());
			preBalance = this.glAssBalaneMapper.selectOne(wrapper);
			
			if(preBalance == null) {
				preBalance = new GlAssBalane();
				preBalance.setYearcreditmount(BigDecimal.valueOf(0));
				preBalance.setYeardebitamount(BigDecimal.valueOf(0));
			}
			
			
			//计算期末方向 根据科目借贷方向  以及辅助余额本年累计 计算
			if(vr.getBalanorient() == 0) {
				//借方
				balance = yearBalance.getBeginbalancemount().add(vr.getYeardebitamount()).subtract(vr.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYeardebitamount()).subtract(preBalance.getYearcreditmount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方
				balance = yearBalance.getBeginbalancemount().add(vr.getYearcreditmount()).subtract(vr.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
				//计算期初方向
				beginBalance = yearBalance.getBeginbalancemount().add(preBalance.getYearcreditmount()).subtract(preBalance.getYeardebitamount());
				if(balance.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			obj = new Object[8];
			obj[0] = StringUtils.isNotBlank(vr.getPkAssbalance())?vr.getPkAssbalance():" ";
			obj[1] = StringUtils.isNotBlank(vr.getAssname())?vr.getAssname():" ";
			obj[2] = 0==vr.getBeginBalanorient()?"借":"贷";
			obj[3] = vr.getBeginbalancemount()!=null?NumberParseUtil.getCurrency(vr.getBeginbalancemount().toString()):"0.00";
			obj[4] = vr.getDebitamount()!=null?NumberParseUtil.getCurrency(vr.getDebitamount().toString()):"0.00";
			obj[5] = vr.getCreditmount()!=null?NumberParseUtil.getCurrency(vr.getCreditmount().toString()):"0.00";
			obj[6] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[7] = vr.getEndbalancemount()!=null?NumberParseUtil.getCurrency(vr.getEndbalancemount().toString()):"0.00";
			objList.add(obj);
		}
		
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

	private HSSFWorkbook exportFzxxzFile(ExportRequestVo exportRequestVo) throws Exception {
		
		List<BdFinanceAccass> list = this.bdFinanceAccassMapper.exportAccAssList(exportRequestVo);
		String[] cNames = new String[]{"辅助类型","辅助编码","辅助名称","辅助级别","辅助描述"};
		int[] cWidths = new int[]{9000, 7000,9000,6000,9000};
		String[] names = new String[]{"辅助信息表","辅助信息表"};
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		for (BdFinanceAccass ass : list) {
			obj = new Object[5];
			obj[0] = StringUtils.isNotBlank(ass.getAsstype())?ass.getAsstype():" ";
			obj[1] = StringUtils.isNotBlank(ass.getPkBunessies())?ass.getPkBunessies():" ";
			obj[2] = StringUtils.isNotBlank(ass.getAssname())?ass.getAssname():" ";
			obj[3] = StringUtils.isNotBlank(ass.getAsslevel())?ass.getAsslevel():" ";
			obj[4] = StringUtils.isNotBlank(ass.getAssdes())?ass.getAssdes():" ";
			objList.add(obj);
		}
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

	private HSSFWorkbook exportZflzbFile(ExportRequestVo exportRequestVo) throws Exception {
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		List<GlBalanceVr> list = this.glBalanceMapper.exportSumTotalList(exportRequestVo);
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		
		String minPeriod = null;//最小会计期间
		for (GlBalanceVr vr : list) {
			minPeriod = this.glBalanceMapper.selectMinPeriod(vr);
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", minPeriod);
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriod())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(vr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(vr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(vr.getLocaldebitamount()).subtract(vr.getLocalcreditamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
			}
			if(vr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(vr.getLocalcreditamount()).subtract(vr.getLocaldebitamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
			}
			vr.setFbeginBalanceLocal(monthMOney);
			vr.setFendBalanceLocal(endMonthMoney);
			
			obj = new Object[24];
			obj[0] = StringUtils.isNotBlank(vr.getCode())?vr.getCode():" ";
			obj[1] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[2] = vr.getPeriod();
			obj[3] = "期初余额";
			obj[4] = 0.00;
			obj[5] = 0.00;
			obj[6] = 0==vr.getBeginBalanorient()?"借":"贷";
			obj[7] = vr.getFbeginBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getFbeginBalanceLocal().toString()):"0.00";
			obj[8] = StringUtils.isNotBlank(vr.getCode())?vr.getCode():" ";
			obj[9] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[10] = vr.getPeriod();
			obj[11] = "本期合计";
			obj[12] = vr.getLocaldebitamount()!=null?NumberParseUtil.getCurrency(vr.getLocaldebitamount().toString()):"0.00";
			obj[13] = vr.getLocalcreditamount()!=null?NumberParseUtil.getCurrency(vr.getLocalcreditamount().toString()):"0.00";
			obj[14] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[15] = vr.getFendBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getFendBalanceLocal().toString()):"0.00";
			obj[16] = StringUtils.isNotBlank(vr.getCode())?vr.getCode():" ";
			obj[17] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[18] = vr.getPeriod();
			obj[19] = "本年累积";
			obj[20] = vr.getFyearDeditLocal()!=null?NumberParseUtil.getCurrency(vr.getFyearDeditLocal().toString()):"0.00";
			obj[21] = vr.getFyearCreditLocal()!=null?NumberParseUtil.getCurrency(vr.getFyearCreditLocal().toString()):"0.00";
			obj[22] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[23] = vr.getFendBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getFendBalanceLocal().toString()):"0.00";
			objList.add(obj);
		}
		
		String[] cNames = new String[]{"科目代码","科目名称","期间","摘要","借方","贷方","余额-方向","余额"};
		int[] cWidths = new int[]{3500, 9000,3500,3500,3500,3500,3500,3500};
		String[] names = new String[]{"总分类账","总分类账"};
		
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,FinancialDataExport.ZONGZHANG);
		return workBook;
	}

	private HSSFWorkbook exportKmyebFile(ExportRequestVo exportRequestVo) throws Exception {
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		List<GlBalanceVr> list = this.glBalanceMapper.exportSumTotalList(exportRequestVo);
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		String minPeriod = null;//最小会计期间
		for (GlBalanceVr vr : list) {
			minPeriod = this.glBalanceMapper.selectMinPeriod(vr);
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", minPeriod);
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYear());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriod())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(vr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(1);
				}else {
					vr.setBeginBalanorient(0);
				}
			}
			
			if(vr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setBeginBalanorient(0);
				}else {
					vr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(vr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(vr.getLocaldebitamount()).subtract(vr.getLocalcreditamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(1);
				}else {
					vr.setEndBalanorient(0);
				}
			}
			if(vr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(vr.getLocalcreditamount()).subtract(vr.getLocaldebitamount());
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					vr.setEndBalanorient(0);
				}else {
					vr.setEndBalanorient(1);
				}
			}
			vr.setFbeginBalanceLocal(monthMOney);
			vr.setFendBalanceLocal(endMonthMoney);
			
			obj = new Object[11];
			obj[0] = StringUtils.isNotBlank(vr.getCode())?vr.getCode():" ";
			obj[1] = StringUtils.isNotBlank(vr.getName())?vr.getName():" ";
			obj[2] = vr.getPeriod();
			obj[3] = 0==vr.getBeginBalanorient()?"借":"贷";
			obj[4] = vr.getFbeginBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getFbeginBalanceLocal().toString()):"0.00";
			obj[5] = vr.getLocaldebitamount()!=null?NumberParseUtil.getCurrency(vr.getLocaldebitamount().toString()):"0.00";
			obj[6] = vr.getLocalcreditamount()!=null?NumberParseUtil.getCurrency(vr.getLocalcreditamount().toString()):"0.00";
			obj[7] = vr.getFyearDeditLocal()!=null?NumberParseUtil.getCurrency(vr.getFyearDeditLocal().toString()):"0.00";
			obj[8] = vr.getFyearCreditLocal()!=null?NumberParseUtil.getCurrency(vr.getFyearCreditLocal().toString()):"0.00";
			obj[9] = 0==vr.getEndBalanorient()?"借":"贷";
			obj[10] = vr.getFendBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getFendBalanceLocal().toString()):"0.00";
			objList.add(obj);
		}
		
		String[] cNames = new String[]{ "科目编码", "科目名称","期间", "期初余额-方向","期初余额-金额","本期发生-借方","本期发生-贷方","累计发生-借方","累计发生-贷方","期末余额-方向","期末余额-金额"};
		int[] cWidths = new int[]{ 3000, 5000,4000,4000,4000,4000,4000,4000,4000,4000,4000};
		String[] names = new String[]{"科目余额表","科目余额表"};
		
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

	private HSSFWorkbook exportMxflzFile(ExportRequestVo exportRequestVo) throws Exception {
		
		List<GlDetailVr> pageList = this.glDetailMapper.exportMxflzList(exportRequestVo);
		
		
		String[] names = new String[]{"明细分类账","明细分类账"};
		
        GlBalance yearb = null;//每个科目每年的期初余额
		
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		
		GlDetail sumPreDetail = null;//当前凭证之前的数据
		
		String pkAccount = "";//当前循环的科目编号
		
		GlBalanceVr currentBr = null;//当前余额信息
		BigDecimal sumDmoney = null;//本期借方累计发生额
		BigDecimal sumCmoney = null;//本期贷方累计发生额
		
		BigDecimal yearmoney = null;//年初借方金额
		
		BigDecimal monthMOney = null;//本月月初金额
		BigDecimal endMonthMoney = null;//本月月末金额
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
        Object[] objs = null;
		
		for (GlDetailVr vr : pageList) {
			
			if(StringUtils.isBlank(pkAccount) || !vr.getPkAccasoa().equals(pkAccount)) {
				pkAccount = vr.getPkAccasoa();
				ycw.clear();
				ycw.eq("DATAORIGINFLAG", -2 );
				ycw.eq("PK_ORG", vr.getPkOrg());
				ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
				ycw.eq("YEAR", vr.getYearv());
				ycw.eq("PERIOD", "1");
				yearb = this.glBalanceMapper.selectOne(ycw);
				if(yearb == null) {
					yearmoney = new BigDecimal(0);
				}else {
					yearmoney = yearb.getFbeginBalanceLocal();
				}
			}
				
			//查找当前科目在  本月份之前   的   明细   发 生额总额
				sumPreDetail = this.glDetailMapper.selectPreDetailSum(vr);
				sumDmoney = sumPreDetail.getDebitamount();
				sumCmoney = sumPreDetail.getCreditamount();
				
			currentBr = vr.getGlBalanceVr();
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriodv())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(currentBr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				monthMOney = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setBeginBalanorient(1);
				}else {
					currentBr.setBeginBalanorient(0);
				}
			}
			
			if(currentBr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				monthMOney = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(monthMOney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setBeginBalanorient(0);
				}else {
					currentBr.setBeginBalanorient(1);
				}
			}
			
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(currentBr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				endMonthMoney = monthMOney.add(sumDmoney).subtract(sumCmoney);
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
				}
			}
			if(currentBr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				endMonthMoney = monthMOney.add(sumCmoney).subtract(sumDmoney);
				if(endMonthMoney.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
				}
			}
			currentBr.setFbeginBalanceLocal(monthMOney);
			currentBr.setFendBalanceLocal(endMonthMoney);
			vr.setGlBalanceVr(currentBr);
			
			objs = new Object[9];
        	objs[0] = vr.getPrepareddatev()!=null?DateUtil.parseDate(vr.getPrepareddatev(),DateUtil.DATE_SMALL_STR):"";
        	objs[1] = vr.getGvnum()!=null?vr.getGvnum():"";
        	objs[2] = StringUtils.isNotBlank(vr.getExplanation())?vr.getExplanation():"";
        	objs[3] = StringUtils.isNotBlank(vr.getGlBalanceVr().getName())?vr.getGlBalanceVr().getName():"";
        	objs[4] = StringUtils.isNotBlank(vr.getGlBalanceVr().getCode())?vr.getGlBalanceVr().getCode():"";
        	objs[5] = vr.getDebitamount()!=null?NumberParseUtil.getCurrency(vr.getDebitamount().toString()):"0.00";
        	objs[6] = vr.getCreditamount()!=null?NumberParseUtil.getCurrency(vr.getCreditamount().toString()):"0.00";
        	objs[7] = 0 == vr.getGlBalanceVr().getEndBalanorient()?"借":"贷";
        	objs[8] = vr.getGlBalanceVr().getFendBalanceLocal()!=null?NumberParseUtil.getCurrency(vr.getGlBalanceVr().getFendBalanceLocal().toString()):"0.00";
        	objList.add(objs);
		}
		
		String[] cNames = new String[]{ "日期","凭证号", "摘要", "科目名称","科目编号","借方金额","贷方金额","余额-方向","余额"};
		int[] cWidths = new int[]{ 3500, 3500, 7000,3500,3500,3500,3500,3500,3500};
		
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

	private HSSFWorkbook exportPzkFile(ExportRequestVo exportRequestVo) throws Exception {
		
		List<GlVoucher> pageList = this.glVoucherMapper.exportVoucherList(exportRequestVo);
		
		String[] cNames = new String[]{"凭证日期","凭证号","会计期间","摘要","凭证类别","年份","出纳人员","审核人员","主管人员","签字日期","总贷方金额","总贷方金额（本位币）","总贷方金额（集团）","总借方金额","总借方金额（本位币）","总借方金额（集团）"};
		int[] cWidths = new int[]{3000,3000,3000,8000,3000,3000,3000,3000,3000,3000,3000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"凭证库","凭证库"};
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		
		for (GlVoucher bkpf : pageList) {
			objs = new Object[16];
			objs[0] = bkpf.getPrepareddate()!=null?DateUtil.parseDate(bkpf.getPrepareddate(),DateUtil.DATE_SMALL_STR):"";
			objs[1] = bkpf.getNum() != null?bkpf.getNum():"";
			objs[2] = StringUtils.isNotBlank(bkpf.getPeriod())?bkpf.getPeriod():"";
			objs[3] = StringUtils.isNotBlank(bkpf.getExplanation())?bkpf.getExplanation():"";
			objs[4] = StringUtils.isNotBlank(bkpf.getVoucherTypeName())?bkpf.getVoucherTypeName():"";
			objs[5] = StringUtils.isNotBlank(bkpf.getYear())?bkpf.getYear():"";
			objs[6] = StringUtils.isNotBlank(bkpf.getCashername())?bkpf.getCashername():"";
			objs[7] = StringUtils.isNotBlank(bkpf.getCheckedname())?bkpf.getCheckedname():"";
			objs[8] = StringUtils.isNotBlank(bkpf.getManagername())?bkpf.getManagername():"";		
			objs[9] = bkpf.getSigndate()!=null?DateUtil.parseDate(bkpf.getSigndate(),DateUtil.DATE_SMALL_STR):"";
			objs[10] = bkpf.getTotalcredit()!=null?NumberParseUtil.getCurrency(bkpf.getTotalcredit().toString()):"0.00";
			objs[11] = bkpf.getTotalcreditglobal()!=null?NumberParseUtil.getCurrency(bkpf.getTotalcreditglobal().toString()):"0.00";
			objs[12] = bkpf.getTotalcreditgroup()!=null?NumberParseUtil.getCurrency(bkpf.getTotalcreditgroup().toString()):"0.00";
			objs[13] = bkpf.getTotaldebit()!=null?NumberParseUtil.getCurrency(bkpf.getTotaldebit().toString()):"0.00";
			objs[14] = bkpf.getTotaldebitglobal()!=null?NumberParseUtil.getCurrency(bkpf.getTotaldebitglobal().toString()):"0.00";
			objs[15] = bkpf.getTotaldebitgroup()!=null?NumberParseUtil.getCurrency(bkpf.getTotaldebitgroup().toString()):"0.00";
			objList.add(objs);
		}
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

	/**
	 * 日记账导出
	 * @param exportRequestVo
	 * @param bookInfo 
	 * @return
	 * @throws Exception
	 */
	private HSSFWorkbook exportRjzFile(ExportRequestVo exportRequestVo, FaAccbookinfoUtil bookInfo) throws Exception {
		String[] cNames = new String[]{ "凭证字号", "摘要", "科目名称","借方金额","贷方金额","余额-方向","余额"};
		int[] cWidths = new int[]{ 3000, 9000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"日记账","日记账"};
		
		GlDetailVo vo = new GlDetailVo();
		vo.setPkOrg(exportRequestVo.getPkOrg());
		
		List<GlDetailVr> list = this.glDetailMapper.exportDetailBookPage(exportRequestVo);
		
		List<GlDetailVr> vrList = new ArrayList<GlDetailVr>();
		GlDetailVr returnVr = null;
		
		
		GlBalanceVr currentBr = null;//当前余额信息
		
		String prePkAccasoa = null;//记录上次循环的科目
		String preMonth = null;//记录上次循环的月份
		
		List<String> detailPkList = null;//记录科目当前年度 当前年份 每个月份第一日的 凭证明细主键 集合 
		String ycPkDetail = null;
		
		BigDecimal yearmoney = null;//年初借方金额
		QueryWrapper<GlBalance> ycw = new QueryWrapper<GlBalance>();
		GlBalance yearb = null;//每个科目每年的期初余额
		GlBalance preMonthB = null;//上个月的余额 用来获取本年累计发生额  计算当前月份的年初方向
		
		GlDetail sumPreDetail = null;//当前凭证之前的数据
		
		GlDetail sumDayDetail = null;//本日累计数据
		
		BigDecimal sumDmoney = null;//本期借方累计发生额
		BigDecimal sumCmoney = null;//本期贷方累计发生额
		
		Date nextDate = null;//下一笔明细信息
		
		BigDecimal calAmount = null;//计算后的得数
		
		for (GlDetailVr vr : list) {
			currentBr = vr.getGlBalanceVr();
			//查找上一个月的 余额  1月份的为空
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", Integer.parseInt(vr.getPeriodv())-1);
			preMonthB = this.glBalanceMapper.selectOne(ycw);
			if(preMonthB == null) {
				preMonthB = new GlBalance();
				preMonthB.setFyearCreditLocal(new BigDecimal(0));
				preMonthB.setFyearDeditLocal(BigDecimal.valueOf(0));
			}
			
			//查找当前科目在  本月份之前   的   明细   发 生额总额
			sumPreDetail = this.glDetailMapper.selectPreDetailSum(vr);
			sumDmoney = sumPreDetail.getDebitamount();
			sumCmoney = sumPreDetail.getCreditamount();
			
			
			//查找本科目本年度 第一个月份的年初数
			ycw.clear();
			ycw.eq("DATAORIGINFLAG", -2 );
			ycw.eq("PK_ORG", vr.getPkOrg());
			ycw.eq("PK_ACCASOA", vr.getPkAccasoa());
			ycw.eq("YEAR", vr.getYearv());
			ycw.eq("PERIOD", "1");
			yearb = this.glBalanceMapper.selectOne(ycw);
			if(yearb == null) {
				yearmoney = new BigDecimal(0);
			}else {
				yearmoney = yearb.getFbeginBalanceLocal();
			}
			
			//根据科目方向  判定年初数方向，并计算当前月份期初方向
			if(currentBr.getBalanorient() == 0 ){
				//借方 ; 年初数加借方-贷方  根据结构判断 正数  月初方向为借方，负数月初方向为贷方
				calAmount = yearmoney.add(preMonthB.getFyearDeditLocal()).subtract(preMonthB.getFyearCreditLocal());
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
					currentBr.setBeginBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
					currentBr.setBeginBalanorient(0);
				}
			}
			if(currentBr.getBalanorient() == 1) {
				//贷方 ; 年初数加贷方 - 借方方  根据结构判断 正数  月初方向为贷方，负数月初方向为借方
				calAmount = yearmoney.add(preMonthB.getFyearCreditLocal()).subtract(preMonthB.getFyearDeditLocal());
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
					currentBr.setBeginBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
					currentBr.setBeginBalanorient(1);
				}
			}
			
			//如果是第一次循环 或者 切换科目查询相关信息 或者 月份发生改变
			if(StringUtils.isBlank(prePkAccasoa) || !vr.getPkAccasoa().equals(prePkAccasoa) || !vr.getPeriodv().equals(preMonth)) {
				prePkAccasoa = vr.getPkAccasoa();
				preMonth = vr.getPeriodv();
				//判断当前项是不是此科目本年本月第一条记录
				detailPkList = this.glDetailMapper.selectFirstDayYear(vo, bookInfo,prePkAccasoa,vr.getYearv(),vr.getPeriodv());
				ycPkDetail = detailPkList==null||detailPkList.size()==0?"":detailPkList.get(0);
				if(ycPkDetail.equals(vr.getPkDetail())) {
					//当前循环是本年第一个月第一参数 输入期初余额
					returnVr = new GlDetailVr();
					returnVr.setExplanation("期初余额");
					returnVr.setPrepareddatev(DateUtil.formatDate(vr.getYearv()+"-"+vr.getPeriodv()+"-01", DateUtil.DATE_SMALL_STR));
					returnVr.setCreditamount(BigDecimal.valueOf(0));
					returnVr.setDebitamount(BigDecimal.valueOf(0));
					currentBr.setFendBalanceLocal(calAmount);
					returnVr.setGlBalanceVr(currentBr);
					vrList.add(returnVr);
				}
			}
			
			//计算本笔明细的余额 和 方向
			//计算本地的余额 根据月初方向 判断逻辑为借方+借-待 或 待+待-借  得到余额 根据余额正负判断余额方向
			if(currentBr.getBeginBalanorient().compareTo(0) == 0) {
				//借方
				calAmount = calAmount.add(sumDmoney).subtract(sumCmoney);
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(1);
				}else {
					currentBr.setEndBalanorient(0);
				}
			}
			if(currentBr.getBeginBalanorient().compareTo(1) == 0) {
				//贷方
				calAmount = calAmount.add(sumCmoney).subtract(sumDmoney);
				if(calAmount.compareTo(BigDecimal.ZERO) < 0) {
					currentBr.setEndBalanorient(0);
				}else {
					currentBr.setEndBalanorient(1);
				}
			}
			currentBr.setFendBalanceLocal(calAmount);
			vr.setGlBalanceVr(currentBr);
			vrList.add(vr);
			//获取下一笔的明细信息日期 如果下一笔明细为null 则直接输出本日累计、本月累计和本年累计
			
			nextDate = this.glDetailMapper.selectNextDetailDate(vr,bookInfo);
			
			if(nextDate == null) {
				//直接输出本日累计、本月累计，和本年累计
				
				//查找本日累计
				sumDayDetail = this.glDetailMapper.selectCurrentDaySumDetail(vr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本日累计");
				returnVr.setPrepareddatev(vr.getPrepareddatev());
				returnVr.setCreditamount(sumDayDetail.getCreditamount());
				returnVr.setDebitamount(sumDayDetail.getDebitamount());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本月累计");
				returnVr.setCreditamount(currentBr.getLocalcreditamount());
				returnVr.setDebitamount(currentBr.getLocaldebitamount());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
				returnVr = new GlDetailVr();
				returnVr.setExplanation("本年累计");
				returnVr.setCreditamount(currentBr.getFyearCreditLocal());
				returnVr.setDebitamount(currentBr.getFyearDeditLocal());
				currentBr.setFendBalanceLocal(calAmount);
				returnVr.setGlBalanceVr(currentBr);
				vrList.add(returnVr);
				
			}else {
				//如果不为空 证明此月明细未全部展示，判断日期是否相等，如果不相等则输出本日累计
				if(DateUtil.compare_date(DateUtil.parseDate(vr.getPrepareddatev(), DateUtil.DATE_SMALL_STR), DateUtil.parseDate(nextDate, DateUtil.DATE_SMALL_STR)) != 0) {
					sumDayDetail = this.glDetailMapper.selectCurrentDaySumDetail(vr);
					returnVr = new GlDetailVr();
					returnVr.setExplanation("本日累计");
					returnVr.setPrepareddatev(vr.getPrepareddatev());
					returnVr.setCreditamount(sumDayDetail.getCreditamount());
					returnVr.setDebitamount(sumDayDetail.getDebitamount());
					currentBr.setFendBalanceLocal(calAmount);
					returnVr.setGlBalanceVr(currentBr);
					vrList.add(returnVr);
				}
			}
		}
		
		 List<Object[]> objList = new ArrayList<Object[]>(0);
	     Object[] objs = null;
		 for (GlDetailVr bseg : vrList) {
	       	 //"凭证字号", "摘要", "对方科目","借方金额","贷方金额","余额-方向","余额"
			objs = new Object[7];
	       	objs[0] = bseg.getGvnum();
	       	objs[1] = bseg.getExplanation();
	       	objs[2] = bseg.getGlBalanceVr().getName();
	       	objs[3] = bseg.getGlBalanceVr().getDebitamount();
	       	objs[4] = bseg.getGlBalanceVr().getCreditamount();
	       	objs[5] = 0==bseg.getGlBalanceVr().getEndBalanorient()?"借":"贷";
	       	objs[6] = bseg.getGlBalanceVr().getFendBalanceLocal();
	       	objList.add(objs);
		}
		
		
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		
		return workBook;
	}

	/**
	 * 科目表导出
	 * @param exportRequestVo
	 * @return
	 * @throws Exception
	 */
	private HSSFWorkbook exportAccountFile(ExportRequestVo exportRequestVo) throws Exception {
		
		//查找需要导出的科目列表信息
		List<BdAccountVr> accountList = this.bdAccountMapper.selectListByExport(exportRequestVo);
		String[] names = new String[]{"科目表","科目表"};
		int[] cWidths = new int[]{ 3000, 5000,3000,5000,5000,3000,3000};
		String[] cNames = new String[]{ "科目代码", "科目名称", "方向","上一级科目","全名","级次","底层科目"};
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		for (BdAccountVr sub : accountList) {
			obj = new Object[7];
			obj[0] = sub.getCode();
			obj[1] = sub.getName();
			obj[2] = 0==(sub.getBalanorient())?"借":"贷";
			obj[3] = sub.getParCode();
			obj[4] = sub.getParName();
			obj[5] = sub.getSumprintLevel();
			obj[6] = sub.getEndflag()!=null&&"1".equals(sub.getEndflag())?"是":"否";
			objList.add(obj);
		}
		HSSFWorkbook workBook = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return workBook;
	}

}
