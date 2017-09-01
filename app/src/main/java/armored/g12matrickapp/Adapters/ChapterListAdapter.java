package armored.g12matrickapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import armored.g12matrickapp.R;

/**
 * Created by Falcon on 7/29/2017 :: 06:46 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class ChapterListAdapter extends ArrayAdapter<ChapterListStructure> {

private Context context;
private LayoutInflater layoutInflater;
private List<ChapterListStructure> dataList;


public ChapterListAdapter(Context context, int resource, List<ChapterListStructure> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataList = objects;
        layoutInflater = LayoutInflater.from(context);
        }

private class ViewHolder{
    AppCompatTextView chapter_address;
    AppCompatCheckBox chapter_needed;
}

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.chapters_list_item , null);

            holder.chapter_address = (AppCompatTextView) view.findViewById(R.id.chapternumber);
            holder.chapter_needed = (AppCompatCheckBox) view.findViewById(R.id.checkedItem);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.chapter_address.setText("Chapter " + dataList.get(position).getCapter_number());
        holder.chapter_needed.setChecked(dataList.get(position).isCapter_clicked());

        holder.chapter_needed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dataList.get(position).setCapter_clicked(b);
            }
        });

        return view;
    }

    public ArrayList<Integer> giveMeChoosedChapters(){

        ArrayList<Integer> chapters = new ArrayList<>();

        for(int i = 0; i < dataList.size();i++){
            if(dataList.get(i).isCapter_clicked()){
                chapters.add(dataList.get(i).getCapter_number());
            }
        }

        return chapters;
    }

}
