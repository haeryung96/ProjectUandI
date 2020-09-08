package org.techtown.uiproject.mypage;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//네아로 연동
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

//카카오톡 연동
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;


import org.techtown.uiproject.MainActivity;
import org.techtown.uiproject.R;

public class mypageActivity extends AppCompatActivity {

    Button LogInButton, RegisterButton ;
    EditText Email, Password ;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    MemberOpenHelper memberOpenHelper;
    Cursor cursor;
    String TempPassword = "NOT_FOUND" ;
    public static final String UserEmail = "";

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

        LogInButton = (Button)findViewById(R.id.buttonLogin);
        RegisterButton = (Button)findViewById(R.id.buttonRegister);
        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        memberOpenHelper = new MemberOpenHelper(this);

        //버튼에 클릭 이벤트주기 & 로그
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 에디트 택스트가 비었는지 아닌지 체크하는 함수
                CheckEditTextStatus();

                // 로그인 함수
                LoginFunction();


            }
        });

        //회원가입 버튼에 클릭 이벤트
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
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
    //로그인 기능의 부분
    public void LoginFunction(){
        if(EditTextEmptyHolder) {
            // 데이터베이스 쓰기
            sqLiteDatabaseObj = memberOpenHelper.getWritableDatabase();
            // 커서에 이메일 찾는 쿼리 추가
            cursor = sqLiteDatabaseObj.query(memberOpenHelper.TABLE_NAME, null, " " + memberOpenHelper.Table_Column_2_Email + "=?",
                    new String[]{EmailHolder}, null, null, null);
            while (cursor.moveToNext()) {

                if (cursor.isFirst()) {

                    cursor.moveToFirst();

                    // Storing Password associated with entered email.
                    TempPassword = cursor.getString(cursor.getColumnIndex(memberOpenHelper.Table_Column_3_Password));

                    // Closing cursor.
                    cursor.close();
                }
            }

            // 마지막 결과를 체크하기 위해 불러온 함수
            CheckFinalResult();

        }
        else {

            //만약 로그인 에디트텍스트가 비어있으면 실행하지 못한다고 알려주는 토스트
            Toast.makeText(getApplicationContext(),"이메일과 비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show();

        }

    }

    //editText가 비었는지 아닌지 체크하는 부분
    public void CheckEditTextStatus(){

        //모든 에디트 택스트로부터 value 얻어오고 저장
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        //에디트 텍스트가 비었는지 아닌지 확인
        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }
    //데이터베이스 안의 이메일의 비밀번호와 사용자가 누른 비밀번호가 같은지 확인하는 부분
    public void CheckFinalResult(){

        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {
            Toast.makeText(getApplicationContext(),"로그인 성공!",Toast.LENGTH_LONG).show();
            //로그인 성공과 동시에 dash로 이동
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            //DashBoard 액티비티에 인텐트사용을 알려줌
            intent.putExtra(UserEmail, EmailHolder);
            startActivity(intent);

        }
        else {
            Toast.makeText(getApplicationContext(),"이메일 혹은 비밀번호가 잘못 입력되었습니다. 다시 입력해주세요.",Toast.LENGTH_LONG).show();
        }
        TempPassword = "미가입입니다." ;
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