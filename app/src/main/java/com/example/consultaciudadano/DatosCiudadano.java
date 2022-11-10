package com.example.consultaciudadano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DatosCiudadano extends AppCompatActivity {

    TextView txtNombresC,txtApellidosC,txtGrupoSanC,txtRHC,txtLugarEC,txtLugarNC,txtFechaEC,txtNoIdC,txtFechaNC,txtEstaturaC;
    ImageView iconEditar, iconEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_ciudadano);
        txtNombresC = findViewById(R.id.txtNombresC);
        txtApellidosC = findViewById(R.id.txtApellidosC);
        txtNoIdC = findViewById(R.id.txtNoIdC);
        txtFechaEC = findViewById(R.id.txtFechaEC);
        txtLugarEC = findViewById(R.id.txtLugarEC);
        txtFechaNC = findViewById(R.id.txtFechaNC);
        txtLugarNC = findViewById(R.id.txtLugarNC);
        txtRHC = findViewById(R.id.txtRHC);
        txtGrupoSanC = findViewById(R.id.txtGrupoSanC);
        txtEstaturaC = findViewById(R.id.txtEstaturaC);
        Bundle recibeId = getIntent().getExtras();
        String id = recibeId.getString("noId");
        consultarCiudadano(id);

    }
    private void consultarCiudadano(String id) {
       // showCustomToast(getApplicationContext(),id, Toast.LENGTH_LONG);
        String url = "https://antecedentes--back.herokuapp.com/api/consulta?identificacion="+id;
        RequestQueue queue = Volley.newRequestQueue(DatosCiudadano.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e= jsonObject.getString("msg");
                    String code = jsonObject.getString("code");

                    if (!code.equals("1")) {
                        Intent i = new Intent(getApplicationContext(), HomeAdmin.class);
                        startActivity(i);
                        showCustomToast(getApplicationContext(),e, Toast.LENGTH_LONG);

                    }else{
                        txtNombresC.setText(jsonObject.getString("primer_nombre")+" "+jsonObject.getString("segundo_nombre"));
                        txtApellidosC.setText(jsonObject.getString("primer_apellido")+" "+jsonObject.getString("segundo_apellido"));
                        txtNoIdC.setText(jsonObject.getString("no_doc"));
                        txtFechaEC.setText(jsonObject.getString("fecha_exp").split("T")[0]);
                        txtLugarEC.setText(jsonObject.getString("lugar_exp"));
                        txtFechaNC.setText(jsonObject.getString("fecha_nacimiento").split("T")[0]);
                        txtLugarNC.setText(jsonObject.getString("lugar_nacimiento"));
                        txtRHC.setText(jsonObject.getString("rh"));
                        txtGrupoSanC.setText(jsonObject.getString("grupo_sanguineo"));
                        txtEstaturaC.setText(jsonObject.getString("estatura")+" cm");
                        iconEditar = findViewById(R.id.iconEditar);
                        iconEditar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle enviarDatos = new Bundle();
                                enviarDatos.putString("datos", txtNombresC.getText().toString()+" "
                                        +txtApellidosC.getText().toString()+" "
                                +txtNoIdC.getText().toString()+" "
                                +txtFechaEC.getText().toString()+" "
                                +txtLugarEC.getText().toString()+" "
                                +txtFechaNC.getText().toString()+" "
                                +txtLugarNC.getText().toString()+" "
                                +txtRHC.getText().toString()+" "
                                +txtGrupoSanC.getText().toString()+" "
                                +txtEstaturaC.getText().toString());
                                Intent i = new Intent(getApplicationContext(), ModificarCiudadano.class);
                                i.putExtras(enviarDatos);
                                startActivity(i);
                            }
                        });
                        iconEliminar = findViewById(R.id.iconEliminar);
                        iconEliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               eliminarCiudadano(txtNoIdC.getText().toString());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DatosCiudadano.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
    private void eliminarCiudadano(String id) {
        // showCustomToast(getApplicationContext(),id, Toast.LENGTH_LONG);
        String url = "https://antecedentes--back.herokuapp.com/api/borrar?identificacion="+id;
        RequestQueue queue = Volley.newRequestQueue(DatosCiudadano.this);
        StringRequest request = new StringRequest(Request.Method.DELETE, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e= jsonObject.getString("msg");
                    String code = jsonObject.getString("code");

                    if (!code.equals("1")) {
                        Intent i = new Intent(getApplicationContext(), HomeAdmin.class);
                        startActivity(i);
                        showCustomToast(getApplicationContext(),e, Toast.LENGTH_LONG);
                    }else{
                        showCustomToast(getApplicationContext(),"Ciudadano eliminado", Toast.LENGTH_LONG);
                        Intent i = new Intent(getApplicationContext(), HomeAdmin.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DatosCiudadano.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
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
}