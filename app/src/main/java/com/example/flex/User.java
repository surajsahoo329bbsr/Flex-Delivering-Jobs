package com.example.flex;

public class User {

    private String userId;
    private String userName;
    private String userMail;
    private String userPhone;
    private int userDLFlag ;
    private int userPhotoFlag ;

    //Constructor
    //Function Overloading(Look it up)

    //Another constructor to initialize our varibales
    User(String userId, String userName, String userMail, String userPhone, int userDLFlag, int userPhotoFlag) {
        this.userId = userId;
        this.userName = userName;
        this.userMail = userMail;
        this.userPhone = userPhone;
        this.userDLFlag = userDLFlag;
        this.userPhotoFlag = userPhotoFlag;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public int getUserDLFlag() { return userDLFlag; }

    public int getUserPhotoFlag() { return userPhotoFlag; }
}
