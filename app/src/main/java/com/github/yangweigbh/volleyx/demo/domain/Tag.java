
package com.github.yangweigbh.volleyx.demo.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * 
     * @return
     *     The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
