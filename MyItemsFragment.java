package com.example.project1;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyItemsFragment extends Fragment {

    DatabaseHelper database;
    RecyclerView recyclerView;
    TextView txtEmpty;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_my_items,
                container,
                false
        );

        database =
                new DatabaseHelper(requireContext());

        recyclerView =
                view.findViewById(R.id.recyclerMyItems);

        txtEmpty =
                view.findViewById(R.id.txtMyEmpty);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        loadItems();

        return view;
    }

    private void loadItems() {

        ArrayList<Item> items =
                database.getAllItems();

        ItemAdapter adapter =
                new ItemAdapter(items, item -> {

                    Intent intent =
                            new Intent(
                                    requireContext(),
                                    DetailActivity.class
                            );

                    intent.putExtra(
                            "item_id",
                            item.id
                    );

                    startActivity(intent);
                });

        recyclerView.setAdapter(adapter);

        if (items.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (database != null) {
            loadItems();
        }
    }
}
