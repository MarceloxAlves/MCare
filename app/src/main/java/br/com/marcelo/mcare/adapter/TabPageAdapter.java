package br.com.marcelo.mcare.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import br.com.marcelo.mcare.app.ConsultaFragment;
import br.com.marcelo.mcare.app.ExameFragment;

/**
 * Created by Marcelo on 28/02/2018.
 */

public class TabPageAdapter extends FragmentPagerAdapter {

    final int TOTAL_PAGES = 2;
    String[] legendas = {"Consultas","Exames"};

    public TabPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ConsultaFragment();
            case 1:
                return new ExameFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return legendas[position];
    }
}
