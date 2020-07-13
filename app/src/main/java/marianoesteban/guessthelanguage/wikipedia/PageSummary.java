package marianoesteban.guessthelanguage.wikipedia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageSummary {

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("extract")
    @Expose
    private String extract;

    public String getLang() {
        return lang;
    }

    public String getExtract() {
        return extract;
    }
}
