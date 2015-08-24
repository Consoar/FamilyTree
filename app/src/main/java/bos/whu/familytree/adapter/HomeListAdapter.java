package bos.whu.familytree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Iterator;
import java.util.List;

import bos.whu.familytree.R;
import bos.whu.familytree.model.PersonBean;
import bos.whu.familytree.support.utils.NumberFormat;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private List<PersonBean> list;
    Context context;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
        }
    };


    public HomeListAdapter(Context context, List<PersonBean> list) {
        this.list = list;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getFullname());
        if (list.get(position).getPhotourl()!=null) {
            Picasso.with(context).load(list.get(position).getPhotourl()).into(holder.image);
        } else {
            if (list.get(position).getSex()==1)
                holder.image.setImageResource(R.drawable.male_sill);
            else if (list.get(position).getSex()==2)
                holder.image.setImageResource(R.drawable.female_sill);
            else
                holder.image.setImageResource(R.drawable.unknown_sill);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getHeaderId(int position) {
        return list.get(position).getGgen();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setText("第"+ NumberFormat.formatInteger((int)list.get(position).getGgen())+"世代");
        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
        return Color.HSVToColor(150, new float[]{
                221, 221, 221
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.head);
        }
    }
}
