/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainClass;

import dtoserver.Map;
import dtoserver.socketAccept;
import exception.InvalidPacketException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Anil
 */
public class DtoServer {
    private static Logger logger=Logger.getLogger(DtoServer.class);
    public static HashMap<String, Object> map;
    public Socket socket = null;
    //public ServerSocket serversocket;

    public static void main(String[] args) throws IOException, InterruptedException, InvalidPacketException {
        try {
            map = new HashMap<String, Object>();
            Map.databaseMapUpdate();
            ServerSocket serversocket = new ServerSocket(1234);
            logger.info("SERVER READY");

            Thread socketAccept = new Thread(new socketAccept(serversocket));
            socketAccept.start();
            socketAccept.join();
        } catch (Exception e) {
            logger.info(e);
        }
    }

}
