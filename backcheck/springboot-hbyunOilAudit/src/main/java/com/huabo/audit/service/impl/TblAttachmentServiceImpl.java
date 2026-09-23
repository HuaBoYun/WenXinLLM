package com.huabo.audit.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.R;


@Service
public class TblAttachmentServiceImpl implements TblAttachmentService {

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
    private UserProvider userProvider;
	

    public R uploadAttachment(MultipartHttpServletRequest multiRequest, String token, MultipartFile file,String fileUrl) {
    	
        TblAttachment att = null;
        String text = "";
        String myFileName = "";
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        // 取得request中的所有文件名
        try {
            Iterator<String> iter = multiRequest.getFileNames();

            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
            	return R.fail("用户已失效！");
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
                        String path = fileUrl +"/"+ fileName;
                        File localFile = new File(path);
                        if (!localFile.exists()) {
                            localFile.mkdirs();
                        }

                        String oldname = myFileName.substring(0, myFileName.lastIndexOf("."));
                        String newname = myFileName.replace(oldname, timeInMillis + "");
                        String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
                        if (StringUtils.isEmpty(attPath)) {
                        	return R.fail("文件上传失败");
                        }
                        file.transferTo(localFile);

                        //text = FtpUtil.hhqDemo(path);

                        att = new TblAttachment();
                        att.setAttsize(file.getSize() / 1000);
                        att.setAttpath(newname);
                        att.setFilename(myFileName);
                        att.setAttname(file.getOriginalFilename());
                        att.setUploader(staff.getRealname());
                        att.setUploadtime(new Date());
                        att.setContentText(path);
                        this.tblAttachmentMapper.insertEntity(att);
                        resultMap.put("code", "1");
                        resultMap.put("msg", "附件保存成功！");
                        resultMap.put("data", att);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(att);
    
    }
	
	@Override
	public void saveEntity(TblAttachment tblAttachmentEntity) throws Exception {
		
		this.tblAttachmentMapper.insertEntity(tblAttachmentEntity);
	}

	@Override
	public TblAttachment selectEntityById(String attId) throws Exception {
		return this.tblAttachmentMapper.selectEntityById(attId);
	}

	@Override
	public void removeEntityById(BigDecimal attid) throws Exception {
		this.tblAttachmentMapper.deleteEntity(attid);
	}

	
	
	@Override
	public JsonBean inMeetRecordFileList(String token, Integer enterid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByPJEnterid(enterid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean outMeetRecordFileList(String token, Integer leaveid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByPJLeaveid(leaveid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dgFileList(String token, Integer sheetid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListBySheetid(sheetid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean workReportFileList(String token, Integer reportid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByReportid(reportid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dpFileList(String token, Integer dpointid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByDpointid(dpointid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean projectFileList(String token, Integer projectId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
				return ResponseFormat.retParam(0,20006,null);
			}
			
			if(null == projectId) {
				TblNbsjProject tnp = this.tblnbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
				if(tnp == null) {
					return ResponseFormat.retParam(0,30003,resultMap);
				}
				projectId = tnp.getProjectId();
			}
			
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByProjectid(projectId);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dataprojectFileList(String token, Integer dataId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblnbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp!=null) {
    			if(loginStaff.getStaffid().toString().equals(tnp.getPmId().toString())) {
    				List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByDataid(dataId);
        			resultMap.put("data", attList);
    			}else {
    				List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByDataids(dataId,loginStaff.getStaffid()+"");
        			resultMap.put("data", attList);
    			}
    			
    		}else {
    			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByDataid(dataId);
    			resultMap.put("data", attList);
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"查询失败",resultMap);
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean noticeFileList(String token, Integer adviceid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByAdviceid(adviceid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean reportFileList(String token, Integer reportid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByReport(reportid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean auditSuggestFileList(String token, Integer proid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByAuditSuggest(proid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean confirmationFileList(String token, Integer factid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByFactbook(factid);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean outFileList(String token, Integer outerId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByOut(outerId);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean certificateFileList(String token, Integer certificateId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByCertificate(certificateId);
			resultMap.put("data", attList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean xfry(String token, String staffids, String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentListByattids(attids);
			if(attList!=null && attList.size()>0) {
				for (TblAttachment tblAttachment : attList) {
					tblAttachmentMapper.updatexfry(staffids, tblAttachment.getAttid());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"下发失败",null);
		}
		return ResponseFormat.retParam(1,200,null);
	}
	
	
	
//	@Value("${file.path}")
//    private String fileUrl;
//
//    public Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String token,
//                                                String staffId, MultipartFile file)
//            throws Exception {
//        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//            TblAttachment att = null;
//            String text = "";
//            String myFileName = "";
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            // 取得request中的所有文件名
//            try {
//                Iterator<String> iter = multiRequest.getFileNames();
//
//                TblStaffUtil staff = userProvider.get();
//                if (staff == null) {
//                    resultMap.put("code", "0");
//                    resultMap.put("msg", "用户已失效！");
//                    return resultMap;
//                }
//
//                while (iter.hasNext()) {
//                    // 取得上传文件
//                    file = multiRequest.getFile(iter.next());
//                    if (file != null) {
//                        // 取得当前上传文件的文件名称
//                        myFileName = file.getOriginalFilename();
//                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//                        if (myFileName.trim() != "") {
//                            System.out.println(myFileName);
//                            int lastIndexOf = myFileName.lastIndexOf(".");
//                            String type = myFileName.substring(lastIndexOf);
//                            // 重命名上传后的文件名
//                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
//                            String fileName = timeInMillis + type;
//                            // 定义上传路径
//                            // String path = FtpUtil.DOCDIC +"/" + fileName;
//                            String path = fileUrl + fileName;
//                            File localFile = new File(path);
//                            if (!localFile.exists()) {
//                                localFile.mkdirs();
//                            }
//
//                            String oldname = myFileName.substring(0, myFileName.lastIndexOf("."));
//                            String newname = myFileName.replace(oldname, timeInMillis + "");
//                            String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
//                            if (StringUtils.isEmpty(attPath)) {
//                                resultMap.put("code", "0");
//                                resultMap.put("msg", "文件上传失败！");
//                                return resultMap;
//                            }
//                            file.transferTo(localFile);
//
//                            //text = FtpUtil.hhqDemo(path);
//
//                            att = new TblAttachment();
//                            att.setAttsize(new BigDecimal(file.getSize() / 1000));//.setAttsize(file.getSize() / 1000);
//                            att.setAttpath(newname);
//                            att.setAttname(myFileName);
//                            att.setAttname(file.getOriginalFilename());
//                            att.setUploader(staff.getRealname());
//                            att.setUploadtime(new Date());
////                            att.setContentText(path);
//                            this.tblAttachmentMapper.insertEntity(att);
////                            log.info("附件保存成功，附件Id:" + att.getAttid());
//                            resultMap.put("code", "1");
//                            resultMap.put("msg", "附件保存成功！");
//                            resultMap.put("data", att);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultMap;
//        } else {
//            TblAttachmentMySql att = null;
//            String text = "";
//            String myFileName = "";
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            // 取得request中的所有文件名
//            try {
//                Iterator<String> iter = multiRequest.getFileNames();
//
//                TblStaffUtil staff = userProvider.get();
//                if (staff == null) {
//                    resultMap.put("code", "0");
//                    resultMap.put("msg", "用户已失效！");
//                    return resultMap;
//                }
//
//                while (iter.hasNext()) {
//                    // 取得上传文件
//                    file = multiRequest.getFile(iter.next());
//                    if (file != null) {
//                        // 取得当前上传文件的文件名称
//                        myFileName = file.getOriginalFilename();
//                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
//                        if (myFileName.trim() != "") {
//                            System.out.println(myFileName);
//                            int lastIndexOf = myFileName.lastIndexOf(".");
//                            String type = myFileName.substring(lastIndexOf);
//                            // 重命名上传后的文件名
//                            long timeInMillis = Calendar.getInstance().getTimeInMillis();
//                            String fileName = timeInMillis + type;
//                            // 定义上传路径
//                            // String path = FtpUtil.DOCDIC +"/" + fileName;
//                            String path = fileUrl + fileName;
//                            File localFile = new File(path);
//                            if (!localFile.exists()) {
//                                localFile.mkdirs();
//                            }
//
//                            String oldname = myFileName.substring(0, myFileName.lastIndexOf("."));
//                            String newname = myFileName.replace(oldname, timeInMillis + "");
//                            String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
//                            if (StringUtils.isEmpty(attPath)) {
//                                resultMap.put("code", "0");
//                                resultMap.put("msg", "文件上传失败！");
//                                return resultMap;
//                            }
//                            file.transferTo(localFile);
//
//                            //text = FtpUtil.hhqDemo(path);
//
//                            att = new TblAttachmentMySql();
//                            att.setAttsize(new BigDecimal(file.getSize() / 1000));
//                            att.setAttpath(newname);
////                            att.setAttname(attPath)//seta(myFileName);
//                            att.setAttname(file.getOriginalFilename());
//                            att.setUploader(staff.getRealname());
////                            att.setUploadtime(new Date());
////                            att.setContentText(path);
////                            this.tblAttachmentMySqlMapper.add(att);
////                            log.info("附件保存成功，附件Id:" + att.getAttid());
//                            resultMap.put("code", "1");
//                            resultMap.put("msg", "附件保存成功！");
//                            resultMap.put("data", att);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultMap;
//        }
//    }

}




