package com.huabo.monitor.service.impl;

import com.huabo.monitor.config.DateBaseConfig;
import com.huabo.monitor.entity.TblBug;
import com.huabo.monitor.mapper.TblBugMapper;
import com.huabo.monitor.service.ITblBugService;
import com.huabo.monitor.util.ConstClass;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
public class TblBugServiceImpl extends ServiceImpl<TblBugMapper, TblBug> implements ITblBugService {
	
	@Resource
    TblBugMapper tblBugMapper;

	@Override
	public List<TblBug> findByCriterionId(BigDecimal criterionId) {
		String sql = "select * from TBL_BUG TB LEFT JOIN TBL_BUG_CRITERION TBC ON TB.BUGID =TBC.BUGID WHERE TBC.BUGCRIID =" + criterionId;
        List<TblBug> list = tblBugMapper.findBySql(sql);
        return list;
	}
	
	@Override
    public IPage<TblBug> findALLHy(TblBug bug, Integer pageNumber, String startdate, String enddate, String orgid, String orgtype) {
        IPage<TblBug> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql = " SELECT * FROM TBL_BUG bu WHERE 1=1 and INBUGIDB=1 and BUGDEPARTMENT='" + orgid + "' ";
        if (bug != null) {
            if (bug.getBugnumber() != null) {
                sql += " and BUGNUMBER like '%" + bug.getBugnumber() + "%'";
            }
            if (bug.getBugsource() != null) {
                sql += " and BUGSOURCE like '%" + bug.getBugsource() + "%'";
            }
            if (bug.getBugreformstatus() != null) {
                sql += " and BUGREFORMSTATUS like '%" + bug.getBugreformstatus() + "%' ";
            }
            if (bug.getDiscoverperson() != null) {
                sql += " and DISCOVERPERSON like '%" + bug.getDiscoverperson() + "%' ";
            }
        }
        if (startdate != null && !"".equals(startdate)) {
            sql += " and DISCOVERTIME >= TO_DATE('" + startdate + "', 'yyyy-MM-dd') ";
        }
        if (enddate != null && !"".equals(enddate)) {
            sql += " and DISCOVERTIME <= TO_DATE('" + enddate + "', 'yyyy-MM-dd') ";
        }
        sql += "  ORDER BY bu.BUGID desc ";

        return this.tblBugMapper.getSqlPage(page, sql);
    }
	
	@Override
    public TblBug findById(BigDecimal bugid) {
        return this.tblBugMapper.findById(bugid);

    }
	
	@Override
    public TblBug findByCode(String code, String type, String orgid) {
        String sql = "select * from TBL_BUG b where b.BUGNUMBER = '" + code + "' AND b.BUGBYSYSTEM = '" + type + "'";
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            sql += "AND b.BUGDEPARTMENT IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= " + orgid + " AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT " + orgid + "  FROM DUAL)";

        } else {
            Object result = this.tblBugMapper.excuteFunReturnUniqueBug("select getChildrenDeptList(" + orgid + ")");
            sql += "AND ( FIND_IN_SET(b.BUGDEPARTMENT,'" + result + "') OR b.BUGDEPARTMENT = " + orgid + ")";
        }
        IPage<TblBug> page = new Page<>(ConstClass.DEFAULT_SIZE, ConstClass.DEFAULT_SIZE);
        IPage<TblBug> iPage = tblBugMapper.getSqlPage(page, sql);
        long size = iPage.getSize();
        if (size > 0) {
            return iPage.getRecords().get(0);
        }

        return null;
    }
	
	 @Override
	    public void add(TblBug tblBug) {
	        tblBugMapper.insert(tblBug);
	    }
}
