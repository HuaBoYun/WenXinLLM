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
import com.huabo.audit.oracle.entity.TblYqnsSjbgSlbg;
import com.huabo.audit.oracle.entity.TblYqnsSjssWdrwSjnr;
import com.huabo.audit.oracle.mapper.TblYqnsSjssWdrwSjnrMapper;
import com.huabo.audit.service.TblYqnsSjssWdrwSjnrService;
import com.huabo.audit.util.PageResult;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJSS_WDRW_SJNR(审减内容表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjssWdrwSjnrServiceImpl extends ServiceImpl<TblYqnsSjssWdrwSjnrMapper, TblYqnsSjssWdrwSjnr>
        implements TblYqnsSjssWdrwSjnrService {
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjssWdrwSjnr vo) throws Exception {
       /* JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        /*PageInfo<TblYqnsSjssWdrwSjnr> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/
        //创建分页对象
        IPage<TblYqnsSjssWdrwSjnr> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsSjssWdrwSjnr> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsSjssWdrwSjnr::getCjr, vo.getCjr());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjssWdrwSjnr::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjssWdrwSjnr::getEndDate, vo.getEndDate());
        }
        if(vo.getTemplateId() != null) {
        	wrapper.lambda().eq(TblYqnsSjssWdrwSjnr::getTemplateId, vo.getTemplateId());
        }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        PageInfo<TblYqnsSjssWdrwSjnr> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjssWdrwSjnr> page = new PageResult<TblYqnsSjssWdrwSjnr>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, page);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjssWdrwSjnr vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getSjnrid()==null) {
            vo.setSjnrid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
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
    public JsonBean detail(String token, TblYqnsSjssWdrwSjnr vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
        TblYqnsSjssWdrwSjnr bean = this.getById(vo.getSjnrid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
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
    public JsonBean delete(String token, TblYqnsSjssWdrwSjnr vo) throws Exception {
        /*JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;*/
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




