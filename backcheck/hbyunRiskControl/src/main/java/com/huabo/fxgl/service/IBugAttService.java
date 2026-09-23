package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.BugAtt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IBugAttService extends IService<BugAtt> {

    void deleteByBudId(String selectProjectid);

    List<Attachment> getBugAttachmentByBugId(String id);
}
