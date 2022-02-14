package vn.hust.soict.project.iotcommunication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.hust.soict.project.iotcommunication.ui.ManageHomeFragment;
import vn.hust.soict.project.iotcommunication.ui.MyProfileFragment;
import vn.hust.soict.project.iotcommunication.ui.NotificationFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ManageHomeFragment();
            case 1:
                return new NotificationFragment();
            case 2:
                return new MyProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
