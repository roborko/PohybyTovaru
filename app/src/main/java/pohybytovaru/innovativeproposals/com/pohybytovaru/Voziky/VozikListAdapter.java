package pohybytovaru.innovativeproposals.com.pohybytovaru.Voziky;


import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Vozik;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;

import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.VozikActivityRowBinding;


public class VozikListAdapter extends DataBoundAdapter<VozikActivityRowBinding, Vozik> {
    private final ISimpleRowClickListener actionCallback;

    public VozikListAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Vozik> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);

    }

    @Override
    protected void bindItem(DataBoundViewHolder<VozikActivityRowBinding> holder, int position, List<Object> payloads) {
        holder.binding.setObj(this.filterView.get(position));
        holder.binding.setCallback(actionCallback);
    }

    @Override
    public int getItemCount() {
        try {
            return this.filterView.size();
        } catch (Exception ex) {
            return 0;
        }
    }
}

