package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblOrgBankaccount;
import com.huabo.contract.mapper.TblContractInvoicesmanagemenMapper;
import com.huabo.contract.mapper.TblContractPaymentMapper;
import com.huabo.contract.mapper.TblContractPlannodeMapper;
import com.huabo.contract.mapper.TblContractSpnodeMapper;
import com.huabo.contract.mapper.TblCounterpartBankinfoMapper;
import com.huabo.contract.mapper.TblCyhwProjectbudgetMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblOrgBankaccountMapper;
import com.huabo.contract.service.TblContractPaymenService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TblContractPaymenServiceImpl implements TblContractPaymenService {
	
	@Autowired
	private UserProvider userProvider;

    @Resource
    private TblContractPaymentMapper tblContractPaymentMapper;
    
    @Resource
    private TblContractSpnodeMapper tblContractSpnodeMapper;

    @Resource
    private TblContractPlannodeMapper tblContractPlannodeMapper;

    @Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;
    @Resource
    private TblCounterpartBankinfoMapper tblCounterpartBankinfoMapper;
    @Resource
    private TblContractInvoicesmanagemenMapper tblContractInvoicesmanagemenMapper;

    @Resource
    private  TblOrgBankaccountMapper tblOrgBankaccountMapper;
    @Resource
    private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
    
    
	@Override
	public Map<String, Object> findPaymentManagemenByPageInfo(Integer pageNumber, Integer pageSize,
			TblContractPayment payment, TblCyhwUnit unit) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		String orgid = staff.getCurrentOrg().getOrgid().toString();
		if(!JudgeRoleRight.judgeRoleRight("合同管理员",staff.getRoleNames())) {
			payment.setCreatestaff(staff.getStaffid());
		}
		
		IPage<TblContractPayment> page = new Page<TblContractPayment>(pageNumber,pageSize);
		IPage<TblContractPayment> pageList =  this.tblContractPaymentMapper.selectPaymentManagemenByPageInfo(page, orgid, payment, unit);
		
		PageInfo<TblContractPayment> pageInfo = new PageInfo<TblContractPayment>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        dataMap.put("pageInfo", pageInfo);
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", dataMap);
        return resultMap;
	}

	@Override
	public String mengerPaymentInfo(TblContractPayment payment, BigDecimal invoiceid, BigDecimal applyStaffId) throws Exception {
		
		TblStaffUtil staff = userProvider.get();
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		
		payment.setLinkorg(pid);
		if(applyStaffId == null) {
			applyStaffId = staff.getStaffid();
		}
		payment.setApplystaff(applyStaffId);
		TblContractInvoicesmanagemen invoice = this.tblContractInvoicesmanagemenMapper.getInvoiceid(invoiceid);
		payment.setPaymenmoney(invoice.getInvoicemoney());
		
		if(payment.getPaymentid() != null) {
			this.tblContractPaymentMapper.updateById(payment);
			return JsonBean.success("修改成功");
		}else {
			payment.setCreatestaff(staff.getStaffid());
			payment.setPaymenmoney(invoice.getInvoicemoney());
			payment.setPaymentid(RandomUtil.uuBigDecimalId());
			this.tblContractPaymentMapper.insert(payment);//savePaymentInfo(payment);
			return JsonBean.success("新增成功");
		}
	}

	@Override
	public void removePaymentInfo(BigDecimal paymentId) throws Exception {
		 this.tblContractPaymentMapper.deletePaymentId(paymentId);
	}

	@Override
    public TblContractPayment findPaymentInfoByParmentId(BigDecimal paymentId) throws Exception {
		return tblContractPaymentMapper.findPaymentInfoByParmentId(paymentId);
    }

	@Override
	public String setData(JSONArray arr, String type) {
		// TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (arr != null) {
            try {
                for (Object j : arr) {
                    net.sf.json.JSONObject obj = (net.sf.json.JSONObject) j;
                    TblContractPayment payment = tblContractPaymentMapper.findByParment(obj.getString("zyx62"), obj.getString("zyx61"));
                    if(payment==null){
                        payment=new TblContractPayment();
                        payment.setPaymenttitle(obj.getString("collectionaccount") + obj.getString("total"));
                        payment.setPaymentlatedate(obj.get("paydate") != null ? sdf.parse(obj.get("paydate").toString()) : null);
                        payment.setBankaccount(obj.get("collectionaccount") != null ? obj.get("collectionaccount").toString() : null);
                        payment.setNodeid(new BigDecimal(obj.getString("zyx61")));
                        payment.setContractid(new BigDecimal(obj.getString("zyx62")));
                        TblCyhwUnit  unit=tblCyhwUnitMapper.findContractById(payment.getContractid());
                        payment.setCreatestaff(unit.getCreateuser());
                        payment.setLinkorg(unit.getOrgid());
                        payment.setContractstatus(6);
                        payment.setBudgetid(unit.getContractxdfxinfo());
                        payment.setPaymentstatus(TblContractPayment.STATE_WC);
                        payment.setPaymenmoney(obj.get("total") != null ? new BigDecimal(obj.get("total").toString()) : new BigDecimal(0));
                        //获取合同相对方 并查询是否有该银行账号 否则新增
                        TblCounterpartBankinfo bank= tblCounterpartBankinfoMapper.selectBankInfoById(unit.getContractxdfxinfo().toString(),payment.getBankaccount());
                        TblOrgBankaccount count= tblOrgBankaccountMapper.findByBankNum(obj.getString("collectionaccount"),unit.getOrgid());
                        if(bank==null){
                            bank=new TblCounterpartBankinfo();
                            bank.setBankaccount(obj.getString("collectionaccount"));
                            bank.setBankkhyh(obj.getString("collectionbank"));
                            bank.setBanknature(new BigDecimal(0));
                            bank.setBankstatus(new BigDecimal(1));
                            bank.setCreatestaff(unit.getCreateuser());
                            bank.setCreatetime(new Date());
                            bank.setBudgetid(unit.getContractxdfxinfo());
                            tblCounterpartBankinfoMapper.saveBank(bank);
                        }
                        if(count==null){
                            count= new TblOrgBankaccount();
                            count.setBankaccname(obj.getString("collectionbank"));
                            count.setBankcode(obj.getString("collectionaccount"));
                            count.setBankaccnum(obj.getString("collectionaccount"));
                            count.setBankkhyh(obj.getString("collectionbank"));
                            count.setBankstate(new BigDecimal(0));
                            count.setBankstatus(new BigDecimal(1));
                            count.setOrgid(unit.getOrgid());
                            count.setCreatedate(new Date());
                            tblOrgBankaccountMapper.savebankInfo(count);
                        }
                        payment.setCounterbank(bank.getBankid());
                        payment.setOrgbank(count.getBankid());
                        TblContractInvoicesmanagemen men=new TblContractInvoicesmanagemen();
                        men.setInvoiceno((new Date()).getTime()+"");
                        men.setInvoicemoney(obj.get("total") != null ? new BigDecimal(obj.get("total").toString()) : new BigDecimal(0));
                        men.setInvoicedate(obj.get("paydate") != null ? sdf.parse(obj.get("paydate").toString()) : null);
//
//                    TblContractSpnode  spNode = new TblContractSpnode();
//                    spNode.setContractid(new BigDecimal(obj.getString("zyx62").toString()));
//                    spNode.setNodeid(new BigDecimal(obj.getString("zyx61").toString()));
//                     this.tblContractSpnodeMapper.saveBySpnode(spNode);
                        if (obj.getString("zyx61") != null) {
                            TblContractPlannode node = tblContractPlannodeMapper.findBynodeId(new BigDecimal(obj.getString("zyx61")));
//                        node.setPlannodestatus(new BigDecimal("2"));
//                        tblContractPlannodeMapper.updateContractPlannode(node);
                            men.setInvoicepost(new BigDecimal(node.getNodepost()));
                            men.setInvoicecontent(node.getNodecontent());
                        }
                        tblContractInvoicesmanagemenMapper.saveMergenEntity(men);
                        payment.setInvoiceid(men.getInvoiceid());
                        payment.setPaymentid(RandomUtil.uuBigDecimalId());
                        tblContractPaymentMapper.savePaymenInfo(payment);

                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            resultMap.put("code", "1");
            resultMap.put("msg", "失败！");
        }
        return resultMap.toString();
	}

	@Override
	public Map<String, Object> setUpdatePayment(String token, String contract, String payAmount, String payDate,
			String mark, String bankName, String bank, String accountName, String accountNo, String paybank,
			String payaccountName, String payaccountNo, String abstracts) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                resultMap.put("code", "500");
                resultMap.put("flag", "false");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            TblContractPayment payment=new TblContractPayment();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //付款时间
            payment.setPaymentlatedate(sdf.parse(payDate));
            //申请日期
            payment.setApplydate(new Date());
            //付款备注
            payment.setPaymentmemo(mark);
            //1.根据合同号去TBL_CYHW_UNIT表查询合同的ID。
            TblCyhwUnit  cyunit=tblCyhwUnitMapper.findContractByContractno(contract);
            payment.setContractid(new BigDecimal(cyunit.getContractid().toString()));
            //合同总金额
            BigDecimal contractmoney=cyunit.getContractmoney();
            payment.setPaymenttitle(cyunit.getContractname()+" 付款金额:"+payAmount ); //abstracts
            payment.setBankaccount(accountNo);
            payment.setCreatestaff(cyunit.getCreateuser());
            payment.setLinkorg(cyunit.getOrgid());
            payment.setApplystaff(staff.getStaffid());
            payment.setApplyorg(staff.getCurrentOrg().getOrgid());
            payment.setContractstatus(6);
            //获取合同的相对方列表
            List<TblCyhwProjectbudget> budgetList = this.tblCyhwProjectbudgetMapper.seleCyhwProjectbudgets(cyunit.getContractid());
            //根据传入的收款方名称判断是否有该相对方
            for(TblCyhwProjectbudget bug:budgetList){
            	if(bug.getBudgetname().contentEquals(bankName)){
                    payment.setBudgetid(new BigDecimal(bug.getBudgetid().toString()));//匹配相对方信息
            	}
            }
            /*
             * 收款银行账号信息
             * */
            //根据传入的收款账号信息判断是否有该银行账号 
            List<TblCounterpartBankinfo> bankInfoList=tblCounterpartBankinfoMapper.findListByBankInfo(accountNo);
            //有该银行账号匹配 收款银行账号及相对方信息
            if(bankInfoList!=null&&bankInfoList.size()>0){
            	for(TblCounterpartBankinfo info:bankInfoList){
            		//查询系统银行账号与付款接口账号相同的数据
            		 if(info.getBankaccount().equals(accountNo)){
                         payment.setCounterbank(info.getBankid());//匹配收款银行账号
            		 }
            	}
            }else{
            //没有该银行账号新建银行信息
            	TblCounterpartBankinfo  newBank=new TblCounterpartBankinfo();
            	     newBank.setBankaccount(accountNo);
            	     newBank.setBankaccname(accountName);
            	     newBank.setBankkhyh(bank);
                     newBank.setBanknature(new BigDecimal(0));
                     newBank.setBankstatus(new BigDecimal(1));
                     newBank.setCreatestaff(staff.getStaffid());
                     newBank.setCreatetime(new Date());
                     newBank.setBudgetid(payment.getBudgetid());
                     newBank.setBankid(RandomUtil.uuBigDecimalId());
                     tblCounterpartBankinfoMapper.saveBank(newBank);
                     payment.setCounterbank(newBank.getBankid());//匹配收款银行账号
            }
            /*
             *  付款银行账号信息
             *  验证是否新增
             * */
            TblOrgBankaccount count= tblOrgBankaccountMapper.findByBankNum(payaccountNo,cyunit.getOrgid());
            if(count==null){
            	count= new TblOrgBankaccount();
                count.setBankaccname(payaccountName);
                count.setBankaccnum(payaccountNo);
                count.setBankkhyh(paybank);
                count.setBankstate(new BigDecimal(0));
                count.setBankstatus(new BigDecimal(1));
                count.setOrgid(cyunit.getOrgid());
                count.setCreatedate(new Date());
                count.setBankid(RandomUtil.uuBigDecimalId());
                tblOrgBankaccountMapper.savebankInfo(count);
            }
            payment.setOrgbank(count.getBankid());
            //本次付款金额
            BigDecimal fkMoney= new BigDecimal(payAmount);
            //未付金额
            BigDecimal nopayMoney=null;
            //累积付款金额
            BigDecimal payMoney=null;
            //不算本次的累积付款金额
            BigDecimal paymenmoney= tblContractPaymentMapper.getAllPaymenmoney(cyunit.getContractid());
            if(paymenmoney!=null){
            	payMoney=paymenmoney.add(fkMoney); //累积付款金额+本次付款金额
            }else{
            	payMoney= fkMoney;
            }
            //判断是否有合同金额
            if(cyunit!=null&&( cyunit.getContractmoney().compareTo(new BigDecimal("0"))>0)){
              nopayMoney=cyunit.getContractmoney().subtract(payMoney);  //未付款金额  = 合同总金额-已经付款金额
              payment.setNoPaymoney(nopayMoney);
              payment.setPaymenmoney(fkMoney);
              payment.setAccumulatedpayments(payMoney);
              payment.setPaymentstatus(TblContractPayment.STATE_WC);
            }else{ //无金额的直接新增
            	payment.setAccumulatedpayments(paymenmoney);
            	payment.setPaymenmoney(fkMoney);
            	payment.setPaymentstatus(TblContractPayment.STATE_WC);
            }
            payment.setPaymentid(RandomUtil.uuBigDecimalId());
            tblContractPaymentMapper.savePaymenInfo(payment);
            resultMap.put("code", "200");
            resultMap.put("flag", "true");
            resultMap.put("msg", "新增付款明细成功！");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "201");
            resultMap.put("flag", "false");
            resultMap.put("msg", "数据异常，插入失败！");
        }
        return resultMap;
	}

	@Override
	public Map<String, Object> setCustomers(JSONArray inParametersArr) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        JSONObject obj =null;
        if (inParametersArr != null&&inParametersArr.size()>0) {
            try {

                for (Object j : inParametersArr) {
                    obj = (JSONObject) j;
                    //基本信息
                    TblCyhwProjectbudget projectbudget=new TblCyhwProjectbudget();
                    projectbudget.setCounterpartno(obj.getString("bmbh") != null ? obj.get("bmbh").toString() : null);//dwbh   客户编号
                    projectbudget.setBudgetname(obj.getString("dwmc")!= null ? obj.get("dwmc").toString() : null);//dwmc	客户名称
                    projectbudget.setCounterparttype(obj.getString("dwlb")!= null ? obj.get("dwlb").toString() : null);//dwlb	单位类别
                    projectbudget.setCounterpartaddress(obj.getString("ssdq")!= null ? obj.get("ssdq").toString() : null);//ssdq	所属地区
                    projectbudget.setServicetype(obj.getString("sfnbdw")!= null ? obj.get("dwmc").toString() : null);//sfnbdw	是否内部单位
                    projectbudget.setProjectgoal(obj.getString("nbdwbh")!= null ? obj.get("nbdwbh").toString() : null);//nbdwbh	内部单位编号
                    projectbudget.setContactsadress(obj.getString("ssgj")!= null ? obj.get("ssgj").toString() : null);//ssgj	所属国家
                    projectbudget.setOppositenature(obj.getString("sshy")!= null ? obj.get("sshy").toString() : null);//sshy	所属行业
                    projectbudget.setCounterparttype(obj.getString("syzlx")!= null ? obj.get("syzlx").toString() : null);//syzlx	所有者类型
                    projectbudget.setRecordtype(obj.getString("nslx")!= null ? obj.get("nbdwbh").toString() : null);//nslx	纳税类型
                    projectbudget.setCounterpartphone(obj.getString("tel")!= null ? obj.get("tel").toString() : null);//tel	电话

                    //根据客户编号查询，如果有就更新基本信息和银行信息，没有就新增
                    TblCyhwProjectbudget budget = tblCyhwProjectbudgetMapper.findByBmbh(obj.getString("bmbh"));

                    int num=0;
                    if(budget==null){
                    	projectbudget.setBudgetid(RandomUtil.uuBigDecimalId());
                        num=tblCyhwProjectbudgetMapper.insert(projectbudget);
                    }else{
                        tblCyhwProjectbudgetMapper.updateBybmbh(projectbudget);
                    }

                    TblCounterpartBankinfo tblCounterpartBankinfo=null;

                    //银行信息

                    if(obj.get("yhzhList")!=null){
                        JSONArray yhzhList= (JSONArray) obj.get("yhzhList");
                        for (Object h : yhzhList) {
                            JSONObject obh = (JSONObject) h;
                            tblCounterpartBankinfo=new TblCounterpartBankinfo();
                            tblCounterpartBankinfo.setBankaccount(obh.getString("yhzhbh")!= null ? obj.get("syzlx").toString() : null);//yhzhbh	银行账户编号
                            tblCounterpartBankinfo.setBankaccname(obh.getString("yhzhmc")!= null ? obj.get("yhzhmc").toString() : null);//yhzhmc	银行账户名称
                            tblCounterpartBankinfo.setBankstatus(new BigDecimal(obh.getString("zhzt"))!= null ? new BigDecimal(obj.getString("yhzhmc")) : null);//zhzt	账户状态
                            tblCounterpartBankinfo.setBanknature(new BigDecimal(obh.getString("zhlb"))!= null ? new BigDecimal(obj.get("zhlb").toString()) : null);//zhlb	账户类别
                            tblCounterpartBankinfo.setBankkhyh(obh.getString("khh")!= null ? obj.get("khh").toString() : null);//khh	开户行
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("dpkhh")!= null ? obj.get("dpkhh").toString() : null);//dpkhh	电票开户行  ????
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("sfmrzh")!= null ? obj.get("sfmrzh").toString() : null);//sfmrzh	默认账户  ????
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("shuihao")!= null ? obj.get("shuihao").toString() : null);//shuihao	税号 ????????
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("xzqy")!= null ? obj.get("xzqy").toString() : null);//xzqy	行政区域 ????????
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("qqlsh")!= null ? obj.get("qqlsh").toString() : null);//qqlsh	流水号????????
                            //tblCounterpartBankinfo.setBankaccount(obh.getString("tybz")!= null ? obj.get("tybz").toString() : null);//tybz	停用标志????????
                            if(num>0){
                            	tblCounterpartBankinfo.setBankid(RandomUtil.uuBigDecimalId());
                                tblCounterpartBankinfoMapper.insert(tblCounterpartBankinfo);
                            }else{
                                tblCounterpartBankinfoMapper.updateBybmbh(tblCounterpartBankinfo);
                            }
                        }
                    }
                }
                resultMap.put("qqlsh", obj.getString("bmbh"));
                resultMap.put("successFlg", "0");
                //resultMap.put("faiLReason", "新增/更新成功！");
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("qqlsh", obj.getString("bmbh"));
                resultMap.put("successFlg", "1");
                resultMap.put("faiLReason", "新增/更新失败！");
            }
        }else{
            //resultMap.put("qqlsh", obj.getString("bmbh"));
            resultMap.put("successFlg", "1");
            resultMap.put("faiLReason", "入参不能为空");

        }
        return resultMap;
	}

}
