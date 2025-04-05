package com.csu.bakery.model;

public class Account {
    private Long userid;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int gender;
    private int age;
    private String addr1;
    private String addr2;
    private int VIPLevel;
    private String avatar_url;
    private String login_type;
    private int token_version;

    public Account() {}

    public Account(Long userid) {
        this.userid = userid;
    }

    public Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Account(Long userid, String email, String password) {
        this.userid = userid;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public int getTokenVersion() {
        return token_version;
    }

    public void setTokenVersion(int tokenVersion) {
        this.token_version = tokenVersion;
    }
}
