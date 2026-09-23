package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TblAttachmentServiceImpl implements TblAttachmentService {

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;

	@Override
	public List<TblAttachment> findByIds(String ids) {
		Example example = new Example(TblAttachment.class);
		example.createCriteria().andIn("attid", Arrays.asList(ids.split(",")));
		return tblAttachmentMapper.selectByExample(example);
	}
}
