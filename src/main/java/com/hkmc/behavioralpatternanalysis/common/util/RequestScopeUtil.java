package com.hkmc.behavioralpatternanalysis.common.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestScopeUtil {

    private ThreadLocal<Map<String, Object>> attributeMap;

    public RequestScopeUtil() {
        if(attributeMap == null)
            attributeMap = ThreadLocal.withInitial( () -> new HashMap<>());
    }

    public Object getAttribute(final String name) {
        return attributeMap.get().get(name);
    }

    public <T> T getAttribute(final String name, final Class<T> targetClass) {
        return ( attributeMap.get().get(name) != null)?targetClass.cast(attributeMap.get().get(name)):null;
    }

    public void setAttribute(final String name, final Object object) {
        attributeMap.get().put(name, object);
    }

    public void removeAttribute(final String name) {
        attributeMap.get().remove(name);
    }

}
