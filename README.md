
# Contact Manager Application

This Android app project involves the development of a personalized contacts management app for Android using Java and XML with a backend SQLite database. The app enables users to efficiently store and manage their contacts' information.


## Tech Stack

**Android Native**

 - Java
 - XML

**Database:**
 - SQLite


## Requirements

**User Interface**

Design the user interface using XML to create an intuitive and user-friendly form for adding and updating contacts. The form should include the following fields:

    1. Contact Name (TextInputLayout)
    2. Contact Email (TextInputLayout)
    3. Contact Phone Number (TextInputLayout)
    4. Contact Birthday (DatePicker)
    5. Save Button (Button) to add a new contact or update an existing contact

**Database Schema**

Define the structure of the SQLite database that will store the contact information. The database should have a single table named "Contacts" with the following columns:

    1. ID (Primary Key, Auto-increment)
    2. Name (Text)
    3. Email (Text)
    4. Phone Number (Text)
    5. Birthday (Text)

**Java Code Implementation**

Implement the necessary Java classes to handle the database operations, including:

    1. DatabaseHelper: 
        - A subclass of SQLiteOpenHelper to manage database creation and version management.
    2. ContactModel:
        - A POJO (Plain Old Java Object) class to represent a contact entity, and it should include appropriate getter and setter methods.
    3. ContactDAO (Data Access Object):
        - A class to perform CRUD (Create, Read, Update, Delete) operations on the database. The class should contain methods for:
            - Inserting a new contact into the database.
            - Updating an existing contact's information.
            - Deleting a contact from the database.
            - Retrieving a list of all contacts from the database.
            - Querying contacts by their attributes (e.g., name, email, phone number, or birthday).
## Features

- When the user opens the app, they should see a list of their saved contacts (if any) displayed in a RecyclerView or ListView.
- Users can tap on a contact in the list to view its details in the form fields.
- The user can add a new contact by entering the necessary information in the form and tapping the "Save" button.
- If the user taps on an existing contact in the list, the form should populate with that contact's details. The user can then edit the information and save the changes.
- The app should perform appropriate input validation to ensure that the required fields are filled correctly, such as valid email addresses and phone numbers.

**Other Enhancements**

- Allow users to sort and filter their contacts based on different attributes (e.g., name, birthday).
- Implement a search feature that allows users to search for specific contacts by name or email.
- Implement a "Delete" confirmation dialog to prevent accidental deletions.

## Submission Guidelines

Submit your solution as a well-organized Android Studio project, including all the necessary XML layout files, Java classes, and any additional resources used. Ensure that your code is thoroughly commented and follows best coding practices for maintainability and readability. Also, include screenshots of the UI that are necessary for evaluation.
## Acknowledgements

Proposed by **[Richfield: Graduate Institute of Technology]('https://github.com/ShekoG1/the_contact_manager')** as one of my final year (and final semester, 2023) projects.


## Authors

- Made with &#x2764; by **[Shekhar Maharaj]('https://www.theshekharmaharaj.com')**



*'Making dreams come true'*

