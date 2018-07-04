package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Poschodie;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListBudovaRowBinding;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListPoschodieRowBinding;

public class ListPoschodieAdapter extends DataBoundAdapter<ActivityListPoschodieRowBinding, Poschodie > {

    private final ISimpleRowClickListener actionCallback;

    public ListPoschodieAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Poschodie> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);

    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListPoschodieRowBinding> holder, int position, List<Object> payloads) {
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

