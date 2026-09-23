package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchugMx;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG_MX(计划管理计划初稿明细)】的数据库操作Service
 */
public interface TblYqnsJhglJhchugMxService extends IService<TblYqnsJhglJhchugMx> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJhglJhchugMx vo) throws Exception;

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
    JsonBean saveOrUpdate(String token, TblYqnsJhglJhchugMx vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsJhglJhchugMx vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Transactional
    JsonBean delete(String token, TblYqnsJhglJhchugMx vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsJhglJhchugMx vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;
}
