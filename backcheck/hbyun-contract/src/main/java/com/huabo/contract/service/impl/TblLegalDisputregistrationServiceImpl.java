package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblDispuinfo;
import com.huabo.contract.entity.TblLegalDisputregistration;
import com.huabo.contract.mapper.TblLegalDisputregistrationMapper;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.service.TblLegalDisputregistrationService;
import com.huabo.contract.service.TblOrganizaService;

import net.sf.json.JSONObject;

@Service
public class TblLegalDisputregistrationServiceImpl implements TblLegalDisputregistrationService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblLegalDisputregistrationMapper tblLegalDisputregistrationMapper;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

	@Override
	public void saveBidType(Integer type, BigDecimal bid, BigDecimal aid) throws Exception {
		 tblLegalDisputregistrationMapper.saveAttacheMent(type, bid, aid);
	}
    
	@Override
    public Map<String, Object> findAttacheMentByBid(Integer type, BigDecimal bid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        //findAttacheMentByBid分页查询
        List<TblAttachment> list = tblLegalDisputregistrationMapper.findAttacheMentByBid(type, bid);
        resultMap.put("code", "1");
        resultMap.put("msg", "成功");
        resultMap.put("data", list);
        return resultMap;
    }

	@Override
	public Map<String, Object> deleteAttacheMentByBid(Integer type, BigDecimal aid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        this.tblLegalDisputregistrationMapper.deleteAttacheMentByBid(type, aid);
        resultMap.put("code", "1");
        resultMap.put("msg", "成功");
        resultMap.put("data", null);
        return resultMap;
	}

	@Override
	public JsonBean getDisputeNo() throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        String writen = this.tblOrganizationMapper.selectWrittenDeptByOrgId(loginStaff.getLinkOrg().getOrgid());
		String year = DateUtil.parseDate(new Date(), "yyyy");
		String contractno = writen+"-JF-"+year+"-";
		
		String no = this.tblLegalDisputregistrationMapper.selectCountByDisputreNo(contractno);
		if(no != null) {
			Integer nextno = Integer.parseInt(no)+1;
			if(nextno < 10) {
				no = "0"+nextno;
			}else {
				no = nextno.toString();
			}
		}else {
			no = "01";
		}
        contractno += no;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("disputerno", contractno);
        return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public TblLegalDisputregistration findBydisputeId(BigDecimal disputeId) throws Exception {
		return tblLegalDisputregistrationMapper.findBydisputeId(disputeId);
	}

	@Override
	public void findListByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception {
		if(StringUtils.isBlank(companyId)) {
			companyId = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(pid.toString());
		}
		IPage<TblLegalDisputregistration> page = new Page<TblLegalDisputregistration>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalDisputregistration> pageList = tblLegalDisputregistrationMapper.findListByPageInfo(companyId, page, dispute, pid);
		
		//findListByPageInfo方法进行分页查询
        pageInfo.setTlist(pageList.getRecords());
        //findListByPageInfoCount方法查询总记录数
        pageInfo.setTotalRecord((int) pageList.getTotal());
	}

	@Override
	public void findListLSByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception {
		String companyIdStrs = this.tblOrganizaService.selectChidrenIdStrsByFatherOrgId(pid.toString());
		IPage<TblLegalDisputregistration> page = new Page<TblLegalDisputregistration>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalDisputregistration> pageList = tblLegalDisputregistrationMapper.findListLSByPageInfo(companyId,page, dispute, companyIdStrs);
		
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
	}

	@Override
	public void addDiputregistration(TblLegalDisputregistration dispute) throws Exception {
		dispute.setDisputeid(RandomUtil.uuBigDecimalId());
		this.tblLegalDisputregistrationMapper.insert(dispute);
	}

	@Override
	public TblLegalDisputregistration findByDisputeId(BigDecimal disputeId) throws Exception {
		return tblLegalDisputregistrationMapper.findByDisputeId(disputeId);
	}

	@Override
	public void modifyDiputregistration(TblLegalDisputregistration oldDispute) throws Exception {
		tblLegalDisputregistrationMapper.updateById(oldDispute);
	}

	@Override
	public void deleteRelation(String attid) throws Exception {
		tblLegalDisputregistrationMapper.deleteRelation(attid);
	}

	@Override
	public TblLegalDisputregistration findByDidputeid(BigDecimal disputeId) throws Exception {
		return tblLegalDisputregistrationMapper.findDisputeId(disputeId);
	}

	@Override
	public void deleteAttacheMents(Integer type, BigDecimal bid) throws Exception {
		this.tblLegalDisputregistrationMapper.deleteAttacheMents(type, bid);
	}

	@Override
	public void removecaseInformation(BigDecimal disputeId) throws Exception {
		tblLegalDisputregistrationMapper.removecaseInformation(disputeId);
	}

	@Override
	public TblLegalDisputregistration findById(BigDecimal disputeId) throws Exception {
		return tblLegalDisputregistrationMapper.selectDisputeId(disputeId);
	}

	@Override
	public void findListByPageInfoDispute(PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal pid, Integer choiceType, BigDecimal oid) throws Exception {

        //纠纷结案 查询纠纷信息
        String disputeIds = null;
        List<TblDispuinfo> list = null;
        if (choiceType == 1) {
            list = this.tblLegalDisputregistrationMapper.findDisputeIdsForDispute("DISPUTINFO AS DISPUINFO", "TBL_LEGAL_CLOSEINFORMATION", "LINKORG", "", oid);
        } else if (choiceType == 0) {
            //协商过程选择纠纷信息
            list = this.tblLegalDisputregistrationMapper.findDisputeIdsForDispute("DISPUINFO", "TBL_LEGAL_NEGOTIATEDSETTLEMEN", "LINKORG", "", oid);
        } else if (choiceType == 2) {
            //诉讼过程 选择纠纷信息 剔除协商一致 的记录
            list = this.tblLegalDisputregistrationMapper.findDisputeIdsForDispute("DISPUINFO", "TBL_LEGAL_NEGOTIATEDSETTLEMEN", "LINKORG", " AND ISAGGREE = 1", oid);
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                TblDispuinfo tblDispuinfo = list.get(i);
                if (tblDispuinfo != null) {
                    if (i == 0) {
                        disputeIds = tblDispuinfo.getDISPUINFO();
                    } else {
                        disputeIds += ',' + tblDispuinfo.getDISPUINFO();
                    }
                }

            }
        }
        IPage<TblLegalDisputregistration> page = new Page<TblLegalDisputregistration>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        IPage<TblLegalDisputregistration> pageList = tblLegalDisputregistrationMapper.findListByPageInfoDispute(page, dispute, pid, disputeIds);
        		
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public TblLegalDisputregistration findDisputeId(BigDecimal disputeId) throws Exception {
		return tblLegalDisputregistrationMapper.findDisputeId(disputeId);
	}

	@Override
	public TblLegalDisputregistration findByOrgid(BigDecimal disputeId, BigDecimal orgid) throws Exception {
		return tblLegalDisputregistrationMapper.findByOrgid(disputeId, orgid);
	}

	@Override
	public void delClassicCase(BigDecimal disputeId) throws Exception {
		this.tblLegalDisputregistrationMapper.delClassicCase(disputeId);
	}

	@Override
	public void addClassicCase(BigDecimal disputeid) throws Exception {
		this.tblLegalDisputregistrationMapper.addClassicCase(disputeid);
	}

	@Override
	public void findClassicCaseListByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal orgid) throws Exception {
		IPage<TblLegalDisputregistration> page = new Page<TblLegalDisputregistration>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalDisputregistration> pageList = tblLegalDisputregistrationMapper.findClassicCaseListByPageInfo(companyId,page, dispute, orgid);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public Map<String, Object> setDisputeStatistics(Integer year) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		  TblStaffUtil user = userProvider.get();
         if (user == null) {
             resultMap.put("codes", "0");
             resultMap.put("msg", "获取用户失败！");
             return resultMap;
         }
		 Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(year == null) {
            year = calendar.get(Calendar.YEAR);
        }
      //当年新增及存量未结案件
        List<JSONObject> JfslList=tblLegalDisputregistrationMapper.selectJfslList(year,user.getCurrentOrg().getOrgid());
        List<Object> Jfsl=new ArrayList<>();
        Object title=new String[]{"x","当年新增","存量未结"};
         Jfsl.add(title);
        for(int i=0;i<JfslList.size();i++){
       	 JSONObject obj=JfslList.get(i);
       	 Object o=new String[]{obj.get("KEY").toString(),obj.get("XZ").toString(),obj.get("WJ").toString()};
       	 Jfsl.add(o);
        }
        resultMap.put("jfslList", Jfsl);
      //涉诉案件金额统计表
        List<JSONObject> moneyList=tblLegalDisputregistrationMapper.selectMoneyValue(year,user.getCurrentOrg().getOrgid());
        Object[] moneyX = new Object[moneyList.size()] ;
        Object[] moneyY = new Object[moneyList.size()] ;
        for(int i=0;i<moneyList.size();i++){
       	 JSONObject obj=moneyList.get(i);
       	 moneyX[i]=obj.get("KEY");
       	 moneyY[i]=obj.get("MONEY");
        }
        resultMap.put("moneyX", moneyX);
        resultMap.put("moneyY", moneyY);
      //诉讼地位统计表
        List<JSONObject>  SsTypeList=tblLegalDisputregistrationMapper.selectSsTypeList(year,user.getCurrentOrg().getOrgid());
        List<Object> SsType=new ArrayList<>();
          title=new String[]{"x","原告","被告","第三方"};
   	 SsType.add(title);
        for(int i=0;i<SsTypeList.size();i++){
       	 JSONObject obj=SsTypeList.get(i);
       	 Object o=new String[]{obj.get("KEY").toString(),obj.get("YG").toString(),obj.get("BG").toString(),obj.get("DSR").toString()};
       	 SsType.add(o);
        }
        resultMap.put("SsTypeList" , SsType);
      //法律纠纷阶段统计表
        List<JSONObject>  SlztList=tblLegalDisputregistrationMapper.selectSlztList(year,user.getCurrentOrg().getOrgid());
        List<Object> Slzt=new ArrayList<Object>();
          title=new String[]{"x","一审","二审","再审"};
          Slzt.add(title);
        for(int i=0;i<SlztList.size();i++){
       	 JSONObject obj=SlztList.get(i);
       	 Object o=new String[]{obj.get("KEY").toString(),obj.get("YS").toString(),obj.get("ES").toString(),obj.get("ZS").toString()};
       	 Slzt.add(o);
        }
        resultMap.put("SlztList" , Slzt);
		return resultMap;
	}

	@Override
	public Map<String, Object> setDisputeMoneyList(Integer year) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		  TblStaffUtil user = userProvider.get();
       if (user == null) {
           resultMap.put("codes", "0");
           resultMap.put("msg", "获取用户失败！");
           return resultMap;
       }
		 Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      if(year == null) {
          year = calendar.get(Calendar.YEAR);
      }
      List<JSONObject> list=tblLegalDisputregistrationMapper.selectDisputeMoneyList(year,user.getCurrentOrg().getOrgid());
      resultMap.put("jfslList", list);
      return resultMap;
	}

}
