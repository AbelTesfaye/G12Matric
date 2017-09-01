package armored.g12matrickapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import armored.g12matrickapp.R;

/**
 * Created by Falcon on 7/18/2017 :: 14:56 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class GridCatagoryAdapter extends ArrayAdapter<GridCatagoryStructure> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<GridCatagoryStructure> dataList;


    public GridCatagoryAdapter(Context context, int resource, List<GridCatagoryStructure> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataList = objects;
        layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        FrameLayout rippleFrame;
        AppCompatTextView Subject_name;
        AppCompatImageView Subject_vector;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.grid_item , null);

            holder.Subject_name = (AppCompatTextView) view.findViewById(R.id.mainSubjectName);
            holder.Subject_vector = (AppCompatImageView) view.findViewById(R.id.mainSubjectIcon);
            holder.rippleFrame = (FrameLayout) view.findViewById(R.id.rippleFrameLayout);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.Subject_name.setText(dataList.get(position).getSubjectName());
        holder.Subject_vector.setImageDrawable(ContextCompat.getDrawable(context , dataList.get(position).getResourceAddress()));
        holder.Subject_vector.setBackgroundColor(ContextCompat.getColor(context , dataList.get(position).getColorBg()));

        return view;
    }
}
