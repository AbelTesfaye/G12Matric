package armored.g12matrickapp.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.Activities.QuizziWebActivity;
import armored.g12matrickapp.Activities.Subject_Choose;
import armored.g12matrickapp.Adapters.ChapterListAdapter;
import armored.g12matrickapp.Adapters.ChapterListStructure;
import armored.g12matrickapp.Adapters.GridCatagoryAdapter;
import armored.g12matrickapp.Adapters.GridCatagoryStructure;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Widgets.ExpandableGridView;
import armored.g12matrickapp.Widgets.ExpandableListView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.View.GONE;

/**
 * Created by Falcon on 7/20/2017 :: 01:35 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Quizzi_Fragment extends Fragment {

    ExpandableGridView catGrid;

    MaterialProgressBar mpbar;
    LinearLayout progressLayout;

    int AmCurrently = 0;

    List<GridCatagoryStructure> dataStructureList = new ArrayList<GridCatagoryStructure>();
    String[] subs = {
            "Mathematics",
            "English",
            "Physics",
            "Chemistry",
            "History",
            "Biology",
            "Geography",
            "Economics",
            "Civics",
            "SAT"
    };


    public Quizzi_Fragment() {
    }

    ImageButton back_btn;
    TextView action_text;
    CardView firstChoiceCard;
    CardView secondChoiceCard;
    AppCompatButton thegobtn;

    String subject_choice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_quizzi, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        ((Subject_Choose) getActivity()).hide_container_two();

        catGrid = (ExpandableGridView) v.findViewById(R.id.mainGrid);
        catGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(AmCurrently == 0) {
                   subject_choice = subs[i];
                    int Subject = Constants.getCodeFromString(subject_choice);

                   if(Subject != Constants.ENGLISH && Subject != Constants.CIVICS && Subject != Constants.SAT)
                   {
                       //Choose chapters now!
                       PopulateChapters chapters = new PopulateChapters();
                       chapters.execute();
                   }

               } else if(AmCurrently == 1){

               }

            }
        });

        back_btn = (ImageButton) v.findViewById(R.id.back_btn_on_quizzi);
        action_text = (TextView) v.findViewById(R.id.current_action_on_quizzi);

        g11list = (ExpandableListView) v.findViewById(R.id.g11listview);
        g12list = (ExpandableListView) v.findViewById(R.id.g12listview);

        thegobtn = (AppCompatButton) v.findViewById(R.id.goDoTheJob);

        firstChoiceCard = (CardView) v.findViewById(R.id.firstChoiceCard);
        secondChoiceCard = (CardView) v.findViewById(R.id.secondChoiceCard);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                PopulateGridSubjects pop = new PopulateGridSubjects();
                pop.execute();
            }
        });

        progressLayout = (LinearLayout) v.findViewById(R.id.progresslayout);
        mpbar = (MaterialProgressBar) v.findViewById(R.id.indeterminate_progress_library);

        if (mpbar.getIndeterminateDrawable() != null) {
            mpbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

        thegobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter != null && adapter2 != null) {
                    Grade11Units = adapter.giveMeChoosedChapters();
                    Grade12Units = adapter2.giveMeChoosedChapters();

                    //Now that we get the chapters , lets pass everything to quizziWebViewActivity and let that do the job
                    Intent webView = new Intent(getActivity() , QuizziWebActivity.class);
                    webView.putExtra("Subject" , Constants.getCodeFromString(subject_choice));
                    webView.putExtra("G11Chapters" , Grade11Units);
                    webView.putExtra("G12Chapters" , Grade12Units);
                    ((Subject_Choose) getActivity()).setAmOnChaptersPart(false);
                    startActivity(webView);
                }
            }
        });

        showProgress(true);

        PopulateGridSubjects pop = new PopulateGridSubjects();
        pop.execute();

        return v;

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressLayout.setVisibility(show ? View.VISIBLE : GONE);
            catGrid.setVisibility(!show ? View.VISIBLE : GONE);
            mpbar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressLayout.setVisibility(show ? View.VISIBLE : GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressLayout.setVisibility(show ? View.VISIBLE : GONE);
            progressLayout.setVisibility(show ? GONE : View.VISIBLE);
            catGrid.setVisibility(!show ? View.VISIBLE : GONE);
        }
    }

    private class PopulateGridSubjects extends AsyncTask<Void , Void , Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            int[] resources = {
                    R.drawable.ic_maths,
                    R.drawable.ic_eng,
                    R.drawable.ic_phy,
                    R.drawable.ic_chem,
                    R.drawable.ic_hist,
                    R.drawable.ic_bio,
                    R.drawable.ic_geo,
                    R.drawable.ic_eco,
                    R.drawable.ic_civic,
                    R.drawable.ic_aptitude
            };

            int[] colors = {
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll,
                    R.color.colorAll
            };

            for(int i = 0; i < colors.length;i++){
                GridCatagoryStructure grid = new GridCatagoryStructure(subs[i] , resources[i] , colors[i]);
                dataStructureList.add(grid);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            back_btn.setVisibility(GONE);
            action_text.setText(R.string.choose_subject);
            firstChoiceCard.setVisibility(View.VISIBLE);
            secondChoiceCard.setVisibility(GONE);
            catGrid.setAdapter(new GridCatagoryAdapter(getActivity() , R.layout.grid_item , dataStructureList));
            catGrid.setExpanded(true);
            ((Subject_Choose) getActivity()).setAmOnChaptersPart(false);
            AmCurrently = 0;
            showProgress(false);
        }

        @Override
        protected void onPreExecute() {
            dataStructureList.clear();
            thegobtn.setVisibility(GONE);
            super.onPreExecute();
        }
    }

    ExpandableListView g11list;
    ExpandableListView g12list;

    ChapterListAdapter adapter;
    ChapterListAdapter adapter2;

    ArrayList<Integer> Grade11Units;
    ArrayList<Integer> Grade12Units;

    public void performBackButtonClick(){
        back_btn.performClick();
    }

    private class PopulateChapters extends AsyncTask<Void , Void , Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            int unitInG11 = Constants.getHowManyUnitsSubjectHasG11(Constants.getCodeFromString(subject_choice));
            int unitInG12 = Constants.getHowManyUnitsSubjectHasG12(Constants.getCodeFromString(subject_choice));

            ArrayList<ChapterListStructure> g11c = new ArrayList<>();
            ArrayList<ChapterListStructure> g12c = new ArrayList<>();

            for(int i = 1;i <= unitInG11;i++){
                ChapterListStructure x = new ChapterListStructure(i , false);
                g11c.add(x);
            }

            for(int i = 1;i <= unitInG12;i++){
                ChapterListStructure x = new ChapterListStructure(i , false);
                g12c.add(x);
            }

            adapter = new ChapterListAdapter(getActivity() , R.layout.chapters_list_item , g11c);
            adapter2 = new ChapterListAdapter(getActivity() , R.layout.chapters_list_item , g12c);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            g11list.setAdapter(adapter);
            g12list.setAdapter(adapter2);
            g11list.setExpanded(true);
            g12list.setExpanded(true);
            firstChoiceCard.setVisibility(GONE);
            secondChoiceCard.setVisibility(View.VISIBLE);
            back_btn.setVisibility(View.VISIBLE);
            thegobtn.setVisibility(View.VISIBLE);
            ((Subject_Choose) getActivity()).setAmOnChaptersPart(true);
            action_text.setText("Choose Chapters");
            AmCurrently = 1;
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
           try {
               adapter.clear();
               adapter2.clear();
           } catch (NullPointerException n){
               n.printStackTrace();
           }
            super.onPreExecute();
        }
    }


}
