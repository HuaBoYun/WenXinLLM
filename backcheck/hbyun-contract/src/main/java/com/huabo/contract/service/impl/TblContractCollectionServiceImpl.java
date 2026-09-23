package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollectionUtil;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractCollection;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblOrgBankaccount;
import com.huabo.contract.mapper.TblContractCollectionMapper;
import com.huabo.contract.mapper.TblContractInvoicesmanagemenMapper;
import com.huabo.contract.mapper.TblContractPlannodeMapper;
import com.huabo.contract.mapper.TblCounterpartBankinfoMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblOrgBankaccountMapper;
import com.huabo.contract.service.TblContractCollectionService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Service("tblContractCollectionService")
@Slf4j
public class TblContractCollectionServiceImpl implements TblContractCollectionService {
	
	@Autowired
	private UserProvider userProvider;
	
	@Resource
    private TblContractCollectionMapper tblContractCollectionMapper;
	
	@Resource
	private TblCyhwUnitMapper tblCyhwUnitMapper;
	
	@Resource
	private TblContractInvoicesmanagemenMapper tblContractInvoicesmanagemenMapper;
	
	@Resource
	private TblCounterpartBankinfoMapper tblCounterpartBankinfoMapper;
	    
	@Resource
	private  TblOrgBankaccountMapper tblOrgBankaccountMapper;
	    
	@Resource
    private TblContractPlannodeMapper tblContractPlannodeMapper;
	@Resource
	private TblStaffService tblStaffService;
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Override
	public Map<String, Object> findCollectionListByPageInfo(String contractname, String contractno,
			TblContractCollection collection, Integer pageNumber, Integer pageSize) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		// 解析用户令牌，获取用户信息
		TblStaffUtil staff = userProvider.get();
		
