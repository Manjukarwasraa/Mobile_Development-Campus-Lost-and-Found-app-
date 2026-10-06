package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QueriesFragment extends Fragment {
    DatabaseHelper db;
    RecyclerView recycler;
    TextView empty;
    Spinner filterSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queries, container, false);
        db = new DatabaseHelper(requireContext());
        recycler = view.findViewById(R.id.recyclerQueries);
        empty = view.findViewById(R.id.txtQueryEmpty);
        filterSpinner = view.findViewById(R.id.spinnerFilter);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        String[] filters = {"All", "Lost", "Found", "Resolved"};
        filterSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, filters));
        filterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View v, int position, long id) {
                load(filters[position]);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        String filter = getArguments() == null ? "All" : getArguments().getString("filter", "All");
        int pos = java.util.Arrays.asList(filters).indexOf(filter);
        if (pos >= 0) filterSpinner.setSelection(pos);
        return view;
    }

    private void load(String filter) {
        ArrayList<Item> list;
        if ("Lost".equals(filter)) list = db.getItemsByType("Lost");
        else if ("Found".equals(filter)) list = db.getItemsByType("Found");
        else if ("Resolved".equals(filter)) list = db.getResolvedItems();
        else list = db.getAllItems();

        empty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        recycler.setAdapter(new ItemAdapter(list, item -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("item_id", item.id);
            startActivity(intent);
        }));
    }

    @Override public void onResume() {
        super.onResume();
        if (filterSpinner != null && filterSpinner.getSelectedItem() != null) {
            load(filterSpinner.getSelectedItem().toString());
        }
    }
}
