package com.hbfk.util.database;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;

public class DataBaseSqlGetStr {

	/**
	 * 获取sql语句 where 条件 
	 * @param sql
	 * @return
	 * @throws JSQLParserException
	 */
	public static String extractWhereClause(String sql) throws JSQLParserException {
        // 解析SQL语句
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                // 获取 WHERE 条件
                if (plainSelect.getWhere() != null) {
                    return plainSelect.getWhere().toString();
                }
            }
        }
        return null; // 如果没有 WHERE 子句，返回 null
    }
	
	/**
	 * 获取sql语句  select 查询字段
	 * @param sql
	 * @return
	 * @throws JSQLParserException
	 */
	public static List<String> extractSelectItems(String sql) throws JSQLParserException {
        // 解析SQL语句
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                List<SelectItem> selectItemList = plainSelect.getSelectItems();
                List<String> cols = new ArrayList<String>(0);
    			for (SelectItem item : selectItemList) {
    				cols.add(item.toString());
    			}
                
                return  cols;
            }
        }
        return null;
    }
	
	/**
	 * 获取sql语句中Order By 的语句
	 * @param sql
	 * @return
	 * @throws JSQLParserException
	 */
	public static List<String> extractOrderByClause(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<OrderByElement> oeList =  plainSelect.getOrderByElements();
            
            List<String> cols = new ArrayList<String>(0);
			for (OrderByElement oe : oeList) {
				cols.add(oe.toString());
			}
            
            return  cols;
        }
        return null;
    }
}
