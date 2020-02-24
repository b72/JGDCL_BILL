package com.mamun72.billarApi;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.HashMap;

public interface ApiRequest {

    public String getRequest(HashMap<String, String> queryParam) throws IOException;
    public String jsonPost(PostBody postBody) throws IOException;
}
