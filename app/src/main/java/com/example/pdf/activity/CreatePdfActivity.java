package com.example.pdf.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdf.R;
import com.example.pdf.model.Document;
import com.example.pdf.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;

public class CreatePdfActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText documentNamePrompt;
    private EditText descriptionPrompt;
    private EditText pricePrompt;
    private TextView errorDocumentName;
    private TextView errorDescription;
    private TextView errorPrice;
    private TextView importFileName;
    private ImageView documentImage;

    private Uri fileUri = null;
    private Uri documentImageUri = null;

    private Uri successFileUri = null;
    private Uri successDocumentImageUri = null;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_create);

        userName = getIntent().getStringExtra("createUserObj");
        System.out.println(userName);

        toolbar = findViewById(R.id.create_toolbar);
        documentNamePrompt = findViewById(R.id.document_name_prompt);
        descriptionPrompt = findViewById(R.id.description_prompt);
        pricePrompt = findViewById(R.id.price_prompt);
        errorDocumentName = findViewById(R.id.error_document_name);
        errorDescription = findViewById(R.id.error_description);
        errorPrice = findViewById(R.id.error_price);
        importFileName = findViewById(R.id.import_file_name);
        documentImage = findViewById(R.id.doc_image);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean validateFile(){
        if (fileUri == null){
            Toast.makeText(this, "You don't have file to upload.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validateDocumentName(){
        String documentName = documentNamePrompt.getText().toString().trim();
        if (documentName.isEmpty()){
            errorDocumentName.setText("Field can't be empty");
            return false;
        }
        errorDocumentName.setText(null);
        return true;
    }

    public boolean validateDescription(){
        String description = descriptionPrompt.getText().toString().trim();
        if (description.isEmpty()){
            errorDescription.setText("Field can't be empty");
            return false;
        } else if (description.length() < 20){
            errorDescription.setText("Description can more 20 letters");
            return false;
        } else if (description.length() > 100){
            errorDescription.setText("Description can't more 100 letters");
            return false;
        }
        errorDescription.setText(null);
        return true;
    }

    public boolean validatePrice(){
        String priceString = pricePrompt.getText().toString().trim();
        if (priceString.isEmpty()){
            errorPrice.setText("Field can't be empty");
            return false;
        } else if (!priceString.matches("([1-9][0-9]+)|([1-9][0-9]+[.][0-9]+)")){
            errorPrice.setText("Field can be a real number");
            return false;
        }
        errorPrice.setText(null);
        return true;
    }

    public void submit(View view){
        if (!validateFile() | !validateDocumentName() | !validateDescription() | !validatePrice()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is uploading...");
        progressDialog.show();

        StorageReference documentFolder = FirebaseStorage.getInstance().getReference().child("document");
        StorageReference documentFile = documentFolder.child("uploadPdf"+ System.currentTimeMillis()+ ".pdf");

        if (documentImageUri != null){
            StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("image");
            StorageReference imageFile = imageFolder.child("image"+ System.currentTimeMillis());
            imageFile.putFile(documentImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask1 = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask1.isComplete());
                    successDocumentImageUri = uriTask1.getResult();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressNumber = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Image uploaded.."+ (int) progressNumber+ "%");
                }
            });
        }

        documentFile.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String successDocumentImageUriString;
                if (successDocumentImageUri != null){
                    successDocumentImageUriString = successDocumentImageUri.toString();
                } else{
                    successDocumentImageUriString = "https://firebasestorage.googleapis.com/v0/b/pdf-project-81c89.appspot.com/o/image%2Fdefault.png?alt=media&token=e24b63e1-f04d-4ada-bd38-d4c997b529d1";
                }

                Task<Uri> uriTask2 = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask2.isComplete());
                successFileUri = uriTask2.getResult();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(String.format("%s/create", userName));
                Document doc = new Document(successFileUri.toString(), documentNamePrompt.getText().toString(), userName, LocalDate.now().toString(),
                        descriptionPrompt.getText().toString(), Double.parseDouble(pricePrompt.getText().toString()), successDocumentImageUriString);
                databaseReference.child(databaseReference.push().getKey()).setValue(doc);

                progressDialog.dismiss();
                finish();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressNumber = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded.."+ (int) progressNumber+ "%");
            }
        });
    }

    public void importDocument(View view){
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("application/pdf");
        startActivityForResult(fileIntent, 42);
    }

    public void importDocumentImage(View view){
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == Activity.RESULT_OK){
            if (data != null){
                fileUri = data.getData();
                String path = fileUri.getPath();
                importFileName.setText(path);
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            if (data != null){
                documentImageUri = data.getData();
                documentImage.setImageURI(documentImageUri);
            }
        }
    }
}