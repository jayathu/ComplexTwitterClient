package com.codepath.apps.complextweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.complextweets.R;
import com.codepath.apps.complextweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jnagaraj on 2/28/16.
 */
public class UserListAdapter extends ArrayAdapter<User> {

    //Constructor
    public UserListAdapter(Context context, List<User> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);

    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            User user = getItem(position);

            if(convertView != null) {

                holder = (ViewHolder)convertView.getTag();

            }else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user_result, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder.ivProfilePhoto.setImageResource(0);
            holder.tvName.setText(user.getName());
            holder.tvScreenMae.setText(user.getScreenName());
            holder.tagLine.setText(user.getTagLine());

            Picasso.with(getContext()).load(user.getProfileImageUrl()).fit().centerInside().into(holder.ivProfilePhoto);

            return convertView;

        }

    static class ViewHolder {

        private ImageView ivProfilePhoto;
        private TextView tvName;
        private TextView tvScreenMae;
        private TextView tagLine;

        public ViewHolder(View itemView) {

            tagLine = (TextView)itemView.findViewById(R.id.tvScreenName);
            tvScreenMae = (TextView)itemView.findViewById(R.id.tvTagline);
            tvName = (TextView)itemView.findViewById(R.id.tvUsername);
            ivProfilePhoto = (ImageView)itemView.findViewById(R.id.ivProfilePic);

        }
    }

}
