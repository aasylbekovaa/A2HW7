package com.example.todomanager06.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todomanager06.App;
import com.example.todomanager06.adapter.HomeAdapter;
import com.example.todomanager06.databinding.FragmentHomeBinding;
import com.example.todomanager06.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragments extends Fragment {
    HomeAdapter homeAdapter;
    private FragmentHomeBinding binding;
    
    private ArrayList<TaskModel> taskModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        initClickers();
    }

    private void initClickers() {
        binding.openCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTaskFragment createTaskFragment = new CreateTaskFragment();
                createTaskFragment.show(requireActivity().getSupportFragmentManager(), "");
            }
        });
    }


    private void initAdapter() {
        App.getApp().getDb().taskDao().getData().observe(getViewLifecycleOwner(), listTask -> {
            homeAdapter = new HomeAdapter(listTask);
            binding.homeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.homeRecycler.setAdapter(homeAdapter);
        });
    }


    public void OnLongClick(TaskModel model) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Удалить запись")
                .setMessage("Вы уверены, что хотите удалить эту запись?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        App.getApp().getDb().taskDao().delete(model);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}