package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ProjectSortEntity;

/**
 * @author Rui
 * @InterfaceName ProjectSortService
 * @Description
 * @DATE 2023/9/23
 */
public interface ProjectSortService {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(ProjectSortEntity projectSortEntity) throws Exception;

    void saveEntity(String token, ProjectSortEntity projectSortEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
}
