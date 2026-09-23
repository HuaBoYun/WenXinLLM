package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.huabo.audit.oracle.dto.TblYqnsGcxmzjJsdwStatisticalToOneDto;
import com.huabo.audit.oracle.dto.TblYqnsWtzgAuditResultsStatisticsDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssuesRecord;
import com.huabo.audit.oracle.entity.TblYqnsProposalbg;
import com.huabo.audit.oracle.entity.TblYqnsProposeAdopt;
import com.huabo.audit.oracle.entity.TblYqnsProposeEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjbgJjzrSjjgbg;
import com.huabo.audit.oracle.entity.TblYqnsSjzgGzzdqksm;
import com.huabo.audit.oracle.entity.TblYqnsSjzgSjzgtj;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.oracle.entity.TblYqnsSjzgYsnr;
import com.huabo.audit.oracle.entity.TblYqnsSjzgZgbg;
import com.huabo.audit.oracle.mapper.AuditIssueListMapper;
import com.huabo.audit.oracle.mapper.AuditProposeMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAdvicenoteMapper;
import com.huabo.audit.oracle.mapper.TblYqnsIssuesRecordMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProposalbgMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProposeAdoptMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgJjzrSjjgbgMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgGzzdqksmMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgSjzgtjMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgWtzgMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgYsnrMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjzgZgbgMapper;
import com.huabo.audit.service.TblYqnsSjzgWtzgService;
import com.huabo.audit.service.TblYqnsSjzgZgbgService;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.result.FlowTaskInfo;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_WTZG(问题整改表)】的数据库操作Service实现
 */
