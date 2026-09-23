package com.huabo.system.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.yy.Abnormal;
import com.huabo.system.entity.yy.Administrative;
import com.huabo.system.entity.yy.Annualreports;
import com.huabo.system.entity.yy.Auction;
import com.huabo.system.entity.yy.BaseInfos;
import com.huabo.system.entity.yy.Bond;
import com.huabo.system.entity.yy.Branch;
import com.huabo.system.entity.yy.Business;
import com.huabo.system.entity.yy.BusinessRisk;
import com.huabo.system.entity.yy.Capitals;
import com.huabo.system.entity.yy.Cert;
import com.huabo.system.entity.yy.CertDetail;
import com.huabo.system.entity.yy.ChangeInfo;
import com.huabo.system.entity.yy.ChangeRecord;
import com.huabo.system.entity.yy.Changeinformation;
import com.huabo.system.entity.yy.Chattel;
import com.huabo.system.entity.yy.Company;
import com.huabo.system.entity.yy.Competing;
import com.huabo.system.entity.yy.Copyright;
import com.huabo.system.entity.yy.Coreteam;
import com.huabo.system.entity.yy.Credit;
import com.huabo.system.entity.yy.Defendants;
import com.huabo.system.entity.yy.Details;
import com.huabo.system.entity.yy.Development;
import com.huabo.system.entity.yy.Dishonest;
import com.huabo.system.entity.yy.Enterprise;
import com.huabo.system.entity.yy.EquityChangeInfo;
import com.huabo.system.entity.yy.Executives;
import com.huabo.system.entity.yy.Executor;
import com.huabo.system.entity.yy.Financing;
import com.huabo.system.entity.yy.Judicialrisk;
import com.huabo.system.entity.yy.Keypersonnel;
import com.huabo.system.entity.yy.Legal;
import com.huabo.system.entity.yy.Managementcondition;
import com.huabo.system.entity.yy.Nodes;
import com.huabo.system.entity.yy.Notice;
import com.huabo.system.entity.yy.Noticecourt;
import com.huabo.system.entity.yy.OutGuaranteeInfo;
import com.huabo.system.entity.yy.Outbound;
import com.huabo.system.entity.yy.OutboundInvestment;
import com.huabo.system.entity.yy.Patentinformation;
import com.huabo.system.entity.yy.PawnInfo;
import com.huabo.system.entity.yy.PeopleInfos;
import com.huabo.system.entity.yy.Plaintiffs;
import com.huabo.system.entity.yy.Propertie;
import com.huabo.system.entity.yy.PrpductInformation;
import com.huabo.system.entity.yy.Publicopinion;
import com.huabo.system.entity.yy.Recruit;
import com.huabo.system.entity.yy.Relationship;
import com.huabo.system.entity.yy.Relationships;
import com.huabo.system.entity.yy.ReportSocialSecurity;
import com.huabo.system.entity.yy.Search;
import com.huabo.system.entity.yy.Serious;
import com.huabo.system.entity.yy.Shareholder;
import com.huabo.system.entity.yy.Spotcheck;
import com.huabo.system.entity.yy.Stock;
import com.huabo.system.entity.yy.Taxation;
import com.huabo.system.entity.yy.WebInfo;
import com.huabo.system.entity.yy.Websitefiling;
import com.huabo.system.entity.yy.YHttpclicent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


