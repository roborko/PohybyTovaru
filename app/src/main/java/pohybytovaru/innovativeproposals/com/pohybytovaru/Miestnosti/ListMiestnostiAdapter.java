package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Miestnost;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListMiestnostiRowBinding;

/**
 * Created by Robert on 16.01.2018.
 */

public class ListMiestnostiAdapter extends DataBoundAdapter<ActivityListMiestnostiRowBinding, Miestnost> {
    private final ISimpleRowClickListener actionCallback;

    public ListMiestnostiAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Miestnost> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListMiestnostiRowBinding> holder, int position, List<Object> payloads) {
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
