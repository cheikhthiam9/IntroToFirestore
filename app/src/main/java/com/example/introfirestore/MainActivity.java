package com.example.introfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText enterTitle;
    private EditText enterThoughts;
    private Button saveButton, showButton;
    private TextView recTitle;
    private TextView recThought;

    //Collections works on a key/value pairs systems
    //The keys below will be attached to the values entered by the user
    //Creating Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHTS = "thoughts";

    //Creating a connection to firestore
    // now  database is now connected to our collection in firestore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference journalRef = database.collection("Journal").document("First Thoughts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.edit_text_title);
        enterThoughts = findViewById(R.id.edit_text_thoughts);
        recTitle = findViewById(R.id.rec_title);
        recThought = findViewById(R.id.rec_thought);
        showButton = findViewById(R.id.show_data_button);
        saveButton = findViewById(R.id.save_button);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GETTING THE VALUES FROM THE USER
                String title = enterTitle.getText().toString().trim();
                String thoughts = enterThoughts.getText().toString().trim();

                //USING A HASHMAP TO STORE DATA USING KEY/VALUE SYSTEM (REQUIRES)
                Map<String, Object> data = new HashMap<>();
                data.put(KEY_TITLE, title);
                data.put(KEY_THOUGHTS, thoughts);

                //Connection data Map to collections in firestore
                //We are creating a collection named 'Journal'
                //Inside of our Journal Collection we are creating a Document named 'First Thought'
                //Then, inside of the First Thought document we are inserting data in the doc
                journalRef.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainActivity", "onFailure" +e.toString());
                    }
                });



            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieving
                journalRef.get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
if (documentSnapshot.exists()){
    String title = documentSnapshot.getString(KEY_TITLE);
    String thought = documentSnapshot.getString(KEY_THOUGHTS);

    recTitle.setText(title);
    recThought.setText(thought);



}else {
    Toast.makeText(MainActivity.this, "No data exists", Toast.LENGTH_LONG).show();

}
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MainActivity", "onFailure" +e.toString());
                    }
                });

            }
        });

    }
}
