package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.compliance.entity.TblAssesstemple;
import com.huabo.compliance.mapper.TblAssesscategoryMapper;
import com.huabo.compliance.mapper.TblAssesstempleMapper;
import com.huabo.compliance.service.ITblAssesstempleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
public class TblAssesstempleServiceImpl extends ServiceImpl<TblAssesstempleMapper, TblAssesstemple> implements ITblAssesstempleService {
	
	@Resource
    TblAssesstempleMapper tblAssesstempleMapper;
    @Resource
    TblAssesscategoryMapper tblAssesscategoryMapper;

	
	@Override
    public IPage<TblAssesstemple> findAll(String orgid, Integer pageNumber, TblAssesstemple assesstemple,Integer pageSize) {
        IPage<TblAssesstemple> page = new Page<>(pageNumber, pageSize);
        String hql = "SELECT t.*,s.realname as realname,(select wm_concat(orgname) from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorgText,(select wm_concat(orgid) from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorg,(select count(0) from TBL_ASSESS WHERE asstemid=t.ASSTEMID) as numbers FROM Tbl_assesstemple t left join tbl_staff s on s.staffid=t.staffid where t.orgid="+orgid;
        if (StringUtils.isNotBlank(assesstemple.getTemplename())) {
            hql += " and t.templename like '%" + assesstemple.getTemplename() + "%'";
        }
        if (StringUtils.isNotBlank(assesstemple.getTemplenumber())) {
            hql += " and t.templeNumber like '%" + assesstemple.getTemplenumber() + "%'";
        }
        hql += " order by t.asstemid desc";
        return tblAssesstempleMapper.getSqlPage(page, hql);
    }
	
	@Override
    public List<TblAssesstemple> getTmplByNumber(String number, BigDecimal orgid) {
        return tblAssesstempleMapper.getTmplByNumber(number, orgid);
    }
	
	@Override
    public Serializable add(TblAssesstemple tblAssesstemple) {
        int insert = tblAssesstempleMapper.insert(tblAssesstemple);
        return insert;
    }
	
	@Override
    public void modify(TblAssesstemple tblAssesstemple) {
        tblAssesstempleMapper.updateById(tblAssesstemple);
    }
	
	@Override
    public TblAssesstemple findByid(BigDecimal id) {
        return tblAssesstempleMapper.findById(id);
    }
	
	@Override
    public void delete(TblAssesstemple tblAssesstemple) {
        tblAssesstempleMapper.deleteTempleOrg(tblAssesstemple.getAsstemid());
        tblAssesscategoryMapper.deleteByTempleId(tblAssesstemple.getAsstemid());
        tblAssesstempleMapper.deleteById(tblAssesstemple);
    }

	@Override
	public void saveTempleOrg(BigDecimal orgid, BigDecimal tempid) {
		// TODO Auto-generated method stub
		tblAssesstempleMapper.insertTempOrg(orgid,tempid);
	}

	@Override
	public void removeTempleOrg(BigDecimal tmplId) {
		// TODO Auto-generated method stub
		tblAssesstempleMapper.deleteTempOrg(tmplId);

	}

	@Override
	public void insertTemples(TblAssesstemple tblAssesstemple)throws Exception{
		// TODO Auto-generated method stub
		tblAssesstempleMapper.insertTemples(tblAssesstemple);

	}

	@Override
	public void updateTemples(TblAssesstemple tblAssesstemple)throws Exception {
		// TODO Auto-generated method stub
		tblAssesstempleMapper.updateTemples(tblAssesstemple);

	}
}
