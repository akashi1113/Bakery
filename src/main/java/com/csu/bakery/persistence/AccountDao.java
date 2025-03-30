package com.csu.bakery.persistence;

import com.csu.bakery.dto.AccountUpdateRequest;
import com.csu.bakery.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface AccountDao {
    public int insertEmailAccount(@Param("email") String email, @Param("username") String username, @Param("password") String password);
    public int insertGithubAccount(Account account);
    public int insertGithub(@Param("userid") Long userid, @Param("github_id") int github_id, @Param("github_login") String github_login);
    public Account searchGithubAccount(@Param("github_id") int github_id);
    public Account login(@Param("email") String email, @Param("password") String password);
    public Account getAccount(Account account);
    public Account getAccountById(@Param("userid") Long userid);
    public Account getAccountByEmail(@Param("email") String email);
    public int updateAccount(Account account);
    public int resetEmail(@Param("userid") Long userid, @Param("newEmail") String newEmail);
    public int resetPassword(@Param("userid") Long userid, @Param("newPassword") String newPassword);
    public int updateAccountVIP(Account account);
    public int setAvatar(@Param("userid") Long userid, @Param("avatar_url") String avatar_url);
    public int setLoginType(@Param("userid") Long userid, @Param("login_type") String login_type);
    public int updateTokenVersion(@Param("userid") Long userid, @Param("token_version") int token_version);
}

