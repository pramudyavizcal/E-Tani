package com.pramudya.e_tani;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VerifikasiPesawahActivity extends AppCompatActivity {
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth fbAuth;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    Button btnverif,btnverifkode,btnkirimulang;
    TextView timer;
    EditText edtnohp;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String phoneNumber;

    BaseApp BaseApp;
    EditText edt1;
    EditText edt2;
    EditText edt3;
    EditText edt4;
    EditText edt5;
    EditText edt6;
    LinearLayout lvutama,lv2;

    DatabaseReference rootref;
    Query inboxQuery;
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);
        FirebaseApp.initializeApp(VerifikasiPesawahActivity.this);
        lv2=(LinearLayout)findViewById(R.id.lv2);
        lvutama=(LinearLayout)findViewById(R.id.lvutama);
        btnkirimulang=(Button)findViewById(R.id.btnkirimulang);
        btnverifkode=(Button)findViewById(R.id.btnverifkode);
        timer=(TextView) findViewById(R.id.timer);
        long startTime = 90 * 1000;
        long interval = 1000;
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        edt1=(EditText)findViewById(R.id.edt1);
        edt2=(EditText)findViewById(R.id.edt2);
        edt3=(EditText)findViewById(R.id.edt3);
        edt4=(EditText)findViewById(R.id.edt4);
        edt5=(EditText)findViewById(R.id.edt5);
        edt6=(EditText)findViewById(R.id.edt6);
        btnverif=(Button)findViewById(R.id.btnverif);
        edtnohp=(EditText)findViewById(R.id.edtnohp);
        fbAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference();
        firebaseUser=mAuth.getCurrentUser();
        BaseApp = com.pramudya.e_tani.BaseApp.getInstance();
        if (firebaseUser != null) {
            firebaseUser.getIdToken(true).toString();
        }


        btnverif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lanjut();

            }
        });
        btnkirimulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnkirimulang.isClickable()){
                    resendCode();
                    btnkirimulang.setVisibility(View.GONE);
                    timer.setVisibility(View.VISIBLE);
                    countDownTimer.start();
                }
            }
        });
        btnverifkode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifKode();
            }
        });
        edt1.requestFocus();
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt2.getText().toString().length()==0){
                    edt2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt2.getText().toString().length()==0){
                    edt3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt3.getText().toString().length()==0){
                    edt4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt4.getText().toString().length()==0){
                    edt5.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt5.getText().toString().length()==0){
                    edt6.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long l) {
            timer.setText(""+l/1000);
        }

        @Override
        public void onFinish() {
            timer.setText("");
            countDownTimer.cancel();
            timer.setVisibility(View.GONE);
            btnkirimulang.setVisibility(View.VISIBLE);
        }
    }
        public void Lanjut() {
        phoneNumber= edtnohp.getText().toString();


        if((!TextUtils.isEmpty(phoneNumber)) && phoneNumber.length()<=14){

            Send_Number_tofirebase(phoneNumber);
            lvutama.setVisibility(View.GONE);
            lv2.setVisibility(View.VISIBLE);

        }else {
            Toast.makeText(this, "Nomor salah", Toast.LENGTH_SHORT).show();
        }
    }
    public void Send_Number_tofirebase(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private void setUpVerificatonCallbacks() {
        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        signInWithPhoneAuthCredential(credential);

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        Log.d("responce",e.toString());
                        Toast.makeText(VerifikasiPesawahActivity.this, "Nomor salah, gunakan kode negara", Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                        } else if (e instanceof FirebaseTooManyRequestsException) {

                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;


                    }
                };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getdatauser(edtnohp.getText().toString());



                        } else {
                            Toast.makeText(VerifikasiPesawahActivity.this,"Kode Salah",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void resendCode() {

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack,
                resendToken);
    }
    public void verifKode() {
        String code = "" + edt1.getText().toString() + edt2.getText().toString() + edt3.getText().toString() + edt4.getText().toString() + edt5.getText().toString() + edt6.getText().toString();
        if (!code.equals("")) {

            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else {
            Toast.makeText(this, "Masukan kode", Toast.LENGTH_SHORT).show();
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            phoneVerificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


            if (code != null) {
                edt1.setText(code);
                verifyCode(code);
            }else {
                getdatauser(edtnohp.getText().toString());
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifikasiPesawahActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void getdatauser(String no_telp){
        inboxQuery = rootref.child("pesawah").orderByChild("no_telp").equalTo(no_telp);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String adsads = "asdfghj";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                   adsads= ds.child("nama").getValue().toString();
                }

                if (adsads.equals("asdfghj")){
                    Intent intent=new Intent(VerifikasiPesawahActivity.this, PesawahDaftarActivity.class);
                    intent.putExtra("no_telp",edtnohp.getText().toString());

                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(VerifikasiPesawahActivity.this,PesawahActivity.class);
                    intent.putExtra("no_telp",edtnohp.getText().toString());
                    startActivity(intent);
                    finish();
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };


        inboxQuery.addValueEventListener(valueEventListener);
    }

}
