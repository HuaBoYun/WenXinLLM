package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.RequireSuggestionMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.RequireSuggestionService;
import com.huabo.audit.service.TblOrganizaService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.AutoNo;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName RequireSuggestionServiceImpl
 * @Description
 * @DATE 2023/9/6
 */
@Service
public class RequireSuggestionServiceImpl implements RequireSuggestionService {

    @Autowired
    private RequireSuggestionMapper requireSuggestionMapper;

    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, BigDecimal suggestionNo, String concerns,BigDecimal draftId, String projectType, Integer queryYear) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        RequireSuggestionEntity requireSuggestionEntity = new RequireSuggestionEntity();
        if(suggestionNo != null){
            requireSuggestionEntity.setSuggestionNo(suggestionNo);
        }
        if(StringUtil.isNotEmpty(concerns)){
            requireSuggestionEntity.setConcerns(concerns);
        }
        if(draftId != null){
            requireSuggestionEntity.setDraftId(draftId);
        }
        
        if(StringUtil.isNotEmpty(projectType)) {
        	requireSuggestionEntity.setProjectType(projectType);
        }
        
        if(queryYear != null) {
        	requireSuggestionEntity.setQueryYear(queryYear);
        }
        
        if(StringUtils.isNotBlank(user.getDeptIds())){
        	requireSuggestionEntity.setQueryDeptIds(user.getDeptIds());
        }



        
        requireSuggestionEntity.setCurrentStaffId(user.getStaffid());
        
        Page<RequireSuggestionEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> requireSuggestionMapper.selectByEntity(requireSuggestionEntity));
		PageInfo<RequireSuggestionEntity> pageInfo = new PageInfoUtil<RequireSuggestionEntity>().parsePageInfo(page);

        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
    @Override
    public List<RequireSuggestionEntity> findExportListAll(String token, Integer pageNumber, Integer pageSize, BigDecimal suggestionNo, String concerns,BigDecimal draftId, String projectType, Integer queryYear,String ids) throws Exception {
        TblStaffUtil user = userProvider.get();

        RequireSuggestionEntity requireSuggestionEntity = new RequireSuggestionEntity();
        if(suggestionNo != null){
            requireSuggestionEntity.setSuggestionNo(suggestionNo);
        }
        if(StringUtil.isNotEmpty(concerns)){
            requireSuggestionEntity.setConcerns(concerns);
        }
        if(draftId != null){
            requireSuggestionEntity.setDraftId(draftId);
        }
        
        if(StringUtils.isNotBlank(user.getDeptIds())){
        	requireSuggestionEntity.setQueryDeptIds(user.getDeptIds());
        }
        if(StringUtil.isNotEmpty(projectType)) {
        	requireSuggestionEntity.setProjectType(projectType);
        }
        
        if(queryYear != null) {
        	requireSuggestionEntity.setQueryYear(queryYear);
        }
        if(StringUtils.isNotBlank(ids)){
            requireSuggestionEntity.setIds(ids);
        }
        
        requireSuggestionEntity.setCurrentStaffId(user.getStaffid());
        
        List<RequireSuggestionEntity> suList = requireSuggestionMapper.selectByEntity(requireSuggestionEntity);

        return suList;
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        RequireSuggestionEntity requireSuggestionEntity = requireSuggestionMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", requireSuggestionEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(RequireSuggestionEntity requireSuggestionEntity) throws Exception{
        requireSuggestionMapper.updateEntity(requireSuggestionEntity);
    }

    @Override
    public void saveEntity(String token, RequireSuggestionEntity requireSuggestionEntity, int type) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            requireSuggestionEntity.setCreateUser(tblStaff);
        }
        
        if((type == 1 && requireSuggestionEntity.getSuggestionNo() == null) || type == 0) {
	        Integer currentYear = LocalDate.now().getYear();
	        BigDecimal maxNo = this.requireSuggestionMapper.selectMapRequireSuggestionNo(currentYear);
	        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
	        requireSuggestionEntity.setSuggestionNo(autoNo);
        }
        requireSuggestionEntity.setId(RandomUtil.uuBigDecimalId());
        requireSuggestionMapper.insertEntity(requireSuggestionEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        requireSuggestionMapper.deleteEntity(ids);
    }

    @Override
    public void resolveSheet(XSSFWorkbook workBook, String token, Integer isCover) throws Exception {

    	XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        List<TblOrganization> tblOrganizations = tblOrganizationMapper.selectByExport();
        RequireSuggestionEntity requireSuggestionEntity = null;
        RequireSuggestionEntity preEntity = null;
        TblStaffUtil user = userProvider.get();
        TblStaff tblStaff = new TblStaff();
        tblStaff.setStaffid(user.getStaffid());
        
        for (int i =0; i < workBook.getNumberOfSheets(); i++){
            sheet = workBook.getSheetAt(i);
            for (int k = 1; k < sheet.getPhysicalNumberOfRows(); k++){
                row = sheet.getRow(k);
                if (row != null) {
                    requireSuggestionEntity = new RequireSuggestionEntity();

                    if(user != null) {
                        requireSuggestionEntity.setCreateUser(tblStaff);
                    }
                    
                    cell = row.getCell(0);
                    if(cell != null){
                        cell.setCellType(1);
                        requireSuggestionEntity.setSuggestionNo(StringUtils.isBlank(cell.getStringCellValue())?null:new BigDecimal(cell.getStringCellValue()));
                    }

                    
                    cell = row.getCell(1);
                    if(cell != null){
                        cell.setCellType(1);
                        String cellValue = cell.getStringCellValue();
                        if(StringUtil.isNotEmpty(cellValue)){
                            List<TblOrganization> collect = tblOrganizations.stream().filter(t -> {
                                return cellValue.equals(t.getOrgname());
                            }).collect(Collectors.toList());
                            if(collect.size() > 0){
                                TblOrganization tblOrganization = collect.get(0);
                                requireSuggestionEntity.setOrganizationId(tblOrganization.getOrgid()+"");
                            }
                        }
                    }
                    
                    
                    cell = row.getCell(2);
                    if(cell != null){
                        cell.setCellType(1);
                        requireSuggestionEntity.setConcerns(cell.getStringCellValue());
                    }

                    cell = row.getCell(3);
                    if(cell != null){
                        cell.setCellType(1);
                        requireSuggestionEntity.setConcernsContent(cell.getStringCellValue());
                    }


                    cell = row.getCell(4);
                    if(cell != null){
                        cell.setCellType(1);
                        requireSuggestionEntity.setProjectType(cell.getStringCellValue());
                    }

                    cell = row.getCell(5);
                    if(cell != null){
                        cell.setCellType(1);
                        requireSuggestionEntity.setRemark(cell.getStringCellValue());
                    }

                    if(requireSuggestionEntity.getSuggestionNo() != null) {
                    	preEntity = this.requireSuggestionMapper.selecrRepeatSuggentionNoCount(requireSuggestionEntity.getSuggestionNo());
                    	 if(preEntity != null) {
                    		 if(isCover == 0) {
                    			 continue;
                    		 }
                    		 requireSuggestionEntity.setId(preEntity.getId());
                             updateEntity(requireSuggestionEntity);
                    	 }else {
                             saveEntity(token, requireSuggestionEntity,1);
                    	 }
                    }else {
                        saveEntity(token, requireSuggestionEntity,1);
                    }
                }
            }
        }
    }

    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            RequireSuggestionEntity requireSuggestionEntity = requireSuggestionMapper.selectById(id);
            if(requireSuggestionEntity != null){
                requireSuggestionEntity.setPersonIds(personIds);
                this.updateEntity(requireSuggestionEntity);
            }
        }

    }

	@Override
	public JsonBean getAutoNo(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
		
		//获取今年年份
        Integer currentYear = LocalDate.now().getYear();
        BigDecimal maxNo = this.requireSuggestionMapper.selectMapRequireSuggestionNo(currentYear);
        BigDecimal autoNo = AutoNo.getAutoNo(currentYear, maxNo);
        return ResponseFormat.retParam(1,200,autoNo);
	}
}
