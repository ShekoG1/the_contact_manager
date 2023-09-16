package com.theshekharmaharaj.contactmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactDAO contactDAO;
    private List<ContactModel> contactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_item);

        recyclerView = findViewById(R.id.recyclerViewContacts);
        contactDAO = new ContactDAO(this);
        contactsList = new ArrayList<>();

        // Fetch the contacts from the database
        fetchContacts();

        // Display "No contacts found" message if no contacts
        if (contactsList.isEmpty()) {
            // Display a message like "No contacts found"
            // This can be a TextView or any other UI element
        } else {
            // Set up RecyclerView and Adapter to display contacts
            ContactsAdapter adapter = new ContactsAdapter(contactsList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        // Button to navigate to MainActivity
        findViewById(R.id.createNewcontact).setOnClickListener(view -> {
            startActivity(new Intent(ContactsListActivity.this, MainActivity.class));
        });
    }

    private void fetchContacts() {
        contactDAO.open();
        contactsList = contactDAO.getAllContacts();
        contactDAO.close();
    }
}