package com.samahmakki.companion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MedicineListAdapter extends BaseAdapter {
    private Context context;
    private int layout;

    private ArrayList<medicine> medicinelist;

    public MedicineListAdapter(Context context, int layout, ArrayList<medicine> medicinelist) {
        this.context = context;
        this.layout = layout;
        this.medicinelist = medicinelist;

    }

    @Override
    public int getCount() {
        return medicinelist.size();

    }

    @Override
    public Object getItem(int position) {
        return medicinelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.med_name);
            holder.imageView = row.findViewById(R.id.med_img);



            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        medicine med = medicinelist.get(position);
        holder.txtName.setText(med.getMedName());
        byte[] medimage = med.getMedimg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(medimage, 0, medimage.length);
        holder.imageView.setImageBitmap(bitmap);




        return row;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtName;

    }
}
