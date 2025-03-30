package com.csu.bakery.dto;

import com.csu.bakery.controller.enums.AccountResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

git remote add origin https://github.com/akashi1113/Bakery.git

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AccountResponse<T> {
    private int code;
    private String message;
    private T data;
    private Map<String, String> errors;

    public AccountResponse() {
    }

    public AccountResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //成功响应
    public static <T> AccountResponse<T> success(AccountResponseCode code, T data) {
        return new AccountResponse<>(
                code.getCode(),
                code.getMessage(),
                data
        );
    }

    //错误响应
    public static <T> AccountResponse<T> errors(AccountResponseCode code, BindingResult bindingResult) {
        AccountResponse<T> response = new AccountResponse<>();
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());

        if (bindingResult != null) {
            response.setErrors(
                    bindingResult.getFieldErrors().stream()
                            .collect(Collectors.toMap(
                                    FieldError::getField,
                                    FieldError::getDefaultMessage
                            ))
            );
        }

        return response;
    }

    //返回单条错误消息
    public static <T> AccountResponse<T> error(AccountResponseCode code, String errorMessage) {
        AccountResponse<T> response = new AccountResponse<>();
        response.setCode(code.getCode());
        response.setData((T) errorMessage);
        return response;
    }

    //用户基础信息响应
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class BasicResponse {
        private String token;
        private Long userid;
        private String username;
        private String email;

        public BasicResponse() {}

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getUserid() {
            return userid;
        }

        public void setUserid(Long userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    //用户详细信息响应
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class DetailResponse {
        private Long userid;
        private String username;
        private String email;
        private String phone;
        private int gender;
        private int age;
        private String addr1;
        private String addr2;
        private int VIPLevel;
        private String avatar_url;

        public DetailResponse() {}

        public Long getUserid() {
            return userid;
        }

        public void setUserid(Long userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public int getVIPLevel() {
            return VIPLevel;
        }

        public void setVIPLevel(int VIPLevel) {
            this.VIPLevel = VIPLevel;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
