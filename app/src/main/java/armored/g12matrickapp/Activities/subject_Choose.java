package armored.g12matrickapp.Activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.util.List;

import armored.g12matrickapp.Fragments.Choose_Subject_Fragment;
import armored.g12matrickapp.Fragments.Quizzi_Fragment;
import armored.g12matrickapp.R;
import armored.g12matrickapp.Utils.Constants;
import armored.g12matrickapp.Utils.Functions;
import armored.g12matrickapp.Widgets.ExpandableGridView;

import static android.view.View.GONE;

public class subject_Choose extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private LinearLayout container_two;

    private FloatingActionButton bottomFabMenu;

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

    public void show_menu_fab(boolean show){
        if(show){
            if(!bottomFabMenu.isShown()){
                bottomFabMenu.show();
            }
        } else{
            if(bottomFabMenu.isShown()){
                bottomFabMenu.hide();
            }
        }
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

        bottomFabMenu = (FloatingActionButton) findViewById(R.id.fab);

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

        navItemIndex = 0;
        CURRENT_TAG = TAG_CHANGE_SUBJECT;
        navigationView.getMenu().getItem(0).setChecked(true);
        loadChoosedFragment();

        bottomMenu = (NestedScrollView) findViewById(R.id.paperPageMenu);
        behavior = BottomSheetBehavior.from(bottomMenu);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomFabMenu.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        container_one = (NestedScrollView) findViewById(R.id.container_one);
        container_two = (LinearLayout) findViewById(R.id.container_two);

        bottomFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionOnBottomMenu(Constants.EXPANDE_BOTTOM_MENU);
                bottomFabMenu.hide();
            }
        });

    }



    DrawerLayout drawer;
    NestedScrollView bottomMenu;
    BottomSheetBehavior behavior;

    public void SetTitleFromFragment(String title){
        getSupportActionBar().setTitle(title);
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


    private void setToolbarTitle(){
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }


    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                actionOnBottomMenu(Constants.COLLAPSE_BOTTOM_MENU);
            } else {

                if(getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
                    if(getSupportFragmentManager().findFragmentById(R.id.fragment).getTag().equals(TAG_CHANGE_SUBJECT)){
                        Toast.makeText(this, "Exit!!!!!!!!", Toast.LENGTH_SHORT).show();
                    } else {

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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
