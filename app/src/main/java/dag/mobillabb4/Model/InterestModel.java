package dag.mobillabb4.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dag on 1/14/2018.
 */

public class InterestModel {

    private int id;
    private String name;

    public InterestModel(int id, String name) {
        this.id = id;
        this.name = name;
    }
    static ArrayList<InterestModel> userInterests = new ArrayList<>();
    static ArrayList<InterestModel> interests = new ArrayList<>();

    public static ArrayList<InterestModel> getUserInterests() {
        return userInterests;
    }

    public static void setUserInterests(ArrayList<InterestModel> userInterests) {
        InterestModel.userInterests = userInterests;
    }

    public static ArrayList<InterestModel> getInterests() {
        return interests;
    }


    public static void initInterests(JSONArray arr){
        interests = new ArrayList<>();
        for(int i = 0;i<arr.length();i++){
            try {
                JSONObject obj = arr.getJSONObject(i);
                interests.add(new InterestModel(Integer.parseInt(obj.get("id").toString()),obj.get("interest").toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean seeIfAlredyLiked(int id){
        for(InterestModel im : userInterests){
            if(interests.get(id).getId()==im.getId()){
                return true;
            }
        }
        return false;
    }

    public static void initUsersInterests(JSONArray arr){
        userInterests = new ArrayList<>();
        for(int i = 0;i<arr.length();i++){
            try {
                userInterests.add(new InterestModel(Integer.parseInt(arr.get(i).toString()),null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
