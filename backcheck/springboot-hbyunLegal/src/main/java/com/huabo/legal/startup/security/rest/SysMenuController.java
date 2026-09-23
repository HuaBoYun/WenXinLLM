//package com.huabo.legal.startup.security.rest;
//
//import com.huabo.legal.startup.security.domain.SysMenu;
//import com.huabo.legal.startup.security.service.SysMenuService;
//import com.huabo.legal.startup.security.service.dto.SysMenuQueryDto;
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
// * api菜单资源
// *
// * @author zhuhuix
// * @date 2021-10-16
// */
//@Slf4j
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/menu")
//@Tag(name="菜单资源接口",description="菜单资源接口")
//public class SysMenuController {
//
//    private final SysMenuService sysMenuService;
//
//    @Operation(summary = "根据件查询菜单资源")
//    @PostMapping("/list")
//    public ResponseEntity<Object> getMenuList(@RequestBody SysMenuQueryDto sysMenuQueryDto) {
//        return ResponseEntity.ok(sysMenuService.list(sysMenuQueryDto));
//    }
//
//    @Operation(summary = "根据id获取单个菜单资源")
//    @GetMapping("{id}")
//    public ResponseEntity<Object> getMenuById(@PathVariable Long id) {
//        return ResponseEntity.ok(sysMenuService.findById(id));
//    }
//
//    @Operation(summary = "保存菜单资源")
//    @PostMapping
//    public ResponseEntity<Object> saveMenu(@RequestBody SysMenu sysMenu) {
//        if (sysMenu.getId() != null) {
//            return ResponseEntity.ok(sysMenuService.update(sysMenu));
//        } else {
//            return ResponseEntity.ok(sysMenuService.create(sysMenu));
//        }
//    }
//
//    @Operation(summary = "删除菜单资源")
//    @DeleteMapping
//    public ResponseEntity<Object> deleteMenu(@RequestBody Set<Long> ids) {
//        return ResponseEntity.ok(sysMenuService.delete(ids));
//    }
//}
