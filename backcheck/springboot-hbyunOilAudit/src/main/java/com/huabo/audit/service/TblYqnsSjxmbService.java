package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjxmb;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_SJXMB(审计项目表)】的数据库操作Service
 */
public interface TblYqnsSjxmbService extends IService<TblYqnsSjxmb> {
    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjxmb vo) throws Exception;

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    JsonBean saveOrUpdate(String token, TblYqnsSjxmb vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjxmb vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Transactional
    JsonBean delete(String token, TblYqnsSjxmb vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsSjxmb vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;

    /**
     * 下发
     * @param token
     * @param vo
     * @return
     */
    JsonBean xf(String token, TblYqnsSjxmb vo) throws Exception;
    
    JsonBean getLiftMenu(String token, String projectid) throws Exception;
    
    JsonBean projectArchiveList(String token) throws Exception;
    
    JsonBean daList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception;
    
    JsonBean dajyList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception;
    
    JsonBean dajySaveOrUpdate(String token, TblNbsjBorrowRecordEntity vo) throws Exception;
    
    JsonBean dajyDetail(String token, Integer borrowId) throws Exception;
    
    JsonBean jyrzList(String token, Integer pageNumber, Integer pageSize, String projectName, String qdcode) throws Exception;
    
    JsonBean jyrzxqList(String token, String id) throws Exception;
    
}
