package com.huabo.fxgl.service.impl;

import com.hbfk.entity.TblAttachment;
import com.huabo.fxgl.entity.RiskAtt;
import com.huabo.fxgl.mapper.RiskAttMapper;
import com.huabo.fxgl.service.IRiskAttService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.ws.mime.Attachment;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskAttServiceImpl extends ServiceImpl<RiskAttMapper, RiskAtt> implements IRiskAttService {

    @Override
    public List findBysql(String sql) {
        return null;
    }

    @Override
    public void add(Attachment a) {

    }

    @Override
    public void modify(TblAttachment a) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void delete(TblAttachment a) {

    }

    @Override
    public TblAttachment findById(String id) {
        return null;
    }

    @Override
    public List<TblAttachment> findByIds(String attis) {
        return null;
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public List<TblAttachment> findtTblAttachmentByRectsolid(String rectsolid) {
        return null;
    }

    @Override
    public List<TblAttachment> findtTblAttachmentByProjectid(String projectid) {
        return null;
    }

    @Override
    public List<TblAttachment> findTblAttachmentByOthartid(String othartid) {
        return null;
    }

    @Override
    public List<TblAttachment> findtTblAttachmentByTask(String taskid) {
        return null;
    }

    @Override
    public List<TblAttachment> findtTblAttachmentByRisk(String riskid) {
        return null;
    }
}
