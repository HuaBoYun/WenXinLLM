package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.EnterpriseInfo;
import com.huabo.cybermonitor.vo.EnterpriseInfoQueryVO;
import com.huabo.cybermonitor.vo.EnterpriseStatisticsVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 企业基础信息表 服务类
 *
 * @author system
 * @since 2024-01-01
 */
public interface IEnterpriseInfoService extends IService<EnterpriseInfo> {

    /**
     * 分页查询企业列表
     *
     * @param queryVO 查询条件
     * @return 企业列表
     */
    IPage<EnterpriseInfo> getEnterpriseList(EnterpriseInfoQueryVO queryVO);

    /**
     * 根据企业ID获取企业详情（包含母公司和子公司信息）
     *
     * @param enterpriseId 企业ID
     * @return 企业详情
     */
    Map<String, Object> getEnterpriseDetail(String enterpriseId);

    /**
     * 新增企业
     *
     * @param enterpriseInfo 企业信息
     * @return 是否成功
     */
    boolean addEnterprise(EnterpriseInfo enterpriseInfo);

    /**
     * 更新企业信息
     *
     * @param enterpriseInfo 企业信息
     * @return 是否成功
     */
    boolean updateEnterprise(EnterpriseInfo enterpriseInfo);

    /**
     * 删除企业
     *
     * @param enterpriseId 企业ID
     * @return 是否成功
     */
    boolean deleteEnterprise(String enterpriseId);

    /**
     * 批量删除企业
     *
     * @param enterpriseIds 企业ID列表
     * @return 是否成功
     */
    boolean batchDeleteEnterprise(List<String> enterpriseIds);

    /**
     * 获取母公司列表（用于下拉选择）
     *
     * @return 母公司列表
     */
    List<Map<String, Object>> getParentEnterpriseList();

    /**
     * 验证统一社会信用代码是否重复
     *
     * @param creditCode 统一社会信用代码
     * @param excludeId 排除的企业ID（编辑时使用）
     * @return 是否重复
     */
    boolean validateCreditCode(String creditCode, String excludeId);

    /**
     * 验证企业名称是否重复
     *
     * @param enterpriseName 企业名称
     * @param excludeId 排除的企业ID（编辑时使用）
     * @return 是否重复
     */
    boolean validateEnterpriseName(String enterpriseName, String excludeId);

    /**
     * 获取企业统计数据
     *
     * @return 统计数据
     */
    EnterpriseStatisticsVO getEnterpriseStatistics();

    /**
     * 获取企业类型分布
     *
     * @return 类型分布
     */
    List<Map<String, Object>> getEnterpriseTypeDistribution();

    /**
     * 获取企业地区分布
     *
     * @return 地区分布
     */
    List<Map<String, Object>> getEnterpriseRegionDistribution();

    /**
     * 获取企业行业分布
     *
     * @return 行业分布
     */
    List<Map<String, Object>> getEnterpriseIndustryDistribution();

    /**
     * 导出企业列表
     *
     * @param queryVO 查询条件
     * @param response HTTP响应
     */
    void exportEnterpriseList(EnterpriseInfoQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载企业导入模板
     *
     * @param response HTTP响应
     */
    void downloadEnterpriseTemplate(HttpServletResponse response);

    /**
     * 批量导入企业
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importEnterpriseList(MultipartFile file);

    /**
     * 获取子公司列表
     *
     * @param parentEnterpriseId 母公司ID
     * @return 子公司列表
     */
    List<EnterpriseInfo> getChildEnterprises(String parentEnterpriseId);

    /**
     * 根据企业类型获取企业列表
     *
     * @param enterpriseType 企业类型
     * @return 企业列表
     */
    List<EnterpriseInfo> getEnterprisesByType(String enterpriseType);

    /**
     * 根据监管层级获取企业列表
     *
     * @param supervisionLevel 监管层级
     * @return 企业列表
     */
    List<EnterpriseInfo> getEnterprisesBySupervisionLevel(String supervisionLevel);

    /**
     * 获取上市企业列表
     *
     * @return 上市企业列表
     */
    List<EnterpriseInfo> getListedEnterprises();

    /**
     * 根据行业代码获取企业列表
     *
     * @param industryCode 行业代码
     * @return 企业列表
     */
    List<EnterpriseInfo> getEnterprisesByIndustry(String industryCode);

    /**
     * 根据地区代码获取企业列表
     *
     * @param regionCode 地区代码
     * @return 企业列表
     */
    List<EnterpriseInfo> getEnterprisesByRegion(String regionCode);
}
