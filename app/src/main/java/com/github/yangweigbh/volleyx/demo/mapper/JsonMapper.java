package com.github.yangweigbh.volleyx.demo.mapper;

import com.github.yangweigbh.volleyx.demo.domain.Music;
import com.github.yangweigbh.volleyx.demo.domain.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by yangwei on 2016/10/30.
 */

public class JsonMapper {
    Gson gson;

    public JsonMapper() {
        this.gson = new Gson();
    }

    public List<Music> transformJsonToMusicCollection(String string) {
        Result result = gson.fromJson(string, Result.class);
        return result.getMusics();
    }
}
