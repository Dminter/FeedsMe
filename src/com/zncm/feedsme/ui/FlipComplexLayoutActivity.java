/*
 * Copyright 2012 Aphid Mobile
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zncm.feedsme.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.aphidmobile.flip.FlipViewController;
import com.zncm.feedsme.pojo.News;

public class FlipComplexLayoutActivity extends Activity {

    private FlipViewController flipView;

    private GridViewAdapter gridViewAdapter;
    ArrayList<News> news_list;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String paper = getIntent().getExtras().getString("paper");
        int current = getIntent().getExtras().getInt("current");
        setTitle(paper);
        ArrayList list = getIntent().getExtras().getParcelableArrayList("news_list");
        news_list = (ArrayList<News>) list.get(0);
        flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
        // Use RGB_565 can reduce peak memory usage on large screen device, but it's up to you to
        // choose the best bitmap format
        flipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
        gridViewAdapter = new GridViewAdapter(this, news_list);
        flipView.setAdapter(gridViewAdapter);
        flipView.setSelection(current);
        setContentView(flipView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flipView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flipView.onPause();
    }


    private class GridViewAdapter extends BaseAdapter {

        Context context;
        ArrayList<News> news_list;

        public GridViewAdapter(Context context, ArrayList<News> news_list) {
            this.context = context;
            this.news_list = news_list;
        }

        @Override
        public int getCount() {

            return news_list.size();
        }

        @Override
        public Object getItem(int position) {

            return news_list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.news_content, null);
            TextView tv01 = (TextView) convertView.findViewById(R.id.news_content_title);
            TextView tv02 = (TextView) convertView.findViewById(R.id.news_content_description);
            Button btn_url = (Button) convertView.findViewById(R.id.news_content_site);
            tv01.setText((position + 1) + "." + news_list.get(position).getTitle());
            tv02.setText(news_list.get(position).getContent());
            final String links = news_list.get(position).getLinks();
            if (links != null) {
                btn_url.setEnabled(true);
            } else {
                btn_url.setEnabled(false);
            }
            btn_url.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    Intent intent =
                            new Intent(FlipComplexLayoutActivity.this, FlipWebViewActivity.class);
                    intent.putExtra("links", links);
                    startActivity(intent);



                }
            });


            return convertView;
        }
    }

}
