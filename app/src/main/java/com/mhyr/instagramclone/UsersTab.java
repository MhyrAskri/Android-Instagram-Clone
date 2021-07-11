package com.mhyr.instagramclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersTab extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    public UsersTab() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext() , android.R.layout.simple_list_item_1 , arrayList);

        listView.setOnItemClickListener(UsersTab.this);
        listView.setOnItemLongClickListener(UsersTab.this);

        TextView txtLoading , txtWait;
        txtLoading = view.findViewById(R.id.txtLoading);
        txtWait = view.findViewById(R.id.txtWait);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                try {

                    if (e == null) {

                        if (users.size() > 0) {

                            for (ParseUser user : users) {

                                arrayList.add(user.getUsername());

                            }

                            listView.setAdapter(arrayAdapter);
                            txtLoading.animate().alpha(0f).setDuration(1500);
                            txtWait.animate().alpha(0f).setDuration(1500);
                            listView.animate().alpha(1f).setDuration(1500);

                        }

                    }

                } catch (Exception error) {

                    error.printStackTrace();

                }

            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getContext() , UsersPosts.class);
        intent.putExtra("username" , arrayList.get(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username" , arrayList.get(i));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null && e == null) {

                    new AlertDialog.Builder(getContext())
                            .setTitle(user.getUsername() + "'s Info")
                            .setMessage("Bio: " + user.get("profileBio") + "\n"
                            + "Profession: " + user.get("profileProfession") + "\n"
                            + "Hobbies: " + user.get("profileHobbies") + "\n"
                            + "Sport: " + user.get("profileSport"))
                            .setIcon(R.drawable.person)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                } else {



                }

            }
        });

        return true;

    }

}