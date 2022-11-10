package com.example.consultaciudadano;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class HomeAdmin extends AppCompatActivity {

    Button btnRegCiu, btnConsultarCiud;
    EditText txtNoIdD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        btnRegCiu = findViewById(R.id.btnRegCiu);
        btnRegCiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistrarCiudadano.class);
                startActivity(i);
            }
        });
        btnConsultarCiud = findViewById(R.id.btnConsultarCiud);
        btnConsultarCiud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoConsulta();
            }
        });
    }
    public void mostrarMenu(View v){
        ImageView iconMenu = findViewById(R.id.iconMenu2);
        PopupMenu p = new PopupMenu(this,iconMenu);
        p.getMenuInflater().inflate(R.menu.menuuser,p.getMenu());
        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.itemCerrarSesion:
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        Toast.makeText(HomeAdmin.this,"Cerrando sesión", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        p.show();
    }
    private void mostrarDialogoConsulta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeAdmin.this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_dialog_consulta,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        txtNoIdD = view.findViewById(R.id.txtNoIdDC);

        Button btnReq = view.findViewById(R.id.btnConsulta);
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           // Toast.makeText(getApplicationContext(),txtNoIdD.getText().toString(), Toast.LENGTH_SHORT).show();
                Bundle enviarId = new Bundle();
                enviarId.putString("noId", txtNoIdD.getText().toString());
                Intent i = new Intent(getApplicationContext(), DatosCiudadano.class);
                i.putExtras(enviarId);
                startActivity(i);
                dialog.dismiss();
            }
        });
    }
}