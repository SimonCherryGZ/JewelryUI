package simoncherry.jewelryui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import simoncherry.jewelryui.R;

/**
 * Created by Simon on 2017/2/19.
 */

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    private Context mContext;
    private List<String> nameList;
    private List<String> priceList;

    public CheckOutAdapter(Context mContext, List<String> nameList, List<String> priceList) {
        this.mContext = mContext;
        this.nameList = nameList;
        this.priceList = priceList;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_check_out, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(nameList.get(position));
        holder.tvPrice.setText(priceList.get(position));
    }

    @Override public int getItemCount() {
        return nameList.size();
    }

    public void remove(int position) {
        nameList.remove(position);
        priceList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String name, String price, int position) {
        nameList.add(position, name);
        priceList.add(position, price);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
