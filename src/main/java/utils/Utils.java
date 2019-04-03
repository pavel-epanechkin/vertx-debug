package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RawMessageInfo;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Object getObjectFromBytes(byte[] bytes, Class objectClass) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (bytes != null) {
            try {
                return objectMapper.readValue(bytes, objectClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] getBytesFromObject(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (object != null) {
            try {
                return objectMapper.writeValueAsBytes(object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Object getObjectFromJsonString(String json, Class objectClass) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (json != null) {
            try {
                return objectMapper.readValue(json, objectClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String getMD5Hash(Object object) {
        String result = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(object.toString().getBytes());
            byte[] digest = messageDigest.digest();
            result = DatatypeConverter.printBase64Binary(digest);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getMD5Hash(List<Object> objects) {
        String result = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            for (Object object : objects) {
                messageDigest.update((object.toString() + "_").getBytes());
            }
            byte[] digest = messageDigest.digest();
            result = DatatypeConverter.printBase64Binary(digest);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }
}
