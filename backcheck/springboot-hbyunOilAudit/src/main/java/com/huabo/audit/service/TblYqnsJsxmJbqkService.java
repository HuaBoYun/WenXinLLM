package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JSXM_JBQK(建设项目基本情况表)】的数据库操作Service
 */
public interface TblYqnsJsxmJbqkService extends IService<TblYqnsJsxmJbqk> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJsxmJbqk vo) throws Exception;

    /**
     * 分类验证
     * @param token  token
     * @param vo TblYqnsJsxmJbqk
     * @return JsonBean
     */
    JsonBean flVerify(String token, TblYqnsJsxmJbqk vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsJsxmJbqk vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsJsxmJbqk vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmJbqk vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token) throws Exception;

    /**
     * 通过ids查询 建设项目基本情况表
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<TblYqnsJsxmJbqk> findByIds(String ids)  ;


	JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception;
	
	JsonBean jswclist(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception;
}
