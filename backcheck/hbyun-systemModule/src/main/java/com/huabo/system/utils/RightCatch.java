package com.huabo.system.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.service.impl.SysConfig;

public class RightCatch {

    private static String url = ResourceBundle.getBundle("setting/process").getString("url").toString();
    private static String driverClassName = ResourceBundle.getBundle("setting/process").getString("driverClassName").toString();
    private static String username = ResourceBundle.getBundle("setting/process").getString("username").toString();
    private static String password = ResourceBundle.getBundle("setting/process").getString("password").toString();

//    @Value("${jdbc:oracle:thin:@192.0.2.200:1521:orcl}")
//    private static String url;
//    @Value("${spring.application.datasource.oracle.driverClassName}")
//    private static String driverClassName;
//    @Value("${spring.application.datasource.oracle.username}")
//    private static String username;
//    @Value("${spring.application.datasource.oracle.password}")
//    private static String password;

    public RightCatch() {
    }

    public static Connection getCon() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        return con;
    }

    public static void close(Connection con, Statement s, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (s != null) {
                s.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static String getRightForUser(String userId) {
        String rightJson = null;
        String sql = "select t1.rightid,t1.rightname,t1.righturl,t1.memo,t1.leaf,t1.fatherrightid ,t1.rightcode,t1.funcorder,t1.custompage,t1.indicatorstatus,t1.rightdesc, t1.rightisbz,t1.rightcontent,t1.cssClass from TBL_MANAGE_RIGHT t1 left join  TBL_MANAGE_USER_RIGHT t2 on T1.RIGHTID=T2.RIGHTID where t2.STAFFID =" + userId + " and t1.indicatorstatus = " + 1;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;

        try {
            con = getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ArrayList list = new ArrayList();

            while (rs.next()) {
                TblManageRight trs = new TblManageRight();
                trs.setRightid(rs.getBigDecimal("rightid"));
                trs.setRightname(rs.getString("rightname"));
                trs.setRighturl(rs.getString("righturl"));
                trs.setFatherrightid(rs.getBigDecimal("fatherrightid"));
                trs.setFuncorder(new BigDecimal(rs.getInt("funcorder")));
                trs.setIndicatorstatus(String.valueOf(rs.getInt("indicatorstatus")));
                list.add(trs);
            }

            rightJson = JSON.toJSONString(list);
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            close(con, ps, rs);
        }

        return rightJson;
    }

    public static void delRightFororg(String orgid) {
        String sql = "delete from TBL_ORG_RIGHT_NEW where RIGHTNAME is NULL AND ORGID=" + orgid;
        Connection con = null;

        try {
            con = getCon();
            Statement stmtAccess = con.createStatement();
            stmtAccess.executeUpdate(sql);
            stmtAccess.close();
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException var11) {
                var11.printStackTrace();
            }

        }

    }

    public static void delRightFororg(String orgid, String rightid) {
        String sql = "delete from TBL_ORG_RIGHT_NEW where  RIGHTID in (  SELECT DISTINCT RIGHTID from TBL_ORG_RIGHT where RIGHTID in (" + rightid + ") ) AND ORGID=" + orgid;
        Connection con = null;

        try {
            con = getCon();
            Statement stmtAccess = con.createStatement();
            stmtAccess.executeUpdate(sql);
            stmtAccess.close();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

    }

    public static void delQx(String modelid, String userid) {
        String sql = "delete from TBL_HOME_PAGE_JURISDICTION WHERE MODELID=" + modelid + " and STAFFID=" + userid;
        Connection con = null;

        try {
            con = getCon();
            Statement stmtAccess = con.createStatement();
            stmtAccess.executeUpdate(sql);
            stmtAccess.close();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

    }

    public static void insertQx(String modelid, String userid) {
        String sql = "insert into TBL_HOME_PAGE_JURISDICTION(MODELID,STAFFID) VALUES( " + modelid + "," + userid + ")";
        Connection con = null;

        try {
            con = getCon();
            Statement stmtAccess = con.createStatement();
            stmtAccess.executeUpdate(sql);
            stmtAccess.close();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

    }

    public static void insertRightForUser(String rgihtid, String orgid) {
        String sql = "insert into TBL_ORG_RIGHT_NEW(ORGID,RIGHTID,INDICATORSTATUS)  SELECT DISTINCT " + orgid + ",RIGHTID,1 from TBL_MANAGE_RIGHT where RIGHTID in (" + rgihtid + ")";
        Connection con = null;

        try {
            con = getCon();
            Statement stmtAccess = con.createStatement();
            stmtAccess.executeUpdate(sql);
            stmtAccess.close();
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

    }

    public static List<TblStaff> userinfolist() {
        List<TblStaff> list = new ArrayList();
        String userInfosql = "SELECT f.orgid,f.username FROM TBL_STAFF f LEFT JOIN TBL_ORGANIZATION o on o.ORGID=f.ORGID where f.STATUS !=0 and o.STATUS =0";
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            con = getCon();
            ps = con.prepareStatement(userInfosql);
            rs = ps.executeQuery();
            ResultSetMetaData rt = rs.getMetaData();
            new HashMap();

            while (rs.next()) {
                TblStaff trs = new TblStaff();
                TblOrganization org = new TblOrganization();
                org.setOrgid(rs.getBigDecimal("orgid"));
                //trs.setTblOrganization(org);
                trs.setUsername(rs.getString("username"));
                list.add(trs);
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            close(con, ps, rs);
        }

        return list;
    }

    public static String getOrglist(String orgid) {
        String sql = null;
        String orgids = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        if ("MySql".equals(SysConfig.get("databaseType"))) {
           // Object result = excuteFunReturnUniqueCatch("select getParentOrgList(" + orgid + ")");
            //sql = "select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS from TBL_ORGANIZATION o where FIND_IN_SET(o.ORGID,'" + result + "')";
        } else {
            sql = "select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS from TBL_ORGANIZATION o where o.ORGTYPE != 0 START WITH o.ORGID = " + orgid + " CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc";
        }

        try {
            con = getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rt = rs.getMetaData();
            ArrayList temp = new ArrayList();

            while (rs.next()) {
                TblOrganization po = new TblOrganization();
                po.setOrgid(rs.getBigDecimal("orgid"));
                po.setOrgname(rs.getString("orgname"));
                temp.add(po);
            }

            if (temp.size() > 0) {
                orgid = ((TblOrganization) temp.get(0)).getOrgid().toString();
            }
        } catch (SQLException var12) {
            var12.printStackTrace();
        } finally {
            close(con, ps, rs);
        }

        return orgid;
    }

//    public static String findOrgByAllGSJT(String orgid) {
//        ResultSet rs = null;
//        PreparedStatement ps = null;
//        Connection con = null;
//        String sql = "select orgid,orgname,fatherorgid,orgtype,status from tbl_organization  WHERE ORGTYPE<100 and ORGTYPE != 0 and FATHERORGID=" + orgid + " and  status = 0 ORDER BY orderid ASC";
//        ArrayList organizations = new ArrayList();
//
//        try {
//            con = getCon();
//            ps = con.prepareStatement(sql);
//            rs = ps.executeQuery();
//            ResultSetMetaData var6 = rs.getMetaData();
//
//            while (rs.next()) {
//                TblOrganization po = new TblOrganization();
//                po.setOrgid(rs.getBigDecimal("orgid"));
//                po.setOrgname(rs.getString("orgname"));
//                po.setFatherorgid(rs.getBigDecimal("fatherorgid"));
//                po.setOrgtype(rs.getInt("orgtype"));
//                po.setStatus(rs.getInt("status"));
//                organizations.add(po);
//            }
//        } catch (SQLException var42) {
//            var42.printStackTrace();
//        } finally {
//            close((Connection) null, ps, rs);
//        }
//
//        String sqlpo = "select orgid,orgname,fatherorgid,orgtype,status from TBL_ORGANIZATION o where o.ORGID=" + orgid;
//        List<TblOrganization> temp1 = new ArrayList();
//        TblOrganization organization = null;
//
//        try {
//            ps = con.prepareStatement(sqlpo);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                TblOrganization po = new TblOrganization();
//                po.setOrgid(rs.getBigDecimal("orgid"));
//                po.setOrgname(rs.getString("orgname"));
//                po.setFatherorgid(rs.getBigDecimal("fatherorgid"));
//                po.setOrgtype(rs.getInt("orgtype"));
//                po.setStatus(rs.getInt("status"));
//                temp1.add(po);
//            }
//
//            organization = (TblOrganization) temp1.get(0);
//        } catch (SQLException var40) {
//            var40.printStackTrace();
//        } finally {
//            close((Connection) null, ps, rs);
//        }

//        String sqllist = "select orgid,orgname,fatherorgid,orgtype,status from tbl_organization  WHERE ORGTYPE<100 and ORGTYPE != 0 and FATHERORGID=" + orgid + " and  status = 0 ORDER BY orderid ASC";
//        ArrayList list = new ArrayList();
//
//        try {
//            ps = con.prepareStatement(sqllist);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                TblOrganization po = new TblOrganization();
//                po.setOrgid(rs.getBigDecimal("orgid"));
//                po.setOrgname(rs.getString("orgname"));
//                po.setFatherorgid(rs.getBigDecimal("fatherorgid"));
//                po.setOrgtype(rs.getInt("orgtype"));
//                po.setStatus(rs.getInt("status"));
//                list.add(po);
//            }
//        } catch (SQLException var38) {
//            var38.printStackTrace();
//        } finally {
//            close(con, ps, rs);
//        }

//        list.add(organization);
//        walkDepartmentListGS(organizations, list);
//        Map nodeList = new LinkedHashMap();
//        Node root = null;
//        Integer str = null;
//        String pc = ",";
//
//        Node node;
//        for(int i = 0; i < list.size(); ++i) {
//            TblOrganization tbl = (TblOrganization)list.get(i);
//            if (tbl.getStatus() == null || tbl.getStatus() != 1) {
//                if (i > 0 && tbl.getOrgtype() != null && tbl.getOrgtype() == 0 && !tbl.getOrgid().toString().equals(orgid)) {
//                    pc = pc + tbl.getOrgid().toString() + ",";
//                } else if (pc.contains("," + tbl.getFatherorgid().toString() + ",")) {
//                    pc = pc + tbl.getOrgid().toString() + ",";
//                } else {
//                    node = new Node();
//                    node.id = tbl.getOrgid().toString();
//                    node.text = tbl.getOrgname().toString();
//                    node.parentId = tbl.getFatherorgid().toString();
//
//                    for(int k = 0; k < list.size(); ++k) {
//                        TblOrganization copyMap = (TblOrganization)list.get(k);
//                        if (copyMap.getStatus() != null && copyMap.getStatus() != 1 && copyMap.getFatherorgid() != null) {
//                            if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() == 0) {
//                                node.state = "closed";
//                                break;
//                            }
//
//                            if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
//                                node.state = "closed";
//                                break;
//                            }
//                        }
//                    }
//
//                    if (tbl.getFatherorgid().toString().equals("-1") || tbl.getFatherorgid().toString().equals("1")) {
//                        node.state = "open";
//                    }
//
//                    nodeList.put(node.id, node);
//                }
//            }
//        }
//
//        Set entrySet = nodeList.entrySet();
//        Iterator it = entrySet.iterator();
//
//        while(true) {
//            while(it.hasNext()) {
//                node = (Node)((Map.Entry)it.next()).getValue();
//                if (node.parentId != null && !node.parentId.equals("") && !node.parentId.equals("0") && nodeList.get(node.parentId) != null) {
//                    ((Node)nodeList.get(node.parentId)).addChild(node);
//                } else {
//                    root = node;
//                }
//            }
//
//            logger.info("组织切换返回值为：" + root.toString());
//            return root.toString();
//        }
//    }

//    private static void walkDepartmentListGS(Collection<TblOrganization> toList, List<TblOrganization> list) {
//        Iterator var2 = toList.iterator();
//
//        while(var2.hasNext()) {
//            TblOrganization top = (TblOrganization)var2.next();
//            if (top.getOrgtype() != null && top.getOrgtype() != 0 && top.getStatus() != null && top.getStatus() == 0) {
//                TblOrganization copy = new TblOrganization();
//                copy.setOrgid(top.getOrgid());
//                copy.setOrgname(top.getOrgname());
//                copy.setFatherorgid(top.getFatherorgid());
//                copy.setOrgtype(top.getOrgtype());
//                copy.setStatus(top.getStatus());
//                list.add(copy);
//                walkDepartmentListGS(top.getChildren(), list);
//            }
//        }
//
//    }
//
//    public static Object excuteFunReturnUniqueCatch(String sql) {
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Object result = null;
//
//        try {
//            con = getCon();
//            ps = con.prepareStatement(sql);
//
//            for(rs = ps.executeQuery(); rs.next(); result = rs.getObject(0)) {
//            }
//        } catch (SQLException var9) {
//            var9.printStackTrace();
//        } finally {
//            close(con, ps, rs);
//        }
//
//        return result;
//    }
//
//    static {
//        try {
//            Class.forName(driverClassName);
//        } catch (ClassNotFoundException var1) {
//            var1.printStackTrace();
//        }
//
//    }

}
