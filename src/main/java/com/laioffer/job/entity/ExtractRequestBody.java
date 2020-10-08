package com.laioffer.job.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExtractRequestBody {

    @JsonProperty("data") // it will work if not use @JsonProperty here becasue has the same name
    public List<String> data;

    @JsonProperty("max_keywords") // snake case: underscore
    public int maxKeywords; // small camel case

    public ExtractRequestBody(List<String> data, int maxKeywords) {
        this.data = data;
        this.maxKeywords = maxKeywords;
    }
}
