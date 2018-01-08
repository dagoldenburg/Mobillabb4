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
    private static AccountModel myAccount;

    public static AccountModel getMyAccount() {
        return myAccount;
    }

    public AccountModel(){

    }

    /**
     * Används när använder har autensierat sig mot backend
     * @param id
     */
    public AccountModel(int id){
        myAccount = new AccountModel();
        myAccount.id = id;
    }

    /**
     * Används för add skapa konversationer i MainChatActivity
     * @param id
     * @param username
     */
    public AccountModel(int id,String username){
        this.id=id;
        this.username=username;
    }
    /**
     * Används för att logga in
     * @param username
     * @param password
     */
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
