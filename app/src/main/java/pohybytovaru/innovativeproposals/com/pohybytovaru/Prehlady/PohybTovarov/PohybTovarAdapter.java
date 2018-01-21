package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Pohyb;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListMiestnostiRowBinding;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListTovarRowBinding;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityPohybTovarRowBinding;

/**
 * Created by Robert on 21.01.2018.
 */

public class PohybTovarAdapter extends DataBoundAdapter<ActivityPohybTovarRowBinding, Pohyb> {
    private final ISimpleRowClickListener actionCallback;

    public PohybTovarAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Pohyb> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityPohybTovarRowBinding> holder, int position, List<Object> payloads) {
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
