package com.huabo.financialdata.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DynamicDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);


    private String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl?connectTimeout=1000&socketTimeout=60000";
    private String password = "1";

    /**
     * 用户名
     */
    private String username;

    /**
     * 表的所在域
     */
    private final String owner = "HBGRCTEST";

    private JdbcTemplate jdbcTemplate;

    /**
     * 创建连库信息
     *
     * @param username 用户名
     */
    public DynamicDataSource(String username) throws SQLException {
        if (Objects.isNull(username)) {
            throw new SQLException("没有指定数据库用户名");
        }
        this.username = username;
        System.out.println("用户名为" + this.username);
        DataSource d = new DriverManagerDataSource(this.url, this.username, this.password);
        this.jdbcTemplate = new JdbcTemplate(d);

    }

    public static void main(String[] args) throws SQLException, IOException {
        DynamicDataSource d = new DynamicDataSource("HBFKCWZT2018");
        d.getTbable("select * from TBL_ACC_ASS").forEach(System.out::println);
    }

    /**
     * 判断oracle当中是否存在某张表
     *
     * @param tableName
     * @return
     */
    public boolean existTable(String tableName) {
        logger.info("判断" + tableName + "是否存在!!!");
        if (Objects.isNull(tableName)) {
            logger.info("DynamicDataSource - > existTable->tableName | bookname 为空!! tablename为" + tableName);
            throw new RuntimeException("DynamicDataSource - > existTable->tableName | bookname 为空!! tablename为" + tableName);
        }
        List<Map<String, Object>> list = null;
        try {
            list = this.jdbcTemplate.queryForList("SELECT COUNT(*) num FROM ALL_TABLES WHERE OWNER=UPPER('" + this.username + "') AND TABLE_NAME=UPPER('" + tableName + "') ");
        } catch (Exception e) {
            logger.info("数据连接错误" + e.getMessage());
            return false;
        }
        Integer num = null;
        if (null != list && !list.isEmpty()) {
            String o = list.get(0).get("num").toString();
            num = Integer.valueOf(o);
        }
        return num > 1 ? true : false;
    }

    /**
     * 执行sql
     *
     * @param tbaleName
     * @param params
     */
    public void executeSql(String tbaleName, List<String> params) {
        List<String> sqlList = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        buffer.append("create table " + this.username + "." + tbaleName + " (");
        String sql = "";
        buffer.append("ID NUMBER NOT NULL,");
        buffer.append("EXECTIME  VARCHAR2(500 BYTE),");
        for (int i = 0; i < params.size(); i++) {
            if (i == params.size() - 1) {
                buffer.append("" + params.get(i) + " VARCHAR2(4000 BYTE) )");
            } else {
                buffer.append("" + params.get(i) + " VARCHAR2(4000 BYTE) ,");
            }
        }
        sqlList.add(buffer.toString());
        this.jdbcTemplate.execute(sql.toString());
    }

    /**
     * 删除表
     *
     * @param tableName
     * @throws SQLException
     */
    public void deleteTable(String tableName) throws SQLException {
        if (this.existTable(tableName)) {
            this.jdbcTemplate.execute("drop table " + tableName);
        }
    }

    /**
     * 获取当前表的基本信息
     *
     * @param sql 语句
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public List<String> getTbable(String sql) throws SQLException, IOException {
        Statement stmt = null;
        List<String> typelist = new ArrayList<String>();
        this.jdbcTemplate.query(sql, (rs) -> {
            ResultSetMetaData meta = rs.getMetaData();

            for (int i = 1, columncount = meta.getColumnCount(); i <= columncount; i++) {
                if (!typelist.contains(meta.getColumnName(i))) {
                    typelist.add((meta.getColumnName(i)));
                }
            }
            return typelist;
        });
        return typelist;
    }

    /**
     * 返回表信息以及数据信息
     *
     * @param tableName 表名
     * @param time      时间
     * @return
     * @throws SQLException
     */
    public Map<String, Object> getDataExport(String tableName, String time) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> columeName = new ArrayList<String>();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        String countSql = "select count(*) as count from " + this.username + "." + tableName + " where  EXECTIME = '" + time + "'";
        jdbcTemplate.query(countSql, (rs) -> {
            count.set(rs.getInt("count"));
        });
        String sql1 = "select * from (select row_number() over(order by o.ID asc) as r,o.*  from  " + this.username + "." + tableName + " o  where o.EXECTIME = '" + time + "' " + " ORDER BY o.id) ";
        jdbcTemplate.query(sql1, (res) -> {
            int columncount = res.getMetaData().getColumnCount();
            for (int i = 2; i <= columncount; i++) {
                String string = res.getMetaData().getColumnName(i);
                columeName.add(string);
            }
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            while (res.next()) {
                List<Object> data = new ArrayList<Object>();
                for (String object : columeName) {
                    data.add(res.getObject(object));
                }
                dataList.add(data);
            }
            map.put("data", dataList);
            map.put("column", columeName);
        });
        return map;
    }

    /**
     * 分页的方式拿到数据
     *
     * @param tableName
     * @param time
     * @param currentPage
     * @param pageSize
     * @param findStr
     * @return
     * @throws SQLException
     */
    public Map<String, Object> getData(String tableName, String time, Integer currentPage, Integer pageSize, String findStr) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        PageBean pageBean = new PageBean();
        if (null == currentPage) {
            pageBean.setCurrentPage(1);
        } else {
            pageBean.setCurrentPage(currentPage);
        }
        pageBean.setPageSize(pageSize);
        if (null == currentPage) {
            currentPage = 0;
        } else {
            currentPage = currentPage - 1;
        }
        currentPage = currentPage * pageSize;
        pageSize = currentPage + pageSize;
        try {
            String countSql = "select count(*) as count from " + this.username + "." + tableName + " where  EXECTIME = '" + time + "'" + findStr;
            Map<String, Object> map1 = this.jdbcTemplate.queryForMap(countSql);
            Integer count = (Integer) map1.get("count");
            pageBean.setRecordCount(count);
            String sql1 = "select * from (select row_number() over(order by o.ID asc) as r,o.*  from  " + this.username + "." + tableName + " o  where o.EXECTIME = '" + time + "' " + findStr + " ORDER BY o.id) where r<=" + pageSize + " and r>" + currentPage;
            List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql1);
            pageBean.setRecordList(list);
            map.put("data", pageBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

//    public static JdbcTemplate initJdbcTemplate(String username) {
//        DruidDataSource d = new DruidDataSource();
//        d.setUsername(username);
//        d.setUrl(url);
//        d.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        d.setPassword(password);
//        return new JdbcTemplate(d);
//    }
//
//
//    public static void executeSql(String tbaleName, String bookName, List<String> params, String username) {
//        List<String> sqlList = new ArrayList<String>();
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("create table " + bookName + "." + tbaleName + " (");
//        String sql = "";
//        buffer.append("ID NUMBER NOT NULL,");
//        // buffer.append("STAFFID NUMBER NOT NULL,");
//        buffer.append("EXECTIME  VARCHAR2(500 BYTE),");
//        for (int i = 0; i < params.size(); i++) {
//            if (i == params.size() - 1) {
//                buffer.append("" + params.get(i) + " VARCHAR2(4000 BYTE) )");
//            } else {
//                buffer.append("" + params.get(i) + " VARCHAR2(4000 BYTE) ,");
//            }
//        }
//        sqlList.add(buffer.toString());
//        DynamicDataSource.initJdbcTemplate(username).execute(sql.toString());
//    }

//    public static void deleteTable(String tableName, String bookName) throws SQLException {
//
//        List<Map<String, Object>> list = DynamicDataSource.initJdbcTemplate(bookName).queryForList("select * from information_schema.TABLES where TABLE_NAME = " + tableName);
//        if (!Objects.isNull(list) || list.isEmpty() || list.size() == 0) {
//            DynamicDataSource.initJdbcTemplate(bookName).execute("drop table " + tableName);
//        }
//    }

//
//    public static List<String> getTbable(String sql, String username) throws SQLException, IOException {
//        Statement stmt = null;
//        List<String> typelist = new ArrayList<String>();
//        DynamicDataSource.initJdbcTemplate(username).query(sql, new ResultSetExtractor<List<String>>() {
//            @Override
//            public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
//                ResultSetMetaData meta = rs.getMetaData();
//                int columncount = meta.getColumnCount();
//
//                for (int i = 1; i <= columncount; i++) {
//                    if (typelist.indexOf(meta.getColumnName(i)) == -1) {
//                        typelist.add((meta.getColumnName(i)));
//                    }
//                }
//                return typelist;
//            }
//        });
//        return typelist;
//    }

//
//    public static Map<String, Object> getDataExport(String tableName, String bookName, String time) throws SQLException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        List<String> columeName = new ArrayList<String>();
//        AtomicReference<Integer> count = new AtomicReference<>(0);
//        String countSql = "select count(*) as count from " + bookName + "." + tableName + " where  EXECTIME = '" + time + "'";
//        DynamicDataSource.initJdbcTemplate(bookName).query(countSql, (rs) -> {
//            count.set(rs.getInt("count"));
//        });
//        String sql1 = "select * from (select row_number() over(order by o.ID asc) as r,o.*  from  " + bookName + "." + tableName + " o  where o.EXECTIME = '" + time + "' " + " ORDER BY o.id) ";
//        DynamicDataSource.initJdbcTemplate(bookName).query(sql1, (res) -> {
//            int columncount = res.getMetaData().getColumnCount();
//            for (int i = 2; i <= columncount; i++) {
//                String string = res.getMetaData().getColumnName(i);
//                columeName.add(string);
//            }
//            List<List<Object>> dataList = new ArrayList<List<Object>>();
//            while (res.next()) {
//                List<Object> data = new ArrayList<Object>();
//                for (String object : columeName) {
//                    data.add(res.getObject(object));
//                }
//                dataList.add(data);
//            }
//            map.put("data", dataList);
//            map.put("column", columeName);
//        });
//        return map;
//    }
//
//    public static Map<String, Object> getData(String tableName, String bookName, String time, Integer currentPage, Integer pageSize, String findStr) throws SQLException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        List<String> columeName = new ArrayList<String>();
//
//        PageBean pageBean = new PageBean();
//        if (null == currentPage) {
//            pageBean.setCurrentPage(1);
//        } else {
//            pageBean.setCurrentPage(currentPage);
//        }
//        pageBean.setPageSize(pageSize);
//        if (null == currentPage) {
//            currentPage = 0;
//        } else {
//            currentPage = currentPage - 1;
//        }
//        currentPage = currentPage * pageSize;
//        pageSize = currentPage + pageSize;
//        ResultSet res = null;
//        try {
//            String countSql = "select count(*) as count from " + bookName + "." + tableName + " where  EXECTIME = '" + time + "'" + findStr;
//            Map<String, Object> map1 = DynamicDataSource.initJdbcTemplate(bookName).queryForMap(countSql);
//            Integer count = (Integer) map1.get("count");
//            pageBean.setRecordCount(count);
//            String sql1 = "select * from (select row_number() over(order by o.ID asc) as r,o.*  from  " + bookName + "." + tableName + " o  where o.EXECTIME = '" + time + "' " + findStr + " ORDER BY o.id) where r<=" + pageSize + " and r>" + currentPage;
//            DynamicDataSource.initJdbcTemplate(bookName).query(sql1, new ResultSetExtractor<List<String>>() {
//                @Override
//                public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
//                    ResultSetMetaData meta = rs.getMetaData();
//                    int columncount = meta.getColumnCount();
//
//                    for (int i = 1; i <= columncount; i++) {
//                        columeName.add((meta.getColumnName(i)));
//                    }
//                    return columeName;
//                }
//            });
//
//            List<List<Object>> dataList = new ArrayList<List<Object>>();
//            while (res.next()) {
//                List<Object> data = new ArrayList<Object>();
//                for (String object : columeName) {
//                    data.add(res.getObject(object));
//                }
//                dataList.add(data);
//            }
//            pageBean.setRecordList(dataList);
//            map.put("data", pageBean);
//            map.put("column", columeName);
//            res.close();
//        } finally {
//            if (null != res) {
//                res.close();
//            }
//        }
//        return map;
//    }


}
