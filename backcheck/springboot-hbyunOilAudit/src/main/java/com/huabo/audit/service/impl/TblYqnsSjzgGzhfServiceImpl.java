package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgGzhf;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgGzhfMapper;
import com.huabo.audit.service.TblYqnsSjzgGzhfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_GZHF(跟踪回访表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjzgGzhfServiceImpl extends ServiceImpl<TblYqnsSjzgGzhfMapper, TblYqnsSjzgGzhf>
        implements TblYqnsSjzgGzhfService {

    TblStaffUtil loginStaff;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgGzhf vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        PageInfo<TblYqnsSjzgGzhf> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
        return ResponseFormat.retParam(1, 200, pageInfo);
    }


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsSjzgGzhf vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getGzhfid()==null) {
            vo.setGzhfid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }

        this.baseMapper.deleteAttByPk(vo.getGzhfid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getGzhfid().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsSjzgGzhf vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjzgGzhf bean = this.getById(vo.getGzhfid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getGzhfid().toString());
        bean.setAttachments(attachments);
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsSjzgGzhf vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
     *
     * @param token
     * @return null or not null
     * @throws Exception
     */
    private JsonBean validToken(String token) throws Exception {
        loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return null;
    }
}




