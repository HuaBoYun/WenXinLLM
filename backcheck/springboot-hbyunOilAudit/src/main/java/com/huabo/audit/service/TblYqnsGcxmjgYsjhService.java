package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblYqnsGcxmjgYsjh;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMJG_YSJH(工程项目竣工验收计划)】的数据库操作Service
 */
public interface TblYqnsGcxmjgYsjhService extends IService<TblYqnsGcxmjgYsjh> {

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @param queryYear 
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo, Integer queryYear) throws Exception;

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @param type 
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsGcxmjgYsjh vo, int type) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsGcxmjgYsjh vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsGcxmjgYsjh vo) throws Exception;

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcxmjgYsjh vo) throws Exception;

    /**
     * 导入
     *
     * @param file
     * @param isCover 
     * @return
     * @throws IOException
     */
    JsonBean importData(MultipartFile file, String token, Integer isCover) throws Exception;



    /**
     * 通过ids查询 工程项目竣工验收计划
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<TblYqnsGcxmjgYsjh> findByIds(String ids)  ;

	JsonBean getAutoNo(String token) throws Exception;

	JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo,
			Integer queryYear) throws Exception;
	
	

	JsonBean getxzListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsGcxmjgYsjh vo,
			Integer queryYear) throws Exception;

}
