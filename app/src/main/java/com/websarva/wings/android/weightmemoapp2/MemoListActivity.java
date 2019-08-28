package com.websarva.wings.android.weightmemoapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoListActivity extends AppCompatActivity {

    ListView lvWeightMemo;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        // 画面部品「lvWeightMemo」を表示場所に取得。
        lvWeightMemo = findViewById(R.id.lvWeightMemo);

        // データベース接続オブジェクト。
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        try {
            String sql = "SELECT * FROM weightmemo";
            Cursor cursor = db.rawQuery(sql, null);

            // adapter準備（第4，5引数）
            String[] from = {"weight","date"};
            int[] to = {android.R.id.text1, android.R.id.text2};

            adapter = new SimpleCursorAdapter(
                    MemoListActivity.this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    from,
                    to,
                    0);

            lvWeightMemo.setAdapter(adapter);

        } finally {
            db.close();
        }

        // contextmenu
        registerForContextMenu(lvWeightMemo);

    }

    //OptionMemu表示メソッド（本体はxml）
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //メニューインフレーターの取得。
        MenuInflater inflater = getMenuInflater();

        // OptionMenu用.xmlファイル(= menu_options_menu_list.xml)をinflate(膨らませる)
        inflater.inflate(R.menu.menu_options_menu_list, menu);

        //親クラスの同名メソッドを呼び出し、その戻り値を返却。
        return super.onCreateOptionsMenu(menu);
    }

    // OptionMemu選択時処理メソッド
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        //選択されたメニューのIDを追加
        int itemId = item.getItemId();
        switch (itemId) {
            //入力画面が選択された場合の処理。
            case R.id.menuListOptionNyuryoku:
                Intent intent = new Intent(MemoListActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.menuListOptionMemo:
                Intent intent2 = getIntent();
                overridePendingTransition(0, 0);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

                overridePendingTransition(0, 0);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    // ContextMenu表示メソッド
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        //親クラスの同名メソッドの呼び出し
        super.onCreateContextMenu(menu, view, menuInfo);

        //メニューインフレーターの取得。
        getMenuInflater().inflate(R.menu.memo_context_memo_list, menu);

        //コンテキストメニューのヘッドタイトルを設定。
        menu.setHeaderTitle(R.string.memo_list_context_header);
    }

    // ContextMenu選択時処理
    @Override
    public boolean onContextItemSelected(MenuItem item){

        //長押しされたビューに関する情報が格納されたオブジェクトを用意。
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // infoのidメソッドで"_id"を取得。
        int listPosition = (int)info.id;

        switch (item.getItemId()){
            // [削除] 選択時の処理
            case R.id.memoListContextDelete:

                // ファイル削除
                // データベース接続オブジェクト。
                SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

                try {
                    String sqlDelete = "DELETE FROM weightmemo WHERE _id = ?";
                    SQLiteStatement stmt = db.compileStatement(sqlDelete);
                    stmt.bindLong(1, listPosition);
                    stmt.executeUpdateDelete();

                } finally {
                    db.close();
                }

                // 「削除しました」トーストを表示
                Toast.makeText(MemoListActivity.this, R.string.msg_del, Toast.LENGTH_LONG).show();

                // ListView のデータ変更を表示に反映
                // (ActivityA,B間でA画面からB画面を立ち上げB画面をfinish()しA画面を再読み込みする)

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

                overridePendingTransition(0, 0);
                startActivity(intent);

                break;
        }
        return super.onContextItemSelected(item);
    }

}
