package com.example.todomanager06.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.todomanager06.App;
import com.example.todomanager06.R;
import com.example.todomanager06.databinding.FragmentCreateTaskBinding;
import com.example.todomanager06.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateTaskFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {
    FragmentCreateTaskBinding binding;
    private int startYear;
    private int startMonth;
    private int startDay;
    private String date;
    private String repeat;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClickers();
    }

    private void initClickers() {
        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToDataBase();
                dismiss();
            }
        });
        binding.chooseDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        binding.chooseRepeatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRepeatDialog();
            }
        });
    }


    private void writeToDataBase() {
        String text = binding.taskEd.getText().toString();
        TaskModel taskModel = new TaskModel(text, date, repeat);
        App.getApp().getDb().taskDao().insert(taskModel);
        Map<String, String> task = new HashMap<>();
        task.put("task", taskModel.getTask());
        task.put("date", taskModel.getDate());
        task.put("repeat", taskModel.getRepeat());

        db.collection("tasks").add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, startYear, startMonth, startDay);
        datePickerDialog.show();
    }

    private void showRepeatDialog() {

        Dialog alertDialog = new Dialog(requireContext(), R.style.CustomBottomSheetDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.repeat_dialog, requireView().findViewById(R.id.bottom_shit_con));

        alertDialog.setContentView(view);
        alertDialog.show();


        RadioButton never = alertDialog.findViewById(R.id.never_btn);
        RadioButton everyDay = alertDialog.findViewById(R.id.day_btn);
        RadioButton everyWeer = alertDialog.findViewById(R.id.week_btn);
        RadioButton everyMonth = alertDialog.findViewById(R.id.month_btn);
        RadioButton everyYear = alertDialog.findViewById(R.id.year_btn);
        RadioButton custom = alertDialog.findViewById(R.id.Custom_btn);
        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "never";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "Every day";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
        everyWeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "Every week";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
        everyMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "Every month";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
        everyYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "Every year";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat = "Custom";
                binding.chooseRepeatTv.setText(repeat);
                alertDialog.dismiss();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        binding.chooseDateTv.setText("" + day + "." + month + 1 + "." + year);
        date = ("" + day + "." + month + 1 + "." + year);

    }
}