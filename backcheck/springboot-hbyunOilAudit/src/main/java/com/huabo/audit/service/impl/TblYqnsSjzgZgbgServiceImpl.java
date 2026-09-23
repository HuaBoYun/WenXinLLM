package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.oracle.entity.TblYqnsSjzgZgbg;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgZgbgMapper;
import com.huabo.audit.service.TblYqnsSjzgZgbgService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_ZGBGINFO(审计整改报告表)】的数据库操作Service
 */
@Service
public class TblYqnsSjzgZgbgServiceImpl extends ServiceImpl<TblYqnsSjzgZgbgMapper, TblYqnsSjzgZgbg>
        implements TblYqnsSjzgZgbgService {
    TblStaffUtil loginStaff;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgZgbg vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;

        //查询条件
        QueryWrapper<TblYqnsSjzgZgbg> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getTitle())) {
            wrapper.lambda().like(TblYqnsSjzgZgbg::getTitle, vo.getTitle());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjzgZgbg::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjzgZgbg::getEndDate, vo.getEndDate());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsSjzgZgbg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjzgZgbg> page = new PageResult<TblYqnsSjzgZgbg>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjzgZgbg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(vo.getZgbgid() == null) {
			vo.setZgbgid(RandomUtil.uuBigDecimalId());
			vo.setCjr(loginStaff.getRealname());
			vo.setCjrId(loginStaff.getStaffid());
			vo.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
			vo.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
			vo.setCjsj(new Date());
			this.save(vo);
		}else {
			vo.setGxr(loginStaff.getRealname());
			vo.setGxsj(new Date());
			this.updateById(vo);
		}
		
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getZgbgid().toString(), attId);
            }
        }
        return ResponseFormat.retParam(1, 200,vo);
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
    public JsonBean detail(String token, TblYqnsSjzgZgbg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        TblYqnsSjzgZgbg bean = this.getById(vo.getZgbgid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getZgbgid().toString());
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
    public JsonBean delete(String token, TblYqnsSjzgZgbg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        boolean ret = this.removeById(vo.getZgbgid());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getZgbgid().toString());
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 附件删除
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteAttach(String token, String attid, BigDecimal zgbgid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Integer count = this.baseMapper.selectFileRelationCount(attid);
		
		if(zgbgid == null || count.compareTo(1) == 0) {
			baseMapper.deleteAttById(attid);
	        BigDecimal attId = new BigDecimal(attid);
	        tblAttachmentMapper.deleteEntity(attId);
		}else {
			this.baseMapper.deleteFileRelation(attid,zgbgid);
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




