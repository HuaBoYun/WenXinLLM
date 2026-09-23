package com.huabo.audit.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkhz;

/**
* @author Administrator
* @description 针对表【TBL_YQNS_JSXM_TZWCQK(建设项目投资完成情况)】的数据库操作Service
*/
public interface TblYqnsJsxmTzwcqkService extends IService<TblYqnsJsxmTzwcqk> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqk vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqk vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsJsxmTzwcqk vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsJsxmTzwcqk vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmTzwcqk vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;

    /**
     * 分类验证
     * @param token token
     * @param vo TblYqnsJsxmJbqk
     * @return JsonBean
     */
    JsonBean flVerify(String token, TblYqnsJsxmJbqk vo) throws Exception;
    
    /**
     * 验证金额
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean checkMoney(String token, TblYqnsJsxmTzwcqk vo) throws Exception;
    
}
