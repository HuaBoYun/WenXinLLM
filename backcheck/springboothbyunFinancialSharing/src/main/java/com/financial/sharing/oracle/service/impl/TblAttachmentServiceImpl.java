package com.financial.sharing.oracle.service.impl;

import com.financial.sharing.oracle.entity.TblAttachment;
import com.financial.sharing.oracle.mapper.TblAttachmentMapper;
import com.financial.sharing.oracle.service.TblAttachmentService;
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
