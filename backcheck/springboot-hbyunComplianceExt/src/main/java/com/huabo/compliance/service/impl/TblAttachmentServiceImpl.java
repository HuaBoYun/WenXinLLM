package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.mapper.TblAttachmentMapper;
import com.huabo.compliance.mapper.YhrPageMapper;
import com.huabo.compliance.service.ITblAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
@Service
public class TblAttachmentServiceImpl extends ServiceImpl<TblAttachmentMapper, TblAttachment> implements ITblAttachmentService {

    @Resource
    TblAttachmentMapper  attachmentMapper;
    @Resource
    YhrPageMapper mapper;

    @Override
    public List<TblAttachment> findtTblAttachmentByTask(BigDecimal taskid) {
        String sql = "SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN ( SELECT ATTID FROM TBL_COM_EXT_TESTTASK_ATT where TESTTASKID= "+taskid+")";
        return attachmentMapper.getListBySql(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttAndTaskAtt(BigDecimal attid) {
        String sql=" delete from TBL_COM_EXT_TESTTASK_ATT where attid="+attid;
        mapper.delete(sql);
        attachmentMapper.deleteEntity(attid);
    }
    
	@Override
	public void saveEntity(TblAttachment tblAttachmentEntity) throws Exception {
		this.attachmentMapper.insertEntity(tblAttachmentEntity);
	}
}
