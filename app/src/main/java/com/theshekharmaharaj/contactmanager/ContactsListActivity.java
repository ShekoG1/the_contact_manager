package com.theshekharmaharaj.contactmanager;

import static java.lang.Math.toIntExact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.theshekharmaharaj.contactmanager.ContactsAdapter;

public class ContactsListActivity extends AppCompatActivity implements OnItemClickListener{

    private RecyclerView recyclerView;
    private ContactDAO contactDAO;
    private List<ContactModel> contactsList;
    private boolean isDelete = false;
    private ContactsAdapter adapter;

    @Override
    public void onItemClick(ContactModel contact) {
        if(!isDelete) {
            // Open MainActivity and pass the contact details
            Intent intent = new Intent(ContactsListActivity.this, MainActivity.class);
            intent.putExtra("contactName", contact.getName());
            intent.putExtra("contactEmail", contact.getEmail());
            intent.putExtra("contactPhone", contact.getPhoneNumber());
            intent.putExtra("contactBirthday", contact.getBirthday());
            intent.putExtra("itemID", contact.getId());
            intent.putExtra("isUpdate", true);
            startActivity(intent);
        }else{
            // Display delete confirmation
            confirmDeleteDialogue(contact.getId());
        }
    }

    // This delete toggle is it's own function so that we can reference it from another onclick event. Particularly, the cancel event.
    private void deleteToggle(){
        Button deleteButton = findViewById(R.id.startDelete);
        Button createButton = findViewById(R.id.createNewcontact);
        if(!isDelete){
            deleteButton.setText("Cancel");
            createButton.setVisibility(View.INVISIBLE);
            isDelete = true;
        }else{
            deleteButton.setText("Delete");
            createButton.setVisibility(View.VISIBLE);
            isDelete = false;
        }
    }

    private void confirmDeleteDialogue(long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");  // Dialog title
        builder.setMessage("Once you delete this contact, you cannot recover it!");  // Dialog message

        // Positive button (Yes)
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the contact when "Yes" is clicked
                contactDAO.open();
                contactDAO.deleteContact(id);
                contactDAO.close();
                refreshAdapterData();
                dialog.dismiss();  // Dismiss the dialog
            }
        });

        // Negative button (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss model and disable delete mode when "No" is clicked
                deleteToggle();
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int selectedOption = 0;  // Initialize the selected option

    public void showRadioButtonDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an option");

        // Set sorting options
        final String[] options = {"None","Name", "Email Address", "Birthday"};

        builder.setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
            // Update the selected option
            selectedOption = which;
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle the OK button click
            Toast.makeText(this, "Selected: " + options[selectedOption], Toast.LENGTH_SHORT).show();
            contactDAO.open();
            List<ContactModel> contacts = contactDAO.sortContacts(options[selectedOption]);
            contactDAO.close();
            refreshAdapterData(contacts);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle the Cancel button click (if needed)
            dialog.dismiss();
        });
        builder.create().show();
    }

    public void showFilterDialog(View view) {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Contacts");

        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(dialogView);

        // Get the radio group and text field from the dialog layout
        RadioGroup radioGroupColumns = dialogView.findViewById(R.id.radioGroupColumns);
        EditText etFilterValue = dialogView.findViewById(R.id.etFilterValue);

        // Set up the positive button click listener
        builder.setPositiveButton("Filter", (dialog, which) -> {
            // Get the selected radio button's text (the selected column)
            int selectedRadioButtonId = radioGroupColumns.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
            String selectedColumn = selectedRadioButton.getText().toString();

            // Get the filter value from the text field
            String filterValue = etFilterValue.getText().toString();

            //Format column
            switch (selectedColumn){
                case "Name":
                    selectedColumn = "name";
                    break;

                case "Email Address":
                    selectedColumn = "email";

                default:
                    selectedColumn = "None";
            }

            // Call the method to filter based on the selected column and filter value
            filterData(selectedColumn, filterValue);
        });

        // Set up the negative button click listener
        builder.setNegativeButton("Cancel", null);

        // Show the dialog
        builder.create().show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_item);

        recyclerView = findViewById(R.id.recyclerViewContacts);
        contactDAO = new ContactDAO(this);
        contactsList = new ArrayList<>();
        Button startDelete = findViewById(R.id.startDelete);
        EditText searchButton = findViewById(R.id.searchButton);

        startDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteToggle();
            }
        });

        // Set up RecyclerView and Adapter to display contacts
        adapter = new ContactsAdapter(this,contactsList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set the item click listener for the RecyclerView
        adapter.setOnItemClickListener(this);

        // Fetch the contacts from the database
        fetchContacts();

        // Display "No contacts found" message if no contacts
        if (contactsList.isEmpty()) {
            // Display the preset EMPTY error message
            TextView tv = (TextView)findViewById(R.id.contactsError);
            tv.setVisibility(View.VISIBLE);
        } else {
            // Set up RecyclerView and Adapter to display contacts
            adapter = new ContactsAdapter(this,contactsList,this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        // Button to navigate to MainActivity
        findViewById(R.id.createNewcontact).setOnClickListener(view -> {
            startActivity(new Intent(ContactsListActivity.this, MainActivity.class));
        });

        searchButton.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String query = searchButton.getText().toString().trim();
                    List<ContactModel> searchResults = contactDAO.searchContacts(query);
                    // Refresh the adapter data with the search results
                    refreshAdapterData(searchResults);
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    private void fetchContacts() {
        contactDAO.open();
        contactsList = contactDAO.getAllContacts();
        contactDAO.close();
    }
    private void refreshAdapterData(){
        fetchContacts();
        adapter = new ContactsAdapter(this,contactsList,this);
        recyclerView.setAdapter(adapter);
    }
    private void refreshAdapterData(List<ContactModel> contacts){
        fetchContacts();
        adapter = new ContactsAdapter(this,contacts,this);
        recyclerView.setAdapter(adapter);
    }
    // This method is specifically used from the XML "clearSearch" button
    public void refreshAdapterData(View view) {
        fetchContacts();
        adapter = new ContactsAdapter(this,contactsList,this);
        recyclerView.setAdapter(adapter);
    }

    private void filterData(String filterColumn, String filterValue){
        if(filterColumn != "None") {
            // Validate column value
            if (filterColumn != "name" && filterColumn != "email") {
                Toast.makeText(this, "Invalid column selected", Toast.LENGTH_SHORT).show();
            }

            contactDAO.open();
//            List<ContactModel> contacts = contactDAO.getContactsByAttribute(filterColumn, filterValue);
            List<ContactModel> contacts = contactDAO.getAllContacts();
            contactDAO.close();
            contacts = filterContactsByColumn(contacts,filterColumn,filterValue);
            Toast.makeText(this, ""+contacts, Toast.LENGTH_SHORT).show();
            refreshAdapterData(contacts);
        }else{
            fetchContacts();
        }
    }

    private List<ContactModel> filterContactsByColumn(List<ContactModel> contacts, String filterColumn, String filterValue) {
        return contacts.stream()
                .filter(contact -> {
                    switch (filterColumn) {
                        case "name":
                            return contact.getName().equalsIgnoreCase(filterValue);
                        case "email":
                            return contact.getEmail().equalsIgnoreCase(filterValue);
                        default:
                            return false;  // Handle unknown column
                    }
                })
                .collect(Collectors.toList());
    }

}