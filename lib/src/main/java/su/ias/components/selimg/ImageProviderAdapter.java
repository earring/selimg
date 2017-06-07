package su.ias.components.selimg;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import su.ias.components.adapters.BaseListAdapter;
import su.ias.components.adapters.OnItemClickListener;
import su.ias.components.selimg.providers.ImageProvider;

class ImageProviderAdapter extends BaseListAdapter<ImageProvider, ImageProviderAdapter.ViewHolder> {

    public ImageProviderAdapter(List<ImageProvider> list, OnItemClickListener listener) {
        super(list, listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater(parent).inflate(R.layout.item_open, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final ImageProvider item = getItem(position);

        viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.selectImage();
            }
        });
        Drawable icon = ContextCompat.getDrawable(viewHolder.imgItem.getContext(), item.getImg());
        viewHolder.imgItem.setImageDrawable(icon);
        viewHolder.txtItem.setText(item.getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewGroup layoutItem;

        ImageView imgItem;

        TextView txtItem;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutItem = (ViewGroup) itemView.findViewById(R.id.layout_item);
            imgItem = (ImageView) itemView.findViewById(R.id.img_item);
            txtItem = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }
}