package com.huabo.legal.startup.security.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

/**
 * 权限表
 *
 * @author zhuhuix
 * @date 2021-10-26
 */
@Schema(name="权限信息")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_permission")
public class SysPermission {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roleId;

    private Long menuId;

    private Timestamp createTime;

    public SysPermission(Long roleId, Long menuId, Timestamp createTime) {
        this.roleId = roleId;
        this.menuId = menuId;
        this.createTime = createTime;
    }
}
