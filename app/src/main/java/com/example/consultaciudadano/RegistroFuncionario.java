package com.example.consultaciudadano;

import androidx.appcompat.app.AlertDialog;
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

public class RegistroFuncionario extends AppCompatActivity {

    Button btnRegistrar;
    EditText txtNombres,txtApellidos,txtFuerzaPublica,txtRango,txtCorreo,txtIdentificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtIdentificacion = findViewById(R.id.txtIdentificacion);
        txtFuerzaPublica = findViewById(R.id.txtFuerzaPublica);
        txtRango = findViewById(R.id.txtRango);
        txtCorreo = findViewById(R.id.txtCorreo);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarFuncionario(txtNombres.getText().toString(),txtApellidos.getText().toString(),txtIdentificacion.getText().toString(),txtFuerzaPublica.getText().toString(),txtRango.getText().toString(),txtCorreo.getText().toString());
            }
        });
    }
    private void registrarFuncionario(String nombres, String apellidos, String id, String fuerza, String rango, String correo) {
        String p_n="";
        String s_n="";
        String p_a="";
        String s_a="";
        try{
            p_n =nombres.split(" ")[0];
            s_n=nombres.split(" ")[1];
            p_a=apellidos.split(" ")[0];
            s_a=apellidos.split(" ")[1];
        }catch (Exception e){
              showCustomToast(getApplicationContext(),"Verifique los campos",Toast.LENGTH_LONG);
            p_n=null;
            s_n=null;
            p_a=null;
            s_a=null;
        }
        String url = "https://antecedentes--back.herokuapp.com/api/registro-funcionario?p_nombre="+p_n+"&s_nombre="+s_n+"&p_apellido="+p_a+"&s_apellido="+s_a+"&id="+id+"&fuerza="+fuerza+"&rango="+rango+"&correo="+correo+"&sexo=1";
        RequestQueue queue = Volley.newRequestQueue(RegistroFuncionario.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e = jsonObject.getString("msg");
                    String codigo = jsonObject.getString("code");
                    if (!codigo.equals("1")){
                        showCustomToast(getApplicationContext(),e,Toast.LENGTH_LONG);
                    }else{
                        mostrarDialogoRegis();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroFuncionario.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    public static void showCustomToast(Context ctx, String msg, int duration){
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
    private void mostrarDialogoRegis(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroFuncionario.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_dialog_exito,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnReq = view.findViewById(R.id.btnListo);
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
    }
}