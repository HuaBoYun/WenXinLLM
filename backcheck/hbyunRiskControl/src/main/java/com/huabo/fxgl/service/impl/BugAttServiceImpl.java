package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.BugAtt;
import com.huabo.fxgl.mapper.BugAttMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IBugAttService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.eclipse.jetty.websocket.api.util.QuoteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class BugAttServiceImpl extends ServiceImpl<BugAttMapper, BugAtt> implements IBugAttService {

    @Override
    public void deleteByBudId(String BugId) {
        QueryWrapper<BugAtt> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",BugId);
        this.remove(queryWrapper);
    }

    @Autowired
    private IAttachmentService iAttachmentService;
    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 根据bugId 通过BUGATT中间表查出来该缺陷所有的属性
    * @Date 2022/8/12
    * @param id
    * @return java.util.List<com.huabo.fxgl.entity.Attachment>
    * @url:
    **/
    @Override
    public List<Attachment> getBugAttachmentByBugId(String id) {
       QueryWrapper<BugAtt> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BUGID",id);
        List<BugAtt> bugAtts = list(queryWrapper);
        if (bugAtts!=null&&bugAtts.size()>0) {
            List<BigDecimal> bugAttsIds = bugAtts.stream().map(BugAtt::getAttid).collect(Collectors.toList());
           return iAttachmentService.listByIds(bugAttsIds);
        }
        return null;
    }
}
