package info.krushik.android.myapplication10;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

//AsyncTaskLoader - работа в фоновом потоке, отличие от AsyncTask: возвращает результат не обращаясь повторно в БД
class StudentsLoader extends AsyncTaskLoader<List<Student>> {

    private Context mContext;
    private List<Student> Students;

    //Конструктор
    public StudentsLoader(Context context) {
        super(context);

        this.mContext = context;
    }

    //Работа в фоновом потоке
    @Override
    public List<Student> loadInBackground() {
        DataBaseHelper helper = new DataBaseHelper(mContext);//создаем helper

        return helper.getStudents();//вызываем метод все студенты
    }

    //Возврат результата
    @Override
    public void deliverResult(List<Student> data) {
        if (isReset()) {//проверка зарезечен
            return;
        }

        Students = data;

        if (isStarted()) {//проверка застартован
            super.deliverResult(data);
        }
    }

    //Старт
    @Override
    protected void onStartLoading() {
        if (Students != null) {//если студенты не пустые
            deliverResult(Students);//он их возвращает
        }

        if (takeContentChanged() || Students == null) {
            forceLoad();
        }
    }

    //Стоп
    @Override
    protected void onStopLoading() {
        cancelLoad();//останавливает работу
    }

    //Перезагрузка
    @Override
    protected void onReset() {
        onStopLoading();

        if (Students != null) {
            Students = null;//очищает массив студентов
        }
    }
}
