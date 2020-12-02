package com.example.kakao0;

import java.util.HashMap;
import java.util.Map;

public class KakaoTalkMessageBuilder {
    public final Map<String, String> messageParams = new HashMap<String, String>();

    public KakaoTalkMessageBuilder addParam(String key, String value) {
        messageParams.put("${" + key + "}", value);
        return this;
    }

    public Map<String, String> build() {
        return messageParams;
    }
}
