package dag.mobillabb4.Model;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

/**
 * Created by Dag on 1/13/2018.
 */

public class MapUserModel {

    private MarkerOptions marker;
    private AccountModel owner;
    private static LinkedList<MapUserModel> mapUsers = new LinkedList<>();

    public MapUserModel(MarkerOptions marker, AccountModel owner) {
        this.marker = marker;
        this.owner = owner;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public AccountModel getOwner() {
        return owner;
    }

    public void setOwner(AccountModel owner) {
        this.owner = owner;
    }

    public static LinkedList<MapUserModel> getMapUsers() {
        return mapUsers;
    }

    public static void setMapUsers(LinkedList<MapUserModel> mapUsers) {
        MapUserModel.mapUsers = mapUsers;
    }
}
