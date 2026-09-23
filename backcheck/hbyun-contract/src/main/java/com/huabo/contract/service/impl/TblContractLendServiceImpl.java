package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.hbfk.util.JudgeRoleRight;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractLend;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mapper.TblContractLendMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.service.TblContractLendService;
import com.huabo.contract.service.TblStaffService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblContractLendServiceImpl implements TblContractLendService {

    @Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;

    @Resource
    private TblContractLendMapper tblContractLendMapper;

    @Resource
    private TblStaffService tblStaffService;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public Map<String, Object> saveTblContractLead(String contractId,String lenddate,String returndate, String token, String staffId, TblContractLend lend) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 TblCyhwUnit tbcontract = null;
		 String result = null;
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
		 lend.setLenddate(sdf.parse(lenddate));//借阅日期
		 lend.setReturndate(sdf.parse(returndate));//归还日期
		 try {
		     TblStaffUtil user = userProvider.get();
		     if (user == null) {
		         resultMap.put("code", "0");
		         resultMap.put("msg", "用户已失效！");
		         return resultMap;
		     }
		     //tbcontract=tblCyhwUnitMapper.findContractById(Integer.parseInt(contractId));
		     if (null != lend.getLendid()) {
		     	lend.setContractid(new BigDecimal(contractId));
		         this.tblContractLendMapper.updateTblContractLead(lend);
		     } else {
		         lend.setUserid(user.getStaffid());
		         lend.setLendstatus(0);//1.借阅审批中
		         lend.setContractid(new BigDecimal(contractId));
		         lend.setLendid(RandomUtil.uuBigDecimalId());
		         this.tblContractLendMapper.saveTblContractLead(lend);
		     }
		     //this.ContractLendApproval(user,lend.getLendid());
		     resultMap.put("code", "1");
		     resultMap.put("msg", "成功！");
		     resultMap.put("lendId", lend.getLendid());
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		 return resultMap;
    }


	@Override
    public Map<String, Object> findByContractId(Integer pageNumber, Integer pageSize, String contractId, String token, String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
		    TblStaffUtil user = userProvider.get();
		    if (user == null) {
		        resultMap.put("code", "0");
		        resultMap.put("msg", "用户已失效！");
		        return resultMap;
		    }
		    //TblCyhwUnit tbcontract= tblCyhwUnitMapper.findContractById(Integer.parseInt(contractId));
		    TblContractLend tblContractLead = new TblContractLend();
		    if(contractId != null) {
		    	tblContractLead.setContractid(new BigDecimal(contractId));
		    }
			//如果未传用户id，则查询当前登录人
			if(Objects.isNull(tblContractLead.getUserid())){
				tblContractLead.setUserid(user.getStaffid());
			}
			//如果是合同管理员 则查询全部
			if(JudgeRoleRight.judgeRoleRight("合同管理员",user.getRoleNames())) {
				tblContractLead.setUserid(null);
			}

		    IPage<TblContractLend> page = new Page<TblContractLend>(pageNumber,pageSize);
		    IPage<TblContractLend> pageList = tblContractLendMapper.findByContractId(page,tblContractLead);
		    
		    PageInfo<TblContractLend> pageInfo = new PageInfo<TblContractLend>();
		    tblContractLead.setUserid(user.getStaffid());
		    pageInfo.setCondition(tblContractLead);
		    pageInfo.setCurrentPage(pageNumber);
		    pageInfo.setPageSize(pageSize);
		    pageInfo.setTlist(pageList.getRecords());
		    pageInfo.setTotalRecord((int)pageList.getTotal());
		    dataMap.put("pageInfo", pageInfo);
		    dataMap.put("currentUser", user);
		    resultMap.put("code", "1");
		    resultMap.put("msg", "成功！");
		    resultMap.put("data", dataMap);
		
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return resultMap;
    }

    @Override
    public TblContractLend findById(String lendid) {
    	return tblContractLendMapper.findById(lendid);
    }

	@Override
	public void saveTblContractLeadEntity(TblContractLend lend) {
		 this.tblContractLendMapper.updateTblContractLead(lend);
	}

	@Override
	public String getLendInfo(String token, String lendId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
		TblStaffUtil loginStaff = userProvider.get();
		
		JSONObject jsonObj = null;
		if (loginStaff == null) {
			resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            jsonObj = new JSONObject(resultMap);
    		return jsonObj.toString();
		}
		TblContractLend lend = tblContractLendMapper.findById(lendId);
		dataMap.put("lend", lend);
		resultMap.put("code", "1");
        resultMap.put("msg", "访问成功");
        resultMap.put("data", dataMap);
        jsonObj = new JSONObject(resultMap);
		return jsonObj.toString();
	}

}
