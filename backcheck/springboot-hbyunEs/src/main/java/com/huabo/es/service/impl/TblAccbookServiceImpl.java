package com.huabo.es.service.impl;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.huabo.es.domain.TblAccbook;
import com.huabo.es.mapper.TblAccbookMapper;
import com.huabo.es.service.TblAccbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (tbl_accbook)表服务实现类
 *
 * @author : caozhibo
 * @date : 2023-3-22
 */
@Service
public class TblAccbookServiceImpl implements TblAccbookService {
    @Autowired
    private TblAccbookMapper tblAccbookMapper;
    @Value("${sqltype}")
    private String dbName;

    /**
     * 通过ID查询单条数据
     *
     * @param bookid 主键
     * @return 实例对象
     */
    public TblAccbook queryById(String bookid) {
        return tblAccbookMapper.selectById(bookid);
    }

    /**
     * 新增数据
     *
     * @param tblAccbook 实例对象
     * @return 实例对象
     */
    public TblAccbook insert(TblAccbook tblAccbook) {
        tblAccbookMapper.insert(tblAccbook);
        return tblAccbook;
    }

    /**
     * 更新数据
     *
     * @param tblAccbook 实例对象
     * @return 实例对象
     */
    public TblAccbook update(TblAccbook tblAccbook) {
        //1. 根据条件动态更新
        LambdaUpdateChainWrapper<TblAccbook> chainWrapper = new LambdaUpdateChainWrapper<TblAccbook>(tblAccbookMapper);
        //2. 设置主键，并更新
        chainWrapper.set(TblAccbook::getBookid, tblAccbook.getBookid());
        boolean ret = chainWrapper.update();
        //3. 更新成功了，查询最最对象返回
        if (ret) {
            return queryById(tblAccbook.getBookid());
        } else {
            return tblAccbook;
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param bookid 主键
     * @return 是否成功
     */
    public boolean deleteById(String bookid) {
        int total = tblAccbookMapper.deleteById(bookid);
        return total > 0;
    }

    /**
     * 通过acctid去重
     *
     * @return
     */
    @Override
    public List<String> queryDistinctByAcctid() {
        return tblAccbookMapper.queryDistinctByAcctid();
    }

    @Override
    public Integer checkTableExists(String schemaName, String tableName) {
        if (dbName.equals("mysql")) {
            return tblAccbookMapper.checkMysqlTableExists(schemaName, tableName);
        } else if (dbName.equals("oracle")) {
            return tblAccbookMapper.checkOracleTableExists(schemaName, tableName);
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> queryAccAssDatas(String acctid, String accass) {
        return tblAccbookMapper.queryAccAssDatas(acctid, accass);
    }
}