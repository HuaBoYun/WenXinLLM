package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblBugCriterionEntity;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblBugCriterionMapper;
import com.huabo.monitor.service.TblBugCriterionService;
import com.huabo.monitor.vo.param.TblBugCriterionEntityParam;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class TblBugCriterionServiceImpl extends ServiceImpl<TblBugCriterionMapper, TblBugCriterionEntity> implements TblBugCriterionService {
    public static final int DEFAULT_SIZE = 20;

    @Resource
    TblBugCriterionMapper tblBugCriterionMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblBugCriterionEntity findByTblBugCriterion(String bugid) {
        TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByTblBugCriterion(bugid);
        return tblBugCriterionEntity;
    }

    @Override
    public IPage<TblBugCriterionEntity> findAll(Integer pageNumber, String orgId, Integer pageSize) {
        Page<TblBugCriterionEntity> page = new Page<>(pageNumber, DEFAULT_SIZE);
        String sql = " SELECT * FROM TBL_BUGCRITERION where 1=1 and BUGCRIID in ( SELECT max(BUGCRIID) FROM TBL_BUGCRITERION where ORGID=" + orgId + " GROUP BY BUGCRILEVEL )";
        return tblBugCriterionMapper.getSqlPage(page, sql);
    }
     
    @Override
    public List<TblBugCriterionEntity> findAll() {
        List<TblBugCriterionEntity> list = baseMapper.findAll("");
        return list;
    }

    @Override
    public List<TblBugCriterionEntity> findAll(String orgid) {
        List<TblBugCriterionEntity> list = baseMapper.findAll(orgid);
        return list;
    }

    @Override
    public TblBugCriterionEntity findByid(String id) {
        TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByid(id);
        return tblBugCriterionEntity;
    }

    @Override
    public List<TblBugCriterionEntity> fingByLevel(String orgid, String level) {
        List<TblBugCriterionEntity> list = baseMapper.fingByLevel(orgid, level);
        return list;
    }

    @Override
    public void saveEntity(TblBugCriterionEntity entity) {
        baseMapper.insert(entity);
    }

    @Override
    public void update(TblBugCriterionEntity entity) {
        tblBugCriterionMapper.updateById(entity);
    }

    @Override
    public void del(BigDecimal bugcriid) {
        tblBugCriterionMapper.deleteById(bugcriid);
    }

	@Override
	public PageInfo<TblBugCriterionEntity> findAllNewPage(Integer pageNumber, String orgId, Integer pageSize,String token) throws Exception {
		// TODO Auto-generated method stub
//		 Page<TblBugCriterionEntity> page = new Page<>(pageNumber, DEFAULT_SIZE);
//	        String sql = " SELECT * FROM TBL_BUGCRITERION where 1=1 and BUGCRIID in ( SELECT max(BUGCRIID) FROM TBL_BUGCRITERION where ORGID=" + orgId + " GROUP BY BUGCRILEVEL )";
//	        return tblBugCriterionMapper.getSqlPage(page, sql);
		PageInfo<TblBugCriterionEntity> pageinfo=null;
		try {
		TblStaffUtil staff = userProvider.get();
		List<Integer> bugcriids=tblBugCriterionMapper.selectMaxIds(orgId);
		TblBugCriterionEntityParam param=new TblBugCriterionEntityParam();
		param.setBugcriids(bugcriids);
		String sql = GeneralSQLConcatConfig.concatSecrectSql(staff.getCurrentOrg().getUseSecrect(), false, "orgid", "linkdeptid", "createstaffid", "SECRECTLEVELID", "STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds());
		pageinfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblBugCriterionMapper.findList(param,sql));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pageinfo;
	}

	
}
