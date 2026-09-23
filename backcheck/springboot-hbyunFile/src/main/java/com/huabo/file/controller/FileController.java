package com.huabo.file.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// import com.eetrust.label.LabelOperator;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.user.UserProvider;
import com.huabo.file.db.service.TblAttachmentService;
import com.huabo.file.util.FileDecryptor;
import com.huabo.file.util.JsonBean;
import com.huabo.file.vo.FileUploadRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/file")
@Tag(name="文件管理接口",description="文件管理接口")
public class FileController {
	
    @Resource
    TblAttachmentService tblAttachmentService;
    
    @Autowired(required = false)
    SecretLabel secretLabel;

    
    // @Resource
    // LabelOperator labelOperator;

    @Resource
    private UserProvider userProvider;
    
 //这些密钥需要与前端的密钥保持一致
    private static final String AES_KEY = "b8e9a1c7d4f265a830e7b1f4d8a9c6e2";
    private static final String AES_IV = "a1b2c3d4e5f6g7h8";
    
    private final FileDecryptor fileDecryptor = new FileDecryptor(AES_KEY, AES_IV);
    
    @PostMapping("/upload")
    @Operation(summary="附件上传接口")
    public JsonBean<List<FileUploadRes>> fileUpload(HttpServletRequest request,
    		@Parameter(name="file",description="附件上传entity",required=true) MultipartFile[]file,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
            @Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return  new JsonBean(500, "用户已失效！", null);
		}
		System.out.println("表单密级信息为："+formlevel);
        //判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
//        if(formlevel!=null && !"".equals(formlevel) && !"undefined".equals(formlevel)){
//            for (MultipartFile multipartFile : file) {
//                BigDecimal attid = null;
//				InputStream inputStream = null;
//                try {
//                	 // 获取加密文件的字节数组
//					String path = "";
//                    System.out.println("获取加密文件字节流！！！！");
//                    InputStream decryptedStream = fileDecryptor.decryptMultipartFile(multipartFile);
//					inputStream = decryptedStream;
//					System.out.println("获取加密文件解密为InputStream进行密标验证！！！！");
//					Integer result = secretLabel.testyz(decryptedStream);
//					System.out.println("是否是密标文件："+result);
//                        if (result == 0){
//                        	 return  new JsonBean(500, "包含非密标文件！", null);
//                        }else {
//                            //判断文件名是否包含密级信息
//                            String str = multipartFile.getOriginalFilename();
//                            String AttachmentLevel="";
//                            if(str.length()>=4){
//                                int index1 = str.indexOf("[");
//                                int index2 = str.indexOf("]");
//                                if(index1 !=-1 && index2 !=-1){
//									//获取密级级别------文件流的方式secrectLabelInfoz
//									AttachmentLevel = secretLabel.secrectLabelInfoz(decryptedStream);
//									if (AttachmentLevel == null || AttachmentLevel.equals("")){
//										AttachmentLevel = str.substring(index1+1, index2);
//									}
//    			                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
//    			                        System.out.println("附件真实的密级级别："+AttachmentLevel);
//    			                        System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
//    			                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
//    			                            log.info("文件上传失败，文件名不包含正确密级信息！");
//    			                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
//    			                        }
//    			                    }
//                                }else {
//									//删除临时文件并返回错误
//									new File(path).delete();
//                                	log.info("文件上传失败，文件名不包含正确密级信息！");
//    			                    return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
//                                }
//                            }else {
//								//删除临时文件并返回错误
//								new File(path).delete();
//                            	log.info("文件上传失败，文件名不包含正确密级信息！");
//    			                return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
//                            }
//                            //根据密级信息查询密级ID
//							System.out.println("附件密级为：" + AttachmentLevel);
//							if(AttachmentLevel.equals("非密")){
//								AttachmentLevel = "公开";
//							}
//							BigDecimal attachmentLevelId = null;
//							Integer count = 0;
//							if (AttachmentLevel != null && !AttachmentLevel.equals("")){
//								attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
//    			            attid = attachmentLevelId;
//    			            System.out.println(attid);
//    			            //根据表单密级信息，查询附件密级是否合理
//    			            count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
//							}
//							//删除临时文件并返回错误
//							new File(path).delete();
//							if (count == 0){
//    			                return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
//    			            }
//                        }
//                        MultipartFile[] filec = {multipartFile};
//    			        return JsonBean.success(tblAttachmentService.fileUpload(filec,false,formlevel,attid,inputStream,token));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return  new JsonBean(500, "上传失败！", null);
//                }
//            }
//        }else {
        	return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,false,formlevel,null));
