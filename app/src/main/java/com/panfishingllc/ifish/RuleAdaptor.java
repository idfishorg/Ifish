package com.panfishingllc.ifish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jing on 5/3/2016.
 */
public class RuleAdaptor extends BaseAdapter {
    private ArrayList<String> ruleList = new ArrayList<String>();

    public static final int TYPE_SEASON = 0;
    public static final int TYPE_RULE = 1;

    public RuleAdaptor(ArrayList<String> ruleList) {
        this.ruleList = ruleList;
    }
    
    public RuleAdaptor() {
    }

    @Override
    public int getCount() {
        return ruleList.size();
    }

    @Override
    public Object getItem(int position) {
        return ruleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_SEASON and TYPE_RULE
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String rule = (String) getItem(position);
        if (rule.indexOf("-") > 0) {
            return TYPE_SEASON;
        }

        return TYPE_RULE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (type) {
                case TYPE_SEASON:
                    convertView = inflater.inflate(R.layout.season_item, parent, false);
                    break;
                case TYPE_RULE:
                    convertView = inflater.inflate(R.layout.rule_item, parent, false);
                    break;
            }
//            convertView = inflater.inflate(R.layout.rule_item, parent, false);
        }

        String rule = ruleList.get(position);
        switch (type) {
            case TYPE_SEASON:
                TextView seasonView = (TextView)convertView.findViewById(R.id.seasonView);
                seasonView.setText(rule);
                break;
            case TYPE_RULE:
                TextView ruleView = (TextView) convertView.findViewById(R.id.ruleView);
                ruleView.setText(rule);
                break;
        }

        return convertView;
    }

    public void setRuleList(ArrayList<String> list) {
        this.ruleList = list;
    }
}
