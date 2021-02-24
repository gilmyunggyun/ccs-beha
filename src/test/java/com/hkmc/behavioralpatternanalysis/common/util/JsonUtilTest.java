package com.hkmc.behavioralpatternanalysis.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class JsonUtilTest {

    @Test
    void testStr2Map() throws Exception {
        String in = "{\"val\":\"1\"}";
        assertNotNull(JsonUtil.str2map(in));
    }

    @Test
    void testStr2MapNull() throws Exception {
        assertNull(JsonUtil.str2map(null));
    }

    @Test
    void testStr2ObjEx() throws Exception {
        String in = "{\"val\":\"1";
        JsonUtil.str2obj(in, Map.class);
    }

    @Test
    void testStr2Obj() throws Exception {
        String in = "{\"val\":\"1\"}";
        assertNotNull(JsonUtil.str2obj(in, Map.class));
    }

    @Test
    void testStr2ObjNull() throws Exception {
        assertNull(JsonUtil.str2obj(null, Map.class));
    }

    @Test
    void testStr2MapEx() throws Exception {
        String in = "{\"val\":\"1";
        JsonUtil.str2map(in);
    }

    @Test
    void testObj2Str() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("val", "1");

        assertNotNull(JsonUtil.obj2str(map));
    }

    @Test
    void testObj2StrNull() throws Exception {
        assertNull(JsonUtil.obj2str(null));
    }

    @Test
    void testObj2StrEx() throws Exception {
        JsonUtil.obj2str(new JsonUtil());
    }

}