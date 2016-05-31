package info.krushik.android.myapplication10;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

class StudentsLoader extends AsyncTaskLoader<List<Student>> {

    private Context mContext;
    private List<Student> Students;

    public StudentsLoader(Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    public List<Student> loadInBackground() {
        Students = new ArrayList<Student>();

        Students.add(new Student());
        Students.add(new Student());

        return Students;
    }

    @Override
    public void deliverResult(List<Student> data) {
        if (isReset()) {
            return;
        }

        Students = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (Students != null) {
            deliverResult(Students);
        }

        if (takeContentChanged() || Students == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (Students != null) {
            Students = null;
        }
    }
}

