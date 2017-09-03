package armored.g12matrickapp.Activities;

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
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Utils.DbManager;
import armored.g12matrickapp.Utils.Functions;
import armored.g12matrickapp.Widgets.NestedWebView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.View.GONE;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

/**
 * Created by Falcon on 9/3/2017 :: 01:00 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class QuizziWebActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NestedScrollView bottomMenu;
    BottomSheetBehavior behavior;

    NestedWebView mainWebViewSheets;
    int Subject = -1;
    ArrayList<Integer> Unit;
    ArrayList<String> Contents;
    ArrayList<String> Answers;
    ArrayList<Integer> Years;
    ArrayList<Integer> PreviouslyWorked;
    String theBigData = "";
    CountDownTimer mainTimer;
    int didfinsh = 0;
    boolean amoutdontshow = false;
    boolean isPaused = false;

    private View navHeader;
    private AppCompatImageView imgNavProfile , imgNavBackground;
    private TextView txtName , txtWebsite;

    MaterialProgressBar mpbar;
    LinearLayout progressLayout;
    LinearLayout errorLayout;
    LinearLayout search_bar;

    NavigationView mainNavView;

    ArrayList<Integer> Grade11Units;
    ArrayList<Integer> Grade12Units;

    @Override
    protected void onResume() {
        super.onResume();
        View decodedView = getWindow().getDecorView();
        int uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions = SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        decodedView.setSystemUiVisibility(uiOptions);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressLayout.setVisibility(show ? View.VISIBLE : GONE);
            mainWebViewSheets.setVisibility(!show ? View.VISIBLE : GONE);
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
            mainWebViewSheets.setVisibility(!show ? View.VISIBLE : GONE);
        }
    }

    void loadNavHeader(){

        txtName.setText("Brook Mezgebu");
        txtWebsite.setText("Bole Kale Hiwot School");

        try{  Drawable drawable = ContextCompat.getDrawable(this , R.drawable.male);
            Bitmap b = Functions.getHexagonShape(Functions.drawableToBitmap(drawable));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG , 100 , stream);
            byte[] bitmapdata = stream.toByteArray();

            Glide.with(this)
                    .load(bitmapdata)
                    .crossFade()
                    .override(150 , 150)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgNavProfile);

            Glide.with(this)
                    .load(R.drawable.lib_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgNavBackground);

        }
        catch (Exception a){
            a.printStackTrace();
        }
    }

    public void deselectNavMenu(){
        int i = mainNavView.getMenu().size();
        for(int x = 0; x < i; x++){
            mainNavView.getMenu().getItem(x).setChecked(false);
        }
    }

    int seekprogress = 0;

    private void setupMenu(){
        blackOnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainWebViewSheets.loadUrl("javascript:changeStyle(3);");
            }
        });

        darktheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainWebViewSheets.loadUrl("javascript:changeStyle(0);");
            }
        });

        oldtheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainWebViewSheets.loadUrl("javascript:changeStyle(1);");
            }
        });

        darkblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainWebViewSheets.loadUrl("javascript:changeStyle(2);");
            }
        });

        seek.setMax(280);
        if(seekprogress == 0){seek.setProgress(mainWebViewSheets.getSettings().getTextZoom());}else{ seek.setProgress(seekprogress);}
        seekprogress = seek.getProgress();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                mainWebViewSheets.getSettings().setTextZoom(i);
                seekprogress = i;
                seekBar.setProgress(seekprogress);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mainNavView = (NavigationView) findViewById(R.id.nav_view);
        mainNavView.setNavigationItemSelectedListener(this);

        navHeader = mainNavView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.userName);
        txtWebsite = (TextView) navHeader.findViewById(R.id.userSchool);
        imgNavProfile = (AppCompatImageView) navHeader.findViewById(R.id.profilePic);
        imgNavBackground = (AppCompatImageView) navHeader.findViewById(R.id.libBackgroundNav);

        loadNavHeader();

        bottomMenu = (NestedScrollView) findViewById(R.id.paperPageMenu);
        behavior = BottomSheetBehavior.from(bottomMenu);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mainWebViewSheets = (NestedWebView) findViewById(R.id.sheetswebview);
        progressLayout = (LinearLayout) findViewById(R.id.progresslayout);
        mpbar = (MaterialProgressBar) findViewById(R.id.indeterminate_progress_library);
        errorLayout = (LinearLayout) findViewById(R.id.ErrorSheet);
        search_bar = (LinearLayout) findViewById(R.id.search_bar);


        blackOnWhite = (TextView) findViewById(R.id.blackONWhite);
        darktheme = (TextView) findViewById(R.id.darknight);
        oldtheme = (TextView) findViewById(R.id.oldpaperc);
        darkblue = (TextView) findViewById(R.id.darkbluish);
        seek = (AppCompatSeekBar) findViewById(R.id.sizesetterbar);

        setupMenu();

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionOnBottomMenu(Constants.EXPANDE_BOTTOM_MENU);
                fab.hide();
            }
        });

        AppCompatImageView cancel = (AppCompatImageView) findViewById(R.id.close);
        final AppCompatEditText edit_search = (AppCompatEditText) findViewById(R.id.mainEditText);
        AppCompatImageView goUp = (AppCompatImageView) findViewById(R.id.goup);
        AppCompatImageView goDown = (AppCompatImageView) findViewById(R.id.godown);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchInSheet(false);
            }
        });

        edit_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    search_bar.setAlpha((float) 1);
                } else {
                    search_bar.setAlpha((float) 0.7);
                }
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                mainWebViewSheets.findAll(edit_search.getText().toString());

                try {
                    // Can't use getMethod() as it's a private method
                    for (Method m : WebView.class.getDeclaredMethods()) {
                        if (m.getName().equals("setFindIsUp")) {
                            m.setAccessible(true);
                            m.invoke(mainWebViewSheets, true);
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        goDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWebViewSheets.findNext(true);

            }
        });

        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainWebViewSheets.findNext(false);

            }
        });


        if (mpbar.getIndeterminateDrawable() != null) {
            mpbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        showProgress(true);

        try{
            //Debug purpose
            //Subject = 6;
            //Year = 2005;
            Subject = getIntent().getExtras().getInt("Subject" , 6);
            Grade11Units = getIntent().getExtras().getIntegerArrayList("G11Chapters");
            Grade12Units = getIntent().getExtras().getIntegerArrayList("G12Chapters");
        } catch (Exception e){
            Subject = -1;
            e.printStackTrace();
        }

        if(Grade11Units == null) {
            if(Subject != -1) {
                //Currently The database has all chapters as value 20
                /*int g11units = Constants.getHowManyUnitsSubjectHasG11(Subject);
                for (int x = 1; x <= g11units; x++) {
                    Grade11Units.add(x);
                }*/

                //For the time being
                Grade11Units.add(20);

            }
        }

        if(Grade12Units == null){
            if(Subject != -1) {
                //Currently The database has all chapters as value 20
               /* int g12units = Constants.getHowManyUnitsSubjectHasG12(Subject);
                for (int x = 1; x <= g12units; x++) {
                    Grade12Units.add(x);
                }*/

                //For the time being
                Grade12Units.add(20);
            }
        }

        shuffle_build object = new shuffle_build();
        object.execute();
    }

    private void makeAlertDialogFromJs(String data){
        String[] a = {};
        data = data.replace("popupg12://" , "");
        data = data.replace("/" , "");
        a = data.split(";");
        Log.e("XXXX" , Arrays.toString(a));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void actionOnBottomMenu(int i){
        switch (i){
            case Constants.COLLAPSE_BOTTOM_MENU:
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case Constants.EXPANDE_BOTTOM_MENU:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case Constants.HIDE_BOTTOM_MENU:
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                actionOnBottomMenu(Constants.COLLAPSE_BOTTOM_MENU);
            else {

                AlertDialog.Builder exitQ = new AlertDialog.Builder(this);
                exitQ.setTitle("Are you sure?");
                exitQ.setMessage("Are you sure you want to exit to the homepage? All your progress will be lost.");
                exitQ.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(QuizziWebActivity.this , Subject_Choose.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(0, 0);
                    }
                }).setNegativeButton("No", null);
                exitQ.show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_view_q, menu);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                Rect outside = new Rect();
                bottomMenu.getGlobalVisibleRect(outside);

                if(!outside.contains((int) ev.getRawX() , (int) ev.getRawY())){
                    actionOnBottomMenu(Constants.COLLAPSE_BOTTOM_MENU);
                }

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void SearchInSheet(boolean show){
        search_bar.setVisibility(show ? View.VISIBLE : GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_in_sheet) {
            SearchInSheet(true);
            return true;
        } else if(id == R.id.evaluate_all){
            mainWebViewSheets.loadUrl("javascript:evaluate_from_menu();");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_sub) {
            AlertDialog.Builder exitQ = new AlertDialog.Builder(this);
            exitQ.setTitle("Are you sure?");
            exitQ.setMessage("Are you sure you want to exit to the homepage? All your progress will be lost.");
            exitQ.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(QuizziWebActivity.this , Subject_Choose.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(0, 0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deselectNavMenu();
                }
            });
            exitQ.show();
        } else if (id == R.id.nav_quizzi) {
            AlertDialog.Builder exitQ = new AlertDialog.Builder(this);
            exitQ.setTitle("Are you sure?");
            exitQ.setMessage("Are you sure you want to exit to the quizzipage? All your progress will be lost.");
            exitQ.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(QuizziWebActivity.this , Subject_Choose.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("alsoGoTo" , 1));
                    overridePendingTransition(0, 0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deselectNavMenu();
                }
            });
            exitQ.show();
        } else if (id == R.id.nav_my_progress) {

        } else if (id == R.id.nav_challenge) {

        } else if (id == R.id.euee_result) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            if(hours > 1){
                finalTimerString = hours + "hrs ";
            }else {
                finalTimerString = hours + "hr ";
            }
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if(minutes > 1) {
            finalTimerString = finalTimerString + minutes + "mins " + secondsString + "s";
        }else{
            finalTimerString = finalTimerString + minutes + "min " + secondsString + "s";
        }
        // return timer string
        return finalTimerString;
    }

    private void startTimer(){

        double times = 1.0;
        times = (20 * Constants.getTimerDataForSubject(Subject)) / Constants.giveMeQuestionNumber(Subject);

        mainTimer = new CountDownTimer((int)(30 * 1000 * 60 * times), 1000) { //remove * 60 for debug
            @Override
            public void onTick(long l) {
                try{getSupportActionBar().setSubtitle(milliSecondsToTimer(l));}catch (Exception e){e.printStackTrace();}
            }

            private void ad() {
                AlertDialog.Builder b = new AlertDialog.Builder(QuizziWebActivity.this);
                b.setTitle("Time's up");
                b.setMessage("Sorry, You did not finish in time. Please try again to improve your mark");
                b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(QuizziWebActivity.this, Subject_Choose.class);
                        startActivity(a);
                    }
                });
                b.show();
            }

            @Override
            public void onFinish() {
                if (didfinsh != 0) {
                    getSupportActionBar().setTitle(Constants.getSubjectName(Subject) + " Quizzi");
                    try{getSupportActionBar().setSubtitle("");}catch (Exception e){e.printStackTrace();}
                } else {
                    if (!amoutdontshow) {
                        if (!isPaused) {
                            try {
                                Vibrator v = (Vibrator) QuizziWebActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                ad();
                            } catch (Exception x) {
                                x.printStackTrace();
                            }
                        } else {
                            try {
                                Vibrator v = (Vibrator) QuizziWebActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                Toast.makeText( QuizziWebActivity.this , "Your exam time has ended.", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(QuizziWebActivity.this , Subject_Choose.class);
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

    TextView darktheme;
    TextView blackOnWhite;
    TextView oldtheme;
    TextView darkblue;
    AppCompatSeekBar seek;

    private void stopTimer(){
        if (mainTimer != null) {
            didfinsh = 1;
            mainTimer.cancel();
            mainTimer.onFinish();
        }
    }

    private class parseTheAnswer extends AsyncTask<String , Void , String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                DbManager db = new DbManager(QuizziWebActivity.this);
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

                        int question_tested = db.getSingleInt(DbManager.TABLE_NAME_TopicWise, DbManager.COLUMN_QUESTION_TESTED, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit);
                        int question_answered = db.getSingleInt(DbManager.TABLE_NAME_TopicWise, DbManager.COLUMN_QUESTION_ANSWERED, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit);
                        int attempted_before = db.getSingleInt(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ATTEMPTED_BEFORE , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                        int answered_before = db.getSingleInt(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ANSWERED_BEFORE , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                        if (!choice.equals("X")) {
                            //the user have choosed something!
                            if (question_answered != -1 && question_tested != -1) {
                                if (attempted_before == 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put(DbManager.COLUMN_ATTEMPTED_BEFORE, 1);
                                    db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                    //Now its attempted

                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                }
                            } else{
                                //no data under the specific unit subject year...
                                ContentValues cv2 = new ContentValues();
                                cv2.put(DbManager.COLUMN_YEAR , Years.get(i));
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
                                    db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                    //Now its attempted

                                    question_tested++;
                                    if(answered_before == 0){
                                        //not answered so add only if the user is correct
                                        if (choice.equals(Answers.get(i))) {
                                            //Correct answer
                                            question_answered++;
                                            ContentValues cv3 = new ContentValues();
                                            cv3.put(DbManager.COLUMN_ANSWERED_BEFORE, 1);
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);

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
                                            db.updateDataInsideDb(DbManager.TABLE_NAME_Questions, cv3, DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit + " AND " + DbManager.COLUMN_QUESTION_NUM + " = " + i);
                                        }
                                    }
                                }

                            }
                        }

                        ContentValues cvmain = new ContentValues();
                        cvmain.put(DbManager.COLUMN_YEAR , Years.get(i));
                        cvmain.put(DbManager.COLUMN_SUBJECT , Subject);
                        cvmain.put(DbManager.COLUMN_UNIT , unit);
                        cvmain.put(DbManager.COLUMN_QUESTION_TESTED , question_tested);
                        cvmain.put(DbManager.COLUMN_QUESTION_ANSWERED , question_answered);

                        db.updateDataInsideDb(DbManager.TABLE_NAME_TopicWise , cvmain , DbManager.COLUMN_SUBJECT + " = " + Subject + " AND " + DbManager.COLUMN_YEAR + " = " + Years.get(i) + " AND " + DbManager.COLUMN_UNIT + " = " + unit);

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


    //THE MAIN THING :p
    private class shuffle_build extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... strings) {
            DbManager db = new DbManager(QuizziWebActivity.this);

            String dbWhereAddon = "";
            boolean dbWhereAddonFirst = true;
            boolean db2WhereAddonFirst = true;
            String db2WhereAddon = "";

            for(int y = 0; y < Grade11Units.size();y++){
                if(dbWhereAddonFirst) {
                    dbWhereAddon += " AND " + DbManager.COLUMN_UNIT + " = " + 11 + Grade11Units.get(y);
                    dbWhereAddonFirst = false;
                } else{
                    dbWhereAddon += " OR " + DbManager.COLUMN_UNIT + " = " + 11 + Grade11Units.get(y);
                }
            }

            for(int z = 0; z < Grade12Units.size(); z++){
                if(db2WhereAddonFirst) {
                    db2WhereAddon += " AND " + DbManager.COLUMN_UNIT + " = " + 12 + Grade12Units.get(z);
                    db2WhereAddonFirst = false;
                } else{
                    db2WhereAddon += " OR " + DbManager.COLUMN_UNIT + " = " + 12 + Grade12Units.get(z);
                }
            }

            String allYearsDb = DbManager.COLUMN_YEAR + " = 2005 OR " + DbManager.COLUMN_YEAR + " = 2006 OR " + DbManager.COLUMN_YEAR + " = 2007 OR " + DbManager.COLUMN_YEAR + " = 2008 OR " + DbManager.COLUMN_YEAR + " = 2009";

            Unit = db.getIntArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_UNIT , DbManager.COLUMN_SUBJECT + " = " + Subject + dbWhereAddon + db2WhereAddon, DbManager.COLUMN_QUESTION_NUM + " ASC");
            Contents = db.getStringArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_CONTENT , DbManager.COLUMN_SUBJECT + " = " + Subject + dbWhereAddon + db2WhereAddon, DbManager.COLUMN_QUESTION_NUM + " ASC");
            Answers = db.getStringArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_ANS , DbManager.COLUMN_SUBJECT + " = " + Subject + dbWhereAddon + db2WhereAddon, DbManager.COLUMN_QUESTION_NUM + " ASC");
            Years = db.getIntArray(DbManager.TABLE_NAME_Questions , DbManager.COLUMN_YEAR , DbManager.COLUMN_SUBJECT + " = " + Subject + dbWhereAddon + db2WhereAddon, DbManager.COLUMN_QUESTION_NUM + " ASC");

            //Now remove as much items needed from the arraylists above to make them at the size [20]
            int initialUnitSize = Unit.size();
            int howManyItemsToRemove = initialUnitSize - 20;
            Random itemToRemove = new Random();
            for(int remove = 0; remove < howManyItemsToRemove; remove++){
                //so basically everytime generate a random number from 0 - remove and remove that item from all the lists above
                int randomNumber = itemToRemove.nextInt(initialUnitSize - remove);
                Unit.remove(randomNumber);
                Contents.remove(randomNumber);
                Answers.remove(randomNumber);
                Years.remove(randomNumber);
            }

            //Should build header before going to the array
            InputStream input;
            try {

                theBigData = "";
                theBigData += "<html><head><title>" + Constants.getSubjectName(Subject) + " Quizzi </title>";
                theBigData += "<script src=\"js/core.js\"></script><link href=\"css/materialize.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><link href=\"css/style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen,projection\"/><style></style></head><body>";

                /*The ad banner*/
                theBigData += "<div class=\"row center\"><div class=\"col s12 m7 center\"><div class=\"card white darken-1 center\"><div class=\"card-image\" style=\"width:100%;height:200px;\"><img src=\"lv.jpg\" style=\"width:100%;height:100%;\"><span class=\"card-title\">ETMDB</span><a class=\"btn-floating halfway-fab waves-effect waves-light red white-text\" href=\"traveltohttp://facebook.com\">Go</a></div><div class=\"card-content black-text\"><p>We are proud of ethiopian movies. And we plan to make them big. Join us and help to make the database for Ethiopian movies.</p></div></div></div></div><div style=\"margin:2%;padding:1%\">";

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

                //after the array then comes material evaluate button
                theBigData += "<center><input class=\"btn-flat waves-effect waves-light blue white-text\" style=\"margin-top:2%;margin-bottom:2%;\" type=Button id=EvaluateBtn value=Evaluate onclick=evaluateforown(\"" + Constants.getSubjectNameForEvaluation(Subject) + "\",\"1000\") ></center></div></body></html>";
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

                stopTimer();

                mainWebViewSheets.getSettings().setJavaScriptEnabled(true);
                mainWebViewSheets.getSettings().setBuiltInZoomControls(true);
                mainWebViewSheets.getSettings().setDisplayZoomControls(false);
                mainWebViewSheets.getSettings().setAppCacheEnabled(true);
                mainWebViewSheets.getSettings().setAllowFileAccess(true);
                mainWebViewSheets.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                mainWebViewSheets.getSettings().setSupportZoom(true);

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
                        } else if(url.contains("traveltohttp://")){
                            String data = url;
                            data = data.replace("traveltohttp://" , "");
                            data = data.replace("/" , "");

                            Log.e("XXXX" , data);

                            Intent browserIntetn = new Intent(Intent.ACTION_VIEW , Uri.parse("http://" + data));
                            startActivity(browserIntetn);

                        } else if(url.contains("traveltohttps://")){
                            String data = url;
                            data = data.replace("traveltohttps://" , "");
                            data = data.replace("/" , "");

                            Log.e("XXXX" , data);

                            Intent browserIntetn = new Intent(Intent.ACTION_VIEW , Uri.parse("https://" + data));
                            startActivity(browserIntetn);
                        } else if(url.contains("showmyhint://")){
                            String data = url;
                            data = data.replace("showmyhint://" , "");
                            data = data.replace("/" , "");

                            Log.e("XXXX" , data);

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

                mainWebViewSheets.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Toast.makeText(QuizziWebActivity.this, "Long click is disabled", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                mainWebViewSheets.setLongClickable(false);
                mainWebViewSheets.setHapticFeedbackEnabled(false);

                mainWebViewSheets.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        try{ getSupportActionBar().setTitle(title); }catch (Exception e){e.printStackTrace();}
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                    }
                });

                if(Subject == -1){
                    //Error occured take them back to the home page
                    errorLayout.setVisibility(View.VISIBLE);
                }else{
                    if(!s.equals("null")){
                        mainWebViewSheets.loadDataWithBaseURL("file:///android_asset/" , s , "text/html" , "UTF-8" , "");
                        mainWebViewSheets.getSettings().setDomStorageEnabled(true);
                    }
                }

            } catch (Exception e){
                Log.e("XXXX" , e.getStackTrace().toString());
            }
        }
    }








}
