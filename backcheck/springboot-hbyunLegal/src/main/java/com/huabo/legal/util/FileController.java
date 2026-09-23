package com.huabo.legal.util;
//package com.huabo.contract.util;
//
//import com.alibaba.fastjson.JSONObject;
//import com.hbfk.util.FtpUtil;
//
//import com.huabo.contract.oracle.entity.TblAttachment;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.math.BigDecimal;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.*;
//import java.util.Map.Entry;
//
//@Controller
//@RequestMapping(value = "/file")
//public class FileController extends BaseController {
//	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
//	private static final String DOCDIC = PropertyFileReader.getItem("file.path");
//	private static final String separator = System.getProperty("file.separator");
//
//	@RequestMapping(value = "/download")
//	public ResponseEntity<byte[]> download(BigDecimal id, HttpServletResponse response) throws IOException {
//		TblAttachment tblAttachment = this.tblAttachmentService.get(id);
//		String path = DOCDIC +separator+ tblAttachment.getAttpath();
//		File file = new File(path);
//		HttpHeaders headers = new HttpHeaders();
//		String fileName = processFileName(request, tblAttachment.getAttname());// 为了解决中文名称乱码问题
//		headers.setContentDispositionFormData("attachment", fileName);
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.set("Content-disposition", "attachment;filename=" + fileName);
//		headers.set("Content-Length", String.valueOf(file.length()));
//		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
//	}
//
//
//
////	@RequestMapping(value = "/downloadFtp/{type}", produces = "application/json; charset=utf-8")
////	public void downloadFtp(@PathVariable String type,BigDecimal id, HttpServletResponse response,HttpServletRequest request) {
////		boolean bool =false;
////		if(id!=null ){
////			TblAttachment tblAttachment = this.tblAttachmentService.get(id);
////			if(tblAttachment!=null){
////				if(type!=null && type.equals("upload")){
////					bool= FtpUtil.downUploadFile(tblAttachment, response);
////				}
////				if(type!=null && type.equals("uploadPython")){
////					bool= FtpUtil.downUploadFilePython(tblAttachment, response);
////				}
////				if(type!=null && type.equals("send")){
////					bool= FtpUtil.downloadSendFile(tblAttachment, response);
////				}
////				if(type!=null && type.equals("template")){
////					bool= FtpUtil.downloadTemplateFile(tblAttachment, response);
////				}
////			}else{
////				logger.info("附件不存在");
////			}
////		}else{
////			if(type!=null && type.equals("czsc")){
////				String fileName=request.getParameter("fileName");
////				String name=request.getParameter("name");
////				bool= FtpUtil.downUploadFile(name,fileName, response);
////			}
////		}
////		if(bool){
////			logger.info("下载成功");
////		}else{
////			logger.info("下载失败");
////		}
////
////	}
////	@RequestMapping(value = "/downloadFtpDocument", method={RequestMethod.POST,RequestMethod.GET})
////	@ResponseBody
////	public ModelAndView downloadFtpDocument(String type,BigDecimal id, HttpServletResponse response,HttpServletRequest request) {
////		boolean bool =false;
////		if(id!=null ){
////			TblAttachment tblAttachment = this.tblAttachmentService.get(id);
////			if(tblAttachment!=null){
////				if(type!=null && type.equals("upload")){
////					bool= FtpUtil.downUploadFileTo(tblAttachment, response,request);
////				}
////			}else{
////				logger.info("附件不存在");
////			}
////		}else{
////			if(type!=null && type.equals("czsc")){
////				String fileName=request.getParameter("fileName");
////				String name=request.getParameter("name");
////				bool= FtpUtil.downUploadFile(name,fileName, response);
////			}
////		}
////		ModelAndView view = new ModelAndView();
////		view.addObject("result",bool);
////		return view;
////
////	}
//
//
//	@RequestMapping(value = "/downloadFtprem/{type}", produces = "application/json; charset=utf-8")
//	public void downloadFtprem(@PathVariable String type,String name,String filename, HttpServletResponse response,HttpServletRequest request) {
//		if(name!=null && !name.equals("")){
//			boolean bool =false;
//			if(type!=null && type.equals("upload")){
//				bool= FtpUtil.downUploadFileName(name,filename, response);
//
//			}
//			if(bool){
//				logger.info("下载成功");
//			}else{
//				bool=FtpUtil.downUploadFileNameall(name,filename, response);
//				if(bool){
//					logger.info("下载成功");
//				}else{
//					logger.info("下载失败");
//				}
//
//			}
//		}else{
//			logger.info("附件不存在");
//		}
//
//	}
//
//
//
//
//
//
//	@RequestMapping(value = "/download1")
//	public ResponseEntity<byte[]> download1(String fileName, HttpServletResponse response) throws IOException {
//		String path = DOCDIC +separator+ fileName;
//		File file = new File(path);
//		HttpHeaders headers = new HttpHeaders();
//		String fileName1 = processFileName(request, fileName);// 为了解决中文名称乱码问题
//		headers.setContentDispositionFormData("attachment", fileName1);
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.set("Content-disposition", "attachment;filename=" + fileName1);
//		headers.set("Content-Length", String.valueOf(file.length()));
//		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
//	}
//
//	/**
//	 * @author tyb
//	 * 2016-8-26下午4:30:22
//	 * @Des:内部控制模板下载
//	 */
//	@RequestMapping(value = "/downloadTemplate")
//	public ResponseEntity<byte[]> downloadTemplate(String fileName, HttpServletResponse response) throws IOException {
//		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/data/nbkzTemplate/");
//		if (fileName.trim().equals("kzjz")) {
//			fileName = "内部控制（控制矩阵）导入模板.xls";
//		}else if (fileName.trim().equals("nwgd")) {
//			fileName = "内部控制（内外规）导入模板.xls";
//		}else if(fileName.trim().equals("nbkz")){
//			fileName = "内部控制（基本信息）导入模板.xls";
//		}else if(fileName.trim().equals("ng")){
//			fileName = "内部控制（规章制度）导入模板.xls";
//		}else if(fileName.trim().equals("wg")){
//			fileName = "内部控制（法律规章）导入模板.xls";
//		}else if(fileName.trim().equals("xmzl")){
//			fileName = "智能审计（项目资料准备信息）导入模板.xls";
//		}else if(fileName.trim().equals("sxtb")){
//			fileName = "填报模板.xlsx";
//		}else if(fileName.trim().equals("zzjg")){
//			fileName = "组织架构导入模板.xls";
//		}else if(fileName.trim().equals("jcsj")){
//			fileName = "基础数据导入模板.xls";
//		}
//		path = path + separator +fileName;
//		File file = new File(path);
//		HttpHeaders headers = new HttpHeaders();
//		String fileName1 = processFileName(request, fileName);// 为了解决中文名称乱码问题
//		headers.setContentDispositionFormData("attachment", fileName1);
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.set("Content-disposition", "attachment;filename=" + fileName1);
//		headers.set("Content-Length", String.valueOf(file.length()));
//		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
//	}
//
//	@RequestMapping("/upload")
//	public String upload(HttpServletRequest request, HttpServletResponse response, String filecollbackurl, Model map) throws IllegalStateException, IOException {
//		// 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//		String myFileName = "";
//		// 判断 request 是否有文件上传,即多部分请求
//		if (multipartResolver.isMultipart(request)) {
//			// 转换成多部分request
//			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//			Map<String,List<MultipartFile>> fileMap = multiRequest.getMultiFileMap();
//			// 取得request中的所有文件名
//			System.out.println(multiRequest.getFileNames());
//
//			for (Entry<String, List<MultipartFile>> entry : fileMap.entrySet()){
//
//				for (MultipartFile file : entry.getValue()) {
//
//				// 记录上传过程起始时的时间，用来计算上传时间
//				int pre = (int) System.currentTimeMillis();
//				// 取得上传文件
//				//MultipartFile file = multiRequest.getFile(iter.next());
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						String type = myFileName.substring(myFileName.indexOf("."), myFileName.length());
//						// 重命名上传后的文件名
//						long timeInMillis = Calendar.getInstance().getTimeInMillis();
//						String fileName = timeInMillis+type;
//						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
//						String newname=myFileName.replace(oldname, timeInMillis+"");
//						try {
//							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
//							if(flag){
//								 logger.info("上传成功");
//							}else{
//								 logger.info("上传失败");
//							}
//						} catch (Exception e) {
//						    e.printStackTrace();
//						}
//
//						System.out.println(myFileName);
//
//						// 定义上传路径
//
//						String path = DOCDIC + "/" + fileName;
//						File localFile = new File(path);
//						if (!localFile.exists()) {
//							localFile.mkdirs();
//						}
//						file.transferTo(localFile);
//
//						map.addAttribute("attname",myFileName);
//						map.addAttribute("attsize", file.getSize());
//						map.addAttribute("attpath", newname);
//						map.addAttribute("fileName", myFileName);
//						map.addAttribute("id", request.getParameter("id"));
//					}
//				}
//				Enumeration<String> enu = request.getParameterNames();
//				while (enu.hasMoreElements()) {
//					String paraName = enu.nextElement();
//					if (!paraName.equals("url")) {
//						map.addAttribute(paraName, request.getParameter(paraName));
//					}
//					// System.out.println(paraName+": "+request.getParameter(paraName));
//				}
//				// 记录上传该文件后的时间
//				int finaltime = (int) System.currentTimeMillis();
//				System.out.println(finaltime - pre);
//				}
//			}
//			/*Iterator<String> iter = multiRequest.getFileNames();
//			while (iter.hasNext()) {
//				// 记录上传过程起始时的时间，用来计算上传时间
//				int pre = (int) System.currentTimeMillis();
//				// 取得上传文件
//				MultipartFile file = multiRequest.getFile(iter.next());
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						System.out.println(myFileName);
//						String type = myFileName.substring(myFileName.indexOf("."), myFileName.length());
//						// 重命名上传后的文件名
//						String fileName = UUID.randomUUID().toString() + type;
//						// 定义上传路径
//
//						fileName = getDatePath() + fileName;
//						String path = DOCDIC + "/" + fileName;
//						File localFile = new File(path);
//						if (!localFile.exists()) {
//							localFile.mkdirs();
//						}
//						file.transferTo(localFile);
//						map.addAttribute("attname", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
//						map.addAttribute("attsize", file.getSize());
//						map.addAttribute("attpath", fileName);
//						map.addAttribute("fileName", myFileName);
//						map.addAttribute("id", request.getParameter("id"));
//					}
//				}
//				Enumeration<String> enu = request.getParameterNames();
//				while (enu.hasMoreElements()) {
//					String paraName = enu.nextElement();
//					if (!paraName.equals("url")) {
//						map.addAttribute(paraName, request.getParameter(paraName));
//					}
//					// System.out.println(paraName+": "+request.getParameter(paraName));
//				}
//				// 记录上传该文件后的时间
//				int finaltime = (int) System.currentTimeMillis();
//				System.out.println(finaltime - pre);
//			}*/
//
//		}
//		// return new RedirectView(url,true,false,true);
//		return "redirect:" + filecollbackurl;
//	}
//
//
//	@RequestMapping(value = "/uploadutf8")
//	public String uploadutf8(HttpServletRequest request, HttpServletResponse response, String filecollbackurl, Model map){
//		// 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//		String myFileName = "";
//		// 判断 request 是否有文件上传,即多部分请求
//		try {
//			if (multipartResolver.isMultipart(request)) {
//                // 转换成多部分request
//                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//                // 取得request中的所有文件名
//                Iterator<String> iter = multiRequest.getFileNames();
//                while (iter.hasNext()) {
//                    // 记录上传过程起始时的时间，用来计算上传时间
//                    int pre = (int) System.currentTimeMillis();
//                    // 取得上传文件
//                    MultipartFile file = multiRequest.getFile(iter.next());
//                    if (file != null) {
//                        // 取得当前上传文件的文件名称
//                        myFileName = file.getOriginalFilename();
//                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//                        if (myFileName.trim() != "") {
//                            System.out.println(myFileName);
//							int lastIndexOf = myFileName.lastIndexOf(".");
//                            String type = myFileName.substring(lastIndexOf);
//                            // 重命名上传后的文件名
//                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
//                            String fileName = timeInMillis + type;
//                            // 定义上传路径
//
//                            String path = DOCDIC + "/" + fileName;
//                            File localFile = new File(path);
//                            if (!localFile.exists()) {
//                                localFile.mkdirs();
//                            }
//
//    						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
//    						String newname=myFileName.replace(oldname, timeInMillis+"");
//    						try {
//    							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
//    							if(flag){
//    								 logger.info("上传成功");
//    							}else{
//    								 logger.info("上传失败");
//    							}
//    						} catch (Exception e) {
//    						    e.printStackTrace();
//    						}
//                           file.transferTo(localFile);
//                            map.addAttribute("attname", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
//                            map.addAttribute("attsize", file.getSize()/1000);
//                            map.addAttribute("attpath", newname);
//                            map.addAttribute("fileName", myFileName);
//                            map.addAttribute("id", request.getParameter("id"));
//                        }
//                    }
//                    Enumeration<String> enu = request.getParameterNames();
//                    while (enu.hasMoreElements()) {
//                        String paraName = enu.nextElement();
//                        if (!paraName.equals("url")) {
//                            map.addAttribute(paraName, request.getParameter(paraName));
//                        }
//                        // System.out.println(paraName+": "+request.getParameter(paraName));
//                    }
//                    // 记录上传该文件后的时间
//                    int finaltime = (int) System.currentTimeMillis();
//                    System.out.println(finaltime - pre);
//                }
//
//            }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// return new RedirectView(url,true,false,true);
//		return "redirect:" + filecollbackurl;
//	}
//
//	@RequestMapping(value = "/uploadutf8Document")
//	public String uploadutf8Document(HttpServletRequest request, HttpServletResponse response, String filecollbackurl, Model map,String savepath){
//		// 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//		String myFileName = "";
//		// 判断 request 是否有文件上传,即多部分请求
//		try {
//			if (multipartResolver.isMultipart(request)) {
//                // 转换成多部分request
//                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//                // 取得request中的所有文件名
//                Iterator<String> iter = multiRequest.getFileNames();
//                while (iter.hasNext()) {
//                    // 记录上传过程起始时的时间，用来计算上传时间
//                    int pre = (int) System.currentTimeMillis();
//                    // 取得上传文件
//                    MultipartFile file = multiRequest.getFile(iter.next());
//                    if (file != null) {
//                        // 取得当前上传文件的文件名称
//                        myFileName = file.getOriginalFilename();
//                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//                        if (myFileName.trim() != "") {
//                            System.out.println(myFileName);
//                            String type = myFileName.substring(myFileName.indexOf("."), myFileName.length());
//                            // 重命名上传后的文件名
//                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
//                            String fileName = timeInMillis + type;
//                            // 定义上传路径
//
//                            String path = DOCDIC + "/" +savepath+"/"+ fileName;
//                            File localFile = new File(path);
//                            if (!localFile.exists()) {
//                                localFile.mkdirs();
//                            }
//
//    						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
//    						String newname=myFileName.replace(oldname, timeInMillis+"");
//    						try {
//    							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
//    							if(flag){
//    								 logger.info("上传成功");
//    							}else{
//    								 logger.info("上传失败");
//    							}
//    						} catch (Exception e) {
//    						    e.printStackTrace();
//    						}
//                           file.transferTo(localFile);
//                            map.addAttribute("attname", URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
//                            map.addAttribute("attsize", file.getSize()/1000);
//                            map.addAttribute("attpath", newname);
//                            map.addAttribute("fileName", myFileName);
//                            map.addAttribute("id", request.getParameter("id"));
//                        }
//                    }
//                    Enumeration<String> enu = request.getParameterNames();
//                    while (enu.hasMoreElements()) {
//                        String paraName = enu.nextElement();
//                        if (!paraName.equals("url")) {
//                            map.addAttribute(paraName, request.getParameter(paraName));
//                        }
//                        // System.out.println(paraName+": "+request.getParameter(paraName));
//                    }
//                    // 记录上传该文件后的时间
//                    int finaltime = (int) System.currentTimeMillis();
//                    System.out.println(finaltime - pre);
//                }
//
//            }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// return new RedirectView(url,true,false,true);
//		return "redirect:" + filecollbackurl;
//	}
//
//	@RequestMapping(value = "/test")
//	public ModelAndView file() {
//		System.out.println("---------------------");
//		ModelAndView view = new ModelAndView();
//		view.setViewName("filetest");
//		return view;
//	}
//
//	private String getDatePath() {
//		Calendar c = Calendar.getInstance();
//		int year = c.get(Calendar.YEAR);
//		int month = c.get(Calendar.MONTH);
//		int date = c.get(Calendar.DATE);
//		return year +separator + (month + 1) +separator + date + separator;
//	}
//
//	public static String processFileName(HttpServletRequest request, String fileNames) {
//		String codedfilename = null;
//		try {
//			Integer index = fileNames.indexOf(".");
//			if (index < 0) {
//				fileNames = fileNames + ".xls";
//			}
//			String agent = request.getHeader("USER-AGENT");
//			if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie
//
//				String name = URLEncoder.encode(fileNames, "UTF8");
//
//				codedfilename = name;
//			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等
//
//				codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return codedfilename;
//	}
//	/**
//	  * @Author: TYB
//	  * @Date: 2017-03-14 下午 2:37
//	  * @Des: 计划管理 审批表单 删除附件 主要有表单id和附件id
//	  */
////	@RequestMapping(value = "/spbd_deletefile",produces = "application/json; charset=utf-8")
////	public @ResponseBody String spbd_deletefile(HttpServletRequest request){
////		String formid = request.getParameter("formid");
////		String attid = request.getParameter("attid");
////		if (StringUtils.isNotBlank(formid) && StringUtils.isNotBlank(attid)){
////			TblNbsjPlanForm form = tblNbsjPlanFormService.get(formid);
////			TblAttachment att = attachmentService.findById(attid);
////			form.getTblAttachments().remove(att);
////			tblNbsjPlanFormService.updateNbsjPlanForm(form);
////			attachmentService.delete(att);
////			return JsonBean.success();
////		}
////		return JsonBean.error("删除失败");
////	}
//	/**
//	 * 账簿数据操作按钮后下载excel
//	 * @param tblAttachment
//	 * @param response
//	 * @param request
//	 */
//	@RequestMapping(value = "/downloadFtp/loadZbsjCwzt", produces = "application/json; charset=utf-8")
//	public void loadZbsjCwzt(TblAttachment tblAttachment, HttpServletResponse response,HttpServletRequest request) {
//		/*boolean bool =false;*/
//		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/data/excel/");
//		 response.reset();
//	     OutputStream toClient=null;
//	     try {
//	        //设置response的header
//	    	response.setCharacterEncoding("GBK");
//     		response.addHeader("Content-Length", ""+tblAttachment.getAttsize());
//     		response.addHeader("Content-Disposition", "attachment;filename=" + new String(tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" ));
//	        //以流的形式下载文件
//	        InputStream ins = new BufferedInputStream(new FileInputStream(path+"/"+tblAttachment.getAttname()));
//    		response.reset();
//    		response.setCharacterEncoding("GBK");
//    		response.addHeader("Content-Length", ""+tblAttachment.getAttsize());
//    		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" ));
//    		toClient = new BufferedOutputStream(response.getOutputStream());
//    		response.setContentType("application/octet-stream");
//    		byte[] buffer = new byte[1024];
//            int bytesToRead = -1;
//            // 通过循环将读入的Word文件的内容输出到浏览器中
//            while((bytesToRead = ins.read(buffer)) != -1) {
//            	toClient.write(buffer, 0, bytesToRead);
//             }
//    		toClient.write(buffer);
//    		ins.close();
//    		toClient.flush();
//            toClient.close();
//
//
//
//
//	     } catch (Exception e) {
//	        e.printStackTrace();
//	     }finally{
//	        if(toClient!=null){
//	            try {
//	               toClient.close();
//	            } catch (IOException e) {
//	               e.printStackTrace();
//	            }
//	        }
//	    }
//		/*bool = FtpUtil.downUploadFile(tblAttachment, response);
//		if(bool){
//			logger.info("下载成功");
//		}else{
//			logger.info("下载失败");
//		}*/
//	}
////	@RequestMapping(value = "/upload", method={RequestMethod.POST,RequestMethod.GET})
////	  @ResponseBody
////	  public ModelAndView upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
////	      HttpServletRequest request, ModelMap map) {
////	    String message = "";
////	    FileEntity2 entity = new FileEntity2();
////	    FileUploadTool fileUploadTool = new FileUploadTool();
////	    try {
////	      entity = fileUploadTool.createFile(multipartFile, request);
////	      if (entity != null) {
//////	        service.saveFile(entity);
////	        message = "上传成功";
////	        map.put("entity", entity);
////	        map.put("result", message);
////	      } else {
////	        message = "上传失败";
////	        map.put("result", message);
////	      }
////	    } catch (Exception e) {
////	      e.printStackTrace();
////	    }
////	    return new ModelAndView("result", map);
////	  }
//
//	@RequestMapping(value = "/upload2", method={RequestMethod.POST,RequestMethod.GET})
//	  @ResponseBody
//	  public ModelAndView upload2(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
//		      HttpServletRequest request, ModelMap map, String filecollbackurl) throws IOException{
//		// 创建一个通用的多部分解析器
//		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//		String myFileName = "";
//		// 判断 request 是否有文件上传,即多部分请求
//		if (multipartResolver.isMultipart(request)) {
//			// 转换成多部分request
//			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//			Map<String,List<MultipartFile>> fileMap = multiRequest.getMultiFileMap();
//			// 取得request中的所有文件名
//			System.out.println(multiRequest.getFileNames());
//
//			for (Entry<String, List<MultipartFile>> entry : fileMap.entrySet()){
//
//				for (MultipartFile file : entry.getValue()) {
//
//				// 记录上传过程起始时的时间，用来计算上传时间
//				int pre = (int) System.currentTimeMillis();
//				// 取得上传文件
//				//MultipartFile file = multiRequest.getFile(iter.next());
//				if (file != null) {
//					// 取得当前上传文件的文件名称
//					myFileName = file.getOriginalFilename();
//					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//					if (myFileName.trim() != "") {
//						String type = myFileName.substring(myFileName.indexOf("."), myFileName.length());
//						// 重命名上传后的文件名
//						long timeInMillis = Calendar.getInstance().getTimeInMillis();
//						String fileName = timeInMillis+type;
//						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
//						String newname=myFileName.replace(oldname, timeInMillis+"");
//						try {
//							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
//							if(flag){
//								 logger.info("上传成功");
//							}else{
//								 logger.info("上传失败");
//							}
//						} catch (Exception e) {
//						    e.printStackTrace();
//						}
//
//						System.out.println(myFileName);
//
//						// 定义上传路径
//
//						String path = DOCDIC + "/" + fileName;
//						File localFile = new File(path);
//						if (!localFile.exists()) {
//							localFile.mkdirs();
//						}
//						file.transferTo(localFile);
//
//						map.addAttribute("attname",myFileName);
//						map.addAttribute("attsize", file.getSize());
//						map.addAttribute("attpath", newname);
//						map.addAttribute("fileName", myFileName);
//						map.addAttribute("id", request.getParameter("id"));
//					}
//				}
//				Enumeration<String> enu = request.getParameterNames();
//				while (enu.hasMoreElements()) {
//					String paraName = enu.nextElement();
//					if (!paraName.equals("url")) {
//						map.addAttribute(paraName, request.getParameter(paraName));
//					}
//					// System.out.println(paraName+": "+request.getParameter(paraName));
//				}
//				// 记录上传该文件后的时间
//				int finaltime = (int) System.currentTimeMillis();
//				System.out.println(finaltime - pre);
//				}
//			}
//
//		}
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("redirect:"+filecollbackurl);
//		return mv;
//	}
//
//
//	/**
//	 * 上传附件返回附件信息
//	 * @param request
//	 * @param response
//	 * @param map
//	 * @return
//	 */
//
////	@RequestMapping(value = "/uploadFileAttInfo", produces = "application/json; charset=utf-8")
////	@ResponseBody
////	public String uploadFileAttInfo(HttpServletRequest request, HttpServletResponse response, Model map){
////		// 创建一个通用的多部分解析器
////		TblAttachment att = null;
////		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
////		String myFileName = "";
////		 TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
////		// 判断 request 是否有文件上传,即多部分请求
////		 String text="";
////		try {
////			if (multipartResolver.isMultipart(request)) {
////                // 转换成多部分request
////                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
////                // 取得request中的所有文件名
////                Iterator<String> iter = multiRequest.getFileNames();
////                while (iter.hasNext()) {
////                    // 取得上传文件
////                    MultipartFile file = multiRequest.getFile(iter.next());
////                    if (file != null) {
////                        // 取得当前上传文件的文件名称
////                        myFileName = file.getOriginalFilename();
////                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
////                        if (myFileName.trim() != "") {
////                            System.out.println(myFileName);
////							int lastIndexOf = myFileName.lastIndexOf(".");
////                            String type = myFileName.substring(lastIndexOf);
////                            // 重命名上传后的文件名
////                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
////                            String fileName = timeInMillis + type;
////                            // 定义上传路径
////                            String path = DOCDIC + "/" + fileName;
////                            File localFile = new File(path);
////                            if (!localFile.exists()) {
////                                localFile.mkdirs();
////                            }
////
////    						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
////    						String newname=myFileName.replace(oldname, timeInMillis+"");
////    						try {
////    							boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
////    							if(flag){
////    								 logger.info("上传成功");
////    							}else{
////    								 logger.info("上传失败");
////    							}
////    						} catch (Exception e) {
////    						    e.printStackTrace();
////    						}
////
////                           file.transferTo(localFile);
////                           try {
////							text=hhqDemo(path);
////						} catch (Exception e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
////                           att = new TblAttachment();
////                            att.setAttsize(file.getSize()/1000);
////                            att.setAttpath(newname);
////                            att.setFileName(myFileName);
////    				    	att.setAttname(file.getOriginalFilename());
////    				    	att.setUploader(user.getRealname());
////    				    	att.setUploadtime(new Date());
////    				    	att.setContentText(text);
////    				    	attachmentService.add(att);
////
////                        }
////                    }
////                }
////            }
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////		return JSONObject.toJSONString(att).toString();
////	}
//
//
//	/**
//	  * 删除附件
//	  */
////	@RequestMapping(value = "/deleteFileRelation",produces = "application/json; charset=utf-8")
////	public @ResponseBody String deleteFileRelation(HttpServletRequest request){
////		try {
////			String sn = request.getParameter("sn");
////			String attid = request.getParameter("attid");
////			String cl = request.getParameter("cl");
////			if (StringUtils.isNotBlank(sn) && StringUtils.isNotBlank(cl)){
////				//删除关系
////				attachmentService.deleteRelation(sn,attid,cl);
////			}
////			TblAttachment att = attachmentService.findById(attid);
////			attachmentService.delete(att);
////			return JsonBean.success();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return JsonBean.error("删除失败");
////	}
////
//	/*
//	 * 合合ocr识别
//	 * */
//	@RequestMapping(value = "/hhqDemo",produces = "application/json; charset=utf-8")
//	public String hhqDemo(String path) throws Exception {
//        String url = "https://ocr-api.ccint.com/cci_ai/service/v1.1/text_recognize_3d1";
//        String appKey = "26f1b22a5b5a78bce694cb9303d54a4b"; // your app_key
//        String appSecret = "REDACTED"; // your app_secret
//        BufferedReader in = null;
//        DataOutputStream out = null;
//        String whole_text="";
//        try {
//           byte[] imgData = readfile(path); // image
//          // String imgData = imageToBase64(path);
//            URL realUrl = new URL(url);
//            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Content-Type", "application/octet-stream");
//            conn.setRequestProperty("app-key",appKey);
//            conn.setRequestProperty("app-secret",appSecret);
//            conn.setRequestProperty("lang_opt", "zho-s");
//            conn.setRequestProperty("Charset", "UTF-8");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST"); // 设置请求方式
//            out = new DataOutputStream(conn.getOutputStream());
//            out.write(imgData);
//            out.flush();
//            out.close();
//            in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            String line;
//            String result = "";
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            whole_text= JSONObject.parseObject(JSONObject.parseObject(result).getString("result")).getString("whole_text").replaceAll("\n", "<br/>");
//        System.out.println(whole_text);
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！" + e);
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//       return whole_text;
//    }
//
//    public static byte[] readfile(String path)
//    {
//        String imgFile = path;
//        InputStream in = null;
//        byte[] data = null;
//        try
//        {
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//
//}
