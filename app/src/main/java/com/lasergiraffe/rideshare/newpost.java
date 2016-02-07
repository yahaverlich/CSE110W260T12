package com.lasergiraffe.rideshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;
import com.parse.ParseObject;

/**
 * Created by lamki on 2/1/2016.
 */
public class newpost extends Activity{
    private Note note;
    EditText title;
    EditText description; //content
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        submit = (Button) findViewById(R.id.submit_id);
        title = (EditText) findViewById(R.id.title_id);
        description = (EditText) findViewById(R.id.description_id);

        note = new Note();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                note.setId("my new id");
                note.setTitle(title.getText().toString());
                note.setContent(description.getText().toString());

                note.put("title", note.getTitle());
                note.saveInBackground();

                Intent main = new Intent(newpost.this, MainActivity.class);
                startActivity(main);
                /*Intent main = new Intent();
                setResult(0, main);
                finish();*/

            }
        });

        Button clear = (Button) findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent main = new Intent();
                setResult(5, main);
                finish();
            }});
        }
    }


