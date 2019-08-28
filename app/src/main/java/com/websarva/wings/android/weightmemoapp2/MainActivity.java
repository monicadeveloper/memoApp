package com.websarva.wings.android.weightmemoapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String _date = "";
    String _weight = "";

    Button _btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [保存button(ID:btnSave)]　を　変数(_btnSave)と結びつける。場所取得。
        _btnSave = findViewById(R.id.btnSave);

        EditText dateText = (EditText)findViewById(R.id.etDate);
        Time time = new Time("Asia/Tokyo");
        time.setToNow();
        String date = time.year + "年" + (time.month+1) + "月" + time.monthDay + "日　";
        dateText.setText(date);

    }

    // [保存] buttonがタップされたときの処理
    public void onSaveButtonClick(View view){

        //日付欄をfindViewByIdで取得→ [_Date] にStringで代入。
        EditText etDate = findViewById(R.id.etDate);
        String _date = etDate.getText().toString();

        //体重欄をfindViewByIdで取得→ [_Weight] にStringで代入。
        EditText etWeight = findViewById(R.id.etWeight);
        String _weight = etWeight.getText().toString();

        //　------------↓↓Database処理↓↓------------
        // DatabaseHelperObject「helper君」を誕生させる。引数は使うActivity。
        // → helper君のgetWritableDatabaseメソッドを有効化←これこそがDB接続オブジェクトだ！
        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        // DBへの保存処理（DELETE、INSERT）
        try {

            String sqlInsert = "INSERT INTO weightmemo (date, weight) VALUES(?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            stmt.bindString(1,_date);
            stmt.bindString(2,_weight);
            stmt.executeInsert();

        } finally {
            db.close();
        }

        //　------------↓↓Databaseとは関係無い処理↓↓------------
        //日付欄、体重欄の入力値を消去。
        etWeight.setText("kg");

        // [保存]buttonをタップできないように変更。↓いらない？？
        // _btnSave.setEnabled(false);

        // 「保存されました」のトーストを表示したい。
        Toast.makeText(MainActivity.this, R.string.toast_save, Toast.LENGTH_LONG).show();

    }

    //

    // [クリア]buttonがタップされた時の処理
    public void clear(View view) {

        //日付欄を取得。
        EditText etDate = findViewById(R.id.etDate);

        //体重欄を取得。
        EditText etWeight = findViewById(R.id.etWeight);

        //日付欄、体重欄の入力値を消去。
        etDate.setText("");
        etWeight.setText("");
    }

    //OptionMemuを表示させるメソッド（本体はxmlで）
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //メニューインフレーターの取得。
        MenuInflater inflater = getMenuInflater();

        // OptionMenu用.xmlファイル(= menu_options_menu_list.xml)をinflate(膨らませる)
        inflater.inflate(R.menu.menu_options_menu_list, menu);

        //親クラスの同名メソッドを呼び出し、その戻り値を返却。
        return super.onCreateOptionsMenu(menu);
    }

    // OptionMemu選択時処理メソッドを追加
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //選択されたメニューのIDを追加
        int itemId = item.getItemId();
        switch (itemId){
            //一覧画面が選択された場合の処理。
            case R.id.menuListOptionMemo:
                Intent intent = new Intent(MainActivity.this, MemoListActivity.class);
                startActivity(intent);
                break;
            case R.id.menuListOptionNyuryoku:
                Intent intent2 = getIntent();
                overridePendingTransition(0, 0);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

                overridePendingTransition(0, 0);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

}
