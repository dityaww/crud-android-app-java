package com.example.kota;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNama, xAlamat, xTelepon;
    private EditText etNama, etAlamat, etTelepon;
    private Button btnUbah;
    private String yNama, yAlamat, yTelepon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);
        Intent terima = getIntent();
        xId = terima.getIntExtra("xId",-1);
        xNama = terima.getStringExtra("xNama");
        xAlamat = terima.getStringExtra("xAlamat");
        xTelepon = terima.getStringExtra("xTelepon");
        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etTelepon = findViewById(R.id.et_telepon);
        btnUbah = findViewById(R.id.btn_ubah);
        etNama.setText(xNama);
        etAlamat.setText(xAlamat);
        etTelepon.setText(xTelepon);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNama = etNama.getText().toString();
                yAlamat = etAlamat.getText().toString();
                yTelepon = etTelepon.getText().toString();
                updateData();
                Intent intent = new Intent(UbahActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(UbahActivity.this, "Update berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yNama, yAlamat, yTelepon);
        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
            }
        });
    }
}