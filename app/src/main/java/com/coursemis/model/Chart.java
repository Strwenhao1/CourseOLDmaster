package com.coursemis.model;

import java.io.Serializable;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -:- |||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * <p>
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Created by zhxchao on 2018/2/28.
 */

public class Chart implements Serializable {
    private int[] year;
    private int[][] grade;
    private double[] point;
    private double[] years;
    private int[] personnum ;

    public int[] getPersonnum() {
        return personnum;
    }

    public void setPersonnum(int[] personnum) {
        this.personnum = personnum;
    }

    public void setPoint(double[] point) {
        this.point = point;
    }

    public double[] getYears() {
        return years;
    }

    public void setYears(double[] years) {
        this.years = years;
    }


    public int[] getYear() {
        return year;
    }

    public void setYear(int[] year) {
        this.year = year;
    }

    public int[][] getGrade() {
        return grade;
    }

    public void setGrade(int[][] grade) {
        this.grade = grade;
    }

    public double[] getPoint() {
        return point;
    }
}
