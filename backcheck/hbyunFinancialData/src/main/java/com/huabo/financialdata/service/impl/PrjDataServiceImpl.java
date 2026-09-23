package com.huabo.financialdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.entity.AccBook;
import com.huabo.financialdata.entity.entity.AccPeriod;
import com.huabo.financialdata.entity.entity.AccReportMprofit;
import com.huabo.financialdata.entity.entity.AccReportYbal;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.prjData.*;
import com.huabo.financialdata.mapper.PrjDataMapper;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.service.PrjDataService;
import com.huabo.financialdata.util.ContextHolder;
import com.huabo.financialdata.util.LoginTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 报表数据  接口实现类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-12-01
 */
@Service
public class PrjDataServiceImpl implements PrjDataService {

    private Logger logger = LoggerFactory.getLogger(PrjDataServiceImpl.class);

    @Resource
    PrjDataMapper prjDataMapper;
    @Resource
    private IAccBookService accBookService;

    private String dbSource;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 报表数据  资产负债表  列表分页查询
     *
     * @param token          用户登录token
     * @param zcfzbRequestVo 请求参数封装公共类
     * @return
     */
    @Override
    public ApiResponse<PageInfo<ZcfzbResponseVo>> getZcfzbList(String token, ZcfzbRequestVo zcfzbRequestVo) throws Exception {
        logger.info("进入报表数据资产负债表列表分页查询接口：：：token=" + token);
        Integer bookYear = zcfzbRequestVo.getBookYear();
        TblStaffUtil staff = userProvider.get();
        
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(staff);
        AccBookVO accBookVO = accBookService.getSelectedBookByStaffId(userInfoDO.getStaffId(), userInfoDO.getCurrentOrgId());
        if (Objects.isNull(accBookVO)) {
            return ApiResponse.fail("当前登录用户未选中财务账套");
        }

        dbSource = accBookVO.getAcctId();
        zcfzbRequestVo.setDbSource(dbSource);
        //获取账套数据
        List list = prjDataMapper.findAllZcfzb(zcfzbRequestVo);
        if (list == null) {
            return ApiResponse.fail("当前账套无数据!");
        }
        //资产负债表查询数据
        List<AccPeriod> accPeriodList = prjDataMapper.findAllAccPeriod(zcfzbRequestVo);
        if (accPeriodList == null) {
            return ApiResponse.fail("获取数据失败!");
        }

        AccPeriod AccPeriod = accPeriodList.get(0);
        List<AccPeriod> findByProperty = new ArrayList<AccPeriod>();
        findByProperty.add(new AccPeriod("1", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("2", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("3", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("4", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("5", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("6", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("7", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("8", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("9", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("10", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("11", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));
        findByProperty.add(new AccPeriod("12", bookYear.toString(), AccPeriod.getStartdate(), AccPeriod.getEnddate(), AccPeriod.getImport_(), AccPeriod.getVoutbname()));


        AccBook accBook = new AccBook();
        accBook.setBookyear(bookYear.toString());
//        accBook.setOrgid(new BigDecimal(orgId));
        accBook.setAcctid(zcfzbRequestVo.getBook());
        accBook.setDbSource(dbSource);
        List<AccBook> accBookList = prjDataMapper.findAccBookUrl(accBook);
        accBook = accBookList.size() > 0 ? accBookList.get(0) : null;

        String strMonth = zcfzbRequestVo.getDate();
        boolean bYear = false;// 默认非全年
        if (strMonth == null) {// 全年资产负债表
            String year1 = accPeriodList.get(accPeriodList.size() - 1).getAyear().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String year2 = sdf.format(new Date());
            if (Integer.parseInt(year1) == Integer.parseInt(year2)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                strMonth = Integer.parseInt(sdf1.format(calendar.getTime())) + "";
            } else {
                strMonth = accPeriodList.get(accPeriodList.size() - 1).getAmonth().toString();
            }
            bYear = true;
        }

        // 所有目标数据
        List<AccReportYbal> listDesc = new ArrayList<AccReportYbal>();

        for (Object obj : list) {
            AccReportYbal zcfzb = (AccReportYbal) obj;
            System.out.println(zcfzb.getZc());
            String strJSGS1 = zcfzb.getJsgs1();
            String strJSGS2 = zcfzb.getJsgs2();
            String strJSGS3 = zcfzb.getJsgs3();
            String strJSGS4 = zcfzb.getJsgs4();
            // 如果公式为空，则不进行计算
            // if(strJSGS1 == null){
            // System.out.println("公式为空，不参与计算");
            // continue;
            // }
            // 替换公式中的的会计期间和月份
            String currentUser = ContextHolder.getContext();
            //String strYear = "2014";
            String strYear = findByProperty.get(0).getAyear().toString(); //tyb修改
            // strJSGS1.replaceAll("AccYear", strYear);
            // strJSGS2.replaceAll("AccYear", strYear);

            // strJSGS3.replaceAll("AccYear", strYear);
            // strJSGS4.replaceAll("AccYear", strYear);
			/*if (zcfzb.getZc() != null && zcfzb.getZc().contains("非流动资产合计")){
				System.out.println(333);
			}*/
            Boolean bool = false;
            double dQCS1 = 0d;
            if (strJSGS1 != null) {
                dQCS1 = CaclReport(strJSGS1.replaceAll("\\s*", ""), strYear, listDesc, "Z");// 计算公式1
                zcfzb.setQcs1(dQCS1);
                bool = true;
            }
            double dQMS1 = 0d;
            if (strJSGS2 != null) {
                strJSGS2 = strJSGS2.replaceAll("AccMonth", strMonth);
                dQMS1 = CaclReport(strJSGS2.replaceAll("\\s*", ""), strYear, listDesc, "Z");// 计算公式2
                zcfzb.setQms1(dQMS1);
                bool = true;
            }
            double dQCS2 = 0d;
            if (strJSGS3 != null) {
                dQCS2 = CaclReport(strJSGS3.replaceAll("\\s*", ""), strYear, listDesc, "Z");// 计算公式3
                zcfzb.setQcs2(dQCS2);
                bool = true;
            }
            double dQMS2 = 0d;
            if (strJSGS4 != null) {
                strJSGS4 = strJSGS4.replaceAll("AccMonth", strMonth);
                dQMS2 = CaclReport(strJSGS4.replaceAll("\\s*", ""), strYear, listDesc, "Z");// 计算公式4
                zcfzb.setQms2(dQMS2);
                bool = true;
            }
            if (bYear && bool) {
                try {
                    zcfzb.setDbSource(dbSource);
                    //资产负债表更新数据
                    prjDataMapper.updateTblAccReportYbal(zcfzb);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            listDesc.add(zcfzb);
        }
        ZcfzbResponseVo zcfzbResponseVo = new ZcfzbResponseVo();
        zcfzbResponseVo.setZcfzb_list(listDesc);
        zcfzbResponseVo.setMty(zcfzbRequestVo.getMty());
        zcfzbResponseVo.setAccPeriodList(accPeriodList);
        zcfzbResponseVo.setAccBook(accBook);
        zcfzbResponseVo.setStrMonth(strMonth);

        List list1 = new ArrayList();
        list1.add(zcfzbResponseVo);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(list1);
        return ApiResponse.success(pageInfo);
    }


    /**
     * 报表数据 - 利润表-- 列表分页查询
     *
     * @param token        用户登录token
     * @param lrbRequestVo 请求参数封装公共类
     * @return
     */
    @Override
    public ApiResponse<PageInfo<ZcfzbResponseVo>> getLrbList(String token, LrbRequestVo lrbRequestVo) throws Exception {

        // 获取界面上选择的日期
        // 获取当前账套单位信息
        TblStaffUtil staff = userProvider.get();
        
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(staff);
        AccBookVO accBookVO = accBookService.getSelectedBookByStaffId(userInfoDO.getStaffId(), userInfoDO.getCurrentOrgId());
        if (Objects.isNull(accBookVO)) {
            return ApiResponse.fail("当前登录用户未选中财务账套");
        }

        dbSource = accBookVO.getAcctId();

        String strMonth = lrbRequestVo.getDate();

        AccBook accBook = new AccBook();
        accBook.setBookyear(lrbRequestVo.getBookYear().toString());
        accBook.setAcctid(lrbRequestVo.getBook());
        accBook.setDbSource(dbSource);

        List<AccBook> accBookList = prjDataMapper.findAccBookUrl(accBook);
        // 获取所有数据
        List list = prjDataMapper.findAllMprofit(dbSource);
        if (list == null) {
            return ApiResponse.fail("当前账套无数据！");
        }
        ZcfzbRequestVo zcfzbRequestVo = new ZcfzbRequestVo();
        zcfzbRequestVo.setDbSource(dbSource);
        zcfzbRequestVo.setBookYear(lrbRequestVo.getBookYear());
        List<AccPeriod> list2 = prjDataMapper.findAllAccPeriod(zcfzbRequestVo);

        if (list2 == null) {
            return ApiResponse.fail("获取数据失败！");
        }
        boolean bYear = false;// 默认非全年
        if (strMonth == null) {// 全年资产负债表
            String year1 = list2.get(list2.size() - 1).getAyear().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String year2 = sdf.format(new Date());
            if (Integer.parseInt(year1) == Integer.parseInt(year2)) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                strMonth = Integer.parseInt(sdf1.format(calendar.getTime())) + "";
            } else {
                strMonth = list2.get(list2.size() - 1).getAmonth().toString();
            }
            bYear = true;
        }
        //bYear = false;
        // 所有目标数据
        List list3 = new ArrayList();
        String strYear = list2.get(0).getAyear().toString();//tyb修改
        for (Object obj : list) {
            AccReportMprofit accReportMprofit = (AccReportMprofit) obj;
            String strBys = accReportMprofit.getBysFormula();
            double dCuryearBys = 0d;
            if (strBys != null) {
                strBys = strBys.replaceAll("AccMonth", strMonth);
                dCuryearBys = CaclReport(strBys, strYear, list, "L");// 计算公式1
                if (accReportMprofit.getItem().contains("利息收入") && dCuryearBys < 0) {
                    dCuryearBys = dCuryearBys * -1;
                }
            }
            accReportMprofit.setCuryearBys(dCuryearBys);
            String strBnlj = accReportMprofit.getBnljFormula();
            double dCuryearBnlj = 0d;
            if (strBnlj != null) {
                strBnlj = strBnlj.replaceAll("AccMonth", strMonth);
                dCuryearBnlj = CaclReport(strBnlj, strYear, list, "L");// 计算公式1
                if (accReportMprofit.getItem().contains("利息收入") && dCuryearBnlj < 0) {
                    dCuryearBnlj = dCuryearBnlj * -1;
                }
            }
            accReportMprofit.setCuryearBnlj(dCuryearBnlj);
            list3.add(accReportMprofit);
        }
        List listDesc = new ArrayList();
        for (Object obj : list) {
            AccReportMprofit accReportMprofit = (AccReportMprofit) obj;
            System.out.println(accReportMprofit.getItem());
            accReportMprofit.setDbSource(dbSource);
            String strBys = accReportMprofit.getBysFormula();
            double dCuryearBys = 0d;
            if (strBys != null) {
                strBys = strBys.replaceAll("AccMonth", strMonth);
                dCuryearBys = CaclReport(strBys, strYear, list3, "L");// 计算公式1
                if (accReportMprofit.getItem().contains("利息收入") && dCuryearBys < 0) {
                    dCuryearBys = dCuryearBys * -1;
                }
            }
            accReportMprofit.setCuryearBys(dCuryearBys);

            String strBnlj = accReportMprofit.getBnljFormula();
            double dCuryearBnlj = 0d;
            if (strBnlj != null) {
                strBnlj = strBnlj.replaceAll("AccMonth", strMonth);
                dCuryearBnlj = CaclReport(strBnlj, strYear, list3, "L");// 计算公式1
                if (accReportMprofit.getItem().contains("利息收入") && dCuryearBnlj < 0) {
                    dCuryearBnlj = dCuryearBnlj * -1;
                }
            }
            accReportMprofit.setCuryearBnlj(dCuryearBnlj);
            if (bYear) {
                // 修改当年主营业务收入和当年本年利润
                prjDataMapper.updateTblAccReportMprofit(accReportMprofit);
            }
            listDesc.add(accReportMprofit);
        }

        PageInfo pager = new PageInfo();
        pager.setPageSize(100);
        LrbResponseVo lrbResponseVo = new LrbResponseVo();
        lrbResponseVo.setAccBook(accBook);
        lrbResponseVo.setMty(lrbRequestVo.getMty());
        lrbResponseVo.setDate(strMonth);
        lrbResponseVo.setAccPeriodList(list2);
        lrbResponseVo.setAccReportMprofitList(listDesc);
        List list1 = new ArrayList();
        list1.add(lrbResponseVo);
        pager.setList(list1);
        return ApiResponse.success(pager);
    }


    // 计算资产负债报表主函数

    /**
     * @author tyb
     * 2016-6-16下午3:09:39
     * @Des:
     */
    public double CaclReport(String strJSGS, String strYear, List listDescCacl,
                             String ReportType) {
        System.out.println(strJSGS);
        double dValue = 0;
        try {
            // 如果只是单元格相加
            if (strJSGS.substring(0, 4).equals("Cell")) {
                double fCell = 0;
                String jsgs = strJSGS;
                String[] strs = strJSGS.split("Cell");
                for (int i = 1; i < strs.length; i++) {
                    String cellStr = "Cell" + strs[i].substring(0, strs[i].lastIndexOf(")") + 1);
                    double fCell1 = CaclCell(cellStr, listDescCacl, ReportType);
                    jsgs = jsgs.replace(cellStr, fCell1 < 0 ? "(" + fCell1 + ")" : fCell1 + "");
                }
                ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
                dValue = Double.parseDouble(jse.eval(jsgs).toString());
            } else if (strJSGS.substring(0, 7).equals("SumCell")) {// 如果是计算合计
                int iSubtractSum = strJSGS.indexOf('+');
                String jsgs = strJSGS;
                if (iSubtractSum > 0 || strJSGS.indexOf('-') > 0) {
                    // 分割公式
                    String strSums[] = strJSGS.split("[+-]");
                    int iCount = strSums.length;
                    if (iCount > 1) {
                        double dSumCell = 0.0;
                        //jsgs = jsgs.replace(strs[k],dSumQCMD<0?"("+dSumQCMD+")":dSumQCMD+"");

                        for (int i = 0; i < iCount; i++) {
                            if (strSums[i].indexOf("SumCell") >= 0) {
                                dSumCell = CaclSumCell(strSums[i], listDescCacl);
                            } else if (strSums[i].indexOf("Cell") >= 0) {
                                dSumCell = CaclCell(strSums[i], listDescCacl, ReportType);
                            }

                            jsgs = jsgs.replace(strSums[i], dSumCell < 0 ? "(" + dSumCell + ")" : dSumCell + "");
                        }
                        if (StringUtils.isNotBlank(jsgs) && !jsgs.equals(strJSGS)) {
                            ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
                            dSumCell = Double.parseDouble(jse.eval(jsgs).toString());
                        }
                        dValue = dSumCell;
                    }
                } else {
                    double dSumCell = CaclSumCell(strJSGS, listDescCacl);
                    dValue = dSumCell;
                }
            }
            // 如果公式以if开头
            else if (strJSGS.substring(0, 2).equals("if")) {
                // 如果计算公式中包含减号
                int iSubtractIf = strJSGS.indexOf('-');
                if (iSubtractIf > 0) {
                    // 分割公式
                    String strValues[] = strJSGS.split("-");
                    int iCount = strValues.length;

                    if (iCount == 2) {
//                        strValues[0] = strValues[0].substring(3,strJSGS.indexOf(')') + 1);
//                        strValues[1] = strValues[1].substring(4,strJSGS.indexOf(')') + 2);
                        float fSumQCMD1 = 0;// CaclQCValue(strValues[0],
                        // strYear);
                        float fSumQCMD2 = 0;// CaclQCValue(strValues[1],
                        // strYear);
                        float fSumQCMD = fSumQCMD1 - fSumQCMD2;
                        dValue = fSumQCMD;
                    }
                }
            }
            /**
             * tyb
             * 判断公式中有+号 或者有-号
             */
            else if (strJSGS.indexOf('-') > 0 || strJSGS.indexOf('+') > 0) {
                //JFQM('1401,1402,1403,1405,1406,1407,1408,1409,1410,1411,1412,5001,5002', AccYear, AccMonth)-DFQM('1404', AccYear, AccMonth)-JFQM('1471', AccYear, AccMonth)
                String[] strs = strJSGS.split("[+-]");
                System.out.println("strs" + strs);
                String jsgs = strJSGS;
                for (int k = 0; k < strs.length; k++) {
                    String strLeft = strs[k].substring(0, 4);// 左边字符串
                    List<String> nums = new ArrayList<String>();
                    if (strLeft.equals("JFQM")) {// 只是简单的数据库合计，计算借方期初数
                        String strDC = "D";
                        for (int j = 0; j < nums.size(); j++) {
                            if (strs[k].contains(nums.get(j))) {
                                strDC = "C";
                            }
                        }
                        double dSumQCMD = CaclQMValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    } else if (strLeft.equals("JFQC")) {
                        String strDC = "D";
                        for (int j = 0; j < nums.size(); j++) {
                            if (strs[k].contains(nums.get(j))) {
                                strDC = "C";
                            }
                        }
                        double dSumQCMD = CaclQCValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    } else if (strLeft.equals("DFQC")) {
                        String strDC = "C";
                        double dSumQCMD = CaclQCValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    } else if (strLeft.equals("DFQM")) {
                        String strDC = "C";
                        double dSumQCMD = CaclQMValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    } else if (strLeft.equals("JFFS")) {
                        String strDC = "D";
                        double dSumQCMD = CaclFSValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    } else if (strLeft.equals("DFFS")) {
                        String strDC = "C";
                        double dSumQCMD = CaclFSValue(strs[k], strYear, strDC);
                        jsgs = jsgs.replace(strs[k], dSumQCMD < 0 ? "(" + dSumQCMD + ")" : dSumQCMD + "");
                        //}
                    }
                }
                if (StringUtils.isNotBlank(jsgs) && !jsgs.equals(strJSGS)) {
                    ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
                    dValue = Double.parseDouble(jse.eval(jsgs).toString());
                }
            } else {
                List<String> nums = new ArrayList<String>();
                // 获取公式的前4个字母，然后再分别进行不同处理
                String strLeft = strJSGS.substring(0, 4);// 左边字符串
                if (strLeft.equals("JFQC")) {// 只是简单的数据库合计，计算借方期初数
                    String strDC = "D";
                    for (int j = 0; j < nums.size(); j++) {
                        if (strJSGS.contains(nums.get(j))) {
                            strDC = "C";
                        }
                    }
                    double dSumQCMD = CaclQCValue(strJSGS, strYear, strDC);
                    dValue = dSumQCMD;
                } else if (strLeft.equals("DFQC")) {// 贷方期初
                    double dSumQCMC = CaclQCValue(strJSGS, strYear, "C");
                    if (dSumQCMC == 0) {// 数据有可能在借方，按负数处理
                        double dSumQCMD = CaclQCValue(strJSGS, strYear, "D");
                        if (dSumQCMD != 0) {// 不等于0，说明数据在借方
                            dSumQCMC = dSumQCMD - dSumQCMD - dSumQCMD;
                        }
                    }
                    dValue = dSumQCMC;
                } else if (strLeft.equals("JFQM")) {// 计算借方期末数
                    String strDC = "D";
                    for (int j = 0; j < nums.size(); j++) {
                        if (strJSGS.contains(nums.get(j))) {
                            strDC = "C";
                        }
                    }
                    double dSumQCMD = CaclQMValue(strJSGS, strYear, strDC);
                    if (strDC.equals("D") && dSumQCMD == 0) {// 数据有可能在借方，按负数处理
                        double dSumQCMC = CaclQMValue(strJSGS, strYear, "C");
                        if (dSumQCMC != 0) {// 不等于0，说明数据在借方
                            dSumQCMD = dSumQCMC - dSumQCMC - dSumQCMC;
                        }
                    }
                    dValue = dSumQCMD;
                } else if (strLeft.equals("DFQM")) {// 计算贷方期末数
                    double dSumQCMC = CaclQMValue(strJSGS, strYear, "C");
                    if (dSumQCMC == 0) {// 数据有可能在借方，按负数处理
                        double dSumQCMD = CaclQMValue(strJSGS, strYear, "D");
                        if (dSumQCMD != 0) {// 不等于0，说明数据在借方
                            dSumQCMC = dSumQCMD - dSumQCMD - dSumQCMD;
                        }
                    }
                    dValue = dSumQCMC;
                } else if (strLeft.equals("JFFS")) {// 贷方发生
                    double dSumQCMC = CaclFSValue(strJSGS, strYear, "D");
                    dValue = dSumQCMC;
                } else if (strLeft.equals("DFFS")) {// 贷方发生
                    double dSumDFFS = CaclFSValue(strJSGS, strYear, "C");
                    dValue = dSumDFFS;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dValue;
    }

    // 计算cell
    public double CaclCell(String strJSGS, List listDesc, String reportType) {
        double fSumQCMD = 0;
        try {
            strJSGS = strJSGS.substring(strJSGS.indexOf('(') + 1,
                    strJSGS.indexOf(')'));
            // 获取所有数字
            String strDigit[] = strJSGS.split(",");
            strDigit[1] = strDigit[1].trim();
            double dValue = 0.0;
            for (Object obj : listDesc) {
                if (reportType.equals("Z")) {
                    AccReportYbal zcfzb = (AccReportYbal) obj;
                    if (Integer.parseInt(zcfzb.getZid()) == Integer
                            .parseInt(strDigit[1])) {
                        if (strDigit[0].equals("4")) {
                            if (zcfzb.getJsgs1() != null)
                                dValue = zcfzb.getQcs1();
                        } else if (strDigit[0].equals("6")) {
                            if (zcfzb.getJsgs2() != null)
                                dValue = zcfzb.getQms1();
                        } else if (strDigit[0].equals("10")) {
                            if (zcfzb.getJsgs3() != null)
                                dValue = zcfzb.getQcs2();
                        } else if (strDigit[0].equals("12")) {
                            if (zcfzb.getJsgs4() != null)
                                dValue = zcfzb.getQms2();
                        }
                    }
                } else if (reportType.equals("L")) {
                    AccReportMprofit lrb = (AccReportMprofit) obj;
                    if (lrb.getId().intValue() == Integer.parseInt(strDigit[1])) {
                        if (strDigit[0].equals("3")) {
                            if (lrb.getCuryearBys() != null)
                                dValue = lrb.getCuryearBys();
                        } else if (strDigit[0].equals("5")) {
                            if (lrb.getCuryearBnlj() != null)
                                dValue = lrb.getCuryearBnlj();
                        }
                    }
                }
            }
            fSumQCMD = dValue;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fSumQCMD;
    }

    // 计算合计
    public double CaclSumCell(String strJSGS, List listDesc) {
        double dSumQCMD = 0;
        try {
            strJSGS = strJSGS.substring(strJSGS.indexOf('(') + 1,
                    strJSGS.indexOf(')'));
            // 获取所有数字
            String strDigit[] = strJSGS.split(",");
            strDigit[0] = strDigit[0].trim();
            strDigit[1] = strDigit[1].trim();
            strDigit[2] = strDigit[2].trim();
            strDigit[3] = strDigit[3].trim();
            // 按照公式进行计算

            double dValue = 0.0;
            for (Object obj : listDesc) {
                AccReportYbal zcfzb = (AccReportYbal) obj;
                if (Integer.parseInt(zcfzb.getZid()) >= Integer
                        .parseInt(strDigit[1])
                        && Integer.parseInt(zcfzb.getZid()) <= Integer
                        .parseInt(strDigit[3])) {
                    if (strDigit[0].equals("4")) {
                        if (zcfzb.getJsgs1() != null || zcfzb.getQcs1() != null)
                            dValue += zcfzb.getQcs1();
                    } else if (strDigit[0].equals("6")) {
                        if (zcfzb.getJsgs2() != null)
                            dValue += zcfzb.getQms1();
                    } else if (strDigit[0].equals("10")) {
                        if (zcfzb.getJsgs3() != null)
                            dValue += zcfzb.getQcs2();
                    } else if (strDigit[0].equals("12")) {
                        if (zcfzb.getJsgs4() != null)
                            dValue += zcfzb.getQms2();
                    }
                }
            }
            dSumQCMD = dValue;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dSumQCMD;
    }

    // 根据指定月份，计算期末数
    public double CaclFSValue(String strJSGS, String strYear, String strDC) {
        double dSumQMMC = 0;
        try {
            int iLeft = strJSGS.indexOf('(');// 最左边起始位置
            int iRight = strJSGS.indexOf("AccYear");// 最右边的位置
            int iMonth = strJSGS.indexOf(')');// 公式终点位置

            String strContext = strJSGS.substring(iLeft + 1, iRight - 2);// 公式内容,并去掉前面括号
            String strMonth = strJSGS.substring(iRight + 8, iMonth);// 公式内容,确定月份
            // 获取所有数字
            String strDigit[] = strMonth.split(",");
            boolean bMonth = false;// 保存是否是计算本月数
            if (strDigit.length > 0) {
                strDigit[0] = strDigit[0].trim();
                strMonth = strDigit[0];
                bMonth = true;
            }
            if (strDigit.length > 1) {
                strDigit[1] = strDigit[1].trim();
                strMonth = strDigit[1];
                bMonth = false;
            }

            if (strDC.equals("D")) {
                // 按照公式进行计算
                strContext = strContext.replaceAll("'", "");
                String[] strConte = strContext.split(",");
                //资产负债表查询数据
                List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                Double dValue = 0.0;
                for (Object obj : list) {
                    AccSumUtil accSum = (AccSumUtil) obj;
                    if (bMonth) {
                        if (accSum.getBqmd() != null)
                            dValue += accSum.getBqmd().doubleValue();
                    } else {
                        if (accSum.getLjmd() != null)
                            dValue += accSum.getLjmd().doubleValue();
                    }
                }
                dSumQMMC = dValue;
            } else {
                // 按照公式进行计算

                ZcfzbRequestVo zcfzbRequestVo = new ZcfzbRequestVo();
                zcfzbRequestVo.setBookYear(Integer.parseInt(strYear));
                strContext = strContext.replaceAll("'", "");
                String[] strConte = strContext.split(",");
                //资产负债表查询数据
                List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                Double dValue = 0.0;
                for (Object obj : list) {
                    AccSumUtil accSum = (AccSumUtil) obj;

                    if (bMonth) {
                        if (accSum.getBqmc() != null)
                            dValue += accSum.getBqmc().doubleValue();
                    } else {
                        if (accSum.getLjmc() != null)
                            dValue += accSum.getLjmc().doubleValue();
                    }
                }
                dSumQMMC = dValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dSumQMMC;
    }

    // 根据指定月份，计算期末数
    public double CaclQMValue(String strJSGS, String strYear, String strDC) {
        double dSumQMMC = 0;
        try {
            int iLeft = strJSGS.indexOf('(');// 最左边起始位置
            int iRight = strJSGS.indexOf("AccYear");// 最右边的位置
            int iMonth = strJSGS.indexOf(')');// 公式终点位置

            String strContext = strJSGS.substring(iLeft + 1, iRight - 2);// 公式内容,并去掉前面括号
            String strMonth = strJSGS.substring(iRight + 8, iMonth);// 公式内容,确定月份
            strMonth = strMonth.trim();

            if (strMonth.equals('1')) {

            } else {
                if (strDC.equals("D")) {
                    // 按照公式进行计算
                    strContext = strContext.replaceAll("'", "");
                    String[] strConte = strContext.split(",");
                    //资产负债表查询数据
                    List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                    Double dValue = 0.0;
                    for (Object obj : list) {
                        AccSumUtil accSum = (AccSumUtil) obj;
                        if (accSum.getQmmd() != null)
                            dValue += accSum.getQmmd().doubleValue();
                    }
                    dSumQMMC = dValue;
                } else {
                    // 按照公式进行计算
                    strContext = strContext.replaceAll("'", "");
                    String[] strConte = strContext.split(",");
                    List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                    Double dValue = 0.0;
                    for (Object obj : list) {
                        AccSumUtil accSum = (AccSumUtil) obj;
                        if (accSum.getQmmc() != null)
                            dValue += accSum.getQmmc().doubleValue();
                    }
                    dSumQMMC = dValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dSumQMMC;
    }

    public double CaclQCValue(String strJSGS, String strYear, String strDC) {
        double dSumQCMD = 0;

        try {
            int iLeft = strJSGS.indexOf('(');// 最左边起始位置
            int iRight = strJSGS.indexOf("AccYear");// 最右边的位置
            int iMonth = strJSGS.indexOf(')');// 公式终点位置
            // System.out.println(strJSGS);

            String strContext = strJSGS.substring(iLeft + 1, iRight - 2);// 公式内容,并去掉前面括号
            String strMonth = strJSGS.substring(iRight + 8, iMonth);// 公式内容,确定月份
            strMonth = strMonth.trim();
            // System.out.println(strContext);

            // if(strMonth.equals("1")){
            if (strDC.equals("D")) {
                // 按照公式进行计算
                strContext = strContext.replaceAll("'", "");
                String[] strConte = strContext.split(",");
                // 资产负债表查询数据
                List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                Double dValue = 0.0;
                if (list != null) {
                    for (Object obj : list) {
                        AccSumUtil accSum = (AccSumUtil) obj;
                        if (accSum.getQcmd() != null)
                            dValue += accSum.getQcmd();// 改Bigde数据类型后导航的
                    }
                }
                dSumQCMD = dValue;
            } else {
                // 按照公式进行计算
                strContext = strContext.replaceAll("'", "");
                String[] strConte = strContext.split(",");
                List list = prjDataMapper.findAllAccSum(dbSource, strConte, strYear, strMonth);

                Double dValue = 0.0;
                for (Object obj : list) {
                    AccSumUtil accSum = (AccSumUtil) obj;
                    if (accSum.getQcmc() != null)
                        dValue += accSum.getQcmc();
                }
                dSumQCMD = dValue;
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dSumQCMD;
    }


    public static void main(String[] str) {
        String strJSGS = "if(DFQC('2341',AccYear,1)<=0,0-DFQC('2341',AccYear,1),0)";
        // 如果计算公式中包含减号
        int iSubtractIf = strJSGS.indexOf('-');
        if (iSubtractIf > 0) {
            // 分割公式
            String strValues[] = strJSGS.split("-");
            int iCount = strValues.length;

            if (iCount == 2) {
                int ss = strJSGS.indexOf(')');
                int a = ss + 2;
                int aaa = strJSGS.indexOf(')');
                int aaaaa = strJSGS.lastIndexOf(')');
                strValues[0] = strValues[0].substring(3, strJSGS.indexOf(')') + 1);
                strValues[1] = strValues[1].substring(4, strJSGS.indexOf(')') + 2);
                float fSumQCMD1 = 0;// CaclQCValue(strValues[0],
                // strYear);
                float fSumQCMD2 = 0;// CaclQCValue(strValues[1],
                // strYear);
                float fSumQCMD = fSumQCMD1 - fSumQCMD2;

            }
        }
    }
}
