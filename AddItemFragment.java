package com.example.project1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class AddItemFragment extends Fragment {
    EditText edtTitle, edtDescription, edtDate, edtPhone, edtEmail;
    RadioGroup radioType;
    Spinner spinnerCategory;
    ImageView imagePreview;
    String imageUri = "";

    ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri.toString();
                    imagePreview.setImageURI(uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        edtTitle = view.findViewById(R.id.edtTitle);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtDate = view.findViewById(R.id.edtDate);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtEmail = view.findViewById(R.id.edtEmail);
        radioType = view.findViewById(R.id.radioType);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        imagePreview = view.findViewById(R.id.imagePreview);

        String[] categories = {"Book", "Mobile", "ID Card", "Bottle", "Laptop", "Other"};
        spinnerCategory.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, categories));

        edtDate.setOnClickListener(v -> showDatePicker());
        view.findViewById(R.id.btnChooseImage).setOnClickListener(v -> imagePicker.launch("image/*"));
        view.findViewById(R.id.btnSave).setOnClickListener(v -> saveItem());
        return view;
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (v, year, month, day) ->
                edtDate.setText(day + "/" + (month + 1) + "/" + year),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveItem() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioType.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(requireContext(), "Select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = radioType.findViewById(selectedId);
        String type = selected.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        Item item = new Item(0, type, title, description, category, date, imageUri,
                edtPhone.getText().toString().trim(), edtEmail.getText().toString().trim(), 0);

        long result = new DatabaseHelper(requireContext()).addItem(item);
        if (result != -1) {
            Toast.makeText(requireContext(), "Item posted successfully", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).showHome();
        } else {
            Toast.makeText(requireContext(), "Could not save item", Toast.LENGTH_SHORT).show();
        }
    }
}
