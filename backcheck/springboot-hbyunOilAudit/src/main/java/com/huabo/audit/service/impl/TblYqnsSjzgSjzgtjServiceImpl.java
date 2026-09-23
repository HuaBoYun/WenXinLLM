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
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgGzzdqksmMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgSjzgtjMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgYsnrMapper;
import com.huabo.audit.service.TblYqnsSjzgSjzgtjService;
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
 * @description 针对表【TBL_YQNS_SJZG_SJZGTJ(审计整改统计表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjzgSjzgtjServiceImpl extends ServiceImpl<TblYqnsSjzgSjzgtjMapper, TblYqnsSjzgSjzgtj>
        implements TblYqnsSjzgSjzgtjService {

    TblStaffUtil loginStaff;

    @Resource
    private TblYqnsSjzgGzzdqksmMapper gzzdqksmMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblYqnsSjzgYsnrMapper tblYqnsSjzgYsnrMapper;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgSjzgtj vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        IPage<TblYqnsSjzgSjzgtj> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsSjzgSjzgtj> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getCjr())) {
            wrapper.lambda().like(TblYqnsSjzgSjzgtj::getCjr, vo.getCjr());
        }

        if(StringUtils.isNotBlank(vo.getIssueNumber())) {
        	wrapper.lambda().like(TblYqnsSjzgSjzgtj::getIssueNumber, vo.getIssueNumber());
        }
        if(StringUtils.isNotBlank(vo.getYsclsx())) {
        	wrapper.lambda().like(TblYqnsSjzgSjzgtj::getYsclsx, vo.getYsclsx());
        }
        if(StringUtils.isNotBlank(vo.getSjsfjgysbg())) {
        	wrapper.lambda().like(TblYqnsSjzgSjzgtj::getSjsfjgysbg, vo.getSjsfjgysbg());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjzgSjzgtj::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjzgSjzgtj::getEndDate, vo.getEndDate());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        PageInfo<TblYqnsSjzgSjzgtj> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsSjzgSjzgtj> page = new PageResult<TblYqnsSjzgSjzgtj>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjzgSjzgtj vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if(vo.getTblYqnsSjzgGzzdqksm()!=null){
        	TblYqnsSjzgGzzdqksm gzzd = vo.getTblYqnsSjzgGzzdqksm();
        	
        	if(gzzd.getGzzdqksmid() != null) {
        		gzzdqksmMapper.updateById(gzzd);
        	}else {
        		gzzd.setGzzdqksmid(RandomUtil.uuBigDecimalId());
        		gzzd.setCjr(loginStaff.getRealname());
        		gzzd.setCjrId(loginStaff.getStaffid());
        		gzzd.setCjsj(new Date());
        		gzzdqksmMapper.insert(gzzd);
        	}
            List<String> attIds = vo.getTblYqnsSjzgGzzdqksm().getAttIds();
            if (attIds != null && attIds.size() > 0) {
                for (String attId : attIds) {
                    gzzdqksmMapper.saveAtt(vo.getTblYqnsSjzgGzzdqksm().getGzzdqksmid().toString(), attId);
                }
            }
            vo.setGzzdqksmid(gzzd.getGzzdqksmid());
        }
        
        if(vo.getSjzgtjid() != null) {
        	this.updateById(vo);
        }else {
        	vo.setCjr(loginStaff.getRealname());
        	vo.setCjrId(loginStaff.getStaffid());
        	vo.setCjsj(new Date());
        	vo.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
        	vo.setLinkOrgId(loginStaff.getLinkDetp().getOrgid());
        	vo.setSjzgtjid(RandomUtil.uuBigDecimalId());
        	this.save(vo);
        }
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getSjzgtjid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsSjzgSjzgtj vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        TblYqnsSjzgSjzgtj bean = this.getById(vo.getSjzgtjid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getSjzgtjid().toString());
        bean.setAttachments(attachments);
        /*//根据规章制度情况说明ID查询规章制度信息
        if(bean.getGzzdqksmid()!=null){
            TblYqnsSjzgGzzdqksm gzzdqksm = gzzdqksmMapper.selectById(bean.getGzzdqksmid());
            //根据情况说明ID查询附件信息
            List<TblAttachment> attachs = gzzdqksmMapper.selectAttachmentListByPk(bean.getGzzdqksmid().toString());
            gzzdqksm.setAttachments(attachs);
            bean.setTblYqnsSjzgGzzdqksm(gzzdqksm);
        }*/
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
    public JsonBean delete(String token, TblYqnsSjzgSjzgtj vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblYqnsSjzgSjzgtj delEntity = this.getById(vo.getSjzgtjid());
		/*TblYqnsSjzgGzzdqksm gzzdqksm = gzzdqksmMapper.selectById(delEntity.getGzzdqksmid());
		if(gzzdqksm != null) {
			this.gzzdqksmMapper.deleteAttByPk(gzzdqksm.getGzzdqksmid());
			this.gzzdqksmMapper.deleteById(gzzdqksm.getGzzdqksmid());
		}*/
		
		this.baseMapper.deleteAttByPk(vo.getSjzgtjid());
		this.baseMapper.deleteById(delEntity.getSjzgtjid());
		
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 整改统计附件删除
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteZgtjAttach(String token, String attid, BigDecimal sjzgtjid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Integer count = this.baseMapper.selectFilRelationCount(attid);
		if(sjzgtjid == null || count.compareTo(1) == 0 ) {
			baseMapper.deleteAttById(attid);
	        BigDecimal attId = new BigDecimal(attid);
	        tblAttachmentMapper.deleteEntity(attId);
		}else {
			this.baseMapper.deleteFileRelation(attid,sjzgtjid);
		}
        
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 情况说明附件删除
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteQksmAttach(String token, String attid, BigDecimal gzzdqksmid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Integer count = this.gzzdqksmMapper.selectFileRelationCount(attid);
		
		if(gzzdqksmid == null || count.compareTo(1) == 0) {
			gzzdqksmMapper.deleteAttById(attid);
	        BigDecimal attId = new BigDecimal(attid);
	        tblAttachmentMapper.deleteEntity(attId);
		}else {
			this.gzzdqksmMapper.deleteFileRelation(attid,gzzdqksmid);
		}
		
        
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


	@Override
	public JsonBean getStatisticsInfo(String token, BigDecimal projectId, String dqzjjjcgtype, String dqqtjjcgtype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		List<TblYqnsSjzgYsnr> ysList = this.tblYqnsSjzgYsnrMapper.selectListByProjectId(projectId);
		TblYqnsSjzgSjzgtj zgtj = new TblYqnsSjzgSjzgtj();
		
		Integer ysclsx = 0;
		Integer sjsfjgysbg = 0;
		BigDecimal ysclje = BigDecimal.valueOf(0);
		Integer ysclr = 0;
		Integer sjsfjgysbgr = 0;
		Integer yscllsqk = 0;
		Integer qzsjdjcf = 0;
		Integer qzsjzwcf = 0;
		Integer qzsjnbjlcf = 0;
		Integer qtjjcf = 0;
		Integer lsysclsx = 0;
		BigDecimal ljzjjjcg = BigDecimal.valueOf(0);
		BigDecimal ljqtjjcg = BigDecimal.valueOf(0);
		for (TblYqnsSjzgYsnr ys : ysList) {
			if(ys.getYsclsx() != null) {
				ysclsx = ysclsx + ys.getYsclsx();
			}
			if(StringUtils.isNotBlank(ys.getSjsfjgyssx())) {
				sjsfjgysbg = sjsfjgysbg + Integer.parseInt(ys.getSjsfjgyssx());
			}
			if(ys.getYsclsjje() != null) {
				ysclje = ysclje.add(ys.getYsclsjje());
			}
			if(ys.getYsclr() != null) {
				ysclr = ysclr + ys.getYsclr();
			}
			if(StringUtils.isNotBlank(ys.getSjsfjgyssxr())) {
				sjsfjgysbgr = sjsfjgysbgr + Integer.parseInt(ys.getSjsfjgyssxr());
			}
			if(ys.getYscllsqk() != null) {
				yscllsqk = yscllsqk + ys.getYscllsqk();
			}
			if(StringUtils.isNotBlank(ys.getSjdjcf())) {
				qzsjdjcf = qzsjdjcf + Integer.parseInt(ys.getSjdjcf());
			}
			if(StringUtils.isNotBlank(ys.getSjzwcf())) {
				qzsjzwcf = qzsjzwcf + Integer.parseInt(ys.getSjzwcf());
			}
			if(StringUtils.isNotBlank(ys.getSjnbjlcf())) {
				qzsjnbjlcf = qzsjnbjlcf + Integer.parseInt(ys.getSjnbjlcf());
			}
			if(StringUtils.isNotBlank(ys.getQtjjcf())) {
				qtjjcf = qtjjcf + Integer.parseInt(qtjjcf.toString());
			}
			if(StringUtils.isNotBlank(dqzjjjcgtype) && dqzjjjcgtype.equals(ys.getDqzjjjcgtype())) {
				ljzjjjcg = ljzjjjcg.add(ys.getDqzjjjcg());
			}
			if(StringUtils.isNotBlank(dqqtjjcgtype) && dqzjjjcgtype.equals(ys.getDqqtjjcgtype())) {
				ljqtjjcg = ljqtjjcg.add(ys.getDqqtjjcg());
			}
			if(ys.getLsysclsx() != null ) {
				lsysclsx = lsysclsx + ys.getLsysclsx();
			}
		}
		
		zgtj.setYsclsx(ysclsx.toString());
		zgtj.setSjsfjgysbg(sjsfjgysbg.toString());
		zgtj.setYsclje(ysclje);
		zgtj.setYsclr(ysclr.toString());
		zgtj.setSjsfjgysbgr(sjsfjgysbgr.toString());
		zgtj.setYscllsqk(yscllsqk.toString());
		zgtj.setQzsjdjcf(qzsjdjcf.toString());
		zgtj.setQzsjzwcf(qzsjzwcf.toString());
		zgtj.setQzsjnbjlcf(qzsjnbjlcf.toString());
		zgtj.setQtjjcf(qtjjcf.toString());
		zgtj.setLjzjjjcg(ljzjjjcg);
		zgtj.setLjqtjjcg(ljqtjjcg);
		zgtj.setLsysclsx(lsysclsx.toString());
		return ResponseFormat.retParam(1, 200, zgtj);
	}
}




