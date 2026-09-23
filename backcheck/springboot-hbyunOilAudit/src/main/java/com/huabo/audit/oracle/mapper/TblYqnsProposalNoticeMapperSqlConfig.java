package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wystan
 * @description 针对表【TBL_YQNS_FGLDHZ(分管领导汇总表)】的数据库操作Mapper
 * @Entity TblYqnsFgldhz
 */
public class TblYqnsProposalNoticeMapperSqlConfig {


    public String selectCountByPageInfo(PageInfo<TblYqnsProposalNoticeEntity> pageInfo, TblYqnsProposalNoticeEntity vo) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
                + " FROM TBL_YQNS_PROPOSAL_NOTICE TBL1 "
                + " WHERE 1=1");

        sqlQuery(vo, sb);

        return sb.toString();
    }


    public String selectListByPageInfo(PageInfo<TblYqnsProposalNoticeEntity> pageInfo, TblYqnsProposalNoticeEntity vo) {
        StringBuffer sb = new StringBuffer("SELECT * FROM "
                + "(SELECT T1.*,ROWNUM RN  FROM "
                + "(SELECT TBL1.* "
                + "FROM TBL_YQNS_PROPOSAL_NOTICE TBL1 "
                + "WHERE 1=1");

        sqlQuery(vo, sb);
        sb.append(" ORDER BY TBL1.ID DESC) T1 WHERE ROWNUM <= " + (pageInfo.getCurrentRecord() + pageInfo.getPageSize()) + " ) T2 WHERE T2.RN > " + pageInfo.getCurrentRecord());
        return sb.toString();
    }

    private void sqlQuery(TblYqnsProposalNoticeEntity vo, StringBuffer sb) {

        if (StringUtils.isNotEmpty(vo.getDocument())) {
            sb.append(" AND TBL1.DOCUMENT LIKE '%" + vo.getDocument() + "%'");
        }

        if (StringUtils.isNotEmpty(vo.getTitle())) {
            sb.append(" AND TBL1.TITLE LIKE '%" + vo.getTitle() + "%'");
        }

        if (!"5555".equals(vo.getCreateUser())) {
            sb.append(" AND ( TBL1.ID  IN (SELECT NOTICEID FROM TBL_YQNS_PROPOSAL_NOTICE_XF WHERE USERID='" + vo.getCreateUser()+"') or TBL1.CREATEUSER = '" +vo.getCreateUser()+"') ");
        }


    }

}




