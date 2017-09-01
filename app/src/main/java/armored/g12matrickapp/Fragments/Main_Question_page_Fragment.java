package armored.g12matrickapp.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import armored.g12matrickapp.Activities.subject_Choose;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Utils.DbManager;
import armored.g12matrickapp.Widgets.CustomWebView;
import armored.g12matrickapp.Widgets.NestedWebView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Falcon on 7/20/2017 :: 01:35 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class Main_Question_page_Fragment extends Fragment {

    NestedWebView mainWebViewSheets;
    SwipeRefreshLayout sheetsSwipeRefresh;
    int Subject = -1;
    int Year = -1;
    ArrayList<Integer> Unit;
    ArrayList<String> Contents;
    ArrayList<String> Answers;
    ArrayList<Integer> PreviouslyWorked;
    String theBigData = "";
    CountDownTimer mainTimer;
    int didfinsh = 0;
    boolean amoutdontshow = false;
    boolean isPaused = false;
    MaterialProgressBar mpbar;
    LinearLayout progressLayout;
    LinearLayout errorLayout;


    public Main_Question_page_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_question_page, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mainWebViewSheets = (NestedWebView) v.findViewById(R.id.sheetswebview);

        ((subject_Choose) getActivity()).hide_second_toolbar();
        ((subject_Choose) getActivity()).deselectNavMenu();

        sheetsSwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.sheetsSwipeRefresh);
        progressLayout = (LinearLayout) v.findViewById(R.id.progresslayout);
        mpbar = (MaterialProgressBar) v.findViewById(R.id.indeterminate_progress_library);
        errorLayout = (LinearLayout) v.findViewById(R.id.ErrorSheet);

        sheetsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertDialog.Builder sure = new AlertDialog.Builder(getActivity());
                String html = "<p>Are you sure you want to refresh this exam.<br><spam style=\"color:#ff0000\">*Your current progress will be lost.</spam></p>";
                sure.setTitle("Are you sure?").setMessage(Html.fromHtml(html));
                sure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgress(true);
                        stopTimer();
                        mainWebViewSheets.reload();
                        startTimer();
                        sheetsSwipeRefresh.setRefreshing(false);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing
                        sheetsSwipeRefresh.setRefreshing(false);
                    }
                }).show();
            }
        });

        if (mpbar.getIndeterminateDrawable() != null) {
            mpbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        showProgress(true);

        buildSheetsUI builder = new buildSheetsUI();
        builder.execute(new String[]{"http://something.com/klr.php"});

        return v;

    }

    private void handleFilterSheet(){
        if(Subject != -1 && Year != -1){

        } else{
            Toast.makeText(getActivity(), "Internal Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try{
            //Debug purpose
            //Subject = 6;
            //Year = 2005;
            Bundle bundle = this.getArguments();
            Subject = bundle.getInt("Subject" , 6);
            Year = bundle.getInt("Year" , 2005);
        } catch (Exception e){
            Subject = -1;
            Year = -1;
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressLayout.setVisibility(show ? View.VISIBLE : View.GONE);
            mainWebViewSheets.setVisibility(!show ? View.VISIBLE : View.GONE);
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
            mainWebViewSheets.setVisibility(!show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void startTimer(){

        int times = 1;
        times = Constants.getTimerDataForSubject(Subject);

        mainTimer = new CountDownTimer(30 * 1000 * 60 * times, 1000) { //remove * 60 for debug
            @Override
            public void onTick(long l) {
                try{((subject_Choose) getActivity()).SetSubTitleFromFragment(milliSecondsToTimer(l));}catch (Exception e){e.printStackTrace();}
            }

            private void ad() {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Time's up");
                b.setMessage("Sorry, You did not finish in time. Please try again to improve your mark");
                b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(getActivity(), subject_Choose.class);
                        startActivity(a);
                    }
                });
                b.show();
            }

            @Override
            public void onFinish() {
                if (didfinsh != 0) {
                    ((subject_Choose) getActivity()).SetTitleOfDetailFromFragment(Constants.getSubjectName(Subject) + " " + Year);
                    try{((subject_Choose) getActivity()).SetSubTitleFromFragment("");}catch (Exception e){e.printStackTrace();}
                } else {
                    if (!amoutdontshow) {
                        if (!isPaused) {
                            try {
                                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                ad();
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                        } else {
                            try {
                                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                Toast.makeText( getActivity() , "Your exam time has ended.", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(getActivity() , subject_Choose.class);
                                startActivity(a);
                            } catch (Exception d) {
                                d.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }

    private void stopTimer(){
        if (mainTimer != null) {
            didfinsh = 1;
            mainTimer.cancel();
            mainTimer.onFinish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private class buildSheetsUI extends AsyncTask<String , Void , String>{

        @Override
        protected String doInBackground(String... strings) {
            DbManager db = new DbManager(getActivity());
            Unit = db.getIntArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_UNIT , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year , DbManager.COLUMN_QUESTION_NUM + " ASC");
            Contents = db.getStringArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_CONTENT , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year , DbManager.COLUMN_QUESTION_NUM + " ASC");
            Answers = db.getStringArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ANS , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year , DbManager.COLUMN_QUESTION_NUM + " ASC");

            //Should build header before going to the array
            InputStream input;
            try {

                theBigData += "<html><head><title>" + Constants.getSubjectName(Subject) + " " + Year + "</title>";
                String js1 = "function evaluateandgive(){document.getElementById('EvaluateBtn').disabled=true;var ChoosedChoices = \"X\";var allInputs = document.getElementsByTagName('input');var allqnum = (allInputs.length - 1) / 4;for(var i = 1; i <= allqnum; i++){var choicesUnderQuestion = document.getElementsByName('choiceq' + i.toString());if(choicesUnderQuestion[0].checked){ChoosedChoices = ChoosedChoices + \"A\";} else if(choicesUnderQuestion[1].checked){ChoosedChoices = ChoosedChoices + \"B\";} else if(choicesUnderQuestion[2].checked){ChoosedChoices = ChoosedChoices + \"C\";} else if(choicesUnderQuestion[3].checked){ChoosedChoices = ChoosedChoices + \"D\";} else{ChoosedChoices = ChoosedChoices + \"X\";}}window.location.href = \"answers://\" + ChoosedChoices;}var answersArray = \"\";var RedColor=\"#ed1c24\";var GreenColor=\"#3bff2a\";var GrayColor =\"#788990\";var answered = 0;var xanswers = 0;var worked = 0;function setAnswersArray(array){answersArray = array;}function evaluateforown(sub,year){document.getElementById('EvaluateBtn').disabled=true;var allInputs = document.getElementsByTagName('input');var allqnum = (allInputs.length - 1) / 4;for(var i = 1; i <= allqnum; i++){var choicesUnderQuestion = document.getElementsByName('choiceq' + i.toString());var ChoosedChoices = \"\";if(choicesUnderQuestion[0].checked){ChoosedChoices = ChoosedChoices + \"A\";}else if(choicesUnderQuestion[1].checked){ChoosedChoices = ChoosedChoices + \"B\";}else if(choicesUnderQuestion[2].checked){ChoosedChoices = ChoosedChoices + \"C\";}else if(choicesUnderQuestion[3].checked){ChoosedChoices = ChoosedChoices + \"D\";}else{ChoosedChoices = ChoosedChoices + \"X\";worked++;}var choiceABackground = document.getElementById(sub+year+i.toString()+'a');var choiceBBackground = document.getElementById(sub+year+i.toString()+'b');var choiceCBackground = document.getElementById(sub+year+i.toString()+'c');var choiceDBackground = document.getElementById(sub+year+i.toString()+'d');if(ChoosedChoices != answersArray[i] && answersArray[i] != \"X\"){switch(ChoosedChoices){case \"A\":choiceABackground.style.backgroundColor=RedColor;break;case \"B\":choiceBBackground.style.backgroundColor=RedColor;break;case \"C\":choiceCBackground.style.backgroundColor=RedColor;break;case \"D\":choiceDBackground.style.backgroundColor=RedColor;break;case \"X\":choiceABackground.style.backgroundColor=RedColor;choiceBBackground.style.backgroundColor=RedColor;choiceCBackground.style.backgroundColor=RedColor;choiceDBackground.style.backgroundColor=RedColor;break;}}else if(answersArray[i] == \"X\"){choiceABackground.style.backgroundColor=GrayColor;choiceBBackground.style.backgroundColor=GrayColor;choiceCBackground.style.backgroundColor=GrayColor;choiceDBackground.style.backgroundColor=GrayColor;xanswers++;} else if(ChoosedChoices == answersArray[i] && answersArray[i] != \"X\"){answered++;switch(ChoosedChoices){case \"A\":choiceABackground.style.backgroundColor=GreenColor;break;case \"B\":choiceBBackground.style.backgroundColor=GreenColor;break;case \"C\":choiceCBackground.style.backgroundColor=GreenColor;break;case \"D\":choiceDBackground.style.backgroundColor=GreenColor;break;case \"X\":choiceABackground.style.backgroundColor=GreenColor;choiceBBackground.style.backgroundColor=GreenColor;choiceCBackground.style.backgroundColor=GreenColor;choiceDBackground.style.backgroundColor=GreenColor;break;}}}worked = allqnum - worked;allqnum = allqnum - xanswers;presentMeData(allqnum , answered , worked);}function presentMeData(all , answered , worked){window.location.href = \"popupg12://\" + all + \";\" + answered + \";\" + worked;};function changeStyle(mode){switch (mode){case 0:document.getElementsByTagName('body')[0].style.backgroundColor = \"#222\";document.getElementsByTagName('body')[0].style.color = \"#eee\";break;case 1:document.getElementsByTagName('body')[0].style.backgroundColor = \"#fae9ab\";document.getElementsByTagName('body')[0].style.color = \"#000\";break;case 2:document.getElementsByTagName('body')[0].style.backgroundColor = \"#0E535B\";document.getElementsByTagName('body')[0].style.color = \"#fff\";document.getElementsByTagName('body')[0].style.textAlign = \"center\";break;}}";
                theBigData += "<script>" + js1 + "</script></head><body>";

                for (int i = 0; i < Contents.size(); i++) {
                    theBigData += Contents.get(i) + "\n";
                }

                //Debug purpose
                /*
                Log.e("XXXX" , Integer.toString(Contents.size()));
                Log.e("XXXX" , Contents.get(0));
                Log.e("XXXX" , Integer.toString(Answers.size()));

                Log.e("XXXX" , Integer.toString(Unit.size()));
                Log.e("XXXX" , Integer.toString(Unit.get(0)));
                */

                Log.e("XXXX" , Answers.get(0));

                //after the array then comes evaluate button and js codes
                theBigData += "<center><input type=Button id=EvaluateBtn value=Evaluate onclick=evaluateforown(\"" + Constants.getSubjectNameForEvaluation(Subject) + "\",\"" + Year + "\") ></center></body></html>";
                //Log.e("XXXX" , theBigData);
            } catch (Exception e){
                e.printStackTrace();
            }
            return theBigData;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(String s) {
            try{
                mainWebViewSheets.getSettings().setJavaScriptEnabled(true);
                mainWebViewSheets.getSettings().setBuiltInZoomControls(true);
                mainWebViewSheets.getSettings().setDisplayZoomControls(false);
                mainWebViewSheets.getSettings().setAppCacheEnabled(true);
                mainWebViewSheets.getSettings().setAllowFileAccess(true);
                mainWebViewSheets.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                mainWebViewSheets.getSettings().setSupportZoom(true);
                mainWebViewSheets.getSettings().setUseWideViewPort(true);

                mainWebViewSheets.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        try{
                            showProgress(false);
                            startTimer();
                            String ansString = "";
                            for(String s : Answers){
                                ansString += s;
                            }
                            mainWebViewSheets.loadUrl("javascript:setAnswersArray('" + ansString + "')");
                        }catch (Exception e){e.printStackTrace();}
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //return super.shouldOverrideUrlLoading(view, url);
                        if(url.contains("answers://")){
                            Log.e("XXXX" , url);
                            parseAnswers(url);
                            stopTimer();
                        } else if(url.contains("popupg12://")) {
                            Log.e("XXXX" , url);
                            makeAlertDialogFromJs(url);
                            stopTimer();
                        }
                        else {
                            Log.e("XXXX" , url);
                            //view.loadUrl(url);
                        }
                        return true;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if(url.contains("answers://")){
                           // Snackbar.make(view , url , Snackbar.LENGTH_SHORT).show();
                        }else {
                            super.onPageStarted(view, url, favicon);
                        }
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                    }
                });

                mainWebViewSheets.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        try{ ((subject_Choose) getActivity()).SetTitleFromFragment(title); }catch (Exception e){e.printStackTrace();}
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                    }
                });


                if(Subject == -1 || Year == -1){
                    //Error occured take them back to the home page
                    errorLayout.setVisibility(View.VISIBLE);
                }else{
                    if(!s.equals("null")){
                        mainWebViewSheets.loadData(s , "text/html" , "UTF-8");
                        mainWebViewSheets.getSettings().setDomStorageEnabled(true);
                    }
                }

            } catch (Exception e){
                Log.e("XXXX" , e.getStackTrace().toString());
            }
        }
    }


    private void makeAlertDialogFromJs(String data){
        String[] a = {};
        data = data.replace("popupg12://" , "");
        data = data.replace("/" , "");
        a = data.split(";");
        Log.e("XXXX" , Arrays.toString(a));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Your Result");
        String html = "<div><h3>" + a[1] + " / " + a[2] + "</h3><br><h2>Overall</h2><br><h3>" + a[1] + " / " + a[0] + "</h3></div>";
        builder.setMessage(Html.fromHtml(html));
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Just exit
            }
        });
        builder.show();
        mainWebViewSheets.loadUrl("javascript:evaluateandgive()");
    }

    private class parseTheAnswer extends AsyncTask<String , Void , String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                DbManager db = new DbManager(getActivity());
                String data = strings[0];
                data = data.replace("answers://" , "");
                data = data.replace("/" , "");
                int numberofquestions = data.length();
                Log.e("XXXX" , "NOQ = " + Integer.toString(numberofquestions));
                Log.e("XXXX" , "AS = " +Integer.toString(Answers.size()));
                if(numberofquestions == Answers.size()){
                    for(int i = 0;i < numberofquestions;i++) {
                        int unit = Unit.get(i);
                        String choice = String.valueOf(data.charAt(i));

                        int question_tested = db.getSingleInt(DbManager.TABLE_NAME_TopicWise, DbManager.COLUMN_QUESTION_TESTED, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit);
                        int question_answered = db.getSingleInt(DbManager.TABLE_NAME_TopicWise, DbManager.COLUMN_QUESTION_ANSWERED, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit);
                        int attempted_before = db.getSingleInt(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ATTEMPTED_BEFORE , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                        int answered_before = db.getSingleInt(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ANSWERED_BEFORE , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                        if (!choice.equals("X")) {
                            //the user have choosed something!
                            if (question_answered != -1 && question_tested != -1) {
                                if (attempted_before == 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put(DbManager.COLUMN_ATTEMPTED_BEFORE, 1);
                                    db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                    //Now its attempted

                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

                                        } else {
                                            //wrong answer
                                        }
                                    } else{
                                        //answered so substract only if the user is incorrect
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                        } else {
                                            //wrong answer
                                            question_answered--;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 0);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                    question_tested++;
                                } else {
                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

                                        } else {
                                            //wrong answer
                                        }
                                    } else{
                                        //answered so substract only if the user is incorrect
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                        } else {
                                            //wrong answer
                                            question_answered--;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 0);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                }
                            } else{
                                //no data under the specific unit subject year...
                                ContentValues cv2 = new ContentValues();
                                cv2.put(DbManager.COLUMN_YEAR , Year);
                                cv2.put(DbManager.COLUMN_SUBJECT , Subject);
                                cv2.put(DbManager.COLUMN_UNIT , unit);
                                cv2.put(DbManager.COLUMN_QUESTION_TESTED , 0);
                                cv2.put(DbManager.COLUMN_QUESTION_ANSWERED , 0);
                                db.insertNewData(DbManager.TABLE_NAME_TopicWise , cv2);

                                question_answered = 0;
                                question_tested = 0;

                                if (attempted_before == 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put(DbManager.COLUMN_ATTEMPTED_BEFORE, 1);
                                    db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                    //Now its attempted

                                    question_tested++;
                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

                                        } else {
                                            //wrong answer
                                        }
                                    } else{
                                        //answered so substract only if the user is incorrect
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                        } else {
                                            //wrong answer
                                            question_answered--;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 0);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                } else {
                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

                                        } else {
                                            //wrong answer
                                        }
                                    } else{
                                        //answered so substract only if the user is incorrect
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                        } else {
                                            //wrong answer
                                            question_answered--;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 0);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                }

                            }
                        }

                        ContentValues cvmain = new ContentValues();
                        cvmain.put(DbManager.COLUMN_YEAR , Year);
                        cvmain.put(DbManager.COLUMN_SUBJECT , Subject);
                        cvmain.put(DbManager.COLUMN_UNIT , unit);
                        cvmain.put(DbManager.COLUMN_QUESTION_TESTED , question_tested);
                        cvmain.put(DbManager.COLUMN_QUESTION_ANSWERED , question_answered);

                        db.updateDataInsideDb(DbManager.TABLE_NAME_TopicWise , cvmain , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Year + " AND " + DbManager.COLUMN_UNIT + " = " + unit);

                    }
                    return "K";

                }else{
                    //Error
                    //mismatch of number of answers and the data that was given
                    return "K";
                }
            } catch (Exception e){
                e.printStackTrace();
                return "K";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    private int parseAnswers(String data){
        parseTheAnswer ans = new parseTheAnswer();
        ans.execute(new String[]{data});
        return 0;
    }



}
