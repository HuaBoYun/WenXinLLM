package com.huabo.contract.controller;

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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.yy.Abnormal;
import com.huabo.contract.entity.yy.Administrative;
import com.huabo.contract.entity.yy.Annualreports;
import com.huabo.contract.entity.yy.Auction;
import com.huabo.contract.entity.yy.BaseInfos;
import com.huabo.contract.entity.yy.Bond;
import com.huabo.contract.entity.yy.Branch;
import com.huabo.contract.entity.yy.Business;
import com.huabo.contract.entity.yy.BusinessRisk;
import com.huabo.contract.entity.yy.Capitals;
import com.huabo.contract.entity.yy.Cert;
import com.huabo.contract.entity.yy.CertDetail;
import com.huabo.contract.entity.yy.ChangeInfo;
import com.huabo.contract.entity.yy.ChangeRecord;
import com.huabo.contract.entity.yy.Changeinformation;
import com.huabo.contract.entity.yy.Chattel;
import com.huabo.contract.entity.yy.Company;
import com.huabo.contract.entity.yy.Competing;
import com.huabo.contract.entity.yy.Copyright;
import com.huabo.contract.entity.yy.Coreteam;
import com.huabo.contract.entity.yy.Credit;
import com.huabo.contract.entity.yy.Defendants;
import com.huabo.contract.entity.yy.Details;
import com.huabo.contract.entity.yy.Development;
import com.huabo.contract.entity.yy.Dishonest;
import com.huabo.contract.entity.yy.Enterprise;
import com.huabo.contract.entity.yy.EquityChangeInfo;
import com.huabo.contract.entity.yy.Executives;
import com.huabo.contract.entity.yy.Executor;
import com.huabo.contract.entity.yy.Financing;
import com.huabo.contract.entity.yy.Judicialrisk;
import com.huabo.contract.entity.yy.Keypersonnel;
import com.huabo.contract.entity.yy.Legal;
import com.huabo.contract.entity.yy.Managementcondition;
import com.huabo.contract.entity.yy.Nodes;
import com.huabo.contract.entity.yy.Notice;
import com.huabo.contract.entity.yy.Noticecourt;
import com.huabo.contract.entity.yy.OutGuaranteeInfo;
import com.huabo.contract.entity.yy.Outbound;
import com.huabo.contract.entity.yy.OutboundInvestment;
import com.huabo.contract.entity.yy.Patentinformation;
import com.huabo.contract.entity.yy.PawnInfo;
import com.huabo.contract.entity.yy.PeopleInfos;
import com.huabo.contract.entity.yy.Plaintiffs;
import com.huabo.contract.entity.yy.Propertie;
import com.huabo.contract.entity.yy.PrpductInformation;
import com.huabo.contract.entity.yy.Publicopinion;
import com.huabo.contract.entity.yy.Recruit;
import com.huabo.contract.entity.yy.Relationship;
import com.huabo.contract.entity.yy.Relationships;
import com.huabo.contract.entity.yy.ReportSocialSecurity;
import com.huabo.contract.entity.yy.Search;
import com.huabo.contract.entity.yy.Serious;
import com.huabo.contract.entity.yy.Shareholder;
import com.huabo.contract.entity.yy.Spotcheck;
import com.huabo.contract.entity.yy.Stock;
import com.huabo.contract.entity.yy.Taxation;
import com.huabo.contract.entity.yy.WebInfo;
import com.huabo.contract.entity.yy.Websitefiling;
import com.huabo.contract.util.YHttpclicent;

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
@Tag(name="用友外部接口api",description="用友外部接口api")
public class UfidaController  {
    private static final Log logger = LogFactory.getLog(UfidaController.class);
    public static Map<String, String[]> hxmap = new HashMap<String, String[]>();

    
    @Resource
    private UserProvider userProvider;

