package org.techtown.uiproject.mypage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//네아로 연동
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

//카카오톡 연동
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

//파이어베이스 연동
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import org.techtown.uiproject.R;

public class mypageActivity extends AppCompatActivity {

    //파이어베이스 연동을 위한 부분
    Button mLoginBtn, mResigeterBtn;
    EditText mEmailText, mPasswordText;
    private FirebaseAuth firebaseAuth;

    //네아로 연동 부분
    public static OAuthLogin mOAuthLoginInstance;
    public static OAuthLoginButton mOAuthLoginButton;
    public static Context mContext;
    private static String OAUTH_CLIENT_ID = "VK2nqfT5xr6i3_72M2IC";
    private static String OAUTH_CLIENT_SECRET = "Na_BHeawcc";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    //카카오톡 연동 부분
    private LoginButton btn_login;
    Session session;
    private SessionCallback sessionCallback = new SessionCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //파이어베이스 연동 부분
        firebaseAuth =  FirebaseAuth.getInstance();

        //버튼 등록하기
        mResigeterBtn = findViewById(R.id.buttonRegister);
        mLoginBtn = findViewById(R.id.buttonLogin);
        mEmailText = findViewById(R.id.editEmail);
        mPasswordText = findViewById(R.id.editPassword);

        //가입 버튼이 눌리면
        mResigeterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(mypageActivity.this,Register.class));
            }
        });

        //로그인 버튼이 눌리면
        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(mypageActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // 로그인 성공시 페이지를 어디로 옮길것인가에 대한 코드
                                    //Intent intent = new Intent(MainActivity.this, BottomNaviActivity.class);
                                    //startActivity(intent);
                                    Toast.makeText(mypageActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mypageActivity.this,"로그인 오류",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //네아로 연동 부분
        mContext = mypageActivity.this;

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext,OAUTH_CLIENT_ID,OAUTH_CLIENT_SECRET,OAUTH_CLIENT_NAME);

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.button_naver);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

    }


    //네아로 연동 부분
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if(success){
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
            }else{
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                Toast.makeText(mContext,"errorcode:"+errorCode+",errorDesc:"+errorDesc,Toast.LENGTH_LONG);

            }
        }
    };

    //카카오톡 연동 부분
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}