package com.laioffer.job.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Item { // only return the ones we want
    private String id;
    private String title;
    private String location;
    private String companyLogo;
    private String url;
    private String description ;
    private Set<String> keywords; // for later extraction
    private boolean favorite;


    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }
    @JsonProperty("company_logo")
    public String getCompanyLogo() {
        return companyLogo;
    }
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
    @JsonProperty("description")
    public String getDescription() {
       // int maxLength = 2500;
       // if (description.length() > maxLength) description = description.substring(0, maxLength);
        return description;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite){
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(title, item.title) &&
                Objects.equals(location, item.location) &&
                Objects.equals(companyLogo, item.companyLogo) &&
                Objects.equals(url, item.url) &&
                Objects.equals(description, item.description) &&
                Objects.equals(keywords, item.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, location, companyLogo, url, description, keywords);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", keywords=" + keywords +
                '}';
    }

    // builder v.s. name parameter
    // builder: avoid mistakes in orde
        public static class Builder {
            private String id;
            private String title;
            private String location;
            private String companyLogo;
            private String url;
            private String description;
            private Set<String> keywords;
            private Boolean favorite;

        public Builder id(String id) {
                this.id = id;
                return this;
            }

            public Builder title(String title) {
                this.title = title;
                return this;
            }

            public Builder location(String location) {
                this.location = location;
                return this;
            }

            public Builder companyLogo(String companyLogo) {
                this.companyLogo = companyLogo;
                return this;
            }

            public Builder url(String url) {
                this.url = url;
                return this;
            }

            public Builder description(String description) {

                this.description = description;
                return this;
            }

            public Builder keywords(Set<String> keywords) {
                this.keywords = keywords;
                return this;

            }

            public Item build() {
                Item item = new Item();
                item.id = id;
                item.title = title;
                item.location = location;
                item.companyLogo = companyLogo;
                item.url = url;
                item.description = description;
                item.keywords = keywords;
                item.favorite = favorite;
                return item;

            }

        public Builder favorite(Boolean favorite) {
                       this.favorite = favorite;
                       return this;
            }
        }

    }