    @RequestMapping(value = "/search", method = {RequestMethod.GET} )
    @Operation(summary = "搜索")
    public JsonBean enterpriselist(HttpServletRequest request,
                                   @Parameter(name = "word", description = "关键字", required = false) @RequestParam(name = "word", required = false) String word) {
        try {
            logger.info("根据企业名称查询企业信息");
            //ModelAndView mv = new ModelAndView();
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }

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
    @Operation(summary = "企业画像")
    public JsonBean qyhx(HttpServletRequest request,
                         @Parameter(name = "name", description = "企业名称", required = true) @RequestParam(name = "name") String name,
                         @Parameter(name = "id", description = "企业id", required = true) @RequestParam(name = "id") String id,
                         @Parameter(name = "reportName", description = "报告名称", required = true) @RequestParam(name = "reportName") String reportName) throws Exception {
        logger.info("根据企业名称查询企业画像");
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "产品简介")
    private JsonBean productPfString(@Parameter(name = "id", description = "企业id", required = true) @RequestParam(name = "id") String id) throws Exception {
        String[] qyhx = null;
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "基本信息")
    private JsonBean findbyJbxx(@Parameter(name = "companyid", description = "公司id", required = true) @RequestParam(name = "companyid") String companyid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "变更信息")
    private JsonBean findbyBgxx(@Parameter(name = "keyword", description = "企业id", required = true) @RequestParam("keyword") String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam("pageNum") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam("pageSize") String pageSize) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "企业年报")
    private JsonBean findbyQynb(@Parameter(name = "id", description = "企业id", required = false) @RequestParam(value = "id", required = false) String id,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name) {
        List<Annualreports> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    private Annualreports findbydetail(String json) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return null;
        }
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
    @Operation(summary = "导出详情信息")
    public JsonBean exportdeail(HttpServletRequest request,
                                @Parameter(name="josnstr",description="",required=true) @RequestParam("josnstr") String josnstr,
                                @Parameter(name="mty",description="",required=true) @RequestParam("mty") String mty,
                                @Parameter(name="name",description="",required=true) @RequestParam("name") String name) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "分支机构")
    private JsonBean findbyFzjg(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
    	
    	
        List<Branch> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "主要人员")
    private JsonBean findbyZyrr(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Keypersonnel> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "股东信息")
    private JsonBean findbyGdxx(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Shareholder> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "对外投资")
    private JsonBean findbyDwtz(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Outbound> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "招投标")
    private JsonBean findbyManage(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                  @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String name,
                                  @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum) {
        List<Managementcondition> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "税务评级")
    private JsonBean findbySwpj(@Parameter(name = "keyword", description = "关键字", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true) String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) String pageSize) {
        List<Taxation> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "抽查检查")
    private JsonBean findbyCcjc(@Parameter(name = "companyName", description = "公司名称", required = true) @RequestParam("companyName") String companyName) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "债券信息")
    private JsonBean findbyZqxx(@Parameter(name = "keyword", description = "关键字", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true) String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true) String pageSize) {
        List<Bond> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "招聘")
    private JsonBean findbyZP(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                              @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                              @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Recruit> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "行政处罚")
    private JsonBean findbyrisk(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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
    @Operation(summary = "行政处罚（信用中国）")
    private JsonBean findbyXzcfzg(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id", required = false) String companyid,
                                  @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                  @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Credit> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "股权出质")
    private JsonBean findbyGqcz(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize) {
        List<Stock> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "严重违法")
    private JsonBean findbyYzfw(@Parameter(name = "keyword", description = "关键词", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Serious> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "动产抵押")
    private JsonBean findbyDcdy(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Chattel> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "司法拍卖")
    private JsonBean findbySfpm(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {

            List<Auction> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "欠税公告")
    private JsonBean findbyQsgg(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Notice> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "经营异常")
    private JsonBean findbyJyyc(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Abnormal> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "法院公告")
    private JsonBean findbyJudirisk(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                    @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                    @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Judicialrisk> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "失信人")
    private JsonBean findbySxr(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                               @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                               @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Dishonest> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "法律诉讼")
    private JsonBean findbyFlss(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Legal> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "被执行人")
    private JsonBean findbyBzxr(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Executor> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "开庭公告")
    private JsonBean findbyKtgg(@Parameter(name = "keyword", description = "关键词", required = false) @RequestParam(value = "keyword", required = false) String companyid,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true, defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true, defaultValue = "20") String pageSize) {
        List<Noticecourt> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "投资事件")
    private JsonBean findbyDeve(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Development> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "融资历史")
    private JsonBean findbyRzls(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Financing> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "核心团队")
    private JsonBean findbyHxtd(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Coreteam> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "竞品信息")
    private JsonBean findbyJpxx(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Competing> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "企业业务")
    private JsonBean findbyQyxx(@Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name",required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum) {
        List<Enterprise> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "舆情信息")
    private JsonBean findbyOpein(@Parameter(name = "endTime", description = "结束时间", required = false) @RequestParam(value = "endTime",required = false) String endTime,
                                 @Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String id,
                                 @Parameter(name = "name", description = "公司全名", required = false) @RequestParam(value = "name",required = false) String companyName,
                                 @Parameter(name = "pageNum", description = "当前页数", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
                                 @Parameter(name = "pageSize", description = "每页条数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize,
                                 @Parameter(name = "startTime", description = "开始时间", required = false) @RequestParam(value = "startTime",required = false) String startTime,
                                 @Parameter(name = "type", description = "公司类型", required = false) @RequestParam(value = "type",required = false) String type) throws Exception {
        List<Publicopinion> lists = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "行政许可")
    private JsonBean findbyXzxk(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Administrative> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "网站备案")
    private JsonBean findbyWzba(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Websitefiling> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "专利信息")
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
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "著作权")
    private JsonBean findbyZzq(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String keyword,
                               @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                               @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) throws Exception {
        List<Copyright> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "产品信息")
    private JsonBean findbyCpxx(@Parameter(name = "keyword", description = "关键字", required = true) @RequestParam(value = "keyword", required = true) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<PrpductInformation> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "证书")
    private JsonBean findbyCert(@Parameter(name = "certificateName", description = "证书类型", required = false) @RequestParam(value = "certificateName",required = false) String certificateName,
                                @Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Cert> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "高管信息")
    private JsonBean findExecutives(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                    @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName,
                                    @Parameter(name = "pageNum", description = "当前页数", required = true) @RequestParam(value = "pageNum", required = true,defaultValue = "1") String pageNum,
                                    @Parameter(name = "pageSize", description = "每页条数", required = true) @RequestParam(value = "pageSize", required = true,defaultValue = "20") String pageSize) {
        List<Executives> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "企业简介")
    private JsonBean findcompanyintroduct(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid,
                                          @Parameter(name = "name", description = "公司名称", required = false) @RequestParam(value = "name", required = false) String companyName) {
        List<Executives> list = null;
        try {
        	TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
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
    @Operation(summary = "关系图谱")
    private JsonBean findbyRela(@Parameter(name = "id", description = "公司id", required = false) @RequestParam(value = "id",required = false) String companyid) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
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


    /**
     * 公司背景
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/companybackground")
	public JsonBean companybackground(HttpServletRequest request,
									  @Parameter(name = "name", description = "企业名称", required = true) String name,
									  @Parameter(name = "id", description = "企业id", required = true) String id,
									  @Parameter(name = "mty", description = "mty", required = true) String mty ) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		try {

			//========1===公司背景=================
			//1.1基本信息
			Business business = findbyJbxx(id);
			//mv.addObject("business", business);
			map.put("business", business);
			//1.2个人信息
			List<Keypersonnel> keypersonnel = findbyZyrr(id);
			//mv.addObject("keypersonnel", keypersonnel);
			map.put("keypersonnel", keypersonnel);
			//1.3股东信息
			List<Shareholder> shareholder = findbyGdxx(id);
			//mv.addObject("shareholder", shareholder);
			map.put("shareholder", shareholder);
			//1.4对外投资
			List<Outbound> outbounds = findbyDwtz(id);
			if(outbounds!=null){
				List<Outbound> list=new ArrayList<Outbound>();
				for (Outbound outbound : outbounds) {
					if(outbound.getEstiblishTime()!=null && outbound.getEstiblishTime().length()>0){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Long time =Long.parseLong(outbound.getEstiblishTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						outbound.setEstiblishDate(string);
					}
					list.add(outbound);
				}
				outbounds.clear();
				outbounds=list;
			}
			//mv.addObject("outbound", outbounds);
			map.put("outbound", outbounds);
			//1.5变更信息
			List<Changeinformation> changeinformation = findbyBgxx(id);
			//mv.addObject("changeinformation", changeinformation);
			map.put("changeinformation", changeinformation);
			//1.6企业年报
			Map<String, Annualreports> annualreports = findbyQynb(id);
			//mv.addObject("annualreports", annualreports);
			map.put("annualreports", annualreports);
			//1.7分支机构
			List<Branch> branchs = findbyFzjg(id);
			if(branchs!=null){
				List<Branch> list=new ArrayList<Branch>();
				for (Branch branch2 : branchs) {
					if(branch2.getEstiblishTime()!=null && branch2.getEstiblishTime().length()>0){
						Long time =Long.parseLong( branch2.getEstiblishTime());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String string = sdf.format(d);
						branch2.setEstiblishDate(string);
					}
					list.add(branch2);
				}
				branchs.clear();
				branchs=list;
			}
			//mv.addObject("branch", branchs);
			map.put("branch", branchs);
			//mv.addObject("mty", request.getParameter("mty"));
			map.put("mty", mty);
			String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.setViewName("yy/companybackground");
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/


    /**
     * 经营状况
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/management_condition ")
	public JsonBean management_condition(HttpServletRequest request,
										 @Parameter(name = "name", description = "企业名称", required = true) String name,
										 @Parameter(name = "id", description = "企业id", required = true) String id,
										 @Parameter(name = "mty", description = "mty", required = true) String mty,
										 @Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		try {
			//2.1招投标
			List<Managementcondition> managementcondition = findbyManage(id);
			if(managementcondition!=null){
				List<Managementcondition> list=new ArrayList<>();
				for (Managementcondition managementcondition2 : managementcondition) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(managementcondition2.getCreateTime()!=null &&managementcondition2.getCreateTime().length()>0){
						Long time =Long.parseLong(managementcondition2.getCreateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						managementcondition2.setCreateDate(string);
					}
					if(managementcondition2.getUpdateTime()!=null &&managementcondition2.getUpdateTime().length()>0){
						Long time =Long.parseLong(managementcondition2.getUpdateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						managementcondition2.setUpdateDate(string);
					}
					if(managementcondition2.getPublishTime()!=null &&managementcondition2.getPublishTime().length()>0){
						Long time =Long.parseLong(managementcondition2.getPublishTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						managementcondition2.setPublishDate(string);
					}
					list.add(managementcondition2);
				}
				managementcondition.clear();
				managementcondition=list;
			}
			//mv.addObject("managementcondition", managementcondition);
			map.put("managementcondition", managementcondition);
			//2.2债券信息
			List<Bond> bond = findbyZqxx(name);
			if(bond!=null){
				List<Bond> list=new ArrayList<>();
				for (Bond bond2 : bond) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(bond2.getCreateTime()!=null &&bond2.getCreateTime().length()>0){
						Long time =Long.parseLong(bond2.getCreateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						bond2.setCreateDate(string);
					}
					if(bond2.getUpdateTime()!=null &&bond2.getUpdateTime().length()>0){
						Long time =Long.parseLong(bond2.getUpdateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						bond2.setUpdateDate(string);
					}
					if(bond2.getPublishTime()!=null &&bond2.getPublishTime().length()>0){
						Long time =Long.parseLong(bond2.getPublishTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						bond2.setPublishDate(string);
					}
					if(bond2.getBondTradeTime()!=null &&bond2.getBondTradeTime().length()>0){
						Long time =Long.parseLong(bond2.getBondTradeTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						bond2.setBondTradeDate(string);
					}
					if(bond2.getPublishExpireTime()!=null &&bond2.getPublishExpireTime().length()>0){
						Long time =Long.parseLong(bond2.getPublishExpireTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						bond2.setPublishExpireDatel(string);
					}
					list.add(bond2);
				}
				bond.clear();
				bond=list;
			}
			//mv.addObject("bond", bond);
			map.put("bond", bond);
			//2.3招聘
			List<Recruit> recruit = findbyZP(name);
			if(recruit!=null){
				List<Recruit> list =new ArrayList<>();
				for (Recruit recruit2 : recruit) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(recruit2.getCreateTime()!=null &&recruit2.getCreateTime().length()>0){
						Long time =Long.parseLong(recruit2.getCreateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						recruit2.setCreateDate(string);
					}
					if(recruit2.getUpdateTime()!=null &&recruit2.getUpdateTime().length()>0){
						Long time =Long.parseLong(recruit2.getUpdateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						recruit2.setUpdateDate(string);
					}
					list.add(recruit2);
				}
				recruit.clear();
				recruit=list;
			}
			//mv.addObject("recruit", recruit);
			map.put("recruit", recruit);
			//2.4税务评级
			List<Taxation> taxation = findbySwpj(id);
			//mv.addObject("taxation", taxation);
			map.put("taxation", taxation);
			//2.5抽查检查
			List<Spotcheck> spotcheck = findbyCcjc(name);
			//mv.addObject("spotcheck", spotcheck);
			map.put("spotcheck", spotcheck);
			//mv.addObject("mty",request.getParameter("mty"));
			map.put("mty",mty);
			//String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.setViewName("yy/management_condition");
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/

    /**
     * 经营风险
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/business_risk")
	public JsonBean business_risk(HttpServletRequest request,
								  @Parameter(name = "name", description = "企业名称", required = true) String name,
								  @Parameter(name = "id", description = "企业id", required = true) String id,
								  @Parameter(name = "mty", description = "mty", required = true) String mty,
								  @Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {

		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String, Object> map = new HashMap<>();
		try {

			//========3===经营风险=================
			//3.1经营异常
			List<Abnormal> abnormal = findbyJyyc(id);
			//mv.addObject("abnormal", abnormal);
			map.put("abnormal", abnormal);
			//3.2行政处罚
			List<BusinessRisk> businessRisk = findbyrisk(name);
			//mv.addObject("businessRisk", businessRisk);
			map.put("businessRisk", businessRisk);
			//3.3行政处罚-信用中国
			List<Credit> credit = findbyXzcfzg(id);
			//mv.addObject("credit", credit);
			map.put("credit", credit);
			//3.4严重违法
			List<Serious> serious = findbyYzfw(name);
			if(serious!=null){
				List<Serious> list=new ArrayList<>();
				for (Serious serious2 : serious) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(serious2.getRemoveDate()!=null && serious2.getRemoveDate().length()>0){
						Long time =Long.parseLong(serious2.getRemoveDate());
						Date d = new Date(time);
						String string = sdf.format(d);
						serious2.setRemoveTime(string);
					}
					if(serious2.getPutDate()!=null && serious2.getPutDate().length()>0){
						Long time =Long.parseLong(serious2.getPutDate());
						Date d = new Date(time);
						String string = sdf.format(d);
						serious2.setPutTime(string);
					}
					list.add(serious2);
				}
				serious.clear();
				serious=list;
			}
			//mv.addObject("serious", serious);
			map.put("serious", serious);
			//3.5股权出质
			List<Stock> stock = findbyGqcz(name);
			if(stock!=null){
				List<Stock> list=new ArrayList<>();
				for (Stock stock2 : stock) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(stock2.getRegDate()!=null && stock2.getRegDate().length()>0){
						Long time =Long.parseLong(stock2.getRegDate());
						Date d = new Date(time);
						String string = sdf.format(d);
						stock2.setRegTime(string);
					}
					if(stock2.getPutDate()!=null && stock2.getPutDate().length()>0){
						Long time =Long.parseLong(stock2.getPutDate());
						Date d = new Date(time);
						String string = sdf.format(d);
						stock2.setPutTime(string);
					}
					list.add(stock2);
				}
				stock.clear();
				stock=list;
			}
			//mv.addObject("stock", stock);
			map.put("stock", stock);
			//3.6动产抵押
			List<Chattel> chattel = findbyDcdy(name);
			//mv.addObject("chattel", chattel);
			map.put("chattel", chattel);
			//3.7欠税公告
			List<Notice> notice = findbyQsgg(id);
			//mv.addObject("notice", notice);
			map.put("notice", notice);
			//3.8司法拍卖
			List<Auction> auction = findbySfpm(id);
			//mv.addObject("auction", auction);
			map.put("auction", auction);
			//mv.addObject("mty",request.getParameter("mty"));
			map.put("mty",mty);
			//String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.setViewName("yy/business_risk");
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/


    /**
     * 司法风险
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/judicial_risk")
	public JsonBean judicial_risk(HttpServletRequest request,
								  @Parameter(name = "name", description = "企业名称", required = true) String name,
								  @Parameter(name = "id", description = "企业id", required = true) String id,
								  @Parameter(name = "mty", description = "mty", required = true) String mty,
								  @Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String, Object> map = new HashMap<>();
		try {
			//========4===司法风险=================
			//4.1法律诉讼
			List<Legal> legal = findbyFlss(name);
			if(legal!=null){
				List<Legal> list=new ArrayList<>();
				for (Legal legal2 : legal) {
					if(legal2.getSubmittime()!=null && legal2.getSubmittime().length()>0){
						Long time =Long.parseLong(legal2.getSubmittime());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						legal2.setSubmitDate(string);
					}
					list.add(legal2);
				}
				legal.clear();
				legal=list;
			}
			//mv.addObject("legal", legal);
			map.put("legal", legal);
			//4.2法院公告
			List<Judicialrisk> judicialrisk = findbyJudirisk(name);
			//mv.addObject("judicialrisk", judicialrisk);
			map.put("judicialrisk", judicialrisk);
			//4.3失信人
			List<Dishonest> dishonest = findbySxr(name);
			if(dishonest!=null){
				List<Dishonest> list=new ArrayList<>();
				for (Dishonest dishonest2 : dishonest) {
					if(dishonest2.getPublishdate()!=null && dishonest2.getPublishdate().length()>0){
						Long time =Long.parseLong(dishonest2.getPublishdate());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						dishonest2.setPublishtime(string);
					}
					list.add(dishonest2);
				}
				dishonest.clear();
				dishonest=list;
			}
			//mv.addObject("dishonest", dishonest);
			map.put("dishonest", dishonest);
			//4.4被执行人
			List<Executor> executor = findbyBzxr(id);
			if(executor!=null){
				List<Executor> list=new ArrayList<>();
				for (Executor executor2 : executor) {
					if(executor2.getCaseCreateTime()!=null && executor2.getCaseCreateTime().length()>0){
						Long time =Long.parseLong(executor2.getCaseCreateTime());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						executor2.setCaseCreateDate(string);
					}
					list.add(executor2);
					
				} 
				executor.clear();
				executor=list;
			}
			//mv.addObject("executor", executor);
			map.put("executor", executor);
			//4.5开庭公告
			List<Noticecourt> noticecourt = findbyKtgg(id);
			if(noticecourt!=null){
				List<Noticecourt> list=new ArrayList<>();
				for (Noticecourt noticecourt2 : noticecourt) {
					if(noticecourt2.getStartDate()!=null && noticecourt2.getStartDate().length()>0){
					Long time =Long.parseLong(noticecourt2.getStartDate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					noticecourt2.setStartTime(string);
					}
					list.add(noticecourt2);
				}
				noticecourt.clear();
				noticecourt=list;
			}
			//mv.addObject("noticecourt", noticecourt);
			map.put("noticecourt", noticecourt);
			//String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.setViewName("yy/judicial_risk");
			//mv.addObject("mty",request.getParameter("mty"));
			map.put("mty",mty);
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/


    /**
     * 发展信息
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/development_information")
	public JsonBean development_information(HttpServletRequest request,
												@Parameter(name = "name", description = "企业名称", required = true) String name,
												@Parameter(name = "id", description = "企业id", required = true) String id,
												@Parameter(name = "mty", description = "mty", required = true) String mty,
												@Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		try {
			//========5===发展信息=================
			//5.1融资历史
			List<Financing> financing = findbyRzls(name);
			if(financing!=null){
				List<Financing> list=new ArrayList<>();
				for (Financing financing2 : financing) {
					if(financing2.getDate()!=null && financing2.getDate().length()>0){
						Long time =Long.parseLong(financing2.getDate());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						financing2.setDatetime(string);
					}
					if(financing2.getPubTime()!=null && financing2.getPubTime().length()>0){
						Long time =Long.parseLong(financing2.getPubTime());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						financing2.setDatetime(string);
					}
					list.add(financing2);
				}
				financing.clear();
				financing=list;
			}
			
			//mv.addObject("financing", financing);
			map.put("financing", financing);
			//5.2核心团队
			List<Coreteam> coreteam = findbyHxtd(name);
			if(coreteam!=null){
				List<Coreteam> list=new ArrayList<>();
				for (Coreteam coreteam2 : coreteam) {
					if(coreteam2.getCreateTime()!=null && coreteam2.getCreateTime().length()>0){
						Long time =Long.parseLong(coreteam2.getCreateTime());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						coreteam2.setCreateDate(string);
					}
					list.add(coreteam2);
				}
				coreteam.clear();
				coreteam=list;
			}
			//.addObject("coreteam", coreteam);
			map.put("coreteam", coreteam);
			//5.3企业业务
			List<Enterprise> enterprise = findbyQyxx(name);
			if(enterprise!=null){
				List<Enterprise> list=new ArrayList<>();
				for (Enterprise enterprise2 : enterprise) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if(enterprise2.getCreateTime()!=null && enterprise2.getCreateTime().length()>0){
						Long time =Long.parseLong(enterprise2.getCreateTime());
						Date d = new Date(time);
						String string = sdf.format(d);
						enterprise2.setCreateDate(string);
					}
					if(enterprise2.getSetupDate()!=null && enterprise2.getSetupDate().length()>0){
						Long time =Long.parseLong(enterprise2.getSetupDate());
						Date d = new Date(time);
						String string = sdf.format(d);
						enterprise2.setSetupTime(string);
					}
					list.add(enterprise2);
				}
			}
			//mv.addObject("enterprise", enterprise);
			map.put("enterprise", enterprise);
			//5.4投资事件
			List<Development> development = findbyDeve(name);
			if(development!=null ){
				List<Development> list=new ArrayList<>();
				for (Development development2 : development) {
					if(development2.getTzdate()!=null && development2.getTzdate().length()>0){
						Long time =Long.parseLong(development2.getTzdate());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						development2.setTzTime(string);
					}
					list.add(development2);
				}
			}
			//mv.addObject("development", development);
			map.put("development", development);
			//5.5竞品信息
			List<Competing> competing = findbyJpxx(name);
			if(competing!=null){
				List<Competing> list=new ArrayList<>();
				for (Competing competing2 : competing) {
					if(competing2.getDate()!=null && competing2.getDate().length()>0){
						Long time =Long.parseLong(competing2.getDate());
						Date d = new Date(time);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String string = sdf.format(d);
						competing2.setDateTime(string);
					}
					list.add(competing2);
				}
				competing.clear();
				competing=list;
			}
			//mv.addObject("competing", competing);
			map.put("competing", competing);
			//String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.setViewName("yy/development_information");
			//mv.addObject("mty",request.getParameter("mty"));
			map.put("mty",mty);
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/

    /**
     * 舆情信息
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/opinion_information")
	public JsonBean opinion_information(HttpServletRequest request,
											@Parameter(name = "name", description = "企业名称", required = true) String name,
											@Parameter(name = "id", description = "企业id", required = true) String id,
											@Parameter(name = "mty", description = "mty", required = true) String mty,
											@Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		try {
			//========6===舆情信息=================
			List<Publicopinion> publicopinion = findbyOpein(name);
			//mv.addObject("publicopinion", publicopinion);
			map.put("publicopinion", publicopinion);
			//String reportName = request.getParameter("reportName");
			saveprice(reportName);
		} finally {
			//mv.addObject("mty",request.getParameter("mty"));
			map.put("mty",mty);
			//mv.setViewName("yy/opinion_information");
			//mv.addObject("name", name);
			map.put("name", name);
		}
		return ResponseFormat.retParam(1, 200, map);
	}*/
    /**
     * 风控报告
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/yyreport/riskcontrolreport")
	public JsonBean businessbasereport(HttpServletRequest request,
									   @Parameter(name = "name", description = "企业名称", required = true) String name,
									   @Parameter(name = "id", description = "企业id", required = true) String id,
									   @Parameter(name = "mty", description = "mty", required = true) String mty,
									   @Parameter(name = "reportName", description = "报告名称", required = true) String reportName) {
		logger.info("根据企业名称查询企业信息");
		//ModelAndView mv = new ModelAndView();
		//String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		//String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		try {
		
		//========1===公司背景=================
		//1.1基本信息
		Business business = findbyJbxx(id);
		//mv.addObject("business", business);
		map.put("business", business);
		//1.2个人信息
		*//**//*List<Keypersonnel> keypersonnel = findbyZyrr(id);
		//mv.addObject("keypersonnel", keypersonnel);
		map.put("keypersonnel", keypersonnel);
		//1.3股东信息
		List<Shareholder> shareholder = findbyGdxx(id);
		//mv.addObject("shareholder", shareholder);
		map.put("shareholder", shareholder);
		//1.4对外投资
		List<Outbound> outbounds = findbyDwtz(id);
		if(outbounds!=null){
			List<Outbound> list=new ArrayList<Outbound>();
			for (Outbound outbound : outbounds) {
				if(outbound.getEstiblishTime()!=null && outbound.getEstiblishTime().length()>0){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Long time =Long.parseLong(outbound.getEstiblishTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					outbound.setEstiblishDate(string);
				}
				list.add(outbound);
			}
			outbounds.clear();
			outbounds=list;
		}
		//mv.addObject("outbound", outbounds);
		map.put("outbound", outbounds);
		//1.5变更信息
		List<Changeinformation> changeinformation = findbyBgxx(id);
		//mv.addObject("changeinformation", changeinformation);
		map.put("changeinformation", changeinformation);
		//1.6企业年报
		Map<String, Annualreports> annualreports = findbyQynb(id);
		//mv.addObject("annualreports", annualreports);
		map.put("annualreports", annualreports);
		//1.7分支机构
		List<Branch> branchs = findbyFzjg(id);
		if(branchs!=null){
			List<Branch> list=new ArrayList<Branch>();
			for (Branch branch2 : branchs) {
				if(branch2.getEstiblishTime()!=null && branch2.getEstiblishTime().length()>0){
					Long time =Long.parseLong( branch2.getEstiblishTime());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String string = sdf.format(d);
					branch2.setEstiblishDate(string);
				}
				list.add(branch2);
			}
			branchs.clear();
			branchs=list;
		}
		//mv.addObject("branch", branchs);
		map.put("branch", branchs);
		//mv.addObject("mty", request.getParameter("mty"));
		map.put("mty",mty);

		//2.1招投标
		List<Managementcondition> managementcondition = findbyManage(id);
		if(managementcondition!=null){
			List<Managementcondition> list=new ArrayList<>();
			for (Managementcondition managementcondition2 : managementcondition) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(managementcondition2.getCreateTime()!=null &&managementcondition2.getCreateTime().length()>0){
					Long time =Long.parseLong(managementcondition2.getCreateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					managementcondition2.setCreateDate(string);
				}
				if(managementcondition2.getUpdateTime()!=null &&managementcondition2.getUpdateTime().length()>0){
					Long time =Long.parseLong(managementcondition2.getUpdateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					managementcondition2.setUpdateDate(string);
				}
				if(managementcondition2.getPublishTime()!=null &&managementcondition2.getPublishTime().length()>0){
					Long time =Long.parseLong(managementcondition2.getPublishTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					managementcondition2.setPublishDate(string);
				}
				list.add(managementcondition2);
			}
			managementcondition.clear();
			managementcondition=list;
		}
		//mv.addObject("managementcondition", managementcondition);
		map.put("managementcondition", managementcondition);
		//2.2债券信息
		List<Bond> bond = findbyZqxx(name);
		if(bond!=null){
			List<Bond> list=new ArrayList<>();
			for (Bond bond2 : bond) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(bond2.getCreateTime()!=null &&bond2.getCreateTime().length()>0){
					Long time =Long.parseLong(bond2.getCreateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					bond2.setCreateDate(string);
				}
				if(bond2.getUpdateTime()!=null &&bond2.getUpdateTime().length()>0){
					Long time =Long.parseLong(bond2.getUpdateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					bond2.setUpdateDate(string);
				}
				if(bond2.getPublishTime()!=null &&bond2.getPublishTime().length()>0){
					Long time =Long.parseLong(bond2.getPublishTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					bond2.setPublishDate(string);
				}
				if(bond2.getBondTradeTime()!=null &&bond2.getBondTradeTime().length()>0){
					Long time =Long.parseLong(bond2.getBondTradeTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					bond2.setBondTradeDate(string);
				}
				if(bond2.getPublishExpireTime()!=null &&bond2.getPublishExpireTime().length()>0){
					Long time =Long.parseLong(bond2.getPublishExpireTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					bond2.setPublishExpireDatel(string);
				}
				list.add(bond2);
			}
			bond.clear();
			bond=list;
		}
		//mv.addObject("bond", bond);
		map.put("bond", bond);
		//2.3招聘
		List<Recruit> recruit = findbyZP(name);
		if(recruit!=null){
			List<Recruit> list =new ArrayList<>();
			for (Recruit recruit2 : recruit) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(recruit2.getCreateTime()!=null &&recruit2.getCreateTime().length()>0){
					Long time =Long.parseLong(recruit2.getCreateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					recruit2.setCreateDate(string);
				}
				if(recruit2.getUpdateTime()!=null &&recruit2.getUpdateTime().length()>0){
					Long time =Long.parseLong(recruit2.getUpdateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					recruit2.setUpdateDate(string);
				}
				list.add(recruit2);
			}
			recruit.clear();
			recruit=list;
		}
		//mv.addObject("recruit", recruit);
		map.put("recruit", recruit);
		//2.4税务评级
		List<Taxation> taxation = findbySwpj(id);
		//mv.addObject("taxation", taxation);
		map.put("taxation", taxation);
		//2.5抽查检查
		List<Spotcheck> spotcheck = findbyCcjc(name);
		//mv.addObject("spotcheck", spotcheck);
		map.put("spotcheck", spotcheck);
		//========3===经营风险=================
		//3.1经营异常
		List<Abnormal> abnormal = findbyJyyc(id);
		//mv.addObject("abnormal", abnormal);
		map.put("abnormal", abnormal);
		//3.2行政处罚
		List<BusinessRisk> businessRisk = findbyrisk(name);
		//mv.addObject("businessRisk", businessRisk);
		map.put("businessRisk", businessRisk);
		//3.3行政处罚-信用中国
		List<Credit> credit = findbyXzcfzg(id);
		//mv.addObject("credit", credit);
		map.put("credit", credit);
		//3.4严重违法
		List<Serious> serious = findbyYzfw(name);
		if(serious!=null){
			List<Serious> list=new ArrayList<>();
			for (Serious serious2 : serious) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(serious2.getRemoveDate()!=null && serious2.getRemoveDate().length()>0){
					Long time =Long.parseLong(serious2.getRemoveDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					serious2.setRemoveTime(string);
				}
				if(serious2.getPutDate()!=null && serious2.getPutDate().length()>0){
					Long time =Long.parseLong(serious2.getPutDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					serious2.setPutTime(string);
				}
				list.add(serious2);
			}
			serious.clear();
			serious=list;
		}
		//mv.addObject("serious", serious);
		map.put("serious", serious);
		//3.5股权出质
		List<Stock> stock = findbyGqcz(name);
		if(stock!=null){
			List<Stock> list=new ArrayList<>();
			for (Stock stock2 : stock) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(stock2.getRegDate()!=null && stock2.getRegDate().length()>0){
					Long time =Long.parseLong(stock2.getRegDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					stock2.setRegTime(string);
				}
				if(stock2.getPutDate()!=null && stock2.getPutDate().length()>0){
					Long time =Long.parseLong(stock2.getPutDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					stock2.setPutTime(string);
				}
				list.add(stock2);
			}
			stock.clear();
			stock=list;
		}
		//mv.addObject("stock", stock);
		map.put("stock", stock);
		//3.6动产抵押
		List<Chattel> chattel = findbyDcdy(name);
		//mv.addObject("chattel", chattel);
		map.put("chattel", chattel);
		//3.7欠税公告
		List<Notice> notice = findbyQsgg(id);
		//mv.addObject("notice", notice);
		map.put("notice", notice);
		//3.8司法拍卖
		List<Auction> auction = findbySfpm(id);
		//mv.addObject("auction", auction);
		map.put("auction", auction);
		//========4===司法风险=================
		//4.1法律诉讼
		List<Legal> legal = findbyFlss(name);
		if(legal!=null){
			List<Legal> list=new ArrayList<>();
			for (Legal legal2 : legal) {
				if(legal2.getSubmittime()!=null && legal2.getSubmittime().length()>0){
					Long time =Long.parseLong(legal2.getSubmittime());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					legal2.setSubmitDate(string);
				}
				list.add(legal2);
			}
			legal.clear();
			legal=list;
		}
		//mv.addObject("legal", legal);
		map.put("legal", legal);
		//4.2法院公告
		List<Judicialrisk> judicialrisk = findbyJudirisk(name);
		//mv.addObject("judicialrisk", judicialrisk);
		map.put("judicialrisk", judicialrisk);
		//4.3失信人
		List<Dishonest> dishonest = findbySxr(name);
		if(dishonest!=null){
			List<Dishonest> list=new ArrayList<>();
			for (Dishonest dishonest2 : dishonest) {
				if(dishonest2.getPublishdate()!=null && dishonest2.getPublishdate().length()>0){
					Long time =Long.parseLong(dishonest2.getPublishdate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					dishonest2.setPublishtime(string);
				}
				list.add(dishonest2);
			}
			dishonest.clear();
			dishonest=list;
		}
		//mv.addObject("dishonest", dishonest);
		map.put("dishonest", dishonest);
		//4.4被执行人
		List<Executor> executor = findbyBzxr(id);
		if(executor!=null){
			List<Executor> list=new ArrayList<>();
			for (Executor executor2 : executor) {
				if(executor2.getCaseCreateTime()!=null && executor2.getCaseCreateTime().length()>0){
					Long time =Long.parseLong(executor2.getCaseCreateTime());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					executor2.setCaseCreateDate(string);
				}
				list.add(executor2);
				
			} 
			executor.clear();
			executor=list;
		}
		//mv.addObject("executor", executor);
		map.put("executor", executor);
		//4.5开庭公告
		List<Noticecourt> noticecourt = findbyKtgg(id);
		if(noticecourt!=null){
			List<Noticecourt> list=new ArrayList<>();
			for (Noticecourt noticecourt2 : noticecourt) {
				if(noticecourt2.getStartDate()!=null && noticecourt2.getStartDate().length()>0){
				Long time =Long.parseLong(noticecourt2.getStartDate());
				Date d = new Date(time);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String string = sdf.format(d);
				noticecourt2.setStartTime(string);
				}
				list.add(noticecourt2);
			}
			noticecourt.clear();
			noticecourt=list;
		}
		//mv.addObject("noticecourt", noticecourt);
		map.put("noticecourt", noticecourt);
		//========5===发展信息=================
		//5.1融资历史
		List<Financing> financing = findbyRzls(name);
		if(financing!=null){
			List<Financing> list=new ArrayList<>();
			for (Financing financing2 : financing) {
				if(financing2.getDate()!=null && financing2.getDate().length()>0){
					Long time =Long.parseLong(financing2.getDate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					financing2.setDatetime(string);
				}
				if(financing2.getPubTime()!=null && financing2.getPubTime().length()>0){
					Long time =Long.parseLong(financing2.getPubTime());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					financing2.setDatetime(string);
				}
				list.add(financing2);
			}
			financing.clear();
			financing=list;
		}
		
		//mv.addObject("financing", financing);
		map.put("financing", financing);
		//5.2核心团队
		List<Coreteam> coreteam = findbyHxtd(name);
		if(coreteam!=null){
			List<Coreteam> list=new ArrayList<>();
			for (Coreteam coreteam2 : coreteam) {
				if(coreteam2.getCreateTime()!=null && coreteam2.getCreateTime().length()>0){
					Long time =Long.parseLong(coreteam2.getCreateTime());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					coreteam2.setCreateDate(string);
				}
				list.add(coreteam2);
			}
			coreteam.clear();
			coreteam=list;
		}
		//mv.addObject("coreteam", coreteam);
		map.put("coreteam", coreteam);
		//5.3企业业务
		List<Enterprise> enterprise = findbyQyxx(name);
		if(enterprise!=null){
			List<Enterprise> list=new ArrayList<>();
			for (Enterprise enterprise2 : enterprise) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(enterprise2.getCreateTime()!=null && enterprise2.getCreateTime().length()>0){
					Long time =Long.parseLong(enterprise2.getCreateTime());
					Date d = new Date(time);
					String string = sdf.format(d);
					enterprise2.setCreateDate(string);
				}
				if(enterprise2.getSetupDate()!=null && enterprise2.getSetupDate().length()>0){
					Long time =Long.parseLong(enterprise2.getSetupDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					enterprise2.setSetupTime(string);
				}
				list.add(enterprise2);
			}
		}
		//mv.addObject("enterprise", enterprise);
		map.put("enterprise", enterprise);
		//5.4投资事件
		List<Development> development = findbyDeve(name);
		if(development!=null ){
			List<Development> list=new ArrayList<>();
			for (Development development2 : development) {
				if(development2.getTzdate()!=null && development2.getTzdate().length()>0){
					Long time =Long.parseLong(development2.getTzdate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					development2.setTzTime(string);
				}
				list.add(development2);
			}
		}
		//mv.addObject("development", development);
		map.put("development", development);
		//5.5竞品信息
		List<Competing> competing = findbyJpxx(name);
		if(competing!=null){
			List<Competing> list=new ArrayList<>();
			for (Competing competing2 : competing) {
				if(competing2.getDate()!=null && competing2.getDate().length()>0){
					Long time =Long.parseLong(competing2.getDate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					competing2.setDateTime(string);
				}
				list.add(competing2);
			}
			competing.clear();
			competing=list;
		}
		//mv.addObject("competing", competing);
		map.put("competing", competing);
		//========6===舆情信息=================
		List<Publicopinion> publicopinion = findbyOpein(name);
		//mv.addObject("publicopinion", publicopinion);
		map.put("publicopinion", publicopinion);
		//行政许可
		List<Administrative> xzxk = findbyXzxk(name);
		//mv.addObject("xzxk", xzxk);
		map.put("xzxk", xzxk);
		//网站备案
		List<Websitefiling> wzba = findbyWzba(name);
		//mv.addObject("wzba", wzba);
		map.put("wzba", wzba);
		//专利信息
		List<Patentinformation> zlxx = findbyZlxx(name);
		//mv.addObject("zlxx", zlxx);
		map.put("zlxx", zlxx);
		//著作权
		List<Copyright> zzq = findbyZzq(name);
		//mv.addObject("zzq", zzq);
		map.put("zzq", zzq);
		//产品信息
		List<PrpductInformation> cpxx = findbyCpxx(name);
		//mv.addObject("cpxx", cpxx);
		map.put("cpxx", cpxx);
		//证书
		List<Cert> cert = findbyCert(name);
		//mv.addObject("cert", cert);
		map.put("cert", cert);
		//企业高管
		List<Executives> executives = findExecutives(name);
		//mv.addObject("executives", executives);
		map.put("executives", executives);
		//String reportName = request.getParameter("reportName");
		saveprice(reportName);
		}finally {
			//mv.addObject("name", name);
			map.put("name", name);
			//mv.addObject("id", id);
			map.put("id", id);
			//mv.setViewName("yy/yyreport/report_lx_detail");
	}
		return ResponseFormat.retParam(1, 200, map);
		
	}*/

    /**
     * 贷后报告
     * @param request
     * @return
     */
/*  @RequestMapping(value = "/yyreport/afterLoanReport")
	public ModelAndView afterLoanReport(HttpServletRequest request) {
		logger.info("根据企业名称查询企业信息");
		ModelAndView mv = new ModelAndView("yy/yyreport/report_after_loan");
		String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		String id=request.getParameter("id");
		//========1===公司背景=================
		//1.1基本信息
		mv.addObject("name", name);
		mv.addObject("id", id);
		Business business = findbyJbxx(id);
		mv.addObject("business", business);
		//1.5变更信息
		List<Changeinformation> changeinformation = findbyBgxx(id);
		mv.addObject("changeinformation", changeinformation);
		//2.4税务评级
		List<Taxation> taxation = findbySwpj(id);
		mv.addObject("taxation", taxation);

		//========3===经营风险=================
		//3.1经营异常
		List<Abnormal> abnormal = findbyJyyc(id);
		mv.addObject("abnormal", abnormal);
		//3.2行政处罚
		List<BusinessRisk> businessRisk = findbyrisk(name);
		mv.addObject("businessRisk", businessRisk);
		//3.4严重违法
		List<Serious> serious = findbyYzfw(name);
		if(serious!=null){
			List<Serious> list=new ArrayList<>();
			for (Serious serious2 : serious) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(serious2.getRemoveDate()!=null && serious2.getRemoveDate().length()>0){
					Long time =Long.parseLong(serious2.getRemoveDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					serious2.setRemoveTime(string);
				}
				if(serious2.getPutDate()!=null && serious2.getPutDate().length()>0){
					Long time =Long.parseLong(serious2.getPutDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					serious2.setPutTime(string);
				}
				list.add(serious2);
			}
			serious.clear();
			serious=list;
		}
		mv.addObject("serious", serious);
		//3.7欠税公告
		List<Notice> notice = findbyQsgg(id);
		mv.addObject("notice", notice);
		//4.2法院公告
		List<Judicialrisk> judicialrisk = findbyJudirisk(name);
		mv.addObject("judicialrisk", judicialrisk);
		//4.3失信人
				List<Dishonest> dishonest = findbySxr(name);
				if(dishonest!=null){
					List<Dishonest> list=new ArrayList<>();
					for (Dishonest dishonest2 : dishonest) {
						if(dishonest2.getPublishdate()!=null && dishonest2.getPublishdate().length()>0){
							Long time =Long.parseLong(dishonest2.getPublishdate());
							Date d = new Date(time);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String string = sdf.format(d);
							dishonest2.setPublishtime(string);
						}
						list.add(dishonest2);
					}
					dishonest.clear();
					dishonest=list;
				}
				mv.addObject("dishonest", dishonest);
		//4.5开庭公告
		List<Noticecourt> noticecourt = findbyKtgg(id);
		if(noticecourt!=null){
			List<Noticecourt> list=new ArrayList<>();
			for (Noticecourt noticecourt2 : noticecourt) {
				if(noticecourt2.getStartDate()!=null && noticecourt2.getStartDate().length()>0){
				Long time =Long.parseLong(noticecourt2.getStartDate());
				Date d = new Date(time);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String string = sdf.format(d);
				noticecourt2.setStartTime(string);
				}
				list.add(noticecourt2);
			}
			noticecourt.clear();
			noticecourt=list;
		}
		mv.addObject("noticecourt", noticecourt);
		return mv;

	}
	*/


    /**
     * 贷前报告
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/yyreport/beforeLoanReport")
	public ModelAndView beforeLoanReport(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("yy/yyreport/report_before_loan");
		String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		String id=request.getParameter("id");
		Map<String,Object> map = new HashMap<>();
		//========1===公司背景=================
		//1.1基本信息
		mv.addObject("name", name);
		mv.addObject("id", id);
		Business business = findbyJbxx(id);
		mv.addObject("business", business);
		JsonBean jsonBean = this.productPfString(id);
		String[] qyhx = (String[]) jsonBean.getData();
		mv.addObject("qypfsum",qyhx[5]);

		return mv;
	}*/

    /**
     * 审贷报告 or 贷中报告
     * @param request
     * @return
     */
/*	@RequestMapping(value = "/yyreport/middleLoanReport")
	public ModelAndView middleLoanReport(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("yy/yyreport/report_middle_loan");
		String name=request.getParameter("name");
		name=name.replace("<em>", "");
		name=name.replace("</em>", "");
		String id=request.getParameter("id");
		//========1===公司背景=================
		//1.1基本信息
		mv.addObject("name", name);
		mv.addObject("id", id);
		Business business = findbyJbxx(id);
		mv.addObject("business", business);
		//1.3股东信息
		List<Shareholder> shareholder = findbyGdxx(id);
		mv.addObject("shareholder", shareholder);
		//企业高管
		List<Executives> executives = findExecutives(name);
		mv.addObject("executives", executives);
		//3.6动产抵押
		List<Chattel> chattel = findbyDcdy(name);
		mv.addObject("chattel", chattel);
		//3.5股权出质
		List<Stock> stock = findbyGqcz(name);
		if(stock!=null){
			List<Stock> list=new ArrayList<>();
			for (Stock stock2 : stock) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(stock2.getRegDate()!=null && stock2.getRegDate().length()>0){
					Long time =Long.parseLong(stock2.getRegDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					stock2.setRegTime(string);
				}
				if(stock2.getPutDate()!=null && stock2.getPutDate().length()>0){
					Long time =Long.parseLong(stock2.getPutDate());
					Date d = new Date(time);
					String string = sdf.format(d);
					stock2.setPutTime(string);
				}
				list.add(stock2);
			}
			stock.clear();
			stock=list;
		}
		mv.addObject("stock", stock);
		//3.7欠税公告
		List<Notice> notice = findbyQsgg(id);
		mv.addObject("notice", notice);
		//4.3失信人
		List<Dishonest> dishonest = findbySxr(name);
		if(dishonest!=null){
			List<Dishonest> list=new ArrayList<>();
			for (Dishonest dishonest2 : dishonest) {
				if(dishonest2.getPublishdate()!=null && dishonest2.getPublishdate().length()>0){
					Long time =Long.parseLong(dishonest2.getPublishdate());
					Date d = new Date(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String string = sdf.format(d);
					dishonest2.setPublishtime(string);
				}
				list.add(dishonest2);
			}
			dishonest.clear();
			dishonest=list;
		}
		mv.addObject("dishonest", dishonest);
		return mv;
	}*/
}
