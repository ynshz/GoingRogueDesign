package com.example.goingroguedesign.ui.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.goingroguedesign.R;

public class CalculatorFragment extends Fragment {

    CardView cvCommercial, cvHotel, cvResidential;
    ImageView ivCommercial, ivHotel, ivResidential;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        cvCommercial = root.findViewById(R.id.cvCommercial);
        cvHotel = root.findViewById(R.id.cvHotel);
        cvResidential = root.findViewById(R.id.cvResidential);
        ivResidential = root.findViewById(R.id.ivResidential);
        ivCommercial = root.findViewById(R.id.ivCommercial);
        ivHotel = root.findViewById(R.id.ivHotel);


        Glide.with(getActivity()).load("https://images.adsttc.com/media/images/5584/3b40/e58e/ce17/3700/0135/large_jpg/1.jpg?1434729259").centerCrop().into(ivResidential);
        Glide.with(getActivity()).load("https://qph.fs.quoracdn.net/main-qimg-835e544d1c56af5d70a0c27659ce6b7f").centerCrop().into(ivCommercial);
        Glide.with(getActivity()).load("https://bloximages.chicago2.vip.townnews.com/tucson.com/content/tncms/assets/v3/editorial/f/01/f01d605e-e440-5c9d-94f9-be1c05b27060/5d571739c611e.image.jpg?resize=1200%2C715").centerCrop().into(ivHotel);


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
