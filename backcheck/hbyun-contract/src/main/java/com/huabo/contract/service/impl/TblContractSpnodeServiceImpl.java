package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblContractSpnode;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mapper.TblContractPlannodeMapper;
import com.huabo.contract.mapper.TblContractSpnodeMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.service.TblContractSpnodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblContractSpnodeServiceImpl implements TblContractSpnodeService {
	
	@Resource
	private TblContractPlannodeMapper tblContractPlannodeMapper;
	
	@Resource
    private TblContractSpnodeMapper tblContractSpnodeMapper;
	
	@Resource
    private TblCyhwUnitMapper tblCyhwUnitMapper;
	
	@Resource
	private UserProvider userProvider;
	
	@Override
	public Map<String, Object> findListByXdf(Integer pageNumber, Integer pageSize, String budgetid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 
		IPage<TblContractSpnode> page = new Page<TblContractSpnode>(pageNumber, pageSize);
        IPage<TblContractSpnode> pageList = tblContractSpnodeMapper.findListByXdf(page,budgetid);
		
		PageInfo<TblContractSpnode> pageInfo = new PageInfo<TblContractSpnode>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		// 设置分页查询结果
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		// 设置返回结果码和消息
		resultMap.put("code", "1");
		resultMap.put("msg", "访问接口成功");
		resultMap.put("data", pageInfo);
		return resultMap;
	}

	@Override
	public Map<String, Object> saveOrUpdateContractSpnode(TblContractSpnode spNode, BigDecimal nodeId,
			BigDecimal contractId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TblCyhwUnit unit = this.tblCyhwUnitMapper.getCyhwUnitEntity(contractId);
        TblContractPlannode node = this.tblContractPlannodeMapper.findBynodeId(nodeId);
        if (spNode.getSpnodeid() != null) {
            spNode.setNodeid(node.getNodeid());
            spNode.setContractid(unit.getContractid());
            this.tblContractSpnodeMapper.updateBySpnode(spNode);
        } else {
            spNode.setNodeid(node.getNodeid());
            spNode.setContractid(unit.getContractid());
            spNode.setSpnodeid(RandomUtil.uuBigDecimalId());
            this.tblContractSpnodeMapper.saveBySpnode(spNode);
        }
        this.tblContractPlannodeMapper.updatePlanNodeStatus(node.getNodeid(),1);
        
        //判断合同所有履行阶段均已完成
        /*Integer noCount = this.tblContractPlannodeMapper.selectNoDoneCount(contractId);
        if(noCount == 0) {
        	this.tblCyhwUnitMapper.updaContractStatus(contractId.toString(), 17);
        }*/
        
        resultMap.put("code", "1");
        resultMap.put("msg", "成功！");
        resultMap.put("data", spNode.getSpnodeid());
        return resultMap;
	}

	@Override
	public JsonBean modifyCompleteSpNode(String token, BigDecimal nodeId, BigDecimal contractId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		this.tblContractPlannodeMapper.updatePlanNodeStatus(nodeId,2);
		Integer noCount = this.tblContractPlannodeMapper.selectNoDoneCount(contractId);
        if(noCount == 0) {
        	this.tblCyhwUnitMapper.updaContractStatus(contractId.toString(), 17);
        }
        return ResponseFormat.retParam(1, 200, null);
	}

}
