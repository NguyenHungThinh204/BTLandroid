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
import com.example.btlandroid.models.Message;

import java.util.Calendar;
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

        boolean showTime = true;
        boolean showDate = true;

        if (position > 0) {
            Message previous = messageList.get(position - 1);
            long timeDiff = current.timestamp - previous.timestamp;
            showTime = timeDiff >= 60000;

            // So sánh ngày để hiển thị line ngày
            Calendar cal1 = Calendar.getInstance();
            cal1.setTimeInMillis(previous.timestamp);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTimeInMillis(current.timestamp);
            showDate = cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)
                    || cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR);
        } else {
            // Tin nhắn đầu tiên luôn hiển thị ngày
            showDate = true;
        }

        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).bind(current, showTime, showDate);
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).bind(current, showTime, showDate);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime, txtDate;

        SentViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtSentMessage);
            txtTime = itemView.findViewById(R.id.txtSentTime);
            txtDate = itemView.findViewById(R.id.txtSentDate);
        }

        void bind(Message message, boolean showTime, boolean showDate) {
            txtMessage.setText(message.text);

            if (showTime) {
                txtTime.setVisibility(View.VISIBLE);
                txtTime.setText(DateFormat.format("HH:mm", message.timestamp));
            } else {
                txtTime.setVisibility(View.GONE);
            }

            if (showDate) {
                txtDate.setVisibility(View.VISIBLE);
                txtDate.setText(DateFormat.format("dd/MM/yyyy", message.timestamp));
            } else {
                txtDate.setVisibility(View.GONE);
            }
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime, txtDate;

        ReceivedViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtReceivedMessage);
            txtTime = itemView.findViewById(R.id.txtReceivedTime);
            txtDate = itemView.findViewById(R.id.txtReceivedDate);
        }

        void bind(Message message, boolean showTime, boolean showDate) {
            txtMessage.setText(message.text);

            if (showTime) {
                txtTime.setVisibility(View.VISIBLE);
                txtTime.setText(DateFormat.format("HH:mm", message.timestamp));
            } else {
                txtTime.setVisibility(View.GONE);
            }

            if (showDate) {
                txtDate.setVisibility(View.VISIBLE);
                txtDate.setText(DateFormat.format("dd/MM/yyyy", message.timestamp));
            } else {
                txtDate.setVisibility(View.GONE);
            }
        }
    }
}