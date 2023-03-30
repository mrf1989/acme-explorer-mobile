package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import java.util.Date;

import us.mis.acmeexplorer.entity.Filter;

public class FilterActivity extends AppCompatActivity {
    private Filter filter = new Filter();
    private TextView textViewStartDate, textViewEndDate;
    private ImageView calendarStartDate, calendarEndDate;
    private EditText editMinPrice, editMaxPrice;
    private Button btnSearch;

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
        LocalDate today = LocalDate.now();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        calendarStartDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Date startDate = new Date(year - 1900, month, day);
                    filter.setStarDate(startDate.getTime());
                    textViewStartDate.setText(formatter.format(startDate));
                }
            }, today.getYear(), today.getMonthValue()-1, today.getDayOfMonth());
            datePickerDialog.show();
        });

        calendarEndDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Date endDate = new Date(year - 1900, month, day);
                    filter.setEndDate(endDate.getTime());
                    textViewEndDate.setText(formatter.format(endDate));
                }
            }, today.getYear(), today.getMonthValue()-1, today.getDayOfMonth());
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

                Intent intent = new Intent();
                intent.putExtra("SEARCH_FILTER", filter);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(FilterActivity.this, "Filter not valid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}