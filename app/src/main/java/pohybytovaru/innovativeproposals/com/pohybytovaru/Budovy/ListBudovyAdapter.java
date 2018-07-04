package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Budova;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListBudovaRowBinding;

import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

public class ListBudovyAdapter extends DataBoundAdapter<ActivityListBudovaRowBinding, Budova > {

    private final ISimpleRowClickListener actionCallback;

    public ListBudovyAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Budova> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);

    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListBudovaRowBinding> holder, int position, List<Object> payloads) {
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
