package Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.CaraouselActivity;
import com.example.khatabook.InventoryDetails;
import com.example.khatabook.MainActivity;
import com.example.khatabook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Inventory;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder>{

    private ArrayList<Inventory> productList;
    private Context context;
    private String TAG = "InventoryAdapter";
    private String Parentkey;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference().child("inventory");


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
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewDialog alert = new ViewDialog();
                alert.showDialog(context,String.valueOf(inventory.getQuantity()), String.valueOf(inventory.getProduct_name()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mProductName, mQuantity, mValue;
        ImageView mEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProductName = itemView.findViewById(R.id.product_name);
            mQuantity = itemView.findViewById(R.id.quantity);
            mValue = itemView.findViewById(R.id.value);
            mEdit = itemView.findViewById(R.id.go_to_edit_inventory);
        }
    }


        public class ViewDialog{
            public void showDialog(Context activity, String msg, String name){
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
//                dialog.setTitle("Edit Item");
                dialog.setContentView(R.layout.edit_inventory);

                EditText text = (EditText) dialog.findViewById(R.id.quantityUpdate);
                text.setText(msg);

                Button updateButton = dialog.findViewById(R.id.button_save_user_data);
                Button dialogButton = (Button) dialog.findViewById(R.id.button_cancel_user_data);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("inventory");
                       databaseReference
                               .orderByChild("product_name")
                               .equalTo(name)
                               .addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                                            Parentkey =snapshot1.getKey();
                                            Map<String, Object> taskMap = new HashMap<String, Object>();
                                            taskMap.put("quantity", Integer.parseInt(String.valueOf(text.getText())));
                                            FirebaseDatabase.getInstance().getReference("inventory").child(Parentkey).updateChildren(taskMap);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                }

                        );

                        dialog.dismiss();
                        Intent startIntent = new Intent(context, InventoryDetails.class);
                        context.startActivity(startIntent);
                        if(context instanceof  Activity)
                        {
                            ((Activity)context).finish();
                        }
                    }
                });

                dialog.show();

            }

        }



}
