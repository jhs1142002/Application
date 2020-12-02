package com.example.kakao0;

import com.kakao.kakaotalk.response.model.ChatInfo;

public interface IChatListCallback {
    void onItemSelected(int position, ChatInfo chatInfo);
    void onPreloadNext();
}
