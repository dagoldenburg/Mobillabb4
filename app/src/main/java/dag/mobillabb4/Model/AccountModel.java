package dag.mobillabb4.Model;

import java.util.ArrayList;
import java.util.Date;

import dag.mobillabb4.Firebase.Messages;

/**
 * Created by Dag on 1/2/2018.
 */

public class AccountModel {
    private int id;
    private String username;
    private String password;
    private String email;
    private Date birthday;
    private static AccountModel myAccount;
    private static AccountModel targetAccount;

    public static AccountModel getMyAccount() {
        return myAccount;
    }

    public static void initMyAcc(int id){
        myAccount = new AccountModel();
        myAccount.id = id;
    }

    public static AccountModel getTargetAccount() {
        return targetAccount;
    }

    public static void setTargetAccount(AccountModel targetAccount) {
        AccountModel.targetAccount = targetAccount;
    }
    static ArrayList<AccountModel> conversations = new ArrayList<>();

    public static ArrayList<AccountModel> getConversations() {
        return conversations;
    }

    public static void setConversations(ArrayList<AccountModel> conversations) {
        AccountModel.conversations = conversations;
    }

    static ArrayList<AccountModel> filteredConversations = new ArrayList<>();

    public static ArrayList<AccountModel> getFilteredConversations() {
        return filteredConversations;
    }

    public static void setFilteredConversations(ArrayList<AccountModel> filteredConversations) {
        AccountModel.filteredConversations = filteredConversations;
    }

    public static void filterConversations(String filterString){
        if(filterString.equals("")){
            setFilteredConversations(conversations);
        }
        for(AccountModel am : conversations){
            if(am.getUsername().startsWith(filterString)){
                filteredConversations.add(am);
            }
        }
    }

    public AccountModel(){

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

    public AccountModel(String username, String password, String email, Date birthday) {
        this.username = username;
        this.password = password;
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
