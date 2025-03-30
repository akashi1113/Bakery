package com.csu.bakery.service;

import com.csu.bakery.dto.AccountUpdateRequest;
import com.csu.bakery.model.Account;
import com.csu.bakery.persistence.AccountDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    public int insertEmailAccount(String email, String username, String password) {
        return accountDao.insertEmailAccount(email, username, password);
    }

    public int insertGithubAccount(Account account) {
        return accountDao.insertGithubAccount(account);
    }

    public int insertGithub(Long userid, int github_id, String github_login){
        return accountDao.insertGithub(userid, github_id, github_login);
    }

    public Account searchGithubAccount(int github_id){
        return accountDao.searchGithubAccount(github_id);
    }

    public Account login(String email, String password){
        return accountDao.login(email, password);
    }

    public Account getAccount(Account account) {
        return accountDao.getAccount(account);
    }

    public Account getAccountById(Long userid){
        return accountDao.getAccountById(userid);
    }

    public Account getAccountByEmail(String email) {
        return accountDao.getAccountByEmail(email);
    }

    @Transactional
    public int updateAccount(Long userId, AccountUpdateRequest request) {
        Account account = accountDao.getAccountById(userId);
        if(account == null) {
            return 0;
        }
        account.setUsername(request.getUsername());
        account.setGender(request.getGender());
        account.setAge(request.getAge());
        account.setAddr1(request.getAddr1());
        account.setAddr2(request.getAddr2());
        account.setPhone(request.getPhone());
        account.setAvatar_url(request.getAvatar_url());
        return accountDao.updateAccount(account);
    }

    public int resetEmail(Long userid, String newEmail){
        return accountDao.resetEmail(userid, newEmail);
    }

    public int resetPassword(Long userid, String newPassword){
        return accountDao.resetPassword(userid,newPassword);
    }

    public int updateAccountVIP(Account account){
        return accountDao.updateAccountVIP(account);
    }

    public int setAvatar(Long userid, String avatar_url){
        return accountDao.setAvatar(userid, avatar_url);
    }

    public int setLoginType(Long userid, String login_type){
        return accountDao.setLoginType(userid, login_type);
    }

    public int updateTokenVersion(Long userid){
        int token_version=accountDao.getAccountById(userid).getTokenVersion();
        return accountDao.updateTokenVersion(userid, token_version+1);
    }
}