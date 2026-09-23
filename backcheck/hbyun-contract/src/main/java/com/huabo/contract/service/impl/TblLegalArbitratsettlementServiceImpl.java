package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblLegalArbitratsettlement;
import com.huabo.contract.mapper.TblLegalArbitratsettlementMapper;
import com.huabo.contract.service.TblLegalArbitratsettlementService;

@Service
public class TblLegalArbitratsettlementServiceImpl implements TblLegalArbitratsettlementService {

    @Resource
    private TblLegalArbitratsettlementMapper tblLegalArbitratsettlementMapper;

	@Override
	public TblCyhwUnit findContractByDisputeId(BigDecimal disputeId) throws Exception {
		return tblLegalArbitratsettlementMapper.findContractByDisputeId(disputeId);
	}

	@Override
	public TblLegalArbitratsettlement findById(BigDecimal arbitraId) throws Exception {
		return tblLegalArbitratsettlementMapper.findByArbitraId(arbitraId);
	}

	@Override
	public void findListByPageInfo(PageInfo<TblLegalArbitratsettlement> pageInfo, TblLegalArbitratsettlement negotia,
			BigDecimal disputeid) throws Exception {
		IPage<TblLegalArbitratsettlement> page = new Page<TblLegalArbitratsettlement>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalArbitratsettlement> pageList = tblLegalArbitratsettlementMapper.findListByPageInfo(page, negotia, disputeid);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public void addDiputregistration(TblLegalArbitratsettlement arbitrat) throws Exception {
		tblLegalArbitratsettlementMapper.saveDiputergistration(arbitrat);
	}

	@Override
	public void modifyNegotiatedSettlement(TblLegalArbitratsettlement arbitrat) throws Exception {
		tblLegalArbitratsettlementMapper.modifyNegotiatedSettlement(arbitrat);
	}

	@Override
	public void removeLegalNegotiatedSettlemen(BigDecimal arbitraid) throws Exception {
		tblLegalArbitratsettlementMapper.removeLegalNegotiatedSettlemen(arbitraid);
	}
    

}
