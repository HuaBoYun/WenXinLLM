package com.huabo.cybermonitor.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 组织层级查询工具 - 基于物化路径(Materialized Path)方案
 *
 * 查询逻辑: WHERE (ORG_PATH LIKE '%/orgId/%' OR (ORG_PATH IS NULL AND COMPANY_ID = orgId))
 * 这样既支持穿透查询，又兼容 ORG_PATH 未填充的旧数据
 */
@Component
public class OrgQueryHelper {

    private static final Logger log = LoggerFactory.getLogger(OrgQueryHelper.class);

    /**
     * 生成 ORG_PATH 的 LIKE 查询条件值
     */
    public String getOrgPathPattern(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }
        return "%/" + orgId + "/%";
    }

    /**
     * 判断是否需要按组织过滤
     */
    public boolean needOrgFilter(String orgId) {
        return StringUtils.isNotEmpty(orgId);
    }

    /**
     * 构建某个组织的完整路径
     */
    public String buildOrgPath(String parentPath, String orgId) {
        if (StringUtils.isEmpty(orgId)) return null;
        if (StringUtils.isEmpty(parentPath)) return "/" + orgId + "/";
        if (!parentPath.endsWith("/")) parentPath += "/";
        return parentPath + orgId + "/";
    }
}
