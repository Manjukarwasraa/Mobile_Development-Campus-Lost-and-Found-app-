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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    DatabaseHelper db;
    TextView txtLostCount, txtFoundCount, txtResolvedCount, txtEmpty;
    RecyclerView recyclerHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = new DatabaseHelper(requireContext());
        recyclerHome = view.findViewById(R.id.recyclerHome);
        txtLostCount = view.findViewById(R.id.txtLostCount);
        txtFoundCount = view.findViewById(R.id.txtFoundCount);
        txtResolvedCount = view.findViewById(R.id.txtResolvedCount);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        recyclerHome.setLayoutManager(new LinearLayoutManager(requireContext()));

        MaterialButton btnPost = view.findViewById(R.id.btnPostItem);
        btnPost.setOnClickListener(v -> ((MainActivity) requireActivity()).showAdd());

        MaterialCardView lostCard = view.findViewById(R.id.cardLost);
        MaterialCardView foundCard = view.findViewById(R.id.cardFound);
        MaterialCardView resolvedCard = view.findViewById(R.id.cardResolved);

        lostCard.setOnClickListener(v -> ((MainActivity) requireActivity()).showQueries("Lost"));
        foundCard.setOnClickListener(v -> ((MainActivity) requireActivity()).showQueries("Found"));
        resolvedCard.setOnClickListener(v -> ((MainActivity) requireActivity()).showQueries("Resolved"));

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> ((MainActivity) requireActivity()).showAdd());

        loadItems();
        return view;
    }

    private void loadItems() {
        ArrayList<Item> items = db.getAllItems();
        int lost = 0, found = 0, resolved = 0;
        ArrayList<Item> recent = new ArrayList<>();

        for (Item item : items) {
            if (item.resolved == 1) {
                resolved++;
            } else {
                if ("Lost".equals(item.type)) lost++;
                if ("Found".equals(item.type)) found++;
                if (recent.size() < 5) recent.add(item);
            }
        }

        txtLostCount.setText(String.valueOf(lost));
        txtFoundCount.setText(String.valueOf(found));
        txtResolvedCount.setText(String.valueOf(resolved));

        txtEmpty.setVisibility(recent.isEmpty() ? View.VISIBLE : View.GONE);

        recyclerHome.setAdapter(new ItemAdapter(recent, item -> {
            Intent intent = new Intent(requireContext(), DetailActivity.class);
            intent.putExtra("item_id", item.id);
            startActivity(intent);
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) loadItems();
    }
}
