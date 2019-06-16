package emschenn.csie.ncku.webparsetest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "card_table")
public class cardData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "card")
    private String title;
    private String episode;
    private String website;

    public cardData(@NonNull String title,String website,String episode) {
        this.title = title;
        this.episode = episode;
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }

    public String getEpisode() {
        return episode;
    }
}

