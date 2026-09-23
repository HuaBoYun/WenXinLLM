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
public class TblNbsjAuthorizationServiceImpl extends ServiceImpl<TblNbsjAuthorizationMapper, TblNbsjAuthorizationEntity> implements TblNbsjAuthorizationService {

	@Autowired
	private TblNbsjAuthorizationMapper tblNbsjAuthorizationMapper;
	
	@Autowired
	private TblNbsjOperateMapper tblNbsjOperateMapper;
	
	
	@Override
	public TblNbsjAuthorizationEntity get(BigDecimal projectId, Integer aduitProGramId) {
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
	public boolean saveBatch(Collection<TblNbsjAuthorizationEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjAuthorizationEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjAuthorizationEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjAuthorizationEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjAuthorizationEntity getOne(Wrapper<TblNbsjAuthorizationEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjAuthorizationEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjAuthorizationEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjAuthorizationMapper getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjAuthorizationEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
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
	public void newsave(List<TblNbsjAuthorizationEntity> list) {
		try {
			for (TblNbsjAuthorizationEntity tblNbsjAuthorization : list) {
				TblNbsjOperateEntity nbsjOperate = new TblNbsjOperateEntity();
				nbsjOperate.setFinish(TblNbsjOperateEntity.UNFINISH);
//			Serializable serializable = this.tblNbsjAuthorizationDao.save(tblNbsjAuthorization);
//			TblNbsjAuthorization authorization = this.tblNbsjAuthorizationDao.get(serializable);
				
				this.tblNbsjAuthorizationMapper.insertnewEntity(tblNbsjAuthorization);
				
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