		// 获取用户所在的组织ID
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		// 如果用户不是合同管理员，设置收款记录的创建者为当前用户
		if(!JudgeRoleRight.judgeRoleRight("合同管理员",staff.getRoleNames())) {
			collection.setCreatestaff(staff.getStaffid());
		}
		IPage<TblContractCollection> page = new Page<TblContractCollection>(pageNumber,pageSize);
		IPage<TblContractCollection> pageList = tblContractCollectionMapper.selectListByPageInfo(page, contractname, contractno, pid, collection);
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
		// 如果flowid为空或为空字符串，从请求Session中获取
		PageInfo<TblContractCollection> pageInfo = new PageInfo<TblContractCollection>();
		// 设置分页信息
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
		resultMap.put("date", pageInfo);
		return resultMap;
	}

	@Override
	public String mengerCollectionEntity(BigDecimal nodeid, BigDecimal contractid, BigDecimal invoiceid,
			BigDecimal invoicemoney, BigDecimal bankbankid, BigDecimal bankid, TblContractCollection collection) throws Exception {
		TblStaffUtil staff = userProvider.get();
		BigDecimal pid = staff.getCurrentOrg().getOrgid();
		BigDecimal staffid = staff.getStaffid();
		
		TblCyhwUnit unit = new TblCyhwUnit();
		unit.setContractid(contractid);
		TblContractPlannode node = new TblContractPlannode();
		node.setNodeid(nodeid);
		TblContractInvoicesmanagemen invoice = new TblContractInvoicesmanagemen();
		invoice.setInvoiceid(invoiceid);
		TblCounterpartBankinfo counterBank = new TblCounterpartBankinfo();
		counterBank.setBankid(bankbankid);//合同管理-财务管理-收款管理-新增-付款银行账号
		TblOrgBankaccount orgBank = new TblOrgBankaccount();
		orgBank.setBankid(bankid);//财务管理-收款管理-新增-收款银行账号
		
		
		/*BigDecimal money = this.tblContractCollectionMapper.checkMoneyNodeId(nodeid);
		BigDecimal skMoney = this.tblContractCollectionMapper.getSkMoneyNodeId(nodeid);
		//开票金额应<=本期应收金额
		if(invoicemoney.compareTo(skMoney)!=1)
		{    //开票金额应<=本期待收金额
			if(Integer.valueOf(money.toString())>0&&invoicemoney.compareTo(new BigDecimal(money))==1){
				return "开票金额应该少于本期代收金额";
			}
		} else{
			return "-1";
		}*/
		
		if(collection.getCollectionid() != null) {
			TblContractCollection oldCollection = this.tblContractCollectionMapper.findByContractId(collection.getCollectionid());
			//oldCollection.setContract(unit);
			oldCollection.setContractid(contractid);
			oldCollection.setNodeid(nodeid);
			oldCollection.setInvoiceid(invoiceid);
			//oldCollection.setCollectionskdate(collection.getCollectionskdate());
			oldCollection.setCreatestaff(staffid);
			//oldCollection.setCreatedate(new Date());
			oldCollection.setCollectionbank(collection.getCollectionbank());
			oldCollection.setCollectionaccount(collection.getCollectionaccount());
			oldCollection.setCollectionorgname(collection.getBudgetname());
			oldCollection.setCounterbank(bankbankid);//合同管理-财务管理-收款管理-新增-收款银行账号
			oldCollection.setOrgbank(bankid);//财务管理-收款管理-新增-付款银行账号
			oldCollection.setLinkorg(pid);
			this.tblContractCollectionMapper.updateById(oldCollection);
			return JsonBean.success("修改成功");
		}else {
			collection.setCollectionid(RandomUtil.uuBigDecimalId());
			collection.setContractid(contractid);//收款合同id
			collection.setNodeid(nodeid);//nodeid//对应收款项id
			collection.setInvoiceid(invoiceid);//invoiceid发票号
			collection.setCollectionskdate(collection.getCollectionskdate());
			collection.setCreatestaff(staffid);//staffid
			collection.setCreatedate(new Date());
			collection.setLinkorg(pid);//pid
			collection.setCollectionorgname(collection.getBudgetname());
			collection.setCounterbank(bankbankid);//合同管理-财务管理-收款管理-新增-收款银行账号
			collection.setOrgbank(bankid);//财务管理-收款管理-新增-付款银行账号
			this.tblContractCollectionMapper.insert(collection);
			return JsonBean.success("新增成功");
		}
	}

	@Override
	public void removeContractCollection(BigDecimal collectionId) throws Exception {
		this.tblContractCollectionMapper.removeContractCollection(collectionId);
	}

	@Override
	public TblContractCollection findCollectionInfoByCollectId(BigDecimal collectionId) throws Exception {
		return tblContractCollectionMapper.findCollectionInfoByCollectId(collectionId);
	}

	@Override
	public List<String> getSkContractIds() {
		 return tblContractCollectionMapper.getSkContractIds();
	}

	@Override
	public String setData(JSONArray arr) {
		 // TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (arr != null) {
            try {
                for (Object j : arr) {
                    net.sf.json.JSONObject obj = (net.sf.json.JSONObject) j;
                    TblContractCollection cole =tblContractCollectionMapper.findByContract(obj.getString("def20"),obj.getString("def19"));
                    if(cole==null){
                    cole=new TblContractCollection();
                    cole.setCollectionbank(obj.getString("collectionbank"));
                    cole.setCollectionaccount(obj.getString("collectionaccount"));
                    cole.setCollectionskdate(obj.get("bill_date") != null ? sdf.parse(obj.get("bill_date").toString()) : null);
                    cole.setNodeid(new BigDecimal(obj.getString("def19")));
                    cole.setContractid(new BigDecimal(obj.getString("def20")));
                    cole.setNodemoney(obj.get("local_money") != null ? new BigDecimal(obj.get("local_money").toString()) : null);
                    cole.setCollectionstatus(TblContractCollection.STATE_WC);
                    TblCyhwUnit  unit= tblCyhwUnitMapper.findContractById(cole.getContractid());
                    cole.setCreatestaff(unit.getCreateuser());
                    cole.setLinkorg(unit.getOrgid());
                  
                    //获取合同相对方 并查询是否有该银行账号 否则新增 
                    TblCounterpartBankinfo bank= tblCounterpartBankinfoMapper.selectBankInfoById(unit.getContractxdfxinfo().toString(),cole.getCollectionaccount());
                    TblOrgBankaccount count= tblOrgBankaccountMapper.findByBankNum(obj.getString("collectionaccount"),unit.getOrgid());
                    if(bank==null){
                        bank=new TblCounterpartBankinfo();
                        bank.setBankaccount(cole.getCollectionaccount());
                        bank.setBankkhyh(cole.getCollectionbank());
                        bank.setBanknature(new BigDecimal(0));
                        bank.setBankstatus(new BigDecimal(1));
                        bank.setCreatestaff(unit.getCreateuser());
                        bank.setCreatetime(new Date());
                        bank.setBudgetid(unit.getContractxdfxinfo());
                        bank.setBankid(RandomUtil.uuBigDecimalId());
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
                        count.setBankid(RandomUtil.uuBigDecimalId());
                        tblOrgBankaccountMapper.savebankInfo(count);
                    }
                     cole.setCounterbank(bank.getBankid());
                     cole.setOrgbank(count.getBankid());
                     cole.setOrgBankName(bank.getBankaccname());
                    TblContractInvoicesmanagemen men=new TblContractInvoicesmanagemen();
                    men.setInvoiceno((new Date()).getTime()+"");
                    men.setInvoicemoney(obj.get("local_money") != null ? new BigDecimal(obj.get("local_money").toString()) : new BigDecimal(0));
                    men.setInvoicedate(obj.get("bill_date") != null ? sdf.parse(obj.get("bill_date").toString()) : null);
                 //   tblCyhwUnitMapper.updaContractStatus(unit.getContractid().toString(), 8);  验证合同收付款金额是否已经全部完成，验证合同履行阶段是否全部完成
//                    TblContractSpnode  spNode = new TblContractSpnode();
//                    spNode.setContractid(new BigDecimal(obj.getString("def20").toString()));
//                    spNode.setNodeid(new BigDecimal(obj.getString("def19").toString()));
//                     this.tblContractSpnodeMapper.saveBySpnode(spNode);
                    if (obj.getString("def19")!= null) {
                        TblContractPlannode node = tblContractPlannodeMapper.findBynodeId(new BigDecimal(obj.getString("def19")));
//                        node.setPlannodestatus(new BigDecimal("2"));
//                        tblContractPlannodeMapper.updateContractPlannode(node);
                        men.setInvoicepost(new BigDecimal(node.getNodepost()));
                        men.setInvoicecontent(node.getNodecontent());
                    }
                    men.setInvoiceid(RandomUtil.uuBigDecimalId());
                    tblContractInvoicesmanagemenMapper.saveMergenEntity(men);
                    cole.setInvoiceid(men.getInvoiceid());
                    cole.setCollectionid(RandomUtil.uuBigDecimalId());
                    tblContractCollectionMapper.SaveMengerCollectionEntity(cole);
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

    

}
