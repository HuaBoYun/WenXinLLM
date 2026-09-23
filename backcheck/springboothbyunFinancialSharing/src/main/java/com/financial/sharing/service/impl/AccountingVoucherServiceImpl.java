package com.financial.sharing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.oracle.entity.AccountingVoucherEntity;
import com.financial.sharing.oracle.mapper.AccountingVoucherMapper;
import com.financial.sharing.service.AccountingVoucherService;
import com.financial.sharing.vo.param.AccountingVoucherQueryParam;
import com.financial.sharing.vo.param.VoucherSaveParam;
import com.financial.sharing.vo.result.AccountingVoucherVO;
import com.financial.sharing.vo.result.VoucherDetailVO;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import com.financial.sharing.oracle.entity.VoucherEntryEntity;
import com.financial.sharing.oracle.mapper.VoucherEntryMapper;
import com.financial.sharing.vo.result.VoucherEntryVO;

/**
 * 会计凭证服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class AccountingVoucherServiceImpl implements AccountingVoucherService {

    @Resource
    private AccountingVoucherMapper accountingVoucherMapper;

    @Resource
    private VoucherEntryMapper voucherEntryMapper;

    @Resource
    private com.financial.sharing.oracle.mapper.VoucherUnpostRecordMapper voucherUnpostRecordMapper;

    @Resource
    private com.financial.sharing.oracle.mapper.VoucherPostingLogMapper voucherPostingLogMapper;

    @Override
    public PageInfo<AccountingVoucherVO> getVoucherPage(AccountingVoucherQueryParam param) {
        // 使用PageHelper进行分页
        PageHelper.startPage(param.getPageNo(), param.getPageSize(), "CREATE_TIME DESC");
        List<AccountingVoucherVO> list = accountingVoucherMapper.selectAccountingVoucherPage(param);
        return new PageInfo<>(list);
    }

    @Override
    public VoucherDetailVO getVoucherDetail(Long voucherId) {
        log.info("查询凭证详情，凭证ID：{}", voucherId);

        // 1. 查询凭证主表信息
        AccountingVoucherEntity voucher = accountingVoucherMapper.selectById(voucherId);
        if (voucher == null) {
            log.warn("凭证不存在，凭证ID：{}", voucherId);
            return null;
        }

        // 2. 构建返回对象
        VoucherDetailVO detailVO = new VoucherDetailVO();
        // 复制基础属性
        org.springframework.beans.BeanUtils.copyProperties(voucher, detailVO);

        // 3. 查询凭证分录列表（使用自定义SQL方法）
        List<VoucherEntryVO> entryVOs = voucherEntryMapper.selectByVoucherId(voucherId);
        // 转换为Entity列表
        List<VoucherEntryEntity> entries = new java.util.ArrayList<>();
        if (entryVOs != null) {
            for (VoucherEntryVO vo : entryVOs) {
                VoucherEntryEntity entry = new VoucherEntryEntity();
                org.springframework.beans.BeanUtils.copyProperties(vo, entry);
                entries.add(entry);
            }
        }
        detailVO.setEntries(entries);

        // 4. 设置状态名称
        detailVO.setStatusName(getStatusName(voucher.getVoucherStatus()));

        // 5. 设置操作权限
        Integer status = voucher.getVoucherStatus();
        detailVO.setCanEdit(status != null && status <= 2);      // 草稿、已保存可编辑
        detailVO.setCanDelete(status != null && status <= 2);    // 草稿、已保存可删除
        detailVO.setCanSubmit(status != null && status == 2);    // 已保存可提交
        detailVO.setCanReview(status != null && status == 3);    // 已提交可审核
        detailVO.setCanApprove(status != null && status == 3);   // 已提交可审批
        detailVO.setCanPost(status != null && status == 4);      // 已审核可过账
        detailVO.setCanUnpost(status != null && status == 5);    // 已过账可反过账

        log.info("查询凭证详情成功，凭证ID：{}，分录数量：{}", voucherId, entries.size());
        return detailVO;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "草稿";
            case 2: return "已保存";
            case 3: return "已提交";
            case 4: return "已审核";
            case 5: return "已过账";
            case 6: return "已取消";
            default: return "未知状态";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountingVoucherEntity saveVoucher(VoucherSaveParam param, TblStaffUtil currentUser) {
        try {
            log.info("开始保存凭证，参数：{}", param);

            // 创建凭证实体
            AccountingVoucherEntity voucher = new AccountingVoucherEntity();

            // 手动生成ID - 使用时间戳+随机数确保唯一性
            voucher.setVoucherId(System.currentTimeMillis() + (long)(Math.random() * 1000));

            // 设置基本信息
            voucher.setVoucherNo(param.getVoucherNo());
            // 兼容前端传参字段名，优先使用voucherType
            voucher.setVoucherTypeId(param.getVoucherType() != null ? param.getVoucherType() : param.getVoucherTypeId());
            voucher.setVoucherDate(param.getVoucherDate());
            voucher.setAccountingPeriod(param.getAccountingPeriod());
            voucher.setVoucherDesc(param.getVoucherDesc());

            // 设置租户和账簿信息
            if (currentUser != null && currentUser.getCurrentOrg() != null && currentUser.getLinkDetp() != null) {
                voucher.setTenantId(currentUser.getCurrentOrg().getOrgid().longValue());
                voucher.setCreator(currentUser.getStaffid().longValue());
            } else {
                // 测试环境使用默认值
                voucher.setTenantId(1000L);
                voucher.setCreator(5555L);
            }

            // 使用传入的bookId，如果没有则使用默认值
            voucher.setBookId(param.getBookId() != null ? param.getBookId() : 799415L);

            // 设置默认状态：草稿
            voucher.setVoucherStatus(1);

            // 设置默认审核状态：未审核
            voucher.setAuditStatus(0);

            // 设置版本号
            voucher.setVersion(1);

            // 设置删除标识：未删除
            voucher.setIsDeleted(0);

            // 计算借贷金额（从分录中获取）
            if (param.getEntries() != null && !param.getEntries().isEmpty()) {
                double totalDebit = param.getEntries().stream()
                    .mapToDouble(entry -> {
                        Object debitObj = entry.get("debitAmount");
                        return debitObj != null ? Double.parseDouble(debitObj.toString()) : 0.0;
                    })
                    .sum();

                double totalCredit = param.getEntries().stream()
                    .mapToDouble(entry -> {
                        Object creditObj = entry.get("creditAmount");
                        return creditObj != null ? Double.parseDouble(creditObj.toString()) : 0.0;
                    })
                    .sum();

                voucher.setTotalDebit(new java.math.BigDecimal(totalDebit));
                voucher.setTotalCredit(new java.math.BigDecimal(totalCredit));
            } else {
                voucher.setTotalDebit(new java.math.BigDecimal(0));
                voucher.setTotalCredit(new java.math.BigDecimal(0));
            }

            // 保存到数据库
            int result = accountingVoucherMapper.insert(voucher);

            if (result > 0) {
                log.info("凭证保存成功，凭证ID：{}", voucher.getVoucherId());
                return voucher;
            } else {
                log.error("凭证保存失败，插入结果：{}", result);
                throw new RuntimeException("凭证保存失败");
            }
        } catch (Exception e) {
            log.error("保存凭证时发生异常", e);
            throw new RuntimeException("保存凭证失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountingVoucherEntity updateVoucher(Long voucherId, VoucherSaveParam param, TblStaffUtil currentUser) {
        // TODO: 实现凭证更新
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVoucher(Long voucherId, TblStaffUtil currentUser) {
        // TODO: 实现凭证删除
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteVouchers(List<Long> voucherIds, TblStaffUtil currentUser) {
        // TODO: 实现批量删除
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitVoucher(Long voucherId, TblStaffUtil currentUser) {
        // TODO: 实现凭证提交
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewVoucher(Long voucherId, TblStaffUtil currentUser) {
        // TODO: 实现凭证审核
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveVoucher(Long voucherId, String approvalRemark, TblStaffUtil currentUser) {
        // TODO: 实现凭证审批
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectVoucher(Long voucherId, String rejectReason, TblStaffUtil currentUser) {
        // TODO: 实现凭证驳回
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> postVouchers(List<Long> voucherIds, TblStaffUtil currentUser) {
        log.info("开始过账凭证，凭证ID列表：{}，操作人：{}", voucherIds, currentUser != null ? currentUser.getStaffid() : "未知");

        Map<String, Object> result = new java.util.HashMap<>();
        int successCount = 0;
        int failCount = 0;
        java.util.List<String> errorMessages = new java.util.ArrayList<>();

        for (Long voucherId : voucherIds) {
            try {
                // 1. 更新凭证状态为已过账（状态3）
                Long posterId = currentUser != null && currentUser.getStaffid() != null ? currentUser.getStaffid().longValue() : null;
                int updateResult = accountingVoucherMapper.postVoucher(voucherId, posterId);

                if (updateResult > 0) {
                    log.info("凭证 {} 过账成功", voucherId);

                    // 2. 更新反过账记录表状态为"已重新过账"（如果存在未重新过账的记录）
                    try {
                        updateUnpostRecordStatus(voucherId, currentUser);
                    } catch (Exception e) {
                        log.warn("更新反过账记录状态失败，凭证ID：{}，错误：{}", voucherId, e.getMessage());
                        // 不影响主流程
                    }

                    // 3. 记录过账日志
                    try {
                        recordPostingLog(voucherId, "POST", "SUCCESS", null, currentUser);
                    } catch (Exception e) {
                        log.warn("记录过账日志失败，凭证ID：{}，错误：{}", voucherId, e.getMessage());
                        // 不影响主流程
                    }

                    successCount++;
                } else {
                    log.warn("凭证 {} 过账失败，可能凭证不存在或状态不正确", voucherId);
                    failCount++;
                    errorMessages.add("凭证ID " + voucherId + " 过账失败");

                    // 记录失败日志
                    try {
                        recordPostingLog(voucherId, "POST", "FAILED", "凭证不存在或状态不正确", currentUser);
                    } catch (Exception e) {
                        log.warn("记录过账日志失败", e);
                    }
                }
            } catch (Exception e) {
                log.error("凭证 {} 过账异常：{}", voucherId, e.getMessage(), e);
                failCount++;
                errorMessages.add("凭证ID " + voucherId + " 过账异常：" + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", voucherIds.size());
        result.put("errorMessages", errorMessages);

        log.info("过账完成，成功：{}，失败：{}", successCount, failCount);
        return result;
    }

    /**
     * 更新反过账记录状态为已重新过账
     */
    private void updateUnpostRecordStatus(Long voucherId, TblStaffUtil currentUser) {
        try {
            // 查询该凭证的未重新过账记录
            java.util.List<com.financial.sharing.oracle.entity.VoucherUnpostRecordEntity> records =
                voucherUnpostRecordMapper.selectByVoucherId(voucherId);

            if (records != null && !records.isEmpty()) {
                for (com.financial.sharing.oracle.entity.VoucherUnpostRecordEntity record : records) {
                    // 只更新状态为UNPOSTED的记录
                    if ("UNPOSTED".equals(record.getUnpostStatus())) {
                        Long repostBy = currentUser != null && currentUser.getStaffid() != null ? currentUser.getStaffid().longValue() : null;
                        String repostByName = currentUser != null ? currentUser.getRealname() : null;

                        voucherUnpostRecordMapper.updateUnpostStatus(
                            record.getRecordId(),
                            "REPOSTED",
                            repostBy,
                            repostByName
                        );
                        log.info("更新反过账记录状态成功，记录ID：{}，凭证ID：{}", record.getRecordId(), voucherId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("更新反过账记录状态失败，凭证ID：{}", voucherId, e);
            throw e;
        }
    }

    /**
     * 记录过账日志
     */
    private void recordPostingLog(Long voucherId, String operationType, String status, String errorMsg, TblStaffUtil currentUser) {
        try {
            java.util.Map<String, Object> logData = new java.util.HashMap<>();
            logData.put("voucherId", voucherId);
            logData.put("operationType", operationType);
            logData.put("operationStatus", status);
            logData.put("errorMessage", errorMsg);
            if (currentUser != null) {
                logData.put("operatorId", currentUser.getStaffid());
                logData.put("operatorName", currentUser.getRealname());
            }

            voucherPostingLogMapper.insertPostingLog(logData);
            log.info("记录过账日志成功，凭证ID：{}，操作类型：{}，状态：{}", voucherId, operationType, status);
        } catch (Exception e) {
            log.error("记录过账日志失败，凭证ID：{}", voucherId, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> unpostVouchers(List<Long> voucherIds, String reason, TblStaffUtil currentUser) {
        log.info("开始反过账凭证，凭证ID列表：{}，原因：{}，操作人：{}", voucherIds, reason, currentUser != null ? currentUser.getStaffid() : "未知");

        Map<String, Object> result = new java.util.HashMap<>();
        int successCount = 0;
        int failCount = 0;
        java.util.List<String> errorMessages = new java.util.ArrayList<>();

        for (Long voucherId : voucherIds) {
            try {
                // 1. 更新凭证状态为已审核（状态2，从已过账回退）
                Long updaterId = currentUser != null && currentUser.getStaffid() != null ? currentUser.getStaffid().longValue() : null;
                int updateResult = accountingVoucherMapper.unpostVoucher(voucherId, updaterId);

                if (updateResult > 0) {
                    log.info("凭证 {} 反过账成功", voucherId);

                    // 2. 记录反过账日志
                    try {
                        recordPostingLog(voucherId, "UNPOST", "SUCCESS", null, currentUser);
                    } catch (Exception e) {
                        log.warn("记录反过账日志失败，凭证ID：{}，错误：{}", voucherId, e.getMessage());
                    }

                    successCount++;
                } else {
                    log.warn("凭证 {} 反过账失败，可能凭证不存在或状态不正确", voucherId);
                    failCount++;
                    errorMessages.add("凭证ID " + voucherId + " 反过账失败");

                    try {
                        recordPostingLog(voucherId, "UNPOST", "FAILED", "凭证不存在或状态不正确", currentUser);
                    } catch (Exception e) {
                        log.warn("记录反过账日志失败", e);
                    }
                }
            } catch (Exception e) {
                log.error("凭证 {} 反过账异常：{}", voucherId, e.getMessage(), e);
                failCount++;
                errorMessages.add("凭证ID " + voucherId + " 反过账异常：" + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", voucherIds.size());
        result.put("errorMessages", errorMessages);

        log.info("反过账完成，成功：{}，失败：{}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchApproveVouchers(List<Long> voucherIds, String approvalRemark, TblStaffUtil currentUser) {
        // TODO: 实现批量审批
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVoucherStatus(Long voucherId, Integer status, TblStaffUtil currentUser) {
        // TODO: 实现凭证状态更新
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateVoucherStatus(List<Long> voucherIds, Integer status, TblStaffUtil currentUser) {
        // TODO: 实现批量状态更新
        return 0;
    }

    @Override
    public Map<String, Object> getVoucherStatistics(AccountingVoucherQueryParam param, TblStaffUtil currentUser) {
        // TODO: 实现凭证统计
        return null;
    }

    @Override
    public List<Map<String, Object>> getApprovalHistory(Long voucherId) {
        // TODO: 实现审批历史查询
        return null;
    }

    @Override
    public String generateVoucherNo(Long voucherTypeId, String accountingPeriod, Long bookId, Long tenantId) {
        // TODO: 实现凭证编号生成
        return null;
    }

    @Override
    public boolean validateBalance(List<Map<String, Object>> entries) {
        // TODO: 实现借贷平衡校验
        return false;
    }

    @Override
    public boolean canOperateVoucher(Long voucherId, String operation, TblStaffUtil currentUser) {
        // TODO: 实现操作权限检查
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importVouchers(java.io.InputStream inputStream, String fileName, Long tenantId) {
        Map<String, Object> result = new java.util.HashMap<>();
        java.util.List<String> errorList = new java.util.ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try {
            log.info("开始导入凭证，文件名：{}，租户ID：{}", fileName, tenantId);

            // TODO: 集成EasyExcel进行Excel解析
            // 当前使用模拟数据处理流程
            // 实际应该使用: EasyExcel.read(inputStream).head(VoucherImportDTO.class).sheet(0).doReadSync();

            // 模拟解析Excel数据（实际项目中应替换为真实的Excel解析逻辑）
            java.util.List<Map<String, Object>> voucherDataList = parseExcelData(inputStream);

            log.info("从Excel读取到{}条凭证数据", voucherDataList.size());

            // 遍历处理每条凭证
            for (int i = 0; i < voucherDataList.size(); i++) {
                Map<String, Object> voucherData = voucherDataList.get(i);
                try {
                    // 1. 数据验证
                    validateImportData(voucherData);

                    // 2. 转换为实体
                    AccountingVoucherEntity voucher = convertToEntity(voucherData, tenantId);

                    // 3. 检查凭证号是否已存在
                    AccountingVoucherEntity existingVoucher = accountingVoucherMapper.selectByVoucherNo(
                        voucher.getVoucherNo(),
                        voucher.getBookId(),
                        voucher.getTenantId(),
                        null // isDeleted参数,传null表示不限制删除状态
                    );

                    if (existingVoucher != null) {
                        // 更新现有凭证
                        voucher.setVoucherId(existingVoucher.getVoucherId());
                        accountingVoucherMapper.updateById(voucher);
                        log.info("更新凭证：{}", voucher.getVoucherNo());
                    } else {
                        // 插入新凭证
                        accountingVoucherMapper.insert(voucher);
                        log.info("插入新凭证：{}", voucher.getVoucherNo());
                    }

                    successCount++;

                } catch (Exception e) {
                    failCount++;
                    String errorMsg = "第" + (i + 2) + "行: " + e.getMessage();
                    errorList.add(errorMsg);
                    log.error("导入凭证失败: {}", voucherData, e);
                }
            }

            // 构建返回结果
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorList", errorList);
            result.put("totalCount", voucherDataList.size());
            result.put("message", String.format("导入完成！成功%d条，失败%d条", successCount, failCount));

            log.info("凭证导入完成，成功{}条，失败{}条", successCount, failCount);

        } catch (Exception e) {
            log.error("导入凭证异常", e);
            throw new RuntimeException("导入失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 解析Excel数据（模拟实现）
     * TODO: 集成EasyExcel实现真实的Excel解析
     */
    private java.util.List<Map<String, Object>> parseExcelData(java.io.InputStream inputStream) {
        // 模拟返回数据
        // 实际应该使用EasyExcel解析Excel文件
        java.util.List<Map<String, Object>> dataList = new java.util.ArrayList<>();

        // TODO: 实际实现
        // List<VoucherImportDTO> list = EasyExcel.read(inputStream)
        //     .head(VoucherImportDTO.class)
        //     .sheet(0)
        //     .doReadSync();

        log.warn("当前使用模拟数据，请集成EasyExcel实现真实的Excel解析功能");
        return dataList;
    }

    /**
     * 验证导入数据
     */
    private void validateImportData(Map<String, Object> data) {
        if (data.get("voucherNo") == null || data.get("voucherNo").toString().trim().isEmpty()) {
            throw new RuntimeException("凭证号不能为空");
        }
        if (data.get("voucherType") == null || data.get("voucherType").toString().trim().isEmpty()) {
            throw new RuntimeException("凭证类型不能为空");
        }
        if (data.get("voucherDate") == null) {
            throw new RuntimeException("凭证日期不能为空");
        }

        // 验证分录数据
        if (data.get("entries") == null || !(data.get("entries") instanceof List)) {
            throw new RuntimeException("分录数据不能为空");
        }

        @SuppressWarnings("unchecked")
        java.util.List<Map<String, Object>> entries = (java.util.List<Map<String, Object>>) data.get("entries");

        if (entries.isEmpty()) {
            throw new RuntimeException("至少需要一条分录");
        }

        // 验证借贷平衡
        java.math.BigDecimal totalDebit = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalCredit = java.math.BigDecimal.ZERO;

        for (Map<String, Object> entry : entries) {
            if (entry.get("debitAmount") != null) {
                totalDebit = totalDebit.add(new java.math.BigDecimal(entry.get("debitAmount").toString()));
            }
            if (entry.get("creditAmount") != null) {
                totalCredit = totalCredit.add(new java.math.BigDecimal(entry.get("creditAmount").toString()));
            }
        }
        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new RuntimeException(String.format("借贷不平衡：借方合计%s，贷方合计%s", totalDebit, totalCredit));
        }
    }

    /**
     * 转换导入数据为实体
     */
    private AccountingVoucherEntity convertToEntity(Map<String, Object> data, Long tenantId) {
        AccountingVoucherEntity entity = new AccountingVoucherEntity();

        // 设置ID - 使用Long类型
        entity.setVoucherId(System.currentTimeMillis() + (long)(Math.random() * 1000));

        // 设置基本信息
        entity.setVoucherNo(data.get("voucherNo").toString());
        entity.setVoucherTypeId(Long.valueOf(data.get("voucherType").toString()));

        // 处理日期
        if (data.get("voucherDate") instanceof java.time.LocalDate) {
            entity.setVoucherDate((java.time.LocalDate) data.get("voucherDate"));
        } else if (data.get("voucherDate") instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) data.get("voucherDate");
            entity.setVoucherDate(new java.sql.Date(date.getTime()).toLocalDate());
        }

        // 设置会计期间
        if (data.get("accountingPeriod") != null) {
            entity.setAccountingPeriod(data.get("accountingPeriod").toString());
        } else {
            // 根据凭证日期生成会计期间
            java.time.LocalDate voucherDate = entity.getVoucherDate();
            if (voucherDate != null) {
                String period = voucherDate.getYear() + String.format("%02d", voucherDate.getMonthValue());
                entity.setAccountingPeriod(period);
            }
        }

        // 设置其他字段
        entity.setVoucherDesc(data.get("voucherDesc") != null ? data.get("voucherDesc").toString() : "");

        // 默认状态为草稿
        entity.setVoucherStatus(1);

        // 设置租户和账簿信息
        entity.setTenantId(tenantId);
        entity.setBookId(1L); // TODO: 从用户信息获取

        // 设置创建信息 - 使用LocalDateTime
        entity.setCreateTime(java.time.LocalDateTime.now());
        entity.setCreator(1L); // TODO: 从用户信息获取

        return entity;
    }

    @Override
    public Map<String, Object> getUnpostRecordPage(Map<String, Object> param, TblStaffUtil currentUser) {
        log.info("查询反过账记录，参数：{}", param);

        // 设置默认分页参数
        int pageNo = param.get("pageNo") != null ? Integer.parseInt(param.get("pageNo").toString()) : 1;
        int pageSize = param.get("pageSize") != null ? Integer.parseInt(param.get("pageSize").toString()) : 20;

        // 注意：暂时不强制过滤租户和账簿，以便查询测试数据
        // 如果前端传入了 bookId 和 tenantId，则使用前端传入的值
        // 否则尝试从当前用户获取（如果需要启用，取消下面的注释）
        // if (param.get("bookId") == null && currentUser != null && currentUser.getLinkDetp() != null) {
        //     param.put("bookId", currentUser.getLinkDetp().getOrgid().longValue());
        // }
        // if (param.get("tenantId") == null && currentUser != null && currentUser.getCurrentOrg() != null) {
        //     param.put("tenantId", currentUser.getCurrentOrg().getOrgid().longValue());
        // }

        // 使用PageHelper进行分页
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = voucherUnpostRecordMapper.selectUnpostRecordPage(param);
        Long total = voucherUnpostRecordMapper.selectUnpostRecordCount(param);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("tlist", list);
        result.put("totalRecord", total != null ? total : 0);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);

        log.info("查询反过账记录结果：total={}, listSize={}", total, list != null ? list.size() : 0);

        return result;
    }
}