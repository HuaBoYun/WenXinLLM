package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import com.huabo.audit.service.RequireSuggestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName RequireSuggestionController
 * @Description 计划管理-需求建议
 * @DATE 2023/9/6
 */
@RestController
@Slf4j
@Tag(name="计划管理-需求建议",description="计划管理-需求建议")
@RequestMapping(value = "/plan/require/suggestion")
public class RequireSuggestionController {
    @Autowired
    private RequireSuggestionService requireSuggestionService;

    @GetMapping("/getList")
    @Operation(summary = "需求建议列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "suggestionNo", description = "序号", required = false)@RequestParam(value = "suggestionNo", required = false, defaultValue = "") BigDecimal suggestionNo,
                            @Parameter(name = "concerns", description = "重点关注领域、项目、事项和风险", required = false)@RequestParam(value = "concerns", required = false, defaultValue = "") String concerns,
                            @Parameter(name = "draftId", description = "底稿编号", required = false)@RequestParam(value = "draftId", required = false, defaultValue = "") BigDecimal draftId,
                            @Parameter(name = "projectType", description = "项目类型", required = false)@RequestParam(value = "projectType", required = false) String projectType,
                            @Parameter(name = "queryYear", description = "查询年份", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = requireSuggestionService.findAll(token,pageNumber,pageSize,suggestionNo,concerns,draftId,projectType,queryYear);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/detail")
    @Operation(summary = "根据ID获取需求建议详细信息")
    public JsonBean getById(HttpServletRequest request,@Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id){
        JsonBean jsonBean = null;
        try{
            jsonBean = requireSuggestionService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @PostMapping(value="/saveOrUpdate" , produces = "application/json; charset=utf-8")
    @Operation(summary = "添加或修改信息")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "rs", description = "实体", required = true) @RequestBody RequireSuggestionEntity requireSuggestionEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != requireSuggestionEntity.getId()){
                requireSuggestionService.updateEntity(requireSuggestionEntity);
            }else{
                requireSuggestionService.saveEntity(token,requireSuggestionEntity,0);
            }
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    public JsonBean deleteByIds(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "ids", description = "Id,多个用,号隔开", required = true) @RequestParam String ids
    ){
        JsonBean jsonBean = null;
        try{
            requireSuggestionService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/export")
    @Operation(summary = "列表导出")
    public void exportList( HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                           @Parameter(name = "suggestionNo", description = "序号", required = false)@RequestParam(value = "suggestionNo", required = false, defaultValue = "") BigDecimal suggestionNo,
                            @Parameter(name = "draftId", description = "底稿编号", required = false)@RequestParam(value = "draftId", required = false, defaultValue = "") BigDecimal draftId,
                           @Parameter(name = "concerns", description = "重点关注领域、项目、事项和风险", required = false)@RequestParam(value = "concerns", required = false, defaultValue = "")String concerns,
                           @Parameter(name = "projectType", description = "项目类型", required = false)@RequestParam(value = "projectType", required = false) String projectType,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false) String ids,
                           @Parameter(name = "queryYear", description = "查询年份", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear){
        log.info("需求建议列表导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "需求建议列表" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            List<RequireSuggestionEntity> list = requireSuggestionService.findExportListAll(token, pageNumber, pageSize, suggestionNo, concerns,draftId,projectType,queryYear,ids);
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号", "单位","重点关注领域、项目、事项或风险", "关注内容", "项目类型", "备注"};
            for (int i = 0; i < list.size(); i++) {
                RequireSuggestionEntity r = list.get(i);
                Object[] o = {r.getSuggestionNo(),r.getOrganization()!=null?r.getOrganization().getOrgname():"",r.getConcerns(),r.getConcernsContent(),r.getProjectType(),r.getRemark()};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("需求建议列表导出失败",e );
        }
    }

    @PostMapping("/import")
    @Operation(summary = "列表导入")
    @ResponseBody
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
    		@Parameter(name = "isCover", description = "是否覆盖 0-否 ，1-是  ，默认0", required = false)@RequestParam(value = "isCover", required = false, defaultValue = "0") Integer isCover,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        try{
             requireSuggestionService.resolveSheet(workBook,token,isCover);
             return ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("需求建议列表导入失败",e );
            return  ResponseFormat.retParam(0,1000,e.getMessage());
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }

    }

    @PostMapping(value="/xf" , produces = "application/json; charset=utf-8")
    @Operation(summary = "下发接口")
    public JsonBean distribute(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "rs", description = "请求参数：ids,personIds", required = true) @RequestBody Map<String,String> params
    ){
        JsonBean jsonBean = null;
        try{
            String ids = params.get("ids");
            String personIds = params.get("personIds");
            if(StringUtil.isEmpty(ids)){
                return ResponseFormat.retParam(30001,"未选择下发的需求建议");
            }
            if(StringUtils.isEmpty(personIds)){
                return ResponseFormat.retParam(30001,"未选择下发的人员");
            }
            requireSuggestionService.distribute(ids,personIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getAutoNo")
    @Operation(summary = "列表导出")
    public JsonBean getAutoNo( HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception{
    	return this.requireSuggestionService.getAutoNo(token);
    }

}
