package com.example.admin.mynotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mynotes.model.TrashNoteMdl;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class TrashListAdapter extends RecyclerView.Adapter<TrashListAdapter.ItemViewHolder> {

    private static final String TAG = "TrashListAdapter";
    private LayoutInflater inflater;
    private List<TrashNoteMdl> trashNotes;
    ItemClickListener listener;
    private SparseBooleanArray selectedItems;


    public TrashListAdapter(Context context, List<TrashNoteMdl> trashNotes,
                            ItemClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.trashNotes = trashNotes;
        this.listener = clickListener;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.list_item, viewGroup, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bindTo(trashNotes.get(position));
        workDamnIt(holder);
        checkedIcon(holder);
        holder.itemView.setActivated(selectedItems.get(position, false));
    }


    @Override
    public int getItemCount() {
        return trashNotes.size();
    }

    private void workDamnIt(final ItemViewHolder holder) {
        final int position = holder.getAdapterPosition();
        holder.info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != NO_POSITION) {
                    Log.d(TAG, "INFO position: " + String.valueOf(position));
                    listener.viewInfo(v, position);
                }
            }
        });

        holder.icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemSelect(position);
            }
        });
    }

    private void checkedIcon(ItemViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (position != NO_POSITION) {
            Log.d(TAG, "ICON position: " + String.valueOf(position));
            if (selectedItems.get(position, false)) {
                holder.row_select.setVisibility(View.VISIBLE);
                Log.e(TAG, "ICON" + String.valueOf(position) + "unchecked: " + selectCount());
            } else {
                holder.row_select.setVisibility(View.INVISIBLE);
                Log.e(TAG, "ICON" + String.valueOf(position) + "checked: " + selectCount());
            }
        }
    }

    public void clearSelected() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int selectCount() {
        return selectedItems.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public interface ItemClickListener {
        void viewInfo(View view, int position);

        void itemSelect(int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView title_initial;
        public ImageView letter_plate;
        public ImageView row_select;
        public TextView start_date;
        public TextView short_desc;
        public FrameLayout icon_layout;
        public ConstraintLayout info_layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_t);
            title_initial = itemView.findViewById(R.id.title_initial_t);
            letter_plate = itemView.findViewById(R.id.letter_plate_t);
            start_date = itemView.findViewById(R.id.start_date_here);
            short_desc = itemView.findViewById(R.id.short_descript_t);
            icon_layout = itemView.findViewById(R.id.icon_layout);
            info_layout = itemView.findViewById(R.id.info_layout);
            row_select = itemView.findViewById(R.id.item_row_select);
        }

        public void bindTo(TrashNoteMdl note) {
            title.setText(note.title);
            title_initial.setText(note.title.substring(0, 1));
            letter_plate.setColorFilter(note.color);
            start_date.setText(note.getDate_created());
            short_desc.setText(note.getDescription());
        }
    }
}
