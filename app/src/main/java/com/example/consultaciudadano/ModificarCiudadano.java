package com.example.consultaciudadano;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ModificarCiudadano extends AppCompatActivity {

    Button btnAceptarM;
    EditText txtNombresR,txtApellidosR,txtLugarER,txtLugarNR,txtFechaER,txtNoIdR,txtFechaNR,txtEstaturaR;
    TextInputLayout txtInputRH,textInputLayoutGrupSan;
    AutoCompleteTextView txtRHR,txtGrupoSanR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_ciudadano);

        txtInputRH = findViewById(R.id.textInputLayoutRHM);
        txtRHR = findViewById(R.id.txtRHM);

        String [] rhs = new String[]{
                "-",
                "+"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ModificarCiudadano.this,
                R.layout.dropdown_item,
                rhs
        );
        txtRHR.setAdapter(adapter);

        textInputLayoutGrupSan = findViewById(R.id.textInputLayoutGrupSanM);
        txtGrupoSanR = findViewById(R.id.txtGrupoSanM);

        String [] g_sanguineos = new String[]{
                "A",
                "B",
                "O",
                "AB"
        };
        ArrayAdapter<String> adapterGS = new ArrayAdapter<>(
                ModificarCiudadano.this,
                R.layout.dropdown_item_gs,
                g_sanguineos
        );
        txtGrupoSanR.setAdapter(adapterGS);

        txtNombresR = findViewById(R.id.txtNombresM);
        txtApellidosR = findViewById(R.id.txtApellidosM);
        txtNoIdR = findViewById(R.id.txtNoIdM);
        txtFechaER = findViewById(R.id.txtFechaEM);
        txtLugarER = findViewById(R.id.txtLugarEM);
        txtFechaNR = findViewById(R.id.txtFechaNM);
        txtLugarNR = findViewById(R.id.txtLugarNM);
        txtEstaturaR = findViewById(R.id.txtEstaturaM);

        Bundle recibe = getIntent().getExtras();
        String datos = recibe.getString("datos");
        txtNombresR.setText(datos.split(" ")[0]+" "+datos.split(" ")[1]);
        txtApellidosR.setText(datos.split(" ")[2]+" "+datos.split(" ")[3]);
        txtNoIdR.setText(datos.split(" ")[4]);
        txtFechaER.setText(datos.split(" ")[5]);
        txtLugarER.setText(datos.split(" ")[6]);
        txtFechaNR.setText(datos.split(" ")[7]);
        txtLugarNR.setText(datos.split(" ")[8]);

        Bundle recibeF = getIntent().getExtras();
        String datosF = recibeF.getString("datosF");

        txtRHR.setText(datosF.split(" ")[0]);
        txtGrupoSanR.setText(datosF.split(" ")[1]);
        txtEstaturaR.setText(datosF.split(" ")[2]);

        btnAceptarM = findViewById(R.id.btnAceptarM);
        btnAceptarM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarCiudadano(txtNombresR.getText().toString(),txtApellidosR.getText().toString(),txtNoIdR.getText().toString(),txtFechaER.getText().toString(),txtLugarER.getText().toString(),txtFechaNR.getText().toString(),txtLugarNR.getText().toString(),txtRHR.getText().toString(),txtGrupoSanR.getText().toString(),txtEstaturaR.getText().toString());
            }
        });
    }
    private void modificarCiudadano(String nombres, String apellidos, String id,String fecha_exp, String lugar_exp, String fecha_nac, String lugar_nac,
                                    String rh, String g_sanguineo, String estatura) {
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
            showCustomToast(getApplicationContext(),"Verifique los campos", Toast.LENGTH_LONG);
            p_n=null;
            s_n=null;
            p_a=null;
            s_a=null;
        }
        //String url = "https://antecedentes--back.herokuapp.com/api/registro-ciudadano?p_nombre=jj&s_nombre=pp&p_apellido=oo&s_apellido=dd&id=555556784&fecha_exp=2001-08-06&lugar_exp=2001-08-06&fecha_nac=2001-08-06&lugar_nac=1&rh=+&g_sanguineo=a&estatura=156&tipo_doc=1&sexo=1";
        String url = "https://antecedentes--back.herokuapp.com/api/actualizacion?p_nombre="+p_n+"&s_nombre="+s_n+"&p_apellido="+p_a+
                "&s_apellido="+s_a+
                "&id="+id+
                "&fecha_exp="+fecha_exp+
                "&lugar_exp="+lugar_exp+
                "&fecha_nac="+fecha_nac+
                "&lugar_nac="+lugar_nac+
                "&rh="+rh+
                "&g_sanguineo="+g_sanguineo+
                "&estatura="+estatura+
                "&tipo_doc=1"+
                "&sexo=1";
        RequestQueue queue = Volley.newRequestQueue(ModificarCiudadano.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String e = jsonObject.getString("msg");
                    String codigo = jsonObject.getString("code");
                    if (!codigo.equals("1")){
                        showCustomToast(getApplicationContext(),e, Toast.LENGTH_LONG);
                    }else{
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
                Toast.makeText(ModificarCiudadano.this, "Fail to get response = " + error, Toast.LENGTH_LONG).show();
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