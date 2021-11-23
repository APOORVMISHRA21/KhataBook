package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.expenditure;

public class ProfitLossAdapter extends FirebaseRecyclerAdapter<expenditure,ProfitLossAdapter.profitlossViewHolder> {
    int totalExpense;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("totalExpenseAmount");




    public ProfitLossAdapter(@NonNull FirebaseRecyclerOptions<expenditure> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull profitlossViewHolder holder, int position, @NonNull expenditure model) {

        holder.exDescription.setText(model.getDescription());
        holder.exName.setText(String.valueOf(model.getName()));
        holder.exValue.setText(String.valueOf(model.getValue()));
        totalExpense = totalExpense + Integer.parseInt(model.getValue());
        myref.setValue(String.valueOf(totalExpense));
    }

    @NonNull
    @Override
    public profitlossViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profitloss_item,parent,false);
        return new ProfitLossAdapter.profitlossViewHolder(view);
    }

    class  profitlossViewHolder extends RecyclerView.ViewHolder{
        TextView exName,exValue,exDescription;
        public profitlossViewHolder(@NonNull View itemView) {
            super(itemView);
            exName = itemView.findViewById(R.id.expenditureName);
            exValue = itemView.findViewById(R.id.expenditueValue);
            exDescription = itemView.findViewById(R.id.expenditureDescription);
        }
    }
}
