package armored.g12matrickapp.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.Activities.WebViewActivity;
import armored.g12matrickapp.Activities.subject_Choose;
import armored.g12matrickapp.Adapters.YearsGridAdapter;
import armored.g12matrickapp.Adapters.YearsGridStructure;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Widgets.ExpandableGridView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

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
            showProgress(false);
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


    MaterialProgressBar mpbar;
    LinearLayout progressLayout;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            catGrid.setVisibility(!show ? View.VISIBLE : View.GONE);
            mpbar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            progressLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            catGrid.setVisibility(!show ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.choose_year, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        catGrid = (ExpandableGridView) v.findViewById(R.id.yearsGrid);

        progressLayout = (LinearLayout) v.findViewById(R.id.progresslayout);
        mpbar = (MaterialProgressBar) v.findViewById(R.id.indeterminate_progress_library);

        catGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle ex = new Bundle();
                ex.putInt("Subject" , Constants.getCodeFromString(subject));
                ex.putInt("Year" , Integer.parseInt(y[i]));

                ((subject_Choose) getActivity()).deselectNavMenu();

                Intent goToWebView = new Intent(getActivity() , WebViewActivity.class);
                goToWebView.putExtra("Subject" , Constants.getCodeFromString(subject));
                goToWebView.putExtra("Year" , Integer.parseInt(y[i]));

                startActivity(goToWebView);
                getActivity().overridePendingTransition(0,0);
            }
        });

        if (mpbar.getIndeterminateDrawable() != null) {
            mpbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

        showProgress(true);

        PopulateYears gridPop = new PopulateYears();
        gridPop.execute();

        return v;

    }

}
