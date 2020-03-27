package com.example.goingroguedesign.ui.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.goingroguedesign.R;

public class CalculatorFragment extends Fragment {

    CardView cvCommercial, cvHotel, cvResidential;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        cvCommercial = root.findViewById(R.id.cvCommercial);
        cvHotel = root.findViewById(R.id.cvHotel);
        cvResidential = root.findViewById(R.id.cvResidential);

        cvCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommercialActivity.class);
                getActivity().startActivity(intent);
            }
        });

        cvHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HotelActivity.class);
                getActivity().startActivity(intent);
            }
        });

        cvResidential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResidentialActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return root;
    }
}
