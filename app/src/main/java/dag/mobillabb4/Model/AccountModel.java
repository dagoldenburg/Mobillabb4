package dag.mobillabb4.Model;

import java.util.Date;

import dag.mobillabb4.Firebase.Messages;

/**
 * Created by Dag on 1/2/2018.
 */

public class AccountModel {
    private int id;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Date birthday;

    public AccountModel(String username, String password){
        this.username = username;
        this.password = password;
    }

    public AccountModel(String username, String password, String confirmPassword, String email, Date birthday) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.birthday = birthday;
    }

    public static boolean login(String email, String password){
        //TODO: querry db for login

        int msgId = Messages.login(email,password);
        if(email.equals("asd")&&password.equals("asd"))
            return true;
        return false;
    }

    public static boolean register(AccountModel account,boolean work){
        //TODO: querry db for register

            return work;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
