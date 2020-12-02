package com.example.kakao0;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kakao0.widget.KakaoToast;
import com.example.kakao0.widget.ProfileLayout;
import com.kakao.friends.request.FriendsRequest;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class KakaoTalkMainActivity extends BaseActivity {
    private ProfileLayout profileLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
        onClickProfile();
    }

    // profile view에서 talk profile을 update 한다.
    private void applyTalkProfileToView(final KakaoTalkProfile talkProfile) {
        if (profileLayout != null) {
            final String profileImageURL = talkProfile.getProfileImageUrl();
            if (profileImageURL != null)
                profileLayout.setProfileURL(profileImageURL);

            final String nickName = talkProfile.getNickName();
            if (nickName != null)
                profileLayout.setNickname(nickName);
        }
    }

    private void onClickProfile() {
        KakaoTalkService.getInstance().requestProfile(new KakaoTalkResponseCallback<KakaoTalkProfile>() {
            @Override
            public void onSuccess(KakaoTalkProfile result) {
                KakaoToast.makeToast(getApplicationContext(), "success to get talk profile", Toast.LENGTH_SHORT).show();
                applyTalkProfileToView(result);
            }

            @Override
            public void onNotKakaoTalkUser() {
                super.onNotKakaoTalkUser();
                if (profileLayout != null) {
                    profileLayout.setUserId("Not a KakaoTalk user");
                }
            }
        });
    }

    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //redirectLoginActivity();
            }
        });
    }

    private void initializeView() {
        setContentView(R.layout.layout_kakaotalk_main);
        initializeButtons();
        initializeProfileView();
    }

    private void initializeButtons() {
        findViewById(R.id.profile_button).setOnClickListener(v -> onClickProfile());

        findViewById(R.id.talk_friends).setOnClickListener(v -> showTalkFriendListActivity());

        //findViewById(R.id.logout_button).setOnClickListener(v -> onClickLogout());

        findViewById(R.id.talk_chat_list).setOnClickListener(v -> showChatListActivity());
    }

    private void showChatListActivity() {
        Intent intent = new Intent(this, KakaoTalkChatListActivity.class);
        startActivity(intent);
    }

    private void showTalkFriendListActivity() {
        Intent intent = new Intent(this, KakaoTalkFriendListActivity.class);

        String[] friendType = {FriendsRequest.FriendType.KAKAO_TALK.name()};
        intent.putExtra(FriendsMainActivity.EXTRA_KEY_SERVICE_TYPE, friendType);
        startActivity(intent);
    }

    private void initializeProfileView() {
        profileLayout = findViewById(R.id.com_kakao_user_profile);
        profileLayout.setDefaultBgImage(R.drawable.bg_image_02);
        profileLayout.setDefaultProfileImage(R.drawable.thumb_talk);
        ((TextView)findViewById(R.id.text_title)).setText(getString(R.string.text_kakaotalk));

        findViewById(R.id.title_back).setOnClickListener(v -> finish());
    }

    public abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T> {

        @Override
        public void onNotKakaoTalkUser() {
            KakaoToast.makeToast(getApplicationContext(), "not a KakaoTalk user", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(ErrorResult errorResult) {
            KakaoToast.makeToast(getApplicationContext(), "failure : " + errorResult, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            //redirectLoginActivity();
        }

        @Override
        public void onNotSignedUp() {
            //redirectSignupActivity();
        }

        @Override
        public void onDidStart() {
            showWaitingDialog();
        }

        @Override
        public void onDidEnd() {
            cancelWaitingDialog();
        }
    }
}
