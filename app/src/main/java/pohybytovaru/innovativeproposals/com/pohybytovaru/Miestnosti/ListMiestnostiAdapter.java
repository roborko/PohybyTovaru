package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

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

public class ListMiestnostiAdapter extends DataBoundAdapter<ActivityListMiestnostiRowBinding> {
    private final ISimpleRowClickListener actionCallback;
    List<Miestnost> data = new ArrayList<>();

    public ListMiestnostiAdapter(int layoutId, ISimpleRowClickListener actionCallback, List<Miestnost> data) {
        super(layoutId);
        this.actionCallback = actionCallback;
        this.data = data;
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListMiestnostiRowBinding> holder, int position, List<Object> payloads) {
        holder.binding.setObj(data.get(position));
        holder.binding.setCallback(actionCallback);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
