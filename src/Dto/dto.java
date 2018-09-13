/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anil
 */
public class dto {

    // public static dto obj;
    private String md5;
    private int id;
    private String systemConfig;
    private String location;
    private int isDeleted;
    private int isSyncRequired;
   
   
public void setIsSyncRequired(int isSyncRequired) {
        this.isSyncRequired = isSyncRequired;
    }

    public int getIsSyncRequired() {
        return isSyncRequired;
    }

    
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(String systemConfig) {
        this.systemConfig = systemConfig;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

}
