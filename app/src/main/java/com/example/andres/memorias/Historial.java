package com.example.andres.memorias;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ResponseCache;
import java.util.ArrayList;

/**
 * Created by Andres on 13/07/2016.
 */
public class Historial  extends Fragment {
    ArrayList<Visitado> v = new ArrayList<>();
    private static final String CARPETA_APP="Historial memorias";
    String SD = Environment.getExternalStorageDirectory().toString();
    FloatingActionButton ref;
    public  Historial (){
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.historialtab, container, false);

        final TextView t = (TextView) rootView.findViewById(R.id.tvhistorial);

        ref = (FloatingActionButton) rootView.findViewById(R.id.refrescar);
        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v=devolverlista();
                if (!v.isEmpty()){

                ListView l = (ListView) rootView.findViewById(R.id.lista);
                Visitadoadapter<Visitado> visitadoadapter = new Visitadoadapter<Visitado>(getActivity(),v);
                l.setAdapter(visitadoadapter);
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), Reproductor.class);
                        intent.putExtra("link",v.get(position).getLink());
                        getActivity().startActivity(intent);

                    }
                });


                    t.setVisibility(View.INVISIBLE);
                    registerForContextMenu(l);
                }

            }
        });
        /*File file = new File(SD + File.separator + CARPETA_APP);
        if (!file.exists()) {
            file.mkdir();
        }
        String ruta = SD + File.separator + CARPETA_APP+File.separator+"Historial.txt";
        File f = new File(ruta);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        v = devolverlista();
        if (!v.isEmpty()){
            t.setVisibility(View.INVISIBLE);
        }*/
        //


        return rootView;
    }


    private ArrayList<Visitado> devolverlista(){
        ArrayList<Visitado> v1 = new ArrayList<>();

        String[] s;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(SD + File.separator + CARPETA_APP + File.separator+"Historial.txt"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String linea;
        try {
            while((linea = br.readLine()) != null) {
                s = linea.split(",");
                v1.add(new Visitado(s[0],s[1],s[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v1;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v1,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v1.getId()==R.id.lista) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(v.get(info.position).getPropietario());
            menu.add("Eliminar");
            menu.add("Compartir");

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        int index = info.position;


        File inputFile = new File(SD + File.separator + CARPETA_APP+File.separator+"Historial.txt");
        File tempFile = new File(SD + File.separator + CARPETA_APP+File.separator+"temporal.txt");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(tempFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String lineToRemove = v.get(index).getLink()+","+v.get(index).getPropietario()+","+v.get(index).getDefuncion();
        String currentLine;

        try {
            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove

                String trimmedLine = currentLine.trim();
                if(trimmedLine.startsWith(lineToRemove)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        v.remove(menuItemIndex);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();


        boolean successful = tempFile.renameTo(inputFile);

        return successful;

    }

    @Override
    public void onResume(){
        super.onResume();
        ref.performClick();
    }
}
