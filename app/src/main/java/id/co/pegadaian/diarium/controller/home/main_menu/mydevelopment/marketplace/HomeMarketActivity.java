package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home.AccountFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home.CartFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home.FeaturedFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home.MyCoursesFragment;
import id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home.WishlistFragment;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class HomeMarketActivity extends AppCompatActivity {

  private Fragment fragment;
  private FragmentManager fragmentManager;

  private UserSessionManager session;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_market);

    session = new UserSessionManager(this);

    fragmentManager = getSupportFragmentManager();
    fragment = new FeaturedFragment();
    final FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(R.id.content, fragment).commit();

    BottomNavigationView bottomNavigationView = (BottomNavigationView)
            findViewById(R.id.navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                  case R.id.navigation_featured:
                    fragment = new FeaturedFragment();
                    break;
                  case R.id.navigation_chart:
                    fragment = new CartFragment();
                    break;
                  case R.id.navigation_mycourses:
                    fragment = new MyCoursesFragment();
                    break;
                  case R.id.navigation_wishlist:
                    fragment = new WishlistFragment();
                    break;
                  case R.id.navigation_account:
                    fragment = new AccountFragment();
                    break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment).commit();
                return true;
              }
            });
  }
}
