package pohybytovaru.innovativeproposals.com.pohybytovaru.TovarVMiestnosti;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundAdapter;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters.DataBoundViewHolder;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.Tovar;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.ISimpleRowClickListener;
import pohybytovaru.innovativeproposals.com.pohybytovaru.databinding.ActivityListTovarRowBinding;


/*
select  tovar.nazov as Tovar, miestnost.nazov as Miestnost, aktualneMnozstvo.mnozstvo
        from aktualneMnozstvo
        join miestnost on miestnost.id = aktualneMnozstvo.miestnost
        join tovar on tovar.id = aktualneMnozstvo.tovar
        order by  tovar.nazov, miestnost.nazov
*/


public class ListTovarVMiestnostiAdapter  extends DataBoundAdapter<ActivityListTovarRowBinding, Tovar> {
    private final ISimpleRowClickListener actionCallback;

    public ListTovarVMiestnostiAdapter(@NonNull Context context, int layoutId, ISimpleRowClickListener actionCallback, List<Tovar> data) {
        super(layoutId);
        this.context = context;
        this.actionCallback = actionCallback;
        this.data = data;
        this.filterView.dataSourceChanged(this.data);
    }

    @Override
    protected void bindItem(DataBoundViewHolder<ActivityListTovarRowBinding> holder, int position, List<Object> payloads) {
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
