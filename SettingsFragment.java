package com.example.project1;



import android.content.Context;
import android.content.SharedPreferences;
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

public class SettingsFragment extends Fragment {

    EditText edtName;
    EditText edtRoll;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
        );

        edtName =
                view.findViewById(R.id.edtName);

        edtRoll =
                view.findViewById(R.id.edtRoll);

        SharedPreferences preferences =
                requireContext().getSharedPreferences(
                        "profile",
                        Context.MODE_PRIVATE
                );

        edtName.setText(
                preferences.getString("name", "")
        );

        edtRoll.setText(
                preferences.getString("roll", "")
        );

        Button btnSave =
                view.findViewById(
                        R.id.btnSaveProfile
                );

        btnSave.setOnClickListener(v -> {

            preferences.edit()
                    .putString(
                            "name",
                            edtName.getText().toString()
                    )
                    .putString(
                            "roll",
                            edtRoll.getText().toString()
                    )
                    .apply();

            Toast.makeText(
                    requireContext(),
                    "Profile saved",
                    Toast.LENGTH_SHORT
            ).show();
        });

        return view;
    }
}