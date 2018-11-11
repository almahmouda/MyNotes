package com.example.admin.mynotes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateNote extends AppCompatActivity implements DatePickerFragment.OnFragmentInteractionListener {

    // Declare an intent
    Intent intent;
    // Declare EditTexts - used to reference  the EditText in the resource file
    EditText titleEditText;
    EditText dateCreatedEditText;
    EditText dateEndEditText;
    EditText descriptionEditText;
    // Declare Calendar - used to map the data selected in the DatePickerDialog
    Calendar calendar;
    // Declare a DBHandler to send data to the database
    DBHandler dbHandler;
    // Create a FragmentManager object to manage the creation of the fragment in the current
    // activity
    FragmentManager fm = getSupportFragmentManager();
    // Create a control index to know which button or view is calling the fragment
    int dialogIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Implement an Up Button in the AppBar to return to the calling (parent) activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize Calender
        calendar = Calendar.getInstance();

        // Initialize the EditTexts with associated views in the activity
        titleEditText = findViewById(R.id.titleEditText);
        dateCreatedEditText = findViewById(R.id.dateCreatedEditText);
        dateEndEditText = findViewById(R.id.dateEndEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        // Set the current date in the dateCreateEditText
        dateCreatedEditText.setText(new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(calendar.getTime()));

        // Initialize dbHandler
        dbHandler = new DBHandler(this, null);

        // register an OnClickListener to the date EditText
        dateCreatedEditText.setOnClickListener(new View.OnClickListener() {
            /**
             * This method get called when the date EditText is clicked
             * @param view because the date EditText that calls this method is technically
             *             considered a view, we must pass the method a View.
             */
            @Override
            public void onClick(View view) {
                // Set the dialogIndex for the dateCreatedEditText view
                dialogIndex = 1;
                // Call into view the DatePickerDialog
                callFragment();
            }
        });

        // register an OnClickListener to the date EditText
        dateEndEditText.setOnClickListener(new View.OnClickListener() {
            /**
             * This method get called when the date EditText is clicked
             * @param view because the date EditText that calls this method is technically
             *             considered a view, we must pass the method a View.
             */
            @Override
            public void onClick(View view) {
                // Set the dialogIndex for the dateEndEditText view
                dialogIndex = 2;
                // Call into view the DatePickerDialog
                callFragment();
            }
        });

    }

    // Creates and displays a new DatePickerDialog
    public void callFragment() {
        DialogFragment newDialog = new DatePickerFragment();
        newDialog.show(fm, "Select Date");
    }

    /**
     * This method sets the Action Bar of the CreateList to whatever is defined in the menu
     * create list menu resource.
     *
     * @param menu Menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set the Action Bar of the CreateList to whatever  is defined
        // in the menu main menu resource
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    /**
     * This method gets called when an item in the overflow menu is selected.
     *
     * @param item MenuItem object that contains information about the item
     *             selected  in the overflow : for example , its id
     * @return true if an item is selected, else false
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the id of the item selected
        switch (item.getItemId()) {
            case R.id.action_home:
                // initalize an intent for the CreateList, start intent,
                // return true if the id in the itme selected is for the Main
                // Activity
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method get called when the add_list menu item get pushed.
     *
     * @param menuItem because the add_list item that calls this method is
     *                 a menu item, we must pass the method a MenuItem.
     */
    public void createNote(MenuItem menuItem) {
        //get data in EditText and store it in Strings
        String title = titleEditText.getText().toString();
        String dateCreate = dateCreatedEditText.getText().toString();
        String dateEnd = dateEndEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if (title.trim().equals("") || dateCreate.trim().equals("") || description.trim().equals("")) {
            // required data hasn't been input, so display Toast
            Toast.makeText(this, "Please enter a title, date created, and a " +
                    "description ", Toast.LENGTH_SHORT).show();
        } else if (!dateEnd.trim().equals("") && dateCreate.compareTo(dateEnd) > 0) {
            // Displays a Toast if the End Date is before the Create Date
            Toast.makeText(this, "End date cannot be before create date! ",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Initialize a random color to be added to the database
            int color = plateColor();
            // required data has been input, update the database and display a different Toast
            dbHandler.addMyNote(title, dateCreate, dateEnd, description, color);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            // Destroys this activity and returns to the calling activity
            finish();
        }
    }

    // Creates a random color for the letter_plate in the ListView.
    private int plateColor() {
        int red = (int) (Math.random() * 200);
        int green = (int) (Math.random() * 200);
        int blue = (int) (Math.random() * 200);

        return Color.rgb(red, green, blue);
    }

    /**
     * Override the interface method to set the date in the calling view.
     * @param year the selected year
     * @param month the selected month (months range from 0-11 where 0 represents January)
     * @param day the selected day of the month
     */
    @Override
    public void setDate(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        switch (dialogIndex) {
            case 1:
                dateCreatedEditText.setText(new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(calendar.getTime()));
                Toast.makeText(this, "Create Date Set", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                dateEndEditText.setText(new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(calendar.getTime()));
                Toast.makeText(this, "End Date Set ", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}


