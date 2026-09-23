package com.huabo.fxgl.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Report;
import com.huabo.fxgl.entity.RiskAttWord;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IReportService;
import com.huabo.fxgl.service.IRiskAttWordService;
import com.huabo.fxgl.util.DocHtmlUtil;
import com.huabo.fxgl.util.FileUtil;
import com.huabo.fxgl.util.FreeMarkerUtil;
import com.huabo.fxgl.util.JavaPythonUtils;
import com.huabo.fxgl.util.SysConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import redis.clients.jedis.Jedis;

/**
 * 风险创建导出控制器
 * <p>提供风险识别中风险创建数据的导出接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/greprot")
@Tag(name="风险识别 - 风险创建 - 导出",description="风险识别 - 风险创建 - 导出")
public class GreprotController {

    private static final Logger logger = LoggerFactory.getLogger(RiskController.class);

    @Autowired
    private IRiskAttWordService riskAttWordService;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private IReportService reportService;
    @Autowired
    public FreeMarkerConfig freeMarkerConfig;

    @Resource
    private UserProvider userProvider;
    
    /**
     * 生成报告
     *
     *        public FreeMarkerConfig freeMarkerConfig;
     * @return
     */
    @OperationLog(
            success = "生成报告处理成功",
            busType = "风险识别",
            fail = "生成报告处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/generateReport",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "生成报告 /greprot/generateReport")
    @ResponseBody
    public JsonBean risk_generate_report(
    		@Parameter(name="reportType",description="报告类型, 风险模块默认为fygk",required=true) @RequestParam(required = true, defaultValue = "fygk",value="reportType") String reportType,
            @Parameter(name = "id", description = "主体信息主键 风险模块可以忽略不填") @RequestParam(required = false,value="id") String id,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        String orgId = selectOrg.getOrgid().toString();
        String orgName = selectOrg.getOrgname();
        Jedis jedis = JedisUtil.getJedis();// 获得redis客户端
        Map<String, String> redisMap = new HashMap<String, String>();

        if (StringUtils.isBlank(reportType)) {
            logger.info(orgName + "报告生成type为空");
            return new JsonBean(400, "reportType参数为null，无法继续进行", null);
        }

        String redisKey = "";
        String pypath = "";
        String pytype = "";
        String wordtype = "";
        String pdftype = "";
        String fileName = "";
        try {
            RiskAttWord trraw = riskAttWordService.getFile(reportType, orgId, id);
            // 删除原有报告文件
            if (trraw != null) {
                FtpUtil.removeFile(trraw.getFilename());
                riskAttWordService.removeById(trraw.getAttid());
            }
            List<String> list = new ArrayList<String>();
            list.add("python");
            String assid="";
            if (reportType.equals("fygk")) {
                redisKey = selectOrg.getOrgid().toString() + "zxbg";
                redisMap.put(redisKey, "s");
                jedis.hmset("pybg", redisMap);// python 生成报告redis

                pytype = "zxbg.py";
                wordtype = "咨询报告.docx";
                pdftype = "咨询报告.pdf";
                fileName = "咨询报告";
                // 调取flowid
//                String activitiService = SysConfig.get("activitiModelerUrl");
//                String flowids = httpURLConnectionPOST(activitiService+"/findActivitiPngInfo", orgId);
//                System.out.println(flowids);
//				if (StringUtils.isBlank(flowids)) {
//					logger.info(orgName + "流程文件为空");
//					return "1";
//				}
                pypath = SysConfig.get("pypath") + "/" + pytype;
                list.add(pypath);
                list.add(orgId);
//                if(flowids != null && !"".equals(flowids)) {
//                    list.add(JSON.toJSONString(flowids));
//                }else {
//                    list.add(null);
//                }

                assid="-1";

            } /*else if (reportType.equals("nkhg")) {
                if (StringUtils.isBlank(id)) {
                    logger.info(orgName + "报告生成id为空");
                    return "1";
                }
                redisKey = attribute.getOrgid() + id + "pjbg";
                redisMap.put(redisKey, "s");
                jedis.hmset("pybg", redisMap);// python 生成报告redis

                pytype = "pjbg.py";
                wordtype = "评价报告_" + id + ".docx";
                pdftype = "评价报告_" + id + ".pdf";
                fileName = "评价报告_" + id;


                pypath = SysConfig.get("pypath") + "/" + pytype;
                list.add(pypath);
                list.add(orgId);
                list.add(id);
                assid=id;
            }*/

            String username = staffUtil.getUsername();
            // 生成报告
            JavaPythonUtils.test1(list);
            String wordpath = SysConfig.get("wordpath");
            File filew = new File(wordpath + "/" + selectOrg.getOrgname() + wordtype);
            // 判断文件是否生成成功
            if (!filew.exists()) {
                logger.info(orgName + wordtype + "报告未生成");
                return new JsonBean(0, "报告未生成", null);
            }

            String pdfpath = SysConfig.get("pdfpath");
            // 将word文件转pdf
            DocHtmlUtil.file2pdf(filew, pdfpath, "docx", orgName + fileName);
            String fileNameTmp = selectOrg.getOrgname() + pdftype;
            String newName = Calendar.getInstance().getTimeInMillis() + ".pdf";
            File file = new File(pdfpath + "/" + fileNameTmp);
            if (!file.exists()) {
                logger.info(orgName + pdftype + "报告未生成");
                return new JsonBean(0, "报告未生成", null);
            }

            FileInputStream input = new FileInputStream(file);

            // String path = UploadFileUtil.Uploadfilepath;

            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",
                    IOUtils.toByteArray(input));
            // String url = FileUtils.fileUpload(multipartFile, path, fileName);
            // pdf文件上传
            FtpUtil.uploadFile(newName, multipartFile.getInputStream());
            Attachment ta = new Attachment();
            ta.setAttname(fileNameTmp);
            ta.setAttpath(newName);
            ta.setAttsize(new BigDecimal(file.length()));
            ta.setUploadtime(LocalDateTime.now());
            ta.setUploader(username);
//            AttachmentService service = (AttachmentService) SpringContextHolder.getBean("AttachmentService");
            attachmentService.save(ta);
            // TblRiskRiskAttWord trraw=new TblRiskRiskAttWord();

            trraw = new RiskAttWord();
            trraw.setAttid(ta.getAttid());
            trraw.setOrgid(selectOrg.getOrgid().toString());
            //trraw.setRiskId("-1");
            trraw.setFilename(fileNameTmp);
            trraw.setFilepath(newName);
            trraw.setFilesize(Long.toString(file.length()));
            trraw.setAssid(assid);
            riskAttWordService.save(trraw);

            input.close();
            // 删除word文件
            filew.delete();
            // 删除pdf文件
            file.delete();

            // String fkword = SysConfig.get("fkword");
            // File file = new File(fkword + "/"+attribute.getOrgname()+"企业报告.docx");
            // String fkpdf = SysConfig.get("fkpdf");
            // DocHtmlUtil.file2pdf(file, fkpdf, "docx", attribute.getOrgname());
            return new JsonBean(200, "报告已生成", null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new JsonBean(500, "生成报告时出错", null);
        } finally {
            jedis.hdel("pybg", redisKey);
            // jedis.del(redisKey);
        }
    }

    public static String httpURLConnectionPOST(String u, String data) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(u);// 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
            connection.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
            connection.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
            connection.setDoOutput(true);// 设置连接输出流为true,默认false
            connection.setDoInput(true);// 设置连接输入流为true
            connection.setRequestMethod("POST");// 设置请求方式为post
            connection.setUseCaches(false);// post请求缓存设为false
            connection.setInstanceFollowRedirects(true);// 设置该HttpURLConnection实例是否自动执行重定向
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");// 请求的content-type为application/x-www-form-urlencoded;
            // charset=UTF-8
            connection.connect();// 建立连接
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());// 创建输入输出流,用于往连接里面输出携带的参数
            String unit = "unitId=" + URLEncoder.encode(data, "utf-8");
            dataout.writeBytes(unit);
            dataout.flush();// 输出完成后刷新并关闭流
            dataout.close();
            // System.out.println(connection.getResponseCode());
            // 如果请求响应码是200，则表示成功
            if (connection.getResponseCode() == 200) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                bf.close();
            }
            connection.disconnect();// 断开连接
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @OperationLog(
            success = "导出前判断该文档是否存在处理成功",
            busType = "风险识别",
            fail = "导出前判断该文档是否存在处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/isfile",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "导出前判断该文档是否存在 /greprot/isfile")
    public JsonBean isfile(@Parameter(name="reportType",description="报告类型, 风险模块传入为fygk",required=true) @RequestParam(required = true,value="reportType") String reportType,
    		@Parameter(name = "id", description = "主体信息主键风险模块可以忽略不填") @RequestParam(required = false,value="id") String id,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        try {
            boolean bool = false;

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("orgid", selectOrg.getOrgid().toString());
            if ("fygk".equals(reportType)) {
                queryWrapper.eq("ASSID", "-1");
            }
            if ("nkhg".equals(reportType)) {
                queryWrapper.eq("ASSID", id);
            }

            queryWrapper.orderByDesc("ATTID");
            List<RiskAttWord> list = riskAttWordService.list(queryWrapper);
            if (list==null || list.size()==0) {
                return new JsonBean(0, "该报告在数据库中不存在", null);
            }
            RiskAttWord trraw = list.get(0);
//            bool = FtpUtil.isFTPFileExist(UploadFileUtil.Uploadfilepath + trraw.getFilepath());
            bool = FtpUtil.isFTPFileExist("/opt/ftp/constract/" + trraw.getFilepath());
            if (!bool) {
                return new JsonBean(0, "未找到该报告文件", null);
            } else {
                return new JsonBean(1, "可以导出该报告", null);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new JsonBean(0, "执行错误", null);
        }
    }

    @OperationLog(
            success = "报告编制-导出处理成功",
            busType = "风险识别",
            fail = "报告编制-导出处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险创建"
    )
    @GetMapping("/report/download")
    @Operation(summary = "报告编制-导出")
    public void in_meet_report_export(HttpServletRequest request, HttpServletResponse response,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                      @Parameter(name = "reportid", description = "报告编制主键", required = true)@RequestParam(value = "reportid", required = true) String reportid) throws Exception {
        Map<String, String> map = new HashMap<String,String>();
        Report report= reportService.getById(reportid);
        if ("".equals(report.getRepdesc())||report.getRepdesc()==null){
            report.setRepdesc("");
        }
        map.put("repdesc", report.getRepdesc());
        String fileName =report.getReportname()+".doc";
        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        Boolean flag =(Boolean) FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        if(!flag){//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH +"/"+ fileName;
        FileUtil.downLoad(fileName1, response, false, fileName);
        FileUtil.deleteFile(fileName);
    }



    @OperationLog(
            success = "导出风险报告的接口处理成功",
            busType = "风险识别",
            fail = "导出风险报告的接口处理失败",
            operationType = OperationType.DOWNLOAD,
            subType = "风险创建"
    )
    @RequestMapping(value = "/download",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "导出风险报告的接口 /greprot/download")
    public void download(HttpServletResponse response,
    		@Parameter(name="reportType",description="报告类型, 风险模块传入为fygk",required=true) @RequestParam(required = true,value="reportType") String reportType,
    		@Parameter(name = "id", description = "主体信息主键风险模块可以忽略不填") @RequestParam(required = false,value="id") String id,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        try {
            boolean bool = false;
            if (StringUtils.isBlank(reportType)) {
                return;
            }
            /*if (StringUtils.isBlank(reportType) || !"fygk".equals(reportType)) {
                logger.info(selectOrg.getOrgname() + "报告生成type为空");
                return new JsonBean(0, "报告类型参数错误", null);
            }*/

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("orgid", selectOrg.getOrgid());
            if ("fygk".equals(reportType)) {
                queryWrapper.eq("ASSID", id);
            }
            if ("nkhg".equals(reportType)) {
                queryWrapper.eq("ASSID", id);
            }
            queryWrapper.orderByDesc("ATTID");
            List<RiskAttWord> list = riskAttWordService.list(queryWrapper);

            RiskAttWord trraw = null;
            if (list!=null && list.size()>0) {
                trraw = list.get(0);
            }
            if (trraw != null)
                bool = FtpUtil.downUploadReportFile(trraw.getFilepath(), trraw.getFilename(), response);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
