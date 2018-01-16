package pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Robert on 16.01.2018.
 */

public class DataBoundViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;

    public DataBoundViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Creates a new ViewHolder for the given layout file.
     * <p>
     * The provided layout must be using data binding.
     *
     * @param parent The RecyclerView
     * @param layoutId The layout id that should be inflated. Must use data binding
     * @param <T> The type of the Binding class that will be generated for the <code>layoutId</code>.
     * @return A new ViewHolder that has a reference to the binding class
     */
    public static <T extends ViewDataBinding> DataBoundViewHolder<T> create(ViewGroup parent,
                                                                            @LayoutRes int layoutId) {
        T binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                layoutId, parent, false);
        return new DataBoundViewHolder<>(binding);
    }
}