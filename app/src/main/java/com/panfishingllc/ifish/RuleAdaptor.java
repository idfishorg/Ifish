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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.rule_item, parent, false);
        }

        String rule = ruleList.get(position);

        TextView ruleView = (TextView) convertView.findViewById(R.id.ruleView);
        ruleView.setText(rule);

        return convertView;
    }

    public void setRuleList(ArrayList<String> list) {
        this.ruleList = list;
    }
}
