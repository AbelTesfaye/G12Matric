package armored.g12matrickapp.Activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.Adapters.ChooserArrayAdapter;
import armored.g12matrickapp.Fragments.Choose_Subject_Fragment;
import armored.g12matrickapp.Fragments.Quizzi_Fragment;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Functions;
import armored.g12matrickapp.Widgets.ExpandableGridView;

import static android.view.View.GONE;

public class Subject_Choose extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private View navHeader;
    private AppCompatImageView imgNavProfile , imgNavBackground;
    private TextView txtName , txtWebsite;


    private int navItemIndex = 0;
    private NavigationView navigationView;
    private Toolbar primaryToolbar;
    private Toolbar secondaryToolbar;

    private TextView detailsToolbarTextView;

    private NestedScrollView container_one;
    private NestedScrollView container_two;

    private Handler mHandler;

    private String[] activityTitles;

    private static final String TAG_CHANGE_SUBJECT = "chsub";
    private static final String TAG_QUIZZI = "quizzi";
    private static final String TAG_MY_PROGRESS = "mprogress";
    private static final String TAG_CHALLENGES = "challenge";
    private static final String TAG_EUEE_RESULT = "eueer";

    public static String CURRENT_TAG = TAG_CHANGE_SUBJECT;


    ExpandableGridView catGrid;
    View fragment;

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

    private FragmentManager.OnBackStackChangedListener getListener(){
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if(manager != null){
                    int backstackentrycount = manager.getBackStackEntryCount();
                    if(backstackentrycount == 0){
                        finish();
                    } else{
                        Fragment frag = manager.getFragments().get(backstackentrycount - 1);
                        frag.onResume();
                    }
                }
            }
        };
        return result;
    }

    public void hide_container_one(){
        container_one.setVisibility(GONE);
        container_two.setVisibility(View.VISIBLE);
    }

    public void hide_container_two(){
        container_one.setVisibility(View.VISIBLE);
        container_two.setVisibility(View.GONE);
    }

    public void hide_second_toolbar(){
        secondaryToolbar.setVisibility(GONE);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)secondaryToolbar.getLayoutParams();
        AppBarLayout.LayoutParams paramsp = (AppBarLayout.LayoutParams)primaryToolbar.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        paramsp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);

        primaryToolbar.setLayoutParams(paramsp);
        secondaryToolbar.setLayoutParams(params);
    }

    public void show_second_toolbar(){
        secondaryToolbar.setVisibility(View.VISIBLE);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)secondaryToolbar.getLayoutParams();
        AppBarLayout.LayoutParams paramsp = (AppBarLayout.LayoutParams)primaryToolbar.getLayoutParams();

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        paramsp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        primaryToolbar.setLayoutParams(paramsp);
        secondaryToolbar.setLayoutParams(params);
    }

    int alsoGoTo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_removed);

        primaryToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(primaryToolbar);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mHandler = new Handler();

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        secondaryToolbar = (Toolbar) findViewById(R.id.secondaryTab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, primaryToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        detailsToolbarTextView = (TextView) findViewById(R.id.detailToolbarTextView);
        detailsToolbarTextView.setText("Subjects");

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.userName);
        txtWebsite = (TextView) navHeader.findViewById(R.id.userSchool);
        imgNavProfile = (AppCompatImageView) navHeader.findViewById(R.id.profilePic);
        imgNavBackground = (AppCompatImageView) navHeader. findViewById(R.id.libBackgroundNav);
        fragment = (View) findViewById(R.id.fragment);

        loadNavHeader();

        try {
            alsoGoTo = getIntent().getExtras().getInt("alsoGoTo" , 0);
        } catch (Exception e){
            alsoGoTo = 0;
            e.printStackTrace();
        }

        if(alsoGoTo == 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_CHANGE_SUBJECT;
            navigationView.getMenu().getItem(0).setChecked(true);
            loadChoosedFragment();
        } else {
            navItemIndex = alsoGoTo;
            CURRENT_TAG = getTagFromIndex(alsoGoTo);
            navigationView.getMenu().getItem(alsoGoTo).setChecked(true);
            loadChoosedFragment();
        }


        container_one = (NestedScrollView) findViewById(R.id.container_one);
        container_two = (NestedScrollView) findViewById(R.id.container_two);


    }

    DrawerLayout drawer;

    public void SetTitleFromFragment(String title){
        getSupportActionBar().setTitle(title);
    }

    private String getTagFromIndex(int index){
        switch (index){
            case 0:
                return TAG_CHANGE_SUBJECT;
            case 1:
                return TAG_QUIZZI;
            case 2:
                return TAG_MY_PROGRESS;
            case 3:
                return TAG_CHALLENGES;
            case 4:
                return TAG_EUEE_RESULT;
            default:
                return TAG_CHANGE_SUBJECT;
        }
    }

    public void SetSubTitleFromFragment(String title){
        getSupportActionBar().setSubtitle(title);
    }

    public void SetTitleOfDetailFromFragment(String title){ detailsToolbarTextView.setText(title);}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject__choose, menu);
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

    private void loadChoosedFragment(){
        selectNavMenu();
        setToolbarTitle();

        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null){
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                try{
                    if(getSupportFragmentManager().findFragmentById(R.id.fragment) != null)
                        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment));

                    if(getSupportFragmentManager().findFragmentById(R.id.fragment2) != null)
                        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment2));


                } catch (NullPointerException a){
                    a.printStackTrace();
                }
                fragmentTransaction.replace(R.id.fragment, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable != null){
            mHandler.postDelayed(mPendingRunnable , 250);
        }

        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment(){
        switch (navItemIndex){
            case 0:
                show_second_toolbar();
                return new Choose_Subject_Fragment();
            case 1:
                hide_second_toolbar();
                return new Quizzi_Fragment();
            /*
            case 2:
                return new GraphsFragment();
            case 3:
                return new WhatsHotFragment();*/
            default:
                return new Fragment();
        }
    }

    //This will be used inside onbackpressed to go back from years to subjects on mainPage
    boolean amOnYearsPart = false;
    //This will be used inside onbackpressed to go back from chapters to subjects on quizzi
    boolean amOnChaptersPart = false;

    private void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }
    public void setAmOnYearsPart(boolean b) { amOnYearsPart = b; }
    public void setAmOnChaptersPart(boolean b) { amOnChaptersPart = b; }

    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    boolean isDOubleTOuched = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                if(getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
                    if(getSupportFragmentManager().findFragmentById(R.id.fragment).getTag().equals(TAG_CHANGE_SUBJECT)){
                            if (!isDOubleTOuched) {
                                isDOubleTOuched = true;
                                Snackbar snackbar = Snackbar.make(secondaryToolbar, "Press Again To Exit", Snackbar.LENGTH_SHORT)
                                        .setAction("EXIT NOW", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                finish();
                                            }
                                        });
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(Subject_Choose.this, R.color.colorPrimary));
                                snackbar.show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isDOubleTOuched = false;
                                    }
                                }, 1500);

                                snackbar.show();

                            } else {
                                finish();
                            }
                    } else if(getSupportFragmentManager().findFragmentById(R.id.fragment).getTag().equals(TAG_QUIZZI)) {
                        if(amOnChaptersPart){
                            Quizzi_Fragment tempFrag = (Quizzi_Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                            tempFrag.performBackButtonClick();
                        } else {
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_CHANGE_SUBJECT;
                            navigationView.getMenu().getItem(0).setChecked(true);
                            loadChoosedFragment();
                        }
                    }
                    else {

                        if(amOnYearsPart){
                            navItemIndex = 0;
                            CURRENT_TAG = TAG_CHANGE_SUBJECT;
                            navigationView.getMenu().getItem(0).setChecked(true);
                            loadChoosedFragment();
                        }else {

                            AlertDialog.Builder exitQ = new AlertDialog.Builder(this);
                            exitQ.setTitle("Are you sure?");
                            exitQ.setMessage("Are you sure you want to exit to the homepage? All your progress will be lost.");
                            exitQ.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    navItemIndex = 0;
                                    CURRENT_TAG = TAG_CHANGE_SUBJECT;
                                    navigationView.getMenu().getItem(0).setChecked(true);
                                    loadChoosedFragment();
                                }
                            }).setNegativeButton("No", null);
                            exitQ.show();
                        }
                    }
                }
        }
    }

    public void deselectNavMenu(){
        int i = navigationView.getMenu().size();
        for(int x = 0; x < i; x++){
            navigationView.getMenu().getItem(x).setChecked(false);
        }
    }

    public void shareDialog() {
        final List<String> packages = new ArrayList<String>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        final List<ResolveInfo> resInfosNew = new ArrayList<ResolveInfo>();
        final List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);
        resInfosNew.addAll(resInfos);
        if (!resInfos.isEmpty()) {
            System.out.println("Have package");
            int count = 0;
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;
                if (packageName.contains("com.facebook.katana")) {
                    resInfosNew.remove(count);
                } else
                    packages.add(packageName);
                count++;
            }
        }

        if (packages.size() > 1) {
            ArrayAdapter<String> adapter = new ChooserArrayAdapter(this, android.R.layout.select_dialog_item, android.R.id.text1, packages);

            String title = "<b>Share via...</b>";
            new AlertDialog.Builder(this)
                    .setTitle(Html.fromHtml(title))
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            invokeApplication(packages.get(item), resInfosNew.get(item));
                        }
                    })
                    .show();
        } else if (packages.size() == 1) {
            invokeApplication(packages.get(0), resInfos.get(0));
        }
    }

    private void invokeApplication(String packageName, ResolveInfo resolveInfo) {
        // if(packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana") || packageName.contains("com.kakao.story")) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharetxt = "Practice makes perfect. Exercise previous grade 12 matrick questions to do your best.\n Download our app from [zufanapps.tk/g12matrick] and Thank you.";
        intent.putExtra(Intent.EXTRA_TEXT, sharetxt);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share G12");
        intent.setPackage(packageName);
        startActivity(intent);
        // }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_sub) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_CHANGE_SUBJECT;
            if(item.isChecked()){
                item.setChecked(false);
            } else{
                item.setChecked(true);
            }
            item.setChecked(true);
            loadChoosedFragment();
            return true;
        } else if (id == R.id.nav_quizzi) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_QUIZZI;
            if(item.isChecked()){
                item.setChecked(false);
            } else{
                item.setChecked(true);
            }
            item.setChecked(true);
            loadChoosedFragment();
            return true;
        } else if (id == R.id.nav_my_progress) {

        } else if (id == R.id.nav_challenge) {

        } else if (id == R.id.euee_result) {

        } else if(id == R.id.settings) {

        } else if (id == R.id.nav_share) {
            shareDialog();
        } else if (id == R.id.nav_send) {

        } else if(id == R.id.rate_us){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
