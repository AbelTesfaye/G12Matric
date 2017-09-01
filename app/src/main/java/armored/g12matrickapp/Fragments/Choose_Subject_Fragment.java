package armored.g12matrickapp.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.Activities.subject_Choose;
import armored.g12matrickapp.Adapters.GridCatagoryAdapter;
import armored.g12matrickapp.Adapters.GridCatagoryStructure;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Widgets.ExpandableGridView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Falcon on 7/20/2017 :: 01:35 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Choose_Subject_Fragment extends Fragment {

    ExpandableGridView catGrid;

    MaterialProgressBar mpbar;
    LinearLayout progressLayout;

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


    public Choose_Subject_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_catagory, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        catGrid = (ExpandableGridView) v.findViewById(R.id.mainGrid);
        catGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sub = subs[i];
                Bundle ex = new Bundle();
                ex.putString("Subject" , sub);

                Fragment fragment = new Choose_Year_Fragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragment, "TAG_YEARS");
                fragment.setArguments(ex);
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        ((subject_Choose) getActivity()).hide_container_two();

        ((subject_Choose) getActivity()).SetTitleOfDetailFromFragment("Subjects");

        AppCompatImageView quiz = (AppCompatImageView) v.findViewById(R.id.appCompatImageView);
        AppCompatImageView challenge = (AppCompatImageView) v.findViewById(R.id.challengeImageView);
        AppCompatImageView fb = (AppCompatImageView) v.findViewById(R.id.appCompatImageView2);

        progressLayout = (LinearLayout) v.findViewById(R.id.progresslayout);
        mpbar = (MaterialProgressBar) v.findViewById(R.id.indeterminate_progress_library);

        Glide.with(this)
                .load(R.drawable.quiz)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(quiz);

        Glide.with(this)
                .load(R.drawable.challenge)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(challenge);

        Glide.with(this)
                .load(R.drawable.face_cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fb);

        if (mpbar.getIndeterminateDrawable() != null) {
            mpbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

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
            catGrid.setAdapter(new GridCatagoryAdapter(getActivity() , R.layout.grid_item , dataStructureList));
            catGrid.setExpanded(true);
            showProgress(false);
        }

    }

}
