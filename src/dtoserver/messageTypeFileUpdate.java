/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoserver;

import Constants.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
import packet.DataPacket;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Anil
 */
public class messageTypeFileUpdate {

    private static final String algorithm = "AES";
    private static byte[] keyvalue;
    private static Logger logger = Logger.getLogger(messageTypeFileUpdate.class);

    public static DataPacket fileUpdateMessageType(DataPacket receivedPacket, DataPacket packetToBeSent) {
        try {
            logger.info("recieving from client for file update");
            logger.info("Encrypting contents of file");
            String Filename = receivedPacket.getString(Constants.FILE_NAME);
            String fileServer = Filename + ".txt";
            //Reading data
            String key = "Thats my Kung Fu";
            File fileToBeReadFromServer = new File("C:\\Users\\Anil\\Documents\\NetBeansProjects\\UpdateServer\\" + fileServer);
            logger.info("file read by server to encrypt");
            FileReader filereader = new FileReader(fileToBeReadFromServer);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            String readFromFile = bufferedreader.readLine();
            String encryptedText = fileEncrypt(key, readFromFile);
            logger.info("Encrypted text: " + encryptedText);

            packetToBeSent.setMessageType(Constants.FILE_UPDATE);
            packetToBeSent.setString(Constants.TEXT_CONTENT, encryptedText);
        } catch (Exception e) {
            logger.info(e);
        }
        return packetToBeSent;

    }

    public static String fileEncrypt(String keyGiven, String fileContentToBeEncrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        logger.info("in encrypt block");
        keyvalue = keyGiven.getBytes();
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        logger.info("block encrypt");
        byte[] encodedvalue = cipher.doFinal(fileContentToBeEncrypted.getBytes());
        String encryptedvalue = new BASE64Encoder().encode(encodedvalue);
        return encryptedvalue;

    }

    public static Key generateKey() {

        Key key = new SecretKeySpec(keyvalue, algorithm);
        return key;
    }

}
