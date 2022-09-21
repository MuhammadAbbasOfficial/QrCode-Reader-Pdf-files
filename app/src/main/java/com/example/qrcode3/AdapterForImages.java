package com.example.qrcode3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.core.utilities.ParsedUrl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterForImages extends RecyclerView.Adapter<AdapterForImages.viewHolderClass> {

    ArrayList<String> list;
    Context context;

    public AdapterForImages(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_images_layout,parent,false );
        return new viewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderClass holder, int position) {

        String getUrl = list.get(position);
        Glide.with(context).load(getUrl).into(holder.myImage);

        /*try {
            URL url = new URL(getUrl);
            System.out.println("----------------------------------------"+url+"--------------------------------------------link1");

            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            System.out.println("----------------------------------------"+bitmap+"--------------------------------------------link2");

            holder.myImage.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolderClass extends RecyclerView.ViewHolder {
        ImageView myImage;
        public viewHolderClass(@NonNull View itemView) {
            super(itemView);

            myImage = itemView.findViewById(R.id.myimage);


        }

    }
}
