package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mapper.TblContractPlannodeMapper;
import com.huabo.contract.mapper.TblCyhwProjectbudgetMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.service.TblContractPlannodeService;
import com.huabo.contract.util.MailInfo;
import com.huabo.contract.util.MessageSender;

import lombok.extern.slf4j.Slf4j;


@Service("TblContractPlannodeService")
@Slf4j
public class TblContractPlannodeServiceImpl implements TblContractPlannodeService {
	
	 @Resource
	 private TblContractPlannodeMapper tblContractPlannodeMapper;

	 @Resource
	 private TblCyhwUnitMapper tblCyhwUnitMapper;
	 
	 @Resource
	 private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
	 
	 @Resource
	 private UserProvider userProvider;
	
	
	@Override
	public void findPlanNodeListForCollection(PageInfo<TblContractPlannode> pageInfo, TblContractPlannode node)
			throws Exception {
		IPage<TblContractPlannode> page = new Page<TblContractPlannode>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblContractPlannode> pageList = tblContractPlannodeMapper.findPlanNodeListForCollection(page, node);
		// 设置分页查询结果
	    pageInfo.setTlist(pageList.getRecords());
	    // 设置查询记录总数
	    pageInfo.setTotalRecord((int)pageList.getTotal());
	}


	@Override
	public void findPlanNodeListForPayment(PageInfo<TblContractPlannode> pageInfo, TblContractPlannode node)
			throws Exception {
		IPage<TblContractPlannode> page = new Page<TblContractPlannode>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblContractPlannode> pageList = tblContractPlannodeMapper.findPlanNodeListForPayment(page, node);
		
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}


	@Override
	public Map<String, Object> saveContractPlannode(TblContractPlannode node, BigDecimal contractId, BigDecimal jbunitid,
			BigDecimal jbstaffid) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            if (node.getNodemoney() != null) {
                BigDecimal contractMoney = this.tblCyhwUnitMapper.findContractMoneyByContractId(contractId);
                BigDecimal sumMoney = this.tblContractPlannodeMapper.findeSumMoneyByContractId(contractId, node.getNodeid());
                if(sumMoney == null) {
                	sumMoney = new BigDecimal(0);
                }
                if (contractMoney!=null && contractMoney.compareTo(sumMoney.add(node.getNodemoney())) == -1) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "合同阶段总金额大于合同金额！");
                    return resultMap;
                }
            }
//        Date startDate = DateUtils.parse(node.getPlanstartdate(), "yyyy-MM-dd");
//        Date endDate = DateUtils.parse(date2, "yyyy-MM-dd");
//        if(date3 != null && !"".equals(date3)) {
//            Date planDate = DateUtils.parse(date3, "yyyy-MM-dd");
//            node.setNodeplanpaydate(planDate);
//        }
            node.setProjectid(contractId);
