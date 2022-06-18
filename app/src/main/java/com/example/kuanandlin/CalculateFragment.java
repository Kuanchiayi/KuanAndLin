package com.example.kuanandlin;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalculateFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    Adapter adapter;
    SQLiteDatabase db;
    FloatingActionButton fab;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String, String> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calculate, container, false);
        initViews();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBaseHelper helper = new DBaseHelper(getActivity(), "DB", null, 1);
        db = helper.getReadableDatabase();

        query_on_All();

        fab.setOnClickListener(view1 -> {
            onCreateDialog();
        });
    }

    private void initViews(){
        fab = view.findViewById(R.id.action_button);
        recyclerView = view.findViewById(R.id.recyclerView_cal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new CalculateFragment.Adapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
    }

    private void InsertDB(String name, String price){
//        final Calendar cal = Calendar.getInstance();
//        final CharSequence time = DateFormat.format("yyyy-MM-dd kk:mm:ss", cal.getTime());

        ContentValues cv = new ContentValues();
        /*  放入發布者  */
        cv.put("name", name);
        /*   放入價錢  */
        cv.put("price", price);
        db.insert("Menu", null, cv);
    }

    private void query_on_All(){
        arrayList.clear();
        Cursor c = db.query("Menu", null, null, null, null, null, null);
        while (c.moveToNext()) {
            data = new HashMap<>();
            data.put("name", c.getString(1));
            data.put("price", c.getString(2));
            arrayList.add(data);
        }
        c.close();
        Log.e("menu", arrayList.toString() + "a" + String.valueOf(arrayList.size()));
        /*
            更新adapter
        * */
        adapter.notifyDataSetChanged();
    }

    private void onCreateDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.add_menu, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // 使用setView()方法將佈局顯示到dialog
        alertDialog.setView(view);
        EditText et_name = (EditText) view.findViewById(R.id.et_name);
        EditText et_price = (EditText) view.findViewById(R.id.et_price);

        alertDialog
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InsertDB(String.valueOf(et_name.getText()), String.valueOf(et_price.getText()));
                                query_on_All();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }
    public class Adapter extends RecyclerView.Adapter<CalculateFragment.Adapter.ViewHolder>{

        private Context context;
        public ArrayList<HashMap<String,String>> arrayList;
        public Adapter(Context context, ArrayList<HashMap<String,String>> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_name, tv_count, tv_price;
            public ViewHolder(View itemsView){
                super(itemsView);
                tv_name = itemsView.findViewById(R.id.tv_name);
                tv_price = itemsView.findViewById(R.id.tv_price);
                tv_count = itemsView.findViewById(R.id.tv_count);
            }
        }

        @NonNull
        @Override
        public CalculateFragment.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_cal_items, parent, false);
            return new CalculateFragment.Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CalculateFragment.Adapter.ViewHolder holder, int position) {
            HashMap<String,String> data = arrayList.get(position);
            holder.tv_name.setText(data.get("name"));
            holder.tv_price.setText(data.get("price"));
            holder.tv_count.setText(data.get("count"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}