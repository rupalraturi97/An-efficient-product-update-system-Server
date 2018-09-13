/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoserver;

import Constants.Constants;
import Dto.dto;
import MainClass.DtoServer;
import static dtoserver.Map.files;
import static dtoserver.Map.isSyncRequiredForAllFilesOnServer;
import static dtoserver.Map.isdeletedForAllFilesOnServer;
import static dtoserver.Map.locationForAllFilesOnServer;
import static dtoserver.Map.md5ForAllFilesOnServer;
import static dtoserver.Map.resultset;
import static dtoserver.Map.systemConfigForAllFilesOnServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import packet.DataPacket;

/**
 *
 * @author Anil
 */
public class messageTypeUpdate {

    private static Logger logger = Logger.getLogger(messageTypeUpdate.class);
    public static DataPacket updateMessageType(DataPacket packetReceived, DataPacket packetToBeSent) throws ClassNotFoundException, SQLException {
    try{    
            String allFilesOnServer = "";
            String allMd5OnServer = "";
            String allIsDeletedOnServer = "";
            String allLocationOnServer = "";
            String allSystemConfigOnServer="";
            String allIsSyncRequiredOnServer = "";
            String filesToBeChangedOnClient = "";
            String md5OfFilesToBeChangedOnClient = "";
            String operationTypeOfFilesToBeChangedOnClient = "";
            String locationOfAllFilesToBeChanged = "";
            //for comma seperated server files
              for (int i = 0; i < isdeletedForAllFilesOnServer.length; i++) {
                allIsDeletedOnServer += Integer.toString(isdeletedForAllFilesOnServer[i]) + ",";
            }
          //  logger.info(allIsDeletedOnServer);

            for (int i = 0; i < isSyncRequiredForAllFilesOnServer.length; i++) {
                allIsSyncRequiredOnServer += Integer.toString(isSyncRequiredForAllFilesOnServer[i]) + ",";
            }
            logger.info(allIsDeletedOnServer);

            for (int i = 0; i < files.length; i++) {
                allFilesOnServer += files[i] + ",";
            }
            logger.info(allFilesOnServer);
            for (int i = 0; i < md5ForAllFilesOnServer.length; i++) {
                allMd5OnServer += md5ForAllFilesOnServer[i] + ",";
            }
            logger.info(allMd5OnServer);
            for (int i = 0; i < locationForAllFilesOnServer.length; i++) {
                allLocationOnServer += locationForAllFilesOnServer[i] + ",";
            }
            logger.info(allLocationOnServer);
            for (int i = 0; i < md5ForAllFilesOnServer.length; i++) {
                allSystemConfigOnServer += systemConfigForAllFilesOnServer[i] + ",";
            }
            logger.info(allSystemConfigOnServer);
            
            String clientFilename = packetReceived.getString(Constants.FILE_NAME);
            String clientMd5 = packetReceived.getString(Constants.MD5);
            String[] filenamesOfClient = clientFilename.split(",");
            String[] md5OfClient = clientMd5.split(",");
            String[] filesOnServer = allFilesOnServer.split(",");
            String[] isDeletedOnServer = allIsDeletedOnServer.split(",");
            String[] locationsOnServer = allLocationOnServer.split(",");
            int isDeletedOnServerSide[] = new int[isDeletedOnServer.length];
            for (int i = 0; i < isDeletedOnServer.length; i++) {
                isDeletedOnServerSide[i] = Integer.parseInt(isDeletedOnServer[i]);
            }
            //operation type of changed md5 files=1
            //operation type of files to be deleted from client =2
            //operation type of files to be added on client =3

            for (int i = 0; i < filenamesOfClient.length; i++) {
                if (DtoServer.map.containsKey(filenamesOfClient[i]) && isDeletedOnServerSide[i] == 0) {
                    dto objectToCheckMd5OfClientAndServer = (dto) DtoServer.map.get(filenamesOfClient[i]);
                    String md5ForAFileOnServer = objectToCheckMd5OfClientAndServer.getMd5();
                    if ((objectToCheckMd5OfClientAndServer.getMd5().compareTo(md5OfClient[i]) == 0)) {
                        logger.info("No need for any update for file : " + filenamesOfClient[i]);
                    } else {
                        logger.info("File needs update as its md5 is different. file : " + filenamesOfClient[i]);
                        filesToBeChangedOnClient += filenamesOfClient[i] + ",";
                        md5OfFilesToBeChangedOnClient += md5ForAFileOnServer + ",";
                        operationTypeOfFilesToBeChangedOnClient += Integer.toString(1) + ",";
                        locationOfAllFilesToBeChanged += locationsOnServer[i] + ",";
                    }
                } else {
                    logger.info("File does'nt exist on server. Delete file : " + filenamesOfClient[i]);
                    filesToBeChangedOnClient += filenamesOfClient[i] + ",";
                    md5OfFilesToBeChangedOnClient += md5OfClient[i] + ",";
                    operationTypeOfFilesToBeChangedOnClient += Integer.toString(2) + ",";
                    locationOfAllFilesToBeChanged += locationsOnServer[i] + ",";

                }
            }
            //for extra files on server
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/files", "root", "root");
            Statement statement = connection.createStatement();
            resultset = statement.executeQuery("select filename,md5,location from filedetails where isSyncReq=" + 1);
            while (resultset.next()) {
                filesToBeChangedOnClient += resultset.getString(1) + ",";
                md5OfFilesToBeChangedOnClient += resultset.getString(2) + ",";
                locationOfAllFilesToBeChanged += resultset.getString(3) + ",";
                operationTypeOfFilesToBeChangedOnClient += Integer.toString(3) + ",";

            }

            logger.info("changed files are : " + filesToBeChangedOnClient);
            logger.info("changed md5 are : " + md5OfFilesToBeChangedOnClient);
            logger.info("operation types are : " + operationTypeOfFilesToBeChangedOnClient);
            logger.info("Locations of changed files are: " + locationOfAllFilesToBeChanged);

            packetToBeSent.setMessageType(Constants.UPDATE);
            packetToBeSent.setString(Constants.FILES_TO_BE_CHANGED, filesToBeChangedOnClient);
            packetToBeSent.setString(Constants.MD5_TO_BE_CHANGED, md5OfFilesToBeChangedOnClient);
            packetToBeSent.setString(Constants.OPERATION_TYPE_TO_BE_CHANGED, operationTypeOfFilesToBeChangedOnClient);
            packetToBeSent.setString(Constants.LOCATIONS_FOR_FILES_TO_BE_CHANGED, locationOfAllFilesToBeChanged);
            logger.info("packet needs to be returned");
            return packetToBeSent;
    }catch(Exception e)
    {
    logger.info(e);
    logger.info("sending the same packet received");
    return packetToBeSent;}
        
    }
    

}
