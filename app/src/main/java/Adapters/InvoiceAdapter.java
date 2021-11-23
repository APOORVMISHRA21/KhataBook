package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khatabook.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Models.Invoice;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private String TAG = "InvoiceAdapter";
    private ArrayList<Invoice> invoicesList;
    private Context context;

    public InvoiceAdapter(ArrayList<Invoice> invoicesList, Context context){
        this.invoicesList = invoicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_invoice, parent, false);
        InvoiceAdapter.ViewHolder vh = new InvoiceAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.ViewHolder holder, int position) {

        Invoice invoice = invoicesList.get(position);

        Timestamp timestamp = new Timestamp(invoice.getTimestamp());
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String invoiceTime = timeFormat.format(date);
        String invoiceDate = dateFormat.format(date);

        Log.e(TAG, "invoiceTime : " + invoiceTime);
        Log.e(TAG, "invoiceDate : " + invoiceDate);

        holder.mCustomerName.setText(invoice.getCustomer_name());
        holder.mCustomerMail.setText(invoice.getCustomer_mail());
        holder.mProductName.setText(invoice.getProduct_name());

        StringBuilder quantity = new StringBuilder();
        quantity.append(invoice.getProduct_quantity());
        holder.mNoOfBoxes.setText(quantity.toString());

        StringBuilder discount = new StringBuilder();
        discount.append(invoice.getProduct_discount());
        discount.append("%");
        holder.mDiscount.setText(discount.toString());
        holder.mBillDate.setText(invoiceDate + "  " + invoiceTime);
        StringBuilder totalAmount = new StringBuilder();
        totalAmount.append("Rs. ");
        totalAmount.append(invoice.getTotal_amount());

        holder.mTotalAmount.setText(totalAmount.toString());
    }

    @Override
    public int getItemCount() {
        return invoicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mCustomerName, mCustomerMail, mProductName,
            mBillDate, mNoOfBoxes, mDiscount, mTotalAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCustomerName = itemView.findViewById(R.id.invoice_customer_name);
            mCustomerMail = itemView.findViewById(R.id.invoice_customer_mail);
            mProductName  = itemView.findViewById(R.id.invoice_product_name);
            mBillDate     = itemView.findViewById(R.id.invoice_date);
            mNoOfBoxes    = itemView.findViewById(R.id.invoice_noOfBoxes);
            mDiscount     = itemView.findViewById(R.id.invoice_discount);
            mTotalAmount  = itemView.findViewById(R.id.invoice_amount);
        }
    }
}
