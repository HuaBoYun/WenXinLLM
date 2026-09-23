package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ProjectSuggestionEntity;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;

public interface ProjectSuggestionNoticeService extends IService<ProjectSuggestionNoticeEntity> {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String name) throws Exception;

    JsonBean findById(String id);

    JsonBean updateEntity(ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity);

    JsonBean saveEntity(String token, ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity) throws Exception;

    void deleteByIds(String ids);

    void distribute(String token,String ids, String personIds)throws Exception;
}
