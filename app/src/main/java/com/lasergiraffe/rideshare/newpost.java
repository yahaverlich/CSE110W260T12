package com.lasergiraffe.rideshare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by lamki on 2/1/2016.
 */
public class newpost extends Activity {
    private Note note;
    EditText noteDetails; //details
    EditText noteName; //name
    EditText notePhone; //phone
    EditText noteToDest;
    EditText noteFromDest;
    EditText notePrice;
    EditText noteCapacity;
    Button submit;

    /* Calendar for the newPost */
    //final Calendar inputCal = Calendar.getInstance();
    //EditText inputDate = (EditText) findViewById(R.id.date_id);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        submit = (Button) findViewById(R.id.submit_id);

        noteName = (EditText) findViewById(R.id.name_id);
        notePhone = (EditText) findViewById(R.id.phone_id);
        noteToDest = (EditText) findViewById(R.id.destTo_id);
        noteFromDest = (EditText) findViewById(R.id.destFrom_id);
        notePrice = (EditText) findViewById(R.id.price_id);
        noteCapacity = (EditText) findViewById(R.id.numSeats_id);
        noteDetails = (EditText) findViewById(R.id.details_id);

        note = new Note();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                note.setId(note.getObjectId());
                note.setDetails(noteDetails.getText().toString());
                note.setPhone(notePhone.getText().toString());
                note.setCapacity(Integer.parseInt(noteCapacity.getText().toString()));
                note.setFromDest(noteFromDest.getText().toString());
                note.setToDest(noteToDest.getText().toString());
                note.setPrice(Double.parseDouble(notePrice.getText().toString()));

                //make an addCurrNumRider method and do riders++ ?????
                note.setCurrNumRiders(1);
                //USE this for Parse USERNAME -- ParseUser.getCurrentUser().getUsername().toString()
                note.setName(noteName.getText().toString());

                note.put("noteTitle", note.getTitle());
                note.put("noteDetails", note.getDetails());
                note.put("noteName", note.getName());
                note.put("notePhone", note.getPhone());
                note.put("notePrice", note.getPrice());
                note.put("noteToDest", note.getToDest());
                note.put("noteFromDest", note.getFromDest());
                note.put("noteID", note.getId());
                note.put("noteCapacity", note.getCapacity());
                note.put("noteCurrNumRiders", note.getCurrNumRiders());
                note.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser user = ParseUser.getCurrentUser();
                            List<String> myGroups = (ArrayList<String>) user.get("group_key");
                            myGroups.add(note.getObjectId());
                            user.put("group_key", myGroups);
                            user.saveInBackground();
                            //Intent main = new Intent(newpost.this, MainActivity.class);
                            //startActivity(main);
                            finish();
                        } else {
                            Log.v("System.out", e.getMessage());
                        }
                    }
                });

            }
        });


        /* when Date of Departure EditText is Clicked, pop up a calendar on screen */
/*
        inputDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(newpost.this, new DatePickerDialog(), inputCal
                        .get(Calendar.YEAR), inputCal.get(Calendar.MONTH),
                        inputCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/



    }

/*
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            inputCal.set(Calendar.YEAR, year);
            inputCal.set(Calendar.MONTH, monthOfYear);
            inputCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    /* update the Date Label once the Date is chosen
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        inputDate.setText(sdf.format(inputCal.getTime()));
    }
*/
    public void setDriver(View view){

        boolean isOn = ((ToggleButton) view).isChecked();
        EditText price = (EditText) findViewById(R.id.price_id);


        if (isOn) {
            Toast.makeText(getApplicationContext(), "You are a Driver", Toast.LENGTH_SHORT).show();
            price.setHint(R.string.pricePost);

        }
        else {
            Toast.makeText(getApplicationContext(), "You are a Rider", Toast.LENGTH_SHORT).show();
            price.setHint(R.string.priceEstPost);
        }
    }

    public boolean getDriver(View view){

        boolean isOn = ((ToggleButton) view).isChecked();
        return isOn;
    }
}

