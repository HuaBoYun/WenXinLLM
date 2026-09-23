package com.huabo.financialdata.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Case;
import org.springframework.stereotype.Service;

import com.ctc.wstx.util.StringUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FinancialDataExport;
import com.hbfk.util.NumberParseUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.entity.AccSum;
import com.huabo.financialdata.entity.entity.Account;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookResponsetVo;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookResponseVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookResponseVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;
import com.huabo.financialdata.mapper.AccBkpfMapper;
import com.huabo.financialdata.mapper.AccBsegMapper;
import com.huabo.financialdata.mapper.AccSumMapper;
import com.huabo.financialdata.mapper.AccountMapper;
import com.huabo.financialdata.mapper.AssInfoMapper;
import com.huabo.financialdata.service.ExportFileService;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.util.DateUtils;
import com.huabo.financialdata.util.LoginTokenUtil;

/**
 * 会计科目表 - 服务接口实现
 *
 * @author lee
 * @version 1.0.0
 **/
@Service
public class ExportFileServiceImpl implements ExportFileService {

	@Resource
	private IAccBookService accBookService;
	 
    @Resource
    private AccountMapper accountMapper;
    
    
    @Resource
    private AccBsegMapper accBsegMapper;
    
    @Resource
    private AccBkpfMapper accBkpfMapper;
    
    @Resource
    private AccSumMapper accSumMapper;
    
    @Resource
    AssInfoMapper assInfoMapper;
    
    @Resource
    private UserProvider userProvider;

