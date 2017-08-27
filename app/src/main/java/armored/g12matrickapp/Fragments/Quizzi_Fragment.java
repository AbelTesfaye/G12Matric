package armored.g12matrickapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import armored.g12matrickapp.Activities.subject_Choose;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;

/**
 * Created by Falcon on 7/20/2017 :: 01:35 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Quizzi_Fragment extends Fragment {


    public Quizzi_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_quizzi, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        ((subject_Choose) getActivity()).show_menu_fab(false);
        ((subject_Choose) getActivity()).actionOnBottomMenu(Constants.HIDE_BOTTOM_MENU);
        ((subject_Choose) getActivity()).hide_container_two();

        return v;

    }

}
