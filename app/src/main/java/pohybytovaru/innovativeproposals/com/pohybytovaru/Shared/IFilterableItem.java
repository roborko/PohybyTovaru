package pohybytovaru.innovativeproposals.com.pohybytovaru.Shared;

/**
 * Created by Robert on 19.01.2018.
 */

//interface that needs to be implemented on a recycler view item in case we want to allow filtering
public interface IFilterableItem {
    boolean filterFunctionResult(String searchString);
}
