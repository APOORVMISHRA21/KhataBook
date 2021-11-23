package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;

import java.util.ArrayList;

import Models.AssetEntry;


public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private String TAG = "InvoiceAdapter";
    private ArrayList<AssetEntry> entryList;
    private Context context;

    public AssetAdapter(ArrayList<AssetEntry> entryList, Context context){
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_asset_liability_tab,
                parent, false);
        AssetAdapter.ViewHolder vh = new AssetAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetAdapter.ViewHolder holder, int position) {
        AssetEntry entry = entryList.get(position);
        Log.e(TAG, "Entry : " + entry.getName() + "  " + entry.getDescription() + "  " + entry.getValue());
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

