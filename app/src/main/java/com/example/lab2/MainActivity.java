package com.example.lab2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.adapter.TodoAdapter;
import com.example.lab2.dao.TodoDAO;
import com.example.lab2.models.Todo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edtContent,edtDate,edtTitle,edtType;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<Todo> todoArrayList;
    private TodoAdapter adapter;
    private int checkid=-1;
     FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        TodoDAO todoDAO=new TodoDAO(this);
        todoArrayList= (ArrayList<Todo>) todoDAO.getAllTodo();
        adapter=new TodoAdapter((Context) this,todoArrayList,todoDAO, (Todo.UpdateInterFace) MainActivity.this);
        recyclerView.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=edtTitle.getText().toString();
                String content=edtContent.getText().toString();
                String date=edtDate.getText().toString();
                String type=edtType.getText().toString();
                if(!title.isEmpty()&&!content.isEmpty()&&!date.isEmpty()&&!type.isEmpty()) {
                    Todo todo=new Todo(title,content,date,type,0);
                    if (todoDAO.insertTodo(todo)){
                        Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        todoArrayList.add(todo);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void init(){
        edtContent=findViewById(R.id.edtContent);
        edtDate=findViewById(R.id.edtDate);
        edtTitle=findViewById(R.id.edtTitle);
        edtType=findViewById(R.id.edtType);
        btnAdd=findViewById(R.id.btnAdd);
        recyclerView=findViewById(R.id.recyclerview);
    }

    @Override
    public void addOnClickButton(Todo todo) {
        edtTitle.setText(todo.getTitle());
        edtContent.setText(todo.getContent());
        edtDate.setText(todo.getDate());
        edtType.setText(todo.getType());

    }

//    @Override
//    public void updateTodoList(Todo todo) {
//        if (todo != null&&checkid==todo.getId()) {
////            edtTitle.setText(todo.getTitle());
////            edtContent.setText(todo.getContent());
////            edtDate.setText(todo.getDate());
////            edtType.setText(todo.getType());
//
//            // Thực hiện cập nhật
//            String title = edtTitle.getText().toString();
//            String content = edtContent.getText().toString();
//            String date = edtDate.getText().toString();
//            String type = edtType.getText().toString();
//
//            todo.setTitle(title);
//            todo.setContent(content);
//            todo.setDate(date);
//            todo.setType(type);
//
//            TodoDAO todoDAO = new TodoDAO(this);
//            if (!title.isEmpty() && !content.isEmpty() && !date.isEmpty() && !type.isEmpty()) {
//                if (todoDAO.updateTodo(todo)) {
//                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                    int position = todoArrayList.indexOf(todo);
//                    todoArrayList.set(position, todo);
//                    adapter.notifyDataSetChanged();
//                    checkid=-1;
//                    xoa();
//                } else {
//                    Toast.makeText(MainActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else {
//                Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else {
//            Toast.makeText(this, "Id không khớp", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void deleteTodoList(Todo todo) {
        xoa();
    }

    public void xoa(){
        edtTitle.setText("");
        edtType.setText("");
        edtDate.setText("");
        edtContent.setText("");
    }
    private void listenFirebaseFirestore() {
        database.collection("students")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("MainActivity", "Listen failed.", error);
                            return;
                        }

                        if (value != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:
                                        Todo addedStudent = dc.getDocument().toObject(Todo.class);
                                        addedStudent.setId(dc.getDocument().getId());
                                        todoArrayList.add(addedStudent);
                                        TodoAdapter.notifyItemInserted(todoArrayList.size() - 1);

                                        break;

                                    case MODIFIED:
                                        Todo modifiedStudent = dc.getDocument().toObject(Todo.class);
                                        modifiedStudent.setId(dc.getDocument().getId());
                                        int modifiedIndex = getIndexById(dc.getDocument().getId());
                                        if (modifiedIndex != -1) {
                                            todoArrayList.set(modifiedIndex, modifiedStudent);
                                            TodoAdapter.notifyItemChanged(modifiedIndex);
                                        }
                                        break;

                                    case REMOVED:
                                        int removedIndex = getIndexById(dc.getDocument().getId());
                                        if (removedIndex != -1) {
                                            todoArrayList.remove(removedIndex);
                                            TodoAdapter.notifyItemRemoved(removedIndex);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    private int getIndexById(String id) {
        for (int i = 0; i < todoArrayList.size(); i++) {
            if (todoArrayList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

}