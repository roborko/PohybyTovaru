package pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;

/**
 * Created by Robert on 19.01.2018.
 */

public class FilterView<T extends IFilterableItem> {
    private List<T> filteredItems = new ArrayList<>();
    private List<T> dataSource;
    private String filterText;

    public T get(int index) {
        if(index > filteredItems.size()){
            return null;
        }
        return filteredItems.get(index);
    }

    public int size() {
        return filteredItems.size();
    }

    public void filter(List<T> dataSource, String filterText){
        this.filterText = filterText;
        this.dataSource = dataSource;
        refresh();
    }

    public void filterTextChanged(String filterText){
        this.filterText = filterText;
        refresh();
    }

    public void dataSourceChanged(List<T> dataSource){
        this.dataSource = dataSource;
        refresh();
    }

    public void refresh() {
        this.filteredItems.clear();
        if(this.filterText == null || this.filterText.length() == 0) {
            this.filteredItems.addAll(dataSource);
            return;
        }

        for (T dataItem : dataSource) {
            if(dataItem.filterFunctionResult(filterText))
                this.filteredItems.add(dataItem);
        }
    }
}
