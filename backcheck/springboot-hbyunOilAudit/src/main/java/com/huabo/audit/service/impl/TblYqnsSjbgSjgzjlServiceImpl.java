package com.huabo.audit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjgzjl;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgSjgzjlMapper;
import com.huabo.audit.service.TblYqnsSjbgSjgzjlService;
import com.huabo.audit.util.PageResult;

/**
 * @description 审计工作记录Service实现
 */
@Service
public class TblYqnsSjbgSjgzjlServiceImpl extends ServiceImpl<TblYqnsSjbgSjgzjlMapper, TblYqnsSjbgSjgzjl>
        implements TblYqnsSjbgSjgzjlService {
    TblStaffUtil loginStaff;
    @Resource
    private ImplementPlanMapper implementPlanMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjbgSjgzjl vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        //查询条件
        QueryWrapper<TblYqnsSjbgSjgzjl> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getTitle())) {
            wrapper.lambda().like(TblYqnsSjbgSjgzjl::getTitle, vo.getTitle());
        }
        
        if (StringUtils.isNotBlank(vo.getSjzz())) {
            wrapper.lambda().like(TblYqnsSjbgSjgzjl::getSjzz, vo.getSjzz());
        }
        if (StringUtils.isNotBlank(vo.getProjectName())) {
     	   wrapper.and(q -> q.inSql("PROJECT_ID", " select ID from TBL_YQNS_IMPLEMENT_PLAN where PROJECT_NAME like '%"+vo.getProjectName()+"%' "));
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //获得数据
        PageInfo<TblYqnsSjbgSjgzjl> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjbgSjgzjl> build = new PageResult<TblYqnsSjbgSjgzjl>().build(pageInfo);
        List<TblYqnsSjbgSjgzjl> tlist = build.getTlist();
        if (null != tlist && !tlist.isEmpty()) {
            tlist.forEach(it -> {
                if (null != it.getProjectId()) {
                    String projectName = implementPlanMapper.selectProjectNameById(it.getProjectId());
                    it.setProjectName(projectName);

                    //构建预留字段返回
                    reservePropertyService.buildReserveProperty(it);
                }
            });
        }
        return ResponseFormat.retParam(1, 200, build);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjbgSjgzjl vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getId()==null) {
         	vo.setId(RandomUtil.uuLongId());
         }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getId().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getId().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsSjbgSjgzjl vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjbgSjgzjl bean = this.getById(vo.getId());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getId().toString());
        bean.setAttachments(attachments);
        if (null != bean.getProjectId()) {
            String projectName = implementPlanMapper.selectProjectNameById(bean.getProjectId());
            bean.setProjectName(projectName);
        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
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
    public JsonBean delete(String token, TblYqnsSjbgSjgzjl vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeById(vo.getId());
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




