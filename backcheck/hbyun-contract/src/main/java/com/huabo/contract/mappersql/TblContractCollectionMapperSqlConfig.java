package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractCollection;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class TblContractCollectionMapperSqlConfig {
   
	public String selectListByPageInfo(IPage<TblContractCollection> page, String contractname,
			String contractno, BigDecimal pid, TblContractCollection collection) throws Exception {
		StringBuffer sbSql = new StringBuffer("SELECT " +
				"TCC.*,\n" +
				"TCU.CONTRACTNO,\n" +
				"TCU.CONTRACTNAME,\n" +
				"BUD.BUDGETNAME,\n" +
				"TCI.INVOICENO,\n" +
				"TCI.INVOICEMONEY,\n" +
				"TCI.INVOICEDATE,\n" +
				"TCI.INVOICESPDATE,\n" +
				"TCI.INVOICESPORG,\n" +
				"TCI.INVOICEKPORG,\n" +
				"TCI.INVOICETYPE,\n" +
				"TCI.INVOICESTATUS,\n" +
				"TCP.NODECONTENT,\n" +
				"TCP.NODEMONEY,\n" +
				"TCB.BANKACCOUNT,\n" +
				"TOB.BANKACCNUM " +
				" FROM TBL_CONTRACT_COLLECTION TCC " +
                "LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCC.INVOICEID = TCI.INVOICEID " +
                "LEFT JOIN TBL_CYHW_UNIT TCU ON TCC.CONTRACTID = TCU.CONTRACTID " +
                "LEFT JOIN TBL_CYHW_PROJECTBUDGET BUD ON TCU.CONTRACTXDFXINFO = BUD.BUDGETID " +
                "LEFT JOIN TBL_CONTRACT_PLANNODE TCP ON TCC.NODEID = TCP.NODEID " +
                "LEFT JOIN TBL_COUNTERPART_BANKINFO TCB ON TCC.COUNTERBANK = TCB.BANKID " +
                "LEFT JOIN TBL_ORG_BANKACCOUNT TOB ON TCC.ORGBANK = TOB.BANKID " +
                "WHERE LINKORG = "+ pid);
        if (collection.getCreatestaff() != null) {
            sbSql.append(" AND TCC.CREATESTAFF = " + collection.getCreatestaff());
        }
        if (collection.getCollectionorgname() != null && !"".equals(collection.getCollectionorgname())) {
            sbSql.append(" AND TCC.COLLECTIONORGNAME LIKE '%" + collection.getCollectionorgname() + "%'");
        }
        if (collection.getContract() != null) {
            if (contractname != null && !"".equals(contractname)) {
                sbSql.append(" AND TCU.CONTRACTNAME LIKE '%" + contractname+ "%'");
            }

            if (contractno != null && !"".equals(contractno)) {
                sbSql.append( " AND TCU.CONTRACTNO LIKE '%" +contractno+ "%'");
            }
        }
        sbSql.append(" ORDER BY COLLECTIONID DESC ");
        return sbSql.toString();
	}
	
	public String SaveMengerCollectionEntity(TblContractCollection collection) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_COLLECTION (COLLECTIONID");
        StringBuffer value = new StringBuffer(" VALUES ("+collection.getCollectionid());

        if(collection.getInvoiceid() != null) {
            column.append(",INVOICEID");
            value.append(",'"+collection.getInvoiceid()+"'");
        }
        if(collection.getCollectionbank() != null) {
            column.append(",COLLECTIONBANK");
            value.append(",'"+collection.getCollectionbank()+"'");
        }
        if(collection.getCollectionskdate() != null) {
            column.append(",COLLECTIONSKDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getCollectionskdate()));
        }
        if(collection.getCollectionaccount() != null) {
            column.append(",COLLECTIONACCOUNT");
            value.append(",'"+collection.getCollectionaccount()+"'");
        }
        if(collection.getCollectionorgname() != null) {
            column.append(",COLLECTIONORGNAME");
            value.append(",'"+collection.getCollectionorgname()+"'");
        }
        if(collection.getContractid() != null) {
            column.append(",CONTRACTID");
            value.append(",'"+collection.getContractid()+"'");
        }
        if(collection.getNodeid() != null) {
            column.append(",NODEID");
            value.append(",'"+collection.getNodeid()+"'");
        }
        if(collection.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'"+collection.getCreatestaff()+"'");
        }
        if(collection.getCollectionstatus() != null) {
            column.append(",COLLECTIONSTATUS");
            value.append(",'"+collection.getCollectionstatus()+"'");
        }
        if(collection.getLinkorg() != null) {
            column.append(",LINKORG");
            value.append(",'"+collection.getLinkorg()+"'");
        }
        if(collection.getCreatedate() != null ) {
            column.append(",CREATEDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getCreatedate()));
        }
//        if(collection.getLinkorg() != null) {
//            column.append(",LINKORG");
//            value.append(",'"+collection.getLinkorg()+"'");
//        }
//        if(collection.getNodemoney() != null) {
//            column.append(",NODEMONEY");
//            value.append(",'"+collection.getNodemoney()+"'");
//        }
        if(collection.getCounterbank() != null) {
            column.append(",COUNTERBANK");
            value.append(",'"+collection.getCounterbank()+"'");
        }
        if(collection.getOrgbank() != null) {
            column.append(",ORGBANK");
            value.append(",'"+collection.getOrgbank()+"'");
        }

		//预留备注字段
		if(StringUtils.isNotBlank(collection.getReservedstring1())) {
			column.append(",RESERVEDSTRING1");
			value.append(",'"+collection.getReservedstring1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedstring2())) {
			column.append(",RESERVEDSTRING2");
			value.append(",'"+collection.getReservedstring2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedstring3())) {
			column.append(",RESERVEDSTRING3");
			value.append(",'"+collection.getReservedstring3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedstring4())) {
			column.append(",RESERVEDSTRING4");
			value.append(",'"+collection.getReservedstring4()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedstring5())) {
			column.append(",RESERVEDSTRING5");
			value.append(",'"+collection.getReservedstring5()+"'");
		}


		if(StringUtils.isNotBlank(collection.getReserveddropdownsinglechoice1())) {
			column.append(",RESERVEDDROPDOWNSINGLECHOICE1");
			value.append(",'"+collection.getReserveddropdownsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownsinglechoice2())) {
			column.append(",RESERVEDDROPDOWNSINGLECHOICE2");
			value.append(",'"+collection.getReserveddropdownsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownsinglechoice3())) {
			column.append(",RESERVEDDROPDOWNSINGLECHOICE3");
			value.append(",'"+collection.getReserveddropdownsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownsinglechoice4())) {
			column.append(",RESERVEDDROPDOWNSINGLECHOICE4");
			value.append(",'"+collection.getReserveddropdownsinglechoice4()+"'");
		}


		if(StringUtils.isNotBlank(collection.getReserveddropdownmultiple1())) {
			column.append(",RESERVEDDROPDOWNMULTIPLE1");
			value.append(",'"+collection.getReserveddropdownmultiple1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownmultiple2())) {
			column.append(",RESERVEDDROPDOWNMULTIPLE2");
			value.append(",'"+collection.getReserveddropdownmultiple2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownmultiple3())) {
			column.append(",RESERVEDDROPDOWNMULTIPLE3");
			value.append(",'"+collection.getReserveddropdownmultiple3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReserveddropdownmultiple4())) {
			column.append(",RESERVEDDROPDOWNMULTIPLE4");
			value.append(",'"+collection.getReserveddropdownmultiple4()+"'");
		}


		if(StringUtils.isNotBlank(collection.getReservedsinglechoice1())) {
			column.append(",RESERVEDSINGLECHOICE1");
			value.append(",'"+collection.getReservedsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedsinglechoice2())) {
			column.append(",RESERVEDSINGLECHOICE2");
			value.append(",'"+collection.getReservedsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedsinglechoice3())) {
			column.append(",RESERVEDSINGLECHOICE3");
			value.append(",'"+collection.getReservedsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedsinglechoice4())) {
			column.append(",RESERVEDSINGLECHOICE4");
			value.append(",'"+collection.getReservedsinglechoice4()+"'");
		}


		if(StringUtils.isNotBlank(collection.getReservedmultiplechoice1())) {
			column.append(",RESERVEDMULTIPLECHOICE1");
			value.append(",'"+collection.getReservedmultiplechoice1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedmultiplechoice2())) {
			column.append(",RESERVEDMULTIPLECHOICE2");
			value.append(",'"+collection.getReservedmultiplechoice2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedmultiplechoice3())) {
			column.append(",RESERVEDMULTIPLECHOICE3");
			value.append(",'"+collection.getReservedmultiplechoice3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedmultiplechoice4())) {
			column.append(",RESERVEDMULTIPLECHOICE4");
			value.append(",'"+collection.getReservedmultiplechoice4()+"'");
		}


		if(Objects.nonNull(collection.getReservedtime1())) {
			column.append(",RESERVEDTIME1");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedtime1()));
		}
		if(Objects.nonNull(collection.getReservedtime2())) {
			column.append(",RESERVEDTIME2");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedtime2()));
		}
		if(Objects.nonNull(collection.getReservedtime3())) {
			column.append(",RESERVEDTIME3");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedtime3()));
		}
		if(Objects.nonNull(collection.getReservedtime4())) {
			column.append(",RESERVEDTIME4");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedtime4()));
		}


		if(Objects.nonNull(collection.getReservedyeartime1())) {
			column.append(",RESERVEDYEARTIME1");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyeartime1()));
		}
		if(Objects.nonNull(collection.getReservedyeartime2())) {
			column.append(",RESERVEDYEARTIME2");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyeartime2()));
		}
		if(Objects.nonNull(collection.getReservedyeartime3())) {
			column.append(",RESERVEDYEARTIME3");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyeartime3()));
		}
		if(Objects.nonNull(collection.getReservedyeartime4())) {
			column.append(",RESERVEDYEARTIME4");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyeartime4()));
		}


		if(Objects.nonNull(collection.getReservedyearaccuratetime1())) {
			column.append(",RESERVEDYEARACCURATETIME1");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyearaccuratetime1()));
		}
		if(Objects.nonNull(collection.getReservedyearaccuratetime2())) {
			column.append(",RESERVEDYEARACCURATETIME2");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyearaccuratetime2()));
		}
		if(Objects.nonNull(collection.getReservedyearaccuratetime3())) {
			column.append(",RESERVEDYEARACCURATETIME3");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyearaccuratetime3()));
		}
		if(Objects.nonNull(collection.getReservedyearaccuratetime4())) {
			column.append(",RESERVEDYEARACCURATETIME1");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(collection.getReservedyearaccuratetime4()));
		}


		if(StringUtils.isNotBlank(collection.getReservedcontent1())) {
			column.append(",RESERVEDCONTENT1");
			value.append(",'"+collection.getReservedcontent1()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedcontent2())) {
			column.append(",RESERVEDCONTENT2");
			value.append(",'"+collection.getReservedcontent2()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedcontent3())) {
			column.append(",RESERVEDCONTENT3");
			value.append(",'"+collection.getReservedcontent3()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedcontent4())) {
			column.append(",RESERVEDCONTENT4");
			value.append(",'"+collection.getReservedcontent4()+"'");
		}
		if(StringUtils.isNotBlank(collection.getReservedcontent5())) {
			column.append(",RESERVEDCONTENT5");
			value.append(",'"+collection.getReservedcontent5()+"'");
		}

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
