package simoncherry.jewelryui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import simoncherry.jewelryui.R;

/**
 * Created by Simon on 2017/2/19.
 */

public class RingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int[] ringResId = {R.drawable.ring1, R.drawable.ring2, R.drawable.ring3, R.drawable.ring4, R.drawable.ring5};

    private Context mContext;

    public RingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ring,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int index = position % ringResId.length;
        ((MyViewHolder)holder).imageView.setImageResource(ringResId[index]);
    }

    @Override
    public int getItemCount() {
        return ringResId.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
