package pohybytovaru.innovativeproposals.com.pohybytovaru.Shared;

/**
 * Created by Robert on 16.01.2018.
 */

public interface ISimpleRowClickListener<T> {
    void onItemClick(T item);
    boolean onItemLongClick(T item);
}
