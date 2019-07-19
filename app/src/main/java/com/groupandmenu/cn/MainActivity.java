package com.groupandmenu.cn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ExpandableListView myExpandableListView;
    private ExPandableListViewAdapter adapter;
    private ArrayList<FatherData> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setData();
        setAdapter();
    }

    // 初始化控件
    private void initView() {
        myExpandableListView = (ExpandableListView) findViewById(R.id.alarm_clock_expandablelist);
        //去掉默认带的箭头
        myExpandableListView.setGroupIndicator(null);

        // 设置ExpandableListView的监听事件
        // 设置一级item点击的监听器
        myExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(MainActivity.this, datas.get(arg2).getTitle(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

//         设置二级item点击的监听器，同时在Adapter中设置isChildSelectable返回值true，同时二级列表布局中控件不能设置点击效果
//        myExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4) {
//                Toast.makeText(MainActivity.this, datas.get(arg2).getList().get(arg3).getTitle(), Toast.LENGTH_LONG).show();
//                Log.i("====cc",datas.get(arg2).getList().get(arg3).getTitle());
//
//                return false;
//            }
//        });
    }

    /**
     * 自定义setAdapter
     */
    private void setAdapter() {
        if (adapter == null) {
            adapter = new ExPandableListViewAdapter(this, datas);
            myExpandableListView.setAdapter(adapter);
        } else {
            adapter.flashData(datas);
        }
    }

    // 定义数据
    private void setData() {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        // 一级列表中的数据
        for (int i = 0; i < 5; i++) {
            FatherData fatherData = new FatherData();
            fatherData.setTitle("闹钟列表" + i);
            // 二级列表中的数据
            ArrayList<ChildrenData> itemList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ChildrenData childrenData = new ChildrenData();
                childrenData.setTitle("闹钟主题" + j);
                childrenData.setDesc(j + ":30");
                itemList.add(childrenData);
            }
            fatherData.setList(itemList);
            datas.add(fatherData);
        }
    }

}
