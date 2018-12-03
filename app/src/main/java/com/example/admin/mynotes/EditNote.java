package com.example.admin.mynotes;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class EditNote extends AppCompatActivity implements DatePickerFragment.OnFragmentInteractionListener {

    // Declare a Bundle for information from parent activity
    Bundle bundle;
    // Declare a long to store retrieved information
    long id;
    // Declare DBHandler to handle modifications to database
    DBHandler dbHandler;
    // Declare EditTexts
    EditText edit_title;
    EditText edit_create_date;
    EditText edit_end_date;
    EditText edit_note;
    // Declare Calendar object
    Calendar calendar;
    // Create a FragmentManager object
    FragmentManager manager = getSupportFragmentManager();
    // Create a control index object initialized to zero (0)
    int dialogIndex = 0;
    // Declare a String object to hold the title
    String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: FloatingActionButton to be remove later
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // initialize the UpButton to return to parent activity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Pass extra data to bundle
        bundle = this.getIntent().getExtras();

        // Assign data in bundle to id if bundle not null
        if (bundle != null) {
            id = bundle.getLong("_id");
        }

        // Initialize the calendar object
        calendar = Calendar.getInstance();

        // initialize the dbHandler object
        dbHandler = new DBHandler(this, null);

        // Initialize the String object
        activityTitle = "Edit: " + dbHandler.getNoteAttribute((int) id, "title");

        // Set the default activity title to the new title
        this.setTitle(activityTitle);

        // Initialize the EditTexts with associated views in the activity
        edit_title = findViewById(R.id.change_title);
        edit_create_date = findViewById(R.id.change_cDate);
        edit_end_date = findViewById(R.id.change_eDate);
        edit_note = findViewById(R.id.change_description);

        // Bind data from the database to the corresponding EditText views to be edited
        edit_title.setText(dbHandler.getNoteAttribute((int) id, "title"));
        edit_create_date.setText(dbHandler.getNoteAttribute((int) id, "date_created"));
        edit_note.setText(dbHandler.getNoteAttribute((int) id, "description"));
        String eDate = dbHandler.getNoteAttribute((int) id, "date_end");
        if (!eDate.equals("None")) {
            edit_end_date.setText(eDate);
        } else {
            edit_end_date.setText("");
        }

        // register an OnClickListener to the date EditText
        edit_create_date.setOnClickListener(new View.OnClickListener() {
            /**
             * This method get called when the date EditText is clicked
             *
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
        edit_end_date.setOnClickListener(new View.OnClickListener() {
            /**
             * This method get called when the date EditText is clicked
             *
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
        newDialog.show(manager, "Set New Date");
    }

    /**
     * This method sets the Action Bar of the EditNote to whatever is defined in the
     * menu_edit_note menu resource.
     *
     * @param menu Menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    /**
     * This method gets called when an item in the overflow menu is selected.
     * (Currently, this method only has additional functionality for the UpButton on
     * the AppBar.)
     *
     * @param item MenuItem object that contains information about the item
     *             selected  in the overflow
     * @return true if an item is selected, else false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Additional functionality to be carried out by the UpButton.  In this case
            // treat UpButton operation as discard functionality.
            case android.R.id.home:
                discardEdit(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // This method updates the database when the save button is clicked in the AppBar
    public void saveEdit(MenuItem item) {
        String title = edit_title.getText().toString();
        String create_date = edit_create_date.getText().toString();
        String end_date = edit_end_date.getText().toString();
        String note = edit_note.getText().toString();

        if (title.trim().equals("") || create_date.trim().equals("") || note.trim().equals("")) {
            // Create date, title and description must be entered.
            Toast.makeText(this, "Please enter a Title, Created Date, and Description.",
                    Toast.LENGTH_SHORT).show();
        } else if (!end_date.trim().equals("") && create_date.compareToIgnoreCase(end_date) > 0) {
            // If there is an end date, it cannot be earlier that the created date
            Toast.makeText(this, "End Date cannot be before Create Date.",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Update the title, create date, end date and description
            dbHandler.updateNote((int) id, title, create_date, end_date, note);
            // Display a Toast when note is successfully edited
            Toast.makeText(this, "Note Edited.", Toast.LENGTH_SHORT).show();
            // Return to the parent activity an ok resultCode
            setResult(Activity.RESULT_OK);
            // Ends this activity
            finish();
        }
    }

    // This method ends the activity regardless if changes were made or not.
    // If changes were made, the will not me stored.
    public void discardEdit(MenuItem item) {
        // Return to the parent activity a canceled resultCode
        setResult(Activity.RESULT_CANCELED);
        finish();
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
                edit_create_date.setText(new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(calendar.getTime()));
                Toast.makeText(this, "Create Date Set", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                edit_end_date.setText(new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(calendar.getTime()));
                Toast.makeText(this, "End Date Set ", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
