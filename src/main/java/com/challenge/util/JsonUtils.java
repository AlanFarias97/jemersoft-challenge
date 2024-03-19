package com.challenge.util;

import com.challenge.exception.BusinessException;
import com.challenge.exception.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;


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

}
