package com.huabo.fxgl.service;

import com.hbfk.entity.TblAttachment;
import com.huabo.fxgl.entity.RiskAtt;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.ws.mime.Attachment;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskAttService extends IService<RiskAtt> {
    public List findBysql(String sql);
    public void add(Attachment a);
    public void modify(TblAttachment a);
    public void delete(String id);
    public void delete(TblAttachment a);
    public TblAttachment findById(String id);
    public List<TblAttachment> findByIds(String attis);
    public List findAll();
    public List<TblAttachment> findtTblAttachmentByRectsolid(String rectsolid);

    public List<TblAttachment> findtTblAttachmentByProjectid(String projectid);

    public List<TblAttachment> findTblAttachmentByOthartid(String othartid);
    public List<TblAttachment> findtTblAttachmentByTask(String taskid);
    public List<TblAttachment> findtTblAttachmentByRisk(String riskid);

}
