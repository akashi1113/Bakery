package com.csu.bakery.controller;

import com.csu.bakery.controller.enums.AccountResponseCode;
import com.csu.bakery.dto.AccountResponse;
import com.csu.bakery.dto.PasswordRequest;
import com.csu.bakery.model.Account;
import com.csu.bakery.config.JwtUtil;
import com.csu.bakery.service.AccountService;
import com.csu.bakery.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    private EmailService emailService = new EmailService();

    //发送邮箱验证码
    @PostMapping("/emailCode")
    public ResponseEntity<AccountResponse<?>> sendCode(@RequestParam String email, @RequestParam String username) {
        String code = emailService.sendEmail(email, username);
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.CODE_SENT, code));
    }

    //用户使用邮箱登录
    @PostMapping("/email")
    public ResponseEntity<AccountResponse<?>> loginWithEmail(
            @RequestParam String email,
            @RequestBody PasswordRequest request) {
        try {
            //验证登录
            Account account = accountService.login(email, request.getPassword());
            accountService.setLoginType(account.getUserid(),"email");

            //生成JWT
            String token = jwtUtil.generateToken(account.getUserid(),account.getUsername(),account.getTokenVersion());

            //构造响应数据
            AccountResponse.BasicResponse data = new AccountResponse.BasicResponse();
            data.setUserid(account.getUserid());
            data.setEmail(account.getEmail());
            data.setUsername(account.getUsername());
            data.setToken(token);
            data.setLogin_type("email");

            //返回成功响应
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(AccountResponse.success(AccountResponseCode.AUTH_SUCCESS, data));

        } catch (AuthenticationException e) {
            //登录失败
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.AUTH_FAILED,null));
        } catch (Exception e) {
            //服务器错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.errors(AccountResponseCode.SERVER_ERROR,null));
        }
    }

    //忘记密码后重置密码
    @PostMapping("/password")
    public ResponseEntity<AccountResponse<?>> resetPasswordByEmail(
            @PathVariable String email,
            @RequestBody @Valid PasswordRequest request,
            BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            String firstErrorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse("请求参数无效");
            return ResponseEntity.badRequest()
                    .body(AccountResponse.error(AccountResponseCode.PASSWORD_INVALID, firstErrorMessage));
        }

        Account account = accountService.getAccountByEmail(email);

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //执行重置
        accountService.resetPassword(account.getUserid(), request.getPassword());
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.PASSWORD_RESET_SUCCESS, null));
    }

    //处理github回调
    @GetMapping("/github")
    public ResponseEntity<AccountResponse<AccountResponse.BasicResponse>> handleOAuth2Callback(@RequestParam String token) {
        try {
            Long userid=jwtUtil.extractUserId(token);
            Account account =accountService.getAccountById(userid);

            //返回JWT和用户信息
            AccountResponse.BasicResponse data=new AccountResponse.BasicResponse();
            data.setUserid(userid);
            data.setUsername(account.getUsername());
            data.setEmail(account.getEmail());
            data.setToken(token);
            data.setLogin_type("github");

            return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.AUTH_SUCCESS, data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.errors(AccountResponseCode.SERVER_ERROR,null));
        }
    }

    //退出登录
    @DeleteMapping
    public ResponseEntity<AccountResponse<?>> logout(
            HttpServletRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long userid = jwtUtil.extractUserId(token);
        String loginType = accountService.getAccountById(userid).getLogin_type();

        //token失效
        jwtUtil.invalidateToken(token);
        accountService.updateTokenVersion(userid);
        SecurityContextHolder.clearContext();

//        //区分登录类型处理
//        if ("github".equalsIgnoreCase(loginType)) {
//            new SecurityContextLogoutHandler().logout(request, null, null);
//            return ResponseEntity.ok()
//                    .header("Logout-Url", "https://github.com/logout") //前端处理跳转
//                    .body(AccountResponse.success(
//                            AccountResponseCode.LOGOUT_SUCCESS,
//                            "已退出GitHub登录，请完成第三方注销"
//                    ));
//        }

        return ResponseEntity.ok(
                AccountResponse.success(AccountResponseCode.LOGOUT_SUCCESS, null)
        );
    }
}
