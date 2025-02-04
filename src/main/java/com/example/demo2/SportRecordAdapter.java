package com.example.demo2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SportRecordAdapter extends RecyclerView.Adapter<SportRecordAdapter.SportRecordViewHolder> {

    private List<SportRecord> sportRecordList;

    public SportRecordAdapter(List<SportRecord> sportRecordList) {
        this.sportRecordList = sportRecordList;
    }

    @NonNull
    @Override
    public SportRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载列表项布局，这里使用 Android 自带的 simple_list_item_2 布局，它包含两个 TextView
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new SportRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportRecordViewHolder holder, int position) {
        // 获取当前位置的运动记录数据
        SportRecord record = sportRecordList.get(position);
        // 设置第一个 TextView 显示日期
        holder.text1.setText(record.getDate());
        // 设置第二个 TextView 显示运动类型和运动时间
        holder.text2.setText("运动类型: " + record.getSportType() + "，运动时间: " + record.getTime() + " 分钟");
    }

    @Override
    public int getItemCount() {
        // 返回运动记录列表的大小
        return sportRecordList.size();
    }

    // 自定义的 ViewHolder 类，用于缓存列表项视图中的控件
    static class SportRecordViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        SportRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化第一个 TextView
            text1 = itemView.findViewById(android.R.id.text1);
            // 初始化第二个 TextView
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}