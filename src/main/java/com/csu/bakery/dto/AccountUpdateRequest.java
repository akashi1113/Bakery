package com.csu.bakery.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class AccountUpdateRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 20, message = "用户名长度1-20字符")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Min(value = 1, message = "年龄最小为1岁")
    @Max(value = 150, message = "年龄最大为150岁")
    private int age;

    private int gender;

    private String addr1;
    private String addr2;
    private String avatar_url;

    public @NotBlank(message = "用户名不能为空") @Size(min = 1, max = 20, message = "用户名长度1-20字符") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "用户名不能为空") @Size(min = 1, max = 20, message = "用户名长度1-20字符") String username) {
        this.username = username;
    }

    public @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String getPhone() {
        return phone;
    }

    public void setPhone(@Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") String phone) {
        this.phone = phone;
    }

    @Min(value = 1, message = "年龄最小为1岁")
    @Max(value = 150, message = "年龄最大为150岁")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 1, message = "年龄最小为1岁") @Max(value = 150, message = "年龄最大为150岁") int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}