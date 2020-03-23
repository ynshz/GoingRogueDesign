package com.example.goingroguedesign.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.goingroguedesign.MainActivity;
import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.account.AccountFragment;
import com.example.goingroguedesign.ui.account.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private static String WEBSITE_URL = "https://www.goingroguedesignllc.com/";
    private static String FACEBOOK_URL = "https://www.facebook.com/goingroguedesign";
    private static String FACEBOOK_PAGE_ID = "1491233820996177";
    private static String TWITTER_URL = "https://twitter.com/SOLELINKS?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
    private static String INSTAGRAM_URL = "https://www.instagram.com/goingroguedesign/";
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView cvHomeListServices = root.findViewById(R.id.cvHomeListServices);
        TextView tvHomeWebLink = root.findViewById(R.id.tvHomeWebLink);
        TextView tvHomeEmailLink = root.findViewById(R.id.tvHomeEmailLink);
        ImageView ivHomeFacebook = root.findViewById(R.id.ivHomeFacebook);
        ImageView ivHomeTwitter = root.findViewById(R.id.ivHomeTwitter);
        ImageView ivHomeInstagram = root.findViewById(R.id.ivHomeInstagram);
        TextView tvSignInButton = root.findViewById(R.id.tvHomeSignInUsername);
        Button btnHomeRequestInfo = root.findViewById(R.id.btnHomeRequestInfo);
        ImageView ivMoreOptions = root.findViewById(R.id.ivMoreOptions);
        if (mUser != null) {
            tvSignInButton.setText(mUser.getDisplayName());


            /*
            tvSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment_container, new AccountFragment(), null)
                            .commit();
                }
            });

             */
            /*
            tvSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create new fragment and transaction
                    Fragment newFragment = new AccountFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                    transaction.replace(R.id.fragment_h, newFragment);
                    transaction.addToBackStack(null);

// Commit the transaction
                    transaction.commit();
                }
            });

             */
            ivMoreOptions.setVisibility(View.VISIBLE);
            ivMoreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                    popupMenu.getMenu().add(0, 0, 0, "Sign Out");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case 0:
                                    mAuth.signOut();
                                    Toast.makeText(getActivity(), "You have signed out successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        } else {
            tvSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
            });
        }



        btnHomeRequestInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestInfoActivity.class);
                startActivity(intent);
            }
        });
        cvHomeListServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListServicesActivity.class);
                startActivity(intent);
            }
        });

        tvHomeWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE_URL));
                startActivity(browserIntent);
            }
        });

        tvHomeEmailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?to=" + "GRD@GoingRogueDesignLLC.com" + "&subject=" + "Project Support");
                intent.setData(data);
                startActivity(intent);
            }
        });

        ivHomeFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookAppIntent;
                try {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + FACEBOOK_PAGE_ID));
                    startActivity(facebookAppIntent);
                } catch (ActivityNotFoundException e) {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
                    startActivity(facebookAppIntent);
                }
            }
        });

        ivHomeTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterIntent;
                try {
                    twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=solelinks"));
                    startActivity(twitterIntent);
                } catch (ActivityNotFoundException e) {
                    twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TWITTER_URL));
                    startActivity(twitterIntent);
                }
            }
        });

        ivHomeInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instagramIntent;
                try {
                    instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/goingroguedesign"));
                    instagramIntent.setPackage("com.instagram.android");
                    startActivity(instagramIntent);
                } catch (ActivityNotFoundException e) {
                    instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTAGRAM_URL));
                    startActivity(instagramIntent);
                }
            }
        });

        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        return root;
    }


}
