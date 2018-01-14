package pohybytovaru.innovativeproposals.com.pohybytovaru.Models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Robert on 25.12.2017.
 * Model pre nakupnu polozku
 */
public class ShoppingItem {
    @DatabaseField
    private String Name;
    @DatabaseField(columnName = "Id", generatedId = true)
    private int Id;

    @DatabaseField(columnName = "Category", foreign = true, foreignAutoRefresh = true)
    private ShoppingCategory Category;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public ShoppingCategory getCategory() {
        return Category;
    }

    public void setCategory(ShoppingCategory category) {
        Category = category;
    }

    public ShoppingItem() {
    }

    public ShoppingItem(int id, String name, ShoppingCategory category) {
        Name = name;
        Id = id;
        Category = category;
    }
}
