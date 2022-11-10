package com.example.consultaciudadano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeUser extends AppCompatActivity {

    Button btnScanner;
    EditText txtCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        btnScanner = findViewById(R.id.btnScanner);
        txtCodigo = findViewById(R.id.txtCodigo);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador = new IntentIntegrator(HomeUser.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Codigos");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });
        findViewById(R.id.btnReporte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarReporte();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result !=null){
            if(result.getContents()==null){
                Toast.makeText(this, "Lector cancelado", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Lectura exitosa", Toast.LENGTH_LONG).show();
                String resultado = result.getContents().toString();

                String line =   resultado.substring(0,70);
                line = line.split("_")[1];
                Pattern p = Pattern.compile("\\p{Alpha}");
                Matcher m = p.matcher(line);
                int indice = 0;
                if (m.find()) {

                    indice = m.start();
                    line = line.substring(0,indice);
                    txtCodigo.setText(line.substring(line.length()-10,indice));

                    enviarCodigo(txtCodigo.getText().toString());
                }else{
                    txtCodigo.setText("No se pudo encontrar cedula");
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    public void mostrarMenu(View v){
        ImageView iconMenu = findViewById(R.id.iconMenu);
        PopupMenu p = new PopupMenu(this,iconMenu);
        p.getMenuInflater().inflate(R.menu.menuuser,p.getMenu());
        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.itemCerrarSesion:
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    Toast.makeText(HomeUser.this,"Cerrando sesión", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        p.show();
    }
    private void enviarCodigo(String noId) {
        if(noId.equals("")){
            noId="0";
        }
        String url = "https://antecedentes--back.herokuapp.com/api/consulta-antecedentes?identificacion="+noId;
        RequestQueue queue = Volley.newRequestQueue(HomeUser.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e = jsonObject.getString("msg");
                    String codigo = jsonObject.getString("code");
                   if (!codigo.equals("1")){
                        showCustomToast(getApplicationContext(),e,Toast.LENGTH_LONG);
                    }else{
                       showCustomToast(getApplicationContext(),e,Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showCustomToast(getApplicationContext(),"error",Toast.LENGTH_LONG);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeUser.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
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
    private void generarReporte() {
        String url = "https://antecedentes--back.herokuapp.com/api/reporte";
        RequestQueue queue = Volley.newRequestQueue(HomeUser.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e = jsonObject.getString("msg");
                    String codigo = jsonObject.getString("code");
                   /*if (!codigo.equals("1")){
                        showCustomToast(getApplicationContext(),e,Toast.LENGTH_LONG);
                    }else{
                        showCustomToast(getApplicationContext(),e,Toast.LENGTH_LONG);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    showCustomToast(getApplicationContext(),"error",Toast.LENGTH_LONG);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeUser.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }
}