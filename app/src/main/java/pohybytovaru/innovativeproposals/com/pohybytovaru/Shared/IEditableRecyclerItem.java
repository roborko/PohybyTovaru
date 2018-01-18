package pohybytovaru.innovativeproposals.com.pohybytovaru.Shared;

/**
 * Created by Robert on 18.01.2018.
 */

//interface used for selecting and deselecting item within recycler view
public interface IEditableRecyclerItem {
    boolean Selected = false;

    boolean isSelected();

    void setSelected(boolean selected);

    int Id = 0;

    int getId();

    void setId(int id);

    //function used in filtering on a view
    boolean filterFunctionResult(String searchString);
}
