package com.csu.bakery.controller;

import com.csu.bakery.controller.enums.AccountResponseCode;
import com.csu.bakery.dto.AccountResponse;
import com.csu.bakery.dto.AccountUpdateRequest;
import com.csu.bakery.dto.PasswordRequest;
import com.csu.bakery.model.Account;
import com.csu.bakery.config.JwtUtil;
import com.csu.bakery.service.AccountService;
import com.csu.bakery.service.NewImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    //用户注册
    @PostMapping
    public ResponseEntity<AccountResponse<?>> signup(
            @RequestParam String email,
            @RequestParam String username,
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

        try {
            //验证邮箱是否已注册
            if (accountService.getAccountByEmail(email) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(AccountResponse.errors(AccountResponseCode.EMAIL_ALREADY_EXISTS, null));
            }

            //创建账户
            accountService.insertEmailAccount(email, username, request.getPassword());
            Account account = accountService.getAccountByEmail(email);
            String jwt = jwtUtil.generateToken(account.getUserid(), username,account.getTokenVersion());

            //成功响应
            AccountResponse.BasicResponse data = new AccountResponse.BasicResponse();
            data.setUserid(account.getUserid());
            data.setEmail(account.getEmail());
            data.setUsername(account.getUsername());
            data.setToken(jwt);
            accountService.setLoginType(account.getUserid(),"email");

            return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.REGISTER_SUCCESS, data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AccountResponse.errors(AccountResponseCode.SERVER_ERROR, null));
        }
    }

    //获取用户id
    @GetMapping
    public ResponseEntity<AccountResponse<?>> getUserId(@RequestHeader("Authorization") String token) {
        Long userid=jwtUtil.extractUserId(token.replace("Bearer ", ""));
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.SUCCESS, userid));
    }

    //获取用户信息
    @GetMapping("/{userid}")
    public ResponseEntity<AccountResponse<AccountResponse.DetailResponse>> getAccount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid) {
        Account account = accountService.getAccountById(userid);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        //构造响应数据
        AccountResponse.DetailResponse data = new AccountResponse.DetailResponse();
        data.setUserid(account.getUserid());
        data.setUsername(account.getUsername());
        data.setEmail(account.getEmail());
        data.setPhone(account.getPhone());
        data.setAge(account.getAge());
        data.setGender(account.getGender());
        data.setAddr1(account.getAddr1());
        data.setAddr2(account.getAddr2());
        data.setVIPLevel(account.getVIPLevel());
        data.setAvatar_url(account.getAvatar_url());

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.GET_SUCCESS, data));
    }

    //修改用户信息
    @PutMapping("/{userid}")
    public ResponseEntity<AccountResponse<?>> updateAccount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestBody @Valid AccountUpdateRequest updateRequest,
            BindingResult bindingResult) {

        //参数校验
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(AccountResponse.errors(AccountResponseCode.PARAM_INVALID, bindingResult));
        }

        Account account = accountService.getAccountById(userid);
        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        //更新用户信息
        int result = accountService.updateAccount(userid, updateRequest);
        if (result == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND, null));
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.UPDATE_SUCCESS, null));
    }

    //上传头像
    @PostMapping("/{userid}/avatar")
    public ResponseEntity<AccountResponse<?>> uploadImage(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestParam("file") MultipartFile file) throws IOException {
        Account account = accountService.getAccountById(userid);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        byte[] fileBytes = file.getBytes();
        String fileUrl = NewImageService.uploadImage(fileBytes);
        accountService.setAvatar(userid,fileUrl);

        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.UPDATE_SUCCESS, null));
    }

    //重置邮箱
    @PostMapping("/{userid}/email")
    public ResponseEntity<AccountResponse<?>> resetEmail(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
            @RequestParam @Email String newEmail) {
        if(accountService.getAccountByEmail(newEmail)!=null)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AccountResponse.errors(AccountResponseCode.EMAIL_ALREADY_EXISTS,null));

        Account account = accountService.getAccountById(userid);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        //执行重置
        accountService.resetEmail(userid, newEmail);
        return ResponseEntity.ok(AccountResponse.success(
                AccountResponseCode.EMAIL_RESET_SUCCESS, newEmail));
    }

    //重置密码
    @PostMapping("/{userid}/password")
    public ResponseEntity<AccountResponse<?>> resetPassword(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid,
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

        Account account = accountService.getAccountById(userid);

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AccountResponse.errors(AccountResponseCode.USER_NOT_FOUND,null));
        }

        //权限校验
        if (!jwtUtil.validateToken(token.replace("Bearer ", ""),userid,account.getUsername(),account.getTokenVersion())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AccountResponse.errors(AccountResponseCode.PERMISSION_DENIED, null));
        }

        //执行重置
        accountService.resetPassword(userid, request.getPassword());
        return ResponseEntity.ok(AccountResponse.success(AccountResponseCode.PASSWORD_RESET_SUCCESS, null));
    }
}