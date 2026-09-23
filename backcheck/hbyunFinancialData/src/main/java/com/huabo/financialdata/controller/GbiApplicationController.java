package com.huabo.financialdata.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.BasePageDO;
import com.huabo.financialdata.entity.dto.gbi.GbiTableDataQuery;
import com.huabo.financialdata.entity.entity.GbiApplicationConfig;
import com.huabo.financialdata.entity.entity.GbiBatch;
import com.huabo.financialdata.entity.entity.GbiKnowledge;
import com.huabo.financialdata.entity.entity.GbiTableColumns;
import com.huabo.financialdata.entity.vo.gbi.GbiApplicationConfigSaveRequestVO;
import com.huabo.financialdata.entity.vo.gbi.GbiBatchListVO;
import com.huabo.financialdata.entity.vo.gbi.GbiKnowledgeRequestVO;
import com.huabo.financialdata.entity.vo.gbi.GbiTableListVO;
import com.huabo.financialdata.service.GbiApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * GBI 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-04-20
 */
@RestController
@RequestMapping("/gbi/export")
@Tag(name="GBI大模型数据导入",description="GBI大模型数据导入")
public class GbiApplicationController {

    @Autowired
    private GbiApplicationService gbiApplicationService;
    
    @Resource
    private UserProvider userProvider;



    /**
     * GBI获取用户应用配置
     *
     * @param token 用户登录token
     * @return
     */
    @GetMapping("/applicationConfig")
    @Operation(summary="GBI获取用户应用配置")
    public ApiResponse<GbiApplicationConfig> getApplicationConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        try {
        	//查询当前财务账套有效的最大月份
            TblStaffUtil staff = userProvider.get();
            
            if(staff == null) {
            	return ApiResponse.fail("用户已失效");
            }
            return ApiResponse.success(gbiApplicationService.getApplicationConfig(staff.getStaffid()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 保存更新应用配置
     * @param token
     * @param requestVO
     * @return
     */
    @PostMapping("/saveApplicationConfig")
    @Operation(summary="GBI更新用户应用配置")
    public ApiResponse<Boolean> saveApplicationConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody GbiApplicationConfigSaveRequestVO requestVO) throws Exception {
        try {
        	//查询当前财务账套有效的最大月份
            TblStaffUtil staff = userProvider.get();
            
            if(staff == null) {
            	return ApiResponse.fail("用户已失效");
            }
            GbiApplicationConfig applicationConfig = new GbiApplicationConfig();
            applicationConfig.setId(requestVO.getId());
            applicationConfig.setContinuousQuestioning(requestVO.getContinuousQuestioning());
            return ApiResponse.success(gbiApplicationService.saveApplicationConfig(staff, applicationConfig));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }



    /**
     * 知识列表
     *
     * @param token 用户登录token
     * @return
     */
    @GetMapping("/getKnowledgeList")
    @Operation(summary="知识列表")
    public ApiResponse<List<GbiKnowledge>> getKnowledgeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.getKnowledgeList(staff.getStaffid()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 新增知识
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/addKnowledge")
    @Operation(summary="新增知识")
    public ApiResponse<Boolean> addKnowledge(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "knowledgeRequestVO", description = "新增知识GbiKnowledgeRequestVO", required = true) @RequestBody GbiKnowledgeRequestVO knowledgeRequestVO) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.addKnowledge(staff,knowledgeRequestVO));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 修改知识
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/updateKnowledge")
    @Operation(summary="修改知识")
    public ApiResponse<Boolean> updateKnowledge(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "knowledgeRequestVO", description = "修改知识GbiKnowledgeRequestVO", required = true) @RequestBody GbiKnowledgeRequestVO knowledgeRequestVO) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.updateKnowledge(knowledgeRequestVO));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 删除知识
     *
     * @param token 用户登录token
     * @return
     */
    @DeleteMapping("/deleteKnowledge")
    @Operation(summary="删除知识")
    public ApiResponse<Boolean> deleteKnowledge(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "knowledgeId", description = "知识id:knowledgeId", required = true) @RequestParam String knowledgeId) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.deleteKnowledge(knowledgeId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    // -------大模型上传数据--------------


    /**
     * 分页查询批次列表
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/queryBatch")
    @Operation(summary="分页查询批次列表")
    public ApiResponse<IPage<GbiBatchListVO>> queryBatch(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "分页参数", description = "分页参数", required = true) @RequestBody BasePageDO pageDO
    ) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }

            Page<GbiBatch> page = new Page(Objects.isNull(pageDO.getPageNo()) ? 1 : pageDO.getPageNo(), Objects.isNull(pageDO.getPageSize()) ? 10 : pageDO.getPageSize());
            IPage<GbiBatchListVO> gbiBatchListVOIPage = gbiApplicationService.queryBatch(staff.getStaffid(), page);
            return ApiResponse.success(gbiBatchListVOIPage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * getBatchList 批次列表包含表信息列表
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/getBatchList")
    @Operation(summary="查询导入批次列表，包含表信息列表")
    public ApiResponse<List<GbiBatchListVO>> getBatchList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.getBatchList(staff.getStaffid()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 查询tableList
     *
     * @param token 用户登录token
     * @return
     */
    @GetMapping("/getTableList")
    @Operation(summary="查询tableList")
    public ApiResponse<List<GbiTableListVO>> getTableList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "batchId", description = "批次id") @RequestParam(required = false) String batchId) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.getTableList(staff.getStaffid(),batchId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 查询表字段
     *
     * @param token 用户登录token
     * @return
     */
    @GetMapping("/getTableColumns")
    @Operation(summary="查询表字段，选择父级")
    public ApiResponse<List<GbiTableColumns>> getTableColumns(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "tableId", description = "tableId", required = true) @RequestParam("tableId") String tableId) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.getTableColumns(tableId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }


    /**
     * 修改表、字段信息
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/updateTableColumns")
    @Operation(summary="修改表、字段信息")
    public ApiResponse<Boolean> updateTableAndSettings(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "gbiTableListRequestVO", description = "gbiTableListRequestVO", required = true) @RequestBody List<GbiTableListVO> gbiTableListRequestVO) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.updateTableAndSettings(gbiTableListRequestVO));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }


    /**
     * 数据详情
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/getTableData")
    @Operation(summary="数据详情")
    public ApiResponse<IPage<Map>> getTableData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "gbiTableDataQuery", description = "GbiTableDataQuery") @RequestBody GbiTableDataQuery gbiTableDataQuery) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.getTableData(staff,gbiTableDataQuery));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }


    /**
     * 上传数据
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/uploadData")
    @Operation(summary="上传数据")
    public ApiResponse<String> uploadData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "file", description = "file", required = true) @RequestParam("file") MultipartFile file) {
        try {
            TblStaffUtil staff = userProvider.get();
            if (Objects.isNull(staff)) {
                throw new BizException("用户未登录！");
            }
            return ApiResponse.success(gbiApplicationService.uploadData(staff,file));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

}
