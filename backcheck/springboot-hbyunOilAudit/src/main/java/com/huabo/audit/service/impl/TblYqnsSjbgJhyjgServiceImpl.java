package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJhyjg;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgJhyjgMapper;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.service.TblYqnsSjbgJhyjgService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TTBL_YQNS_SJBG_JHYJG(交换意见稿表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjbgJhyjgServiceImpl extends ServiceImpl<TblYqnsSjbgJhyjgMapper, TblYqnsSjbgJhyjg>
        implements TblYqnsSjbgJhyjgService {
    TblStaffUtil loginStaff;
    @Resource
    private ImplementPlanMapper implementPlanMapper;
    
    
    @Resource
    private ImplementPlanService implementPlanService;

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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjbgJhyjg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        /*PageInfo<TblYqnsSjbgJhyjg> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsSjbgJhyjg> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getTitle())) {
            wrapper.lambda().like(TblYqnsSjbgJhyjg::getTitle, vo.getTitle());
        }
        if(!loginStaff.getRoleNames().contains("审理") && (vo.getCjr()==null || vo.getCjr()=="") ) {
        	vo.setCjr(loginStaff.getRealname());
        }
        
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsSjbgJhyjg::getCjr, vo.getCjr());
        }
        
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjbgJhyjg::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjbgJhyjg::getEndDate, vo.getEndDate());
        }
        
       
//        if(vo.getProjectId()==null){
//        	 ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//        	 vo.setProjectId(tnp.getId());
//        } 
        if(vo.getProjectId() != null) {
        	wrapper.lambda().eq(TblYqnsSjbgJhyjg::getProjectId, vo.getProjectId());
        }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsSjbgJhyjg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjbgJhyjg> page = new PageResult<TblYqnsSjbgJhyjg>().build(pageInfo);
        List<TblYqnsSjbgJhyjg> tlist = page.getTlist();
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjbgJhyjg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getJhyjgid()==null) {
        	vo.setJhyjgid(RandomUtil.uuLongId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE); 
        }
       
        this.baseMapper.deleteAttByPk(vo.getJhyjgid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJhyjgid().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200, vo);
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
    public JsonBean detail(String token, TblYqnsSjbgJhyjg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjbgJhyjg bean = this.getById(vo.getJhyjgid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJhyjgid().toString());
        bean.setAttachments(attachments);
        if (null != bean.getProjectId()){
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
    public JsonBean delete(String token, TblYqnsSjbgJhyjg vo) throws Exception {
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




