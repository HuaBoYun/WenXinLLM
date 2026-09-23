package com.huabo.fxgl.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.RepAtt;
import com.huabo.fxgl.entity.RiskAssplanAtt;
import com.huabo.fxgl.entity.RiskeventAtt;
import com.huabo.fxgl.entity.TblAttachment;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IBugAttService;
import com.huabo.fxgl.service.IRepAttService;
import com.huabo.fxgl.service.IRiskAssplanAttService;
import com.huabo.fxgl.service.IRiskeventAttService;
import com.huabo.fxgl.service.TblAttachmentService;
import com.huabo.fxgl.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 附件上传下载控制器
 * <p>提供风险管控模块的附件上传、下载、删除等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="附件上传/下载",description="附件上传/下载")
public class FileController{
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	private static final String DOCDIC = PropertyFileReader.getItem("file.path");
	private static final String separator = System.getProperty("file.separator");
	private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	@Autowired
    private TblAttachmentService tblAttachmentService;



    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private IRiskAssplanAttService riskAssplanAttService;
    @Autowired
    private IRiskeventAttService riskeventAttService;
    @Autowired
    private IRepAttService repAttService;
    @Autowired
    private IBugAttService bugAttService;
    
    @Resource
    private UserProvider userProvider;


//    private final List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL".split(","));

