package com.fit2081.myfirstproject.provider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.myfirstproject.BookAdapter;
import com.fit2081.myfirstproject.MainActivity;
import com.fit2081.myfirstproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecycleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class   RecycleViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BookAdapter adapter1 ;

    BookViewModel bookViewModel;

    ViewModelProvider viewModelProvider;
    public RecycleViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecycleViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecycleViewFragment newInstance(String param1, String param2) {
        RecycleViewFragment fragment = new RecycleViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        adapter1 = new BookAdapter();
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.getMyBook().observe(this, newData ->{
                    adapter1.setDp(newData);
                    adapter1.notifyDataSetChanged();
                }
        );
        Log.d("week6App","2");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        adapter1 = new BookAdapter();
        View view = inflater.inflate(R.layout.fragment_recycle_view,container,false);
        recyclerView = view.findViewById(R.id.recycle_view2);
        layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter1);
        Log.d("week6App","1");

        return view;
    }
}