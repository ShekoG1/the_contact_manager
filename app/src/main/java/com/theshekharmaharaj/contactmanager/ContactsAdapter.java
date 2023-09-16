package com.theshekharmaharaj.contactmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    /*public interface OnItemClickListener {
        void onItemClick(ContactModel contact);
    }*/
    private OnItemClickListener listener;
    private List<ContactModel> contactsList;
    private Context context;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ContactsAdapter(Context context, List<ContactModel> contactsList, OnItemClickListener listener) {
        this.context = context;
        this.contactsList = contactsList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView phoneNumberTextView;
        public TextView birthdayTextView;
        public TextView itemID;
        public LinearLayout contactItem;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            phoneNumberTextView = itemView.findViewById(R.id.textViewPhoneNumber);
            birthdayTextView = itemView.findViewById(R.id.textViewBirthday);
            contactItem = itemView.findViewById(R.id.contactItem);
            itemID = itemView.findViewById(R.id.itemID);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactModel contact = contactsList.get(position);
        holder.nameTextView.setText(contact.getId()+" - "+contact.getName() );
        holder.emailTextView.setText(contact.getEmail());
        holder.phoneNumberTextView.setText(contact.getPhoneNumber());
        holder.birthdayTextView.setText(contact.getBirthday());
        holder.itemID.setText(String.valueOf(contact.getId()));

        // Set an OnClickListener for the item
        holder.contactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}

