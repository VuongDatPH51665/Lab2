package com.example.lab2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.R;
import com.example.lab2.dao.TodoDAO;
import com.example.lab2.models.Todo;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Todo> todoArrayList;
    private TodoDAO todoDAO;
    private Todo.UpdateInterFace updateInterFace;
    public TodoAdapter(Context context, ArrayList<Todo> todoArrayList, TodoDAO todoDAO, Todo.UpdateInterFace updateInterFace ) {
        this.context = context;
        this.todoArrayList = todoArrayList;
        this.todoDAO=todoDAO;
        this.updateInterFace=updateInterFace;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_item_todolist,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo=todoArrayList.get(position);
        holder.tvContent.setText(todo.getContent());
        holder.tvDate.setText(todo.getDate());
        if (todo.getStatus()==1){
            holder.chkStatus.setChecked(true);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.chkStatus.setChecked(false);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=todo.getId();
                boolean check= todoDAO.deleteTodo(id);
                if (check) {
                    todoArrayList.remove(holder.getAdapterPosition());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    });
                    updateInterFace.deleteTodoList(todo);
                }
            }
        });
        holder.chkStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = todo.getId();
                boolean check = todoDAO.updateStatus(id, isChecked);
                if (check) {
                    Toast.makeText(context, "Update status thành công", Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            todoArrayList.clear();
                            todoArrayList.addAll(todoDAO.getAllTodo());
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    Toast.makeText(context, "Update status thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInterFace.updateTodoList(todo);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInterFace.addOnClickButton(todo);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (todoArrayList!=null){
            return todoArrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        TextView tvDate;
        CheckBox chkStatus;
        ImageView imgAdd,imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent=itemView.findViewById(R.id.tvContent);
            tvDate=itemView.findViewById(R.id.tvDate);
            chkStatus=itemView.findViewById(R.id.chkStatus);
            imgAdd=itemView.findViewById(R.id.img_add);
            imgDelete=itemView.findViewById(R.id.img_delete);
        }
    }
}
