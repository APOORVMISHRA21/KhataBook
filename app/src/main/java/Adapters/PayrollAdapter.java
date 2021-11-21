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

import Models.Payroll;

public class PayrollAdapter extends FirebaseRecyclerAdapter<Payroll,PayrollAdapter.payrollViewholder> {

    int totalPayment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("payRollAmount");

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PayrollAdapter(@NonNull FirebaseRecyclerOptions<Payroll> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PayrollAdapter.payrollViewholder holder, int position, @NonNull Payroll model) {

        holder.designation.setText(model.getDepartment());
        holder.noOfEmployees.setText(String.valueOf(model.getEmployees()));
        holder.totalDepartment.setText(String.valueOf(model.getEmployees() * model.getSalary()));
        totalPayment = totalPayment + model.getEmployees() * model.getSalary();
        myref.setValue(String.valueOf(totalPayment));
    }

    @NonNull
    @Override
    public payrollViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payroll_item,parent,false);
        return new PayrollAdapter.payrollViewholder(view);
    }



    class  payrollViewholder extends RecyclerView.ViewHolder{
        TextView designation,noOfEmployees,totalDepartment;
        public payrollViewholder(@NonNull View itemView) {
            super(itemView);
            designation = itemView.findViewById(R.id.designation);
            noOfEmployees = itemView.findViewById(R.id.noEmployee);
            totalDepartment = itemView.findViewById(R.id.totalDepart);
        }
    }
}


