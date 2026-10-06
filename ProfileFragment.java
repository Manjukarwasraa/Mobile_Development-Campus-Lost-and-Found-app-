package com.example.project1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    EditText edtName, edtRoll, edtPhone, edtEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edtName = view.findViewById(R.id.edtProfileName);
        edtRoll = view.findViewById(R.id.edtProfileRoll);
        edtPhone = view.findViewById(R.id.edtProfilePhone);
        edtEmail = view.findViewById(R.id.edtProfileEmail);
        Button save = view.findViewById(R.id.btnSaveProfile);

        android.content.SharedPreferences p = requireContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        edtName.setText(p.getString("name", ""));
        edtRoll.setText(p.getString("roll", ""));
        edtPhone.setText(p.getString("phone", ""));
        edtEmail.setText(p.getString("email", ""));

        save.setOnClickListener(v -> {
            p.edit()
                    .putString("name", edtName.getText().toString().trim())
                    .putString("roll", edtRoll.getText().toString().trim())
                    .putString("phone", edtPhone.getText().toString().trim())
                    .putString("email", edtEmail.getText().toString().trim())
                    .apply();
            Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}
