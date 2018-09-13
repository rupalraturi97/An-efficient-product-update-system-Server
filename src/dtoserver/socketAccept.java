/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoserver;

import Constants.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import packet.DataPacket;
import tcp.TCPProcessor;

/**
 *
 * @author Anil
 */
public class socketAccept implements Runnable {

    private static Logger logger = Logger.getLogger(socketAccept.class);
    Socket socket;
    ServerSocket serversocket;
    InputStream inputstream = null;
    OutputStream outputstream = null;

    public socketAccept(ServerSocket serversocket) {
        this.serversocket = serversocket;
    }

    @Override
    public void run() {
        DataPacket receivedFromUpdateMessageType = null;
        try {
            while (true) {
                socket = serversocket.accept();
                inputstream = socket.getInputStream();
                outputstream = socket.getOutputStream();
                DataPacket receivePacket = TCPProcessor.receive(inputstream);
                DataPacket responsePacket = new DataPacket();
                if (receivePacket.getMessageType() == Constants.UPDATE) //UPDATE
                {
                    receivedFromUpdateMessageType = messageTypeUpdate.updateMessageType(receivePacket, responsePacket);
                    TCPProcessor.send(outputstream, receivedFromUpdateMessageType);

                } else if (receivePacket.getMessageType() == Constants.FILE_UPDATE) //FILE UPDATE
                {
                    receivedFromUpdateMessageType = messageTypeFileUpdate.fileUpdateMessageType(receivePacket, responsePacket);
                    TCPProcessor.send(outputstream, receivedFromUpdateMessageType);

                }
            }

        } catch (Exception e) {
            logger.info(e);
        } finally {
            try {
                inputstream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(socketAccept.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                outputstream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(socketAccept.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
