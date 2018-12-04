package com.example.admin.mynotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mynotes.model.TrashNoteMdl;

import java.util.ArrayList;
import java.util.List;

public class TrashListAdapter extends RecyclerView.Adapter<TrashListAdapter.ItemViewHolder> {

    private LayoutInflater inflater;
    private List<TrashNoteMdl> trashNotes;
    private ClickListener listener;
    private SparseBooleanArray selectedItems;

    public TrashListAdapter(Context context, List<TrashNoteMdl> trashNotes, ClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.trashNotes = trashNotes;
        this.listener = listener;
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
        holder.bind_to(trashNotes.get(position));
        holder.itemView.setActivated(selectedItems.get(position, false));
        applyCheckIcon(holder, position);
        clickEvents(holder);
    }

    @Override
    public int getItemCount() {
        return trashNotes.size();
    }

    private void clickEvents(final ItemViewHolder holder) {
        holder.icon_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.icon_cell(v, holder.getAdapterPosition());
            }
        });

        holder.note_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.note_cell(v, holder.getAdapterPosition());
            }
        });
    }

    private void applyCheckIcon(ItemViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.check_icon.setVisibility(View.INVISIBLE);
        } else {
            holder.check_icon.setVisibility(View.VISIBLE);
        }
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        trashNotes.remove(position);
        notifyDataSetChanged();
    }

    public interface ClickListener{
        void icon_cell(View view, int position);
        void note_cell(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title, title_initial;
        public ImageView letter_plate, check_icon;
        public FrameLayout icon_holder;
        public ConstraintLayout note_holder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_t);
            title_initial = itemView.findViewById(R.id.title_initial_t);
            letter_plate = itemView.findViewById(R.id.letter_plate_t);
            check_icon = itemView.findViewById(R.id.check_icon);
            icon_holder = itemView.findViewById(R.id.icon_cell);
            note_holder = itemView.findViewById(R.id.note_cell);
        }

        void bind_to(TrashNoteMdl note){
            title.setText(note.title);
            title_initial.setText(note.title.substring(0, 1));
            letter_plate.setColorFilter(note.color);
        }

    }
}
