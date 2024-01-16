package com.company.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonHelper {
    public JsonHelper() {
    }

    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static String get(Object obj) {
        if (obj != null) {
            try {
                return getMapper().writeValueAsString(obj);
            } catch (IOException var2) {
            }
        }

        return null;
    }

    public static <T> T convert(Object obj, Class<T> cls) {
        if (obj != null && cls != null) {
            try {
                return getMapper().readValue(get(obj), cls);
            } catch (IOException var3) {
            }
        }

        return null;
    }

    public static <T> T convert(String json, Class<T> cls) {
        if (json != null && cls != null) {
            try {
                return getMapper().readValue(json, cls);
            } catch (IOException var3) {
            }
        }

        return null;
    }

    public static <T> List<T> convertList(String json, Class<T> cls) {
        if (json != null && cls != null) {
            try {
                ObjectMapper mapper = getMapper();
                JavaType type = mapper.getTypeFactory().constructParametricType(List.class, new Class[]{cls});
                return (List) mapper.readValue(json, type);
            } catch (IOException var4) {
            }
        }

        return null;
    }
}
