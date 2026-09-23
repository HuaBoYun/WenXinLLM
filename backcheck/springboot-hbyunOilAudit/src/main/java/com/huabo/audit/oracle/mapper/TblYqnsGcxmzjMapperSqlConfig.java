package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMZJ(工程项目造价表)】的数据库操作Mapper
 * @createDate 2023-09-07 16:46:40
 * @Entity TblYqnsGcxmzj
 */
public class TblYqnsGcxmzjMapperSqlConfig {

    public String selectCountByPageInfo(PageInfo<TblYqnsGcxmzj> pageInfo, TblYqnsGcxmzj vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_GCXMZJ TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsGcxmzj> pageInfo, TblYqnsGcxmzj vo) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_GCXMZJ TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);

        sb.append(" ORDER BY TBL1.gcxmzjid DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsGcxmzj vo, StringBuffer sb) throws Exception {
        if (vo.getOrgid() != null) {
            sb.append(" and orgid = ").append(vo.getOrgid());
        }

        if (StringUtils.isNotEmpty(vo.getHtbh())) {
            sb.append(" AND TBL1.htbh LIKE '%" + vo.getHtbh() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getGcmc())) {
            sb.append(" AND TBL1.gcmc LIKE '%" + vo.getGcmc() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getJsdw())) {
            sb.append(" AND TBL1.jsdw LIKE '%" + vo.getJsdw() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getSgdw())) {
            sb.append(" AND TBL1.sgdw LIKE '%" + vo.getSgdw() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getLxr())) {
            sb.append(" AND TBL1.lxr LIKE '%" + vo.getLxr() + "%'");
        }
        if (StringUtils.isNotEmpty(vo.getLxdh())) {
            sb.append(" AND TBL1.lxdh LIKE '%" + vo.getLxdh() + "%'");
        }

        if (vo.getStartDate() != null && vo.getStartDate().length() > 0) {
            sb.append(" AND TBL1.cjsj >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getStartDate()));
        }

        if (vo.getEndDate() != null && vo.getEndDate().length() > 0) {
            sb.append(" AND TBL1.cjsj <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getEndDate()));
        }
    }
    public String insetEntity(TblYqnsGcxmzj entity) throws Exception{
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_GCXMZJ (GCXMZJID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (").append(RandomUtil.uuBigDecimalId());

        if(StringUtil.isNotEmpty(entity.getHtbh())){
            colSb.append(", HTBH");
            valSb.append(", '"+ entity.getHtbh()+"'");
        }
        
        if(entity.getCjsj() != null) {
        	 colSb.append(", CJSJ");
             valSb.append(", ").append(DataBaseSqlConfig.getDateStrFormat(entity.getCjsj()));
        }
        
        if(StringUtil.isNotEmpty(entity.getGcmc())){
            colSb.append(", GCMC");
            valSb.append(", '"+ entity.getGcmc()+"'");
        }

        if(entity.getEsscje() != null){
            colSb.append(", ESSCJE");
            valSb.append(", '"+ entity.getEsscje()+"'");
        }
        
        if(StringUtil.isNotEmpty(entity.getJsdw())){
            colSb.append(", JSDW");
            valSb.append(", '"+ entity.getJsdw()+"'");
        }
        
        if(StringUtil.isNotEmpty(entity.getGcid())){
            colSb.append(", GCID");
            valSb.append(", '"+ entity.getGcid()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getSgdw())){
            colSb.append(", SGDW");
            valSb.append(", '"+ entity.getSgdw()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getLxr())){
            colSb.append(", LXR");
            valSb.append(", '"+ entity.getLxr()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getLxdh())){
            colSb.append(", LXDH");
            valSb.append(", '"+ entity.getLxdh()+"'");
        }


        colSb.append(", CJR)");
        valSb.append(", '" + entity.getCjr() + "')");

        colSb.append(valSb);
        return colSb.toString();
    }
}




