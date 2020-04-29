package com.nurzainpradana.androidfundamental.mypreloaddata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nurzainpradana.androidfundamental.mypreloaddata.adapter.MahasiswaAdapter;
import com.nurzainpradana.androidfundamental.mypreloaddata.database.MahasiswaHelper;
import com.nurzainpradana.androidfundamental.mypreloaddata.model.MahasiswaModel;
import com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService.CANCEL_MESSAGE;
import static com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService.FAILED_MESSAGE;
import static com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService.PREPARATION_MESSAGE;
import static com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService.SUCCESS_MESSAGE;
import static com.nurzainpradana.androidfundamental.mypreloaddata.service.DataManagerService.UPDATE_MESSAGE;

public class MainActivity extends AppCompatActivity implements HandlerCallback {
    Messenger mBoundService;
    boolean mServiceBound;

    private ProgressBar progressBar;

    /* Service connection adalah interface yang digunakan untuk menghubungkan antara boundservice
    dengan activity
     */

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = new Messenger(service);
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);

        //ikat dan lepaskan kelas DataManagerService
        Intent mBoundServiceIntent = new Intent(MainActivity.this, DataManagerService.class);
        Messenger mActivityMessengger = new Messenger(new IncomingHandler(this));
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessengger);

        bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void updateProgress(long progress) {
        Log.e("PROGRESS", "updateProgress: " + progress);
        progressBar.setProgress((int) progress);
    }

    @Override
    public void loadSuccess() {
        Toast.makeText(this, "BERHASIL", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, MahasiswaActivity.class));
        finish();
    }

    @Override
    public void loadFailed() {
        Toast.makeText(this, "GAGAL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreparation(){
        Toast.makeText(this, "MEMULAI MEMUAT DATA", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadCancel() {
        finish();
    }

    private static class IncomingHandler extends Handler {
        final WeakReference<HandlerCallback> weakCallback;

        IncomingHandler(HandlerCallback callback) {
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case PREPARATION_MESSAGE:
                    weakCallback.get().onPreparation();
                    break;
                case UPDATE_MESSAGE:
                    Bundle bundle = msg.getData();
                    long progress = bundle.getLong("KEY_PROGRESS");
                    weakCallback.get().updateProgress(progress);
                    break;
                case SUCCESS_MESSAGE:
                    weakCallback.get().loadSuccess();
                    break;
                case FAILED_MESSAGE:
                    weakCallback.get().loadFailed();
                    break;
                case CANCEL_MESSAGE:
                    weakCallback.get().loadCancel();
                    break;

            }
        }
    }
}

interface HandlerCallback {
    void onPreparation();

    void updateProgress(long progress);

    void loadSuccess();

    void loadFailed();

    void loadCancel();
}
