package org.techtown.uiproject.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.uiproject.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    EditText mEmailText, mPasswordText, mPasswordcheckText, mName; // 이름, 이메일 , 패스워드, 패스워드 확인
    Button mRegisterBtn; //회원가입 버튼
    private FirebaseAuth firebaseAuth; // 파이어베이스 접근을 위해 정의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //액션 바 등록하기 나중에 필요 없으면 지우면 된다.
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘 */

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();

        mName = findViewById(R.id.editName);
        mEmailText = findViewById(R.id.editEmail);
        mPasswordText = findViewById(R.id.editPassword);
        mPasswordcheckText = findViewById(R.id.editPasswordCheck);
        mRegisterBtn = findViewById(R.id.buttonRegister);

        // 파이어베이스 user로 접근

        //가입 버튼 클릭 리스너 -> firebase에 데이터를 저장한다.
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                String pwdcheck = mPasswordcheckText.getText().toString().trim();

                // 패스워드와 check패스워드가 같을 경우, 회원가입을 실행한다.
                if(pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = mName.getText().toString().trim();

                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                //해쉬맵은 테이블 개념과 똑같다. 회원 정보 데이터를 다른 곳에서도 쓰려면 이것을 디비에 저장해야 한다.
                                //이떄 해쉬맵을 이용하여 테이블 형식으로 한번에 집어넣으면 편리하다.

                                HashMap<Object,String> hashMapTable = new HashMap<>();

                                hashMapTable.put("uid",uid);
                                hashMapTable.put("email",email);
                                hashMapTable.put("name",name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMapTable);


                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(Register.this, mypageActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Register.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                Toast.makeText(Register.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.
                            }

                        }
                    });

                    //비밀번호 오류시
                }else{
                    Toast.makeText(Register.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
/*
    public boolean onSupportNavigateUp(){
        onBackPressed();; // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }*/


}