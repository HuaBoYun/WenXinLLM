package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblLegalProceedingsrecord;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.mapper.TblLegalDisputregistrationMapper;
import com.huabo.contract.mapper.TblLegalProceedingsrecordMapper;
import com.huabo.contract.service.TblLegalProceedingsrecordService;

import net.sf.json.JSONObject;

@Service
public class TblLegalProceedingsrecordServiceImpl implements TblLegalProceedingsrecordService {

	@Autowired
	private UserProvider userProvider;
	
    @Resource
    private TblLegalProceedingsrecordMapper tblLegalProceedingsrecordMapper;

    @Resource
    private TblLegalDisputregistrationMapper tblLegalDisputregistrationMapper;
    
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Override
    public void saveProceedingRecord(TblLegalProceedingsrecord proceed,String attids) throws Exception {
    	proceed.setProceedid(RandomUtil.uuBigDecimalId());
		 this.tblLegalProceedingsrecordMapper.saveProceedingRecord(proceed);
		 if(attids != null && !"".equals(attids)) {
		 	String[] attId = attids.split(",");
		 	for (String attid : attId) {
		 		tblLegalProceedingsrecordMapper.inertatt(proceed.getProceedid(), attid);
		 	}
		 }
    }

    @Override
    public TblLegalProceedingsrecord findById(BigDecimal proceedid) throws Exception {
    	return tblLegalProceedingsrecordMapper.findByProceedid(proceedid);
    }

    @Override
    public void modifyNegotiateRecord(TblLegalProceedingsrecord oldproceed,String attids) throws Exception {
		 this.tblLegalProceedingsrecordMapper.updateModifyNegotiateRecord(oldproceed);
		 if(attids != null && !"".equals(attids)) {
		 	String[] attId = attids.split(",");
		 	for (String attid : attId) {
		 		tblLegalProceedingsrecordMapper.inertatt(oldproceed.getProceedid(), attid);
		 	}
		 }
    }

    @Override
    public void removeNegitiateRecord(BigDecimal proceedId) {
        tblLegalProceedingsrecordMapper.deleteatt(proceedId);
        this.tblLegalProceedingsrecordMapper.deleteProceedid(proceedId);
    }

    @Override
    public List<TblLegalProceedingsrecord> findListByLitigationid(BigDecimal litigationid) throws Exception {
    	return this.tblLegalProceedingsrecordMapper.findListByLitigationid(litigationid);
    }


    @Override
    public void findListByPageInfo(PageInfo<TblLegalProceedingsrecord> pageInfo, TblLegalProceedingsrecord record) throws Exception {
    	IPage<TblLegalProceedingsrecord> page = new Page<TblLegalProceedingsrecord>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalProceedingsrecord> pageList = tblLegalProceedingsrecordMapper.findListByPageInfo(page, record);
    	
    	pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
    }

    @Override
    public void findByNegotiaId(PageInfo<TblLegalProceedingsrecord> pageInfo, BigDecimal litigationId) throws Exception {
    	IPage<TblLegalProceedingsrecord> page = new Page<TblLegalProceedingsrecord>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalProceedingsrecord> pageList = tblLegalProceedingsrecordMapper.findByNegotiaId(page, litigationId);
    	
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
    }
    
    @Override
	public JsonBean getAttListBylitigationId(BigDecimal proceedId) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		List<TblAttachment> attList = this.tblAttachmentMapper.findAttachmentproceed(proceedId);
		resultMap.put("attList", attList);
		return ResponseFormat.retParam(1,200,attList);
	}
    
    @Override
	public JsonBean delAttListBylitigationId(BigDecimal attid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(attid!=null) {
			tblLegalProceedingsrecordMapper.deletebyattid(attid);
			this.tblAttachmentMapper.deleteEntity(attid);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(0,10002,null);
	}
	
	@Override
	public Map<String, Object> getproceedTimeAxis(String litigationId) throws Exception {
		List<Map<String, Object>>  list=new ArrayList<Map<String, Object>>();
        //List<JSONObject>  proceList=tblLegalProceedingsrecordMapper.getproceedTimeAxis(litigationId);
		List<JSONObject>  getproceeddis=tblLegalProceedingsrecordMapper.getproceeddis(litigationId);
		Integer nun=0;
		if(getproceeddis!=null && getproceeddis.size()>0) {
			 for(int i=0;i<getproceeddis.size();i++){
				 Map<String, Object> proceList = new HashMap<String, Object>(0);
	        	 JSONObject obj=getproceeddis.get(i);
	        	 if(obj.get("YEAR")==null || obj.get("YEAR").toString().equals("") || obj.get("CONDATION")==null || obj.get("CONDATION").toString().equals("")) {
	        		continue; 
	        	 }
	        	 nun+=1;
	        	 proceList.put("INDEXS", nun);
	        	 proceList.put("YEAR", obj.get("YEAR"));
	        	 proceList.put("CONDATION", obj.get("CONDATION"));
	        	 list.add(proceList);
	         }
		}
        List<JSONObject>  getproceedTim=tblLegalProceedingsrecordMapper.getproceedTim(litigationId);
        if(getproceedTim!=null && getproceedTim.size()>0) {
			 for(int i=0;i<getproceedTim.size();i++){
				 Map<String, Object> proceList = new HashMap<String, Object>(0);
	        	 JSONObject obj=getproceedTim.get(i);
	        	 if(obj.get("YEAR")==null || obj.get("YEAR").toString().equals("") || obj.get("CONDATION")==null || obj.get("CONDATION").toString().equals("")) {
	        		continue; 
	        	 }
	        	 nun+=1; 
	        	 proceList.put("INDEXS", nun);
	        	 proceList.put("YEAR", obj.get("YEAR"));
	        	 proceList.put("CONDATION", obj.get("CONDATION"));
	        	 list.add(proceList);
	         }
		}
        List<JSONObject>  getproceedex=tblLegalProceedingsrecordMapper.getproceedex(litigationId);
        if(getproceedex!=null && getproceedex.size()>0) {
			 for(int i=0;i<getproceedex.size();i++){
				 Map<String, Object> proceList = new HashMap<String, Object>(0);
	        	 JSONObject obj=getproceedex.get(i);
	        	 if(obj.get("YEAR")==null || obj.get("YEAR").toString().equals("") || obj.get("CONDATION")==null || obj.get("CONDATION").toString().equals("")) {
	        		continue; 
	        	 }
	        	 nun+=1;
	        	 proceList.put("INDEXS", nun);
	        	 proceList.put("YEAR", obj.get("YEAR"));
	        	 proceList.put("CONDATION", obj.get("CONDATION"));
	        	 list.add(proceList);
	         }
		}
        List<JSONObject>  getproceedja=tblLegalProceedingsrecordMapper.getproceedja(litigationId);
        if(getproceedja!=null && getproceedja.size()>0) {
			 for(int i=0;i<getproceedja.size();i++){
				 Map<String, Object> proceList = new HashMap<String, Object>(0);
	        	 JSONObject obj=getproceedja.get(i);
	        	 if(obj.get("YEAR")==null || obj.get("YEAR").toString().equals("") || obj.get("CONDATION")==null || obj.get("CONDATION").toString().equals("")) {
	        		continue; 
	        	 }
	        	 nun+=1;
	        	 proceList.put("INDEXS", nun);
	        	 proceList.put("YEAR", obj.get("YEAR"));
	        	 proceList.put("CONDATION", obj.get("CONDATION"));
	        	 list.add(proceList);
	         }
		}
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("proceList", list);
		return resultMap;
	}
}
