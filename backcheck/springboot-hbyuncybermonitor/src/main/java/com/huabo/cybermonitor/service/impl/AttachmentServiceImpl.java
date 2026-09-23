package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.mapper.AttachmentMapper;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {
    @Autowired
    AttachmentMapper attachmentMapper;

    @Override
    public List<Attachment> findTblAttachmentByOthartid(String selectedruleid) {
        List<Attachment> tblAttachmentByOthartid = attachmentMapper.findTblAttachmentByOthartid(selectedruleid);
        return tblAttachmentByOthartid;
    }


}
