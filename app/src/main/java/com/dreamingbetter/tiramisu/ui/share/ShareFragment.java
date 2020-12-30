package com.dreamingbetter.tiramisu.ui.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.dreamingbetter.tiramisu.R;
import com.dreamingbetter.tiramisu.entities.Content;
import com.dreamingbetter.tiramisu.ui.favorite.FavoriteFragment;
import com.dreamingbetter.tiramisu.ui.home.HomeFragment;
import com.dreamingbetter.tiramisu.utils.Helper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class ShareFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        updateUI(root);

        return root;
    }

    private void updateUI(final View view) {
        final FragmentActivity activity = getActivity();

        if (activity == null || view == null) return;

        final Content lastContent = Hawk.get("content", null);
        final Content content = Hawk.get("lastSharedContent", null);

        if (content == null) return;

        RoundedImageView monthImage = view.findViewById(R.id.content_image);

        // Set the month image
        String month = String.format(Locale.getDefault(), "month_%02d", Calendar.getInstance().get(Calendar.MONTH));
        monthImage.setImageResource(Helper.getResId(activity.getApplicationContext(), "drawable", month));

        TextView text = view.findViewById(R.id.content_text);
        text.setText(String.format("%s%s\"", '"', content.text));

        TextView author = view.findViewById(R.id.content_author);
        author.setText(String.format("%s ", content.author));

        LinearLayout sharingContentView = view.findViewById(R.id.sharing_content);

        Bitmap bm = ConvertUtils.view2Bitmap(sharingContentView);
        Uri uri = Helper.shareUriBitmap(activity.getApplicationContext(), bm);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.setType("image/png");
        } else {
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.format("\n%s%s\"", '"', content.text) + "\n" + content.author + "\n\n" + getString(R.string.app_name) + "\n" + getString(R.string.get_it_on) + " Google Play Store");
        }
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

        if (lastContent.uid.equals(content.uid)) {
            FragmentUtils.replace(this, new HomeFragment());
        } else {
            FragmentUtils.replace(this, new FavoriteFragment());
        }
    }
}