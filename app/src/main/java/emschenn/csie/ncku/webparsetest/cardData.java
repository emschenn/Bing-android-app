package emschenn.csie.ncku.webparsetest;

public class cardData {

    // Getter and Setter model for recycler view items
    private String title;
    private String episode;
    private String website;

    public cardData(String title,String website,String episode) {
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

