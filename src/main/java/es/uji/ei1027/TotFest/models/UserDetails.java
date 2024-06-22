package es.uji.ei1027.TotFest.models;

import java.util.List;

public class UserDetails {
    private String username;
    private String password;
    private UserType userType;
    private List<String> userExtraInfo;

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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setUserExtraInfo(List<String> userExtraInfo) {
        this.userExtraInfo = userExtraInfo;
    }

    public List<String> getUserExtraInfo() { return this.userExtraInfo;}
}

