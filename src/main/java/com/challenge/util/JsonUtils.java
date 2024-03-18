package com.challenge.util;

import com.challenge.config.event.EventData;
import com.challenge.exception.BusinessException;
import com.challenge.exception.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.Serializable;
import java.util.Map;

public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    public static Object base64ToObjectJson(String bytesEncoded, String classNameData) {
        return readStringJsonToObject(bytesEncoded == null ? "{}" : new String(Base64.decodeBase64(bytesEncoded)), classNameData);
    }

    public static Object readStringJsonToObject(String jsonStr, String classNameData) {
        try {
            return objectMapper.readValue(jsonStr, Class.forName(classNameData));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MessageCode.ERROR_AT_PROCESS_JSON);
        }
    }

    public static EventData convertMapSerializableToObject(Map<String, Serializable> data, String classNameData) {
        try {
            return (EventData) objectMapper.convertValue(data, Class.forName(classNameData));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException(MessageCode.ERROR_AT_PROCESS_MAP_TO_OBJECT);
        }
    }

    public static Map convertStringJsonToMap(String jsonStr) {
        try {
            if (jsonStr.startsWith("\"") && jsonStr.endsWith("\"") && jsonStr.length() > 2) {
                jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
                jsonStr = jsonStr.replaceAll("\\\\", "");
            }
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MessageCode.ERROR_AT_PROCESS_JSON);
        }
    }
}
