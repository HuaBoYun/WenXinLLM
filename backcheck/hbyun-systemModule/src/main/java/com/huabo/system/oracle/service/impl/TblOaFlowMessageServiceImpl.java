package com.huabo.system.oracle.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblOaFlowMessage;
import com.huabo.system.mapper.TblOaFlowMessageMapper;
import com.huabo.system.oracle.service.TblOaFlowMessageService;

@Service
public class TblOaFlowMessageServiceImpl implements TblOaFlowMessageService {

	@Resource
	private TblOaFlowMessageMapper tblOaFlowMessageMapper;

	@Override
	public void saveOrUpdateEntity(TblOaFlowMessage tblOaFlowMessage) throws Exception {
		
		TblOaFlowMessage message = this.tblOaFlowMessageMapper.selectById(tblOaFlowMessage.getId());
		if(message == null) {
			message.setId(RandomUtil.uuStringId());
			this.tblOaFlowMessageMapper.insert(tblOaFlowMessage);
		}else {
			this.tblOaFlowMessageMapper.updateById(tblOaFlowMessage);
		}
	}

	@Override
	public String selectFlowTypeById(String id) throws Exception {
		return this.tblOaFlowMessageMapper.selectFlowTypeById(id);
	}

	@Override
	public TblOaFlowMessage findEntityById(String id) throws Exception {
		return this.tblOaFlowMessageMapper.selectById(id);
	}

	@Override
	public void modifyFlowMessage(TblOaFlowMessage message) throws Exception {
		this.tblOaFlowMessageMapper.updateById(message);
	}

	@Override
	public void setPageInfoList(PageInfo<TblOaFlowMessage> pageInfo) throws Exception {
		IPage<TblOaFlowMessage> page = new Page<TblOaFlowMessage>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblOaFlowMessage> pageList = this.tblOaFlowMessageMapper.selectPageInfoList(page,pageInfo.getCondition());
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
	}


}
