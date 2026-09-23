package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.TblYqnsPlanfilingMapper;
import com.huabo.audit.service.TblYqnsPlanfilingService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


@Service
public class TblYqnsPlanfilingServiceImpl extends ServiceImpl<TblYqnsPlanfilingMapper, TblYqnsPlanfiling>
        implements TblYqnsPlanfilingService {


    TblStaffUtil loginStaff;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

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

    /**
     * 计划备案列表查询
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean getPlanFilingList(String token, Integer pageNumber, Integer pageSize, TblYqnsPlanfiling vo) throws Exception {

        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        //查询条件
        QueryWrapper<TblYqnsPlanfiling> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 计划项目名称
        if (StringUtils.isNotBlank(vo.getPlanprojectname())) {
            wrapper.lambda().like(TblYqnsPlanfiling::getPlanprojectname, vo.getPlanprojectname());
        }
        
        if (StringUtils.isNotBlank(vo.getNo())) {
            wrapper.lambda().like(TblYqnsPlanfiling::getNo, vo.getNo());
        }
        
        if (StringUtils.isNotBlank(vo.getPlanimplementationtype())) {
            wrapper.lambda().like(TblYqnsPlanfiling::getPlanimplementationtype, vo.getPlanimplementationtype());
        }
        
        if (StringUtils.isNotBlank(vo.getPlanimplementationunitname())) {
            wrapper.lambda().like(TblYqnsPlanfiling::getPlanimplementationunitname, vo.getPlanimplementationunitname());
        }
        
        if (StringUtils.isNotBlank(vo.getPlanauditunitname())) {
            wrapper.lambda().like(TblYqnsPlanfiling::getPlanauditunitname, vo.getPlanauditunitname());
        }

        //倒序
        wrapper.orderByDesc(true, "PLANCREATIONTIME");

        //获得数据
        PageInfo<TblYqnsPlanfiling> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsPlanfiling> build = new PageResult<TblYqnsPlanfiling>().build(pageInfo);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(build.getTlist());
        return ResponseFormat.retParam(1, 200, build);
    }

    /**
     * 增加或修改
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean saveOrUpdate(String token, TblYqnsPlanfiling vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if (vo.getPlanfilingid() == null) {
            vo.setPlanpreparedbyid(loginStaff.getStaffid().toString()); //编制人
            vo.setPlanpreparedbyname(loginStaff.getRealname()); //编制人名称
            vo.setPlancreationtime(new Date()); //创建时间
        }
        if(vo.getPlanfilingid()==null) {
        	vo.setPlanfilingid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttById(vo.getPlanfilingid());
        List<String> attIds = vo.getAttIds();
        if (attIds != null) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getPlanfilingid().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 根据ID删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean delete(String token, BigDecimal id) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsPlanfiling> query = new LambdaQueryWrapper<TblYqnsPlanfiling>()
                .eq(TblYqnsPlanfiling::getPlanfilingid, id);
        int ret = this.baseMapper.delete(query);
        this.baseMapper.deleteAttById(id);
        if (ret < 1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 查询详情
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsPlanfiling vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsPlanfiling bean = this.getById(vo.getPlanfilingid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getPlanfilingid());
        bean.setAttachments(attachments);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }
}
