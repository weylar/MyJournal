package com.softacles.myjournal;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.MyViewHolder> {
    private Context mContext;
    private List<Journal> journalList;
    private OnItemClickListener mListener;

    public JournalAdapter(Context mContext, OnItemClickListener listener) {
        this.mContext = mContext;
        this.journalList = new ArrayList<>();
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, content, preview;
        LinearLayout viewForeground;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.content);
            preview = view.findViewById(R.id.preview);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = journalList.get(getAdapterPosition()).getId();
            mListener.onItemClick(elementId);

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JournalAdapter.MyViewHolder holder, final int position) {
        Journal journal = journalList.get(position);// Gets the item position
        holder.title.setText(journal.getTitle());
        holder.content.setText(journal.getContent());
        if (journal.getTitle().length() != 0) {
            holder.preview.setText(journal.getTitle().substring(0, 1));
        }
        if (journal.getTitle().length() < 5) {
            holder.preview.setBackgroundResource(R.drawable.round_background_accent);
        } else if (journal.getTitle().length() >= 5 && journal.getTitle().length() < 10) {
            holder.preview.setBackgroundResource(R.drawable.round_background_yellow);
        } else if (journal.getTitle().length() >= 10) {
            holder.preview.setBackgroundResource(R.drawable.round_background_red);
        }

    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public void restoreItem(Journal journal, int position, View emptyView) {
        journalList.add(position, journal);
        emptyView.setVisibility(View.GONE);
        notifyItemChanged(position);
    }

    public void removeAllItem(View emptyView) {
        journalList.clear();
        emptyView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    public void updateJournal(List<Journal> journals) {
        journalList = journals;
        notifyDataSetChanged();
    }

    public List<Journal> getJournalList() {
        return journalList;
    }

    public interface OnItemClickListener {
        void onItemClick(int id);
    }
}

