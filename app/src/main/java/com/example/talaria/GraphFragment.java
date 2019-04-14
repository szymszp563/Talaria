package com.example.talaria;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    private RunFragment.OnFragmentInteractionListener mListener;
    LineGraphSeries<DataPoint> series;
    GraphView graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_layout, container, false);
        double y,x;
        x = .5;

        String filename = "/results";
        File directory = getContext().getFilesDir();
        String path = directory.getPath() + filename;
        //File file = new File(directory, filename);
        String line = null;
        List<Double> values = new ArrayList<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(path);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                values.add(Double.parseDouble(line));
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            filename + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + filename + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        catch(Exception ex) {
            System.out.println(
                    ex.getMessage());
            // Or we could just do this:
            // ex.printStackTrace();
        }

        //Toast.makeText(view.getContext(), values.size(), Toast.LENGTH_SHORT).show();

        graph = view.findViewById(R.id.graph1);
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0, 0), true, 100);
        for(int i =0; i<values.size(); i++) {
            series.appendData(new DataPoint(i + 1, values.get(i)), true, 100);
        }
        graph.addSeries(series);

        LinearLayout layout = view.findViewById(R.id.linear);

        if(graph.getParent() != null) {
            ((ViewGroup)graph.getParent()).removeView(graph); // <- fix
        }
        layout.addView(graph); //  <==========  ERROR IN THIS LINE DURING 2ND RUN

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RunFragment.OnFragmentInteractionListener) {
            mListener = (RunFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
