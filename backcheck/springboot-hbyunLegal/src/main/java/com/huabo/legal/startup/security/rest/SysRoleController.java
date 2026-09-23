//package com.huabo.legal.startup.security.rest;
//
//import com.huabo.legal.startup.security.domain.SysRole;
//import com.huabo.legal.startup.security.service.SysRoleService;
//import com.huabo.legal.startup.security.service.dto.RoleQueryDto;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.tags.TagOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
///**
// * 角色信息api
// *
// * @author zhuhuix
// * @date 2021-09-13
// *  * @date 2021-10-26 增加getPermission，savePermission API接口
// */
//@Slf4j
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/role")
//@Tag(name="角色信息接口",description="角色信息接口")
//public class SysRoleController {
//
//    private final SysRoleService sysRoleService;
//
//    @Operation(summary = "根据条件查询角色信息")
//    @PostMapping("/list")
//    public Object getRoleList(@RequestBody RoleQueryDto roleQueryDto) {
//        return sysRoleService.list(roleQueryDto);
//    }
//
//    @Operation(summary = "根据id获取单个角色信息")
//    @GetMapping("{id}")
//    public ResponseEntity<Object> getRoleById(@PathVariable Long id) {
//        return ResponseEntity.ok(sysRoleService.findById(id));
//    }
//
//    @Operation(summary = "根据角色编码获取单个角色信息")
//    @GetMapping("/roleCode/{roleCode}")
//    public ResponseEntity<Object> getRoleByRoleCode(@PathVariable String roleCode) {
//        return ResponseEntity.ok(sysRoleService.findByRoleCode(roleCode));
//    }
//
//    @Operation(summary = "获取所有角色信息")
//    @GetMapping()
//    public ResponseEntity<Object> getAllRole() {
//        return ResponseEntity.ok(sysRoleService.findAll());
//    }
//
//    @Operation(summary = "保存角色信息")
//    @PostMapping
//    public ResponseEntity<Object> saveRole(@RequestBody SysRole role) {
//        if (role.getId() != null) {
//            return ResponseEntity.ok(sysRoleService.update(role));
//        } else {
//            return ResponseEntity.ok(sysRoleService.create(role));
//        }
//    }
//
//    @Operation(summary = "删除角色信息")
//    @DeleteMapping
//    public ResponseEntity<Object> deleteRole(@RequestBody Set<Long> ids) {
//        return ResponseEntity.ok(sysRoleService.delete(ids));
//    }
//
//    @Operation(summary = "获取角色权限信息")
//    @GetMapping("{roleId}/permission")
//    public ResponseEntity<Object> getPermission(@PathVariable Long roleId) {
//        return ResponseEntity.ok(sysRoleService.getPermission(roleId));
//    }
//
//    @Operation(summary = "保存角色权限信息")
//    @PostMapping("{roleId}/permission")
//    public ResponseEntity<Object> savePermission(@PathVariable Long roleId,@RequestBody Set<Long> menus) {
//        return ResponseEntity.ok(sysRoleService.savePermission(roleId,menus));
//    }
//}
