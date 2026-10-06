package com.example.project1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new DatabaseHelper(this);
        int id = getIntent().getIntExtra("item_id", -1);
        item = db.getItem(id);

        if (item == null) {
            finish();
            return;
        }

        ImageView image = findViewById(R.id.detailImage);
        TextView title = findViewById(R.id.detailTitle);
        TextView type = findViewById(R.id.detailType);
        TextView category = findViewById(R.id.detailCategory);
        TextView date = findViewById(R.id.detailDate);
        TextView status = findViewById(R.id.detailStatus);
        TextView description = findViewById(R.id.detailDescription);

        title.setText(item.title);
        type.setText(item.type);
        category.setText(item.category);
        date.setText(item.date);
        status.setText(item.resolved == 1 ? "Resolved" : "Active");
        description.setText(item.description);

        if (item.imageUri != null && !item.imageUri.isEmpty()) image.setImageURI(Uri.parse(item.imageUri));

        Button resolve = findViewById(R.id.btnResolve);
        Button delete = findViewById(R.id.btnDelete);
        Button call = findViewById(R.id.btnCall);
        Button email = findViewById(R.id.btnEmail);
        Button share = findViewById(R.id.btnShare);

        resolve.setText(item.resolved == 1 ? "Mark Active" : "Mark Resolved");
        resolve.setOnClickListener(v -> {
            db.markResolved(item.id, item.resolved == 1 ? 0 : 1);
            Toast.makeText(this, item.resolved == 1 ? "Item marked active" : "Item resolved", Toast.LENGTH_SHORT).show();
            finish();
        });

        delete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> {
                    db.deleteItem(item.id);
                    finish();
                }).show());

        call.setOnClickListener(v -> {
            if (item.phone == null || item.phone.isEmpty()) {
                Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone)));
        });

        email.setOnClickListener(v -> {
            if (item.email == null || item.email.isEmpty()) {
                Toast.makeText(this, "Email not available", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + item.email));
            startActivity(intent);
        });

        share.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, item.type + " item: " + item.title + "\n" + item.description + "\nDate: " + item.date);
            startActivity(Intent.createChooser(intent, "Share Item"));
        });
    }
}
