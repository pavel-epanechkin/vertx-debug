package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RawMessageInfo;

import java.io.IOException;

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
}
