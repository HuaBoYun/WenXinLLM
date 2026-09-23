package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IAttachmentService extends IService<Attachment> {
    List<Attachment> findAllByTblWorksheet(String id);
    List<Attachment> getByEventId(String eventid);

    Map<String, Object> uploadAttachment(MultipartHttpServletRequest multiRequest, String staffName, MultipartFile file) throws Exception;


    List<Attachment> findtTblAttachmentByRectsolid(String rectsolid);

    List<Attachment> getByReportId(String id);

    JsonBean deleteByAttId(String attid);

    List<Attachment> findAttachmentByCoping(String copingId);
    
    Attachment getAttByAttid(String attid) throws Exception;
    
    Set<Attachment> getRiskAssplanAttList(String planid);
    
    Set<Attachment> getRiskGroupPlanAttList(String planid);

    void delete(String attid);

    List<Attachment> findAttachmentFiles(String matterfileids);
    //中核密标新增
    //密标新增
    BigDecimal selectSecretLabel(String attachmentLevel) throws Exception;

    Integer getAttachmentList(String formlevel,String attachmentLevelId) throws Exception;
}
