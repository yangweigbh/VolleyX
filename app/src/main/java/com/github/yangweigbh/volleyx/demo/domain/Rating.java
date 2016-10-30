
package com.github.yangweigbh.volleyx.demo.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("average")
    @Expose
    private String average;
    @SerializedName("numRaters")
    @Expose
    private Integer numRaters;
    @SerializedName("min")
    @Expose
    private Integer min;

    /**
     * 
     * @return
     *     The max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * 
     * @param max
     *     The max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * 
     * @return
     *     The average
     */
    public String getAverage() {
        return average;
    }

    /**
     * 
     * @param average
     *     The average
     */
    public void setAverage(String average) {
        this.average = average;
    }

    /**
     * 
     * @return
     *     The numRaters
     */
    public Integer getNumRaters() {
        return numRaters;
    }

    /**
     * 
     * @param numRaters
     *     The numRaters
     */
    public void setNumRaters(Integer numRaters) {
        this.numRaters = numRaters;
    }

    /**
     * 
     * @return
     *     The min
     */
    public Integer getMin() {
        return min;
    }

    /**
     * 
     * @param min
     *     The min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

}
