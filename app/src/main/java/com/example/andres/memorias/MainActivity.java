package com.example.andres.memorias;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String CARPETA_APP="Historial memorias";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String SD = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(SD + File.separator + CARPETA_APP);
        if (!file.exists()) {
            file.mkdir();
        }String ruta = SD + File.separator + CARPETA_APP+File.separator+"Historial.txt";
        File f = new File(ruta);
        if(!f.exists()) {

            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        final Activity activity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Escanee el codigo QR para reproducir la memoria deseada");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() == null){
                Log.d("Scanner de memorias","escaneo cancelado");
                Toast.makeText(this,"Escaneo cancelado",Toast.LENGTH_LONG).show();
            }else{
                Log.d("Scanner de memorias","Escaneado");
                Toast.makeText(this,"Escaneado",Toast.LENGTH_LONG).show();

                String[]s1 =result.getContents().split(",");
                if(s1.length==3){

                File file = new File(SD + File.separator + CARPETA_APP);
                if (!file.exists()) {
                    file.mkdir();
                }String ruta = SD + File.separator + CARPETA_APP+File.separator+"Historial.txt";
                File f = new File(ruta);
                if(!f.exists()){
                    try {
                        f.createNewFile();
                        FileWriter fw = new FileWriter(f,true); //the true will append the new data
                        fw.write(result.getContents()+"\n");//appends the string to the file
                        fw.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                    try {
                        if(!isRepetido(result.getContents().toString())){
                        FileWriter fw = new FileWriter(f,true);
                        fw.write(result.getContents()+"\n");//appends the string to the file
                        fw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                Intent intent = new Intent(this,Reproductor.class);
                intent.putExtra("link",s1[0]);
                this.startActivity(intent);
            }
                else{
                    Toast.makeText(this,"El QR escaneado no corresponde con el formato de Sfun",Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return Inicio.newInstance();
                case 1:
                    return new Historial();
            }
            return null;
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Inicio";
                case 1:
                    return "Historial";

            }
            return null;
        }
    }

    public boolean isRepetido(String QR){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(SD + File.separator + CARPETA_APP+File.separator+"Historial.txt"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String linea;
        try {
            while((linea = br.readLine()) != null) {
                if(QR.equals(linea)){
                    return true;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
