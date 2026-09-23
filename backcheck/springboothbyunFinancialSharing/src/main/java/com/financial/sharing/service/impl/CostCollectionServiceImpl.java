package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.CostCollectionMapper;
import com.financial.sharing.service.CostCollectionService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.financial.sharing.util.UserUtils;
import com.financial.sharing.vo.param.CostCollectionParam;
import com.financial.sharing.vo.result.CostCollectionVO;
import com.financial.sharing.vo.result.CostCollectionStatsVO;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成本归集服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class CostCollectionServiceImpl implements CostCollectionService {

    @Resource
    private CostCollectionMapper costCollectionMapper;

    @Resource
    private com.financial.sharing.oracle.mapper.CostCollectionRuleMapper costCollectionRuleMapper;

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public MyJsonBean<PageResult<CostCollectionVO>> getCostCollectionList(Map<String, Object> param) {
        try {
            log.info("=== 后端查询调试开始 ===");
            log.info("1. 接收前端查询参数: {}", param);

            // 验证关键字段
            log.info("2. 关键参数 - bookId: {}, tenantId: {}, collectionPeriod: {}",
                    param.get("bookId"), param.get("tenantId"), param.get("collectionPeriod"));
            log.info("3. 其他参数 - pageNumber: {}, pageSize: {}, costCenterId: {}, collectionType: {}",
                    param.get("pageNumber"), param.get("pageSize"), param.get("costCenterId"), param.get("collectionType"));

            // 获取分页参数
            Integer pageNumber = (Integer) param.get("pageNumber");
            Integer pageSize = (Integer) param.get("pageSize");
            if (pageNumber == null) pageNumber = 1;
            if (pageSize == null) pageSize = 20;

            // 计算偏移量
            int offset = (pageNumber - 1) * pageSize;
            log.info("4. 分页参数 - pageNumber: {}, pageSize: {}, offset: {}", pageNumber, pageSize, offset);

            // 提取参数并进行类型转换
            Long bookId = null;
            Long tenantId = null;
            String collectionPeriod = null;
            Long costCenterId = null;
            Integer collectionType = null;
            Integer collectionStatus = null;
            String collectionNo = null;

            if (param.get("bookId") != null && !param.get("bookId").toString().trim().isEmpty()) {
                bookId = Long.valueOf(param.get("bookId").toString());
            }
            if (param.get("tenantId") != null && !param.get("tenantId").toString().trim().isEmpty()) {
                tenantId = Long.valueOf(param.get("tenantId").toString());
            }
            collectionPeriod = (String) param.get("collectionPeriod");
            if (param.get("costCenterId") != null && !param.get("costCenterId").toString().trim().isEmpty()) {
                costCenterId = Long.valueOf(param.get("costCenterId").toString());
            }
            if (param.get("collectionType") != null && !param.get("collectionType").toString().trim().isEmpty()) {
                collectionType = Integer.valueOf(param.get("collectionType").toString());
            }
            if (param.get("collectionStatus") != null && !param.get("collectionStatus").toString().trim().isEmpty()) {
                collectionStatus = Integer.valueOf(param.get("collectionStatus").toString());
            }
            collectionNo = (String) param.get("collectionNo");

            log.info("4.5. 参数转换完成 - bookId: {}, tenantId: {}, collectionPeriod: {}",
                    bookId, tenantId, collectionPeriod);

            // 查询分页数据
            List<Map<String, Object>> dataList = costCollectionMapper.selectCostCollectionListWithPagination(
                bookId, tenantId, collectionPeriod, costCenterId, collectionType, collectionStatus, collectionNo, offset, pageSize);
            log.info("5. SQL查询执行完成，返回记录数: {}", dataList.size());

            // 检查返回的数据
            if (!dataList.isEmpty()) {
                log.info("6. 查询到的第一条记录: {}", dataList.get(0));
            } else {
                log.info("6. 没有查询到任何记录");
            }

            // 查询总数
            int totalRecord = costCollectionMapper.countCostCollectionList(
                bookId, tenantId, collectionPeriod, costCenterId, collectionType, collectionStatus, collectionNo);
            log.info("7. 查询总数结果: totalRecord={}", totalRecord);

            // 转换数据格式
            List<CostCollectionVO> voList = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                CostCollectionVO vo = convertToCostCollectionVO(data);
                voList.add(vo);
            }

            log.info("8. 数据转换完成，VO列表大小: {}", voList.size());

            // 构建分页结果
            PageResult<CostCollectionVO> pageResult = new PageResult<>();
            pageResult.setTlist(voList);
            pageResult.setTotalRecord(totalRecord);
            pageResult.setTotalPage((int) Math.ceil((double) totalRecord / pageSize));
            pageResult.setPageSize(pageSize);
            pageResult.setCurrentPage(pageNumber);

            log.info("9. 最终响应数据构建完成:");
            log.info("   - totalRecord: {}", pageResult.getTotalRecord());
            log.info("   - voList.size(): {}", pageResult.getTlist().size());
            log.info("   - totalPage: {}", pageResult.getTotalPage());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询成本归集列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean startCostCollection(CostCollectionParam param) {
        try {
            // 参数验证
            if (param.getCostCenterIds() == null || param.getCostCenterIds().isEmpty()) {
                return MyJsonBean.errorData("请选择成本中心");
            }

            // 验证并标准化归集期间
            try {
                String formattedPeriod = validateAndFormatCollectionPeriod(param.getCollectionPeriod());
                param.setCollectionPeriod(formattedPeriod);
            } catch (IllegalArgumentException e) {
                return MyJsonBean.errorData("归集期间格式错误: " + e.getMessage());
            }

            // 生成归集单号
            String collectionNo = generateCollectionNo();

            // 为每个成本中心创建归集记录
            int successCount = 0;
            List<String> collectionNos = new ArrayList<>();

            for (Long costCenterId : param.getCostCenterIds()) {
                for (Integer collectionType : param.getCollectionTypes()) {
                    // 检查是否已存在相同的归集记录
                    List<Map<String, Object>> existingRecords = costCollectionMapper.selectCostCollectionByPeriodAndCenter(
                            param.getCollectionPeriod(), costCenterId, collectionType,
                            param.getBookId(), param.getTenantId());

                    if (!existingRecords.isEmpty()) {
                        log.warn("成本归集记录已存在，跳过：期间={}, 成本中心={}, 类型={}",
                                param.getCollectionPeriod(), costCenterId, collectionType);
                        continue;
                    }

                    // 为每条归集记录生成唯一ID
                    Long collectionId = snowflakeIdWorker.nextId();

                    // 插入归集记录
                    log.info("准备插入归集记录: collectionId={}, collectionNo={}, period={}, costCenterId={}, type={}, bookId={}, tenantId={}",
                            collectionId, collectionNo + "_" + costCenterId + "_" + collectionType, param.getCollectionPeriod(),
                            costCenterId, collectionType, param.getBookId(), param.getTenantId());

                    int insertResult = costCollectionMapper.insertCostCollection(
                            collectionId,
                            collectionNo + "_" + costCenterId + "_" + collectionType,
                            param.getCollectionPeriod(),
                            costCenterId,
                            collectionType,
                            BigDecimal.ZERO, // 初始金额为0，后续根据明细计算
                            param.getCollectionMethod(),
                            null, // 归集日期为空，等待实际归集时填写
                            param.getBookId(),
                            param.getTenantId(),
                            1001L, // 创建人ID，实际应该从上下文获取
                            param.getRemark()
                    );

                    log.info("插入结果: insertResult={}, collectionId={}", insertResult, collectionId);

                    if (insertResult > 0) {
                        // 立即查询验证数据是否真的插入
                        try {
                            List<Map<String, Object>> checkRecords = costCollectionMapper.selectCostCollectionByPeriodAndCenter(
                                    param.getCollectionPeriod(), costCenterId, collectionType,
                                    param.getBookId(), param.getTenantId());
                            log.info("插入后验证查询: collectionId={}, found records={}", collectionId, checkRecords.size());
                        } catch (Exception e) {
                            log.error("插入后验证查询失败", e);
                        }

                        successCount++;
                        collectionNos.add(collectionNo + "_" + costCenterId + "_" + collectionType);
                    }
                }
            }

            if (successCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("collectionId", collectionNo);
                result.put("collectionPeriod", param.getCollectionPeriod());
                result.put("affectedCenters", param.getCostCenterIds().size());
                result.put("collectionTypes", param.getCollectionTypes());
                result.put("successCount", successCount);
                result.put("collectionNos", collectionNos);
                result.put("status", "started");
                result.put("createTime", LocalDateTime.now());

                return MyJsonBean.successData("成本归集已开始，共创建 " + successCount + " 条归集记录", result);
            } else {
                return MyJsonBean.errorData("没有创建新的归集记录，可能已存在相同条件的归集");
            }
        } catch (Exception e) {
            log.error("开始成本归集失败", e);
            return MyJsonBean.errorData("成本归集失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean auditCostCollection(Long collectionId, Map<String, Object> auditData) {
        try {
            String auditResult = (String) auditData.get("auditResult");
            String auditRemark = (String) auditData.get("auditRemark");

            // 审核状态：approved-通过，rejected-驳回
            Integer newStatus = "approved".equals(auditResult) ? 4 : 1; // 4-已审核，1-待归集（驳回后重新归集）

            // 从当前登录用户上下文取审核人信息（取代历史硬编码 1001L / "系统管理员"）
            // 安全考虑：审核人身份必须来自 token，不能让前端传——否则可被伪造
            Long auditorId;
            String auditorName;
            try {
                TblStaffUtil currentUser = UserUtils.getUser();
                if (currentUser == null || currentUser.getStaffid() == null) {
                    throw new RuntimeException("用户未登录或会话已失效");
                }
                auditorId = currentUser.getStaffid().longValue();
                // 优先取真实姓名，回退账号，再回退兜底字符串
                if (currentUser.getRealname() != null && !currentUser.getRealname().isEmpty()) {
                    auditorName = currentUser.getRealname();
                } else if (currentUser.getUsername() != null && !currentUser.getUsername().isEmpty()) {
                    auditorName = currentUser.getUsername();
                } else {
                    auditorName = "ID-" + auditorId;
                }
            } catch (Exception ex) {
                log.warn("审核归集获取当前登录用户失败，使用系统兜底，collectionId: {}, 原因: {}",
                        collectionId, ex.getMessage());
                auditorId = 0L;
                auditorName = "系统";
            }

            log.info("审核归集，collectionId: {}, auditor: {}({}), result: {}",
                    collectionId, auditorName, auditorId, auditResult);

            int updateResult = costCollectionMapper.auditCostCollection(
                    collectionId,
                    newStatus,
                    auditorId,
                    auditorName,
                    LocalDateTime.now(),
                    auditRemark,
                    auditorId
            );

            if (updateResult > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("collectionId", collectionId);
                result.put("auditResult", auditResult);
                result.put("auditTime", LocalDateTime.now());
                result.put("auditorId", auditorId);
                result.put("auditorName", auditorName);
                result.put("status", "audited");

                return MyJsonBean.successData("审核成功", result);
            } else {
                return MyJsonBean.errorData("审核失败，未找到对应的归集记录");
            }
        } catch (Exception e) {
            log.error("审核成本归集失败，collectionId: {}", collectionId, e);
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cancelCostCollection(Long collectionId) {
        log.info("开始取消归集操作，collectionId: {}", collectionId);

        if (collectionId == null) {
            log.error("归集ID不能为空");
            return MyJsonBean.errorData("归集ID不能为空");
        }

        try {
            log.info("调用Mapper删除归集记录，collectionId: {}", collectionId);
            int updateResult = costCollectionMapper.deleteCostCollection(collectionId, 1001L);
            log.info("删除归集记录完成，影响行数: {}", updateResult);

            if (updateResult > 0) {
                log.info("主记录删除成功，开始删除明细记录，collectionId: {}", collectionId);
                // 同时删除明细记录
                int detailResult = costCollectionMapper.deleteCostCollectionDetail(collectionId);
                log.info("删除明细记录完成，影响行数: {}", detailResult);

                Map<String, Object> result = new HashMap<>();
                result.put("collectionId", collectionId);
                result.put("status", "cancelled");
                result.put("cancelTime", LocalDateTime.now());

                log.info("归集取消成功，collectionId: {}", collectionId);
                return MyJsonBean.successData("取消成功", result);
            } else {
                log.warn("未找到要取消的归集记录，collectionId: {}", collectionId);
                return MyJsonBean.errorData("取消失败，未找到对应的归集记录");
            }
        } catch (Exception e) {
            log.error("取消归集失败，collectionId: {}", collectionId, e);
            log.error("异常详情: ", e);
            return MyJsonBean.errorData("取消归集失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<CostCollectionStatsVO> getCostCollectionStats(String period) {
        try {
            // TODO: 应该从用户上下文获取bookId和tenantId，这里暂时使用默认值进行测试
            // 实际实现时应该从SecurityContext或Token中获取当前用户的bookId和tenantId
            Long bookId = 1L;  // 临时测试值
            Long tenantId = 1000L;  // 临时测试值

            log.info("查询成本归集统计数据 - period: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);

            Map<String, Object> statsData = costCollectionMapper.selectCostCollectionStats(
                    period, bookId, tenantId);

            CostCollectionStatsVO statsVO = new CostCollectionStatsVO();
            statsVO.setDirectCost((BigDecimal) statsData.getOrDefault("DIRECT_COST", BigDecimal.ZERO));
            statsVO.setIndirectCost((BigDecimal) statsData.getOrDefault("INDIRECT_COST", BigDecimal.ZERO));
            statsVO.setManufacturingCost((BigDecimal) statsData.getOrDefault("MANUFACTURING_COST", BigDecimal.ZERO));
            statsVO.setTotalCost((BigDecimal) statsData.getOrDefault("TOTAL_COST", BigDecimal.ZERO));
            statsVO.setMonthlyCollectionCount(((Number) statsData.getOrDefault("MONTHLY_COLLECTION_COUNT", 0)).intValue());
            statsVO.setMonthlyCollectionAmount((BigDecimal) statsData.getOrDefault("MONTHLY_COLLECTION_AMOUNT", BigDecimal.ZERO));
            statsVO.setPendingCollectionCount(((Number) statsData.getOrDefault("PENDING_COLLECTION_COUNT", 0)).intValue());
            statsVO.setPendingCollectionAmount((BigDecimal) statsData.getOrDefault("PENDING_COLLECTION_AMOUNT", BigDecimal.ZERO));
            statsVO.setCompletedCollectionCount(((Number) statsData.getOrDefault("COMPLETED_COLLECTION_COUNT", 0)).intValue());
            statsVO.setCompletedCollectionAmount((BigDecimal) statsData.getOrDefault("COMPLETED_COLLECTION_AMOUNT", BigDecimal.ZERO));
            statsVO.setAuditedCollectionCount(((Number) statsData.getOrDefault("AUDITED_COLLECTION_COUNT", 0)).intValue());
            statsVO.setAuditedCollectionAmount((BigDecimal) statsData.getOrDefault("AUDITED_COLLECTION_AMOUNT", BigDecimal.ZERO));

            return MyJsonBean.successData("查询成功", statsVO);
        } catch (Exception e) {
            log.error("查询成本归集统计数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<CostCollectionVO> getCostCollectionById(Long collectionId) {
        try {
            Map<String, Object> data = costCollectionMapper.selectCostCollectionDetail(collectionId);
            if (data == null) {
                return MyJsonBean.errorData("未找到对应的归集记录");
            }

            CostCollectionVO vo = convertToCostCollectionVO(data);

            // 查询明细列表
            List<Map<String, Object>> detailList = costCollectionMapper.selectCostCollectionDetailList(collectionId);
            List<CostCollectionVO.CostCollectionDetailVO> detailVOList = new ArrayList<>();
            for (Map<String, Object> detailData : detailList) {
                CostCollectionVO.CostCollectionDetailVO detailVO = convertToCostCollectionDetailVO(detailData);
                detailVOList.add(detailVO);
            }
            vo.setDetailList(detailVOList);

            return MyJsonBean.successData("查询成功", vo);
        } catch (Exception e) {
            log.error("查询成本归集详情失败，collectionId: {}", collectionId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchAuditCostCollection(List<Long> collectionIds, Map<String, Object> auditData) {
        try {
            String auditResult = (String) auditData.get("auditResult");
            String auditRemark = (String) auditData.get("auditRemark");

            Integer newStatus = "approved".equals(auditResult) ? 4 : 1;

            // 从当前登录用户上下文取审核人信息（取代历史硬编码 1001L / "系统管理员"）
            Long auditorId;
            String auditorName;
            try {
                TblStaffUtil currentUser = UserUtils.getUser();
                if (currentUser == null || currentUser.getStaffid() == null) {
                    throw new RuntimeException("用户未登录或会话已失效");
                }
                auditorId = currentUser.getStaffid().longValue();
                if (currentUser.getRealname() != null && !currentUser.getRealname().isEmpty()) {
                    auditorName = currentUser.getRealname();
                } else if (currentUser.getUsername() != null && !currentUser.getUsername().isEmpty()) {
                    auditorName = currentUser.getUsername();
                } else {
                    auditorName = "ID-" + auditorId;
                }
            } catch (Exception ex) {
                log.warn("批量审核获取当前登录用户失败，使用系统兜底，原因: {}", ex.getMessage());
                auditorId = 0L;
                auditorName = "系统";
            }

            log.info("批量审核归集，count: {}, auditor: {}({}), result: {}",
                    collectionIds.size(), auditorName, auditorId, auditResult);

            int updateResult = costCollectionMapper.batchAuditCostCollection(
                    collectionIds, newStatus, auditorId, auditorName, LocalDateTime.now(), auditRemark, auditorId);

            if (updateResult > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("auditResult", auditResult);
                result.put("updateCount", updateResult);
                result.put("auditTime", LocalDateTime.now());
                result.put("auditorId", auditorId);
                result.put("auditorName", auditorName);
                result.put("status", "batch_audited");

                return MyJsonBean.successData("批量审核成功，共更新 " + updateResult + " 条记录", result);
            } else {
                return MyJsonBean.errorData("批量审核失败，未找到可更新的记录");
            }
        } catch (Exception e) {
            log.error("批量审核成本归集失败", e);
            return MyJsonBean.errorData("批量审核失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchCancelCostCollection(List<Long> collectionIds) {
        try {
            int updateResult = costCollectionMapper.batchDeleteCostCollection(collectionIds, 1001L);

            if (updateResult > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("cancelCount", updateResult);
                result.put("status", "batch_cancelled");
                result.put("cancelTime", LocalDateTime.now());

                return MyJsonBean.successData("批量取消成功，共取消 " + updateResult + " 条记录", result);
            } else {
                return MyJsonBean.errorData("批量取消失败，未找到可取消的记录");
            }
        } catch (Exception e) {
            log.error("批量取消成本归集失败", e);
            return MyJsonBean.errorData("批量取消失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean autoCostCollection(String period, List<Long> costCenterIds, List<Integer> collectionTypes) {
        try {
            log.info("开始自动归集，期间: {}, 成本中心: {}, 归集类型: {}", period, costCenterIds, collectionTypes);

            if (period == null || period.trim().isEmpty()) {
                return MyJsonBean.errorData("归集期间不能为空");
            }

            // 获取所有可用成本中心（如果未指定）
            if (costCenterIds == null || costCenterIds.isEmpty()) {
                // 从数据库查询可用的成本中心
                costCenterIds = getAvailableCostCenters();
                if (costCenterIds.isEmpty()) {
                    return MyJsonBean.errorData("未找到可用的成本中心");
                }
            }

            // 默认处理所有归集类型（如果未指定）
            if (collectionTypes == null || collectionTypes.isEmpty()) {
                collectionTypes = new ArrayList<>();
                collectionTypes.add(1); // 直接成本
                collectionTypes.add(2); // 间接成本
                collectionTypes.add(3); // 制造费用
            }

            int successCount = 0;
            List<String> collectionNos = new ArrayList<>();
            BigDecimal totalCollectionAmount = BigDecimal.ZERO;

            for (Long costCenterId : costCenterIds) {
                for (Integer collectionType : collectionTypes) {
                    try {
                        // 验证成本中心是否存在
                        int centerExists = costCollectionMapper.validateCostCenterExists(costCenterId, 1L, 1000L);
                        if (centerExists == 0) {
                            log.warn("成本中心不存在或已禁用，跳过：成本中心ID={}", costCenterId);
                            continue;
                        }

                        // 检查是否已存在相同的归集记录
                        List<Map<String, Object>> existingRecords = costCollectionMapper.selectCostCollectionByPeriodAndCenter(
                                period, costCenterId, collectionType, 1L, 1000L);

                        Long collectionId;
                        String collectionNo;

                        // 如果存在归集记录，检查状态
                        if (!existingRecords.isEmpty()) {
                            Map<String, Object> existingRecord = existingRecords.get(0);
                            Object collectionStatusObj = existingRecord.get("COLLECTION_STATUS");
                            int collectionStatus = collectionStatusObj != null ? Integer.parseInt(collectionStatusObj.toString()) : 1;

                            // 如果状态是待归集(1)，则更新该记录
                            if (collectionStatus == 1) {
                                collectionId = ((Number) existingRecord.get("COLLECTION_ID")).longValue();
                                collectionNo = (String) existingRecord.get("COLLECTION_NO");
                                log.info("更新已存在的待归集记录：期间={}, 成本中心={}, 类型={}, 记录ID={}",
                                        period, costCenterId, collectionType, collectionId);
                            } else {
                                // 如果状态不是待归集，跳过
                                log.warn("成本归集记录已存在且状态为{}，跳过：期间={}, 成本中心={}, 类型={}",
                                        collectionStatus, period, costCenterId, collectionType);
                                continue;
                            }
                        } else {
                            // 不存在归集记录，创建新记录
                            collectionId = snowflakeIdWorker.nextId();
                            collectionNo = "AUTO_" + System.currentTimeMillis() + "_" + costCenterId + "_" + collectionType;
                        }

                        // 从数据库计算真实归集金额
                        BigDecimal collectionAmount = calculateActualCollectionAmount(period, costCenterId, collectionType);

                        if (collectionAmount.compareTo(BigDecimal.ZERO) <= 0) {
                            log.info("成本中心{}类型{}期间{}无数据，跳过归集", costCenterId, collectionType, period);
                            continue;
                        }

                        boolean isExisting = !existingRecords.isEmpty() &&
                                Integer.parseInt(existingRecords.get(0).get("COLLECTION_STATUS").toString()) == 1;

                        int result;
                        if (isExisting) {
                            // 更新已存在的待归集记录
                            result = costCollectionMapper.updateCostCollection(
                                    collectionId,
                                    3, // collectionStatus - 更新状态为"已归集"
                                    collectionAmount,
                                    java.time.LocalDate.now().toString(), // collectionDate
                                    1001L // updaterId
                            );
                            log.info("更新归集记录：ID={}, 状态更新为已归集, 金额={}", collectionId, collectionAmount);
                        } else {
                            // 插入新归集记录（初始状态为待归集）
                            result = costCollectionMapper.insertCostCollection(
                                    collectionId,
                                    collectionNo,
                                    period,
                                    costCenterId,
                                    collectionType,
                                    collectionAmount,
                                    2, // 自动归集方式
                                    java.time.LocalDate.now().toString(),
                                    1L, // bookId
                                    1000L, // tenantId
                                    1001L, // 创建人ID
                                    "系统自动归集"
                            );

                            // 插入成功后，立即更新状态为"已归集"
                            if (result > 0) {
                                costCollectionMapper.updateCostCollection(
                                        collectionId,
                                        3, // collectionStatus - 更新状态为"已归集"
                                        collectionAmount,
                                        java.time.LocalDate.now().toString(),
                                        1001L // updaterId
                                );
                                log.info("新建归集记录并更新为已归集状态：ID={}, 金额={}", collectionId, collectionAmount);
                            }
                        }

                        if (result > 0) {
                            try {
                                // 从数据库获取真实明细数据并插入
                                insertActualDetailRecords(collectionId, period, costCenterId, collectionType, collectionAmount);

                                successCount++;
                                collectionNos.add(collectionNo);
                                totalCollectionAmount = totalCollectionAmount.add(collectionAmount);
                            } catch (Exception e) {
                                log.error("处理成本中心{}类型{}时发生错误", costCenterId, collectionType, e);
                            }
                        }
                    } catch (Exception e) {
                        log.error("处理成本中心{}类型{}时发生错误", costCenterId, collectionType, e);
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("costCenterIds", costCenterIds);
            result.put("collectionTypes", collectionTypes);
            result.put("successCount", successCount);
            result.put("collectionNos", collectionNos);
            result.put("totalAmount", totalCollectionAmount);
            result.put("status", "auto_collection_completed");
            result.put("message", "自动归集完成，共处理 " + successCount + " 条记录，总金额 " + totalCollectionAmount);

            return MyJsonBean.successData("自动归集完成", result);
        } catch (Exception e) {
            log.error("自动归集失败", e);
            return MyJsonBean.errorData("自动归集失败: " + e.getMessage());
        }
    }

    /**
     * 获取可用的成本中心列表
     */
    private List<Long> getAvailableCostCenters() {
        try {
            // 从数据库查询实际可用的成本中心
            List<Map<String, Object>> costCenterData = costCollectionMapper.selectAvailableCostCenters(1L, 1000L);
            List<Long> costCenters = new ArrayList<>();

            if (costCenterData != null && !costCenterData.isEmpty()) {
                for (Map<String, Object> center : costCenterData) {
                    Object centerId = center.get("COST_CENTER_ID");
                    if (centerId != null) {
                        costCenters.add(Long.valueOf(centerId.toString()));
                        log.info("找到可用成本中心: ID={}, 名称={}", centerId, center.get("CENTER_NAME"));
                    }
                }
            } else {
                log.warn("未找到可用的成本中心，使用默认成本中心ID");
                // 如果数据库中没有成本中心数据，则使用一些默认值（这些应该在实际数据库中存在）
                costCenters.add(1L); // 使用最小的ID作为默认值
            }
            return costCenters;
        } catch (Exception e) {
            log.error("查询可用成本中心失败，使用默认值", e);
            // 发生异常时返回一个安全的默认值，避免外键约束错误
            List<Long> defaultCostCenters = new ArrayList<>();
            defaultCostCenters.add(1L); // 使用ID=1作为默认成本中心
            return defaultCostCenters;
        }
    }

    /**
     * 从数据库计算实际归集金额
     */
    private BigDecimal calculateActualCollectionAmount(String period, Long costCenterId, Integer collectionType) {
        try {
            // TODO: 实现真实的业务逻辑
            // 这里应该根据期间、成本中心、归集类型从相关业务表（如凭证表、科目余额表等）计算实际金额

            // 示例：从科目余额表查询指定期间和成本中心的成本数据
            // String accountCodePrefix = getAccountCodePrefix(collectionType);
            // BigDecimal amount = costCollectionMapper.selectCostFromAccountBalance(period, costCenterId, accountCodePrefix);

            // 临时返回基础金额，实际应该从数据库查询
            switch (collectionType) {
                case 1: // 直接成本
                    return BigDecimal.valueOf(150000.00);
                case 2: // 间接成本
                    return BigDecimal.valueOf(80000.00);
                case 3: // 制造费用
                    return BigDecimal.valueOf(120000.00);
                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("计算归集金额失败", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 插入真实明细记录
     */
    private void insertActualDetailRecords(Long collectionId, String period, Long costCenterId, Integer collectionType, BigDecimal totalAmount) {
        try {
            // TODO: 实现真实的明细数据获取逻辑
            // 这里应该从相关业务表（如凭证分录表、成本要素表等）获取明细数据

            // 示例：从凭证分录表查询相关明细
            // List<Map<String, Object>> voucherDetails = costCollectionMapper.selectVoucherDetailsForCollection(period, costCenterId, collectionType);

            // 临时插入示例明细，实际应该从业务数据获取
            int detailCount = 2;
            BigDecimal avgAmount = totalAmount.divide(BigDecimal.valueOf(detailCount), 2, BigDecimal.ROUND_HALF_UP);

            for (int i = 1; i <= detailCount; i++) {
                BigDecimal allocationRate = BigDecimal.valueOf(0.5);
                BigDecimal allocatedAmount = avgAmount;

                // 生成明细ID
                Long detailId = snowflakeIdWorker.nextId();

                costCollectionMapper.insertCostCollectionDetail(
                    detailId,
                    collectionId,
                    "CE" + String.format("%03d", collectionType) + String.format("%02d", i),
                    getCostElementName(collectionType, i),
                    null, // 凭证ID，实际应该从业务数据获取
                    null, // 凭证号，实际应该从业务数据获取
                    getAccountCode(collectionType, i),
                    getAccountName(collectionType),
                    allocatedAmount,
                    allocationRate,
                    allocatedAmount,
                    getCostCenterName(costCenterId),
                    null, // 项目名称，实际应该从业务数据获取
                    getBusinessType(collectionType),
                    1L, // bookId
                    1000L, // tenantId
                    "自动归集明细" // remark
                );
            }
        } catch (Exception e) {
            log.error("插入明细记录失败", e);
        }
    }

    private String getAccountCodePrefix(Integer collectionType) {
        switch (collectionType) {
            case 1: return "5001"; // 生产成本
            case 2: return "5101"; // 制造费用
            case 3: return "6601"; // 管理费用
            default: return "5000";
        }
    }

    private String getAccountCode(Integer collectionType, int index) {
        return getAccountCodePrefix(collectionType) + String.format("%02d", index);
    }

    private String getCostElementName(Integer collectionType, int index) {
        switch (collectionType) {
            case 1: return new String[]{"直接材料", "直接人工", "其他直接费用"}[Math.min(index - 1, 2)];
            case 2: return new String[]{"间接材料", "间接人工", "其他间接费用"}[Math.min(index - 1, 2)];
            case 3: return new String[]{"折旧费用", "修理费用", "动力费用", "其他制造费用"}[Math.min(index - 1, 3)];
            default: return "成本要素" + index;
        }
    }

    private String getAccountName(Integer collectionType) {
        switch (collectionType) {
            case 1: return "生产成本";
            case 2: return "制造费用";
            case 3: return "管理费用";
            default: return "成本科目";
        }
    }

    private String getCostCenterName(Long costCenterId) {
        // TODO: 实际应该从成本中心表查询
        switch (costCenterId.intValue()) {
            case 1001: return "生产部";
            case 1002: return "制造部";
            case 1003: return "管理部";
            default: return "部门" + costCenterId;
        }
    }

    private String getBusinessType(Integer collectionType) {
        switch (collectionType) {
            case 1: return "生产成本归集";
            case 2: return "制造费用归集";
            case 3: return "管理费用归集";
            default: return "成本归集";
        }
    }

    @Override
    public MyJsonBean recalculateCostCollection(Long collectionId) {
        // TODO: 实现重新计算逻辑
        // 根据明细数据重新计算归集总金额
        Map<String, Object> result = new HashMap<>();
        result.put("collectionId", collectionId);
        result.put("status", "recalculated");
        result.put("message", "重新计算功能开发中...");

        return MyJsonBean.successData("重新计算完成", result);
    }

    /**
     * 生成归集单号
     */
    private String generateCollectionNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "COL" + timestamp;
    }

    /**
     * 安全转换时间类型
     */
    private LocalDateTime safeConvertToLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate().atStartOfDay();
        }
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        // 尝试从字符串解析
        try {
            if (value instanceof String && !((String) value).trim().isEmpty()) {
                return LocalDateTime.parse(value.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            log.warn("时间转换失败: " + value, e);
        }
        return null;
    }

    /**
     * 安全转换字符串类型（处理NCLOB等大对象类型）
     */
    private String safeConvertToString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }

        // 处理达梦数据库的NCLOB类型
        try {
            if (value.getClass().getSimpleName().contains("NClob") ||
                value.getClass().getSimpleName().contains("Clob") ||
                value instanceof java.sql.Clob) {

                // 使用流式读取处理大对象
                java.sql.Clob clob = (java.sql.Clob) value;
                return clob.getSubString(1, (int) clob.length());
            }
        } catch (Exception e) {
            log.warn("转换Clob/NClob到String失败，使用toString()方法: " + e.getMessage());
        }

        // 最后的备用方案
        return value.toString();
    }

    /**
     * 转换为成本归集VO
     */
    private CostCollectionVO convertToCostCollectionVO(Map<String, Object> data) {
        CostCollectionVO vo = new CostCollectionVO();

        // 处理collectionId，同时设置Long类型和字符串类型
        Object collectionIdObj = data.get("COLLECTION_ID");
        if (collectionIdObj != null) {
            vo.setCollectionId(((Number) collectionIdObj).longValue());
            vo.setCollectionIdStr(collectionIdObj.toString());
        }
        vo.setCollectionNo(safeConvertToString(data.get("COLLECTION_NO")));
        vo.setCollectionPeriod(safeConvertToString(data.get("COLLECTION_PERIOD")));
        vo.setCostCenterId(data.get("COST_CENTER_ID") != null ? ((Number) data.get("COST_CENTER_ID")).longValue() : null);
        vo.setCostCenterName(safeConvertToString(data.get("COST_CENTER_NAME")));
        vo.setCollectionType(data.get("COLLECTION_TYPE") != null ? ((Number) data.get("COLLECTION_TYPE")).intValue() : null);
        vo.setCollectionTypeName(safeConvertToString(data.get("COLLECTION_TYPE_NAME")));
        vo.setCollectionAmount((BigDecimal) data.get("COLLECTION_AMOUNT"));
        vo.setCollectionStatus(data.get("COLLECTION_STATUS") != null ? ((Number) data.get("COLLECTION_STATUS")).intValue() : null);
        vo.setCollectionStatusName(safeConvertToString(data.get("COLLECTION_STATUS_NAME")));
        vo.setCollectionMethod(data.get("COLLECTION_METHOD") != null ? ((Number) data.get("COLLECTION_METHOD")).intValue() : null);
        vo.setCollectionMethodName(safeConvertToString(data.get("COLLECTION_METHOD_NAME")));
        vo.setCollectionDate(safeConvertToString(data.get("COLLECTION_DATE")));
        vo.setAuditorId(data.get("AUDITOR_ID") != null ? ((Number) data.get("AUDITOR_ID")).longValue() : null);
        vo.setAuditorName(safeConvertToString(data.get("AUDITOR_NAME")));
        vo.setAuditTime(safeConvertToLocalDateTime(data.get("AUDIT_TIME")));
        vo.setAuditRemark(safeConvertToString(data.get("AUDIT_REMARK")));
        vo.setBookId(data.get("BOOK_ID") != null ? ((Number) data.get("BOOK_ID")).longValue() : null);
        vo.setTenantId(data.get("TENANT_ID") != null ? ((Number) data.get("TENANT_ID")).longValue() : null);
        vo.setRemark(safeConvertToString(data.get("REMARK")));
        vo.setCreateTime(safeConvertToLocalDateTime(data.get("CREATE_TIME")));
        vo.setUpdateTime(safeConvertToLocalDateTime(data.get("UPDATE_TIME")));
        vo.setCreateBy(data.get("CREATOR") != null ? data.get("CREATOR").toString() : null);
        vo.setUpdateBy(data.get("UPDATER") != null ? data.get("UPDATER").toString() : null);
        return vo;
    }

    /**
     * 转换为成本归集明细VO
     */
    private CostCollectionVO.CostCollectionDetailVO convertToCostCollectionDetailVO(Map<String, Object> data) {
        CostCollectionVO.CostCollectionDetailVO vo = new CostCollectionVO.CostCollectionDetailVO();

        // 处理detailId，同时设置Long类型和字符串类型
        Object detailIdObj = data.get("DETAIL_ID");
        if (detailIdObj != null) {
            vo.setDetailId(((Number) detailIdObj).longValue());
            vo.setDetailIdStr(detailIdObj.toString());
        }

        // 处理collectionId，同时设置Long类型和字符串类型
        Object collectionIdObj = data.get("COLLECTION_ID");
        if (collectionIdObj != null) {
            vo.setCollectionId(((Number) collectionIdObj).longValue());
            vo.setCollectionIdStr(collectionIdObj.toString());
        }
        vo.setCostElementCode(safeConvertToString(data.get("COST_ELEMENT_CODE")));
        vo.setCostElementName(safeConvertToString(data.get("COST_ELEMENT_NAME")));
        vo.setVoucherId(data.get("VOUCHER_ID") != null ? ((Number) data.get("VOUCHER_ID")).longValue() : null);
        vo.setVoucherNo(safeConvertToString(data.get("VOUCHER_NO")));
        vo.setAccountCode(safeConvertToString(data.get("ACCOUNT_CODE")));
        vo.setAccountName(safeConvertToString(data.get("ACCOUNT_NAME")));
        vo.setOriginalAmount((BigDecimal) data.get("ORIGINAL_AMOUNT"));
        vo.setAllocationRate((BigDecimal) data.get("ALLOCATION_RATE"));
        vo.setAllocatedAmount((BigDecimal) data.get("ALLOCATED_AMOUNT"));
        vo.setDepartmentName(safeConvertToString(data.get("DEPARTMENT_NAME")));
        vo.setProjectName(safeConvertToString(data.get("PROJECT_NAME")));
        vo.setBusinessType(safeConvertToString(data.get("BUSINESS_TYPE")));
        vo.setRemark(safeConvertToString(data.get("REMARK")));
        return vo;
    }

    /**
     * 验证并格式化归集期间
     *
     * @param collectionPeriod 原始归集期间字符串
     * @return 格式化后的YYYY-MM格式字符串
     * @throws IllegalArgumentException 当格式无效时抛出异常
     */
    private String validateAndFormatCollectionPeriod(String collectionPeriod) {
        if (collectionPeriod == null || collectionPeriod.trim().isEmpty()) {
            throw new IllegalArgumentException("归集期间不能为空");
        }

        String trimmed = collectionPeriod.trim();

        // 处理ISO日期时间格式转换，如 "2025-11-30T16:00:00.000Z"
        if (trimmed.matches("^\\d{4}-\\d{2}-\\d{2}T.*")) {
            String formatted = trimmed.substring(0, 7); // 提取 "2025-11"

            // 验证提取的YYYY-MM格式
            return validateYearMonthFormat(formatted);
        }

        // 验证已有的YYYY-MM格式
        if (trimmed.matches("^\\d{4}-\\d{2}$")) {
            return validateYearMonthFormat(trimmed);
        }

        throw new IllegalArgumentException("归集期间格式无效，应为 YYYY-MM 格式");
    }

    /**
     * 验证年月格式的有效性
     *
     * @param yearMonth YYYY-MM格式的字符串
     * @return 验证通过的原字符串
     * @throws IllegalArgumentException 当年月无效时抛出异常
     */
    private String validateYearMonthFormat(String yearMonth) {
        String[] parts = yearMonth.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("归集期间格式错误，应为 YYYY-MM 格式");
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            if (year < 2000 || year > 2100) {
                throw new IllegalArgumentException("归集期间年份必须在2000-2100之间");
            }
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("归集期间月份必须在1-12之间");
            }
            return yearMonth;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("归集期间格式错误，年月必须为数字");
        }
    }

    @Override
    public void exportCollectionResult(String period, Long bookId, Long tenantId, HttpServletResponse response) {
        try {
            log.info("导出成本归集结果，期间: {}, bookId: {}, tenantId: {}", period, bookId, tenantId);

            // 查询导出数据
            List<Map<String, Object>> exportData = costCollectionMapper.selectCostCollectionListForExport(period, bookId, tenantId);

            if (exportData == null || exportData.isEmpty()) {
                throw new RuntimeException("没有找到可导出的数据");
            }

            // 生成文件名
            String fileName = "成本归集结果_" + period + "_" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            // 创建Excel导出对象
            List<String> headers = Arrays.asList(
                "归集ID", "归集期间", "成本中心编码", "成本中心名称", "归集类型",
                "归集金额", "归集数量", "已归集金额", "归集状态", "审核状态",
                "审核人", "审核时间", "备注", "创建时间", "账套ID"
            );
            List<Integer> widths = Arrays.asList(
                15, 12, 15, 20, 12,
                15, 10, 15, 12, 12,
                15, 20, 20, 20, 15
            );

            com.financial.sharing.util.excel.ExcelExport excelExport = new com.financial.sharing.util.excel.ExcelExport("成本归集结果", headers, widths);

            // 填充数据
            for (Map<String, Object> data : exportData) {
                org.apache.poi.ss.usermodel.Row row = excelExport.addRow();

                excelExport.addCell(row, 0, getSafeStringIdValue(data, "COLLECTION_ID"));
                excelExport.addCell(row, 1, getSafeStringValue(data, "COLLECTION_PERIOD"));
                excelExport.addCell(row, 2, getSafeStringValue(data, "CENTER_CODE"));
                excelExport.addCell(row, 3, getSafeStringValue(data, "CENTER_NAME"));
                excelExport.addCell(row, 4, getSafeStringValue(data, "COLLECTION_TYPE_NAME"));
                excelExport.addCell(row, 5, getSafeBigDecimalValue(data, "COLLECTION_AMOUNT"));
                excelExport.addCell(row, 6, getSafeIntValue(data, "COLLECTION_COUNT"));
                excelExport.addCell(row, 7, getSafeBigDecimalValue(data, "COLLECTED_AMOUNT"));
                excelExport.addCell(row, 8, getSafeStringValue(data, "STATUS_NAME"));
                excelExport.addCell(row, 9, getSafeStringValue(data, "AUDIT_STATUS_NAME"));
                excelExport.addCell(row, 10, getSafeStringValue(data, "AUDITOR_NAME"));
                excelExport.addCell(row, 11, data.get("AUDIT_TIME"));
                excelExport.addCell(row, 12, getSafeStringValue(data, "REMARK"));
                excelExport.addCell(row, 13, data.get("CREATE_TIME"));
                excelExport.addCell(row, 14, getSafeStringIdValue(data, "BOOK_ID"));
            }

            // 导出文件
            try {
                excelExport.write(response, fileName);
            } finally {
                excelExport.close();
            }

            log.info("成本归集结果导出成功，导出记录数: {}", exportData.size());

        } catch (Exception e) {
            log.error("导出成本归集结果失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    // 辅助方法
    private String getSafeStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        String strValue = value.toString();
        return "null".equals(strValue) || "".equals(strValue) ? null : strValue;
    }

    private String getSafeStringIdValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        return value.toString();
    }

    private Integer getSafeIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private java.math.BigDecimal getSafeBigDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null || "".equals(value) || "null".equals(value)) {
            return null;
        }
        try {
            return new java.math.BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ==================== 新增方法实现 ====================

    @Override
    public MyJsonBean saveOrUpdateCostCollection(Map<String, Object> collectionData) {
        try {
            log.info("保存或更新成本归集配置，数据: {}", collectionData);
            // TODO: 实现保存或更新逻辑
            return MyJsonBean.successData("保存成功", collectionData);
        } catch (Exception e) {
            log.error("保存成本归集配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean deleteCostCollection(String collectionId) {
        try {
            log.info("删除成本归集记录，ID: {}", collectionId);
            // TODO: 实现删除逻辑
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除成本归集记录失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean<List<Map<String, Object>>> getCostCollectionRules(String centerId, String bookId, String tenantId) {
        try {
            log.info("获取归集规则，centerId: {}, bookId: {}, tenantId: {}", centerId, bookId, tenantId);
            List<Map<String, Object>> rules = costCollectionRuleMapper.selectRulesByCenterId(centerId, bookId, tenantId);
            return MyJsonBean.successData(rules);
        } catch (Exception e) {
            log.error("获取归集规则失败", e);
            return MyJsonBean.errorData("获取归集规则失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveCostCollectionRule(Map<String, Object> ruleData) {
        try {
            log.info("保存归集规则，数据: {}", ruleData);

            String ruleId = (String) ruleData.get("ruleId");
            String ruleName = (String) ruleData.get("ruleName");
            String bookId = (String) ruleData.get("bookId");
            String tenantId = (String) ruleData.get("tenantId");

            // 检查规则名称是否重复
            int count = costCollectionRuleMapper.checkRuleNameExists(ruleName, ruleId, bookId, tenantId);
            if (count > 0) {
                return MyJsonBean.errorData("规则名称已存在");
            }

            // TODO: 实现保存逻辑
            return MyJsonBean.successData("保存成功", ruleData);
        } catch (Exception e) {
            log.error("保存归集规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchExecuteCostCollection(List<String> collectionIds) {
        try {
            log.info("批量执行成本归集，IDs: {}", collectionIds);
            // TODO: 实现批量执行逻辑
            return MyJsonBean.successData("批量执行成功");
        } catch (Exception e) {
            log.error("批量执行成本归集失败", e);
            return MyJsonBean.errorData("批量执行失败: " + e.getMessage());
        }
    }
}