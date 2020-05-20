package com.dreamingbetter.tiramisu.adapters;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dreamingbetter.tiramisu.MainActivity;
import com.dreamingbetter.tiramisu.R;
import com.dreamingbetter.tiramisu.database.AppDatabase;
import com.dreamingbetter.tiramisu.entities.ContentFavorite;
import com.dreamingbetter.tiramisu.ui.favorite.FavoriteFragment;
import com.dreamingbetter.tiramisu.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
    private final List<ContentFavorite> mValues;
    private final FavoriteFragment mFragment;

    public FavoriteRecyclerViewAdapter(List<ContentFavorite> items, FavoriteFragment fragment) {
        mValues = items;
        mFragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_favorite, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mContentView.setText(String.format("%s%s\"", '"', holder.mItem.text));
        holder.mAuthorView.setText(String.format("%s ", holder.mItem.author));

        holder.mFavoriteButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.mView.getContext())
                        .setMessage(R.string.alert_remove_favorite)
                        .setCancelable(true)
                        .setNegativeButton("No", null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                AppDatabase database = Room.databaseBuilder(holder.mView.getContext(), AppDatabase.class, "db").allowMainThreadQueries().build();
                                database.contentFavoriteDao().delete(holder.mItem.uid);

                                Toast.makeText(holder.mView.getContext(), R.string.removed_from_favorite, Toast.LENGTH_SHORT).show();

                                holder.mView.setVisibility(View.GONE);

                                List<ContentFavorite> dataset = database.contentFavoriteDao().getAll();

                                if (dataset.isEmpty()) {
                                    mFragment.goToHome();
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mAuthorView;
        public final ImageView mFavoriteButtonView;
        public ContentFavorite mItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mContentView = view.findViewById(R.id.item_content);
            mAuthorView = view.findViewById(R.id.item_author);
            mFavoriteButtonView = view.findViewById(R.id.item_favorite_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}