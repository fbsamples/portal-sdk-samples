package com.meta.portal.sdk.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;

public class HeaderSecondViewHolder extends RecyclerView.ViewHolder {

    private final TextView mHeader;

    public HeaderSecondViewHolder(View itemView) {
        super(itemView);
        mHeader = (TextView) itemView.findViewById(R.id.header_text);
    }

    public void bind(final String header) {
        mHeader.setText(header);
    }

    public static HeaderSecondViewHolder newInstance(final ViewGroup parent) {
        return new HeaderSecondViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.header_second, parent, false));
    }

}


