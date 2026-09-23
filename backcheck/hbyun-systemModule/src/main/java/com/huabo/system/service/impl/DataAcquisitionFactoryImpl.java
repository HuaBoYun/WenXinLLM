package com.huabo.system.service.impl;

import com.huabo.system.entity.TblOrgExcel;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.service.DataAcquisitionRealization;
import com.huabo.system.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataAcquisitionFactoryImpl implements DataAcquisitionRealization {


    public DataAcquisitionFactoryImpl() {
    }


    public DataAcquisitionRealization dataAcquisitionRealization(TblOrgExcel orgExcel) throws Exception {
        return (DataAcquisitionRealization) Class.forName(orgExcel.getFunctionName()).newInstance();
    }


    @Override
    public void acquisitionData(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization, TblOrgExcel orgExcel, String dataName) throws Exception {

        switch (type) {
            case 1:
                this.importAccountLink(hashMap, year, type, organization, orgExcel, dataName);
                break;
            case 2:
                this.importBkpfBsegLink(hashMap, year, type, organization, orgExcel, dataName);
                break;
            case 3:
                this.importAccBalBsegLink(hashMap, year, type, organization, orgExcel, dataName);
        }

    }

    private void importAccBalBsegLink(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization, TblOrgExcel orgExcel, String dataName) throws Exception {
        String balSql = null;
        List<String> sqlList = new ArrayList(0);
        String delSql = "DELETE FROM " + dataName + ".TBL_ACC_BAL WHERE ORGID = " + organization.getOrgid() + " AND FYEAR = " + year;
        sqlList.add(delSql);
        String dc = null;
        int i = 0;
        String accId = null;
        Iterator var13 = hashMap.entrySet().iterator();

        while (var13.hasNext()) {
            Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry) var13.next();
            Iterator var15 = ((ArrayList) entry.getValue()).iterator();
            while (var15.hasNext()) {
                String[] ele = (String[]) var15.next();
                ++i;
                if (i > orgExcel.getStartRows()) {
                    accId = ele[0].replace("null", "").replace("-", "");
                    if (this.checkAccId(accId)) {
                        dc = "借".equals(ele[5].replace("null", "")) ? "D" : ("贷".equals(ele[5].replace("null", "")) ? "C" : "");
                        balSql = "INSERT INTO " + dataName + ".TBL_ACC_BAL (ACCID,QCYE,DC,FYEAR,ORGID) VALUES ('" + accId + "','" + ele[2].replace("null", "") + "','" + dc + "','" + year + "','" + organization.getOrgid() + "')";
                        sqlList.add(balSql);
                    }
                }
            }
        }

        this.executeSqlBatch(sqlList);


    }

    private void importBkpfBsegLink(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization, TblOrgExcel orgExcel, String dataName) throws Exception {
        int i = 0;
        boolean no = false;
        Calendar cal = Calendar.getInstance();
        String bkpfSql = null;
        String bsegSql = null;
        List<String> sqlList = new ArrayList(0);
        Map<String, Integer> glhMap = new HashMap(0);
        String delSql = "DELETE FROM " + dataName + ".TBL_ACC_BKPF WHERE ORGID = " + organization.getOrgid() + " AND FYEAR = " + year;
        sqlList.add(delSql);
        delSql = "DELETE FROM " + dataName + ".TBL_ACC_BSEG WHERE ORGID = " + organization.getOrgid() + " AND FYEAR = " + year;
        sqlList.add(delSql);
        Date pzDate = null;
        String pzDateStr = null;
        String glh = null;
        Iterator var18 = hashMap.entrySet().iterator();

        while (var18.hasNext()) {
            Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry) var18.next();
            Iterator var20 = ((ArrayList) entry.getValue()).iterator();

            while (var20.hasNext()) {
                String[] ele = (String[]) var20.next();
                ++i;
                if (i > orgExcel.getStartRows()) {
                    pzDateStr = ele[0].replace("null", "");
                    pzDate = DateUtils.parse(pzDateStr, "yyyy-MM-dd");
                    cal.setTime(pzDate);
                    glh = pzDateStr + "-" + (cal.get(2) + 1) + "-" + ele[3].replace("null", "");
                    if (glhMap.containsKey(glh)) {
                        int no1 = (Integer) glhMap.get(glh) + 1;
                        glhMap.put(glh, no1);
                    } else {
                        glhMap.put(glh, 1);
                        no = true;
                        bkpfSql = "INSERT INTO " + dataName + ".TBL_ACC_BKPF (HEADTEXT,PZ_DATE,PZTYPE,GLH,PZH,AYEAR,AMONTH,ORGID,FYEAR) VALUES ('" + ele[6].replace("null", "") + "', TO_DATE('" + pzDateStr + "', 'YYYY-MM-DD'), '" + ele[1].replace("null", "") + "', '" + glh + "', '" + ele[3].replace("null", "") + "','" + year + "', '" + (cal.get(2) + 1) + "','" + organization.getOrgid() + "','" + year + "')";
                        sqlList.add(bkpfSql);
                    }

                    bsegSql = "INSERT INTO " + dataName + ".TBL_ACC_BSEG (GLH,PZH,MD,MC, MD_F,MC_F,LINETEXT,ENTRYID,ACCID,ORGID,FYEAR) VALUES ('" + glh + "', '" + ele[3].replace("null", "") + "','" + ele[7].replace("null", "") + "'," + ele[8].replace("null", "") + ", '0', '0', '" + ele[6].replace("null", "") + "','" + glhMap.get(glh) + "', '" + ele[4].replace("null", "").replace("-", "") + "', '" + organization.getOrgid() + "', '" + year + "')";
                    sqlList.add(bsegSql);
                }
            }
        }

        this.executeSqlBatch(sqlList);

    }

    private void importAccountLink(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization, TblOrgExcel orgExcel, String dataName) throws Exception {
        int i = 0;
        String accountSql = null;
        String accSetSql = null;
        String accId = null;
        String dc = null;
        List<String> sqlList = new ArrayList(0);
        String delSql = "DELETE FROM " + dataName + ".TBL_ACCOUNT WHERE ORGID = " + organization.getOrgid() + " AND FYEAR = " + year;
        sqlList.add(delSql);
        delSql = "DELETE FROM " + dataName + ".TBL_ACCSET WHERE ORGID = " + organization.getOrgid() + " AND FYEAR = " + year;
        sqlList.add(delSql);
        Iterator var14 = hashMap.entrySet().iterator();

        while (var14.hasNext()) {
            Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry) var14.next();
            Iterator var16 = ((ArrayList) entry.getValue()).iterator();

            while (var16.hasNext()) {
                String[] ele = (String[]) var16.next();
                if (i < orgExcel.getStartRows()) {
                    ++i;
                } else {
                    dc = "借".equals(ele[3].replace("null", "")) ? "D" : ("贷".equals(ele[3].replace("null", "")) ? "C" : "");
                    accId = ele[0].replace("null", "");
                    accountSql = "INSERT INTO " + dataName + ".TBL_ACCOUNT ( ACCID, ACCNAME1, ACCNAME2, DC, IGRADE, ORGID, FYEAR,AORGID ) VALUES ('" + accId.replace("-", "") + "','" + ele[1].replace("null", "") + "','" + this.getFaherAccountName(hashMap, accId, ele[1].replace("null", "")) + "','" + dc + "','" + this.findStrCount(ele[0]) + "','" + organization.getOrgid() + "','" + year + "','" + organization.getOrgid() + "')";
                    sqlList.add(accountSql);
                    accSetSql = "INSERT INTO " + dataName + ".TBL_ACCSET ( ACCONE, ACCTWO, ACCTHREE, ACCFOUR, ACCFIVE, ACCSIX, ACCSEVEN, ACCEIGHT, ACCNINE, ACCTEN, ACCSPLIT, ACCID, ORGID,FYEAR )";
                    accSetSql = accSetSql + " VALUES (" + this.calcurAccSet(accId, 1) + "," + this.calcurAccSet(accId, 2) + "," + this.calcurAccSet(accId, 3) + "," + this.calcurAccSet(accId, 4) + "," + this.calcurAccSet(accId, 5) + "," + this.calcurAccSet(accId, 6) + "," + this.calcurAccSet(accId, 7) + "," + this.calcurAccSet(accId, 8) + "," + this.calcurAccSet(accId, 9) + "," + this.calcurAccSet(accId, 10) + ",'-','" + accId.replace("-", "") + "','" + organization.getOrgid() + "','" + year + "')";
                    sqlList.add(accSetSql);
                }
            }
        }

        this.executeSqlBatch(sqlList);
    }

    private Integer calcurAccSet(String accId, Integer index) {
        String[] accIds = accId.split("-");
        Integer count = accIds.length;
        Integer result = 0;
        switch (index) {
            case 1:
                result = 4;
                break;
            case 2:
                if (count < 2) {
                    result = 0;
                } else {
                    result = accIds[1].length();
                }
                break;
            case 3:
                if (count < 3) {
                    result = 0;
                } else {
                    result = accIds[2].length();
                }
                break;
            case 4:
                if (count < 4) {
                    result = 0;
                } else {
                    result = accIds[3].length();
                }
                break;
            case 5:
                if (count < 5) {
                    result = 0;
                } else {
                    result = accIds[4].length();
                }
                break;
            case 6:
                if (count < 6) {
                    result = 0;
                } else {
                    result = accIds[5].length();
                }
                break;
            case 7:
                if (count < 7) {
                    result = 0;
                } else {
                    result = accIds[6].length();
                }
                break;
            case 8:
                if (count < 8) {
                    result = 0;
                } else {
                    result = accIds[7].length();
                }
                break;
            case 9:
                if (count < 9) {
                    result = 0;
                } else {
                    result = accIds[8].length();
                }
                break;
            case 10:
                if (count < 10) {
                    result = 0;
                } else {
                    result = accIds[9].length();
                }
                break;
            default:
                result = 0;
        }

        return result;
    }

    private String getFaherAccountName(HashMap<String, ArrayList<String[]>> hashMap, String accId, String accName) {
        if (accId.length() <= 4) {
            return accName;
        } else {
            Integer lastIndex = accId.lastIndexOf("-");
            String fatherId = accId.substring(0, lastIndex);
            String fatherName = null;
            boolean flag = true;
            Iterator var8 = hashMap.entrySet().iterator();

            while (var8.hasNext()) {
                Map.Entry<String, ArrayList<String[]>> entry = (Map.Entry) var8.next();
                Iterator var10 = ((ArrayList) entry.getValue()).iterator();

                while (var10.hasNext()) {
                    String[] ele = (String[]) var10.next();
                    if (fatherId.equals(ele[0].replace("null", ""))) {
                        fatherName = ele[1].replace("null", "");
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            return fatherName + "_" + accName;
        }
    }

    private Integer findStrCount(String s) {
        String str = s.replace("null", "");
        Integer length = str.length() - str.replace("-", "").length() + 1;
        return length;
    }


    public Integer executeSqlBatch(List<String> sqlList) throws Exception {
//        SessionFactory sessionFactory = (SessionFactory)SpringContextHolder.getBean("sessionFactory");
//        Session session = sessionFactory.getCurrentSession();
//        Transaction tx = session.beginTransaction();
//
//        try {
//            Query query = null;
//            Iterator var6 = sqlList.iterator();
//
//            while(var6.hasNext()) {
//                String sql = (String)var6.next();
//                query = session.createSQLQuery(sql);
//                query.executeUpdate();
//            }
//
//            tx.commit();
//            return 0;
//        } catch (Exception var8) {
//            tx.rollback();
//            throw var8;
//        }
        return 0;
    }

    public boolean checkAccId(String accId) {
        Boolean strResult = accId.matches("-?[0-9]+.?[0-9]*");
        return strResult;
    }


}
