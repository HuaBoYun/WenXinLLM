//package com.huabo.legal.startup.security.rest;
//
//import com.huabo.legal.startup.security.service.AuthService;
//import com.huabo.legal.startup.security.service.dto.AuthUserDto;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.tags.TagOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * api登录授权
// *
// * @author zhuhuix
// * @date 2020-03-30
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/auth")
//@Tag(name="系统授权接口",description="系统授权接口")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @Operation(summary = "发送邮箱验证码")
//    @PostMapping(value = "/getEmailCode")
//    public ResponseEntity<Object> getEmailCode(@RequestParam String email) {
//        authService.sendMailCode(email);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @Operation(summary = "注册")
//    @PostMapping(value = "/register")
//    public ResponseEntity<Object> register(@RequestBody AuthUserDto authUserDto) {
//        return ResponseEntity.ok(authService.register(authUserDto));
//    }
//
//    @Operation(summary = "登录授权")
//    @PostMapping(value = "/login")
//    public ResponseEntity<Object> login(@RequestBody AuthUserDto authUserDto, HttpServletRequest request) {
//        return ResponseEntity.ok(authService.login(authUserDto, request));
//    }
//
//    @Operation(summary = "退出登录")
//    @DeleteMapping(value = "/logout")
//    public ResponseEntity<Object> logout(HttpServletRequest request) {
//        authService.logout(request);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//}
