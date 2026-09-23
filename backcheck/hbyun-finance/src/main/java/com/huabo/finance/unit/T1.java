package com.huabo.finance.unit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.database.DataBaseSqlGetStr;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;

public class T1 {

	public static void main(String[] args) {
		String sql = "SELECT PK_ACCASS AS PK_ACCASS , ASSNAME AS ASSNAME FROM (SELECT A.PK_ACCASS AS PK_ACCASS , B.ASSNAME AS ASSNAME FROM GL_DETAIL A LEFT JOIN ASSBALANCE B ON B.ID = A.FID WHERE A.YEARV = 2021 ) T1";
		try {
			
			
			String whereClause = extractWhereClause(sql);
			List<String> electList = extractSelectItems(sql);
			System.out.println(whereClause);
			System.out.println(String.join(",", electList));
			
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(sql);
	}

	
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



