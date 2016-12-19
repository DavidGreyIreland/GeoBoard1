package com.example.project.geoboard1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by david on 19/12/2016.
 */

public class MapOptionsFragment extends Fragment
{
    Button buttonCreateGeoBoard;
    Button buttonViewGeoBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.map_options_fragment, container, false);

        buttonCreateGeoBoard = (Button)view.findViewById(R.id.buttonCreateBoard);
        buttonViewGeoBoard = (Button)view.findViewById(R.id.buttonViewGeoBoards);

        buttonCreateGeoBoard.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        Toast.makeText(getActivity(), "create new Geo-Board", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        buttonViewGeoBoard.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        Toast.makeText(getActivity(), "view Geo-Boards", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return view;
    }
}
