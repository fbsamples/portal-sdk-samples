package com.meta.portal.sdk.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HeaderFirstViewHolder extends RecyclerView.ViewHolder {

    private final TextView mHeader;

    public HeaderFirstViewHolder(View itemView) {
        super(itemView);
        mHeader = (TextView) itemView.findViewById(R.id.header_text);
    }

    public void bind(final String header) {
        mHeader.setText(header);
    }

    public static HeaderFirstViewHolder newInstance(final ViewGroup parent) {
        return new HeaderFirstViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.header_first, parent, false));
    }
    
}


