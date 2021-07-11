package com.mhyr.instagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment {

    private EditText edtProfileName , edtProfileBio , edtProfileProfession ,
            edtProfileHobbies , edtProfileSport;
    private Button btnProfileUpdateInfo;

    public ProfileTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtProfileBio = view.findViewById(R.id.edtProfileBio);
        edtProfileProfession = view.findViewById(R.id.edtProfileProfession);
        edtProfileHobbies = view.findViewById(R.id.edtProfileHobbies);
        edtProfileSport = view.findViewById(R.id.edtProfileSport);
        btnProfileUpdateInfo = view.findViewById(R.id.btnProfileUpdateInfo);

        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("profileName") == null) {

            edtProfileName.setText("");

        } else {

            edtProfileName.setText(parseUser.get("profileName") + "");

        }

        if (parseUser.get("profileBio") == null) {

            edtProfileBio.setText("");

        } else {

            edtProfileBio.setText(parseUser.get("profileBio") + "");

        }

        if (parseUser.get("profileProfession") == null) {

            edtProfileProfession.setText("");

        } else {

            edtProfileProfession.setText(parseUser.get("profileProfession") + "");

        }

        if (parseUser.get("profileHobbies") == null) {

            edtProfileHobbies.setText("");

        } else {

            edtProfileHobbies.setText(parseUser.get("profileHobbies") + "");

        }

        if (parseUser.get("profileSport") == null) {

            edtProfileSport.setText("");

        } else {

            edtProfileSport.setText(parseUser.get("profileSport") + "");

        }

        btnProfileUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parseUser.put("profileName" , edtProfileName.getText().toString());
                parseUser.put("profileBio" , edtProfileBio.getText().toString());
                parseUser.put("profileProfession" , edtProfileProfession.getText().toString());
                parseUser.put("profileHobbies" , edtProfileHobbies.getText().toString());
                parseUser.put("profileSport" , edtProfileSport.getText().toString());

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating Profile...");
                progressDialog.show();

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        try {

                            if (e == null) {

                                FancyToast.makeText(getContext(),
                                        "Info Updated",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS,
                                        false).show();

                            } else {

                                FancyToast.makeText(getContext(),
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.ERROR,
                                        false).show();

                            }

                            progressDialog.dismiss();

                        } catch (Exception error) {

                            error.printStackTrace();

                        }

                    }
                });

            }
        });

        return view;

    }

}