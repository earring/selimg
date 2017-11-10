package su.ias.components.selimg;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import su.ias.components.adapters.BaseListAdapter;
import su.ias.components.adapters.OnItemClickListener;
import su.ias.components.selimg.providers.ImageProvider;

/**
 * Adapter for image providers (from camera, from file, etc...)
 */
class ImageProviderAdapter extends BaseListAdapter<ImageProvider, ImageProviderAdapter.ViewHolder> {

    private final boolean showIcons;

    public ImageProviderAdapter(List<ImageProvider> list,
                                OnItemClickListener listener,
                                boolean showIcons) {
        super(list, listener);
        this.showIcons = showIcons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater(parent).inflate(R.layout.item_open, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageProvider item = getItem(position);

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.selectImage();
            }
        });
        Context context = holder.imgItem.getContext();
        int color = ContextCompat.getColor(context, item.getColor());

        if (showIcons) {
            Drawable icon = ContextCompat.getDrawable(context, item.getImg());
            icon = DrawableCompat.wrap(icon);
            DrawableCompat.setTint(icon, color);
            holder.imgItem.setVisibility(View.VISIBLE);
            holder.imgItem.setImageDrawable(icon);
        } else {
            holder.imgItem.setVisibility(View.GONE);
        }

        holder.txtItem.setText(item.getTitle());
        holder.txtItem.setTextColor(color);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewGroup layoutItem;

        ImageView imgItem;

        TextView txtItem;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item);
            imgItem = itemView.findViewById(R.id.img_item);
            txtItem = itemView.findViewById(R.id.txt_item);
        }
    }
}