package com.example.firebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirestoreDebug";
    private FirebaseFirestore db;
    
    private EditText etName, etAge, etCourse;
    private Button btnSave, btnRead;
    private TextView tvDisplay, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        tvStatus = findViewById(R.id.tvStatus);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etCourse = findViewById(R.id.etCourse);
        btnSave = findViewById(R.id.btnSave);
        btnRead = findViewById(R.id.btnRead);
        tvDisplay = findViewById(R.id.tvDisplay);

        tvStatus.setText("Using Cloud Firestore ✅");
        tvStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Orange for Firestore

        // ✅ SAVE TO FIRESTORE
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String course = etCourse.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            tvDisplay.setText("Saving to Firestore...");

            // Create a Map for Firestore
            Map<String, Object> student = new HashMap<>();
            student.put("name", name);
            student.put("age", age);
            student.put("course", course);
            student.put("timestamp", com.google.firebase.Timestamp.now());

            // Add a new document with a generated ID
            db.collection("students")
                .add(student)
                .addOnSuccessListener(documentReference -> {
                    tvDisplay.setText("SUCCESS: Saved to Firestore! ✅\nID: " + documentReference.getId());
                    Toast.makeText(this, "Student Added!", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    tvDisplay.setText("FIRESTORE ERROR: " + e.getMessage());
                    Log.e(TAG, "Error adding document", e);
                });
        });

        // ✅ READ FROM FIRESTORE
        btnRead.setOnClickListener(v -> {
            tvDisplay.setText("Fetching last student...");
            
            // Get the most recent student based on timestamp
            db.collection("students")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String course = document.getString("course");
                                tvDisplay.setText("Latest Firestore Entry:\nName: " + name + "\nCourse: " + course);
                            }
                        } else {
                            tvDisplay.setText("Firestore collection 'students' is empty.");
                        }
                    } else {
                        tvDisplay.setText("Fetch Failed: " + task.getException().getMessage());
                    }
                });
        });
    }

    private void clearFields() {
        etName.setText("");
        etAge.setText("");
        etCourse.setText("");
    }
}
