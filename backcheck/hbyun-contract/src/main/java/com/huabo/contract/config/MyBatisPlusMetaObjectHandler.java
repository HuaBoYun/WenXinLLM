package com.huabo.contract.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 自动填充处理器
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充... 实体类: {}", metaObject.getOriginalObject().getClass().getSimpleName());

        // 自动填充创建时间
        LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        log.info("填充 createTime: {}", now);

        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        log.info("填充 updateTime: {}", now);

        // 自动填充创建人 - 从当前上下文获取用户ID
        Long currentUserId = getCurrentUserId();
        this.strictInsertFill(metaObject, "createBy", Long.class, currentUserId);
        log.info("填充 createBy: {}", currentUserId);

        // 自动填充更新人
        this.strictInsertFill(metaObject, "updateBy", Long.class, currentUserId);
        log.info("填充 updateBy: {}", currentUserId);

        // 自动填充交底人ID（项目交底表专用）
        this.strictInsertFill(metaObject, "brieferId", Long.class, currentUserId);
        log.info("填充 brieferId: {}", currentUserId);

        // 自动填充交底时间（项目交底表专用）
        java.util.Date briefingDate = new java.util.Date();
        this.strictInsertFill(metaObject, "briefingTime", java.util.Date.class, briefingDate);
        log.info("填充 briefingTime: {}", briefingDate);

        log.info("插入填充完成");
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");

        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        // 自动填充更新人
        Long currentUserId = getCurrentUserId();
        this.strictUpdateFill(metaObject, "updateBy", Long.class, currentUserId);
        log.info("填充 updateBy: {}", currentUserId);
    }

    /**
     * 获取当前用户ID
     * TODO: 实际项目中应该从Spring Security或其他认证框架中获取
     */
    private Long getCurrentUserId() {
        // 暂时返回固定值，实际项目中应该从认证上下文获取
        return 1L;
    }
}