    @OperationLog(
            success = "删除附件处理成功",
            busType = "附件",
            fail = "删除附件处理失败",
            operationType = OperationType.DELETE,
            subType = "附件"
    )
    @RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @Operation(summary = "删除附件接口")
    public JsonBean delete(HttpServletRequest request, HttpServletResponse response, Model map,
                                      @RequestParam(value = "type", required = false) Integer type,
                                      @Parameter(name="moduleType",description="模块类型",required=true) @RequestParam(name = "moduleType", required = false) String moduleType,
                                      //@Parameter(name = "moduleId", description = "模块ID", required = true) @RequestParam(name = "moduleId", required = true) String moduleId,
                                      @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(name = "attId", required = true) String attId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        BigDecimal aid = null;

        JsonBean jsonBean = new JsonBean();

//        TblStaffUtil staff = userProvider.get();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }

        List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL".split(","));
        if (StringUtils.isBlank(moduleType) || !moduleList.contains(moduleType)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划");
            moduleMap.put("FXSJK", "风险数据库");
            moduleMap.put("FXBG", "风险报告");
            moduleMap.put("ZDYBG", "自定义报告");
            moduleMap.put("QXGL", "缺陷管理");
            return new JsonBean(400, "参数错误：未提供正确的模块类别(moduleType)", moduleMap);
        }
        if (StringUtils.isBlank(attId)) {
            return new JsonBean(400, "参数错误：未提供正确的附件ID(attId)", null);
        }
        /*if (StringUtils.isBlank(moduleId)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划ID");
            moduleMap.put("FXSJK", "风险ID");
            moduleMap.put("FXBG", "风险报告ID");
            moduleMap.put("ZDYBG", "自定义报告ID");
            moduleMap.put("QXGL", "缺陷ID");
            return new JsonBean(400, "参数错误：未提供正确的模块ID(moduleId)", moduleMap);
        }*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ATTID", attId);
        try {
            switch (moduleType) {
                case "PGJH": {
                    //删除风险计划映射的附件
                    /*queryWrapper.eq("ASSPLANID", moduleId);*/
                    riskAssplanAttService.remove(queryWrapper);
                    break;
                }
                case "FXSJK": {
                    //删除风险事件映射的附件
                    //queryWrapper.eq("RISEVEID", moduleId);
                    riskeventAttService.remove(queryWrapper);
                    break;
                }
                case "FXBG":
                case "ZDYBG": {
                    //删除风险报告映射的附件
                    //queryWrapper.eq("REPORTID", moduleId);
                    repAttService.remove(queryWrapper);
                    break;
                }
                case "QXGL": {
                    //删除缺陷映射的附件
                    //queryWrapper.eq("BUGID", moduleId);
                    bugAttService.remove(queryWrapper);
                    break;
                }
            }
            return new JsonBean(200, "删除附件成功！", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JsonBean(500, "删除附件时出错！", ex.getMessage());
        }
    }

    @OperationLog(
            success = "上传附件处理成功",
            busType = "附件",
            fail = "上传附件处理失败",
            operationType = OperationType.UPLOAD,
            subType = "附件"
    )
    @RequestMapping(value = "/uploadFileAttInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @Operation(summary = "上传附件接口")
    public JsonBean uploadFileAttInfo(HttpServletRequest request, HttpServletResponse response, Model map, MultipartFile file,
                                      @RequestParam(value = "type", required = false) Integer type,
                                      @Parameter(name="moduleType",description="模块类型",required=true) @RequestParam(name = "moduleType", required = false) String moduleType,
                                      @Parameter(name = "moduleId", description = "模块ID", required = true) @RequestParam(name = "moduleId", required = true) String moduleId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        BigDecimal aid = null;

        JsonBean jsonBean = new JsonBean();

//        TblStaffUtil staff = userProvider.get();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }

        List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL,FXYD,ZDFX".split(","));
        if (StringUtils.isBlank(moduleType) || !moduleList.contains(moduleType)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划");
            moduleMap.put("FXSJK", "风险数据库");
            moduleMap.put("FXBG", "风险报告");
            moduleMap.put("ZDFX", "重大风险");
            moduleMap.put("ZDYBG", "自定义报告");
            moduleMap.put("QXGL", "缺陷管理");
            moduleMap.put("FXYD", "风险应对");
            return new JsonBean(400, "参数错误：未提供正确的模块类别(moduleType)", moduleMap);
        }
        if (StringUtils.isBlank(moduleId)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划ID");
            moduleMap.put("FXSJK", "风险ID");
            moduleMap.put("FXBG", "风险报告ID");
            moduleMap.put("ZDFX", "重大风险");
            moduleMap.put("ZDYBG", "自定义报告ID");
            moduleMap.put("QXGL", "缺陷ID");
            moduleMap.put("FXYD", "风险应对ID");
            return new JsonBean(400, "参数错误：未提供正确的模块ID(moduleId)", moduleMap);
        }

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        try {
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Map<String, Object> resultMap = attachmentService.uploadAttachment(multiRequest, staffUtil.getStaffid().toString(), file);
                Attachment attachment = (Attachment) resultMap.get("attInfo");
                switch (moduleType) {
                    case "PGJH": {
                        //添加风险计划映射的附件
                        RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt(new BigDecimal(moduleId), attachment.getAttid());
                        riskAssplanAttService.save(riskAssplanAtt);
                        break;
                    }
                    case "FXSJK": {
                        //添加风险事件映射的附件
                        RiskeventAtt riskeventAtt = new RiskeventAtt(new BigDecimal(moduleId), attachment.getAttid());
                        riskeventAttService.save(riskeventAtt);
                        break;
                    }
                    case "FXBG":
                    case "ZDYBG": {
                        //添加风险报告映射的附件
                        RepAtt repAtt = new RepAtt(new BigDecimal(moduleId), attachment.getAttid());
                        repAttService.save(repAtt);
                        break;
                    }
                    case "QXGL": {
                        //添加缺陷映射的附件
//                        BugAtt bugAtt = new BugAtt(new BigDecimal(moduleId), attachment.getAttid());
//                        bugAttService.save(bugAtt);
                        tblAttachmentService.insertQxglFile(new BigDecimal(moduleId), attachment.getAttid());
                        break;
                    }
                    case "FXYD": {
                        //风险应对
                        tblAttachmentService.insertFxydFile(new BigDecimal(moduleId), attachment.getAttid());
                        break;
                    }
                    case "ZDFX":
                }
                return new JsonBean(200, "操作成功", resultMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, "操作异常", e.getMessage());
        }
        return new JsonBean(500, "上传失败", null);
    }

    /**
     * 文件下载
     */
    @OperationLog(
            success = "附件文件下载处理成功",
            busType = "附件",
            fail = "附件文件下载处理失败",
            operationType = OperationType.DOWNLOAD,
            subType = "附件"
    )
    @Operation(summary = "附件文件下载")
    @PostMapping(value = "/download", produces = "application/json; charset=utf-8")
    public void fileDownLoad(@RequestParam(value = "id",required = true)String id, HttpServletResponse httpServletResponse) {
        Attachment tblAttachmentEntity = attachmentService.getById(id);
        FtpUtil.downUploadFileNew(tblAttachmentEntity.toTblAttachment(), httpServletResponse);
    }



	 /**
     * @description 通用文件上传
     * @author lyz
     * @date 2022/4/15 14:57
     */
     @OperationLog(
             success = "上传接口处理成功",
             busType = "附件",
             fail = "上传接口处理失败",
             operationType = OperationType.UPLOAD,
             subType = "附件"
     )
    @PostMapping("/upload")
    @Operation(summary = "上传接口")
    public JsonBean fileUpload(HttpServletRequest request,
    		@Parameter(name = "file", description = "附件上传entity", required = true)MultipartFile[] file,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception {
        String attPath = "";
        TblAttachment tblAttachmentEntity = new TblAttachment();
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
        for (MultipartFile multipartFile : file) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                attPath = FtpUtil.uploadFilePath(imageName + "", inputStream);
                if (StrUtil.isEmpty(attPath)) {
                	return ResponseFormat.retParam(0,203,null);
                }
                tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
                tblAttachmentEntity.setAttpath(attPath + imageName);
                tblAttachmentEntity.setAttsize(new BigDecimal(multipartFile.getSize() / 1024).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                tblAttachmentEntity.setUploadtime(new Date());
                tblAttachmentEntity.setUploader(loginStaff.getRealname());
                tblAttachmentEntity.setAttname(fileName);
                tblAttachmentService.saveEntity(tblAttachmentEntity);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseFormat.retParam(0,30001,null);
            }
        }
        //返回当前添加的文件 前端回显
        return ResponseFormat.retParam(0,20006,null);
    }




    @OperationLog(
            success = "附件上传处理成功",
            busType = "风险评估",
            fail = "附件上传处理失败",
            operationType = OperationType.UPLOAD,
            subType = "附件上传"
    )
	@RequestMapping(value = "/upload2", method={RequestMethod.POST,RequestMethod.GET})
	  @ResponseBody
	  @Operation(summary = "风险评估/附件上传 /file/upload2")
	  public ModelAndView upload2(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
		      HttpServletRequest request, ModelMap map, String filecollbackurl) throws IOException{
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String myFileName = "";
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Map<String,List<MultipartFile>> fileMap = multiRequest.getMultiFileMap();
			// 取得request中的所有文件名
			System.out.println(multiRequest.getFileNames());

			for (Entry<String, List<MultipartFile>> entry : fileMap.entrySet()){

				for (MultipartFile file : entry.getValue()) {

				// 记录上传过程起始时的时间，用来计算上传时间
				int pre = (int) System.currentTimeMillis();
				// 取得上传文件
				//MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						String type = myFileName.substring(myFileName.indexOf("."), myFileName.length());
						// 重命名上传后的文件名
						long timeInMillis = Calendar.getInstance().getTimeInMillis();
						String fileName = timeInMillis+type;
						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
						String newname=myFileName.replace(oldname, timeInMillis+"");
						try {
							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
							if(flag){
								 logger.info("上传成功");
							}else{
								 logger.info("上传失败");
							}
						} catch (Exception e) {
						    e.printStackTrace();
						}

						System.out.println(myFileName);

						// 定义上传路径

						String path = DOCDIC + "/" + fileName;
						File localFile = new File(path);
						if (!localFile.exists()) {
							localFile.mkdirs();
						}
						file.transferTo(localFile);

						map.addAttribute("attname",myFileName);
						map.addAttribute("attsize", file.getSize());
						map.addAttribute("attpath", newname);
						map.addAttribute("fileName", myFileName);
						map.addAttribute("id", request.getParameter("id"));
					}
				}
				Enumeration<String> enu = request.getParameterNames();
				while (enu.hasMoreElements()) {
					String paraName = enu.nextElement();
					if (!paraName.equals("url")) {
						map.addAttribute(paraName, request.getParameter(paraName));
					}
					// System.out.println(paraName+": "+request.getParameter(paraName));
				}
				// 记录上传该文件后的时间
				int finaltime = (int) System.currentTimeMillis();
				System.out.println(finaltime - pre);
				}
			}

		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:"+filecollbackurl);
		return mv;
	}


    @OperationLog(
            success = "风险报告-报告编制 /和相关问题汇总-缺陷管理-风险发现附件上传处理成功",
            busType = "风险报告",
            fail = "风险报告-报告编制 /和相关问题汇总-缺陷管理-风险发现附件上传处理失败",
            operationType = OperationType.UPLOAD,
            subType = "报告编制"
    )
	@RequestMapping(value = "/uploadutf8")
	@Operation(summary = "风险报告-报告编制 /和相关问题汇总-缺陷管理-风险发现/file/uploadutf8")
	public String uploadutf8(HttpServletRequest request, HttpServletResponse response, String filecollbackurl, Model map){
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		String myFileName = "";
		// 判断 request 是否有文件上传,即多部分请求
		try {
			if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    int pre = (int) System.currentTimeMillis();
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        myFileName = file.getOriginalFilename();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (myFileName.trim() != "") {
                            System.out.println(myFileName);
							int lastIndexOf = myFileName.lastIndexOf(".");
                            String type = myFileName.substring(lastIndexOf);
                            // 重命名上传后的文件名
                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
                            String fileName = timeInMillis + type;
                            // 定义上传路径

                            String path = DOCDIC + "/" + fileName;
                            File localFile = new File(path);
                            if (!localFile.exists()) {
                                localFile.mkdirs();
                            }

    						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
    						String newname=myFileName.replace(oldname, timeInMillis+"");
    						try {
    							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
    							if(flag){
    								 logger.info("上传成功");
    							}else{
    								 logger.info("上传失败");
    							}
    						} catch (Exception e) {
    						    e.printStackTrace();
    						}
                           file.transferTo(localFile);
                            map.addAttribute("attname", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
                            map.addAttribute("attsize", file.getSize()/1000);
                            map.addAttribute("attpath", newname);
                            map.addAttribute("fileName", myFileName);
                            map.addAttribute("id", request.getParameter("id"));
                        }
                    }
                    Enumeration<String> enu = request.getParameterNames();
                    while (enu.hasMoreElements()) {
                        String paraName = enu.nextElement();
                        if (!paraName.equals("url")) {
                            map.addAttribute(paraName, request.getParameter(paraName));
                        }
                        // System.out.println(paraName+": "+request.getParameter(paraName));
                    }
                    // 记录上传该文件后的时间
                    int finaltime = (int) System.currentTimeMillis();
                    System.out.println(finaltime - pre);
                }

            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return new RedirectView(url,true,false,true);
		return "redirect:" + filecollbackurl;
	}
}
