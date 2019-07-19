package com.groupandmenu.cn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ditclear.swipelayout.SwipeDragLayout;

import java.util.ArrayList;

public class ExPandableListViewAdapter extends BaseExpandableListAdapter {
    // 定义一个Context
    private Context context;
    // 定义一个LayoutInflater
    private LayoutInflater mInflater;
    // 定义一个List来保存列表数据
    private ArrayList<FatherData> data_list = new ArrayList<>();

    // 定义一个构造方法
    public ExPandableListViewAdapter(Context context, ArrayList<FatherData> datas) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data_list = datas;
    }

    // 刷新数据
    public void flashData(ArrayList<FatherData> datas) {
        this.data_list = datas;
        this.notifyDataSetChanged();
    }

    // 获取二级列表的内容
    @Override
    public Object getChild(int arg0, int arg1) {
        return data_list.get(arg0).getList().get(arg1);
    }

    // 获取二级列表的ID
    @Override
    public long getChildId(int arg0, int arg1) {
        return arg1;
    }

    // 定义二级列表中的数据
    @Override
    public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 定义一个二级列表的视图类
        final HolderView childrenView;
        if (convertView == null) {
            childrenView = new HolderView();
            // 获取子视图的布局文件
            convertView = mInflater.inflate(R.layout.activity_main_children, parent, false);
            childrenView.content_layout = (LinearLayout) convertView.findViewById(R.id.content_layout);
            childrenView.titleView = (TextView) convertView.findViewById(R.id.alarm_clock_tv1);
            childrenView.descView = (TextView) convertView.findViewById(R.id.alarm_clock_tv2);
            childrenView.swipeDragLayout=convertView.findViewById(R.id.swip_layout);
            childrenView.edit = (TextView) convertView.findViewById(R.id.edit);
            childrenView.delete = (TextView) convertView.findViewById(R.id.delete);
            // 这个函数是用来将holderview设置标签,相当于缓存在view当中
            convertView.setTag(childrenView);

            // 标记位置
            // 必须使用资源Id当key（不是资源id会出现运行时异常），android本意应该是想用tag来保存资源id对应组件。
            // 将groupPosition，childPosition通过setTag保存,在onItemLongClick方法中就可以通过view参数直接拿到了！
            convertView.setTag(R.id.alarm_clock_father_tv, groupPosition);//用父类布局中控件id做key
            convertView.setTag(R.id.content_layout, childPosition);//用子类布局中layout的id做key

        } else {
            childrenView = (HolderView) convertView.getTag();
        }

        childrenView.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_list.get(groupPosition).getList().remove(childPosition);
                notifyDataSetChanged();
                childrenView.swipeDragLayout.close();
            }
        });

        /**
         * 设置相应控件的内容
         */
        final String title = data_list.get(groupPosition).getList().get(childPosition).getTitle();
        // 设置标题上的文本信息
        childrenView.titleView.setText(title);
        // 设置副标题上的文本信息
        childrenView.descView.setText(data_list.get(groupPosition).getList().get(childPosition).getDesc());
        childrenView.content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "点击了" + title, Toast.LENGTH_SHORT).show();
            }
        });
//        childrenView.content_layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(context, "长按子项" + title, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        return convertView;
    }

    // 保存二级列表的视图类
    private class HolderView {
        LinearLayout content_layout;
        TextView titleView;
        TextView descView;
        SwipeDragLayout swipeDragLayout;
        TextView edit ,delete;
    }

    // 获取二级列表的数量
    @Override
    public int getChildrenCount(int arg0) {
        return data_list.get(arg0).getList().size();
    }

    // 获取一级列表的数据
    @Override
    public Object getGroup(int arg0) {
        return data_list.get(arg0);
    }

    // 获取一级列表的个数
    @Override
    public int getGroupCount() {
        return data_list.size();
    }

    // 获取一级列表的ID
    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }

    // 设置一级列表的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HodlerViewFather hodlerViewFather;
        if (convertView == null) {
            hodlerViewFather = new HodlerViewFather();
            convertView = mInflater.inflate(R.layout.activity_main_father, parent, false);
            hodlerViewFather.titlev = (TextView) convertView.findViewById(R.id.alarm_clock_father_tv);
            // 新建一个TextView对象，用来显示一级标签上的大体描述的信息
            hodlerViewFather.group_state = (ImageView) convertView.findViewById(R.id.group_state);
            convertView.setTag(hodlerViewFather);

            // 设置同getChildView一样
            convertView.setTag(R.id.alarm_clock_father_tv, groupPosition);
            convertView.setTag(R.id.content_layout, -1); //设置-1表示长按时点击的是父项，到时好判断。

        } else {
            hodlerViewFather = (HodlerViewFather) convertView.getTag();
        }
        // 一级列表右侧判断箭头显示方向
        if (isExpanded) {
            hodlerViewFather.group_state.setImageResource(R.mipmap.down);
        } else {
            hodlerViewFather.group_state.setImageResource(R.mipmap.right);
        }
        /**
         * 设置相应控件的内容
         */
        // 设置标题上的文本信息
        hodlerViewFather.titlev.setText(data_list.get(groupPosition).getTitle());

        // 返回一个布局对象
        return convertView;
    }

    // 定义一个 一级列表的view类
    private class HodlerViewFather {
        TextView titlev;
        ImageView group_state;
    }

    /**
     * 指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //设置子列表是否可选中
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
}
