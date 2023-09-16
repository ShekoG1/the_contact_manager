package com.theshekharmaharaj.contactmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private DatePicker datePicker;
    private ContactDAO contactDAO;
    private long itemID = -1;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ContactDAO
        contactDAO = new ContactDAO(this);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.etContactName);
        emailEditText = findViewById(R.id.etContactEmail);
        phoneNumberEditText = findViewById(R.id.etContactPhone);
        datePicker = findViewById(R.id.datePicker);

        try {
            // Retrieve the contact details from the intent
            String contactName = getIntent().getStringExtra("contactName");
            String contactEmail = getIntent().getStringExtra("contactEmail");
            String contactPhone = getIntent().getStringExtra("contactPhone");
            String contactBirthday = getIntent().getStringExtra("contactBirthday");
            this.itemID = getIntent().getLongExtra("itemID",-1);
            this.isUpdate = getIntent().getBooleanExtra("isUpdate",false);

            if(this.isUpdate && itemID != -1) {
                // Split contactBirthday, then assign each index to it's respective variable
                String[] splitDate = contactBirthday.split("-");
                int birthYear = Integer.parseInt(splitDate[0]);
                int birthMonth = Integer.parseInt(splitDate[1]) - 1;
                int birthDay = Integer.parseInt(splitDate[2]);

                // Assign textual values
                nameEditText.setText(contactName);
                emailEditText.setText(contactEmail);
                phoneNumberEditText.setText(contactPhone);

                // Assign calender values
                datePicker.updateDate(birthYear, birthMonth, birthDay);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error: Could not recieve intent data", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveButtonClick(View view) {
        // Retrieve data from EditText fields
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        // Retrieve the date from DatePicker
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();
        long contactId;

        // Format the date (if needed)
        String birthday = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);

        // Validate that the user has entered valid input data
        if(TextUtils.isEmpty(name)){
            // Throw error
            Toast.makeText(this, "Error: Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            // Throw error
            Toast.makeText(this, "Error: Please enter an email address", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            // Throw error
            Toast.makeText(this, "Error: Please enter a phone number", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if phone number is valid
        if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_LONG).show();
            return ;
        }

        // Create a ContactModel object
        ContactModel contact = new ContactModel();
        contact.setName(name);
        contact.setEmail(email);
        contact.setPhoneNumber(phoneNumber);
        contact.setBirthday(birthday);

        // Save the contact to the database
        contactDAO.open();
        if(!isUpdate) {
            contactId = contactDAO.insertContact(contact);
        }else{
            contact.setId(itemID);
            contactId = contactDAO.updateContact(contact);
        }
        handleDBAction(contact.getId());
        contactDAO.close();
    }

    public void onBackButtonClick (View view){
        startActivity(new Intent(MainActivity.this, ContactsListActivity.class));
    }

    private void handleDBAction(long contactId){
        System.out.println("ContactID is: "+contactId);
        if (contactId != -1) {
            // Contact saved successfully
            Toast.makeText(this, "Success: Your contact has been saved", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, ContactsListActivity.class));
        } else {
            // Contact save failed
            Toast.makeText(this, "Error: Data NOT saved", Toast.LENGTH_LONG).show();
        }
    }
}
