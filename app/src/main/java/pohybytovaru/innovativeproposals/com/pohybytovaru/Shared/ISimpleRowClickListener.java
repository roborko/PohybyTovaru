package pohybytovaru.innovativeproposals.com.pohybytovaru.Shared;

import android.view.View;

/**
 * Created by Robert on 16.01.2018.
 */

public interface ISimpleRowClickListener<T> {
    void onItemClick(T item);
    void onItemClick(View view, T item);
    boolean onItemLongClick(View view, T item);
}
