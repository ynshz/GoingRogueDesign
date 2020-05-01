package com.example.goingroguedesign.ui.projects;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.goingroguedesign.R;
import com.example.goingroguedesign.ui.projects.calendar.CalendarFragment;
import com.example.goingroguedesign.ui.projects.document.DocumentFragment;
import com.example.goingroguedesign.ui.projects.drawing.DrawingFragment;
import com.example.goingroguedesign.ui.projects.invoice.InvoiceFragment;
import com.example.goingroguedesign.ui.projects.note.NoteFragment;
import com.example.goingroguedesign.ui.projects.task.TaskFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_0, R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;
    String id;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String s) {
        super(fm);
        mContext = context;
        id = s;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new DocumentFragment();
                break;
            case 2:
                fragment = new CalendarFragment();
                break;
            case 3:
                fragment = new InvoiceFragment();
                break;
            case 4:
                fragment = new TaskFragment();
                break;
            case 5:
                fragment = new NoteFragment();
                break;
            default:
                fragment = new DrawingFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("projectID", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 6;
    }
}