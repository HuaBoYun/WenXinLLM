package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.AccountSubjectEntity;
import com.financial.sharing.vo.param.AccountSubjectQueryParam;
import com.financial.sharing.vo.result.AccountSubjectVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 会计科目 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-19
 */
@Component("oracleAccountSubjectMapper")
public interface AccountSubjectMapper extends BaseMapper<AccountSubjectEntity> {

    /**
     * 分页查询会计科目
     * 使用PageHelper进行分页
     *
     * @param param 查询参数
     * @return 科目列表
     */
    List<AccountSubjectVO> selectAccountSubjectPage(@Param("param") AccountSubjectQueryParam param);

    /**
     * 根据科目编码查询会计科目
     * 
     * @param subjectCode 科目编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 会计科目
     */
    AccountSubjectEntity selectBySubjectCode(@Param("subjectCode") String subjectCode, 
                                           @Param("bookId") Long bookId, 
                                           @Param("tenantId") Long tenantId, 
                                           @Param("excludeId") Long excludeId);

    /**
     * 根据上级科目ID查询子科目列表
     * 
     * @param parentSubjectId 上级科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 子科目列表
     */
    List<AccountSubjectVO> selectByParentId(@Param("parentSubjectId") Long parentSubjectId, 
                                          @Param("bookId") Long bookId, 
                                          @Param("tenantId") Long tenantId);

    /**
     * 获取会计科目树形结构
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目树形结构
     */
    List<AccountSubjectVO> selectAccountSubjectTree(@Param("bookId") Long bookId, 
                                                  @Param("tenantId") Long tenantId);

    /**
     * 根据科目类型查询会计科目
     * 
     * @param subjectType 科目类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计科目列表
     */
    List<AccountSubjectVO> selectBySubjectType(@Param("subjectType") Integer subjectType, 
                                             @Param("bookId") Long bookId, 
                                             @Param("tenantId") Long tenantId);

    /**
     * 批量启用/禁用会计科目
     * 
     * @param ids 科目ID列表
     * @param isEnabled 启用状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("isEnabled") Integer isEnabled, 
                         @Param("updater") Long updater);

    /**
     * 根据ID删除会计科目
     *
     * @param subjectId 科目ID
     * @return 删除数量
     */
    int deleteById(@Param("subjectId") Long subjectId);

    /**
     * 批量删除会计科目
     *
     * @param ids 科目ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 检查科目是否有子科目
     * 
     * @param subjectId 科目ID
     * @param tenantId 租户ID
     * @return 子科目数量
     */
    int countChildrenByParentId(@Param("subjectId") Long subjectId, @Param("tenantId") Long tenantId);

    /**
     * 更新科目的末级标识
     * 
     * @param subjectId 科目ID
     * @param isLeaf 是否末级
     * @param updater 更新人
     * @return 更新数量
     */
    int updateLeafFlag(@Param("subjectId") Long subjectId,
                      @Param("isLeaf") Integer isLeaf,
                      @Param("updater") Long updater);

    /**
     * 分页查询科目列表 - 兼容任务要求
     *
     * @param param 查询参数
     * @return 分页结果
     */
    List<AccountSubjectEntity> getSubjectList(@Param("param") AccountSubjectQueryParam param);

    /**
     * 根据科目编码查询 - 兼容任务要求
     *
     * @param subjectCode 科目编码
     * @param bookId 账簿ID
     * @return 科目信息
     */
    AccountSubjectEntity getBySubjectCode(@Param("subjectCode") String subjectCode, @Param("bookId") Long bookId);

    /**
     * 查询下级科目 - 兼容任务要求
     *
     * @param parentId 上级科目ID
     * @param bookId 账簿ID
     * @return 下级科目列表
     */
    List<AccountSubjectEntity> getChildSubjects(@Param("parentId") Long parentId, @Param("bookId") Long bookId);

    /**
     * 批量获取科目信息 - 兼容任务要求
     *
     * @param subjectIds 科目ID列表
     * @return 科目信息列表
     */
    List<AccountSubjectEntity> getSubjectsByIds(@Param("subjectIds") List<Long> subjectIds);
}
