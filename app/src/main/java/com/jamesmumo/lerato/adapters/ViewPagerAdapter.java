package com.jamesmumo.lerato.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jamesmumo.lerato.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int[] images = {
            R.drawable.befearless,
            R.drawable.fistcircle,
            R.drawable.empower,
    };

    int[] headings = {
            R.string.onboardingPowerful,
            R.string.onboardingEmpower,
            R.string.onboardingAssist,
    };
    int[] descriptions = {
            R.string.onboardingPowerfulDesc,
            R.string.onboardingAssistDesc,
            R.string.onboardingEmpowerlDesc,
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.onboardingTextTitle);
        TextView slideDescription = (TextView) view.findViewById(R.id.onboardingTextDesc);

        slideTitleImage.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDescription.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
