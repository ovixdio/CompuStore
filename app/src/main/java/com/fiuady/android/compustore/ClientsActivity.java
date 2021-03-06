package com.fiuady.android.compustore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;

public class ClientsActivity extends AppCompatActivity {

    private final String BUSCARPRESSED = "buscarpressed";
    private final String SPINNERSTATES = "indexdespinner";
    private final String STRINGTEXTO = "stringtexto";
    private final String STRINGACTUAL = "stringactual";
    private boolean buscarpressed;

    private RecyclerView clientRV;
    private ClientAdapter C_adapter;
    private CompuStore compuStore;
    private MultiSpinner spinner;
    private EditText texto;
    private String textoactual;
    private String stringquesebusco = "";
    private String statesSpinner;
    private  String SelectedList = null;

    private class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtFullName;
        private TextView txtAddress;
        private TextView txtPhone1;
        private TextView txtPhone2;
        private TextView txtPhone3;
        private TextView txtEmail;

        List<Client> clients;

        public ClientHolder(View itemView,List<Client> clients) {
            super(itemView);
            this.clients=clients;
            itemView.setOnClickListener(this);

            txtFullName=(TextView)itemView.findViewById(R.id.client_fullname_text);
            txtAddress=(TextView)itemView.findViewById(R.id.client_address_text);
            txtPhone1=(TextView)itemView.findViewById(R.id.client_phone1_text);
            txtPhone2=(TextView)itemView.findViewById(R.id.client_phone2_text);
            txtPhone3=(TextView)itemView.findViewById(R.id.client_phone3_text);
            txtEmail=(TextView)itemView.findViewById(R.id.client_email_text);

        }

        public void bindClient(Client client){

            txtFullName.setText(client.getLastName()+", "+client.getFirstName());
            txtAddress.setText(client.getAddress());
            txtEmail.setText(client.getEmail());
            txtPhone1.setText(client.getPhone1());
            txtPhone2.setText(client.getPhone2());
            txtPhone3.setText(client.getPhone3());
        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            final Client client = clients.get(position);

            final PopupMenu popup = new PopupMenu(ClientsActivity.this,itemView);
            popup.getMenuInflater().inflate(R.menu.option2_menu, popup.getMenu());

            if (compuStore.deleteClient(client.getId(), false)) {
                popup.getMenu().removeItem(R.id.menu_item2);
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if ((item.getTitle().toString()).equalsIgnoreCase("Modificar")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientsActivity.this);
                        final View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

                        final EditText txtAdd_firstName = (EditText) view.findViewById(R.id.add_text_firstName);
                        txtAdd_firstName.setText(client.getFirstName());
                        final EditText txtAdd_lastName = (EditText) view.findViewById(R.id.add_text_lastName);
                        txtAdd_lastName.setText(client.getLastName());
                        final EditText txtAdd_address = (EditText) view.findViewById(R.id.add_text_address);
                        txtAdd_address.setText(client.getAddress());
                        final EditText txtAdd_email = (EditText) view.findViewById(R.id.add_text_email);
                        txtAdd_email.setText(client.getEmail());
                        final EditText txtAdd_phone1 = (EditText) view.findViewById(R.id.add_text_phone1);
                        txtAdd_phone1.setText(client.getPhone1());
                        final EditText txtAdd_phone2 = (EditText) view.findViewById(R.id.add_text_phone2);
                        txtAdd_phone2.setText(client.getPhone2());
                        final EditText txtAdd_phone3 = (EditText) view.findViewById(R.id.add_text_phone3);
                        txtAdd_phone3.setText(client.getPhone3());

                        TextView txtTitle = (TextView) view.findViewById(R.id.add_title);
                        txtTitle.setText(R.string.client_update);

                        builder.setCancelable(false);

                        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
                                build.setCancelable(false);
                                build.setTitle("Modificar cliente");
                                build.setMessage(R.string.sure_text);

                                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (compuStore.updateClient(txtAdd_firstName.getText().toString(),txtAdd_lastName.getText().toString(),
                                                txtAdd_address.getText().toString(),txtAdd_email.getText().toString(),txtAdd_phone1.getText().toString(),
                                                txtAdd_phone2.getText().toString(),txtAdd_phone3.getText().toString(),client.getId())) {

                                            Toast.makeText(ClientsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();

                                            C_adapter = new ClientsActivity.ClientAdapter(compuStore.getAllClients());
                                            clientRV.setAdapter(C_adapter);

                                        } else {
                                            Toast.makeText(ClientsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                build.create().show();
                            }
                        });
                        builder.setView(view);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else if ((item.getTitle().toString()).equalsIgnoreCase("Eliminar")) {

                        AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
                        build.setCancelable(false);
                        build.setTitle(getString(R.string.client_delete));  ///AFQFADFQFQ
                        build.setMessage(R.string.sure_text);

                        build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(ClientsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                                compuStore.deleteClient(client.getId(), true);
                                C_adapter = new ClientsActivity.ClientAdapter(compuStore.getAllClients());
                                clientRV.setAdapter(C_adapter);
                            }
                        });
                        build.create().show();
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder>{

        private List<Client> clients;

        public ClientAdapter(List<Client> clients){
            this.clients=clients;
        }

        @Override
        public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.client_list_item,parent,false);
            return new ClientHolder(view,clients);
        }

        @Override
        public void onBindViewHolder(ClientHolder holder, int position) {holder.bindClient(clients.get(position));}

