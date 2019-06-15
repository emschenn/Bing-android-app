package emschenn.csie.ncku.webparsetest;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Teach extends AppCompatActivity {
    private Toolbar toolbar;
    ViewPager viewPager;
    LinearLayout slideDots;
    ViewPagerAdapter viewPagerAdapter;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setPadding(15,0,0,0);
        toolbar.setTitle("使用教學");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("聯絡我們")){
                    Intent intent = new Intent(Teach.this, Contact.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        viewPager = findViewById(R.id.view_page);
        slideDots = findViewById(R.id.slide_dots);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        dots = new  ImageView[viewPagerAdapter.getCount()];
        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            slideDots.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
