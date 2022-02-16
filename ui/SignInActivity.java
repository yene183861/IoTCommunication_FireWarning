package vn.hust.soict.project.iotcommunication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hust.soict.project.iotcommunication.R;
import vn.hust.soict.project.iotcommunication.api.ApiService;
import vn.hust.soict.project.iotcommunication.api.RetrofitInstance;
import vn.hust.soict.project.iotcommunication.data_local.DataLocalManager;
import vn.hust.soict.project.iotcommunication.model.User;
import vn.hust.soict.project.iotcommunication.model.UserResponse;

public class SignInActivity extends AppCompatActivity {
    private LinearLayout layoutSignUp;
    private EditText edtEmail, edtPassword;
    private TextView btnSignIn, btnForgotPassword, txtNotification;
    private ProgressDialog progressDialog;
  private String API_KEY =  "AIzaSyAnlvxVdVQjwPXHfF0gIEdtvlHQqVKSB9Q";
  //String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUi();
        initListener();
    }

    private void initUi() {
        layoutSignUp = findViewById(R.id.layoutSignUp);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        txtNotification = findViewById(R.id.txtNotification);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    txtNotification.setText("You must enter email and password!");
                    txtNotification.setVisibility(View.VISIBLE);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtNotification.setText("Email invalid!");
                    txtNotification.setVisibility(View.VISIBLE);
                } else {
                    progressDialog.show();
                    User user = new User(email, password, DataLocalManager.getFCMToken());
                    ApiService apiService = RetrofitInstance.getRetrofitClient1().create(ApiService.class);
                    Call<UserResponse> call = apiService.loginEmail(user);
                    call.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            progressDialog.dismiss();
                            if(response.code() == 200){
                                String token = response.body().getToken();
                                Log.e("login", response.body().getToken());
                                DataLocalManager.setTokenServer(response.body().getToken());
                                DataLocalManager.setClientId(response.body().getUser().getId());
                                Log.e("id", DataLocalManager.getClientId());
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }else {
                                try{
                                    Log.e("login","error: " + response.code()+ " " + response.errorBody().string());
                                } catch (IOException e){
                                    e.printStackTrace();
                                    Log.e("login","error" + e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.e("login","error" + t);
                        }
                    });

                    //login firebase
//                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                    mAuth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    progressDialog.dismiss();
//                                    if (task.isSuccessful()) {
//                                        User user = new User(email, password);
//                                        Log.e("User", user.toString());
//                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finishAffinity();
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        txtNotification.setText("Email or password wrong");
//                                        txtNotification.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            });

                    //get token
//                    ApiService apiService1 = RetrofitInstance.getRetrofitFirebase().create(ApiService.class);
//                    Call<JsonObject> call1 = apiService1.getTokenSignIn(API_KEY, email, password, true);
//                    call1.enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                            if (response.isSuccessful()) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                                    token = jsonObject.getString("idToken");
//                                    Log.e("token",response.body().toString());
//                                    Log.e("token",token);
//                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finishAffinity();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                Toast.makeText(SignInActivity.this, "Some error occurred...", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Log.e("error",t.getMessage());
//                        }
//                    });

//                    User user = new User(email, password);
//                    ApiService apiService = RetrofitInstance.getRetrofitClient1().create(ApiService.class);
//                    Call<UserResponse> call = apiService.login(user);
//                    call.enqueue(new Callback<UserResponse>() {
//                        @Override
//                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                            progressDialog.dismiss();
//                            if(response.code() == 200){
//                                Log.e("signin", response.body().toString());
//                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finishAffinity();
////                                mListHome = new ArrayList<>(response.body().getHomeList());
////                                for(Home home: mListHome){
////                                    Log.e("callApiGetHomeList", "The list" + home.toString());
////                                }
////                                homeListLiveData.setValue(mListHome);
//                            }else {
//                                try{
//                                    Log.e("login","error body" + response.code() + response.errorBody().string());
//                                } catch (IOException e){
//                                    e.printStackTrace();
//                                    Log.e("callApiGetHomeList","error" + e);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserResponse> call, Throwable t) {
//                            progressDialog.dismiss();
//                            Log.e("callApiGetHomeList","error" + t);
//                        }
//                    });
//                    call.enqueue(new Callback<User>() {
//                        @Override
//                        public void onResponse(Call<User> call, Response<User> response) {
//                            if(response.code() == 200){
//                                Log.e("signin", response.body().toString());
//                                mListHome = new ArrayList<>(response.body().getHomeList());
//                                for(Home home: mListHome){
//                                    Log.e("callApiGetHomeList", "The list" + home.toString());
//                                }
//                                homeListLiveData.setValue(mListHome);
//                            }else {
//                                try{
//                                    Log.e("callApiGetHomeList","error body" + response.errorBody().string());
//                                } catch (IOException e){
//                                    e.printStackTrace();
//                                    Log.e("callApiGetHomeList","error" + e);
//                                }
//                            }
//                        }

//                        @Override
//                        public void onFailure(Call<User> call, Throwable t) {
//
//                        }
//                    });
//                    ApiService.apiAccount.login(user).enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            progressDialog.dismiss();
//                            String token = response.body();
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            progressDialog.dismiss();
//
//                        }
//                    });
                }
            }
        });
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}