package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author Rui
 * @InterfaceName ProjectSuggestionService
 * @Description
 * @DATE 2023/9/6
 */
public interface ProjectSuggestionService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String projectName, String projectType, String createYear, String ids,Integer status) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ProjectSuggestionEntity projectSuggestionEntity) throws Exception;

    JsonBean saveEntity(String token, ProjectSuggestionEntity projectSuggestionEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void resolveSheet(XSSFSheet sheet, String token) throws Exception;

    void distribute(String ids, String personIds) throws Exception;
}
