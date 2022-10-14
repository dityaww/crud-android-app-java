package com.example.kota;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listHotel;
    private int idLaundry;
    private List<DataModel> listLaundry;
    public AdapterData(Context ctx, List<DataModel> listHotel) {
        this.ctx = ctx;
        this.listHotel = listHotel;
    }
    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm =listHotel.get(position);
        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
        holder.tvTelepon.setText(dm.getTelepon());
    }
    @Override
    public int getItemCount() {
        return listHotel.size();
    }
    public class HolderData extends RecyclerView.ViewHolder{
        TextView tvId, tvNama, tvAlamat, tvTelepon;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvTelepon = itemView.findViewById(R.id.tv_telepon);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih operasi yang akan dilakukan!");
                    dialogPesan.setCancelable(true);
                    idLaundry = Integer.parseInt(tvId.getText().toString());
                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            ((MainActivity)ctx).retrieveData();
                        }
                    });
                    dialogPesan.setNegativeButton("ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });
                    dialogPesan.show();
                    return false;
                }
            });
        }
        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idLaundry);
            hapusData.enqueue(new Callback<ResponseModel>() {
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
        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idLaundry);
            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listLaundry = response.body().getData();
                    int varIdLaundry = listLaundry.get(0).getId();
                    String varNamaLaundry = listLaundry.get(0).getNama();
                    String varAlamatLaundry = listLaundry.get(0).getAlamat();
                    String varTeleponLaundry = listLaundry.get(0).getTelepon();
                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("xId", varIdLaundry);
                    kirim.putExtra("xNama", varNamaLaundry);
                    kirim.putExtra("xAlamat", varAlamatLaundry);
                    kirim.putExtra("xTelepon", varTeleponLaundry);
                    ctx.startActivity(kirim);
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                }
            });
        }
    }
}