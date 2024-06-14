package com.example.lesson09_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactDAO contactDAO;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private Button addContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactDAO = App.getInstance().getContactDAO();
        recyclerView = findViewById(R.id.contact_recycler_view);
        addContactButton = findViewById(R.id.add_contact_button);

        List<Contact> contacts = contactDAO.getAll();
        adapter = new ContactAdapter(contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_contact, (ViewGroup) findViewById(android.R.id.content), false);
                final EditText inputName = viewInflated.findViewById(R.id.input_name);
                final EditText inputPhone = viewInflated.findViewById(R.id.input_phone);

                builder.setView(viewInflated)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = inputName.getText().toString();
                                String phone = inputPhone.getText().toString();
                                Contact newContact = new Contact(name, phone);
                                contactDAO.insert(newContact);
                                contacts.add(newContact);
                                adapter.notifyItemInserted(contacts.size() - 1);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

                ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Contact contact = adapter.getContacts().get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to delete this contact?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        contactDAO.delete(contact);
                                        adapter.getContacts().remove(position);
                                        adapter.notifyItemRemoved(position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        adapter.notifyItemChanged(position);
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                };

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);

            }
        });
    }
}