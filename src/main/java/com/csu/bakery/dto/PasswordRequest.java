package com.csu.bakery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordRequest {
    @NotBlank(message = "新密码不能为空")
    @Size(min = 4, max = 20, message = "密码长度需4-20位")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "密码需包含大小写字母和数字")
    private String password;

    public @NotBlank(message = "新密码不能为空") @Size(min = 4, max = 20, message = "密码长度需4-20位") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "密码需包含大小写字母和数字") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "新密码不能为空") @Size(min = 4, max = 20, message = "密码长度需4-20位") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "密码需包含大小写字母和数字") String password) {
        this.password = password;
    }
}
