package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuthorizationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjOperateMapper;
import com.huabo.audit.service.TblNbsjAuthorizationService;
@Service
public class TblNbsjAuthorizationServiceImpl  implements TblNbsjAuthorizationService {

	@Autowired
	private TblNbsjAuthorizationMapper tblNbsjAuthorizationMapper;
	
	@Autowired
	private TblNbsjOperateMapper tblNbsjOperateMapper;
	
	
	@Override
	public TblNbsjAuthorizationEntity get(BigDecimal projectId, BigDecimal aduitProGramId) {
//		TblNbsjAuthorizationEntity tblNbsjAuthorizationEntity = baseMapper.get(projectId+"", aduitProGramId+"");
//		return tblNbsjAuthorizationEntity;
		List<TblNbsjAuthorizationEntity> list = this.tblNbsjAuthorizationMapper.getByPjPg(projectId, aduitProGramId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
		
	}

	@Override
	public void delete(TblNbsjAuthorizationEntity tblNbsjAuthorization) {
		// TODO Auto-generated method stub

	}

	 
	@Override
	public void merge(List<TblNbsjAuthorizationEntity> list) {
		
		try {
			for (TblNbsjAuthorizationEntity tblNbsjAuthorization : list) {
				
				this.tblNbsjAuthorizationMapper.updateEntity(tblNbsjAuthorization);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void save(List<TblNbsjAuthorizationEntity> list) {
		try {
			for (TblNbsjAuthorizationEntity tblNbsjAuthorization : list) {
				TblNbsjOperateEntity nbsjOperate = new TblNbsjOperateEntity();
				nbsjOperate.setFinish(TblNbsjOperateEntity.UNFINISH);
//			Serializable serializable = this.tblNbsjAuthorizationDao.save(tblNbsjAuthorization);
//			TblNbsjAuthorization authorization = this.tblNbsjAuthorizationDao.get(serializable);
				
				this.tblNbsjAuthorizationMapper.insertEntity(tblNbsjAuthorization);
				
				nbsjOperate.setAuthId(tblNbsjAuthorization.getAuthId());
				this.tblNbsjOperateMapper.insertEntity(nbsjOperate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<TblNbsjAuthorizationEntity> getByProjectId(BigDecimal projectId) {
		List<TblNbsjAuthorizationEntity> list = this.tblNbsjAuthorizationMapper.getByProjectId(projectId);
		return list;
	}

}
