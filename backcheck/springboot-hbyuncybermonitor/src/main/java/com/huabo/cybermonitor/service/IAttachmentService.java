package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface IAttachmentService extends IService<Attachment> {

    List<Attachment> findTblAttachmentByOthartid(String selectedruleid);

}