//        node.setPlanstartdate(startDate);
//        node.setPlanenddate(endDate);
            if (jbstaffid != null) {
                node.setDispatchstaff(jbstaffid);
            }
            if (jbunitid != null) {
                node.setDispatchdept(jbunitid);
            }
            if (node.getNodeid() != null) {
                this.tblContractPlannodeMapper.updateContractPlannode(node);
            } else {
            	node.setNodeid(RandomUtil.uuBigDecimalId());
                this.tblContractPlannodeMapper.saveContractPlannode(node);
            }

        } catch (Exception e) {
            resultMap.put("code", "-1");
            resultMap.put("msg", "保存失败");
            return resultMap;
        }
        resultMap.put("code", "1");
        resultMap.put("msg", "保存成功");
        resultMap.put("data", node);
        return resultMap;
	}


	@Override
	public Map<String, Object> selectById(BigDecimal nodeId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblContractPlannode node = this.tblContractPlannodeMapper.selectByPlanId(nodeId);
        if (node.getBudgetIds()!=null) {//相对方信息回显
        	List<TblCyhwProjectbudget> tcpb = tblCyhwProjectbudgetMapper.getEntityBudget(node.getBudgetIds(),nodeId);
        	node.setTblContractBudgetList(tcpb);
		}
        resultMap.put("code", "1");
        resultMap.put("msg", "成功！");
        resultMap.put("data", node);
        return resultMap;
	}


	@Override
	public Map<String, Object> removeContractPlannode(BigDecimal nodeId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            this.tblContractPlannodeMapper.removeContractPlannode(nodeId);
        } catch (Exception e) {
            resultMap.put("code", "-1");
            resultMap.put("msg", "删除失败");
            return resultMap;
        }
        resultMap.put("code", "1");
        resultMap.put("msg", "删除成功");
        return resultMap;
	}


	@Override
	public Map<String, Object> sendTipEmail(TblCyhwUnit tcu, String content, String date, String token, String staffId)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        Date tipDate = fmt.parse(date);
        long timelong = tipDate.getTime() - nowDate.getTime();
        if (timelong <= 0) {
            resultMap.put("code", "2");
            resultMap.put("msg", "提醒时间不能小于当前系统时间");
            return resultMap;
        }
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
        //this.tblContractPlannodeMapper.sendTipEmail(tcu,timelong,staff,content);
        Timer timer = new Timer();
        final String title;
        if ("无".equals(tcu.getDctype())) {
            title = tcu.getContractname() + "提醒";
        } else {
            title = tcu.getContractname() + tcu.getDctype() + "提醒";
        }
        final String str = "<p>合同名称：" + tcu.getContractname() + "</p>" + "<p>合同编号：" + tcu.getContractno() + "</p>";
        TimerTask task = new TimerTask() {
            public void run() {
                String serverHost = "smtp.qq.com";
                String user = "dev@example.com";  //用户名
                String password = "REDACTED";  //密码
                String fromAddress = "dev@example.com";  //邮件地址
                String fileName = "";
                List<String> receiver = new ArrayList<String>();
                receiver.add(staff.getEmail());
                List<String> ccReceiver = new ArrayList<String>();
                MailInfo mailInfo = new MailInfo(serverHost, user, password, fromAddress, title, str + content, receiver, ccReceiver, fileName);
                try {
                    MessageSender.sendHtmlMail(mailInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // 延迟 1s 打印 wtf 一次
        timer.schedule(task, timelong);
        resultMap.put("code", "1");
        resultMap.put("msg", "设置成功！");
        return resultMap;
	}


	@Override
	public Map<String, Object> findPlannodeListById(BigDecimal contractId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblContractPlannode> nodeList = this.tblContractPlannodeMapper.findPlanNodeListByContractId(contractId);
        resultMap.put("code", "1");
        resultMap.put("msg", "成功！");
        resultMap.put("data", nodeList);
        return resultMap;
	}


	@Override
	public Map<String, Object> findWorkableContractNodeByPageInfo(BigDecimal contractId, Integer pageNumber,
			Integer pageSize, String token, TblContractPlannode node) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil user = null;
        try {
            user = userProvider.get();
            if (user == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            node.setProjectid(contractId);
            
            List<TblContractPlannode> pageList = tblContractPlannodeMapper.findContractNodeListByPageInfo(node);
            PageInfo<TblContractPlannode> pageInfo = new PageInfo<TblContractPlannode>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(1000000);
            pageInfo.setCondition(node);
            pageInfo.setTlist(pageList);
            pageInfo.setTotalRecord(pageList.size());
            resultMap.put("code", "1");
            resultMap.put("msg", "成功！");
            resultMap.put("data", pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
	}


	@Override
	public TblContractPlannode findWriteContractPlanNode(BigDecimal planId) {
		return tblContractPlannodeMapper.findWriteContractPlanNode(planId);
	}

	@Override
	public List<TblAttachment> findeWriteContractPlanFileInfo(BigDecimal planId) {
		return tblContractPlannodeMapper.findeWriteContractPlanFileInfo(planId);
	}


	@Override
	public Map<String, Object> modifyPlanNodeStatus(BigDecimal nodeId, Integer planStatus, String feedback) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        this.tblContractPlannodeMapper.modifyPlanNodeStatus(nodeId, planStatus, feedback);
        /*if(planStatus == 3) {
        	TblContractPlannode node = this.tblContractPlannodeMapper.findWriteContractPlanNode(nodeId);
        	this.tblCyhwUnitMapper.updaContractStatus("7", node.getProjectid().intValue());
        }*/
        resultMap.put("code", "1");
        resultMap.put("msg", "成功！");
        return resultMap;
	}


	@Override
	public Map<String, Object> findPlannodeListByContractId(BigDecimal contractId) {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
         List<TblContractPlannode> nodeList = this.tblContractPlannodeMapper.findPlanNodeListByContractId(contractId);
         resultMap.put("code", "1");
         resultMap.put("msg", "成功！");
         resultMap.put("data", nodeList);
         return resultMap;
	}


}
