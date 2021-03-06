package com.fiuady.android.compustore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.fiuady.db.Client;

public class MainActivity extends AppCompatActivity {

    ImageButton imageViewCategorias;
    ImageButton imageViewProductos;
    ImageButton imageViewEnsambles;
    ImageButton imageViewClientes;
    ImageButton imageViewOrdenes;
    ImageButton imageViewReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewCategorias = (ImageButton) findViewById(R.id.idImageButtonCategorias);
        imageViewProductos = (ImageButton) findViewById(R.id.idImageButtonProductos);
        imageViewEnsambles = (ImageButton) findViewById(R.id.idImageButtonEnsambles);
        imageViewClientes = (ImageButton) findViewById(R.id.idImageButtonClientes);
        imageViewOrdenes = (ImageButton) findViewById(R.id.idImageButtonOrdenes);
        imageViewReportes = (ImageButton) findViewById(R.id.idImageButtonReportes);
    }

    public void onImageClick (View v) {
        Intent i = new Intent(this, CategoriesActivity.class);
        startActivity(i);
    }

    public void onImageClickproductos (View v) {
        Intent i = new Intent(this, ProductsActivity.class);
        startActivity(i);
    }

    public void onImageClickclientes (View v) {
        Intent i = new Intent(this, ClientsActivity.class);
        startActivity(i);
    }

    public void onImageClickensambles (View v){
        Intent i = new Intent(this, AssembliesActivity.class);
        startActivity(i);
    }

    public void onImageClickordenes (View view) {
        Intent i = new Intent(this, OrdersActivity.class);
        startActivity(i);
    }
    public void onImageClickreportes (View v) {
        Intent i = new Intent(MainActivity.this, ReportsActivity.class);
        startActivity(i);
    }
}
