package com.financial.sharing.service;

import com.github.pagehelper.PageInfo;
import com.financial.sharing.vo.param.AccountSubjectQueryParam;
import com.financial.sharing.vo.param.AccountSubjectSaveParam;
import com.financial.sharing.vo.result.AccountSubjectVO;

import java.util.List;

/**
 * 会计科目服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface AccountSubjectService {

    /**
     * 分页查询会计科目
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<AccountSubjectVO> getAccountSubjectPage(AccountSubjectQueryParam param);

    /**
     * 根据ID查询会计科目详情
     * 
     * @param subjectId 科目ID
     * @return 会计科目详情
     */
    AccountSubjectVO getAccountSubjectById(Long subjectId);

    /**
     * 保存会计科目
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    AccountSubjectVO saveAccountSubject(AccountSubjectSaveParam param);

    /**
     * 更新会计科目
     * 
     * @param param 更新参数
     * @return 更新结果
     */
    AccountSubjectVO updateAccountSubject(AccountSubjectSaveParam param);

    /**
     * 删除会计科目
     * 
     * @param subjectId 科目ID
     * @return 删除结果
     */
    boolean deleteAccountSubject(Long subjectId);

    /**
     * 批量删除会计科目
     * 
     * @param subjectIds 科目ID列表
     * @return 删除结果
     */
    boolean batchDeleteAccountSubjects(List<Long> subjectIds);

    /**
     * 启用/禁用会计科目
     * 
     * @param subjectId 科目ID
     * @param isEnabled 启用状态
     * @return 更新结果
     */
    boolean updateAccountSubjectStatus(Long subjectId, Integer isEnabled);

    /**
     * 批量启用/禁用会计科目
     * 
     * @param subjectIds 科目ID列表
     * @param isEnabled 启用状态
     * @return 更新结果
     */
    boolean batchUpdateAccountSubjectStatus(List<Long> subjectIds, Integer isEnabled);

    /**
     * 检查科目编码是否存在
     *
     * @param subjectCode 科目编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkAccountSubjectCodeExists(String subjectCode, Long bookId, Long tenantId, Long excludeId);

    /**
     * 获取会计科目树形结构
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目树形结构
     */
    List<AccountSubjectVO> getAccountSubjectTree(Long bookId, Long tenantId);

    /**
     * 根据科目类型查询会计科目
     * 
     * @param subjectType 科目类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计科目列表
     */
    List<AccountSubjectVO> getAccountSubjectsByType(Integer subjectType, Long bookId, Long tenantId);

    /**
     * 根据上级科目ID查询子科目列表
     *
     * @param parentSubjectId 上级科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 子科目列表
     */
    List<AccountSubjectVO> getAccountSubjectsByParentId(Long parentSubjectId, Long bookId, Long tenantId);

    /**
     * 刷新科目缓存
     *
     * @param bookId 账簿ID
     */
    void refreshCache(Long bookId);
}
