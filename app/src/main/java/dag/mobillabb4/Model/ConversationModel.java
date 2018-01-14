package dag.mobillabb4.Model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Dag on 1/12/2018.
 */

public class ConversationModel {

    private String message;
    private String date;
    private AccountModel owner;

    public ConversationModel(String message, String date, AccountModel owner) {
        this.message = message;
        this.date = date;
        this.owner = owner;
    }

    public static ArrayList<ConversationModel> conversations = new ArrayList<>();

    public synchronized static ArrayList<ConversationModel> getConversations() {
        return conversations;
    }

    public static void setConversations(ArrayList<ConversationModel> conversations) {
        ConversationModel.conversations = conversations;
    }

    static ArrayList<ConversationModel> filteredConversations = new ArrayList<>();

    public static ArrayList<ConversationModel> getFilteredConversations() {
        return filteredConversations;
    }

    public static void setFilteredConversations(ArrayList<ConversationModel> filteredConversations) {
        ConversationModel.filteredConversations = filteredConversations;
    }

    public static void removeConversation(int idToDelete){
        ConversationModel accToRemove = null;
        for(ConversationModel am : conversations){
            if(am.getOwner().getId()== idToDelete){
                accToRemove = am;
            }
        }
        conversations.remove(accToRemove);
        setFilteredConversations(conversations);
    }

    public static void filterConversations(String filterString){
        filteredConversations = new ArrayList<>();
        Log.i("text",filterString);
        ArrayList<ConversationModel> temp = getConversations();
        if(filterString.equals("")){
            setFilteredConversations(conversations);
            return;
        }try {
            for (ConversationModel am : temp) {
                if (am.getOwner().getUsername().startsWith(filterString)) {
                    filteredConversations.add(am);
                }
            }
        }catch(NullPointerException e){
            Log.i("filterConversation",e.getMessage());
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AccountModel getOwner() {
        return owner;
    }

    public void setOwner(AccountModel owner) {
        this.owner = owner;
    }
}
