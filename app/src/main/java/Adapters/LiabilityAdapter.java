package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;

import java.util.ArrayList;

import Models.AssetEntry;

public class LiabilityAdapter extends RecyclerView.Adapter<LiabilityAdapter.ViewHolder> {

    private String TAG = "InvoiceAdapter";
    private ArrayList<AssetEntry> entryList;
    private Context context;

    public LiabilityAdapter(ArrayList<AssetEntry> entryList, Context context){
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public LiabilityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_asset_liability_tab, parent, false);
        LiabilityAdapter.ViewHolder vh = new LiabilityAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LiabilityAdapter.ViewHolder holder, int position) {
        AssetEntry entry = entryList.get(position);
        holder.mName.setText(entry.getName());
        holder.mDesc.setText(entry.getDescription());
        holder.mWorth.setText(entry.getValue());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName, mDesc, mWorth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.asset_name);
            mDesc  = itemView.findViewById(R.id.asset_desc);
            mWorth = itemView.findViewById(R.id.asset_worth);
        }
    }
}

