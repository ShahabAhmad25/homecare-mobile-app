package com.example.homecare;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find Search Bar
        EditText etSearch = view.findViewById(R.id.et_search);
        if (etSearch != null) {
            etSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = etSearch.getText().toString().trim();
                    if (!query.isEmpty()) {
                        hideKeyboard(v);
                        openServices(query);
                    }
                    return true;
                }
                return false;
            });
        }

        // Find Banner "Book Now" button
        MaterialButton btnBannerBook = view.findViewById(R.id.btn_banner_book);
        if (btnBannerBook != null) {
            btnBannerBook.setOnClickListener(v -> {
                Log.d(TAG, "Banner 'Book Now' clicked");
                Fragment detailFragment = ServiceDetailFragment.newInstance("Laptop Repair", "Rs. 1600", "4.9", "", 5, "Gadgets");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }

        // Set listeners for each category
        setCategoryListener(view, R.id.cat_shifting, "Shifting");
        setCategoryListener(view, R.id.cat_plumbing, "Plumbing");
        setCategoryListener(view, R.id.cat_painting, "Painting");
        setCategoryListener(view, R.id.cat_cleaning, "Cleaning");
        setCategoryListener(view, R.id.cat_gadgets, "Gadgets");
        setCategoryListener(view, R.id.cat_electrician, "Electrician");
        setCategoryListener(view, R.id.cat_car_service, "Car Service");
        setCategoryListener(view, R.id.cat_gardening, "Gardening");

        return view;
    }

    private void setCategoryListener(View rootView, int id, String categoryName) {
        LinearLayout layout = rootView.findViewById(id);
        if (layout != null) {
            layout.setOnClickListener(v -> {
                Log.d(TAG, "Category clicked: " + categoryName);
                openServices(categoryName);
            });
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openServices(String category) {
        Log.d(TAG, "Opening services list for: " + category);
        Fragment servicesFragment = ServicesListFragment.newInstance(category);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, servicesFragment)
                .addToBackStack(null)
                .commit();
    }
}
