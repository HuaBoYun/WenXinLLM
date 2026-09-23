package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblExperienceTypeEntity;
import com.huabo.audit.oracle.mapper.TblExperienceTypeMapper;
import com.huabo.audit.service.TblExperienceTypeService;

/*import de.odysseus.el.tree.Tree;*/

@Service
public class TblExperienceTypeServiceImpl extends ServiceImpl<TblExperienceTypeMapper, TblExperienceTypeEntity>  implements TblExperienceTypeService {

/*	@Override
	public List<Tree> getRoot(Integer tempId, String url) {
		List<Tree> listTree = baseMapper.getRoot(tempId+"", url);
		return listTree;
	}

	@Override
	public List<Tree> getTree(Integer parentId, Integer tempId, String url) {
		List<Tree> listTree = baseMapper.getTree(parentId+"",tempId+"", url);
		return listTree;
	}*/

	@Override
	public int getCount(Integer tempId, Integer targetId) {
		int cnt = baseMapper.getCount(tempId+"", targetId+"");
		return cnt;
	}

	@Override
	public void deleteByTempId(BigDecimal bigDecimal) {
		baseMapper.deleteByTempId(bigDecimal+"");

	}

	@Override
	public void merge(TblExperienceTypeEntity tblExperienceType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TblExperienceTypeEntity tblExperienceType) {
		BigDecimal targetid = tblExperienceType.getTargetId();
		baseMapper.delete(targetid+"");
		
	}

}
