package pl.krystiankrawczyk.apidataexplorer.explorer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.krystiankrawczyk.apidataexplorer.R;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_list_item_iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.fragment_list_item_tv_name)
    TextView tvName;
    @BindView(R.id.fragment_list_item_tv_second_name)
    TextView tvSecondName;
    @BindView(R.id.fragment_list_item_iv_favourite)
    ImageView ivFavouriteIcon;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public CircleImageView getAvatar() {
        return ivAvatar;
    }

    public TextView getName() {
        return tvName;
    }

    public TextView getSecondName() {
        return tvSecondName;
    }

    public ImageView getFavouriteIcon() {
        return ivFavouriteIcon;
    }
}
