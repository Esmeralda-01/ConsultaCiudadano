package com.example.consultaciudadano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    Button btnRegistro;
    Button btnLongIn;
    EditText txtPass, txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtPass = findViewById(R.id.txtPass);
        txtUser = findViewById(R.id.txtUser);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistroFuncionario.class);
                startActivity(i);
            }
        });
        btnLongIn = findViewById(R.id.btnLongIn);
        btnLongIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iniciarSesion(txtUser.getText().toString(),txtPass.getText().toString());
            }
        });
    }
    private void iniciarSesion(String user, String contrasena) {
        String url = "https://antecedentes--back.herokuapp.com/api/login?identificacion="+user+"&password="+contrasena;
        // String url = "";
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e = jsonObject.getString("detail");
                    String codigo = jsonObject.getString("code");
                   if (!codigo.equals("1")){
                       if(user.equals("admin") && contrasena.equals("1234")){
                           Intent i = new Intent(getApplicationContext(), HomeAdmin.class);
                           startActivity(i);
                       }else {
                           toastError(getApplicationContext(), e, Toast.LENGTH_LONG);
                       }
                    }else{
                       Intent i = new Intent(getApplicationContext(), HomeUser.class);
                       startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
    public static void toastError(Context ctx, String msg, int duration){
        try{
            if(!"".equals(msg)){
                LayoutInflater inflater = LayoutInflater.from(ctx);
                View layout = inflater.inflate(R.layout.activity_toats_msg, null);
                TextView text = layout.findViewById(R.id.textToShow);
                text.setText(msg);
                Toast toast = new Toast(ctx.getApplicationContext());
                toast.setGravity(Gravity.TOP|Gravity.CENTER,0,200);
                toast.setMargin(0.01f, 0.01f);
                toast.setDuration(duration);
                toast.setView(layout);
                toast.show();
            }
        }catch(IllegalStateException ise){
            //  Log.e(Tag., "showCustomToast(), " + ise.getMessage());
        }catch(Exception e){
            //Log.e(TAG, "showCustomToast(), " + e.getMessage());
        }
    }
}