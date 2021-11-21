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

import Models.Inventory;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{

    private ArrayList<Inventory> productList;
    private Context context;
    private String TAG = "InventoryAdapter";

    public InventoryAdapter(ArrayList<Inventory> productList, Context context) {
        this.productList = productList;
        this.context = context;

        Log.e(TAG, productList.size() + "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_inventory, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Inventory inventory = productList.get(position);

        String quantity = inventory.getQuantity() + " Boxes";
        String worth = "Rs. " + inventory.getCp() * inventory.getQuantity();

        holder.mProductName.setText(inventory.getProduct_name());
        holder.mQuantity.setText(quantity);
        holder.mValue.setText(worth);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mProductName, mQuantity, mValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProductName = itemView.findViewById(R.id.product_name);
            mQuantity = itemView.findViewById(R.id.quantity);
            mValue = itemView.findViewById(R.id.value);
        }
    }

}
