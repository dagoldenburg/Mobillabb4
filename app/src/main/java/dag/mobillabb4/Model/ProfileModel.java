package dag.mobillabb4.Model;

import android.media.Image;

/**
 * Created by Dag on 1/4/2018.
 */

public class ProfileModel {

    private String name;
    private String biography;
    private Image profilePicture;
    private static final ProfileModel ourInstance = new ProfileModel();

    private ProfileModel() {}

    public static void setInstance(String name, String biography, Image profilePicture) {
        ourInstance.name = name;
        ourInstance.biography = biography;
        ourInstance.profilePicture = profilePicture;
    }
    static ProfileModel getInstance() {
        return ourInstance;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }
}
