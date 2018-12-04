package com.example.admin.mynotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mynotes.model.TrashNoteMdl;

import java.util.List;

public class TrashListAdapter extends RecyclerView.Adapter<TrashListAdapter.ItemViewHolder> {

    private LayoutInflater inflater;
    private List<TrashNoteMdl> trashNotes;

    public TrashListAdapter(Context context, List<TrashNoteMdl> trashNotes) {
        this.inflater = LayoutInflater.from(context);
        this.trashNotes = trashNotes;
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

    }

    @Override
    public int getItemCount() {
        return trashNotes.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView title_initial;
        public ImageView letter_plate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_t);
            title_initial = itemView.findViewById(R.id.title_initial_t);
            letter_plate = itemView.findViewById(R.id.letter_plate_t);
        }

        void bind_to(TrashNoteMdl note){
            title.setText(note.title);
            title_initial.setText(note.title.substring(0, 1));
            letter_plate.setColorFilter(note.color);
        }

    }
}
