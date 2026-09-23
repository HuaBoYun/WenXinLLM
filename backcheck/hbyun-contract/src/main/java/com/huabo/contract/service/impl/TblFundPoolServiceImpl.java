package com.huabo.contract.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.contract.mapper.TblFundPoolMapper;
import com.huabo.contract.service.TblFundPoolService;

import lombok.extern.slf4j.Slf4j;

@Service("TblFundPoolServiceImpl")
@Slf4j
public class TblFundPoolServiceImpl implements TblFundPoolService {
 
	@Resource
	private TblFundPoolMapper fundPoolMapper;

	public void saveTblFundPool(String id, String no, String name)
			throws Exception {
			try {
				int size = fundPoolMapper.selectTblFundPool(id);
				if (size > 0) {
					fundPoolMapper.updateTblFundPool(id, no, name);
				} else {
					fundPoolMapper.insertTblFundPool(id, no, name);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void updateTblFundPool(String id, String no, String name)
			throws Exception {
			try {
				fundPoolMapper.updateTblFundPool(id, no, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
