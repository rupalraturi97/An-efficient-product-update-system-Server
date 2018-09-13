/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoserver;

import Dto.dto;
import MainClass.DtoServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class Map {

    private static Logger logger = Logger.getLogger(Map.class);
    public static ResultSet resultset;
    public static String files[] = new String[4];
    public static String md5ForAllFilesOnServer[] = new String[4];
    public static String systemConfigForAllFilesOnServer[] = new String[4];
    public static String locationForAllFilesOnServer[] = new String[4];
    public static int isdeletedForAllFilesOnServer[] = new int[4];
    public static int isSyncRequiredForAllFilesOnServer[] = new int[4];

    public static void databaseMapUpdate() {
        String filename = null;
        int id;
        String location = null;
        String md5 = null;
        String systemConfig = null;
        int isDeleted = 0;
        int isSyncRequired = 0;
//        String allFilesOnServer = "";
//        String allMd5OnServer = "";
//        String allLocationOnServer = "";
//        String allSystemConfigOnServer = "";
//        String allIsDeletedOnServer = "";
//        String allIsSyncRequiredOnServer = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/files", "root", "root");
            Statement statement = connection.createStatement();
            id = 1;
            while (id <= 4) {
                resultset = statement.executeQuery("select * from filedetails where id=" + id);
                while (resultset.next()) {
                    filename = resultset.getString(2);
                    files[id - 1] = filename;
                    md5 = resultset.getString(3);
                    systemConfig = resultset.getString(4);
                    location = resultset.getString(5);
                    isDeleted = resultset.getInt(6);
                    isSyncRequired = resultset.getInt(7);
                }
                dto obj = new dto();
                obj.setId(id);
                obj.setMd5(md5);
                md5ForAllFilesOnServer[id - 1] = obj.getMd5();
                obj.setLocation(location);
                locationForAllFilesOnServer[id - 1] = obj.getLocation();
                obj.setSystemConfig(systemConfig);
                systemConfigForAllFilesOnServer[id - 1] = obj.getSystemConfig();
                obj.setIsDeleted(isDeleted);
                isdeletedForAllFilesOnServer[id - 1] = obj.getIsDeleted();
                obj.setIsSyncRequired(isSyncRequired);
                isSyncRequiredForAllFilesOnServer[id - 1] = obj.getIsSyncRequired();
                DtoServer.map.put(filename, obj);
                id++;
                logger.info("Database and map updated");
            }
          
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
