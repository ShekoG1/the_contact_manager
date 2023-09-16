package com.theshekharmaharaj.contactmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<ContactModel> contactsList;

    public ContactsAdapter(List<ContactModel> contactsList) {
        this.contactsList = contactsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView phoneNumberTextView;
        public TextView birthdayTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            phoneNumberTextView = itemView.findViewById(R.id.textViewPhoneNumber);
            birthdayTextView = itemView.findViewById(R.id.textViewBirthday);
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
        holder.nameTextView.setText(contact.getName());
        holder.emailTextView.setText(contact.getEmail());
        holder.phoneNumberTextView.setText(contact.getPhoneNumber());
        holder.birthdayTextView.setText(contact.getBirthday());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}

