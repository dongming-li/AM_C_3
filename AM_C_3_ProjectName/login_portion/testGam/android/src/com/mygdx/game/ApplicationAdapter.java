package com.mygdx.game;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ApplicationAdapter extends ArrayAdapter<Application>{
    private List<Application> items;

    public ApplicationAdapter(Context context, List<Application> items) {
        super(context, R.layout.app_custom_list, items);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.app_custom_list, null);
        }

        Application app = items.get(position);

        if(app != null) {
            TextView usernameText = (TextView)v.findViewById(R.id.usernameTxt);
            TextView nameText = (TextView)v.findViewById(R.id.nameTxt);
            TextView ageText = (TextView)v.findViewById(R.id.ageTxt);
            TextView passwordText = (TextView)v.findViewById(R.id.passwordTxt);

            if(usernameText != null) usernameText.setText(app.getUsername());
            if(nameText != null) nameText.setText(app.getName());
            if(ageText != null) ageText.setText(app.getAge());
            if(passwordText != null) passwordText.setText(app.getPassword());
        }

        return v;
    }
}