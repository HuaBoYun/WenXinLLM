package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblLegalNegotiatedsettlemen;
import com.huabo.contract.mapper.TblLegalNegotiatedsettlemenMapper;
import com.huabo.contract.service.TblLegalNegotiatedsettlemenService;

@Service
public class TblLegalNegotiatedsettlemenServiceImpl implements TblLegalNegotiatedsettlemenService {

    @Resource
    private TblLegalNegotiatedsettlemenMapper tblLegalNegotiatedsettlemenMapper;

	@Override
	public void findListByPage(PageInfo<TblLegalNegotiatedsettlemen> pageInfo, TblLegalNegotiatedsettlemen negotia,
			BigDecimal pid, BigDecimal disputeid) throws Exception {
		IPage<TblLegalNegotiatedsettlemen> page = new Page<TblLegalNegotiatedsettlemen>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalNegotiatedsettlemen> pageList = tblLegalNegotiatedsettlemenMapper.findListByPage(page, negotia, pid, disputeid);
		
		//findListByPage方法进行分页查询
        pageInfo.setTlist(pageList.getRecords());
        //findListByPageInfoCount方法查询总记录数
        pageInfo.setTotalRecord((int) pageList.getTotal());
	}

	@Override
	public TblLegalNegotiatedsettlemen findById(BigDecimal negotiaId) throws Exception {
		return tblLegalNegotiatedsettlemenMapper.findByNegotiaId(negotiaId);
	}

	@Override
	public void addDiputregistration(TblLegalNegotiatedsettlemen negotiated, String attids) throws Exception {
		if (attids != null && !"".equals(attids)) {
            List<TblAttachment> attList = new ArrayList(0);
            String[] attid = attids.split(",");
            TblAttachment att = null;
            String[] var6 = attid;
            int var7 = attid.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String id = var6[var8];
                att = new TblAttachment();
                att.setAttid(new BigDecimal(id));
                attList.add(att);
            }
            negotiated.setAttList(attList);
        }

        this.tblLegalNegotiatedsettlemenMapper.saveDiputregistration(negotiated);
	}

	@Override
	public void modifyNegotiatedSettlement(TblLegalNegotiatedsettlemen oldNegotiated) throws Exception {
		this.tblLegalNegotiatedsettlemenMapper.updateOldNegotiated(oldNegotiated);
	}

	@Override
	public void removeLegalNegotiatedSettlemen(BigDecimal negotiaId) throws Exception {
		tblLegalNegotiatedsettlemenMapper.removeLegalNegotiatedSettlemen(negotiaId);
	}

	@Override
	public void findListForLitiationList(PageInfo<TblLegalNegotiatedsettlemen> pageInfo,
			TblLegalNegotiatedsettlemen negotiate) throws Exception {
		IPage<TblLegalNegotiatedsettlemen> page = new Page<TblLegalNegotiatedsettlemen>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalNegotiatedsettlemen> pageList = tblLegalNegotiatedsettlemenMapper.findListForLitiationList(page, negotiate);
		
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

}
