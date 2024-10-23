package com.example.sharedpreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvCountDown, tvTapCount, tvHistory;
    private Button btnTap, btnStart;
    private int tapCount = 0;
    private int gameRound = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết với các thành phần giao diện
        tvCountDown = findViewById(R.id.tvCountDown);
        tvTapCount = findViewById(R.id.tvTapCount);
        tvHistory = findViewById(R.id.tvHistory);
        btnTap = findViewById(R.id.btnTap);
        btnStart = findViewById(R.id.btnStart);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("TapGamePrefs", MODE_PRIVATE);

        // Lấy lịch sử từ SharedPreferences và hiển thị
        displayHistory();

        // Xử lý sự kiện khi nhấn nút Tap
        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapCount++;
                tvTapCount.setText(String.valueOf(tapCount));
            }
        });

        // Xử lý sự kiện khi nhấn nút Start
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
    }

    private void startGame() {
        tapCount = 0;
        tvTapCount.setText("0");
        btnTap.setEnabled(true);
        btnStart.setEnabled(false);

        // Bộ đếm thời gian 10 giây
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                btnTap.setEnabled(false);
                btnStart.setEnabled(true);
                tvCountDown.setText("10");

                // Lưu kết quả của lượt chơi vào SharedPreferences
                saveGameResult(tapCount);
                displayHistory();  // Hiển thị lịch sử chơi
            }
        }.start();
    }

    private void saveGameResult(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("round_" + gameRound, count);
        editor.apply();
        gameRound++;
    }

    private void displayHistory() {
        StringBuilder history = new StringBuilder("History:\n");
        for (int i = 1; i <= sharedPreferences.getAll().size(); i++) {
            int taps = sharedPreferences.getInt("round_" + i, 0);
            history.append("Round ").append(i).append(": ").append(taps).append(" taps\n");
        }
        tvHistory.setText(history.toString());
    }
}
