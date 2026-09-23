package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.huabo.audit.oracle.entity.TblYqnsSjbgJjzrSjjgbg;
import com.huabo.audit.oracle.entity.TblYqnsSjssWdrwSjnr;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgJjzrSjjgbgMapper;
import com.huabo.audit.service.TblYqnsSjbgJjzrSjjgbgService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJBG_JJZRSJJGBG(经济责任审计结果报告定稿表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjbgJjzrSjjgbgServiceImpl extends ServiceImpl<TblYqnsSjbgJjzrSjjgbgMapper, TblYqnsSjbgJjzrSjjgbg>
        implements TblYqnsSjbgJjzrSjjgbgService {
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjbgJjzrSjjgbg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
//        PageInfo<TblYqnsSjbgJjzrSjjgbg> pageInfo = new PageInfo<>();
//        pageInfo.setPageSize(pageSize);
//        pageInfo.setCurrentPage(pageNumber);
//        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
//        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
        //查询条件
        QueryWrapper<TblYqnsSjbgJjzrSjjgbg> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getDocument())) {
            wrapper.lambda().like(TblYqnsSjbgJjzrSjjgbg::getDocument, vo.getDocument());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getTitle())) {
            wrapper.lambda().like(TblYqnsSjbgJjzrSjjgbg::getTitle, vo.getTitle());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjbgJjzrSjjgbg::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjbgJjzrSjjgbg::getEndDate, vo.getEndDate());
        }
        if (StringUtils.isNotBlank(vo.getProjectName())) {
      	   wrapper.and(q -> q.inSql("PROJECT_ID", " select ID from TBL_YQNS_IMPLEMENT_PLAN where PROJECT_NAME like '%"+vo.getProjectName()+"%' "));
         }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        //获得数据
        PageInfo<TblYqnsSjbgJjzrSjjgbg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjbgJjzrSjjgbg> build = new PageResult<TblYqnsSjbgJjzrSjjgbg>().build(pageInfo);
        List<TblYqnsSjbgJjzrSjjgbg> tlist = build.getTlist();
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getJjzrsjjgbgid()==null) {
        	vo.setJjzrsjjgbgid(RandomUtil.uuLongId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getJjzrsjjgbgid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJjzrsjjgbgid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjbgJjzrSjjgbg bean = this.getById(vo.getJjzrsjjgbgid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJjzrsjjgbgid().toString());
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
    public JsonBean delete(String token, TblYqnsSjbgJjzrSjjgbg vo) throws Exception {
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




