package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMZJ_ZJB(工程项目造价中间表)】的数据库操作Mapper
 * @createDate 2023-09-07 16:46:40
 * @Entity TblYqnsGcxmzjZjb
 */
public class TblYqnsGcxmzjZjbMapperSqlConfig {

    public String selectListByjhzgGlRela(BigDecimal id, BigDecimal relaId) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        if (id != null) {
            sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " )";
        } else {
            sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCHUGGL_RELA WHERE GLID = " + relaId + " )";
        }

        return sql;
    }

    public String selectListByjhchugGlRela(BigDecimal id, BigDecimal relaId) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        if (id != null) {
            sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " )";
        } else {
            sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + relaId + " )";
        }

        return sql;
    }

    public String selectListByjhcgGlRela(BigDecimal id, String jsdw) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE "
        		+ "  (TBL1.NWB != '工程建设公司'    and  TBL1.NWB != '工程建设'    ) ";

        if (org.apache.commons.lang.StringUtils.isNotBlank(jsdw)) {
            sql += "   AND TBL2.XMSTATUS=0  AND TBL1.GCXMZJZJBID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '31')) AND TBL2.JSDW ='" + jsdw + "'";
        } else {
            sql += " and  TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " )";
        }

        return sql;
    }


    public String selectCountByPageInfo(PageInfo<TblYqnsGcxmzjZjb> pageInfo, TblYqnsGcxmzjZjb vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID "
                + " WHERE 1=1 and TBL1.PARENTID is null ");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsGcxmzjZjb> pageInfo, TblYqnsGcxmzjZjb vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh,TBL2.esscjewy "
                + "FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID "
                + "WHERE 1=1 and TBL1.PARENTID is null  ");

        sqlQuery(vo, sb);
        sb.append(" ORDER BY ");

        if (vo.getOrder() == 0) {
            sb.append(" TBL1.GCXMZJJZBNO ASC ");
        } else {
            sb.append(" TBL1.GCXMZJJZBNO DESC ");
        }

        sb.append(" ) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());

        return sb.toString();
    }

    public String selectListByExport(TblYqnsGcxmzjZjb vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh "
                + "FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID "
                + "WHERE 1=1 and TBL1.PARENTID is null ");

        sqlQuery(vo, sb);
        sb.append(" ORDER BY ");

        if (vo.getOrder() != null && vo.getOrder() == 0) {
            sb.append(" TBL1.NO ASC ");
        } else {
            sb.append(" TBL1.NO DESC ");
        }
        return sb.toString();
    }

    private void sqlQuery(TblYqnsGcxmzjZjb vo, StringBuffer sb) {

        if (vo.getGcxmzjZjbNo() != null) {
            sb.append(" AND TBL1.GCXMZJJZBNO LIKE '%").append(vo.getGcxmzjZjbNo()).append("%'");
        }
        if (vo.getIds() != null && !vo.getIds().isEmpty()) {
            sb.append(" AND TBL1.gcxmzjzjbid IN (");
            // 使用循环拼接 ids
            for (int i = 0; i < vo.getIds().size(); i++) {
                sb.append(vo.getIds().get(i));
                if (i < vo.getIds().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }


        if (vo.getOrgid() != null) {
            sb.append(" and TBL2.orgid = ").append(vo.getOrgid());
        }
        TblYqnsGcxmzj dto = vo.getTblYqnsGcxmzj();
        if (dto != null && StringUtils.isNotEmpty(dto.getHtbh())) {
            sb.append(" AND TBL2.htbh LIKE '%" + dto.getHtbh() + "%'");
        }

        if (dto != null && StringUtils.isNotEmpty(dto.getGcmc())) {
            sb.append(" AND TBL2.gcmc LIKE '%" + dto.getGcmc() + "%'");
        }

        if (dto != null && StringUtils.isNotEmpty(vo.getJsdw())) {
            sb.append(" AND TBL2.jsdw LIKE '%" + vo.getJsdw() + "%'");
        }
        if (dto != null && StringUtils.isNotEmpty(dto.getSgdw())) {
            sb.append(" AND TBL2.sgdw LIKE '%" + dto.getSgdw() + "%'");
        }
        if (dto != null && StringUtils.isNotEmpty(dto.getLxr())) {
            sb.append(" AND TBL2.lxr LIKE '%" + dto.getLxr() + "%'");
        }
        if (dto != null && StringUtils.isNotEmpty(dto.getLxdh())) {
            sb.append(" AND TBL2.lxdh LIKE '%" + dto.getLxdh() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getStartDate() + " 00:00:00','yyyy-mm-dd HH24:MI:SS')");
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getEndDate() + " 23:59:59','yyyy-mm-dd HH24:MI:SS')");
        }
        if (StringUtils.isNotEmpty(vo.getCreateYear())) {
            sb.append(" AND TBL1.cjsj >= TO_DATE('" + vo.getCreateYear() + "-01-01','yyyy-mm-dd')");
            sb.append(" AND TBL1.cjsj <= TO_DATE('" + vo.getCreateYear() + "-12-31','yyyy-mm-dd')");
        }

        if (StringUtils.isNotBlank(vo.getDeips())) {
            sb.append(" and ( TBL1.EXT1=" + vo.getExt1() + "  or TBL1.EXT1 in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + vo.getDeips() + ") ) )");
        } else {
            if (StringUtils.isNotBlank(vo.getExt1())) {
                sb.append(" and TBL1.EXT1=" + vo.getExt1());
            }
        }

    }


    public String selectListByRwall(BigDecimal id) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh,TBL2.esscjewy FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " )";
        return sql;
    }


    public String selectListBymyRw(BigDecimal id, BigDecimal staffid) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh,TBL2.esscjewy FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        sql += " TBL1.GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " ) and (TBL1.FZSTATUS!=1 or TBL1.FZSTATUS is NULL) and RWIDS like '%" + staffid + "%'";
        sql += " OR (TBL1.PARENTID IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = " + id + " ) and (TBL1.FZSTATUS!=1 or TBL1.FZSTATUS is NULL)  and RWIDS like '%" + staffid + "%')";
        sql +=" ORDER BY TBL1.GCXMZJZJBID DESC";
        return sql;
    } 
    
    
    
    public String getByRwid(String id) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh,TBL2.esscjewy FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        sql += " TBL1.GCXMZJZJBID  = " + id ;
        return sql;
    }

    
    
    public String selectListByRwallcf(BigDecimal id) throws Exception {
        String sql = "SELECT TBL1.*,TBL2.htbh,TBL2.gcmc,TBL2.jsdw,TBL2.esscje,TBL2.sgdw,TBL2.lxr,TBL2.lxdh,TBL2.esscjewy FROM TBL_YQNS_GCXMZJ_ZJB TBL1 LEFT JOIN TBL_YQNS_GCXMZJ TBL2 ON TBL1.GCXMZJID = TBL2.GCXMZJID WHERE ";

        sql += " TBL1.PARENTID = " + id ;
        return sql;
    }
    
    
}




