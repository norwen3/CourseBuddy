package com.example.coursebuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursebuddy.R;
import com.example.coursebuddy.iModels.OnItemClickListener;
import com.example.coursebuddy.models.MenuModel;

import java.util.ArrayList;

public class AppMenuAdapter extends RecyclerView.Adapter<AppMenuAdapter.Viewholder> {

    private Context context;
    private ArrayList<MenuModel> menuModelArrayList;
    private static OnItemClickListener onItemClickListener;


    public interface OnItemLongClickListener {

        boolean onItemSelected(int position, View view, MenuModel object);
    }

    public void addItems(ArrayList<MenuModel> menuModels) {
        menuModelArrayList.clear();
        menuModelArrayList.addAll(menuModels);
        notifyDataSetChanged();
    }

    public AppMenuAdapter(Context context, ArrayList<MenuModel> menuModelArrayList){
        this.context = context;
        this.menuModelArrayList = menuModelArrayList;
    }

    @NonNull
    @Override
    public AppMenuAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        return new Viewholder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AppMenuAdapter.Viewholder holder, int position) {
        MenuModel model = menuModelArrayList.get(position);
        holder.menuItemName.setText(model.getMenuItemName());
        holder.menuImage.setImageResource(model.getMenuImage());
    }


    @Override
    public int getItemCount() {
        return menuModelArrayList.size();
    }


    public final class Viewholder extends RecyclerView.ViewHolder{
        AppMenuAdapter appMenuAdapter;

        private ImageView menuImage;
        private TextView menuItemName;

        public Viewholder(@NonNull View itemView, AppMenuAdapter appMenuAdapter) {
            super(itemView);
            this.appMenuAdapter = appMenuAdapter;

            menuImage = itemView.findViewById(R.id.menuImage);
            menuItemName = itemView.findViewById(R.id.menuText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){

                        onItemClickListener.onItemSelected(getBindingAdapterPosition(),view,
                                menuModelArrayList.get(getBindingAdapterPosition()));
                    }
                }
            });


        }



    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        AppMenuAdapter.onItemClickListener = onItemClickListener;
    }


}
