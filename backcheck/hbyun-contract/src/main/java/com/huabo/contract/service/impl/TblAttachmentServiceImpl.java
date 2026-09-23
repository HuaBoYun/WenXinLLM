package com.huabo.contract.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.service.TblAttachmentService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Service("tblAttachmentService")
@Slf4j
public class TblAttachmentServiceImpl implements TblAttachmentService {
	
	@Value("${file.path}")
	private String fileUrl;
	
	@Autowired
	private UserProvider userProvider;

	@Resource
	public TblAttachmentMapper tblAttachmentMapper;
	
	@Override
	public Map<String, Object> findAttachmentListByhtId(BigDecimal contractId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByIds(contractId);
            resultMap.put("code", "1");
            resultMap.put("msg", "成功！");
            resultMap.put("data", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}

	@Override
	public TblAttachment get(BigDecimal id) throws Exception {
		return tblAttachmentMapper.getId(id);
	}

	@Override
	public Map<String, Object> uploadAttachment(MultipartFile file)
			throws Exception {
		
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		
		TblAttachment att = new TblAttachment();
		String attPath = "";
			
				try {
					String fileName = new String(file.getOriginalFilename().getBytes()); //重新编码
					String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());

					InputStream inputStream = file.getInputStream();
					long imageName = RandomUtil.uuLongId(); //雪花算法生成图片名称

					attPath = FtpUtil.uploadFilePath(imageName + name + "", inputStream);
					if (StrUtil.isEmpty(attPath)) {
						resultMap.put("code", "0");
						resultMap.put("msg", "文件上传失败！");
						return resultMap;
					}
					String path = fileUrl + fileName;
					   att.setAttsize(file.getSize()/1000);
			           att.setAttpath(imageName + name + "");
				       att.setAttname(fileName);
				       att.setUploader(staff.getRealname());
				       att.setUploadtime(new Date());
				       att.setAttid(RandomUtil.uuBigDecimalId());
				       this.tblAttachmentMapper.insert(att);
				       log.info("附件保存成功，附件Id:"+att.getAttid());
				       resultMap.put("code", "1");
				       resultMap.put("msg", "附件保存成功！");
				       resultMap.put("data", att);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return resultMap;
		
		
		
		/*TblAttachment att = null;
		String text="";
		String myFileName = "";
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		 // 取得request中的所有文件名
        try {
			Iterator<String> iter = multiRequest.getFileNames();
			
			
			
			while (iter.hasNext()) {
			    // 取得上传文件
			     file = multiRequest.getFile(iter.next());
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
			           // String path = FtpUtil.DOCDIC +"/" + fileName;
						String path = fileUrl + fileName;
			            File localFile = new File(path);
			            if (!localFile.exists()) {
			                localFile.mkdirs();
			            }
			            
						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
						String newname = myFileName.replace(oldname, timeInMillis+"");
						String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
						if (StringUtils.isEmpty(attPath)) {
							resultMap.put("code", "0");
							resultMap.put("msg", "文件上传失败！");
							return resultMap;
						}
			           file.transferTo(localFile);
			       
			           //text = FtpUtil.hhqDemo(path);
						
			           att = new TblAttachment();
			           att.setAttsize(file.getSize()/1000);
			           att.setAttpath(newname);
			           att.setFileName(myFileName);
				       att.setAttname(file.getOriginalFilename());
				       att.setUploader(staff.getRealname());
				       att.setUploadtime(new Date());
				       att.setContentText(path);
				       att.setAttid(RandomUtil.uuBigDecimalId());
				       this.tblAttachmentMapper.insert(att);
				       log.info("附件保存成功，附件Id:"+att.getAttid());
				       resultMap.put("code", "1");
				       resultMap.put("msg", "附件保存成功！");
				       resultMap.put("data", att);
			        }
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;*/
	}

	@Override
	public TblAttachment findById(String id) throws Exception {
		return tblAttachmentMapper.findById(id);
	}

	@Override
	public void add(TblAttachment att) throws Exception {
		this.tblAttachmentMapper.insert(att);
	}

	@Override
	public void delete(String attid) throws Exception {
		tblAttachmentMapper.deleteAtt(attid);
	}

	@Override
	public void deleteAttid(TblAttachment att) throws Exception {
		tblAttachmentMapper.deleteAttid(att.getAttid());
	}

	@Override
	public Map<String, Object> removeAttachmentByattId(BigDecimal attid) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			//删除关系
			tblAttachmentMapper.deleteOppsiteFileRelatioin(attid.toString());
			tblAttachmentMapper.removeUnitAttByattid(attid);
			tblAttachmentMapper.deleteContractNode(attid);
			TblAttachment tblAttachmentEntity = tblAttachmentMapper.findById(attid.toString());
	        FtpUtil.removeFile(tblAttachmentEntity.getAttpath());
			tblAttachmentMapper.deleteAttachmentByattId(attid);
			resultMap.put("code", "1");
			resultMap.put("msg", "删除成功！");
			
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> findAttachmentListById(BigDecimal contractId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListById(contractId);
			resultMap.put("code", "1");
			resultMap.put("msg", "成功！");
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token, String staffId,
			MultipartFile file) throws Exception {
		TblAttachment att = null;
		String text="";
		String myFileName = "";
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		 // 取得request中的所有文件名
        try {
			Iterator<String> iter = multiRequest.getFileNames();
			
			TblStaffUtil staff = userProvider.get();
			if(staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			
			while (iter.hasNext()) {
			    // 取得上传文件
			     file = multiRequest.getFile(iter.next());
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
			           // String path = FtpUtil.DOCDIC +"/" + fileName;
						String path = fileUrl + fileName;
			            File localFile = new File(path);
			            if (!localFile.exists()) {
			                localFile.mkdirs();
			            }
			            
						String oldname = myFileName.substring(0,myFileName.lastIndexOf("."));
						String newname = myFileName.replace(oldname, timeInMillis+"");
						String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
						if (StringUtils.isEmpty(attPath)) {
							resultMap.put("code", "0");
							resultMap.put("msg", "文件上传失败！");
							return resultMap;
						}
			           file.transferTo(localFile);
			       
			           //text = FtpUtil.hhqDemo(path);
						
			           att = new TblAttachment();
			           att.setAttsize(file.getSize()/1000);
			           att.setAttpath(newname);
			           att.setFileName(myFileName);
				       att.setAttname(file.getOriginalFilename());
				       att.setUploader(staff.getRealname());
				       att.setUploadtime(new Date());
				       att.setContentText(path);
				       att.setAttid(RandomUtil.uuBigDecimalId());
				       this.tblAttachmentMapper.add(att);
				       log.info("附件保存成功，附件Id:"+att.getAttid());
				       resultMap.put("code", "1");
				       resultMap.put("msg", "附件保存成功！");
				       resultMap.put("data", att);
			        }
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public JsonBean htlxFileList(String token, BigDecimal projectid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByPJId(projectid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public void saveEntity(TblAttachment tblAttachmentEntity) throws Exception {
		this.tblAttachmentMapper.insertEntity(tblAttachmentEntity);
	}
	
	
	

}
