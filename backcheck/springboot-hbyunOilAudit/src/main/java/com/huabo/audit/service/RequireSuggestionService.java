package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @InterfaceName RequireSuggestionService
 * @Description
 * @DATE 2023/9/6
 */
public interface RequireSuggestionService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, BigDecimal suggestionNo, String concerns, BigDecimal draftId, String projectType, Integer queryYear) throws Exception;
    
    List<RequireSuggestionEntity> findExportListAll(String token, Integer pageNumber, Integer pageSize, BigDecimal suggestionNo, String concerns,
			BigDecimal draftId, String projectType, Integer queryYear,String ids) throws Exception;

    JsonBean findById(String id) throws Exception;

    void updateEntity(RequireSuggestionEntity requireSuggestionEntity) throws Exception;

    void saveEntity(String token, RequireSuggestionEntity requireSuggestionEntity, int type) throws Exception;

    void deleteByIds(String ids) throws Exception;

    void resolveSheet(XSSFWorkbook workBook, String token, Integer isCover) throws Exception;

    void distribute(String ids, String personIds) throws Exception;

	JsonBean getAutoNo(String token) throws Exception;
}
