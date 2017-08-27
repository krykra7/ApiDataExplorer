package pl.krystiankrawczyk.apidataexplorer.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.krystiankrawczyk.apidataexplorer.api.model.Person;

/**
 * Created by Krystian Krawczyk on 27.08.2017.
 */

public class UserSession {

    private static final String PREFERENCES_KEY = "preferences";
    private static final String PERSON_LIST_KEY = "people";
    private static final String DEFAULT_EMPTY_STRING = "";

    private static UserSession userSession;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private List<Person> listToRemember = new ArrayList<>();

    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static UserSession getInstance(Context context) {
        if (userSession == null) {
            userSession = new UserSession(context);
        }
        return userSession;
    }

    public List<Person> getPersonList() {
        return listToRemember;
    }

    public void setPersonList(List<Person> listToRemember) {
        this.listToRemember = listToRemember;
    }

    public void savePeopleList() {
        editor.putString(PERSON_LIST_KEY, new Gson().toJson(listToRemember)).apply();
    }

    public void retrieveListFromPreferences() {
        Type personType = new TypeToken<ArrayList<Person>>() {
        }.getType();
        if (sharedPreferences.getString(PERSON_LIST_KEY, DEFAULT_EMPTY_STRING).isEmpty()) {
            listToRemember = new Gson().fromJson(sharedPreferences
                    .getString(PERSON_LIST_KEY, DEFAULT_EMPTY_STRING), personType);
        }
    }
}
