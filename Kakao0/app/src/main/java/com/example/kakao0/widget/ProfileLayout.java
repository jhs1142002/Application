package com.example.kakao0.widget;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.NetworkImageView;
import com.example.kakao0.GlobalApplication;
import com.example.kakao0.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;

public class ProfileLayout extends FrameLayout {
    private MeV2ResponseCallback meV2ResponseCallback;

    private String email;
    private String phoneNumber;
    private String nickname;
    private String userId;
    private String birthDay;
    private String ageRange;
    private String gender;
    private String countryIso;
    private NetworkImageView profile;
    private NetworkImageView background;
    private TextView profileDescription;

    public ProfileLayout(Context context) {
        super(context);
        initView();
    }

    public ProfileLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProfileLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 사용자정보 요청 결과에 따른 callback을 설정한다.
     *
     * @param callback 사용자정보 요청 결과에 따른 callback
     */
    public void setMeV2ResponseCallback(final MeV2ResponseCallback callback) {
        this.meV2ResponseCallback = callback;
    }

    public void setUserInfo(@NonNull final MeV2Response response) {
        final UserAccount account = response.getKakaoAccount();
        setUserId(String.valueOf(response.getId()));
        if (account != null) {
            if (account.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                setEmail(getContext().getString(R.string.needs_account_email_scope));
            } else {
                setEmail(account.getEmail());
            }
            if (account.phoneNumberNeedsAgreement() == OptionalBoolean.TRUE) {
                setPhoneNumber(getContext().getString(R.string.needs_phone_number_scope));
            } else {
                setPhoneNumber(account.getPhoneNumber());
            }
            if (account.birthdayNeedsAgreement() == OptionalBoolean.TRUE) {
                setBirthDay(account.getBirthday());
            }
            if (account.getProfile().getProfileImageUrl() != null) {
                setProfileURL(account.getProfile().getProfileImageUrl());
            }
            if (account.getAgeRange() != null) {
                setAgeRange(account.getAgeRange());
            }
            if (account.getGender() != null) {
                setGender(account.getGender());
            }

            if (account.getProfile().getNickname() != null) {
                setNickname(account.getProfile().getNickname());
            }
        }
        updateLayout();
    }

    public void setEmail(final String email) {
        this.email = email;
        updateLayout();
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateLayout();
    }

    /**
     * 프로필 이미지에 대해 view를 update한다.
     *
     * @param profileImageURL 화면에 반영할 프로필 이미지
     */
    public void setProfileURL(final String profileImageURL) {
        if (profile != null && profileImageURL != null) {
            Application app = GlobalApplication.getGlobalApplicationContext();
            if (app == null)
                throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
            profile.setImageUrl(profileImageURL, ((GlobalApplication) app).getImageLoader());
        }
    }

    public void setBgImageURL(String bgImageURL) {
        if (bgImageURL != null) {
            Application app = GlobalApplication.getGlobalApplicationContext();
            if (app == null)
                throw new UnsupportedOperationException("needs com.kakao.GlobalApplication in order to use ImageLoader");
            background.setImageUrl(bgImageURL, ((GlobalApplication) app).getImageLoader());
        }
    }

    public void setDefaultBgImage(int imageResId) {
        if (background != null) {
            background.setBackgroundResource(imageResId);
        }
    }

    public void setDefaultProfileImage(int imageResId) {
        if (profile != null) {
            profile.setBackgroundResource(imageResId);
        }
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
        updateLayout();
    }

    /**
     * 별명 view를 update한다.
     *
     * @param nickname 화면에 반영할 별명
     */
    public void setNickname(final String nickname) {
        this.nickname = nickname;
        updateLayout();
    }

    public void setBirthDay(final String birthDay) {
        this.birthDay = birthDay;
        updateLayout();
    }

    public void setAgeRange(AgeRange ageRange) {
        if (ageRange != null) {
            this.ageRange = ageRange.getValue();
        }
    }

    public void setGender(Gender gender) {
        if (gender != null) {
            this.gender = gender.getValue();
        }
    }

    public void setBackground(NetworkImageView background) {
        this.background = background;
    }

    /**
     * 사용자 아이디 view를 update한다.
     *
     * @param userId 화면에 반영할 사용자 아이디
     */
    public void setUserId(final String userId) {
        this.userId = userId;
        updateLayout();
    }

    private void updateLayout() {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(email)) {
            builder.append(getResources().getString(R.string.com_kakao_profile_email)).append('\n').append(email).append('\n');
        }
        if (!TextUtils.isEmpty(phoneNumber)) {
            builder.append(getResources().getString(R.string.com_kakao_profile_phone_number)).append('\n').append(phoneNumber).append('\n');
        }
        if (nickname != null && nickname.length() > 0) {
            builder.append(getResources().getString(R.string.com_kakao_profile_nickname)).append("\n").append(nickname).append("\n");
        }

        if (userId != null && userId.length() > 0) {
            builder.append(getResources().getString(R.string.com_kakao_profile_userId)).append("\n").append(userId).append("\n");
        }
        if (gender != null) {
            builder.append(getResources().getString(R.string.com_kakao_profile_gender)).append(" ").append(gender).append("\n");
        }
        if (ageRange != null) {
            builder.append(getResources().getString(R.string.com_kakao_profile_age_range)).append(" ").append(ageRange).append("\n");
        }
        if (birthDay != null && birthDay.length() > 0) {
            builder.append(getResources().getString(R.string.com_kakao_profile_birthday)).append(" ").append(birthDay);
        }
        if (countryIso != null) {
            builder.append(getResources().getString(R.string.kakaotalk_country_label)).append("\n").append(countryIso);
        }
        if (profileDescription != null) {
            profileDescription.setText(builder.toString());
        }
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_common_kakao_profile, this);

        profile = view.findViewById(R.id.com_kakao_profile_image);
        background = view.findViewById(R.id.background);
        profileDescription = view.findViewById(R.id.profile_description);
    }

    /**
     * 사용자 정보를 요청한다.
     */
    public void requestMe() {
        UserManagement.getInstance().me(meV2ResponseCallback);
    }
}
