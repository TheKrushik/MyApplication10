package info.krushik.android.myapplication10;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Student>>{
    //реализация интерфейса LoaderCallbacks(можно писать в любом классе и обратится в этом классе)

    private ListView mListView;//объявим ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);//находим ListView
        getSupportLoaderManager().initLoader(0, null, this);//SupportLoaderManager()-поддержка старых версий v4
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
//                getLoaderManager().initLoader(0, null, this); //Вызов предыдущего результата(id, доп. параметры, контекст)
                break;
            case R.id.button2:
//                getLoaderManager().restartLoader(0, null, this); //Перезапуск, для изменений в базе
                break;
            case R.id.button3:
                break;
            case R.id.button4:
                break;
        }
    }

    //Создание Loader
    @Override
    public Loader<List<Student>> onCreateLoader(int id, Bundle args) {//(id-какой лоадер нкжно создать(БД, сеть, файл),
        return new StudentsLoader(this);
    }

    //Получение ответа Loader(возвращаются студенты)
    @Override
    public void onLoadFinished(Loader<List<Student>> loader, List<Student> data) {
        ArrayAdapter<Student> adapter = new ArrayAdapter<>(//список ArrayAdapter
                this,//контекст
                android.R.layout.simple_list_item_1,//разметка
                android.R.id.text1,//текст
                data//массив
        );
        mListView.setAdapter(adapter);//заполняем ListView данными adapter
    }

    //если Loader резетится, то мы можем на это прореагировать
    @Override
    public void onLoaderReset(Loader<List<Student>> loader) {

    }
}