        @Override
        public int getItemCount() {return clients.size();}
    }

    boolean[] selected1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        compuStore = new CompuStore(this);

        spinner = (MultiSpinner) findViewById(R.id.client_filter_spinner);
        texto = (EditText)findViewById(R.id.edittextdescripcion);

        final List<String> list = new ArrayList<>();
        list.add("Nombre");
        list.add("Apellido");
        list.add("Dirección");
        list.add("Teléfono");
        list.add("Email");

        spinner.setItems(list,null, "Todos", new MultiSpinner.MultiSpinnerListener() {

            @Override
            public void onItemsSelected(boolean[] selected) {
                selected1 = selected;
                SelectedList = spinner.getSelectedItem().toString();
            }
        });

        clientRV=(RecyclerView)findViewById(R.id.activity_clients_RV);
        clientRV.setLayoutManager(new LinearLayoutManager(this)); //Porque el recycler view NO es un layout, necesitamos uno.

        C_adapter=new ClientAdapter(compuStore.getAllClients()); //Creo el adaptador y me pide una lista de empleados.
        clientRV.setAdapter(C_adapter);

        if(savedInstanceState != null){ //Si hay algo guardado
            buscarpressed = savedInstanceState.getBoolean(BUSCARPRESSED);
            textoactual = savedInstanceState.getString(STRINGACTUAL);
            statesSpinner = savedInstanceState.getString(SPINNERSTATES);
            stringquesebusco = savedInstanceState.getString(STRINGTEXTO);



            spinner.setItems(list,statesSpinner, "Todos", new MultiSpinner.MultiSpinnerListener() {

                @Override
                public void onItemsSelected(boolean[] selected) {
                    selected1 = selected;
                    SelectedList = spinner.getSelectedItem().toString();
                }
            });

            texto.setText(textoactual);

            if(buscarpressed){
                //Llenar lista si se presiono buscar..
                List<Client> clientsAdapter = compuStore.filterClients(selected1,texto.getText().toString());

                Collections.sort(clientsAdapter, new Comparator<Client>() {
                    @Override
                    public int compare(Client o1, Client o2) {
                        return o1.getLastName().compareTo(o2.getLastName());
                    }
                });

                for (int i = 1; i<clientsAdapter.size(); i++){
                    Client c1 = clientsAdapter.get(i); //Cliente en lugar 1
                    Client c2 = clientsAdapter.get(i-1); //Cliente en lugar 0

                    if (c1.getId()==c2.getId()){
                        clientsAdapter.remove(c1);
                        i--;
                    }
                }

                C_adapter = new ClientAdapter(clientsAdapter);
                clientRV.setAdapter(C_adapter);

                buscarpressed = true;
                stringquesebusco = texto.getText().toString();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.add_title);

        final EditText txtAdd_firstName = (EditText) view.findViewById(R.id.add_text_firstName);
        final EditText txtAdd_lastName = (EditText) view.findViewById(R.id.add_text_lastName);
        final EditText txtAdd_address = (EditText) view.findViewById(R.id.add_text_address);
        final EditText txtAdd_email = (EditText) view.findViewById(R.id.add_text_email);
        final EditText txtAdd_phone1 = (EditText) view.findViewById(R.id.add_text_phone1);
        final EditText txtAdd_phone2 = (EditText) view.findViewById(R.id.add_text_phone2);
        final EditText txtAdd_phone3 = (EditText) view.findViewById(R.id.add_text_phone3);

        txtTitle.setText(R.string.client_add);

        builder.setCancelable(false);

        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                AlertDialog.Builder build = new AlertDialog.Builder(ClientsActivity.this);
                build.setCancelable(false);
                build.setTitle(getString(R.string.client_add));
                build.setMessage(R.string.sure_text);

                build.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Toast.makeText(ClientsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(R.string.save_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(compuStore.insertClient(txtAdd_firstName.getText().toString(),txtAdd_lastName.getText().toString(),
                        txtAdd_address.getText().toString(),txtAdd_email.getText().toString(),txtAdd_phone1.getText().toString(),
                        txtAdd_phone2.getText().toString(),txtAdd_phone3.getText().toString())){

                            Toast.makeText(ClientsActivity.this, R.string.add_msg, Toast.LENGTH_SHORT).show();
                            C_adapter = new ClientsActivity.ClientAdapter(compuStore.getAllClients());
                            clientRV.setAdapter(C_adapter);
                        } else {
                            Toast.makeText(ClientsActivity.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                build.create().show();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);
    }

    public void onSearchClick(View v){

        List<Client> clientsAdapter = compuStore.filterClients(selected1,texto.getText().toString());

        Collections.sort(clientsAdapter, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });

        for (int i = 1; i<clientsAdapter.size(); i++){
            Client c1 = clientsAdapter.get(i); //Cliente en lugar 1
            Client c2 = clientsAdapter.get(i-1); //Cliente en lugar 0

            if (c1.getId()==c2.getId()){
                clientsAdapter.remove(c1);
                i--;
            }
        }

        buscarpressed = true;
        stringquesebusco = texto.getText().toString();

       C_adapter = new ClientAdapter(clientsAdapter);
       clientRV.setAdapter(C_adapter);
   }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SPINNERSTATES,spinner.getSelectedItem().toString());
        outState.putString(STRINGACTUAL,texto.getText().toString()); //
        outState.putString(STRINGTEXTO,stringquesebusco);
        outState.putBoolean(BUSCARPRESSED,buscarpressed);
    }
}


















