/**
 * 
 */
package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblRiskcategory;
import com.huabo.system.mapper.TblRiskcategoryMapper;
import com.huabo.system.service.TblRiskcategoryService;

/**
 * @创建日期:2015年11月2日
 */
@Service
public class TblRiskcategoryServiceImpl implements TblRiskcategoryService {

	@Resource
	private TblRiskcategoryMapper tblRiskcategoryMapper;

	@Override
	public void initRiskCategory(String orgid, String type) {
			List<TblRiskcategory> orglist = tblRiskcategoryMapper.findBysql(orgid, type);
			if (orglist == null || orglist.size() == 0) {
				String[] orgname = new String[]{"企业风险", "业务风险", "专项风险"};
				TblRiskcategory cat = new TblRiskcategory();
				cat.setRiskstatus("0");
				cat.setRiskcatnumber("1");
				cat.setRiskcatname("风险类型");
				cat.setRiskcatdes("初始化");
				cat.setFatherriskcatid(new BigDecimal("0"));
				cat.setUnit(orgid.toString());
				cat.setModuletype(type);
				cat.setRiskcatid(RandomUtil.uuBigDecimalId());
				tblRiskcategoryMapper.save(cat);
				String riskcatid = cat.getRiskcatid().toString();
				for (int i = 0; i < orgname.length; ++i) {
					TblRiskcategory cat1 = new TblRiskcategory();
					cat1.setRiskstatus("0");
					cat1.setRiskcatnumber("2");
					cat1.setRiskcatname(orgname[i]);
					cat1.setRiskcatdes("初始化");
					cat1.setFatherriskcatid(new BigDecimal(riskcatid));
					cat1.setUnit(orgid.toString());
					cat1.setModuletype(type);
					cat1.setRiskcatid(RandomUtil.uuBigDecimalId());
					tblRiskcategoryMapper.save(cat1);
				}
			}
	}
}
