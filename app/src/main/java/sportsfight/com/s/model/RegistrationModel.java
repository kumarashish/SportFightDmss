package sportsfight.com.s.model;

/**
 * Created by Ashish.Kumar on 31-01-2018.
 */

public class RegistrationModel {
    String userName;
    String userMobile;
    String password;
    String userEmail;
    String role;
    Integer[] intGame=null;
    String gender="";


    public RegistrationModel(String userName, String userEmail, String userMobile, String password,String role) {
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.role=role;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public Integer[] getIntGame() {
        return intGame;
    }

    public void setIntGame(Integer[] intGame) {
        this.intGame = intGame;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }
}
