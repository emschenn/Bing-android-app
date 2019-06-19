package emschenn.csie.ncku.webparsetest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.chip.Chip;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {




    private FloatingActionButton fab;
    private Toolbar toolbar;
    private String url ="http://moviesun101.com/";
    private String title,site;
    private ConstraintLayout layout;
    private static RecyclerView recyclerView;
    ArrayList<cardData> arrayList;
    private SwitchCompat alarmToggle;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.alarmmanager";


    public static cardViewModel cards;


    public static ArrayList<String> myList1 = new ArrayList<String>();
    public static ArrayList<String> myList2 = new ArrayList<String>();
    public static ArrayList<String> myList3 = new ArrayList<String>();
    //private SwitchCompat alarmToggle;
    //String and Integer array for Recycler View Items
//    public static final String[] TITLES= {"進擊的巨人","Running Man","安眠書店"};
//    public static final String[] WEBSITES = {"bilibili","小鴨影音","VM美劇"};
//    public static final String[] EPISODES = {"第一季ep12","20190620","第一季ep12"};
//    private int num = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiag();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.logo);
        toolbar.setPaddingRelative(15,0,0,0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getTitle().equals("聯絡我們")){
                        Intent intent = new Intent(MainActivity.this, Contact.class);
                        startActivity(intent);
                    }
                    else if(menuItem.getTitle().equals("使用教學")){
                        Intent intent = new Intent(MainActivity.this, Teach.class);
                        startActivity(intent);
                    }
                return false;
            }
        });
        arrayList = new ArrayList<>();
        initViews();
        //populatRecyclerView();

        // change cards
        final cardAdapter adapter = new cardAdapter(this);
        recyclerView.setAdapter(adapter);
        cards = ViewModelProviders.of(this).get(cardViewModel.class);
        cards.getAllCards().observe(this, new Observer<List<cardData>>() {
            @Override
            public void onChanged(@Nullable final List<cardData> Cards) {
                // Update the cached copy of the words in the adapter.
                adapter.setCards(Cards);

                String temp1 = "";
                String temp2 = "";
                String temp3 = "";
                myList1.clear();
                myList2.clear();
                myList3.clear();
                for(int i=0;i<adapter.getItemCount();i++){
                    temp1 = cards.getAllCards().getValue().get(i).getEpisode();
                    temp2 = cards.getAllCards().getValue().get(i).getTitle();
                    temp3 = cards.getAllCards().getValue().get(i).getWebsite();
                    myList1.add(i,temp1);
                    myList2.add(i,temp2);
                    myList3.add(i,temp3);
                }

            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.UP) {
                    @Override
                    // We are not implementing onMove() in this app
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a word,
                    // delete that word from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        cardData myCard = adapter.getCardAtPosition(position);
                        // Delete the word
                        cards.deleteCard(myCard);
                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);


        alarmToggle = findViewById(R.id.alarmToggle);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        alarmToggle.setChecked(mPreferences.getBoolean("check", false));

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        //alarmToggle.setChecked(alarmUp);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        TextView a = findViewById(R.id.title);

        // Set the click listener for the toggle button.
        alarmToggle.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged
                            (CompoundButton buttonView, boolean isChecked) {
                        String toastMessage;
                        if (isChecked) {

                            //long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

                            //long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

                            // Set the alarm to start at approximately 2:00 p.m.
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);


                            // If the Toggle is turned on, set the repeating alarm with
                            // a 15 minute interval.
                            if (alarmManager != null) {
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        1000 * 60, notifyPendingIntent);
                            }
                            // Set the toast message for the "on" case.
                            toastMessage = "on";

                        } else {
                            // Cancel notification if the alarm is turned off.
                            mNotificationManager.cancelAll();

                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }
                            // Set the toast message for the "off" case.
                            toastMessage = "off";

                        }

                        // Show a toast to say the alarm is turned on or off.
                        Toast.makeText(MainActivity.this, toastMessage,
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Create the notification channel.
        createNotificationChannel();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


//
//    public class parse extends AsyncTask<Void, Void, Void>{
//        String word;
//        Elements a;
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//               Document doc = Jsoup.connect("https://www.vlive.tv/search/all?query=BTS").get();
//             //  word = doc.text();
//                a = doc.select("li.channel_lst_ct");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//    }

    private void showDiag() {
        final View dialogView = View.inflate(this,R.layout.content_add,null);
        final Dialog dialog = new Dialog(this,R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });
        final Chip chip1 = dialog.findViewById(R.id.all);
        final Chip chip2 = dialog.findViewById(R.id.japan);
        final Chip chip3 = dialog.findViewById(R.id.america);
        final Button cancel = dialog.findViewById(R.id.cancel);
        final Button enter = dialog.findViewById(R.id.enter);
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        final EditText editText = dialog.findViewById(R.id.editText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.init, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        chip1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chip1.isChecked()){
                    chip2.setChecked(false);
                    chip3.setChecked(false);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(),
                            R.array.all, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
        });
        chip2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chip2.isChecked()){
                    chip1.setChecked(false);
                    chip3.setChecked(false);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(),
                            R.array.japan, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
        });
        chip3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chip3.isChecked()){
                    chip2.setChecked(false);
                    chip1.setChecked(false);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(),
                            R.array.america, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                site = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShow(dialogView, false, dialog);
                title = editText.getText().toString();
                //arrayList.add(new cardData(title,site,"??"));
                //RecyclerView_Adapter adapter = new RecyclerView_Adapter(MainActivity.this, arrayList);
                //recyclerView.setAdapter(adapter);// set adapter on recyclerview
                //adapter.notifyDataSetChanged();// Notify the adapter
                Log.d("title",title);
                Log.d("site",site);
                int website = -1;
                if(site.charAt(0) =='5'){
                    website = 5;
                }
                if(site.charAt(0) =='A'){
                    website = 4;
                }
                if(site.charAt(0) =='1'){
                    website = 3;
                }
                if(site.charAt(0) =='V'){
                    website = 2;
                }
                if(site.charAt(0) =='M'){
                    website = 1;
                }
                //crawl.crawl_func(website,title);
                //addCard(title,site);
                cards.insert(new cardData(title,site,"aa"));

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShow(dialogView, false, dialog);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    revealShow(dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();
        int endRadius = (int) Math.hypot(w, h);
        int cx = (int) (fab.getX() + (fab.getWidth()/2));
        int cy = (int) (fab.getY())+ fab.getHeight() + 56;
        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();
        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(700);
            anim.start();
        }
    }

    // Initialize the view
    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Set Back Icon on Activity
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //Set RecyclerView type according to intent value
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to " +
                    "stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean("check", alarmToggle.isChecked());
        preferencesEditor.apply();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void update(View view) {
        cardData ocard;
        cardData ncard;
        for (int i = 0; i < MainActivity.myList1.size(); i++) {
            int website = -1;
            if (MainActivity.myList3.get(i).charAt(0) == '5') {
                website = 5;
            }
            if (MainActivity.myList3.get(i).charAt(0) == 'A') {
                website = 4;
            }
            if (MainActivity.myList3.get(i).charAt(0) == '1') {
                website = 3;
            }
            if (MainActivity.myList3.get(i).charAt(0) == 'V') {
                website = 2;
            }
            if (MainActivity.myList3.get(i).charAt(0) == 'M') {
                website = 1;
            }
            //List1 = Episode
            //List2 = Title
            //List3 = web
            ocard = new cardData(MainActivity.myList2.get(i), MainActivity.myList3.get(i), MainActivity.myList1.get(i));
            ncard = new cardData(MainActivity.myList2.get(i), MainActivity.myList3.get(i), crawl.crawl_func(website, MainActivity.myList2.get(i)));
            cards.update(ocard,ncard);
        }
    }
}
