package com.example.entity;

/**
 * 瑙掕壊鐢ㄦ埛鐖剁被
 */
public class Account {
    private Integer id;
    /** 鐢ㄦ埛鍚?*/
    private String username;
    /** 鍚嶇О */
    private String name;
    /** 瀵嗙爜 */
    private String password;
    /** 瑙掕壊鏍囪瘑 */
    private String role;
    /** 鏂板瘑鐮?*/
    private String newPassword;
    /** 澶村儚 */
    private String avatar;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}

