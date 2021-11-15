package com.example.assignment2;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment2.databinding.FragmentMainBinding;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    DatabaseHelper mDatabaseHelper;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mDatabaseHelper = new DatabaseHelper(requireContext());
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.latitude.setVisibility(View.INVISIBLE);
        binding.longitude.setVisibility(View.INVISIBLE);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.findInputBar.getText().toString();
                if(!address.isEmpty()){
                    ArrayList<Double> coordinates = getLocationFromAddress(address);
                    if(coordinates.size()>0){
                        long success = mDatabaseHelper.addData(address, Double.toString(coordinates.get(0)), Double.toString(coordinates.get(1)));
                        if (success > 0){
                            Toast.makeText(requireContext() , "Added Successful", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(requireContext() , "No data with given address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(requireContext() , "Enter address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.findInputBar.getText().toString();
                ArrayList<String> coordinates = getCoordinatesFromDatabase(address);
                if(coordinates.size() > 0){
                    binding.latitude.setText(coordinates.get(0));
                    binding.longitude.setText(coordinates.get(1));
                    binding.latitude.setVisibility(View.VISIBLE);
                    binding.longitude.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(requireContext() , "No data with given address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.findInputBar.getText().toString();
                if(!address.isEmpty()) {
                    ArrayList<Double> coordinates = getLocationFromAddress(address);
                    if (coordinates.size() > 0) {
                        int success = mDatabaseHelper.update(address, String.format("%.2f", coordinates.get(0)), String.format("%.2f", coordinates.get(1)));
                        if (success > 0) {
                            Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "No address: " + address + " in the database", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "No data with given address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(requireContext(), "Enter address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.findInputBar.getText().toString();
                int success = mDatabaseHelper.deleteGivenAddress(address);
                if (success > 0){
                    Toast.makeText(requireContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireContext(), "No data with given address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.enterCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Retrieve the coordinates from the database given the address
    public ArrayList<String> getCoordinatesFromDatabase(String address){
        Cursor data = mDatabaseHelper.getCoordinates(address);
        ArrayList<String> coords = new ArrayList<>();
        if(data.moveToNext()){
            coords.add(data.getString(2));
            coords.add(data.getString(3));
        }
        return coords;
    }


    // Retrieve coordinates using geocoding given the address
    public ArrayList<Double> getLocationFromAddress(String address){

        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            ArrayList<Double> coords = new ArrayList<>();
            try {
                List<Address> ls= geocoder.getFromLocationName(address,1);
                for (Address addr: ls) {
                    coords.add(addr.getLatitude());
                    coords.add(addr.getLongitude());
                }
                return coords;
            } catch (IOException e) {
                e.printStackTrace();
            }}
        return null;
    }
}