package com.example.btlandroid.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlandroid.R;
import com.example.btlandroid.model.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<Message> messageList;
    private String currentUserId;

    public ChatAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.senderId.equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message current = messageList.get(position);
        Message previous = (position > 0) ? messageList.get(position - 1) : null;

        boolean showTime = true;
        if (previous != null) {
            long diff = current.timestamp - previous.timestamp;
            showTime = diff >= 60000; // 60.000 ms = 1 phút
        }

        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentViewHolder) holder).bind(current, showTime);
        } else {
            ((ReceivedViewHolder) holder).bind(current, showTime);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;

        SentViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtSentMessage);
            txtTime = itemView.findViewById(R.id.txtSentTime);
        }

        void bind(Message message, boolean showTime) {
            txtMessage.setText(message.text);
            if (showTime) {
                txtTime.setVisibility(View.VISIBLE);
                txtTime.setText(DateFormat.format("HH:mm", message.timestamp));
            } else {
                txtTime.setVisibility(View.GONE);
            }
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;

        ReceivedViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtReceivedMessage);
            txtTime = itemView.findViewById(R.id.txtReceivedTime);
        }

        void bind(Message message, boolean showTime) {
            txtMessage.setText(message.text);
            if (showTime) {
                txtTime.setVisibility(View.VISIBLE);
                txtTime.setText(DateFormat.format("HH:mm", message.timestamp));
            } else {
                txtTime.setVisibility(View.GONE);
            }
        }
    }
}
