package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import us.mis.acmeexplorer.entity.Filter;

public class FilterActivity extends AppCompatActivity {
    private Filter filter = new Filter();
    private TextView textViewStartDate, textViewEndDate;
    private ImageView calendarStartDate, calendarEndDate;
    private EditText editMinPrice, editMaxPrice;
    private Button btnSearch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewEndDate = findViewById(R.id.textViewEndDate);
        calendarStartDate = findViewById(R.id.calendarStartDate);
        calendarEndDate = findViewById(R.id.calendarEndDate);
        editMinPrice = findViewById(R.id.editMinPrice);
        editMaxPrice = findViewById(R.id.editMaxPrice);
        btnSearch = findViewById(R.id.btnSearch);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        sharedPreferences = getSharedPreferences("SP_FILTER", MODE_PRIVATE);
        int filterMinPrice = sharedPreferences.getInt("FILTER_MIN_PRICE", 0);
        int filterMaxPrice = sharedPreferences.getInt("FILTER_MAX_PRICE", 0);
        long filterStartDate = sharedPreferences.getLong("FILTER_STARTDATE", new Date().getTime());
        long filterEndDate = sharedPreferences.getLong("FILTER_ENDDATE", new Date().getTime());

        if (filterMinPrice != 0) {
            editMinPrice.setText(String.valueOf(filterMinPrice));
        }

        if (filterMaxPrice != 0) {
            editMaxPrice.setText(String.valueOf(filterMaxPrice));
        }

        Calendar baseStartDate = Calendar.getInstance();
        if (filterStartDate != 0) {
            baseStartDate.setTime(new Date(filterStartDate));
            textViewStartDate.setText(formatter.format(baseStartDate.getTime()));
        }

        calendarStartDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    baseStartDate.set(year, month, day);
                    textViewStartDate.setText(formatter.format(baseStartDate.getTime()));
                }
            }, baseStartDate.get(Calendar.YEAR), baseStartDate.get(Calendar.MONTH), baseStartDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        Calendar baseEndDate = Calendar.getInstance();
        if (filterEndDate != 0) {
            baseEndDate.setTime(new Date(filterEndDate));
            textViewEndDate.setText(formatter.format(baseEndDate.getTime()));
        }

        calendarEndDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    baseEndDate.set(year, month, day);
                    textViewEndDate.setText(formatter.format(baseEndDate.getTime()));
                }
            }, baseEndDate.get(Calendar.YEAR), baseEndDate.get(Calendar.MONTH), baseEndDate.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnSearch.setOnClickListener(view -> {
            int minPrice = 0;
            int maxPrice = 0;

            if (editMinPrice.getText().length() > 0) {
                minPrice = Integer.valueOf(editMinPrice.getText().toString());
            }

            if (editMaxPrice.getText().length() > 0) {
                maxPrice = Integer.valueOf(editMaxPrice.getText().toString());
            }

            boolean isValidPriceRange = (minPrice <= maxPrice) || (maxPrice == 0);
            boolean isValidDateRange = (filter.getStarDate() <= filter.getEndDate())
                    || (filter.getStarDate() == 0 && filter.getEndDate() > 0)
                    || (filter.getStarDate() > 0 && filter.getEndDate() == 0);

            if (isValidPriceRange && isValidDateRange) {
                filter.setMinPrice(minPrice);
                filter.setMaxPrice(maxPrice);
                filter.setStarDate(baseStartDate.getTime().getTime());
                filter.setEndDate(baseEndDate.getTime().getTime());

                Intent intent = new Intent();
                intent.putExtra("SEARCH_FILTER", filter);

                sharedPreferences = getSharedPreferences("SP_FILTER", MODE_PRIVATE);
                sharedPreferences.edit().putInt("FILTER_MIN_PRICE", filter.getMinPrice()).apply();
                sharedPreferences.edit().putInt("FILTER_MAX_PRICE", filter.getMaxPrice()).apply();
                sharedPreferences.edit().putLong("FILTER_STARTDATE", filter.getStarDate()).apply();
                sharedPreferences.edit().putLong("FILTER_ENDDATE", filter.getEndDate()).apply();

                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(FilterActivity.this, "Filter not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}