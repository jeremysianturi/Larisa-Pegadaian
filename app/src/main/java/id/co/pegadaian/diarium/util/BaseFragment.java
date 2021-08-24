package id.co.pegadaian.diarium.util;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public abstract void findViews(View view);
    public abstract void initViews(View view);
    public abstract void initListener(View view);
}
