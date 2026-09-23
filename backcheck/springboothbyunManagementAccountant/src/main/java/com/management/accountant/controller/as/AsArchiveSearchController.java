package com.management.accountant.controller.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsArchiveSearch;
import com.management.accountant.service.as.AsArchiveSearchService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案检索控制器
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/as/archiveSearch")
@Api(tags = "档案检索管理")
public class AsArchiveSearchController {

    @Autowired
    private AsArchiveSearchService archiveSearchService;

    // ==================== 基础CRUD接口 ====================

    @PostMapping("/create")
    @ApiOperation("创建档案检索")
    public MyJsonBean createArchiveSearch(@RequestBody AsArchiveSearch archiveSearch) {
        try {
            AsArchiveSearch result = archiveSearchService.createArchiveSearch(archiveSearch);
            return MyJsonBean.success("档案检索创建成功", result);
        } catch (Exception e) {
            log.error("创建档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("创建档案检索失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新档案检索")
    public MyJsonBean updateArchiveSearch(@RequestBody AsArchiveSearch archiveSearch) {
        try {
            AsArchiveSearch result = archiveSearchService.updateArchiveSearch(archiveSearch);
            return MyJsonBean.success("档案检索更新成功", result);
        } catch (Exception e) {
            log.error("更新档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("更新档案检索失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{tenantId}/{searchId}")
    @ApiOperation("删除档案检索")
    public MyJsonBean deleteArchiveSearch(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.deleteArchiveSearch(tenantId, searchId);
            return result ? MyJsonBean.success("档案检索删除成功") : MyJsonBean.error("档案检索删除失败");
        } catch (Exception e) {
            log.error("删除档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("删除档案检索失败: " + e.getMessage());
        }
    }

    @GetMapping("/get/{tenantId}/{searchId}")
    @ApiOperation("根据ID查询档案检索")
    public MyJsonBean getArchiveSearchById(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            AsArchiveSearch result = archiveSearchService.getArchiveSearchById(tenantId, searchId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询档案检索失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByCode/{tenantId}/{searchCode}")
    @ApiOperation("根据编号查询档案检索")
    public MyJsonBean getArchiveSearchByCode(@PathVariable Long tenantId, @PathVariable String searchCode) {
        try {
            AsArchiveSearch result = archiveSearchService.getArchiveSearchByCode(tenantId, searchCode);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据编号查询档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/page/{tenantId}")
    @ApiOperation("分页查询档案检索")
    public MyJsonBean getArchiveSearchPage(@PathVariable Long tenantId,
                                          @RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String searchName,
                                          @RequestParam(required = false) String searchType,
                                          @RequestParam(required = false) String searchStatus,
                                          @RequestParam(required = false) String searchEngine) {
        try {
            Page<AsArchiveSearch> page = new Page<>(current, size);
            IPage<AsArchiveSearch> result = archiveSearchService.getArchiveSearchPage(page, tenantId, searchName, searchType, searchStatus, searchEngine);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询档案检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 检索管理接口 ====================

    @PostMapping("/start/{tenantId}/{searchId}")
    @ApiOperation("启动检索服务")
    public MyJsonBean startSearchService(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.startSearchService(tenantId, searchId);
            return result ? MyJsonBean.success("检索服务启动成功") : MyJsonBean.error("检索服务启动失败");
        } catch (Exception e) {
            log.error("启动检索服务失败: {}", e.getMessage(), e);
            return MyJsonBean.error("启动检索服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/stop/{tenantId}/{searchId}")
    @ApiOperation("停止检索服务")
    public MyJsonBean stopSearchService(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.stopSearchService(tenantId, searchId);
            return result ? MyJsonBean.success("检索服务停止成功") : MyJsonBean.error("检索服务停止失败");
        } catch (Exception e) {
            log.error("停止检索服务失败: {}", e.getMessage(), e);
            return MyJsonBean.error("停止检索服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/restart/{tenantId}/{searchId}")
    @ApiOperation("重启检索服务")
    public MyJsonBean restartSearchService(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.restartSearchService(tenantId, searchId);
            return result ? MyJsonBean.success("检索服务重启成功") : MyJsonBean.error("检索服务重启失败");
        } catch (Exception e) {
            log.error("重启检索服务失败: {}", e.getMessage(), e);
            return MyJsonBean.error("重启检索服务失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute/{tenantId}/{searchId}")
    @ApiOperation("执行检索")
    public MyJsonBean executeSearch(@PathVariable Long tenantId, 
                                   @PathVariable Long searchId, 
                                   @RequestBody Map<String, Object> searchParams) {
        try {
            Map<String, Object> result = archiveSearchService.executeSearch(tenantId, searchId, searchParams);
            return MyJsonBean.success("检索执行成功", result);
        } catch (Exception e) {
            log.error("执行检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("执行检索失败: " + e.getMessage());
        }
    }

    @PostMapping("/fullTextSearch/{tenantId}/{searchId}")
    @ApiOperation("全文检索")
    public MyJsonBean fullTextSearch(@PathVariable Long tenantId, 
                                    @PathVariable Long searchId,
                                    @RequestParam String keywords,
                                    @RequestBody(required = false) Map<String, Object> filters) {
        try {
            Map<String, Object> result = archiveSearchService.fullTextSearch(tenantId, searchId, keywords, filters);
            return MyJsonBean.success("全文检索成功", result);
        } catch (Exception e) {
            log.error("全文检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("全文检索失败: " + e.getMessage());
        }
    }

    @PostMapping("/semanticSearch/{tenantId}/{searchId}")
    @ApiOperation("语义检索")
    public MyJsonBean semanticSearch(@PathVariable Long tenantId, 
                                    @PathVariable Long searchId,
                                    @RequestParam String query,
                                    @RequestBody(required = false) Map<String, Object> context) {
        try {
            Map<String, Object> result = archiveSearchService.semanticSearch(tenantId, searchId, query, context);
            return MyJsonBean.success("语义检索成功", result);
        } catch (Exception e) {
            log.error("语义检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("语义检索失败: " + e.getMessage());
        }
    }

    @PostMapping("/imageSearch/{tenantId}/{searchId}")
    @ApiOperation("图像检索")
    public MyJsonBean imageSearch(@PathVariable Long tenantId, 
                                 @PathVariable Long searchId,
                                 @RequestParam String imageData,
                                 @RequestBody(required = false) Map<String, Object> options) {
        try {
            Map<String, Object> result = archiveSearchService.imageSearch(tenantId, searchId, imageData, options);
            return MyJsonBean.success("图像检索成功", result);
        } catch (Exception e) {
            log.error("图像检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("图像检索失败: " + e.getMessage());
        }
    }

    @PostMapping("/voiceSearch/{tenantId}/{searchId}")
    @ApiOperation("语音检索")
    public MyJsonBean voiceSearch(@PathVariable Long tenantId, 
                                 @PathVariable Long searchId,
                                 @RequestParam String audioData,
                                 @RequestBody(required = false) Map<String, Object> options) {
        try {
            Map<String, Object> result = archiveSearchService.voiceSearch(tenantId, searchId, audioData, options);
            return MyJsonBean.success("语音检索成功", result);
        } catch (Exception e) {
            log.error("语音检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("语音检索失败: " + e.getMessage());
        }
    }

    @PostMapping("/hybridSearch/{tenantId}/{searchId}")
    @ApiOperation("混合检索")
    public MyJsonBean hybridSearch(@PathVariable Long tenantId, 
                                  @PathVariable Long searchId,
                                  @RequestBody Map<String, Object> multiModalData) {
        try {
            Map<String, Object> result = archiveSearchService.hybridSearch(tenantId, searchId, multiModalData);
            return MyJsonBean.success("混合检索成功", result);
        } catch (Exception e) {
            log.error("混合检索失败: {}", e.getMessage(), e);
            return MyJsonBean.error("混合检索失败: " + e.getMessage());
        }
    }

    // ==================== 索引管理接口 ====================

    @PostMapping("/createIndex/{tenantId}/{searchId}")
    @ApiOperation("创建索引")
    public MyJsonBean createIndex(@PathVariable Long tenantId, 
                                 @PathVariable Long searchId,
                                 @RequestBody Map<String, Object> indexConfig) {
        try {
            boolean result = archiveSearchService.createIndex(tenantId, searchId, indexConfig);
            return result ? MyJsonBean.success("索引创建成功") : MyJsonBean.error("索引创建失败");
        } catch (Exception e) {
            log.error("创建索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("创建索引失败: " + e.getMessage());
        }
    }

    @PostMapping("/rebuildIndex/{tenantId}/{searchId}")
    @ApiOperation("重建索引")
    public MyJsonBean rebuildIndex(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.rebuildIndex(tenantId, searchId);
            return result ? MyJsonBean.success("索引重建成功") : MyJsonBean.error("索引重建失败");
        } catch (Exception e) {
            log.error("重建索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("重建索引失败: " + e.getMessage());
        }
    }

    @PostMapping("/optimizeIndex/{tenantId}/{searchId}")
    @ApiOperation("优化索引")
    public MyJsonBean optimizeIndex(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.optimizeIndex(tenantId, searchId);
            return result ? MyJsonBean.success("索引优化成功") : MyJsonBean.error("索引优化失败");
        } catch (Exception e) {
            log.error("优化索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("优化索引失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteIndex/{tenantId}/{searchId}")
    @ApiOperation("删除索引")
    public MyJsonBean deleteIndex(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.deleteIndex(tenantId, searchId);
            return result ? MyJsonBean.success("索引删除成功") : MyJsonBean.error("索引删除失败");
        } catch (Exception e) {
            log.error("删除索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("删除索引失败: " + e.getMessage());
        }
    }

    @PutMapping("/updateIndexConfig/{tenantId}/{searchId}")
    @ApiOperation("更新索引配置")
    public MyJsonBean updateIndexConfig(@PathVariable Long tenantId, 
                                       @PathVariable Long searchId,
                                       @RequestBody Map<String, Object> indexConfig) {
        try {
            boolean result = archiveSearchService.updateIndexConfig(tenantId, searchId, indexConfig);
            return result ? MyJsonBean.success("索引配置更新成功") : MyJsonBean.error("索引配置更新失败");
        } catch (Exception e) {
            log.error("更新索引配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("更新索引配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/getIndexStatus/{tenantId}/{searchId}")
    @ApiOperation("获取索引状态")
    public MyJsonBean getIndexStatus(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            Map<String, Object> result = archiveSearchService.getIndexStatus(tenantId, searchId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取索引状态失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getIndexStatistics/{tenantId}/{searchId}")
    @ApiOperation("获取索引统计")
    public MyJsonBean getIndexStatistics(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            Map<String, Object> result = archiveSearchService.getIndexStatistics(tenantId, searchId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取索引统计失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== OCR处理接口 ====================

    @PostMapping("/startOcr/{tenantId}/{searchId}")
    @ApiOperation("启动OCR处理")
    public MyJsonBean startOcrProcessing(@PathVariable Long tenantId,
                                        @PathVariable Long searchId,
                                        @RequestBody Map<String, Object> ocrConfig) {
        try {
            boolean result = archiveSearchService.startOcrProcessing(tenantId, searchId, ocrConfig);
            return result ? MyJsonBean.success("OCR处理启动成功") : MyJsonBean.error("OCR处理启动失败");
        } catch (Exception e) {
            log.error("启动OCR处理失败: {}", e.getMessage(), e);
            return MyJsonBean.error("启动OCR处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/stopOcr/{tenantId}/{searchId}")
    @ApiOperation("停止OCR处理")
    public MyJsonBean stopOcrProcessing(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.stopOcrProcessing(tenantId, searchId);
            return result ? MyJsonBean.success("OCR处理停止成功") : MyJsonBean.error("OCR处理停止失败");
        } catch (Exception e) {
            log.error("停止OCR处理失败: {}", e.getMessage(), e);
            return MyJsonBean.error("停止OCR处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/executeOcr/{tenantId}/{searchId}")
    @ApiOperation("执行OCR识别")
    public MyJsonBean executeOcrRecognition(@PathVariable Long tenantId,
                                           @PathVariable Long searchId,
                                           @RequestParam String documentPath) {
        try {
            Map<String, Object> result = archiveSearchService.executeOcrRecognition(tenantId, searchId, documentPath);
            return MyJsonBean.success("OCR识别成功", result);
        } catch (Exception e) {
            log.error("执行OCR识别失败: {}", e.getMessage(), e);
            return MyJsonBean.error("OCR识别失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchOcr/{tenantId}/{searchId}")
    @ApiOperation("批量OCR处理")
    public MyJsonBean batchOcrProcessing(@PathVariable Long tenantId,
                                        @PathVariable Long searchId,
                                        @RequestBody List<String> documentPaths) {
        try {
            Map<String, Object> result = archiveSearchService.batchOcrProcessing(tenantId, searchId, documentPaths);
            return MyJsonBean.success("批量OCR处理成功", result);
        } catch (Exception e) {
            log.error("批量OCR处理失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量OCR处理失败: " + e.getMessage());
        }
    }

    @GetMapping("/getOcrResults/{tenantId}/{searchId}/{taskId}")
    @ApiOperation("获取OCR处理结果")
    public MyJsonBean getOcrResults(@PathVariable Long tenantId,
                                   @PathVariable Long searchId,
                                   @PathVariable String taskId) {
        try {
            Map<String, Object> result = archiveSearchService.getOcrResults(tenantId, searchId, taskId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("获取OCR处理结果失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PutMapping("/updateOcrConfig/{tenantId}/{searchId}")
    @ApiOperation("更新OCR配置")
    public MyJsonBean updateOcrConfig(@PathVariable Long tenantId,
                                     @PathVariable Long searchId,
                                     @RequestBody Map<String, Object> ocrConfig) {
        try {
            boolean result = archiveSearchService.updateOcrConfig(tenantId, searchId, ocrConfig);
            return result ? MyJsonBean.success("OCR配置更新成功") : MyJsonBean.error("OCR配置更新失败");
        } catch (Exception e) {
            log.error("更新OCR配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("更新OCR配置失败: " + e.getMessage());
        }
    }

    // ==================== 语义检索接口 ====================

    @PostMapping("/initSemanticModel/{tenantId}/{searchId}")
    @ApiOperation("初始化语义模型")
    public MyJsonBean initializeSemanticModel(@PathVariable Long tenantId,
                                             @PathVariable Long searchId,
                                             @RequestBody Map<String, Object> modelConfig) {
        try {
            boolean result = archiveSearchService.initializeSemanticModel(tenantId, searchId, modelConfig);
            return result ? MyJsonBean.success("语义模型初始化成功") : MyJsonBean.error("语义模型初始化失败");
        } catch (Exception e) {
            log.error("初始化语义模型失败: {}", e.getMessage(), e);
            return MyJsonBean.error("初始化语义模型失败: " + e.getMessage());
        }
    }

    @PutMapping("/updateSemanticModel/{tenantId}/{searchId}")
    @ApiOperation("更新语义模型")
    public MyJsonBean updateSemanticModel(@PathVariable Long tenantId,
                                         @PathVariable Long searchId,
                                         @RequestParam String modelVersion) {
        try {
            boolean result = archiveSearchService.updateSemanticModel(tenantId, searchId, modelVersion);
            return result ? MyJsonBean.success("语义模型更新成功") : MyJsonBean.error("语义模型更新失败");
        } catch (Exception e) {
            log.error("更新语义模型失败: {}", e.getMessage(), e);
            return MyJsonBean.error("更新语义模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/trainSemanticModel/{tenantId}/{searchId}")
    @ApiOperation("训练语义模型")
    public MyJsonBean trainSemanticModel(@PathVariable Long tenantId,
                                        @PathVariable Long searchId,
                                        @RequestBody Map<String, Object> trainingData) {
        try {
            boolean result = archiveSearchService.trainSemanticModel(tenantId, searchId, trainingData);
            return result ? MyJsonBean.success("语义模型训练成功") : MyJsonBean.error("语义模型训练失败");
        } catch (Exception e) {
            log.error("训练语义模型失败: {}", e.getMessage(), e);
            return MyJsonBean.error("训练语义模型失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculateSimilarity/{tenantId}/{searchId}")
    @ApiOperation("计算语义相似度")
    public MyJsonBean calculateSemanticSimilarity(@PathVariable Long tenantId,
                                                 @PathVariable Long searchId,
                                                 @RequestParam String text1,
                                                 @RequestParam String text2) {
        try {
            Map<String, Object> result = archiveSearchService.calculateSemanticSimilarity(tenantId, searchId, text1, text2);
            return MyJsonBean.success("相似度计算成功", result);
        } catch (Exception e) {
            log.error("计算语义相似度失败: {}", e.getMessage(), e);
            return MyJsonBean.error("相似度计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/vectorization/{tenantId}/{searchId}")
    @ApiOperation("语义向量化")
    public MyJsonBean semanticVectorization(@PathVariable Long tenantId,
                                           @PathVariable Long searchId,
                                           @RequestParam String text) {
        try {
            Map<String, Object> result = archiveSearchService.semanticVectorization(tenantId, searchId, text);
            return MyJsonBean.success("向量化成功", result);
        } catch (Exception e) {
            log.error("语义向量化失败: {}", e.getMessage(), e);
            return MyJsonBean.error("向量化失败: " + e.getMessage());
        }
    }

    // ==================== 推荐系统接口 ====================

    @PostMapping("/startRecommendation/{tenantId}/{searchId}")
    @ApiOperation("启动推荐系统")
    public MyJsonBean startRecommendationSystem(@PathVariable Long tenantId,
                                               @PathVariable Long searchId,
                                               @RequestBody Map<String, Object> recommendConfig) {
        try {
            boolean result = archiveSearchService.startRecommendationSystem(tenantId, searchId, recommendConfig);
            return result ? MyJsonBean.success("推荐系统启动成功") : MyJsonBean.error("推荐系统启动失败");
        } catch (Exception e) {
            log.error("启动推荐系统失败: {}", e.getMessage(), e);
            return MyJsonBean.error("启动推荐系统失败: " + e.getMessage());
        }
    }

    @PostMapping("/stopRecommendation/{tenantId}/{searchId}")
    @ApiOperation("停止推荐系统")
    public MyJsonBean stopRecommendationSystem(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.stopRecommendationSystem(tenantId, searchId);
            return result ? MyJsonBean.success("推荐系统停止成功") : MyJsonBean.error("推荐系统停止失败");
        } catch (Exception e) {
            log.error("停止推荐系统失败: {}", e.getMessage(), e);
            return MyJsonBean.error("停止推荐系统失败: " + e.getMessage());
        }
    }

    @GetMapping("/getRecommendations/{tenantId}/{searchId}")
    @ApiOperation("获取推荐结果")
    public MyJsonBean getRecommendations(@PathVariable Long tenantId,
                                        @PathVariable Long searchId,
                                        @RequestParam String userId,
                                        @RequestBody(required = false) Map<String, Object> context) {
        try {
            Map<String, Object> result = archiveSearchService.getRecommendations(tenantId, searchId, userId, context);
            return MyJsonBean.success("获取推荐成功", result);
        } catch (Exception e) {
            log.error("获取推荐结果失败: {}", e.getMessage(), e);
            return MyJsonBean.error("获取推荐失败: " + e.getMessage());
        }
    }

    @PutMapping("/updateRecommendationAlgorithm/{tenantId}/{searchId}")
    @ApiOperation("更新推荐算法")
    public MyJsonBean updateRecommendationAlgorithm(@PathVariable Long tenantId,
                                                   @PathVariable Long searchId,
                                                   @RequestParam String algorithm) {
        try {
            boolean result = archiveSearchService.updateRecommendationAlgorithm(tenantId, searchId, algorithm);
            return result ? MyJsonBean.success("推荐算法更新成功") : MyJsonBean.error("推荐算法更新失败");
        } catch (Exception e) {
            log.error("更新推荐算法失败: {}", e.getMessage(), e);
            return MyJsonBean.error("更新推荐算法失败: " + e.getMessage());
        }
    }

    @PostMapping("/trainRecommendationModel/{tenantId}/{searchId}")
    @ApiOperation("训练推荐模型")
    public MyJsonBean trainRecommendationModel(@PathVariable Long tenantId,
                                              @PathVariable Long searchId,
                                              @RequestBody Map<String, Object> trainingData) {
        try {
            boolean result = archiveSearchService.trainRecommendationModel(tenantId, searchId, trainingData);
            return result ? MyJsonBean.success("推荐模型训练成功") : MyJsonBean.error("推荐模型训练失败");
        } catch (Exception e) {
            log.error("训练推荐模型失败: {}", e.getMessage(), e);
            return MyJsonBean.error("训练推荐模型失败: " + e.getMessage());
        }
    }

    // ==================== 性能优化接口 ====================

    @PostMapping("/startOptimization/{tenantId}/{searchId}")
    @ApiOperation("启动性能优化")
    public MyJsonBean startPerformanceOptimization(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.startPerformanceOptimization(tenantId, searchId);
            return result ? MyJsonBean.success("性能优化启动成功") : MyJsonBean.error("性能优化启动失败");
        } catch (Exception e) {
            log.error("启动性能优化失败: {}", e.getMessage(), e);
            return MyJsonBean.error("启动性能优化失败: " + e.getMessage());
        }
    }

    @PostMapping("/stopOptimization/{tenantId}/{searchId}")
    @ApiOperation("停止性能优化")
    public MyJsonBean stopPerformanceOptimization(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.stopPerformanceOptimization(tenantId, searchId);
            return result ? MyJsonBean.success("性能优化停止成功") : MyJsonBean.error("性能优化停止失败");
        } catch (Exception e) {
            log.error("停止性能优化失败: {}", e.getMessage(), e);
            return MyJsonBean.error("停止性能优化失败: " + e.getMessage());
        }
    }

    @PostMapping("/configureCacheStrategy/{tenantId}/{searchId}")
    @ApiOperation("配置缓存策略")
    public MyJsonBean configureCacheStrategy(@PathVariable Long tenantId,
                                            @PathVariable Long searchId,
                                            @RequestBody Map<String, Object> cacheConfig) {
        try {
            boolean result = archiveSearchService.configureCacheStrategy(tenantId, searchId, cacheConfig);
            return result ? MyJsonBean.success("缓存策略配置成功") : MyJsonBean.error("缓存策略配置失败");
        } catch (Exception e) {
            log.error("配置缓存策略失败: {}", e.getMessage(), e);
            return MyJsonBean.error("配置缓存策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/clearCache/{tenantId}/{searchId}")
    @ApiOperation("清理缓存")
    public MyJsonBean clearCache(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            boolean result = archiveSearchService.clearCache(tenantId, searchId);
            return result ? MyJsonBean.success("缓存清理成功") : MyJsonBean.error("缓存清理失败");
        } catch (Exception e) {
            log.error("清理缓存失败: {}", e.getMessage(), e);
            return MyJsonBean.error("清理缓存失败: " + e.getMessage());
        }
    }

    @PostMapping("/warmupCache/{tenantId}/{searchId}")
    @ApiOperation("预热缓存")
    public MyJsonBean warmupCache(@PathVariable Long tenantId,
                                 @PathVariable Long searchId,
                                 @RequestBody List<String> keywords) {
        try {
            boolean result = archiveSearchService.warmupCache(tenantId, searchId, keywords);
            return result ? MyJsonBean.success("缓存预热成功") : MyJsonBean.error("缓存预热失败");
        } catch (Exception e) {
            log.error("预热缓存失败: {}", e.getMessage(), e);
            return MyJsonBean.error("预热缓存失败: " + e.getMessage());
        }
    }

    // ==================== 查询统计接口 ====================

    @GetMapping("/getByType/{tenantId}")
    @ApiOperation("根据检索类型查询")
    public MyJsonBean getArchiveSearchesByType(@PathVariable Long tenantId, @RequestParam String searchType) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getArchiveSearchesByType(tenantId, searchType);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据检索类型查询失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByStatus/{tenantId}")
    @ApiOperation("根据检索状态查询")
    public MyJsonBean getArchiveSearchesByStatus(@PathVariable Long tenantId, @RequestParam String searchStatus) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getArchiveSearchesByStatus(tenantId, searchStatus);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据检索状态查询失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByEngine/{tenantId}")
    @ApiOperation("根据检索引擎查询")
    public MyJsonBean getArchiveSearchesByEngine(@PathVariable Long tenantId, @RequestParam String searchEngine) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getArchiveSearchesByEngine(tenantId, searchEngine);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据检索引擎查询失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getActive/{tenantId}")
    @ApiOperation("查询活跃的检索配置")
    public MyJsonBean getActiveArchiveSearches(@PathVariable Long tenantId) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getActiveArchiveSearches(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询活跃检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getIndexing/{tenantId}")
    @ApiOperation("查询正在索引的检索配置")
    public MyJsonBean getIndexingArchiveSearches(@PathVariable Long tenantId) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getIndexingArchiveSearches(tenantId);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询正在索引的检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByAccuracyRange/{tenantId}")
    @ApiOperation("根据准确率范围查询")
    public MyJsonBean getArchiveSearchesByAccuracyRange(@PathVariable Long tenantId,
                                                       @RequestParam BigDecimal minAccuracy,
                                                       @RequestParam BigDecimal maxAccuracy) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getArchiveSearchesByAccuracyRange(tenantId, minAccuracy, maxAccuracy);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据准确率范围查询失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByResponseTimeRange/{tenantId}")
    @ApiOperation("根据响应时间范围查询")
    public MyJsonBean getArchiveSearchesByResponseTimeRange(@PathVariable Long tenantId,
                                                           @RequestParam Long minTime,
                                                           @RequestParam Long maxTime) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.getArchiveSearchesByResponseTimeRange(tenantId, minTime, maxTime);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("根据响应时间范围查询失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getLatestVersion/{tenantId}")
    @ApiOperation("查询最新版本的检索配置")
    public MyJsonBean getLatestVersionByType(@PathVariable Long tenantId, @RequestParam String searchType) {
        try {
            AsArchiveSearch result = archiveSearchService.getLatestVersionByType(tenantId, searchType);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询最新版本检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getBestPerformance/{tenantId}")
    @ApiOperation("查询性能最佳的检索配置")
    public MyJsonBean getBestPerformanceByType(@PathVariable Long tenantId, @RequestParam String searchType) {
        try {
            AsArchiveSearch result = archiveSearchService.getBestPerformanceByType(tenantId, searchType);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询性能最佳检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @GetMapping("/countByStatus/{tenantId}")
    @ApiOperation("统计检索状态分布")
    public MyJsonBean countBySearchStatus(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = archiveSearchService.countBySearchStatus(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("统计检索状态分布失败: {}", e.getMessage(), e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/countByType/{tenantId}")
    @ApiOperation("统计检索类型分布")
    public MyJsonBean countBySearchType(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = archiveSearchService.countBySearchType(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("统计检索类型分布失败: {}", e.getMessage(), e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/countByEngine/{tenantId}")
    @ApiOperation("统计检索引擎分布")
    public MyJsonBean countBySearchEngine(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = archiveSearchService.countBySearchEngine(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("统计检索引擎分布失败: {}", e.getMessage(), e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/getSearchTrend/{tenantId}")
    @ApiOperation("获取检索趋势")
    public MyJsonBean getSearchTrend(@PathVariable Long tenantId,
                                    @RequestParam String startDate,
                                    @RequestParam String endDate,
                                    @RequestParam(defaultValue = "day") String granularity) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Map<String, Object>> result = archiveSearchService.getSearchTrend(tenantId, start, end, granularity);
            return MyJsonBean.success("获取检索趋势成功", result);
        } catch (Exception e) {
            log.error("获取检索趋势失败: {}", e.getMessage(), e);
            return MyJsonBean.error("获取检索趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/getPopularKeywords/{tenantId}")
    @ApiOperation("获取热门关键词")
    public MyJsonBean getPopularKeywords(@PathVariable Long tenantId, @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> result = archiveSearchService.getPopularKeywords(tenantId, limit);
            return MyJsonBean.success("获取热门关键词成功", result);
        } catch (Exception e) {
            log.error("获取热门关键词失败: {}", e.getMessage(), e);
            return MyJsonBean.error("获取热门关键词失败: " + e.getMessage());
        }
    }

    @GetMapping("/getPerformanceMetrics/{tenantId}/{searchId}")
    @ApiOperation("获取性能指标")
    public MyJsonBean getPerformanceMetrics(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            Map<String, Object> result = archiveSearchService.getPerformanceMetrics(tenantId, searchId);
            return MyJsonBean.success("获取性能指标成功", result);
        } catch (Exception e) {
            log.error("获取性能指标失败: {}", e.getMessage(), e);
            return MyJsonBean.error("获取性能指标失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作接口 ====================

    @PostMapping("/batchCreate")
    @ApiOperation("批量创建检索配置")
    public MyJsonBean batchCreateArchiveSearches(@RequestBody List<AsArchiveSearch> archiveSearches) {
        try {
            List<AsArchiveSearch> result = archiveSearchService.batchCreateArchiveSearches(archiveSearches);
            return MyJsonBean.success("批量创建成功", result);
        } catch (Exception e) {
            log.error("批量创建检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/batchUpdateStatus/{tenantId}")
    @ApiOperation("批量更新检索状态")
    public MyJsonBean batchUpdateStatus(@PathVariable Long tenantId,
                                       @RequestBody List<Long> searchIds,
                                       @RequestParam String status) {
        try {
            boolean result = archiveSearchService.batchUpdateStatus(tenantId, searchIds, status);
            return result ? MyJsonBean.success("批量更新状态成功") : MyJsonBean.error("批量更新状态失败");
        } catch (Exception e) {
            log.error("批量更新检索状态失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量更新状态失败: " + e.getMessage());
        }
    }

    @PutMapping("/batchUpdateEngine/{tenantId}")
    @ApiOperation("批量更新检索引擎")
    public MyJsonBean batchUpdateEngine(@PathVariable Long tenantId,
                                       @RequestBody List<Long> searchIds,
                                       @RequestParam String engine) {
        try {
            boolean result = archiveSearchService.batchUpdateEngine(tenantId, searchIds, engine);
            return result ? MyJsonBean.success("批量更新引擎成功") : MyJsonBean.error("批量更新引擎失败");
        } catch (Exception e) {
            log.error("批量更新检索引擎失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量更新引擎失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batchDelete/{tenantId}")
    @ApiOperation("批量删除检索配置")
    public MyJsonBean batchDeleteArchiveSearches(@PathVariable Long tenantId, @RequestBody List<Long> searchIds) {
        try {
            boolean result = archiveSearchService.batchDeleteArchiveSearches(tenantId, searchIds);
            return result ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除检索配置失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchRebuildIndex/{tenantId}")
    @ApiOperation("批量重建索引")
    public MyJsonBean batchRebuildIndex(@PathVariable Long tenantId, @RequestBody List<Long> searchIds) {
        try {
            boolean result = archiveSearchService.batchRebuildIndex(tenantId, searchIds);
            return result ? MyJsonBean.success("批量重建索引成功") : MyJsonBean.error("批量重建索引失败");
        } catch (Exception e) {
            log.error("批量重建索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("批量重建索引失败: " + e.getMessage());
        }
    }

    // ==================== 数据管理接口 ====================

    @GetMapping("/export/{tenantId}")
    @ApiOperation("导出检索数据")
    public MyJsonBean exportArchiveSearchData(@PathVariable Long tenantId, @RequestBody List<Long> searchIds) {
        try {
            List<Map<String, Object>> result = archiveSearchService.exportArchiveSearchData(tenantId, searchIds);
            return MyJsonBean.success("导出数据成功", result);
        } catch (Exception e) {
            log.error("导出检索数据失败: {}", e.getMessage(), e);
            return MyJsonBean.error("导出数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/import/{tenantId}")
    @ApiOperation("导入检索数据")
    public MyJsonBean importArchiveSearchData(@PathVariable Long tenantId, @RequestBody List<Map<String, Object>> searchData) {
        try {
            boolean result = archiveSearchService.importArchiveSearchData(tenantId, searchData);
            return result ? MyJsonBean.success("导入数据成功") : MyJsonBean.error("导入数据失败");
        } catch (Exception e) {
            log.error("导入检索数据失败: {}", e.getMessage(), e);
            return MyJsonBean.error("导入数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/cleanupExpiredData/{tenantId}")
    @ApiOperation("清理过期数据")
    public MyJsonBean cleanupExpiredData(@PathVariable Long tenantId, @RequestParam String expiredDate) {
        try {
            LocalDateTime expired = LocalDateTime.parse(expiredDate);
            boolean result = archiveSearchService.cleanupExpiredData(tenantId, expired);
            return result ? MyJsonBean.success("清理过期数据成功") : MyJsonBean.error("清理过期数据失败");
        } catch (Exception e) {
            log.error("清理过期数据失败: {}", e.getMessage(), e);
            return MyJsonBean.error("清理过期数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/cleanupInvalidIndexes/{tenantId}")
    @ApiOperation("清理无效索引")
    public MyJsonBean cleanupInvalidIndexes(@PathVariable Long tenantId) {
        try {
            boolean result = archiveSearchService.cleanupInvalidIndexes(tenantId);
            return result ? MyJsonBean.success("清理无效索引成功") : MyJsonBean.error("清理无效索引失败");
        } catch (Exception e) {
            log.error("清理无效索引失败: {}", e.getMessage(), e);
            return MyJsonBean.error("清理无效索引失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护接口 ====================

    @GetMapping("/getSystemOverview/{tenantId}")
    @ApiOperation("获取系统概览")
    public MyJsonBean getSystemOverview(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = archiveSearchService.getSystemOverview(tenantId);
            return MyJsonBean.success("获取系统概览成功", result);
        } catch (Exception e) {
            log.error("获取系统概览失败: {}", e.getMessage(), e);
            return MyJsonBean.error("获取系统概览失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateReport/{tenantId}/{searchId}")
    @ApiOperation("生成检索报告")
    public MyJsonBean generateSearchReport(@PathVariable Long tenantId, @PathVariable Long searchId) {
        try {
            Map<String, Object> result = archiveSearchService.generateSearchReport(tenantId, searchId);
            return MyJsonBean.success("生成检索报告成功", result);
        } catch (Exception e) {
            log.error("生成检索报告失败: {}", e.getMessage(), e);
            return MyJsonBean.error("生成检索报告失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkHealth/{tenantId}")
    @ApiOperation("检查检索健康状态")
    public MyJsonBean checkSearchHealth(@PathVariable Long tenantId) {
        try {
            List<Map<String, Object>> result = archiveSearchService.checkSearchHealth(tenantId);
            return MyJsonBean.success("健康检查成功", result);
        } catch (Exception e) {
            log.error("检查检索健康状态失败: {}", e.getMessage(), e);
            return MyJsonBean.error("健康检查失败: " + e.getMessage());
        }
    }
}
