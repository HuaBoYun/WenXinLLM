package com.huabo.fxgl.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.RepAtt;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.IRepAttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.huabo.fxgl.service.IRiskAttService;
import com.huabo.fxgl.service.IRiskeventAttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Slf4j
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

    @Value("${file.path}")
    private String fileUrl;

    @Autowired
    private IRepAttService iRepAttService;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public List<Attachment> findAllByTblWorksheet(String id) {
        return attachmentMapper.findAllByTblWorksheet(id);
    }

    @Autowired
    private IRiskAttService riskAttService;

    @Autowired
    private IRiskeventAttService iRiskeventAttService;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过风险事件Id查询相关属性
     * @Date 2022/8/10
     * @param eventid
     * @return java.util.List<com.huabo.fxgl.entity.Attachment>
     * @url:
     **/
    @Override
    public List<Attachment> getByEventId(String eventid) {
        List<BigDecimal>  eventAttIds= iRiskeventAttService.getAttIdsByEventId(eventid);
        if (eventAttIds==null || eventAttIds.size()==0)
            return new ArrayList<>();

        QueryWrapper<Attachment> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("ATTID",eventAttIds);
        return this.list(queryWrapper);

    }


    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description
    * @Date 2022/8/18
    * @param id
    * @return java.util.List<com.huabo.fxgl.entity.Attachment>
    * @url:
    **/
    @Override
    public List<Attachment> getByReportId(String id) {
       List<BigDecimal> attrIds= iRepAttService.getAttIdsByReportId(id);
       if (attrIds!=null&&attrIds.size()>0){
           return this.listByIds(attrIds);
       }
        return null;
    }

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 通过Attrid删除附件
    * @Date 2022/8/20
    * @param attid
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Transactional
    @Override
    public JsonBean deleteByAttId(String attid) {
        QueryWrapper<RepAtt> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("ATTID",attid);
        boolean remove = iRepAttService.remove(queryWrapper);//删除中间表
        boolean remove1 = removeById(attid);//删除附件表
        return remove&&remove1? new JsonBean(0,"附件删除成功",null): new JsonBean(0,"附件删除失败",null);
    }

    @Override
    public List<Attachment> findAttachmentByCoping(String copingId) {
        return baseMapper.findAttachmentByCoping(copingId);
    }


    /**
     * 本方法负责保存文件附件及数据库表tbl_attachment的信息存储
     * @param multiRequest
     * @param staffName 上传文件人的姓名
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String staffName, MultipartFile file) throws Exception {
        Attachment att = null;
        String text = "";
        String myFileName = "";
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
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
                        
//                        //？？路径为什么会出双重？
//                        int pfIdx = fileUrl.lastIndexOf(":");
//                        String fileUrl2 = fileUrl.substring(pfIdx-1,fileUrl.length());
//                        System.out.println("fileUrl2========================"+fileUrl2);
                        String path = fileUrl + fileName;
                        File localFile = new File(path);
                        if (!localFile.exists()) {
                            localFile.mkdirs();
                        }

                        String oldname = myFileName.substring(0, myFileName.lastIndexOf("."));
                        String newname = myFileName.replace(oldname, timeInMillis + "");
                        log.info("-----------------------------------newname: " + newname);
                        String attPath = FtpUtil.uploadFilePath(newname, file.getInputStream());
                        log.info("-----------------------------------file.getInputStream(): " + file.getInputStream());
                        log.info("-----------------------------------attPath: " + attPath);
                        if (StringUtils.isEmpty(attPath)) {
                            resultMap.put("code", "0");
                            resultMap.put("msg", "文件上传失败！");
                            return resultMap;
                        }
                        
//                        file.transferTo(localFile);

                        att = new Attachment();
                        att.setAttsize(new BigDecimal(localFile.length()));
                        att.setAttpath(newname);
                        att.setFileName(myFileName);
                        att.setAttname(file.getOriginalFilename());
                        att.setUploader(staffName);
                        att.setUploadtime(LocalDateTime.now());
                        att.setContentText(path);
                        this.save(att);
                        
                        resultMap.put("attInfo", att);
//                        resultMap.put("code", "1");
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
    public List<Attachment> findtTblAttachmentByRectsolid(String rectsolid) {
        return attachmentMapper.findtTblAttachmentByRectsolid(rectsolid);
    }


	@Override
	public Attachment getAttByAttid(String attid) throws Exception {
		// TODO Auto-generated method stub
		return attachmentMapper.selectById(attid);
	}


	@Override
	public Set<Attachment> getRiskAssplanAttList(String planid) {
		// TODO Auto-generated method stub
		return attachmentMapper.getRiskAssplanAttList(planid);
	}

    @Override
    public void delete(String attid) {
         attachmentMapper.deleteAtt(attid);
    }

    @Override
    public List<Attachment> findAttachmentFiles(String matterfileids) {
        List<Attachment> list = new ArrayList<>();
        String[] strings = matterfileids.split(",");
        for (String str:strings) {
            Attachment attachment = attachmentMapper.selectById(str);
            list.add(attachment);
        }
        return list;
    }

    @Override
    public BigDecimal selectSecretLabel(String attachmentLevel) throws Exception {
        return attachmentMapper.selectattachmentLevel(attachmentLevel) ;

    }

    @Override
    public Integer getAttachmentList(String formlevel, String attachmentLevelId) throws Exception {
        return attachmentMapper.selectattachmentList(formlevel,attachmentLevelId) ;

    }


    @Override
	public Set<Attachment> getRiskGroupPlanAttList(String planid) {
		return attachmentMapper.getRiskGroupPlanAttList(planid);
	}

}