//        }
//        返回当前添加的文件 前端回显
//        return JsonBean.error(null);
    }




	/*@PostMapping("/upload")
	@Operation(summary="附件上传接口")
	public JsonBean<List<FileUploadRes>> fileUpload(HttpServletRequest request,
													@Parameter(name="file",description="附件上传entity",required=true) MultipartFile[]file,
													@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
													@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return  new JsonBean(500, "用户已失效！", null);
		}
		//判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
		if(formlevel!=null && !"".equals(formlevel)){
			for (MultipartFile multipartFile : file) {

				BigDecimal attid = null;
				try {
					// 获取加密文件的字节数组
					System.out.println("获取加密文件字节流！！！！");
					InputStream decryptedStream = fileDecryptor.decryptMultipartFile(multipartFile);
					System.out.println("获取加密文件解密为InputStream进行密标验证！！！！");
					Integer result = secretLabel.testyz(decryptedStream);
					System.out.println("是否是密标文件："+result);
//					InputStream decryptedStream = multipartFile.getInputStream();
					// 解密为InputStream
					// 解密文件，根据解密之后的字节流生成对应文件，并获取文件地址
					String filePath = saveToTempDirectory2(decryptedStream, multipartFile.getOriginalFilename());
					System.out.println("保存文件到临时目录："+filePath);
					if (result == 0){
						result = secretLabel.test2(filePath);
					}
					if (result == 0){
						boolean deleted = new File(filePath).delete();
						System.out.println("删除临时文件："+deleted);
						return  new JsonBean(500, "包含非密标文件！", null);
					}else {
						//判断文件名是否包含密级信息
						String str = multipartFile.getOriginalFilename();
						String AttachmentLevel="";
						if(str.length()>=4){
							int index1 = str.indexOf("[");
							int index2 = str.indexOf("]");
							if(index1 !=-1 && index2 !=-1){
								//获取密级级别
								System.out.println("开始获取附件密级级别（文件流）↓：");
								AttachmentLevel = secretLabel.secrectLabelInfoz(decryptedStream);
								if (AttachmentLevel == null || AttachmentLevel.equals("")){
									System.out.println("通过地址获取附件密级：");
									AttachmentLevel = secretLabel.secrectLabelInfoc(filePath);
								}
								System.out.println("附件真实的密级级别："+AttachmentLevel);
								boolean deleted = new File(filePath).delete();
								System.out.println("删除临时文件："+deleted);
//    			                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
//    			                    	String string = str.substring(index1+1, index2);
//    			                        System.out.println("附件真实的密级级别："+AttachmentLevel);
//    			                        System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
//    			                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
//    			                            log.info("文件上传失败，文件名不包含正确密级信息！");
//    			                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
//    			                        }
//    			                    }
							}else {
								log.info("文件上传失败，文件名不包含正确密级信息！");
								return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
							}
						}else {
							log.info("文件上传失败，文件名不包含正确密级信息！");
							return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
						}
						//根据密级信息查询密级ID
						System.out.println("附件密级为：" + AttachmentLevel);
						BigDecimal attachmentLevelId = null;
						Integer count = 0;
						if (AttachmentLevel != null && !AttachmentLevel.equals("")){
							attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);

							attid = attachmentLevelId;
							System.out.println(attid);
							//根据表单密级信息，查询附件密级是否合理
							count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
						}
						if (count == 0){
							return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
						}
					}
					MultipartFile[] filec = {multipartFile};
					return JsonBean.success(tblAttachmentService.fileUpload(filec,false,formlevel,attid));
				} catch (Exception e) {
					e.printStackTrace();
					return  new JsonBean(500, "上传失败！", null);
				}
			}
		}else {
			return JsonBean.success(tblAttachmentService.fileUpload(file,false,formlevel,null));
		}
		//返回当前添加的文件 前端回显
		return JsonBean.error(null);
	}
*/




	@PostMapping("/upload2")
	@Operation(summary="附件上传接口2")
	public JsonBean<List<FileUploadRes>> fileUpload2(HttpServletRequest request,
													@Parameter(name="file",description="附件上传entity",required=true) MultipartFile[]file,
													@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
													@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return  new JsonBean(500, "用户已失效！", null);
		}
		//判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
		if(formlevel!=null && !"".equals(formlevel)){
			for (MultipartFile multipartFile : file) {

				BigDecimal attid = null;
				try {
					System.out.println("开始判断是否是密标文件↓：");
					Integer result = secretLabel.test(multipartFile);
					System.out.println("是否是密标文件："+result);
					if (result == 0){
						return  new JsonBean(500, "包含非密标文件！", null);
					}else {
						//判断文件名是否包含密级信息
						String str = multipartFile.getOriginalFilename();
						String AttachmentLevel="";
						if(str.length()>=4){
							int index1 = str.indexOf("[");
							int index2 = str.indexOf("]");
							if(index1 !=-1 && index2 !=-1){
								//获取密级级别
								System.out.println("开始获取附件密级级别（文件流）↓：");
								AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
								System.out.println("附件真实的密级级别："+AttachmentLevel);
//    			                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
//    			                    	String string = str.substring(index1+1, index2);
//    			                        System.out.println("附件真实的密级级别："+AttachmentLevel);
//    			                        System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
//    			                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
//    			                            log.info("文件上传失败，文件名不包含正确密级信息！");
//    			                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
//    			                        }
//    			                    }
							}else {
								log.info("文件上传失败，文件名不包含正确密级信息！");
								return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
							}
						}else {
							log.info("文件上传失败，文件名不包含正确密级信息！");
							return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
						}
						//根据密级信息查询密级ID
						System.out.println("附件密级为：" + AttachmentLevel);
						BigDecimal attachmentLevelId = null;
						Integer count = 0;
						System.out.println("开始判断密级是否符合要求：");
						if (AttachmentLevel != null && !AttachmentLevel.equals("")){
							attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
							attid = attachmentLevelId;
							System.out.println(attid);
							//根据表单密级信息，查询附件密级是否合理
							count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
						}
						if (count == 0){
							return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
						}
					}
					MultipartFile[] filec = {multipartFile};
					return JsonBean.success(tblAttachmentService.fileUploadYuLan(filec,false,formlevel,attid));
				} catch (Exception e) {
					e.printStackTrace();
					return  new JsonBean(500, "上传失败！", null);
				}
			}
		}else {
			return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,false,formlevel,null));
		}
		//返回当前添加的文件 前端回显
		return JsonBean.error(null);
	}
    
    /**
     * 文件上传
     */
    @PostMapping("/uploads")
    @Operation(summary="多文件上传接口（返回多个文件信息）")
    public JsonBean<List<FileUploadRes>> fileUploads(
    		@Parameter(name = "file", description = "文件上传流实体", required = true) MultipartFile[] file
    		,@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) {
    	try {
			//判断是否启用附件密级
			if(formlevel!= null && !formlevel.equals("")) {
				for (MultipartFile multipartFile : file) {
					BigDecimal attid = null;
			        Integer result = secretLabel.test(multipartFile);
			        System.out.println("是否是密标文件："+result);
			        if (result == 0){
			            return  new JsonBean(500, "包含非密标文件！", null);
			        }else {
			            //判断文件名是否包含密级信息
			            String str = multipartFile.getOriginalFilename();
						String AttachmentLevel = "";
			            if(str.length()>=4){
			                int index1 = str.indexOf("[");
			                int index2 = str.indexOf("]");
			                if(index1 !=-1 && index2 !=-1){
			                    //获取密级级别
			                    //真实内容（⬇）
			                    AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
			                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
			                        System.out.println("附件真实的密级级别："+AttachmentLevel);
			                        System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
			                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
			                            log.info("文件上传失败，文件名不包含正确密级信息！");
			                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			                        }
			                    }
			                }else {
			                    log.info("文件上传失败，文件名不包含正确密级信息！");
			                    return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			                }
			            }else {
			                log.info("文件上传失败，文件名不包含正确密级信息！");
			                return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			            }
			            //真实内容（⬇）
			            //获取密级级别
//			            String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
			            //根据密级信息查询密级ID
			            BigDecimal attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
			            attid = attachmentLevelId;
			            System.out.println(attid);
			            //根据表单密级信息，查询附件密级是否合理
			            Integer count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
			            if (count == 0){
			                return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
			            }
//	                    tblAttachmentEntity.setAttachmentlevel(attid);
			        }
			        MultipartFile[] filec = {multipartFile};
			        return JsonBean.success(tblAttachmentService.fileUploadYuLan(filec,false,formlevel,attid));
				}
			}else {
				return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,false,formlevel,null));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return JsonBean.error(null);
    }


	/*
	* 保存为临时文件，以获取文件路径
	* */
	private String saveToTempDirectory(MultipartFile multipartFile) {
		try {
			// 使用固定的临时验证目录
			String tempDir = "/opt/ftp/validation/";
//			String tempDir = "D:\\zzzTest\\";
			File dir = new File(tempDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 保持原始文件名
			String filePath = tempDir + multipartFile.getOriginalFilename();
			File dest = new File(filePath);
			// 保存文件
			multipartFile.transferTo(dest);
			return filePath;
		} catch (IOException e) {
			throw new RuntimeException("保存临时文件失败", e);
		}
	}

	//保存为临时文件并保存文件路径
	private String saveToTempDirectory2(InputStream inputStream,String multiFileName) throws Exception {
		String tempDir = "/opt/ftp/validation/";
//			String tempDir = "D:\\zzzTest\\";
		File targetFile = new File(tempDir,multiFileName);
		// 2. 检查并创建父目录（确保/opt/ftp/validation/存在）
		File parentDir = targetFile.getParentFile();
		if (!parentDir.exists()) {
			// 递归创建所有不存在的父目录
			boolean isDirCreated = parentDir.mkdirs();
			if (!isDirCreated) {
				throw new Exception("文件保存目录创建失败：" + parentDir.getAbsolutePath());
			}
		}
		// 3. 使用try-with-resources自动关闭流，避免资源泄漏
		try (OutputStream outputStream = new FileOutputStream(targetFile)) {
			// 定义字节缓冲区，提高写入效率（避免单字节写入）
			byte[] buffer = new byte[1024 * 8];
			int readLength;
			// 循环读取InputStream中的字节，直到读取完毕（read返回-1）
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			// 强制刷新缓冲区，确保所有字节写入文件
			outputStream.flush();
		}
		return tempDir+multiFileName;
	}

	/**
     * 文件上传（加密）
     */
    @PostMapping("/upload/encrypt")
    @Operation(summary="(加密)多文件上传接口（返回多个文件信息）")
    public JsonBean<List<FileUploadRes>> fileUploadEncrypt(@Parameter(name = "file", description = "文件上传流实体", required = true) MultipartFile[] file
    		,@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) {
    	try {
			//判断是否启用附件密级
			if(formlevel!= null && !formlevel.equals("")) {
				for (MultipartFile multipartFile : file) {
					BigDecimal attid = null;

					String filePath = saveToTempDirectory(multipartFile);
					System.out.println("临时文件路径为："+filePath);
					Integer result = secretLabel.test(multipartFile);
			        System.out.println("是否是密标文件："+result);
			        if (result == 0){
						//删除临时文件并返回错误
						new File(filePath).delete();
			            return  new JsonBean(500, "包含非密标文件！", null);
			        }else {
			            //判断文件名是否包含密级信息
			            String str = multipartFile.getOriginalFilename();
			            if(str.length()>=4){
			                int index1 = str.indexOf("[");
			                int index2 = str.indexOf("]");
			                if(index1 !=-1 && index2 !=-1){
			                    //获取密级级别
			                    //真实内容（⬇）
			                    String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
			                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
			                        System.out.println("附件真实的密级级别："+AttachmentLevel);
			                        System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
			                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
										//删除临时文件并返回错误
										new File(filePath).delete();
			                            log.info("文件上传失败，文件名不包含正确密级信息！");
			                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			                        }
			                    }
			                }else {
								//删除临时文件并返回错误
								new File(filePath).delete();
			                    log.info("文件上传失败，文件名不包含正确密级信息！");
			                    return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			                }
			            }else {
							//删除临时文件并返回错误
							new File(filePath).delete();
			                log.info("文件上传失败，文件名不包含正确密级信息！");
			                return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
			            }
			            //真实内容（⬇）
			            //获取密级级别
			            String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
			            //根据密级信息查询密级ID
			            BigDecimal attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
			            attid = attachmentLevelId;
			            System.out.println(attid);
			            //根据表单密级信息，查询附件密级是否合理
			            Integer count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
			            if (count == 0){
							//删除临时文件并返回错误
							new File(filePath).delete();
			                return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
			            }
//	                    tblAttachmentEntity.setAttachmentlevel(attid);
			        }
			        MultipartFile[] filec = {multipartFile};
			        return JsonBean.success(tblAttachmentService.fileUploadYuLan(filec,true,formlevel,attid));
				}
			}else {
				return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,true,formlevel,null));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return JsonBean.error(null);
    }

    /**
     * 文件下载
     */
    @GetMapping(value = "/download")
    @Operation(summary="文件下载接口")
    public void fileDownLoad(HttpServletResponse response,
                             @Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") String fileId) {
        tblAttachmentService.fileDownLoad(response, fileId,false);
    }

    /**
     * 文件下载（解密）
     */
    @GetMapping(value = "/download/decrypt")
    @Operation(summary="(解密)文件下载接口")
    public void fileDownLoadDecrypt(HttpServletResponse response,
                             @Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") String fileId) {
        tblAttachmentService.fileDownLoad(response, fileId,true);
    }

    /**
     * 文件删除
     */
    @GetMapping(value = "/delete")
    @Operation(summary="文件删除接口")
    public JsonBean<Long> fileRemove(@Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") long fileId) {
        tblAttachmentService.removeFile(fileId);
        return JsonBean.success(fileId);
    }

    /**
     * 根据文件id列表查询文件信息
     */
    @PostMapping("/list")
    @Operation(summary="根据文件id列表查询文件信息")
    public JsonBean<List<FileUploadRes>> listFileUpload(@Parameter(name = "ids", description = "文件id列表", required = true) @RequestBody List<String> ids) {
        return JsonBean.success(tblAttachmentService.listFileUpload(ids));
    }


	//测试所用的
	@PostMapping("/uploadNew")
	@Operation(summary="附件上传接口")
	public JsonBean<List<FileUploadRes>> fileUploadNew(HttpServletRequest request,
													@Parameter(name="file",description="附件上传entity",required=true) MultipartFile[]file,
													@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
													@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return  new JsonBean(500, "用户已失效！", null);
		}
		//判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
		if(formlevel!=null && !"".equals(formlevel)){
			for (MultipartFile multipartFile : file) {
				BigDecimal attid = null;
				try {
					// 获取加密文件的字节数组
					String path = "";
//					System.out.println("获取加密文件字节流！！！！");
//					InputStream decryptedStream = fileDecryptor.decryptMultipartFile(multipartFile);
//					System.out.println("获取加密文件解密为InputStream进行密标验证！！！！");
					Integer result = secretLabel.test(multipartFile);
					System.out.println("是否是密标文件："+result);
					if (result == 0){
						return  new JsonBean(500, "包含非密标文件！", null);
					}else {
						//判断文件名是否包含密级信息
						String str = multipartFile.getOriginalFilename();
						String AttachmentLevel="";
						if(str.length()>=4){
							int index1 = str.indexOf("[");
							int index2 = str.indexOf("]");
							if(index1 !=-1 && index2 !=-1){
								//获取密级级别------文件流的方式secrectLabelInfoz
								AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
								if (AttachmentLevel == null || AttachmentLevel.equals("")){
									//按照临时文件的方式获取文件密级-------secrectLabelInfoc
									path = this.saveToTempDirectory2(multipartFile.getInputStream(), str);
									AttachmentLevel = secretLabel.secrectLabelInfoc(path);
									if (AttachmentLevel == null || AttachmentLevel.equals("")){
										AttachmentLevel = AttachmentLevel.substring(0, AttachmentLevel.length()-1);
									}
								}
								if (AttachmentLevel != null && !AttachmentLevel.equals("")){
									System.out.println("附件真实的密级级别："+AttachmentLevel);
									System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
									if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
										log.info("文件上传失败，文件名不包含正确密级信息！");
										return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
									}
								}
							}else {
								//删除临时文件并返回错误
								new File(path).delete();
								log.info("文件上传失败，文件名不包含正确密级信息！");
								return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
							}
						}else {
							//删除临时文件并返回错误
							new File(path).delete();
							log.info("文件上传失败，文件名不包含正确密级信息！");
							return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
						}
						//根据密级信息查询密级ID
						System.out.println("附件密级为：" + AttachmentLevel);
						if(AttachmentLevel.equals("非密")){
							AttachmentLevel = "公开";
						}
						BigDecimal attachmentLevelId = null;
						Integer count = 0;
						if (AttachmentLevel != null && !AttachmentLevel.equals("")){
							attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
							attid = attachmentLevelId;
							System.out.println(attid);
							//根据表单密级信息，查询附件密级是否合理
							count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
						}
						//删除临时文件并返回错误
						new File(path).delete();
						if (count == 0){
							return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
						}
					}
					MultipartFile[] filec = {multipartFile};
					return JsonBean.success(tblAttachmentService.fileUploadYuLan(filec,false,formlevel,attid));
				} catch (Exception e) {
					e.printStackTrace();
					return  new JsonBean(500, "上传失败！", null);
				}
			}
		}else {
			return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,false,formlevel,null));
		}
//        返回当前添加的文件 前端回显
		return JsonBean.error(null);
	}



	//测试不带密级的文件上传所用的
	@PostMapping("/uploadNewFile")
	@Operation(summary="附件上传接口")
	public JsonBean<List<FileUploadRes>> fileUploadNewFile(HttpServletRequest request,
													   @Parameter(name="file",description="附件上传entity",required=true) MultipartFile[]file,
													   @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
													   @Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return  new JsonBean(500, "用户已失效！", null);
		}
		//判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
		if(formlevel!=null && !"".equals(formlevel)){
			for (MultipartFile multipartFile : file) {
				BigDecimal attid = null;
				try {
					MultipartFile[] filec = {multipartFile};
					return JsonBean.success(tblAttachmentService.fileUploadYuLan(filec,false,formlevel,attid));
				} catch (Exception e) {
					e.printStackTrace();
					return  new JsonBean(500, "上传失败！", null);
				}
			}
		}else {
			return JsonBean.success(tblAttachmentService.fileUploadYuLan(file,false,formlevel,null));
		}
//        返回当前添加的文件 前端回显
		return JsonBean.error(null);
	}




}