@Service
public class TblYqnsSjzgWtzgServiceImpl extends ServiceImpl<TblYqnsSjzgWtzgMapper, TblYqnsSjzgWtzg>
        implements TblYqnsSjzgWtzgService {
	
    TblStaffUtil loginStaff;
    
    @Resource
    private TblYqnsSjzgWtzgMapper tblYqnsSjzgWtzgMapper;
    
    @Autowired
    private  ImplementPlanMapper implementPlanMapper;

    @Resource
    private TblYqnsSjzgYsnrMapper sjzgYsnrMapper;
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private AuditIssueListMapper auditIssueListMapper;

    @Resource
    private TblYqnsSjzgZgbgMapper tblYqnsSjzgZgbgMapper;
    
    @Resource
    private TblYqnsSjzgZgbgService tblYqnsSjzgZgbgService;
    
    @Resource
    private TblYqnsSjzgSjzgtjMapper tblYqnsSjzgSjzgtjMapper;
    
    @Resource
    private TblYqnsSjzgSjzgtjServiceImpl tblYqnsSjzgSjzgtjServiceImpl;
    
    @Resource
    private TblYqnsSjzgGzzdqksmMapper tblYqnsSjzgGzzdqksmMapper;
    
    @Resource
    private TblYqnsIssuesRecordMapper tblYqnsIssuesRecordMapper;
    
    @Resource
    private AuditProposeMapper auditProposeMapper;
    
    @Resource
    private TblYqnsSjbgJjzrSjjgbgMapper tblYqnsSjbgJjzrSjjgbgMapper;
    
    @Resource
    private TblNbsjAdvicenoteMapper tblNbsjAdvicenoteMapper;
    
    @Resource
	private TblYqnsProposalbgMapper tblYqnsProposalbgMapper;
    
    @Resource
    private TblYqnsProposeAdoptMapper tblYqnsProposeAdoptMapper;

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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        //获得数据
		issues.setRectPerson(loginStaff.getStaffid());
        PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.selectPageInfoList(vo,issues));
        PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, page);
    }


    @Override
	public JsonBean getGzhfList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        //获得数据
		issues.setRectPerson(loginStaff.getStaffid());
        PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.selectGzhfList(vo,issues));
        PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjzgWtzg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if( (vo.getStatus() != null && vo.getStatus() >= 6 && (vo.getIscopy() == null || (vo.getIscopy() != null && vo.getIscopy() == 0 ) ) && (vo.getHxspstatus() == 0 || vo.getHxspstatus() == null) ) || (vo.getHxspstatus() != null && vo.getHxspstatus() == 6)) {
    		//审批已完成 复制一个新的问题整改
    		return this.assignment(token, vo, null, null);
    	}
		
		//保存移送信息
        if(vo.getSfys()==1){//移送
        	TblYqnsSjzgYsnr ysnr = vo.getTblYqnsSjzgYsnr();
        	if(ysnr.getYsnrid() != null) {
        		sjzgYsnrMapper.updateById(ysnr);
        	}else {
        		ysnr.setYsnrid(RandomUtil.uuBigDecimalId());
            	ysnr.setCjsj(new Date());
            	ysnr.setCjr(loginStaff.getRealname());
            	ysnr.setCjrId(loginStaff.getStaffid());
                sjzgYsnrMapper.insert(vo.getTblYqnsSjzgYsnr());
        	}
            vo.setYsnrid(ysnr.getYsnrid());
            if(ysnr.getYsAttIds() != null && ysnr.getYsAttIds().size() > 0) {
            	 for (String attId : ysnr.getYsAttIds()) {
            		 this.sjzgYsnrMapper.saveAttId(ysnr.getYsnrid(),attId);
            	 }
            }
        }
		 
        //保存整改问题信息
         
        if(vo.getWtzgid() != null) {
        	this.baseMapper.updateById(vo);
        }else {
        	vo.setCjr(loginStaff.getRealname());
        	vo.setCjrId(loginStaff.getStaffid());
        	vo.setCjsj(new Date());
        	vo.setStatus(0);
        	vo.setHfspstatus(0);
        	vo.setHxspstatus(0);
        	vo.setIscopy(0);
        	vo.setWtzgid(RandomUtil.uuBigDecimalId());
        	this.baseMapper.insert(vo);
        	TblYqnsIssuesRecord record = this.tblYqnsIssuesRecordMapper.selectMaxVersionByIssues(vo.getIssuesId());
        	record.setWtzgId(vo.getWtzgid());
        	this.tblYqnsIssuesRecordMapper.updateById(record);
        }
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
        	for (String attId : attIds) {
        		this.baseMapper.saveAtt(vo.getWtzgid().toString(), attId);
            }
        }
        
        //维护整改报告列表
        if(vo.getZgbgList() != null && vo.getZgbgList().size() > 0) {
        	List<TblYqnsSjzgZgbg> addBgList = vo.getZgbgList().stream().filter(zgbg -> zgbg.getZgbgid() == null).collect(Collectors.toList());
        	List<TblYqnsSjzgZgbg> updateBgList = vo.getZgbgList().stream().filter(zgbg -> zgbg.getZgbgid() != null).collect(Collectors.toList());
        	for (TblYqnsSjzgZgbg bg : addBgList) {
				bg.setZgbgid(RandomUtil.uuBigDecimalId());
				bg.setWtzgid(vo.getWtzgid());
				bg.setCjrId(loginStaff.getStaffid());
				bg.setCjr(loginStaff.getRealname());
				bg.setCjsj(new Date());
				this.tblYqnsSjzgZgbgMapper.insert(bg);
				if(bg.getAttIds() != null && bg.getAttIds().size() > 0) {
					for (String attId : bg.getAttIds()) {
		                this.tblYqnsSjzgZgbgMapper.saveAtt(bg.getZgbgid().toString(), attId);
		            }
				}
			}
        	for (TblYqnsSjzgZgbg bg : updateBgList) {
				bg.setGxr(loginStaff.getRealname());
				bg.setGxsj(new Date());
				if(bg.getAttIds() != null && bg.getAttIds().size() > 0) {
					for (String attId : bg.getAttIds()) {
		                this.tblYqnsSjzgZgbgMapper.saveAtt(bg.getZgbgid().toString(), attId);
		            }
				}
			}
        	this.tblYqnsSjzgZgbgService.updateBatchById(updateBgList);
        }
        
        //维护整改统计信息
        if(vo.getZgtjList() != null && vo.getZgtjList().size() > 0) {
        	List<TblYqnsSjzgSjzgtj> addTjList = vo.getZgtjList().stream().filter(zgtj -> zgtj.getSjzgtjid() == null).collect(Collectors.toList());
        	List<TblYqnsSjzgSjzgtj> updateTjList = vo.getZgtjList().stream().filter(zgtj -> zgtj.getSjzgtjid() != null).collect(Collectors.toList());
        	for (TblYqnsSjzgSjzgtj tj : addTjList) {
        		tj.setCjr(loginStaff.getRealname());
        		tj.setCjrId(loginStaff.getStaffid());
        		tj.setCjsj(new Date());
        		tj.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
        		tj.setLinkOrgId(loginStaff.getLinkDetp().getOrgid());
        		tj.setSjzgtjid(RandomUtil.uuBigDecimalId());
        		tj.setWtzgid(vo.getWtzgid());
        		this.tblYqnsSjzgSjzgtjMapper.insert(tj);
                if (tj.getAttIds() != null && tj.getAttIds().size() > 0) {
                    for (String attId : tj.getAttIds()) {
                        this.tblYqnsSjzgSjzgtjMapper.saveAtt(tj.getSjzgtjid().toString(), attId);
                    }
                }
			}
        	for (TblYqnsSjzgSjzgtj tj : updateTjList) {
        		this.tblYqnsSjzgSjzgtjMapper.updateById(tj);
        		if (tj.getAttIds() != null && tj.getAttIds().size() > 0) {
                    for (String attId : tj.getAttIds()) {
                        this.tblYqnsSjzgSjzgtjMapper.saveAtt(tj.getSjzgtjid().toString(), attId);
                    }
                }
			}
        }
        //维护制度情况说明
        if(vo.getTblYqnsSjzgGzzdqksm() != null) {
        	TblYqnsSjzgGzzdqksm qksm = vo.getTblYqnsSjzgGzzdqksm();
        	if(qksm.getGzzdqksmid() != null) {
        		this.tblYqnsSjzgGzzdqksmMapper.updateById(qksm);
        	}else {
        		qksm.setGzzdqksmid(RandomUtil.uuBigDecimalId());
        		qksm.setCjr(loginStaff.getRealname());
        		qksm.setCjrId(loginStaff.getStaffid());
        		qksm.setCjsj(new Date());
        		qksm.setWtzgid(vo.getWtzgid());
        		this.tblYqnsSjzgGzzdqksmMapper.insert(qksm);
        	}
            if (qksm.getAttIds() != null && qksm.getAttIds().size() > 0) {
                for (String attId : qksm.getAttIds()) {
                	this.tblYqnsSjzgGzzdqksmMapper.saveAtt(qksm.getGzzdqksmid().toString(), attId);
                }
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
    public JsonBean detail(String token, BigDecimal wtzgid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        TblYqnsSjzgWtzg bean = this.getById(wtzgid);
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        if(bean.getYsnrid()!=null){
            TblYqnsSjzgYsnr tblYqnsSjzgYsnr = sjzgYsnrMapper.selectById(bean.getYsnrid());
            List<TblAttachment> attachments = this.sjzgYsnrMapper.selectAttachmentListByPk(tblYqnsSjzgYsnr.getYsnrid());
            tblYqnsSjzgYsnr.setYsAttList(attachments);
            bean.setTblYqnsSjzgYsnr(tblYqnsSjzgYsnr);
        }
        
        TblYqnsIssueListEntity issues = this.auditIssueListMapper.selectById(bean.getIssuesId());
        bean.setTblIssueEntity(issues);
        
        if(issues.getSjbgdgid() != null){
        	issues.setProList(this.auditProposeMapper.selectProposeAdoptList(issues.getSjbgdgid(),bean.getWtzgid()));
        }
        
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getWtzgid().toString());
        bean.setAttachments(attachments);
        
        List<TblAttachment> sjyqAttist = this.baseMapper.selectAttachmentListYsByPk(bean.getWtzgid().toString());
        bean.setSjqyAttList(sjyqAttist);
        
       /* List<TblAttachment> sjqyList = this.baseMapper.selectSjQyAttachmentListByPk(bean.getWtzgid());
        bean.setSjqyAttList(sjqyList);*/
        
        bean.setZgbgList(this.tblYqnsSjzgZgbgMapper.selectListByZg(bean.getWtzgid()));
        
        bean.setZgtjList(this.tblYqnsSjzgSjzgtjMapper.selectListByZg(bean.getWtzgid()));
        
        TblYqnsSjzgGzzdqksm qksm = this.tblYqnsSjzgGzzdqksmMapper.selectUniqueByZg(bean.getWtzgid());
        if(qksm != null) {
        	qksm.setAttachments(this.tblYqnsSjzgGzzdqksmMapper.selectAttachmentListByPk(qksm.getGzzdqksmid().toString()));
        }
       
        bean.setTblYqnsSjzgGzzdqksm(qksm);

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
    public JsonBean delete(String token, TblYqnsSjzgWtzg vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        this.baseMapper.deleteById(vo.getWtzgid());
        this.tblAttachmentMapper.deleteAttachmentByWtzzgId(vo.getWtzgid());
        this.baseMapper.deleteAttByPk(vo.getWtzgid().toString());
        
        //删除整改报告维护
        this.tblYqnsSjzgZgbgMapper.deleteAttachmentByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgZgbgMapper.deleteAttachmentRelaByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgZgbgMapper.deleteByWtzgid(vo.getWtzgid());
        
        //删除整改统计信息
        this.tblYqnsSjzgSjzgtjMapper.deleteAttachmentByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgSjzgtjMapper.deleteAttachmentRelaByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgSjzgtjMapper.deleteByWtzgid(vo.getWtzgid());
        
        
        //删除维护制度情况说明
        this.tblYqnsSjzgGzzdqksmMapper.deleteAttachmentByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgGzzdqksmMapper.deleteAttachmentRelaByWtzzgId(vo.getWtzgid());
        this.tblYqnsSjzgGzzdqksmMapper.deleteByWtzgid(vo.getWtzgid());
        
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
    public JsonBean deleteAttach(String token, String attid, BigDecimal wtzgid) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Integer count = this.baseMapper.selectFileRelationCount(attid);
		
		if(wtzgid == null || count.compareTo(1) == 0) {
			baseMapper.deleteAttById(attid);
	        BigDecimal attId = new BigDecimal(attid);
	        tblAttachmentMapper.deleteEntity(attId);
		}else {
			baseMapper.deleteFileRelation(attid,wtzgid);
		}
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


	@Override
	public JsonBean deleteYsAttach(String token, String attid, BigDecimal ysnrid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Integer count = this.sjzgYsnrMapper.selectFileRelationCount(attid);
		
		if(ysnrid == null || count.compareTo(1) == 0) {
			sjzgYsnrMapper.deleteAttById(attid);
	        BigDecimal attId = new BigDecimal(attid);
	        tblAttachmentMapper.deleteEntity(attId);
		}else {
			this.sjzgYsnrMapper.deleteFileRelation(attid,ysnrid);
		}
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
	}


	@Override
	public JsonBean complete(String token, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		this.tblYqnsSjzgWtzgMapper.updateStatus(wtzgid,7);
		return ResponseFormat.retParam(1, 200);
	}


	@Override
	public JsonBean assignment(String token, TblYqnsSjzgWtzg vo, BigDecimal rectPerson, String rectPersonName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal oldId = vo.getWtzgid();
		
		TblYqnsSjzgWtzg bean = this.getById(oldId);
		
		TblYqnsIssuesRecord record = null;
		TblYqnsIssueListEntity issues = null;
		issues = this.auditIssueListMapper.selectById(bean.getIssuesId());
		record = tblYqnsIssuesRecordMapper.selectMaxVersionByIssues(issues.getId());
		
		if(rectPerson == null) {
			rectPerson = issues.getRectPerson();
		}
		
		if(StringUtils.isBlank(rectPersonName)) {
			rectPersonName = issues.getRectPerName();
		}
		
		issues.setRectPerson(rectPerson);
		issues.setRectPerName(rectPersonName);
		//校验分派的数据  是新增 还是  修改整改人
		if((bean.getStatus() != null && bean.getStatus() >= 6 && (bean.getIscopy() == 0 || bean.getIscopy() == null ) && (bean.getHxspstatus() == 0 || bean.getHxspstatus() == null) ) || (bean.getHxspstatus() != null && bean.getHxspstatus() == 6)) {
			//新增
			//复制审计移送信息
			BigDecimal oldSjysId = null;
			BigDecimal newysId = null;
			TblYqnsSjzgYsnr sjys = null;
			
			if(bean.getYsnrid()!=null){
				newysId = RandomUtil.uuBigDecimalId();
	            sjys = sjzgYsnrMapper.selectById(bean.getYsnrid());
	            oldSjysId = sjys.getYsnrid();
	            sjys.setYsnrid(newysId);
	            this.sjzgYsnrMapper.insert(sjys);
	            this.sjzgYsnrMapper.copyFileRelation(sjys.getYsnrid(),oldSjysId);
	            newysId = sjys.getYsnrid();
	        }
			
			if(vo.getTblYqnsSjzgYsnr() != null) {
				sjys = vo.getTblYqnsSjzgYsnr();
				if(newysId == null) {
					newysId = RandomUtil.uuBigDecimalId();
					sjys.setYsnrid(newysId);
					this.sjzgYsnrMapper.insert(sjys);
				}else {
					sjys.setYsnrid(newysId);
					this.sjzgYsnrMapper.updateById(sjys);
				}
				if(sjys.getYsAttIds() != null && sjys.getYsAttIds().size() > 0) {
	            	 for (String attId : sjys.getYsAttIds()) {
	            		 this.sjzgYsnrMapper.saveAttId(sjys.getYsnrid(),attId);
	            	 }
	            }
			}
			
			
			bean.setYsnrid(newysId);
			bean.setWtzgid(RandomUtil.uuBigDecimalId());
			bean.setHxspstatus(0);
			bean.setIscopy(1);
			this.tblYqnsSjzgWtzgMapper.insert(bean);
			vo.setWtzgid(bean.getWtzgid());
			vo.setYsnrid(newysId);
			vo.setHxspstatus(0);
			vo.setIscopy(1);
			this.tblYqnsSjzgWtzgMapper.updateById(vo);
			
			TblYqnsIssuesRecord newRecord = new TblYqnsIssuesRecord();
			newRecord.setRecordId(RandomUtil.uuBigDecimalId());
			newRecord.setIssuesId(issues.getId());
			newRecord.setRectPerson(rectPerson);
			newRecord.setRectPersonName(rectPersonName);
			newRecord.setCreateTime(new Date());
			newRecord.setWtzgId(bean.getWtzgid());
			newRecord.setVersion(record.getVersion()+1);
			this.tblYqnsIssuesRecordMapper.insert(newRecord);
			
			//复制采纳的审计建议
			List<TblYqnsProposeAdopt> adoptList = this.tblYqnsProposeAdoptMapper.selectListByCopy(oldId);
			
			for (TblYqnsProposeAdopt adopt : adoptList) {
				adopt.setAdoptId(RandomUtil.uuStringId());
				adopt.setWtzgid(bean.getWtzgid());
				this.tblYqnsProposeAdoptMapper.insert(adopt);
			}
			
			
			//复制整改问题附件
			this.tblYqnsSjzgWtzgMapper.copyFileRelation(bean.getWtzgid(),oldId);
			
			List<String> attIds = vo.getAttIds();
	        if (attIds != null && attIds.size() > 0) {
	        	for (String attId : attIds) {
	        		this.baseMapper.saveAtt(vo.getWtzgid().toString(), attId);
	            }
	        }
			
			
			//复制整改报告
			List<TblYqnsSjzgZgbg> bgList = this.tblYqnsSjzgZgbgMapper.selectListByZg(oldId);
			BigDecimal oldGlId = null;
			for (TblYqnsSjzgZgbg bg : bgList) {
				oldGlId = bg.getZgbgid();
				bg.setZgbgid(RandomUtil.uuBigDecimalId());
				bg.setWtzgid(bean.getWtzgid());
				this.tblYqnsSjzgZgbgMapper.copyFileRealtion(bg.getZgbgid(),oldGlId);
			}
			this.tblYqnsSjzgZgbgService.saveBatch(bgList);
			
			//维护整改报告列表
	        if(vo.getZgbgList() != null && vo.getZgbgList().size() > 0) {
	        	for (TblYqnsSjzgZgbg bg : vo.getZgbgList()) {
					bg.setZgbgid(RandomUtil.uuBigDecimalId());
					bg.setWtzgid(vo.getWtzgid());
					bg.setCjrId(loginStaff.getStaffid());
					bg.setCjr(loginStaff.getRealname());
					bg.setCjsj(new Date());
					this.tblYqnsSjzgZgbgMapper.insert(bg);
					if(bg.getAttIds() != null && bg.getAttIds().size() > 0) {
						for (String attId : bg.getAttIds()) {
			                this.tblYqnsSjzgZgbgMapper.saveAtt(bg.getZgbgid().toString(), attId);
			            }
					}
				}
	        }
			
			//复制整改统计信息
			List<TblYqnsSjzgSjzgtj> sjzg = this.tblYqnsSjzgSjzgtjMapper.selectListByZg(oldId);
			BigDecimal newtjId = null;
			for (TblYqnsSjzgSjzgtj zg : sjzg) {
				oldGlId = zg.getSjzgtjid();
				zg.setSjzgtjid(RandomUtil.uuBigDecimalId());
				zg.setWtzgid(bean.getWtzgid());
				newtjId = zg.getSjzgtjid();
				this.tblYqnsSjzgSjzgtjMapper.copyFileRelation(zg.getSjzgtjid(),oldGlId);
			}
			this.tblYqnsSjzgSjzgtjServiceImpl.saveBatch(sjzg);
			
			//维护整改统计信息
	        if(vo.getZgtjList() != null && vo.getZgtjList().size() > 0) {
	        	for (TblYqnsSjzgSjzgtj tj : vo.getZgtjList()) {
	        		if(newtjId == null) {
	        			newtjId = RandomUtil.uuBigDecimalId();
	        			tj.setSjzgtjid(newtjId);
		        		this.tblYqnsSjzgSjzgtjMapper.insert(tj);
	        		}else {
	        			tj.setSjzgtjid(newtjId);
		        		this.tblYqnsSjzgSjzgtjMapper.updateById(tj);
	        		}
	        		
	        		if (tj.getAttIds() != null && tj.getAttIds().size() > 0) {
	                    for (String attId : tj.getAttIds()) {
	                        this.tblYqnsSjzgSjzgtjMapper.saveAtt(tj.getSjzgtjid().toString(), attId);
	                    }
	                }
				}
	        }
			
			
			
			//复制情况说明主键
			TblYqnsSjzgGzzdqksm qksm = this.tblYqnsSjzgGzzdqksmMapper.selectUniqueByZg(oldId);
			oldGlId = qksm.getGzzdqksmid();
			newtjId = RandomUtil.uuBigDecimalId();
			qksm.setGzzdqksmid(newtjId);
			qksm.setWtzgid(bean.getWtzgid());
			this.tblYqnsSjzgGzzdqksmMapper.copyFileRelation(qksm.getGzzdqksmid(),oldGlId);
			this.tblYqnsSjzgGzzdqksmMapper.insert(qksm);
			
	        //维护制度情况说明
	        if(vo.getTblYqnsSjzgGzzdqksm() != null) {
	        	qksm = vo.getTblYqnsSjzgGzzdqksm();
	        	qksm.setGzzdqksmid(newtjId);
	        	qksm.setWtzgid(bean.getWtzgid());
	        	this.tblYqnsSjzgGzzdqksmMapper.updateById(qksm);
	        	
	            if (qksm.getAttIds() != null && qksm.getAttIds().size() > 0) {
	                for (String attId : qksm.getAttIds()) {
	                	this.tblYqnsSjzgGzzdqksmMapper.saveAtt(qksm.getGzzdqksmid().toString(), attId);
	                }
	            }
	        }
			
			
		}else {
			//修改整改人
			record.setRectPerson(rectPerson);
			record.setRectPersonName(rectPersonName);
			this.tblYqnsIssuesRecordMapper.updateById(record);
		}
		this.auditIssueListMapper.updateById(issues);
		return ResponseFormat.retParam(1, 200, bean);
	}


	@Override
	public JsonBean withdraw(String token, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblYqnsSjzgWtzg bean = this.getById(wtzgid);
		
		TblYqnsIssueListEntity issues = this.auditIssueListMapper.selectById(bean.getIssuesId());
		TblYqnsIssuesRecord record = tblYqnsIssuesRecordMapper.selectByIssuesWtzgId(issues.getId(),wtzgid);
		this.auditIssueListMapper.setPersonIsNull(bean.getIssuesId());
		this.tblYqnsIssuesRecordMapper.setPersonIsNull(record.getRecordId());
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean saveSjyqbcAtt(String token, BigDecimal wtzgid, BigDecimal attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		this.tblYqnsSjzgWtzgMapper.insertSjyqbcRealtion(wtzgid,attId);
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean removeSjyqbcAtt(String token, BigDecimal wtzgid, BigDecimal attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		this.tblYqnsSjzgWtzgMapper.deleteSjyqbcRealtion(wtzgid,attId);
		this.tblAttachmentMapper.deleteEntity(attId);
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean getZgtjhzData(String token, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblYqnsSjzgWtzg bean = this.getById(wtzgid);
		TblYqnsIssueListEntity issues = this.auditIssueListMapper.selectById(bean.getIssuesId());
		
		//提出审计建议数
		QueryWrapper<TblYqnsProposeEntity> wrapper = new QueryWrapper<TblYqnsProposeEntity>();
    	wrapper.eq("SJBGDGID", issues.getSjbgdgid());
		long jynum = this.auditProposeMapper.selectCount(wrapper);
		//审计建议采纳数
		wrapper.eq("ISADOPT", 1);
		long cnnum = this.auditProposeMapper.selectCount(wrapper);
		//出具审计要情（份）
		Integer qynum = this.tblYqnsSjzgWtzgMapper.selectSjyqAttCount(wtzgid);
		
		resultMap.put("jynum", jynum);
		resultMap.put("cnnum", cnnum);
		resultMap.put("qynum", qynum);
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	@Override
	public JsonBean getAuditFlowType(String token, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		String flowType = this.tblYqnsSjzgWtzgMapper.selectFlowType(wtzgid);
		return ResponseFormat.retParam(1, 200, flowType);
	}


	@Override
	public JsonBean saveWtzgSjtzs(String token, BigDecimal wtzgid, BigDecimal formId, Integer optype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		switch (optype) {
			case 1:
				//审计通知书
				this.tblYqnsSjzgWtzgMapper.insertWtzgSjtzs(wtzgid,formId);
				break;
			case 2:
				//审计意见及决定书
				this.tblYqnsSjzgWtzgMapper.insertWtzgSjyjjds(wtzgid,formId);
				break;
			case 3:
				//经济责任审计结果报告
				this.tblYqnsSjzgWtzgMapper.insertWtzgJjzrsjjgbg(wtzgid,formId);
				break;
			default:
				break;
		}
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean removeWtzgSjtzs(String token, BigDecimal wtzgid, BigDecimal formId, Integer optype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		switch (optype) {
		case 1:
			//审计通知书
			this.tblYqnsSjzgWtzgMapper.removeWtzgSjtzs(wtzgid,formId);
			break;
		case 2:
			//审计意见及决定书
			this.tblYqnsSjzgWtzgMapper.removetWtzgSjyjjds(wtzgid,formId);
			break;
		case 3:
			//经济责任审计结果报告
			this.tblYqnsSjzgWtzgMapper.removeWtzgJjzrsjjgbg(wtzgid,formId);
			break;
		default:
			break;
		}
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean getWtzgReportInfoList(String token, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
		
		//经济责任审计结果
		QueryWrapper<TblYqnsSjbgJjzrSjjgbg> sjwrapper = new QueryWrapper<>();
		sjwrapper.inSql("JJZRSJJGBGID", "SELECT SJJGBGID FROM TBL_YQNS_WTZG_JJZRSJJGBG WHERE WTZGID = "+wtzgid);
		List<TblYqnsSjbgJjzrSjjgbg> sjjgList = this.tblYqnsSjbgJjzrSjjgbgMapper.selectList(sjwrapper);
		
		//审计通知书
		List<TblYqnsAdvicenoteEntity> tzsList = this.tblNbsjAdvicenoteMapper.selectListByWtzg(wtzgid);
		
		//审计意见书
		QueryWrapper<TblYqnsProposalbg> yjwrapper = new QueryWrapper<>();
		yjwrapper.inSql("BGID", "SELECT SJYJJDID FROM TBL_YQNS_WTZG_SJYJJDS WHERE WTZGID = "+wtzgid);
		List<TblYqnsProposalbg> sjyjList = this.tblYqnsProposalbgMapper.selectList(yjwrapper);
		
		resultMap.put("sjjgList", sjjgList);
		resultMap.put("tzsList", tzsList);
		resultMap.put("sjyjList", sjyjList);
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	@Override
	public JsonBean hgzgList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获得数据
		issues.setRectPerson(loginStaff.getStaffid());
		PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.selectHxzgList(vo,issues));
		PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
		/*List<String> rectName = null;
		for (TblYqnsSjzgWtzg wtzg : page.getTlist()) {
			rectName = this.auditIssueListMapper.selectAllRectPerNameByProjectId(wtzg.getTblIssueEntity().getProjectId());
			if(rectName != null && rectName.size() > 0){
				wtzg.getTblIssueEntity().setRectPerName(String.join(",", rectName));
			}
		}*/
		/*FlowTaskInfo info = null;
		for (TblYqnsSjzgWtzg wtzg : page.getTlist()) {
			wtzg.setIssueDate(this.tblYqnsProposalbgMapper.selectMaxIssuesDate(wtzg.getWtzgid()));//获取审计意见书下发时间
			//判断是否需要获取审批通过时间
			if(("0".equals(wtzg.getZgzt()) || "3".equals(wtzg.getZgzt())) && wtzg.getStatus() == 6 ) {
				info = this.implementPlanMapper.selectApprovalEndInfo(wtzg.getWtzgid(),TblYqnsSjzgWtzg.SHEETID,"end");
				if(info != null) {
					wtzg.setApprovalDate(info.getCreateTime());
				}
			}
		}*/
		return ResponseFormat.retParam(1, 200, page);
	}


	@Override
	public JsonBean hgzgFillInListList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获得数据
		issues.setRectPerson(loginStaff.getStaffid());
		PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.selectHgzgFillInListList(vo,issues));
		PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
		return ResponseFormat.retParam(1, 200, page);
	}


	@Override
	public JsonBean getTotalMoney(String token, BigDecimal projectId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblYqnsSjzgWtzg money =  tblYqnsSjzgWtzgMapper.selectTotalMoney(projectId);
		if(money.getDqzjjjcg() == null){
			money.setDqzjjjcg(BigDecimal.valueOf(0));
		}
		if(money.getDqqtjjcg() == null){
			money.setDqqtjjcg(BigDecimal.valueOf(0));
		}
		
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("ljzgMoney", money.getDqzgje()==null?BigDecimal.valueOf(0):money.getDqzgje());
		resultMap.put("ljjjcgMoney", money.getDqzjjjcg().add(money.getDqqtjjcg()));
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	@Override
	public JsonBean rectificationLedger(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获得数据
		//issues.setRectPerson(loginStaff.getStaffid());
		PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.selectRectificationLedger(vo,issues));
		PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
		List<String> rectName = null;
		for (TblYqnsSjzgWtzg wtzg : page.getTlist()) {
			rectName = this.auditIssueListMapper.selectAllRectPerNameByProjectId(wtzg.getTblIssueEntity().getProjectId());
			if(rectName != null && rectName.size() > 0){
				wtzg.getTblIssueEntity().setRectPerName(String.join(",", rectName));
			}
		}
		return ResponseFormat.retParam(1, 200, page);
	}


	@Override
	public JsonBean rectificationLedgerFillin(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获得数据
		//issues.setRectPerson(loginStaff.getStaffid());
		PageInfo<TblYqnsSjzgWtzg> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsSjzgWtzgMapper.rectificationLedgerFillin(vo,issues));
		PageResult<TblYqnsSjzgWtzg> page = new PageResult<TblYqnsSjzgWtzg>().build(pageInfo);
		return ResponseFormat.retParam(1, 200, page);
	}

 
	@Override
	public JsonBean getHistoryVersion(String token, BigDecimal issuesId, BigDecimal wtzgid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获得数据
		List<TblYqnsSjzgWtzg> wtzgList = tblYqnsSjzgWtzgMapper.getHistoryVersion(issuesId,wtzgid);
		return ResponseFormat.retParam(1, 200, wtzgList);
	}



	/**
	 * 问题整改-审计成果统计
	 *
	 * @return
	 */
	@Override
	public JsonBean selectWtzgAuditResultsStatistics(String token,  Integer queryYear) throws Exception {
//         验证token
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if(queryYear == null) {
			queryYear = Year.now().getValue();
		}

		HashMap<String, Object> result = new HashMap<>();
		TblYqnsWtzgAuditResultsStatisticsDto tblYqnsWtzgAuditResultsStatisticsDto=new TblYqnsWtzgAuditResultsStatisticsDto();
		tblYqnsWtzgAuditResultsStatisticsDto.setQueryYear(queryYear);
		//获得数据
		List<TblYqnsWtzgAuditResultsStatisticsDto> tblYqnsWtzgAuditResultsStatisticsDtoList = tblYqnsSjzgWtzgMapper.selectWtzgAuditResultsStatistics(tblYqnsWtzgAuditResultsStatisticsDto);
		com.huabo.audit.util.PageInfo<TblYqnsWtzgAuditResultsStatisticsDto> info = new com.huabo.audit.util.PageInfo<>();
		// 构建返回值条件
		info.setCurrentPage(1);
		info.setPageSize(50000);
		info.setTlist(tblYqnsWtzgAuditResultsStatisticsDtoList);
		info.setTotalRecord(tblYqnsWtzgAuditResultsStatisticsDtoList.size());
		result.put("pageInfo", info);
		return ResponseFormat.retParam(1, "查询成功", result);

	}


}