	/**
	 * 导出选中信息为Excel
	 * @param token
	 * @param exportRequestVo
	 * @return
	 */
	@Override
	public ApiResponse<TblAttachment> exportFileFunc(String token, ExportRequestVo exportRequestVo) throws Exception {
		//查询当前财务账套有效的最大月份
        TblStaffUtil staff = userProvider.get();
        
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(staff);

        AccBookVO accBookVO = accBookService.getSelectedBookByStaffId(userInfoDO.getStaffId(), userInfoDO.getCurrentOrgId());
        if (Objects.isNull(accBookVO)) {
            return ApiResponse.fail("当前登录用户未选中财务账套");
        }
		
        exportRequestVo.setYear(accBookVO.getBookYear());
        exportRequestVo.setDbSource(accBookVO.getAcctId());
        TblAttachment attachment = null;
		//通用参数 1-导出科目表,2-导出日记账,3-凭证库,4-明细分类账、5-科目余额表、6-总分类账、7-辅助信息表、8-辅助余额表、9-辅助总账
		switch (exportRequestVo.getExprotType()) {
			case 1:
				attachment = this.exportAccountFile(exportRequestVo);
				break;
			case 2:
				attachment = this.exportRjzFile(exportRequestVo);
				break;
			case 3:
				attachment = this.exportPzkFile(exportRequestVo);
				break;
			case 4:
				attachment = this.exportMxflzFile(exportRequestVo);
				break;	
			case 5:
				attachment = this.exportKmyebFile(exportRequestVo);
				break;
			case 6:
				attachment = this.exportZflzbFile(exportRequestVo);
				break;
			case 7:
				attachment = this.exportFzxxzFile(exportRequestVo);
				break;
			case 8:
				attachment = this.exportFzyebFile(exportRequestVo);
				break;
			case 9:
				attachment = this.exportFzzzbFile(exportRequestVo);
				break;
			default:
				break;
		}
        attachment.setUploader(userInfoDO.getRealName());
//        BigDecimal attid = this.accountMapper.selectNextPrimaryKey();
//        attachment.setAttid(attid);
        try {
            this.accountMapper.insertAttchment(attachment);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
       
        return ApiResponse.success(attachment);
	}

	private TblAttachment exportFzzzbFile(ExportRequestVo exportRequestVo) {
		String[] amonths = exportRequestVo.getAmonths().split("~");
		String[] asacIdStrs = exportRequestVo.getAsacIdStrs().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		String[] asacids = null;
		for (String asacId : asacIdStrs) {
			asacids = asacId.split(",");
			sqlList.add(" (S.AMONTH IN ("+amonths[i]+") AND S.ASSID = '"+asacids[0]+"' AND S.ACCID = '"+asacids[1]+"') ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames =new String[]{"科目名称","辅助名称","期间","摘要","借方","贷方","余额-方向","余额"};
		int[] cWidths = new int[]{7000, 9000,3000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"辅助总账","辅助总账"};
		
		List<AuxiliaryBookResponsetVo> auxiliaryBookResponsetVoList = assInfoMapper.selectListByExportFzzz(exportRequestVo);
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		//accNameOne~assname~month~期初余额~qcmd~qcmc~qcdc~.qcmd:qcmc~accNameOne~assname~month~本期合计~qmmd~qmmc~qmdc~qmmd:qmdc~accNameOne~assname~month~本年累计~ljmd~ljmc~qmdc~qmmd:qmmc
		
		for (AuxiliaryBookResponsetVo ass : auxiliaryBookResponsetVoList) {
			obj = new Object[24];
			obj[0] = StringUtils.isNotBlank(ass.getAccName())?ass.getAccName():" ";
			obj[1] = StringUtils.isNotBlank(ass.getAssName())?ass.getAssName():" ";
			obj[2] = ass.getAMonth();
			obj[3] = "期初余额";
			obj[4] = ass.getQcmd()!=null?NumberParseUtil.getCurrency(ass.getQmmc().toString()):"0.00";
			obj[5] = ass.getQcmc()!=null?NumberParseUtil.getCurrency(ass.getQmmc().toString()):"0.00";
			obj[6] = "D".equals(ass.getQcdc())?"借":"贷";
			obj[7] = "D".equals(ass.getQcdc())?ass.getQcmd()!=null?NumberParseUtil.getCurrency(ass.getQcmd().toString()):"0.00":ass.getQcmc()!=null?NumberParseUtil.getCurrency(ass.getQcmc().toString()):"0.00";
			obj[8] = StringUtils.isNotBlank(ass.getAccName())?ass.getAccName():" ";
			obj[9] = StringUtils.isNotBlank(ass.getAssName())?ass.getAssName():" ";
			obj[10] = ass.getAMonth();
			obj[11] = "本期合计";
			obj[12] = ass.getQmmd()!=null?NumberParseUtil.getCurrency(ass.getQmmd().toString()):"0.00";
			obj[13] = ass.getQmmc()!=null?NumberParseUtil.getCurrency(ass.getQmmc().toString()):"0.00";
			obj[14] = "D".equals(ass.getQmdc())?"借":"贷";
			obj[15] = "D".equals(ass.getQmdc())?ass.getQmmd()!=null?NumberParseUtil.getCurrency(ass.getQmmd().toString()):"0.00":ass.getQmmc()!=null?NumberParseUtil.getCurrency(ass.getQmmc().toString()):"0.00";
			obj[16] = StringUtils.isNotBlank(ass.getAccName())?ass.getAccName():" ";
			obj[17] = StringUtils.isNotBlank(ass.getAssName())?ass.getAssName():" ";
			obj[18] = ass.getAMonth();
			obj[19] = "本年累计";
			obj[20] = ass.getLjmd()!=null?NumberParseUtil.getCurrency(ass.getLjmd().toString()):"0.00";
			obj[21] = ass.getLjmc()!=null?NumberParseUtil.getCurrency(ass.getLjmc().toString()):"0.00";
			obj[22] = "D".equals(ass.getQmdc())?"借":"贷";
			obj[23] = "D".equals(ass.getQmdc())?ass.getLjmd()!=null?NumberParseUtil.getCurrency(ass.getLjmd().toString()):"0.00":ass.getLjmc()!=null?NumberParseUtil.getCurrency(ass.getLjmc().toString()):"0.00";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,FinancialDataExport.ZONGZHANG);
		return attachment;
	}

	private TblAttachment exportFzyebFile(ExportRequestVo exportRequestVo) {
		exportRequestVo.setSqlStr("("+exportRequestVo.getAids()+")");
		
		String[] cNames = new String[]{"辅助ID","辅助名称","期初余额-方向","期初余额-金额","本期发生-借方 ","本期发生-贷方","期末余额-方向","期末余额-金额"};
		int[] cWidths = new int[]{8000, 9000,3500,3500,3500,3500,3500,3500};
		String[] names = new String[]{"辅助余额表","辅助余额表"};
		
		List<AuxiliaryBookResponsetVo> auxiliaryBookResponsetVoList = assInfoMapper.selectListByExportFzyeb(exportRequestVo);
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		for (AuxiliaryBookResponsetVo ass : auxiliaryBookResponsetVoList) {
			obj = new Object[8];
			obj[0] = StringUtils.isNotBlank(ass.getAssId())?ass.getAssId():" ";
			obj[1] = StringUtils.isNotBlank(ass.getAssName())?ass.getAssName():" ";
			obj[2] = "D".equals(ass.getQcdc())?"借":"贷";
			obj[3] = "D".equals(ass.getQcdc())?ass.getQcmd()!=null?NumberParseUtil.getCurrency(ass.getQcmd().toString()):"0.00":ass.getQcmc()!=null?NumberParseUtil.getCurrency(ass.getQcmc().toString()):"0.00";
			obj[4] = ass.getBqmd();
			obj[5] = ass.getBqmc();
			obj[6] = "D".equals(ass.getQmdc())?"借":"贷";
			obj[7] = "D".equals(ass.getQmdc())?ass.getQmmd()!=null?NumberParseUtil.getCurrency(ass.getQmmd().toString()):"0.00":ass.getQmmc()!=null?NumberParseUtil.getCurrency(ass.getQmmc().toString()):"0.00";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	private TblAttachment exportFzxxzFile(ExportRequestVo exportRequestVo) {
		exportRequestVo.setSqlStr("('"+exportRequestVo.getAssIds().replace(",", "','")+"')");
		
		String[] cNames = new String[]{"辅助类型","辅助编码","辅助名称","辅助描述","辅助级别","上级编码"};
		int[] cWidths = new int[]{9000, 7000,9000,6000,3000,10000};
		String[] names = new String[]{"辅助信息表","辅助信息表"};
		
		List<AuxiliaryBookResponsetVo> auxiliaryBookResponsetVoList = assInfoMapper.selectListByExportFzxxb(exportRequestVo);
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		
		for (AuxiliaryBookResponsetVo ass : auxiliaryBookResponsetVoList) {
			obj = new Object[6];
			obj[0] = StringUtils.isNotBlank(ass.getAssType())?ass.getAssType():" ";
			obj[1] = StringUtils.isNotBlank(ass.getAssId())?ass.getAssId():" ";
			obj[2] = StringUtils.isNotBlank(ass.getAssName())?ass.getAssName():" ";
			obj[3] = StringUtils.isNotBlank(ass.getAssDes())?ass.getAssDes():" ";
			obj[4] = StringUtils.isNotBlank(ass.getAssLevel())?ass.getAssLevel():" ";
			obj[5] = StringUtils.isNotBlank(ass.getAssSjbm())?ass.getAssSjbm():" ";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	private TblAttachment exportZflzbFile(ExportRequestVo exportRequestVo) {
		String[] accIds = exportRequestVo.getAccidStrs().split(",");
		String[] amonths = exportRequestVo.getAmonths().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		for (String accId : accIds) {
			sqlList.add(" (S.ACCID = '"+accId+"' AND S.AMONTH IN ("+amonths[i]+")) ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames = new String[]{"科目代码","科目名称","期间","摘要","借方","贷方","余额-方向","余额"};
		int[] cWidths = new int[]{3500, 9000,3500,3500,3500,3500,3500,3500};
		String[] names = new String[]{"总分类账","总分类账"};
		
		List<AccSum> sumList = this.accSumMapper.selectListByExportKmyeb(exportRequestVo);
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		//accId~accNameOne~amonth~期初余额~0~0~qmdc~qcmdqcmc~accId~accNameOne~amonth~本期合计~bqmc~bqmd~qmdc~qmmdqmmc~accId~accNameOne~amonth~本年累计~ljmd~ljmc~qmdc~qmdcqmmc
		for (AccSum accSum : sumList) {
			obj = new Object[24];
			obj[0] = StringUtils.isNotBlank(accSum.getACCID())?accSum.getACCID():" ";
			obj[1] = StringUtils.isNotBlank(accSum.getAccName())?accSum.getAccName():" ";
			obj[2] = accSum.getAMONTH();
			obj[3] = "期初余额";
			obj[4] = 0.00;
			obj[5] = 0.00;
			obj[6] = "D".equals(accSum.getQCDC())?"借":"贷";
			obj[7] = "D".equals(accSum.getQCDC())?accSum.getQCMD()!=null?NumberParseUtil.getCurrency(accSum.getQCMD().toString()):"0.00":accSum.getQCMC()!=null?NumberParseUtil.getCurrency(accSum.getQCMC().toString()):"0.00";
			obj[8] = StringUtils.isNotBlank(accSum.getACCID())?accSum.getACCID():" ";
			obj[9] = StringUtils.isNotBlank(accSum.getAccName())?accSum.getAccName():" ";
			obj[10] = accSum.getAMONTH();
			obj[11] = "本期合计";
			obj[12] = accSum.getBQMD()!=null?NumberParseUtil.getCurrency(accSum.getBQMD().toString()):"0.00";
			obj[13] = accSum.getBQMC()!=null?NumberParseUtil.getCurrency(accSum.getBQMC().toString()):"0.00";
			obj[14] = "D".equals(accSum.getQMDC())?"借":"贷";
			obj[15] = "D".equals(accSum.getQMDC())?accSum.getQMMC()!=null?NumberParseUtil.getCurrency(accSum.getQMMC().toString()):"0.00":accSum.getQMMD()!=null?NumberParseUtil.getCurrency(accSum.getQMMD().toString()):"0.00";
			obj[16] = StringUtils.isNotBlank(accSum.getACCID())?accSum.getACCID():" ";
			obj[17] = StringUtils.isNotBlank(accSum.getAccName())?accSum.getAccName():" ";
			obj[18] = accSum.getAMONTH();
			obj[19] = "本年累积";
			obj[20] = accSum.getLJMC()!=null?NumberParseUtil.getCurrency(accSum.getLJMC().toString()):"0.00";
			obj[21] = accSum.getLJMD()!=null?NumberParseUtil.getCurrency(accSum.getLJMD().toString()):"0.00";
			obj[22] = "D".equals(accSum.getQMDC())?"借":"贷";
			obj[23] = "D".equals(accSum.getQMDC())?accSum.getQMMC()!=null?NumberParseUtil.getCurrency(accSum.getQMMC().toString()):"0.00":accSum.getQMMD()!=null?NumberParseUtil.getCurrency(accSum.getQMMD().toString()):"0.00";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,FinancialDataExport.ZONGZHANG);
		return attachment;
	}

	private TblAttachment exportKmyebFile(ExportRequestVo exportRequestVo) {
		String[] accIds = exportRequestVo.getAccidStrs().split(",");
		String[] amonths = exportRequestVo.getAmonths().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		for (String accId : accIds) {
			sqlList.add(" (S.ACCID = '"+accId+"' AND S.AMONTH IN ("+amonths[i]+")) ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames = new String[]{ "科目代码", "科目名称","期间", "期初余额-方向","期初余额-金额","本期发生-借方","本期发生-贷方","累计发生-借方","累计发生-贷方","期末余额-方向","期末余额-金额"};
		int[] cWidths = new int[]{ 3000, 5000,4000,4000,4000,4000,4000,4000,4000,4000,4000};
		String[] names = new String[]{"科目余额表","科目余额表"};
		
		List<AccSum> sumList = this.accSumMapper.selectListByExportKmyeb(exportRequestVo);
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		for (AccSum accSum : sumList) {
			obj = new Object[11];
			obj[0] = StringUtils.isNotBlank(accSum.getACCID())?accSum.getACCID():" ";
			obj[1] = StringUtils.isNotBlank(accSum.getAccName())?accSum.getAccName():" ";
			obj[2] = accSum.getAMONTH();
			obj[3] = StringUtils.isNotBlank(accSum.getQCDC())&&"D".equals(accSum.getQCDC())?"借":"贷";
			obj[4] = StringUtils.isNotBlank(accSum.getQCDC())&&"D".equals(accSum.getQCDC())?accSum.getQCMD()!=null?NumberParseUtil.getCurrency(accSum.getQCMD().toString()):"0.00":accSum.getQCMC()!=null?NumberParseUtil.getCurrency(accSum.getQCMC().toString()):"0.00";
			obj[5] = accSum.getBQMD()!=null?NumberParseUtil.getCurrency(accSum.getBQMD().toString()):"0.00";
			obj[6] = accSum.getBQMC()!=null?NumberParseUtil.getCurrency(accSum.getBQMC().toString()):"0.00";
			obj[7] = accSum.getLJMD()!=null?NumberParseUtil.getCurrency(accSum.getLJMD().toString()):"0.00";
			obj[8] = accSum.getLJMC()!=null?NumberParseUtil.getCurrency(accSum.getLJMC().toString()):"0.00";
			obj[9] = StringUtils.isNotBlank(accSum.getQMDC())&&"D".equals(accSum.getQMDC())?"借":"贷";
			obj[10] = StringUtils.isNotBlank(accSum.getQMDC())&&"D".equals(accSum.getQMDC())?accSum.getQMMD()!=null?NumberParseUtil.getCurrency(accSum.getQMMD().toString()):"0.00":accSum.getQMMC()!=null?NumberParseUtil.getCurrency(accSum.getQMMC().toString()):"0.00";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	private TblAttachment exportMxflzFile(ExportRequestVo exportRequestVo) {
		String[] pzhs = exportRequestVo.getGlhStrs().split(",");
		String[] entryIds = exportRequestVo.getEntryId().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		for (String pzh : pzhs) {
			sqlList.add(" (B.GLH = '"+pzh+"' AND B.ENTRYID IN ("+entryIds[i]+")) ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames = new String[]{ "日期", "摘要", "对方科目","借方金额","贷方金额","余额-方向","余额"};
		int[] cWidths = new int[]{ 3500, 7000,3500,3500,3500,3500,3500};
		String[] names = new String[]{"明细分类账","明细分类账"};
		
		List<DetailedBookResponseVo> detailedBookResponseVoList = accBsegMapper.selectListByExportMxz(exportRequestVo);
		
		String qcdc = null;
        String qmDc = null;
        BigDecimal qmmoney = null;
        DiaryBookResponseVo sumBseg = null;
        DiaryBookRequestVo preBookRequestVo = null;
        List<Object[]> objList = new ArrayList<Object[]>(0);
        Object[] objs = null;
		for (DetailedBookResponseVo bseg : detailedBookResponseVoList) {
        	//获取本笔明细发生之前本月累计的借贷方发生额总和
        	preBookRequestVo = new DiaryBookRequestVo();
        	preBookRequestVo.setBookYear(Integer.parseInt(exportRequestVo.getYear()));
        	preBookRequestVo.setDbSource(exportRequestVo.getDbSource());
        	preBookRequestVo.setAccid(bseg.getAccid());
        	preBookRequestVo.setPzDate(DateUtils.parse(bseg.getPzDate(), "yyyy-MM-dd"));
        	preBookRequestVo.setMonth(bseg.getAmonth());
        	preBookRequestVo.setPzh(bseg.getPzh());
        	preBookRequestVo.setEntryId(Integer.parseInt(bseg.getEntryId()));
        	sumBseg = this.accBsegMapper.selectSumMonthyMdc(preBookRequestVo);
        	objs = new Object[7];
        	qcdc = bseg.getQcdc();
        	if("D".equals(qcdc)) {
        		qmmoney = bseg.getQcmd().add(sumBseg.getMd()).subtract(sumBseg.getMc());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "借";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "贷";
        		}
        	}else {
        		qmmoney = bseg.getQmmc().add(sumBseg.getMc()).subtract(sumBseg.getMd());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "贷";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "借";
        		}
        	}
        	 //"日期", "摘要", "对方科目","借方金额","贷方金额","余额-方向","余额"
        	objs[0] = StringUtils.isNotBlank(bseg.getPzDate())?bseg.getPzDate():" ";
        	objs[1] = StringUtils.isNotBlank(bseg.getLineText())?bseg.getLineText():" ";
        	objs[2] = StringUtils.isNotBlank(bseg.getAccName())?bseg.getAccName():" ";
        	objs[3] = bseg.getMd()!=null?NumberParseUtil.getCurrency(bseg.getMd().toString()):"0.00";
        	objs[4] = bseg.getMc()!=null?NumberParseUtil.getCurrency(bseg.getMc().toString()):"0.00";
        	objs[5] = qmDc;
        	objs[6] = qmmoney!=null?NumberParseUtil.getCurrency(qmmoney.toString()):"0.00";
        	objList.add(objs);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	private TblAttachment exportPzkFile(ExportRequestVo exportRequestVo) {
		String[] pzhs = exportRequestVo.getPzh().split(",");
		String[] entryIds = exportRequestVo.getEntryId().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		for (String pzh : pzhs) {
			sqlList.add(" (B.GLH = '"+pzh+"' AND B.ENTRYID IN ("+entryIds[i]+")) ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames = new String[]{"科目编码","对方科目","凭证日期","凭证号","摘要","借方金额","贷方金额","凭证类型","附件数","月份","财务主管","记账人","出纳人","审核人","制单人"};
		int[] cWidths = new int[]{3000, 8000,3000,3000,8000,3000,3000,3000,3000,3000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"凭证库","凭证库"};
		
		List<AccBkpfResponseVo> accBkpfResponseVoList = accBkpfMapper.selectListByExportPzk(exportRequestVo);
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		
		for (AccBkpfResponseVo bkpf : accBkpfResponseVoList) {
			objs = new Object[15];
			objs[0] = StringUtils.isNotBlank(bkpf.getAccid())?bkpf.getAccid():" ";
			objs[1] = StringUtils.isNotBlank(bkpf.getAccName())?bkpf.getAccName():" ";
			objs[2] = StringUtils.isNotBlank(bkpf.getPzDate())?bkpf.getPzDate():" ";
			objs[3] = StringUtils.isNotBlank(bkpf.getPzh())?bkpf.getPzh():" ";
			objs[4] = StringUtils.isNotBlank(bkpf.getLineText())?bkpf.getLineText():" ";
			objs[5] = bkpf.getMd()!=null?NumberParseUtil.getCurrency(bkpf.getMd().toString()):"0.00";
			objs[6] = bkpf.getMc()!=null?NumberParseUtil.getCurrency(bkpf.getMc().toString()):"0.00";
			objs[7] = StringUtils.isNotBlank(bkpf.getPzType())?bkpf.getPzType():" ";
			objs[8] = bkpf.getFj();
			objs[9] = bkpf.getAMonth();
			objs[10] = StringUtils.isNotBlank(bkpf.getCwzh())?bkpf.getCwzh():" ";
			objs[11] = StringUtils.isNotBlank(bkpf.getJzr())?bkpf.getJzr():" ";
			objs[12] = StringUtils.isNotBlank(bkpf.getCnr())?bkpf.getCnr():" ";
			objs[13] = StringUtils.isNotBlank(bkpf.getShr())?bkpf.getShr():" ";
			objs[14] = StringUtils.isNotBlank(bkpf.getZdr())?bkpf.getZdr():" ";
			objList.add(objs);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	private TblAttachment exportRjzFile(ExportRequestVo exportRequestVo) {
		String[] pzhs = exportRequestVo.getGlhStrs().split(",");
		String[] entryIds = exportRequestVo.getEntryId().split("~");
		
		List<String> sqlList = new ArrayList<String>(0);
		int i = 0;
		for (String pzh : pzhs) {
			sqlList.add(" (B.GLH = '"+pzh+"' AND B.ENTRYID IN ("+entryIds[i]+")) ");
			i++;
		}
		String sqlStr = "( "+String.join("OR", sqlList)+" )";
		exportRequestVo.setSqlStr(sqlStr);
		
		String[] cNames = new String[]{ "凭证字号", "摘要", "对方科目","借方金额","贷方金额","余额-方向","余额"};
		int[] cWidths = new int[]{ 3000, 9000,3000,3000,3000,3000,3000};
		String[] names = new String[]{"日记账","日记账"};
		
		List<DiaryBookResponseVo> diaryBookResponseVoList = accBsegMapper.selectListByExportRjz(exportRequestVo);
		
        String qcdc = null;
        String qmDc = null;
        BigDecimal qmmoney = null;
        DiaryBookResponseVo sumBseg = null;
        DiaryBookRequestVo preBookRequestVo = null;
        List<Object[]> objList = new ArrayList<Object[]>(0);
        Object[] objs = null;
		for (DiaryBookResponseVo bseg : diaryBookResponseVoList) {
        	//获取本笔明细发生之前本月累计的借贷方发生额总和
        	preBookRequestVo = new DiaryBookRequestVo();
        	preBookRequestVo.setBookYear(Integer.parseInt(exportRequestVo.getYear()));
        	preBookRequestVo.setDbSource(exportRequestVo.getDbSource());
        	preBookRequestVo.setAccid(bseg.getAccid());
        	preBookRequestVo.setPzDate(DateUtils.parse(bseg.getPzDate(), "yyyy-MM-dd"));
        	preBookRequestVo.setMonth(bseg.getAmonth());
        	preBookRequestVo.setPzh(bseg.getPzh());
        	preBookRequestVo.setEntryId(bseg.getEntryId());
        	sumBseg = this.accBsegMapper.selectSumMonthyMdc(preBookRequestVo);
        	objs = new Object[7];
        	qcdc = bseg.getQcdc();
        	if("D".equals(qcdc)) {
        		qmmoney = bseg.getQcmd().add(sumBseg.getMd()).subtract(sumBseg.getMc());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "借";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "贷";
        		}
        	}else {
        		qmmoney = bseg.getQmmc().add(sumBseg.getMc()).subtract(sumBseg.getMd());
        		if(qmmoney.compareTo(BigDecimal.ZERO) > 0) {
        			qmDc = "贷";
        		}else {
        			qmmoney = qmmoney.abs();
        			qmDc = "借";
        		}
        	}
        	 //"凭证字号", "摘要", "对方科目","借方金额","贷方金额","余额-方向","余额"
        	objs[0] = StringUtils.isNotBlank(bseg.getPzh())?bseg.getPzh():" ";
        	objs[1] = StringUtils.isNotBlank(bseg.getLineText())?bseg.getLineText():" ";
        	objs[2] = StringUtils.isNotBlank(bseg.getAccName())?bseg.getAccName():" ";
        	objs[3] = bseg.getMd()!=null?NumberParseUtil.getCurrency(bseg.getMd().toString()):"0.00";
        	objs[4] = bseg.getMc()!=null?NumberParseUtil.getCurrency(bseg.getMc().toString()):"0.00";
        	objs[5] = qmDc;
        	objs[6] = qmmoney!=null?NumberParseUtil.getCurrency(qmmoney.toString()):"0.00";
        	objList.add(objs);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	/**
	 * 查找 科目表导出 数据
	 * @param exportRequestVo
	 */
	private TblAttachment exportAccountFile(ExportRequestVo exportRequestVo) {
		String accIdStrs = exportRequestVo.getAccidStrs();
		accIdStrs = accIdStrs.replace(",", "','");
		exportRequestVo.setAccidStrs("'"+accIdStrs+"'");
		//查找需要导出的科目列表信息
		List<Account> accountList = this.accountMapper.selectListByExport(exportRequestVo);
		String[] names = new String[]{"科目表","科目表"};
		int[] cWidths = new int[]{ 3000, 5000,3000,5000,5000,3000,3000};
		String[] cNames = new String[]{ "科目代码", "科目名称", "方向","上一级科目","全名","级次","底层科目"};
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] obj = null;
		for (Account sub : accountList) {
			obj = new Object[7];
			obj[0] = sub.getACCID();
			obj[1] = sub.getACCNAME1();
			obj[2] = "D".equals(sub.getDC())?"借":"贷";
			obj[3] = sub.getHIGHACCID();
			obj[4] = sub.getACCNAME2();
			obj[5] = sub.getIGRADE();
			obj[6] = sub.getISDCACC()!=null&&sub.getISDCACC()==1?"是":"否";
			objList.add(obj);
		}
		TblAttachment attachment = FinancialDataExport.makeLocalExcel(objList,cNames,cWidths,names,null);
		return attachment;
	}

	@Override
	public void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa) throws Exception {
		// TODO Auto-generated method stub
		TblAttachment att=accountMapper.getOne(fileId);
		FinancialDataExport.fileDownLoad(response, att, isCa);
	}


}
