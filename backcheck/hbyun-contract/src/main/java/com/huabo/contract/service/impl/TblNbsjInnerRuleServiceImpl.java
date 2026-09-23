package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollectionUtil;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblNbsjInnerrule;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.mapper.TblNbsjInnerruleMapper;
import com.huabo.contract.service.TblNbsjInnerRuleService;
import com.huabo.contract.util.ModuleCodeDef;
import com.huabo.contract.vo.Result;
import com.huabo.contract.vo.TblNbsjInnerRuleVo;

import cn.hutool.core.util.StrUtil;
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjInnerRuleServiceImpl extends ServiceImpl<TblNbsjInnerruleMapper, TblNbsjInnerrule>  implements TblNbsjInnerRuleService {
    @Autowired
    private TblNbsjInnerruleMapper tblNbsjInnerruleMapper;
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
	@Resource
	private TblStaffService tblStaffService;
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private UserProvider userProvider;

	@Override
	public JsonBean selectInnerrulePageInfo(String token, Integer pageNumber, Integer pageSize, TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception {
		//解析了token参数，如果解析成功，得到一个TblStaffUtil类型的对象，表示当前登录用户。
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		//获取登录用户所属的公司id
		tblNbsjInnerRuleVo.setCompanyid(loginStaff.getCurrentOrg().getOrgid().toString());
		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = 15;
		}
		//resultMap存放最终查询结果
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		IPage<TblNbsjInnerrule> page = new Page<TblNbsjInnerrule>(pageNumber,pageSize);
		IPage<TblNbsjInnerrule> pageList = this.tblNbsjInnerruleMapper.selectInnerruleListView(page, tblNbsjInnerRuleVo);
		/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
			pageList.getRecords().forEach(item->{
				if (Objects.nonNull(item.getStaffid1())) {
					item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
				}
				if (Objects.nonNull(item.getStaffid2())) {
					item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
				}
				if (Objects.nonNull(item.getStaffid3())) {
					item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
				}
				if (Objects.nonNull(item.getStaffid4())) {
					item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
				}
				if (Objects.nonNull(item.getStaffid5())) {
					item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
				}
				if (StringUtils.isNotBlank(item.getStaffids1())) {
					item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
				}
				if (StringUtils.isNotBlank(item.getStaffids2())) {
					item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
				}
				if (StringUtils.isNotBlank(item.getStaffids3())) {
					item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
				}
				if (StringUtils.isNotBlank(item.getStaffids4())) {
					item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
				}
				if (StringUtils.isNotBlank(item.getStaffids5())) {
					item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
				}
				if (Objects.nonNull(item.getOrgid1())) {
					item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
				}
				if (Objects.nonNull(item.getOrgid2())) {
					item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
				}
				if (Objects.nonNull(item.getOrgid3())) {
					item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
				}
				if (Objects.nonNull(item.getOrgid4())) {
					item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
				}
				if (Objects.nonNull(item.getOrgid5())) {
					item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
				}
				if (StringUtils.isNotBlank(item.getOrgids1())) {
					item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
				}
				if (StringUtils.isNotBlank(item.getOrgids2())) {
					item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
				}
				if (StringUtils.isNotBlank(item.getOrgids3())) {
					item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
				}
				if (StringUtils.isNotBlank(item.getOrgids4())) {
					item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
				}
				if (StringUtils.isNotBlank(item.getOrgids5())) {
					item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
				}
			});
		}*/
		PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
		//pageInfo存放分页查询的结果
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		//查询的数据存入Tlist
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int) pageList.getTotal());
		pageInfo.getTotalPage();
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean mergeInnerruleInfo(TblNbsjInnerrule tblNbsjInnerrule, String token, String attIds,String pulishDate)  throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Date parse=null;
		try {
			parse = simpleDateFormat.parse(pulishDate);
		} catch (ParseException e) {
			return ResponseFormat.retParam(1,10003,null);
		}
		tblNbsjInnerrule.setCompanyid(loginStaff.getCurrentOrg().getOrgid().toString());
		tblNbsjInnerrule.setPublishdate(parse);
		if(tblNbsjInnerrule.getInnrulid()!= null) {
			//修改；
//			tblNbsjInnerruleMapper.updateEntity(tblNbsjInnerrule);
			tblNbsjInnerruleMapper.updateById(tblNbsjInnerrule);
		}else {
			//新增；
			tblNbsjInnerrule.setInnrulid(RandomUtil.uuBigDecimalId());
//			tblNbsjInnerruleMapper.insertEntity(tblNbsjInnerrule);
			tblNbsjInnerruleMapper.insert(tblNbsjInnerrule);
		}
		if(StrUtil.isNotBlank(attIds)) {
			tblNbsjInnerruleMapper.deleteAttInfo(tblNbsjInnerrule.getInnrulid());
			String[] split = attIds.split(",");
			for (String attid : split) {
				tblNbsjInnerruleMapper.insertAttInfo(tblNbsjInnerrule.getInnrulid(), attid);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblNbsjInnerrule",tblNbsjInnerrule);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean selectInfo(String innerid) throws Exception {
		//查询文件上传的信息
		TblNbsjInnerrule item = tblNbsjInnerruleMapper.findById(innerid);
		if (Objects.nonNull(item)){
			if (Objects.nonNull(item.getStaffid1())) {
				item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
			}
			if (Objects.nonNull(item.getStaffid2())) {
				item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
			}
			if (Objects.nonNull(item.getStaffid3())) {
				item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
			}
			if (Objects.nonNull(item.getStaffid4())) {
				item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
			}
			if (Objects.nonNull(item.getStaffid5())) {
				item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
			}
			if (StringUtils.isNotBlank(item.getStaffids1())) {
				item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
			}
			if (StringUtils.isNotBlank(item.getStaffids2())) {
				item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
			}
			if (StringUtils.isNotBlank(item.getStaffids3())) {
				item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
			}
			if (StringUtils.isNotBlank(item.getStaffids4())) {
				item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
			}
			if (StringUtils.isNotBlank(item.getStaffids5())) {
				item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
			}
			if (Objects.nonNull(item.getOrgid1())) {
				item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
			}
			if (Objects.nonNull(item.getOrgid2())) {
				item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
			}
			if (Objects.nonNull(item.getOrgid3())) {
				item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
			}
			if (Objects.nonNull(item.getOrgid4())) {
				item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
			}
			if (Objects.nonNull(item.getOrgid5())) {
				item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
			}
			if (StringUtils.isNotBlank(item.getOrgids1())) {
				item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
			}
			if (StringUtils.isNotBlank(item.getOrgids2())) {
				item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
			}
			if (StringUtils.isNotBlank(item.getOrgids3())) {
				item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
			}
			if (StringUtils.isNotBlank(item.getOrgids4())) {
				item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
			}
			if (StringUtils.isNotBlank(item.getOrgids5())) {
				item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//查询结果存入resultMap
		resultMap.put("item",item);
		//查询数据信息
		List<TblAttachment> attList = tblAttachmentMapper.findtTblAttachmentByInner_SJ(new BigDecimal(innerid));
		resultMap.put("attList", attList);
		if(item==null) {
			return ResponseFormat.retParam(1,50001,resultMap);
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public TblNbsjInnerrule getInfo(String innerid) throws Exception {
		//查询
		TblNbsjInnerrule tblNbsjInnerrule = tblNbsjInnerruleMapper.findById(innerid);
		return tblNbsjInnerrule;
	}

	@Override
	public JsonBean deleteInfo(String innerid) throws Exception {
		//查询
		TblNbsjInnerrule tblNbsjInnerrule = tblNbsjInnerruleMapper.findById(innerid);
		if(tblNbsjInnerrule==null) {
			return ResponseFormat.retParam(1,50001,null);
		}
		//根据tblNbsjInnerrule对象的innrulid属性删除相关的附件信息。
		tblNbsjInnerruleMapper.deleteAttInfo(tblNbsjInnerrule.getInnrulid());
		//根据innerid删除对应的信息。
		tblNbsjInnerruleMapper.deleteById(innerid);
		return ResponseFormat.retParam(1,70003,null);
	}

	@Override
	public Result removeAttInfoByAttId(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return Result.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}
	private Result deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbsjInnerruleMapper.deleteFileInfoByAttId(att.getAttid().toString());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return Result.success();
	}
	
	
	@Override
	public JsonBean getAutoCodeByHtzd(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//获取当前月
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		String montrStr = "";
		if(month<10) {
			montrStr = "0"+month;
		}else {
			montrStr = month+"";
		}
		
		//组合自动编号:年度-月-模块-自增数
		String rulecode = year+montrStr+"-"+ModuleCodeDef.HTZD+"-";
		
		//自增数查询
		String no = "0";
		try {
			no = this.tblNbsjInnerruleMapper.selectMaxRulecode(rulecode);
		} catch (Exception e) {
			no = "0";
		}
		if(no != null) {
//			 no = no.replace(rulecode, "");
		}else {
			no = "0";
		}
		rulecode += (Integer.parseInt(no)+1);
		
		resultMap.put("autoCode",rulecode);
		return ResponseFormat.retParam(1,200,resultMap);
	}

}
