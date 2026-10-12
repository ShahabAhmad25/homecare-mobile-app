package com.example.homecare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class ProviderHomeFragment extends Fragment {

    private BarChart barChart;
    private View statusOnline, statusOffline;
    private TextView tvStatusOnline, tvStatusOffline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_home, container, false);

        barChart = view.findViewById(R.id.weekly_bar_chart);
        tvStatusOnline = view.findViewById(R.id.tv_status_online);
        tvStatusOffline = view.findViewById(R.id.tv_status_offline);

        setupBarChart();
        setupStatusToggle();

        return view;
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 40));
        entries.add(new BarEntry(1, 70));
        entries.add(new BarEntry(2, 50));
        entries.add(new BarEntry(3, 85));
        entries.add(new BarEntry(4, 35));
        entries.add(new BarEntry(5, 100)); // Today
        entries.add(new BarEntry(6, 25));

        BarDataSet dataSet = new BarDataSet(entries, "Weekly Revenue");
        
        // Setup colors: all grey except today (index 5)
        int[] colors = new int[7];
        for (int i = 0; i < 7; i++) {
            if (i == 5) {
                colors[i] = Color.parseColor("#006B5C"); // Emerald Green
            } else {
                colors[i] = Color.parseColor("#E2E8F0"); // Light Grey
            }
        }
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);

        // X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.parseColor("#A0AEC0"));
        xAxis.setTextSize(10f);
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setGranularity(1f);

        // Y-Axis
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        
        barChart.invalidate();
    }

    private void setupStatusToggle() {
        tvStatusOnline.setOnClickListener(v -> {
            tvStatusOnline.setBackgroundResource(R.drawable.bg_status_online);
            tvStatusOnline.setTextColor(Color.WHITE);
            tvStatusOffline.setBackground(null);
            tvStatusOffline.setTextColor(Color.parseColor("#718096"));
        });

        tvStatusOffline.setOnClickListener(v -> {
            tvStatusOffline.setBackgroundResource(R.drawable.bg_status_online); // Reusing online background for the "selected" state
            tvStatusOffline.setTextColor(Color.WHITE);
            tvStatusOnline.setBackground(null);
            tvStatusOnline.setTextColor(Color.parseColor("#718096"));
        });
    }
}