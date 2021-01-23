package com.limap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.limap.R;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String[] variety_cowox;
    String[] variety_buffalo;

    ArrayAdapter adaptervariety;
    Spinner variety;

    RadioButton cow;
    RadioButton buffalo;
    RadioButton ox;

    TextView tvspeciality;
    RadioGroup specialitycb;

    RadioGroup category;

    private RadioButton gabhan;
    private String varietyText;

    private Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSearch   =   findViewById(R.id.btnSearch);

        tvspeciality = findViewById(R.id.tvspeciality);
        specialitycb = findViewById(R.id.specialitycb);

        variety_cowox = getResources().getStringArray(R.array.array_variety_cowox);
        variety_buffalo = getResources().getStringArray(R.array.array_variety_buffalo);

        variety = findViewById(R.id.spinnerVariety);
        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);
        // varietyText = variety.getSelectedItem().toString();

        variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                varietyText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category = findViewById(R.id.category);
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.cow:
                        setCow();
                        break;
                    case R.id.buffalo:
                        setBuffalo();
                        break;
                    case R.id.ox:
                        setOx();
                        break;
                }
            }
        });
        specialitycb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.gabhan:
                        break;
                    case R.id.dubhti:
                        break;

                    case R.id.vasaru:
                        setOx();
                        break;
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SearchActivity.this,SearchDetailsActivity.class);
              //  intent.putExtra("category",c)
             //   startActivity(intent);
                next();
            }
        });
    }
    private void setCow() {

        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);

        tvspeciality.setVisibility(View.VISIBLE);
        specialitycb.setVisibility(View.VISIBLE);

        //  agelayout.setHint(getString(R.string.hint_age_cowbuffalo));
    }

    private void setBuffalo() {

        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_buffalo);
        variety.setAdapter(adaptervariety);

        tvspeciality.setVisibility(View.VISIBLE);
        specialitycb.setVisibility(View.VISIBLE);

        //  agelayout.setHint(getString(R.string.hint_age_cowbuffalo));

    }

    private void setOx() {

        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);

        tvspeciality.setVisibility(View.GONE);
        specialitycb.setVisibility(View.GONE);

        //agelayout.setHint(getString(R.string.hint_age_ox));

    }
    private void next() {

        try {
            Bundle bundle = new Bundle();

            switch (category.getCheckedRadioButtonId()) {

                case R.id.cow:

                    bundle.putString("category", "Cow");
                    break;
                case R.id.buffalo:

                    bundle.putString("category", "Buffalo");
                    break;

                case R.id.ox:

                    bundle.putString("category", "Ox");
                    break;
            }

            switch (specialitycb.getCheckedRadioButtonId()) {

                case R.id.gabhan:
                    bundle.putString("speciality", "Gabhan");
                    break;
                case R.id.dubhti:
                    bundle.putString("speciality", "Dubhati");
                    break;

                case R.id.vasaru:
                    bundle.putString("speciality", "Vasaru");
                    break;
            }

            bundle.putString("variety", varietyText);

            Intent target = new Intent(getApplicationContext(), SearchDetailsActivity.class);
            target.putExtras(bundle);
            startActivity(target);

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error:" + e, Toast.LENGTH_LONG).show();
            Log.e("ERROr", "" + e);

        }

    }
}
