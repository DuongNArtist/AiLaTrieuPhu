package com.skynet.ailatrieuphu.models;

public class AccountModel {

    private static AccountModel mInstance;
    private final static String regularExpression = ";-;";
    private final static int NUM_FIELD = 6;

    private String username;
    private String password;
    private String email;
    private String name;
    private String birthDay;
    private String address;

    private AccountModel() {

    }

    public static AccountModel getInstance() {
        if (mInstance == null) {
            mInstance = new AccountModel();
        }
        return mInstance;
    }

    public String toString() {
        String string = genString(username) + regularExpression;
        string += genString(password) + regularExpression;
        string += genString(email) + regularExpression;
        string += genString(name) + regularExpression;
        string += genString(birthDay) + regularExpression;
        string += genString(address);
        return string;
    }

    public boolean fromString(String source) {
        try {
            String[] arr = source.split(regularExpression);
            if (arr == null || arr.length != NUM_FIELD) {
                return false;
            }
            int idx = 0;
            username = arr[idx++];
            password = arr[idx++];
            email = arr[idx++];
            name = arr[idx++];
            birthDay = arr[idx++];
            address = arr[idx++];
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String genString(String source) {
        String resource = source;
        if (resource == null || "".equals(resource)) {
            resource = " ";
        }
        return resource;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
