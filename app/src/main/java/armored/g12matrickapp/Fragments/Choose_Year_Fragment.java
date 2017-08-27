package armored.g12matrickapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.Activities.subject_Choose;
import armored.g12matrickapp.Adapters.YearsGridAdapter;
import armored.g12matrickapp.Adapters.YearsGridStructure;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Widgets.ExpandableGridView;

/**
 * Created by Falcon on 5/28/2010 :: 18:22 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Choose_Year_Fragment extends Fragment {

    ExpandableGridView catGrid;
    String subject;
    List<YearsGridStructure> dataStructureList = new ArrayList<YearsGridStructure>();
    boolean goForIt = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        goForIt = true;
        try{if(bundle != null){
            subject = bundle.getString("Subject" , "Maths");
        }} catch (Exception e){
            e.printStackTrace();
        }
        ((subject_Choose) getActivity()).SetTitleOfDetailFromFragment(subject);
    }

    public Choose_Year_Fragment() {
    }

    String[] y = {
            "2005" ,
            "2006" ,
            "2007" ,
            "2008" , "2009"
    };

    private class PopulateYears extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            catGrid.setAdapter(new YearsGridAdapter(getActivity() , R.layout.grid_item , dataStructureList));
            catGrid.setExpanded(true);
            ((subject_Choose) getActivity()).actionOnBottomMenu(Constants.HIDE_BOTTOM_MENU);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            do{
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(!goForIt);
           // ((subject_Choose) getActivity()).SetTitleOfDetailFromFragment(subject);

            double[] a = {
                    0.26 ,
                    0.55 ,
                    0.77 ,
                    0.19 , 0.89
            };

            for(int i = 0; i< a.length; i++){
                YearsGridStructure yi = new YearsGridStructure(y[i] , a[i]);
                dataStructureList.add(yi);
            }

            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.choose_year, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        catGrid = (ExpandableGridView) v.findViewById(R.id.yearsGrid);
        catGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle ex = new Bundle();
                ex.putInt("Subject" , Constants.getCodeFromString(subject));
                ex.putInt("Year" , Integer.parseInt(y[i]));

                ((subject_Choose) getActivity()).hide_container_one();
                ((subject_Choose) getActivity()).deselectNavMenu();
                ((subject_Choose) getActivity()).show_menu_fab(false);

                Fragment fragment = new Main_Question_page_Fragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment2, fragment, "TAG_QUESTION_PAPER");
                fragment.setArguments(ex);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        PopulateYears gridPop = new PopulateYears();
        gridPop.execute();

        return v;

    }

}