/**
 * 用友外部接口控制器
 * <p>提供与用友系统对接的企业核实、产品查询等API接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/api")
@Tag(name = "用友外部接口api", description = "用友外部接口api")
public class UfidaController  {
    private static final Log logger = LogFactory.getLog(UfidaController.class);
    public static Map<String, String[]> hxmap = new HashMap<String, String[]>();


    @RequestMapping(value = "/search", method = {RequestMethod.GET} )
    @Operation(summary="搜索")
    public JsonBean enterpriselist(HttpServletRequest request,
                                   @Parameter(name = "word", description = "关键字", required = false) @RequestParam(name = "word", required = false) String word) {
        try {
            logger.info("根据企业名称查询企业信息");
            //ModelAndView mv = new ModelAndView();

            //String name=request.getParameter("companyname");
            //String reportName = request.getParameter("reportName");
            word = word.replace("<em>", "");
            word = word.replace("</em>", "");
            //String mty = request.getParameter("mty");
            String result = YHttpclicent.findSerch(word);
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            Map<String, Object> map = new HashMap<>();
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                List<Search> list = JSON.parseObject(items, new TypeReference<List<Search>>() {
                });
                map.put("list", list);

                //	        mv.addObject("list", list);
                //	        mv.addObject("total", total);
                //	        mv.setViewName("yy/enterprise_list");
            }

//      mv.addObject("mty",mty);
//		mv.addObject("name", name);
//		return mv;
            return ResponseFormat.retParam(1, 200, map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "系统异常");
        }

    }

    /**
     * 企业画像
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/qyhx", method = {RequestMethod.GET} )
    @Operation(summary="企业画像")
    public JsonBean qyhx(HttpServletRequest request,
                         @Parameter(name = "name", description = "企业名称", required = true) @RequestParam(name = "name") String name,
                         @Parameter(name = "id", description = "企业id", required = true) @RequestParam(name = "id") String id,
                         @Parameter(name = "reportName", description = "报告名称", required = true) @RequestParam(name = "reportName") String reportName) {
        logger.info("根据企业名称查询企业画像");
        //ModelAndView mv = new ModelAndView();
        //String name=request.getParameter("name");
        name = name.replace("<em>", "");
        name = name.replace("</em>", "");
        //String id=request.getParameter("id");
        JsonBean jsonBean = this.productPfString(id);
        String[] qyhx = (String[]) jsonBean.getData();
        //mv.setViewName("yy/yyreport/qyhxcss");
        //saveprice("企业画像");
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", id);
        map.put("qypf1", qyhx[0]);
        map.put("qypf2", qyhx[1]);
        map.put("qypf3", qyhx[2]);
        map.put("qypf4", qyhx[3]);
        map.put("qypf5", qyhx[4]);
        map.put("qypfsum", qyhx[5]);
        map.put("tz1", qyhx[6]);
        map.put("tz2", qyhx[7]);
        map.put("tz3", qyhx[8]);
        map.put("tz4", qyhx[9]);
        map.put("tz5", qyhx[10]);
        //mv.addObject("name", name);
        //mv.addObject("id", id);
        //mv.addObject("qypf1", qyhx[0]);
        //mv.addObject("qypf2", qyhx[1]);
        //mv.addObject("qypf3", qyhx[2]);
        //mv.addObject("qypf4", qyhx[3]);
        //mv.addObject("qypf5", qyhx[4]);
        //mv.addObject("qypfsum", qyhx[5]);
        //mv.addObject("tz1", qyhx[6]);
        //mv.addObject("tz2", qyhx[7]);
        //mv.addObject("tz3", qyhx[8]);
        //mv.addObject("tz4", qyhx[9]);
        //mv.addObject("tz5", qyhx[10]);
        //String reportName = request.getParameter("reportName");
        saveprice(reportName);

        return ResponseFormat.retParam(1, 200, map);
    }

    /**
     * 产品简介
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/productPfString", method = {RequestMethod.GET} )
    @Operation(summary="产品简介")
    private JsonBean productPfString(@Parameter(name = "id", description = "企业id", required = true) @RequestParam(name = "id") String id) {
        String[] qyhx = null;
        if (hxmap.containsKey(id)) {
            qyhx = hxmap.get(id);
        } else {
            Random rand = new Random();
            //随机生成[0,1000)区间内的数字
            Integer qypf1 = rand.nextInt(50) + 50;
            Integer qypf2 = rand.nextInt(50) + 50;
            Integer qypf3 = rand.nextInt(50) + 50;
            Integer qypf4 = rand.nextInt(50) + 50;
            Integer qypf5 = rand.nextInt(50) + 50;
            Integer qypfsum = qypf1 + qypf2 + qypf3 + qypf4 + qypf5;
            //超过多少企业
            String[] tzlist1 = {"超过30%企业", "超过35%企业", "超过40%企业", "超过45%企业", "超过50%企业", "超过55%企业", "超过60%企业", "超过65%企业", "超过70%企业", "超过75%企业", "超过80%企业", "超过85%企业", "超过90%企业", "超过95%企业"};
            //企业发展阶段
            String[] tzlist2 = {"初创企业", "快速发展企业", "稳定发展企业", "持续发展期企业"};
            String[] tzlist3 = {"行业销售排名靠前", "行业销售排名落后", "行业销售中等排名"};
            String[] tzlist4 = {"经营良好", "经营不稳定", "经营稳定"};
            String[] tzlist5 = {"供应商实力强", "供应商一般", "供应商较弱"};
            String tz1 = tzlist1[rand.nextInt(13)];
            String tz2 = tzlist2[rand.nextInt(3)];
            String tz3 = tzlist3[rand.nextInt(3)];
            String tz4 = tzlist4[rand.nextInt(2)];
            String tz5 = tzlist5[rand.nextInt(2)];
            qyhx = new String[]{qypf1.toString(), qypf2.toString(), qypf3.toString(), qypf4.toString(), qypf5.toString(), qypfsum.toString(), tz1, tz2, tz3, tz4, tz5};
            hxmap.put(id, qyhx);
        }
        JsonBean jsonBean = ResponseFormat.retParam(1, 200, qyhx);
        return jsonBean;
    }

    /**
     * 保存查询记录并扣除查询费用
     *
     * @param name
     */
    private void saveprice(String name) {
		  /*TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
	      TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
	      try {
			this.tblYyUserQueryService.dealUserQuery(organization,tblStaff,name);
			} catch (Exception e) {
				e.printStackTrace();
			}*/


	       /* Tblyyprice tblyyprice = tblyypriceService.findByName(name);
	        Tblyyuser user=new Tblyyuser();
	        user.setCompay(organization);
	        user.setCreatedate(new Date());
	        user.setPrice(tblyyprice);
	        user.setUser(tblStaff);
	        tblyyuserService.saveOrupdate(user);*/
    }


    /**
     * 基本信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/findbyJbxx", method = {RequestMethod.GET} )
    @Operation(summary="基本信息")
    private JsonBean findbyJbxx(@Parameter(name = "companyid", description = "公司id", required = true) @RequestParam(name = "companyid") String companyid) {
        String result = YHttpclicent.findGSXX(companyid, "");
        JSONObject jsonObject = JSONObject.fromObject(result);
        Business context = null;
        String reason = jsonObject.getString("reason");
        //saveprice("基本信息");
        if (reason != null && reason.equals("ok")) {
            String resultall = jsonObject.getString("result");
            JSONObject resultobj = JSONObject.fromObject(resultall);
            JsonConfig jsonC = new JsonConfig();
            jsonC.setExcludes(new String[]{"staffList", "phoneList", "emailList"});
            JSONObject onezyspfx = JSONObject.fromObject(resultobj, jsonC);
            context = (Business) JSONObject.toBean(onezyspfx, Business.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long time = Long.parseLong(context.getFromTime());
            Date d = new Date(time);
            String string = sdf.format(d);
            context.setFromTime(string);
            if (context.getToTime() != null) {
                time = Long.parseLong(context.getToTime());
                d = new Date(time);
                string = sdf.format(d);
                context.setToTime(string);
            }

            if (context.getEstiblishTime() != null) {
                time = Long.parseLong(context.getEstiblishTime());
                d = new Date(time);
                string = sdf.format(d);
                context.setEstiblishTime(string);
            }

            if (context.getApprovedTime() != null) {
                time = Long.parseLong(context.getApprovedTime());
                d = new Date(time);
                string = sdf.format(d);
                context.setApprovedTime(string);
            }
	      /*  String string = context.getStaffList();
	        JSONObject staffobj = JSONObject.fromObject(string);
	        String staffresult=staffobj.getString("result");
	        List<Keypersonnel> list = JSON.parseObject(staffresult, new TypeReference<List<Keypersonnel>>(){});
	        context.setList(list);*/

        }

        return ResponseFormat.retParam(1, 200, context);
    }

    /**
     * 变更信息
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyBgxx", method = {RequestMethod.GET} )
    @Operation(summary="变更信息")
    private JsonBean findbyBgxx(@Parameter(name = "keyword", description = "企业id", required = true) @RequestParam("keyword") String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam("pageNum") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam("pageSize") String pageSize) {
        String result = YHttpclicent.findchange(companyid, "", 1);
        List<Changeinformation> list = null;
        JSONObject jsonObject = JSONObject.fromObject(result);
        String reason = jsonObject.getString("reason");
        //saveprice("变更记录");
        if (reason != null && reason.equals("ok")) {
            String resultall = jsonObject.getString("result");
            JSONObject resultobj = JSONObject.fromObject(resultall);
            String total = resultobj.getString("total");
            String items = resultobj.getString("items");
            list = JSON.parseObject(items, new TypeReference<List<Changeinformation>>() {
            });
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 企业年报
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyQynb", method = {RequestMethod.GET} )
    @Operation(summary="企业年报")
    private JsonBean findbyQynb(@Parameter(name = "id", description = "企业id", required = false) @RequestParam(value = "id", required = false) String id,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name) {
        List<Annualreports> list = null;
        try {
            String result = YHttpclicent.findQynb(id, "");
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            Map<String, Annualreports> map = new HashMap<String, Annualreports>();
            //saveprice("企业年报");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                list = new ArrayList<>();
                JsonConfig jsonC = new JsonConfig();
                jsonC.setExcludes(new String[]{"changeRecordList", "equityChangeInfoList", "reportSocialSecurityInfo", "outGuaranteeInfoList", "outboundInvestmentList", "shareholderList", "webInfoList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onezyspfx = JSONObject.fromObject(object, jsonC);
                    Annualreports company = (Annualreports) JSONObject.toBean(onezyspfx, Annualreports.class);

                    String portall = object.toString();

                    try {
                        company.setPortall(URLEncoder.encode(portall, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String baseInfo = object.getString("baseInfo");
                    JSONObject baseobj = JSONObject.fromObject(baseInfo);
                    BaseInfos base = (BaseInfos) JSONObject.toBean(baseobj, BaseInfos.class);
                    company.setBaseInfos(base);

                    String changeRecordList = object.getString("changeRecordList");
                    List<ChangeRecord> changeRecords = JSON.parseObject(changeRecordList, new TypeReference<List<ChangeRecord>>() {
                    });
                    company.setChangeRecords(changeRecords);

                    String equityChangeInfoList = object.getString("equityChangeInfoList");
                    List<EquityChangeInfo> changeInfos = JSON.parseObject(equityChangeInfoList, new TypeReference<List<EquityChangeInfo>>() {
                    });
                    company.setEquityChangeInfos(changeInfos);

                    String outGuaranteeInfoList = object.getString("outGuaranteeInfoList");
                    List<OutGuaranteeInfo> outGuaranteeInfos = JSON.parseObject(outGuaranteeInfoList, new TypeReference<List<OutGuaranteeInfo>>() {
                    });
                    company.setOutGuaranteeInfoList(outGuaranteeInfoList);

                    String outboundInvestmentList = object.getString("outboundInvestmentList");
                    List<OutboundInvestment> outboundInvestments = JSON.parseObject(outboundInvestmentList, new TypeReference<List<OutboundInvestment>>() {
                    });
                    company.setOutboundInvestments(outboundInvestments);

                    String shareholderList = object.getString("shareholderList");
                    List<Shareholder> shareholders = JSON.parseObject(shareholderList, new TypeReference<List<Shareholder>>() {
                    });
                    company.setShareholders(shareholders);

                    String webInfoList = object.getString("webInfoList");
                    List<WebInfo> webInfos = JSON.parseObject(webInfoList, new TypeReference<List<WebInfo>>() {
                    });
                    company.setWebInfos(webInfos);

                    String reportSocialSecurityInfo = object.getString("reportSocialSecurityInfo");
                    JSONObject reportSocialSecurityInfobj = JSONObject.fromObject(reportSocialSecurityInfo);
                    ReportSocialSecurity reportSocialSecuritie = (ReportSocialSecurity) JSONObject.toBean(reportSocialSecurityInfobj, ReportSocialSecurity.class);
                    company.setReportSocialSecurity(reportSocialSecuritie);

                    map.put(base.getReportYear(), company);

                    list.add(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);
    }

    @SuppressWarnings("unused")
    private Annualreports findbydetail(String json) {
        Annualreports company = null;
        if (json != null && json.length() > 0) {
            company = new Annualreports();
            try {
                String str = URLDecoder.decode(json, "utf-8");
                JSONObject object = JSONObject.fromObject(str);
                String baseInfo = object.getString("baseInfo");
                JSONObject baseobj = JSONObject.fromObject(baseInfo);
                BaseInfos base = (BaseInfos) JSONObject.toBean(baseobj, BaseInfos.class);
                company.setBaseInfos(base);

                String changeRecordList = object.getString("changeRecordList");
                List<ChangeRecord> changeRecords = JSON.parseObject(changeRecordList, new TypeReference<List<ChangeRecord>>() {
                });
                company.setChangeRecords(changeRecords);

                String equityChangeInfoList = object.getString("equityChangeInfoList");
                List<EquityChangeInfo> changeInfos = JSON.parseObject(equityChangeInfoList, new TypeReference<List<EquityChangeInfo>>() {
                });
                company.setEquityChangeInfos(changeInfos);

                String outGuaranteeInfoList = object.getString("outGuaranteeInfoList");
                List<OutGuaranteeInfo> outGuaranteeInfos = JSON.parseObject(outGuaranteeInfoList, new TypeReference<List<OutGuaranteeInfo>>() {
                });
                company.setOutGuaranteeInfoList(outGuaranteeInfoList);

                String outboundInvestmentList = object.getString("outboundInvestmentList");
                List<OutboundInvestment> outboundInvestments = JSON.parseObject(outboundInvestmentList, new TypeReference<List<OutboundInvestment>>() {
                });
                company.setOutboundInvestments(outboundInvestments);

                String shareholderList = object.getString("shareholderList");
                List<Shareholder> shareholders = JSON.parseObject(shareholderList, new TypeReference<List<Shareholder>>() {
                });
                company.setShareholders(shareholders);

                String webInfoList = object.getString("webInfoList");
                List<WebInfo> webInfos = JSON.parseObject(webInfoList, new TypeReference<List<WebInfo>>() {
                });
                company.setWebInfos(webInfos);

                String reportSocialSecurityInfo = object.getString("reportSocialSecurityInfo");
                JSONObject reportSocialSecurityInfobj = JSONObject.fromObject(reportSocialSecurityInfo);
                ReportSocialSecurity reportSocialSecuritie = (ReportSocialSecurity) JSONObject.toBean(reportSocialSecurityInfobj, ReportSocialSecurity.class);
                company.setReportSocialSecurity(reportSocialSecuritie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return company;
    }

    /**
     * 导出详情信息
     *
     * @param request
     * @param josnstr
     * @param mty
     * @param name
     * @return
     */
    @RequestMapping(value = "/exportdeail", method = {RequestMethod.GET} )
    @Operation(summary="导出详情信息")
    public JsonBean exportdeail(HttpServletRequest request,
                                @Parameter(name = "josnstr", description = "", required = true) @RequestParam("josnstr") String josnstr,
                                @Parameter(name = "mty", description = "", required = true) @RequestParam("mty") String mty,
                                @Parameter(name = "name", description = "", required = true) @RequestParam("name") String name) {
        //ModelAndView mv = new ModelAndView();
        //String josnstr=request.getParameter("josnstr");
        Annualreports annualreports = findbydetail(josnstr);
        Map<String, Object> map = new HashMap<>();


        if (annualreports != null) {
            //mv.addObject("annualreports", annualreports);
            map.put("annualreports", annualreports);

        }
        //mv.setViewName("yy/exportdeail");
        //mv.addObject("josnstr", josnstr);
        //mv.addObject("mty", request.getParameter("mty"));
        //mv.addObject("name", request.getParameter("gsname")+"_"+request.getParameter("year")+"年");
        map.put("josnstr", josnstr);
        map.put("mty", mty);
        map.put("name", name);

        return ResponseFormat.retParam(1, 200, map);
    }


    /**
     * 分支机构
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyFzjg", method = {RequestMethod.GET} )
    @Operation(summary="分支机构")
    private JsonBean findbyFzjg(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Branch> list = null;
        try {
            String result = YHttpclicent.findfzjg(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("分支机构");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Branch>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 主要人员
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyZyrr", method = {RequestMethod.GET} )
    @Operation(summary="主要人员")
    private JsonBean findbyZyrr(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Keypersonnel> list = null;
        try {
            String result = YHttpclicent.findzyrr(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("主要人员");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Keypersonnel>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);

    }

    /**
     * 股东信息
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyGdxx", method = {RequestMethod.GET} )
    @Operation(summary="股东信息")
    private JsonBean findbyGdxx(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Shareholder> list = null;
        try {
            String result = YHttpclicent.findgdxx(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("股东信息");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                list = new ArrayList<>();
                JsonConfig jsonC = new JsonConfig();
                jsonC.setExcludes(new String[]{"capitalActl", "capital"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onezyspfx = JSONObject.fromObject(object, jsonC);
                    Shareholder company = (Shareholder) JSONObject.toBean(onezyspfx, Shareholder.class);

                    String outboundInvestmentList = object.getString("capital");
                    List<Capitals> outboundInvestments = JSON.parseObject(outboundInvestmentList, new TypeReference<List<Capitals>>() {
                    });

                    if (outboundInvestments != null) {
                        String sj = "";
                        for (Capitals capitals : outboundInvestments) {
                            sj += capitals.getAmomon() + ",";
                        }
                        if (sj.indexOf(",") > 0) {
                            company.setCapital(sj.substring(0, sj.lastIndexOf(",")));
                        }
                    }

                    String rjstr = object.getString("capitalActl");
                    List<Capitals> lists = JSON.parseObject(rjstr, new TypeReference<List<Capitals>>() {
                    });

                    if (lists != null) {
                        String rj = "";
                        for (Capitals capitals : lists) {
                            rj += capitals.getAmomon() + ",";
                        }
                        if (rj.indexOf(",") > 0) {
                            company.setCapitalActl(rj.substring(0, rj.lastIndexOf(",")));
                        }

                    }

                    company.setCapitals(outboundInvestments);

                    list.add(company);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 对外投资
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyDwtz", method = {RequestMethod.GET} )
    @Operation(summary="对外投资")
    private JsonBean findbyDwtz(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Outbound> list = null;
        try {
            String result = YHttpclicent.findDwtz(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("对外投资");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Outbound>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);
    }


    /**
     * 招投标
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyManage", method = {RequestMethod.GET} )
    @Operation(summary="招投标")
    private JsonBean findbyManage(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                  @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                  @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Managementcondition> list = null;
        try {
            String result = YHttpclicent.findmanage(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("招投标");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Managementcondition>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, list);

    }

    /**
     * 税务评级
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbySwpj", method = {RequestMethod.GET} )
    @Operation(summary="税务评级")
    private JsonBean findbySwpj(@Parameter(name = "keyword", description = "关键字", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true) String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) String pageSize) {
        List<Taxation> list = null;
        try {
            String result = YHttpclicent.findswpj(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("税务评级");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Taxation>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }



    /**
     * 抽查检查
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyCcjc", method = {RequestMethod.GET} )
    @Operation(summary="抽查检查")
    private JsonBean findbyCcjc(@Parameter(name = "companyName", description = "公司名称", required = true) @RequestParam("companyName") String companyName) {
        String result = YHttpclicent.findccjc("", companyName, 1);
        List<Spotcheck> list = null;
        JSONObject jsonObject = JSONObject.fromObject(result);
        String reason = jsonObject.getString("reason");
        //saveprice("抽查检查");
        if (reason != null && reason.equals("ok")) {
            String resultall = jsonObject.getString("result");
            JSONObject resultobj = JSONObject.fromObject(resultall);
            String total = resultobj.getString("total");
            String items = resultobj.getString("items");
            list = JSON.parseObject(items, new TypeReference<List<Spotcheck>>() {
            });
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 债券信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyZqxx", method = {RequestMethod.GET} )
    @Operation(summary="债券信息")
    private JsonBean findbyZqxx(@Parameter(name = "keyword", description = "关键字", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true) String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) String pageSize) {
        List<Bond> list = null;
        try {
            String result = YHttpclicent.findzqxx("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("债券信息");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Bond>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 招聘
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyZP", method = {RequestMethod.GET} )
    @Operation(summary="招聘")
    private JsonBean findbyZP(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                              @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                              @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Recruit> list = null;
        try {
            String result = YHttpclicent.findzp("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("招聘");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Recruit>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 行政处罚
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyrisk", method = {RequestMethod.GET} )
    @Operation(summary="行政处罚")
    private JsonBean findbyrisk(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        String result = YHttpclicent.findRisk("", companyName, 1);
        List<BusinessRisk> list = null;
        JSONObject jsonObject = JSONObject.fromObject(result);
        String reason = jsonObject.getString("reason");
        //saveprice("行政处罚");
        if (reason != null && reason.equals("ok")) {
            String resultall = jsonObject.getString("result");
            JSONObject resultobj = JSONObject.fromObject(resultall);
            String total = resultobj.getString("total");
            String items = resultobj.getString("items");
            list = JSON.parseObject(items, new TypeReference<List<BusinessRisk>>() {
            });
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 行政处罚（信用中国）
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyXzcfzg", method = {RequestMethod.GET} )
    @Operation(summary="行政处罚（信用中国）")
    private JsonBean findbyXzcfzg(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                  @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                  @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Credit> list = null;
        try {
            String result = YHttpclicent.findxzcf(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("行政处罚--信用中国");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Credit>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 股权出质
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyGqcz", method = {RequestMethod.GET} )
    @Operation(summary="股权出质")
    private JsonBean findbyGqcz(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize) {
        List<Stock> list = null;
        try {
            String result = YHttpclicent.findgqcz("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("股权出质");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                list = new ArrayList<>();
                JsonConfig jsonConfig = new JsonConfig();
                // 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
                jsonConfig.setExcludes(new String[]{"pledgeeList", "targetCompany","companyList"});

                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onjne = JSONObject.fromObject(object, jsonConfig);
                    Stock company = (Stock) JSONObject.toBean(onjne, Stock.class);

				   /*String outboundInvestmentList = object.getString("companyList");
				   List<Company> outboundInvestments = JSON.parseObject(outboundInvestmentList, new TypeReference<List<Company>>(){});
				   company.setCompanyList(outboundInvestmentList);*/
                    list.add(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 严重违法
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyYzfw", method = {RequestMethod.GET} )
    @Operation(summary="严重违法")
    private JsonBean findbyYzfw(@Parameter(name = "keyword", description = "关键词", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Serious> list = null;
        try {
            String result = YHttpclicent.findyzwf("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("严重违法");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Serious>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 动产抵押
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyDcdy", method = {RequestMethod.GET} )
    @Operation(summary="动产抵押")
    private JsonBean findbyDcdy(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Chattel> list = null;
        try {
            String result = YHttpclicent.finddcdy("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            System.out.println(jsonObject+"====="+jsonObject.getString("reason"));
            String reason = jsonObject.getString("reason");
            //saveprice("动产抵押");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                list = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    Chattel company = new Chattel();


                    String baseInfo = object.getString("baseInfo");
                    JSONObject baseobj = JSONObject.fromObject(baseInfo);
                    BaseInfos base = (BaseInfos) JSONObject.toBean(baseobj, BaseInfos.class);
                    company.setBaseInfos(base);


                    String changeInfoList = object.getString("changeInfoList");
                    List<ChangeInfo> changeInfos = JSON.parseObject(changeInfoList, new TypeReference<List<ChangeInfo>>() {
                    });
                    company.setChangeInfos(changeInfos);

                    String pawnInfoList = object.getString("pawnInfoList");
                    List<PawnInfo> pawnInfos = JSON.parseObject(pawnInfoList, new TypeReference<List<PawnInfo>>() {
                    });
                    company.setPawnInfos(pawnInfos);

                    String peopleInfo = object.getString("peopleInfo");
                    List<PeopleInfos> peopleInfos = JSON.parseObject(peopleInfo, new TypeReference<List<PeopleInfos>>() {
                    });
                    company.setPeopleInfos(peopleInfos);
                    list.add(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 司法拍卖
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbySfpm", method = {RequestMethod.GET} )
    @Operation(summary="司法拍卖")
    private JsonBean findbySfpm(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {

            List<Auction> list = null;
        try {
            String result = YHttpclicent.findsfpm(companyid, "", 1);
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("司法拍卖");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                list = new ArrayList<>();
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.setExcludes(new String[]{"detail", "imgUrlList", "fileUrlList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject axnre = JSONObject.fromObject(object, jsonConfig);
                    Auction company = (Auction) JSONObject.toBean(axnre, Auction.class);

                    String peopleInfo = object.getString("detail");
                    List<Details> details = JSON.parseObject(peopleInfo, new TypeReference<List<Details>>() {
                    });
                    company.setDetails(details);
                    list.add(company);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 欠税公告
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyQsgg", method = {RequestMethod.GET} )
    @Operation(summary="欠税公告")
    private JsonBean findbyQsgg(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Notice> list = null;
        try {
            String result = YHttpclicent.findqsgg(companyid, "", 1);
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("欠税公告");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Notice>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 经营异常
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyJyyc", method = {RequestMethod.GET} )
    @Operation(summary="经营异常")
    private JsonBean findbyJyyc(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Abnormal> list = null;
        try {
            String result = YHttpclicent.findjyyc(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Abnormal>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        //saveprice("经营异常");
        return ResponseFormat.retParam(1, 200, list);
    }


    /**
     * 法院公告
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyJudirisk", method = {RequestMethod.GET} )
    @Operation(summary="法院公告")
    private JsonBean findbyJudirisk(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                    @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                    @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Judicialrisk> list = null;
        try {
            String result = YHttpclicent.findJudicialrisk("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("法院公告");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<Judicialrisk>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonC = new JsonConfig();
                jsonC.setExcludes(new String[]{"companyList", "party1StrApp", "party2StrApp"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onezyspfx = JSONObject.fromObject(object, jsonC);
                    Judicialrisk company = (Judicialrisk) JSONObject.toBean(onezyspfx, Judicialrisk.class);
                    String companyList = object.getString("companyList");
                    List<Company> companys = JSON.parseObject(companyList, new TypeReference<List<Company>>() {
                    });
                    company.setCompanys(companys);
                    list.add(company);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 失信人
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbySxr", method = {RequestMethod.GET} )
    @Operation(summary="失信人")
    private JsonBean findbySxr(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                               @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                               @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Dishonest> list = null;
        try {
            String result = YHttpclicent.findsxr("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Dishonest>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        //saveprice("失信人");
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 法律诉讼
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyFlss", method = {RequestMethod.GET} )
    @Operation(summary="法律诉讼")
    private JsonBean findbyFlss(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Legal> list = null;
        try {
            String result = YHttpclicent.findfvss("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Legal>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        //saveprice("法律诉讼");
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 被执行人
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyBzxr", method = {RequestMethod.GET} )
    @Operation(summary="被执行人")
    private JsonBean findbyBzxr(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Executor> list = null;
        try {
            String result = YHttpclicent.findbzxr(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Executor>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        //saveprice("被执行人");
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 开庭公告
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyKtgg", method = {RequestMethod.GET} )
    @Operation(summary="开庭公告")
    private JsonBean findbyKtgg(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Noticecourt> list = null;
        try {
            String result = YHttpclicent.findktgg(companyid, "", 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("开庭公告");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<Noticecourt>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"plaintiff", "defendant", "connList", "litigant2"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    Noticecourt company = (Noticecourt) JSONObject.toBean(onelxxx, Noticecourt.class);
                    String companyList = object.getString("plaintiff");
                    List<Plaintiffs> plaintiffs = JSON.parseObject(companyList, new TypeReference<List<Plaintiffs>>() {
                    });
                    company.setPlaintiff(plaintiffs);
                    String defendant = object.getString("defendant");
                    List<Defendants> defendants = JSON.parseObject(defendant, new TypeReference<List<Defendants>>() {
                    });
                    company.setDefendant(defendants);
                    list.add(company);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 投资事件
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyDeve", method = {RequestMethod.GET} )
    @Operation(summary="投资事件")
    private JsonBean findbyDeve(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Development> list = null;
        try {
            String result = YHttpclicent.finddeve("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("投资事件");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Development>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 融资历史
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyRzls", method = {RequestMethod.GET} )
    @Operation(summary="融资历史")
    private JsonBean findbyRzls(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Financing> list = null;
        try {
            String result = YHttpclicent.finddrzls("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("融资历史");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Financing>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }


    /**
     * 核心团队
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyHxtd", method = {RequestMethod.GET} )
    @Operation(summary="核心团队")
    private JsonBean findbyHxtd(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Coreteam> list = null;
        try {
            String result = YHttpclicent.findhxtd("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("核心团队");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Coreteam>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 竞品信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyJpxx", method = {RequestMethod.GET} )
    @Operation(summary="竞品信息")
    private JsonBean findbyJpxx(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Competing> list = null;
        try {
            String result = YHttpclicent.findjpxx("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            JsonConfig jsonConfig = new JsonConfig();
            // 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
            jsonConfig.setExcludes(new String[]{"portray","portrayStr"});
            //saveprice("竞品信息");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Competing>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 企业业务
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyQyxx", method = {RequestMethod.GET} )
    @Operation(summary="企业业务")
    private JsonBean findbyQyxx(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Enterprise> list = null;
        try {
            String result = YHttpclicent.findqyxx("", companyName, 1);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("企业业务");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Enterprise>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 舆情信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyOpein", method = {RequestMethod.GET} )
    @Operation(summary="舆情信息")
    private JsonBean findbyOpein(@Parameter(name = "endTime", description = "结束时间", required = false) @RequestParam(value = "endTime",required = false) String endTime,
                                 @Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String id,
                                 @Parameter(name = "name", description = "公司全名", required = false) @RequestParam(value = "name",required = false) String companyName,
                                 @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
                                 @Parameter(name = "pageSize", description = "每页条数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize,
                                 @Parameter(name = "startTime", description = "开始时间", required = false) @RequestParam(value = "startTime",required = false) String startTime,
                                 @Parameter(name = "type", description = "公司类型", required = false) @RequestParam(value = "type",required = false) String type) {
        List<Publicopinion> lists = null;
        try {
            String result = YHttpclicent.findopein(companyName);
            List<Publicopinion> list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("result");
            //saveprice("舆情信息");
            lists = new ArrayList<Publicopinion>();
            JsonConfig jsonConfig = new JsonConfig();
            // 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
            jsonConfig.setExcludes(new String[]{"tags"});
            if (reason != null && !reason.equals("null") && reason.length() > 0) {
                JSONObject totalobj = JSONObject.fromObject(reason);
                String total = totalobj.getString("total");

                if (total != null && !total.equals("0")) {
                    String resultall = totalobj.getString("items");
                    list = JSON.parseObject(resultall, new TypeReference<List<Publicopinion>>() {
                    });
                    for (Publicopinion publicopinion : list) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Long time = Long.parseLong(publicopinion.getRtm());
                        Date d = new Date(time);
                        String string = sdf.format(d);
                        publicopinion.setTime(string);
                        if (publicopinion.getUri() != null) {
                            publicopinion.setUrl(publicopinion.getUri());
                        }
                        lists.add(publicopinion);
                    }

                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }

        return ResponseFormat.retParam(1, 200, lists);
    }


    /**
     * 行政许可
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyXzxk", method = {RequestMethod.GET} )
    @Operation(summary="行政许可")
    private JsonBean findbyXzxk(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Administrative> list = null;
        try {
            String result =  YHttpclicent.findxzxk(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("行政许可");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Administrative>>() {
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 网站备案
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyWzba", method = {RequestMethod.GET} )
    @Operation(summary="网站备案")
    private JsonBean findbyWzba(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Websitefiling> list = null;
        try {
            String result = YHttpclicent.findwzba(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("网站备案");
            if (reason != null && reason.equals("ok")) {
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String items = resultobj.getString("items");
                list = JSON.parseObject(items, new TypeReference<List<Websitefiling>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }


    /**
     * 专利信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyZlxx", method = {RequestMethod.GET} )
    @Operation(summary="专利信息")
    private JsonBean findbyZlxx(@Parameter(name = "appDateBegin", description = "申请开始时间", required = false) @RequestParam(value = "appDateBegin",required = false) String appDateBegin,
                                @Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "appDateEnd", description = "申请结束时间", required = false) @RequestParam(value = "appDateEnd", required = false) String appDateEnd,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize,
                                @Parameter(name = "patentType", description = "专利类型", required = false) @RequestParam(value = "patentType", required = false) String patentType,
                                @Parameter(name = "pubDateBegin", description = "发布开始时间", required = false) @RequestParam(value = "pubDateBegin", required = false) String pubDateBegin,
                                @Parameter(name = "pubDateEnd", description = "发布结束时间", required = false) @RequestParam(value = "pubDateEnd", required = false) String pubDateEnd) {
        List<Patentinformation> list = null;
        try {
            String result = YHttpclicent.findzlxx(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("专利");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"connList", "lawStatus","priortyInfo"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    Patentinformation company = (Patentinformation) JSONObject.toBean(onelxxx, Patentinformation.class);
                    list.add(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }


    /**
     * 著作权
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyZzq", method = {RequestMethod.GET} )
    @Operation(summary="著作权")
    private JsonBean findbyZzq(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String keyword,
                               @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                               @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Copyright> list = null;
        try {
            String result = YHttpclicent.findzzq(keyword);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("著作权");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"connList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    Copyright company = (Copyright) JSONObject.toBean(onelxxx, Copyright.class);
                    Long time = Long.parseLong(company.getRegtime());
                    Date d = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String string = sdf.format(d);
                    company.setPublishdate(string);
                    list.add(company);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 产品信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyCpxx", method = {RequestMethod.GET} )
    @Operation(summary="产品信息")
    private JsonBean findbyCpxx(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<PrpductInformation> list = null;
        try {
            String result = YHttpclicent.findCpxx(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("产品信息");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"connList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    PrpductInformation company = (PrpductInformation) JSONObject.toBean(onelxxx, PrpductInformation.class);
                    list.add(company);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 证书
     *
     * @param
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyCert", method = {RequestMethod.GET} )
    @Operation(summary="证书")
    private JsonBean findbyCert(@Parameter(name = "certificateName", description = "证书类型", required = false) @RequestParam(value = "certificateName",required = false) String certificateName,
                                @Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Cert> list = null;
        try {
            String result = YHttpclicent.findCert(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("证书");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<Cert>();
                String resultall = jsonObject.getString("result");
                System.out.println(resultall);
                JSONObject resultobj = JSONObject.fromObject(resultall);
                String total = resultobj.getString("total");
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonC = new JsonConfig();
                jsonC.setExcludes(new String[]{"detail"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onezyspfx = JSONObject.fromObject(object, jsonC);
                    Cert cert = (Cert) JSONObject.toBean(onezyspfx, Cert.class);
                    String detail = object.getString("detail");
                    List<CertDetail> certdetail = JSON.parseObject(detail, new TypeReference<List<CertDetail>>() {
                    });
                    cert.setDetail(certdetail);
                    list.add(cert);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 高管信息
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findExecutives", method = {RequestMethod.GET} )
    @Operation(summary="高管信息")
    private JsonBean findExecutives(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                    @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                    @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                    @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Executives> list = null;
        try {
            String result = YHttpclicent.findExecutives(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("高管信息");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"connList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    Executives executives = (Executives) JSONObject.toBean(onelxxx, Executives.class);
                    list.add(executives);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 企业简介
     *
     * @param companyName
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findcompanyintroduct", method = {RequestMethod.GET} )
    @Operation(summary="企业简介")
    private JsonBean findcompanyintroduct(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                          @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName) {
        List<Executives> list = null;
        try {
            String result = YHttpclicent.findExecutives(companyName);
            list = null;
            JSONObject jsonObject = JSONObject.fromObject(result);
            String reason = jsonObject.getString("reason");
            //saveprice("企业简介");
            if (reason != null && reason.equals("ok")) {
                list = new ArrayList<>();
                String resultall = jsonObject.getString("result");
                JSONObject resultobj = JSONObject.fromObject(resultall);
                JSONArray array = resultobj.getJSONArray("items");
                JsonConfig jsonConfinright = new JsonConfig();
                jsonConfinright.setExcludes(new String[]{"connList"});
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    JSONObject onelxxx = JSONObject.fromObject(object, jsonConfinright);
                    Executives executives = (Executives) JSONObject.toBean(onelxxx, Executives.class);
                    list.add(executives);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 60001, "");
        }
        return ResponseFormat.retParam(1, 200, list);
    }

    /**
     * 关系图谱
     *
     * @param companyid
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/findbyRela", method = {RequestMethod.GET})
    @Operation(summary="关系图谱")
    private JsonBean findbyRela(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid) {
        String result = YHttpclicent.findRela(companyid);
        Relationship rela = null;
        JSONObject jsonObject = JSONObject.fromObject(result);
        String reason = jsonObject.getString("reason");
        if (reason != null && reason.equals("ok")) {
            rela = new Relationship();
            String resultall = jsonObject.getString("result");
            JSONObject resultobj = JSONObject.fromObject(resultall);
            JSONArray array = resultobj.getJSONArray("nodes");
            List<Nodes> nodes = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                Nodes node = new Nodes();//(Nodes) JSONObject.toBean(object, Nodes.class);
                node.setId(object.getString("id"));
                node.setLabels(object.getString("labels"));
                node.setProperties(object.getString("properties"));
                String properties = node.getProperties();
                JSONObject contentjsont = JSONObject.fromObject(properties);
                System.out.println("=======+++++++++"+contentjsont);
                Propertie properite = (Propertie) JSONObject.toBean(contentjsont, Propertie.class);
                System.out.println("=========__________"+properite);
                node.setPropertie(properite);
                nodes.add(node);
            }
            JSONArray array1 = resultobj.getJSONArray("relationships");
            List<Relationships> relationships = new ArrayList<>();
            for (int i = 0; i < array1.size(); i++) {
                JSONObject object = (JSONObject) array1.get(i);
                System.out.println(object);
                Relationships relation = new Relationships();//(Relationships) JSONObject.toBean(object, Relationships.class);
                relation.setId(object.getString("id"));
                relation.setProperties(object.getString("properties"));
                relation.setStartNode(object.getString("startNode"));
                relation.setType(object.getString("type"));
                relation.setEndNode(object.getString("endNode"));
                String properties = relation.getProperties();
                JSONObject contentjsont = JSONObject.fromObject(properties);
                Propertie propertie = new Propertie();
                propertie.setLabels(contentjsont.getString("labels"));
                System.out.println("============="+contentjsont);
                //Propertie properite = (Propertie) JSONObject.toBean(contentjsont, Propertie.class);
                relation.setPropertie(propertie);
                relationships.add(relation);
            }
            rela.setNodes(nodes);
            rela.setRelationships(relationships);
        }
        return ResponseFormat.retParam(1, 200, rela);
    }

}
