package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Rui
 * @InterfaceName ExpectLeaveService
 * @Description
 * @DATE 2023/9/14
 */
public interface ExpectLeaveService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String name, String teamLeader, String projectName,String ids) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ExpectLeaveEntity expectLeaveEntity) throws Exception;

    void saveEntity(String token, ExpectLeaveEntity expectLeaveEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void resolveSheet(XSSFSheet sheet, String token) throws Exception;

    void distribute(String ids, String personIds) throws Exception;
}
