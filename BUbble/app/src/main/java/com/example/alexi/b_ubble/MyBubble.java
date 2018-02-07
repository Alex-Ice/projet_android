package com.example.alexi.b_ubble;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MyBubble extends AppCompatActivity {

    String arrayName[] = {"B-Humor",
            "Pictures",
            "Private Messages",
            "Settings",
            "Logout"};
    FirebaseAuth user = FirebaseAuth.getInstance();

    private Bitmap imageBitmap;
    private Uri resultUri;
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private String userID;
    private long nbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bubble);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circlemenu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_add_circle_black_36dp, R.drawable.ic_clear_black_36dp)
                .addSubMenu(Color.parseColor("#6DE1D5"), R.drawable.bubble_icon)
                .addSubMenu(Color.parseColor("#20599A"), R.drawable.picture_icon)
                .addSubMenu(Color.parseColor("#0D5FCA"), R.drawable.mp_icon)
                .addSubMenu(Color.parseColor("#EA6E10"), R.drawable.settings_icon1)
                .addSubMenu(Color.parseColor("#F10C0C"), R.drawable.ic_exit_to_app_black_24dp)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        if(arrayName[index]=="Settings")
                        {Intent intent = new Intent(MyBubble.this, ParamActivity.class);
                            new CounterTask().execute(intent);
                        }
                        if(arrayName[index]=="Private Messages")
                        {Intent intent = new Intent(MyBubble.this, Contacts.class);
                            new CounterTask().execute(intent);
                        }
                        if(arrayName[index]=="Logout")
                        {
                            finishAffinity();
                        }
                        if(arrayName[index]=="Pictures")
                        {
                            selectImage();
                        }

                        Toast.makeText(MyBubble.this, "You selected "+arrayName[index], Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void selectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyBubble.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    cameraIntent();
                }
                else if(items[i].equals("Gallery"))
                {
                    galleryIntent();
                }
                else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, 0);
        }
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            onSelectFromGalleryResult(data);

        }
        else if(requestCode == 0 && resultCode == Activity.RESULT_OK)
        {
            onCaptureImageResult(data);
        }

    }

    private void onSelectFromGalleryResult(Intent data)
    {
        final Uri imageUri = data.getData();
        resultUri = imageUri;
        savePicture();
    }

    private void onCaptureImageResult(Intent data)
    {
        Bundle extras = data.getExtras();
        imageBitmap = (Bitmap) extras.get("data");
        savePicture();
    }

    private void savePicture()
    {
        mCustomerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    mCustomerDatabase.child("Image");
                    if(dataSnapshot.exists())
                    {
                        nbr = dataSnapshot.getChildrenCount();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCustomerDatabase.child("Image").push();

        FirebaseUser user2 = mAuth.getCurrentUser();

        nbr = nbr + 1;

        final String nbrUser = userID + String.valueOf(nbr);
        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("images").child(nbrUser);

        if(user2 != null && resultUri != null)
        {
            Bitmap bitmap = null;

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put(nbrUser, downloadUrl.toString());
                    mCustomerDatabase.updateChildren(newImage);

                    finish();
                    return;

                }
            });

        }
        else if(user2 != null && imageBitmap != null)
        {

            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos2);
            byte[] data2 = baos2.toByteArray();
            UploadTask uploadTask2 = filePath.putBytes(data2);

            uploadTask2.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;
                }
            });
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put(nbrUser, downloadUrl.toString());
                    mCustomerDatabase.updateChildren(newImage);

                    finish();
                    return;

                }
            });

        }
        else
        {
            finish();
            return;
        }

    }

    class CounterTask extends AsyncTask<Intent, Void, Intent> {
        @Override
        protected Intent doInBackground(Intent... intents) {
            int a = 0;
            while (a != 200000000) {
                a++;
            } //environ 2 secondes
            return intents[0];
        }

        protected void onPostExecute(Intent result) {
            startActivity(result);
        }
    }
}
