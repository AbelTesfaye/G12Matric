package armored.g12matrickapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.List;

import armored.g12matrickapp.R;

/**
 * Created by Falcon on 7/18/2017 :: 14:56 inside G12MatrickApp .
 * ALL RIGHTS RECEIVED!
 */

public class YearsGridAdapter extends ArrayAdapter<YearsGridStructure> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<YearsGridStructure> dataList;


    public YearsGridAdapter(Context context, int resource, List<YearsGridStructure> objects) {
        super(context , resource , objects);
        this.context = context;
        this.dataList = objects;
        layoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder{
        ArcProgress mainProgress;
    }

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.years_grid_item , null);

            holder.mainProgress = (ArcProgress) view.findViewById(R.id.mainArcProgress);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.mainProgress.setBottomText(dataList.get(position).getSubject_name());
        holder.mainProgress.setProgress((int)(dataList.get(position).getYear_complition() * 100));

        return view;
    }
}
