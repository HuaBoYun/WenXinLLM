package com.huabo.es.service;

import com.huabo.es.domain.TblAccbook;

import java.util.List;
import java.util.Map;

/**
 * (tbl_accbook)表服务接口
 *
 * @author : caozhibo
 * @date : 2023-3-22
 */
public interface TblAccbookService {

    /**
     * 通过ID查询单条数据
     *
     * @param bookid 主键
     * @return 实例对象
     */
    TblAccbook queryById(String bookid);

    /**
     * 新增数据
     *
     * @param tblAccbook 实例对象
     * @return 实例对象
     */
    TblAccbook insert(TblAccbook tblAccbook);

    /**
     * 更新数据
     *
     * @param tblAccbook 实例对象
     * @return 实例对象
     */
    TblAccbook update(TblAccbook tblAccbook);

    /**
     * 通过主键删除数据
     *
     * @param bookid 主键
     * @return 是否成功
     */
    boolean deleteById(String bookid);

    /**
     * 通过acctid去重
     *
     * @return
     */
    List<String> queryDistinctByAcctid();


    /**
     * 检查表时候存在
     *
     * @param schemaName 表空间/归属者
     * @param tableName  表名
     * @return
     */
    Integer checkTableExists(String schemaName, String tableName);

    List<Map<String, Object>> queryAccAssDatas(String acctid, String accass);
}