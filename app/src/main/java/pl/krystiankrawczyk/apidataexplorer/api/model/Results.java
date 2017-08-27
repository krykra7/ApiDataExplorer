package pl.krystiankrawczyk.apidataexplorer.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class Results {

    @SerializedName("results")
    @Expose
    private List<Person> results = null;

    public List<Person> getResults() {
        return results;
    }

    public void setResults(List<Person> results) {
        this.results = results;
    }
}
