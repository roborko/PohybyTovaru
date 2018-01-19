package pohybytovaru.innovativeproposals.com.pohybytovaru.Adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IEditableRecyclerItem;
import pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.IFilterableItem;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

abstract public class DataBoundAdapter<T extends ViewDataBinding, U extends IEditableRecyclerItem & IFilterableItem> extends BaseDataBoundAdapter<T> {
    public List<U> data = new ArrayList<>();
    public List<U> filteredArray = new ArrayList<>();
    public Context context;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();//selected items
    @LayoutRes
    private final int mLayoutId;

    /**
     * Creates a DataBoundAdapter with the given item layout
     *
     * @param layoutId The layout to be used for items. It must use data binding.
     */
    public DataBoundAdapter(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
    }

    @Override
    public int getItemLayoutId(int position) {
        return mLayoutId;
    }

    public void AddNewCollection(List<U> data) {
        this.data = data;
        this.filteredArray.addAll(this.data);
        notifyDataSetChanged();
    }

    public void RemoveItemId(Integer itemID) {
        //locate item
        Integer indexToRemove = 0;
        U itemToRemove = null;
        for (int iFiltered = 0; iFiltered < filteredArray.size(); iFiltered++) {
            if (filteredArray.get(iFiltered).getId() == itemID) {
                itemToRemove = filteredArray.get(iFiltered);
                indexToRemove = iFiltered;
                break;
            }
        }

        //remove item from sparse boolean array
        if (selectedItems.get(indexToRemove, false)) {
            selectedItems.delete(indexToRemove);
        }

        //now find item under original array and remove it
        for (U dataItem : data) {
            if (dataItem.getId() == itemToRemove.getId()) {
                data.remove(dataItem);
                break;
            }
        }

        //remove item
        boolean result = filteredArray.remove(itemToRemove);
        if (this.filteredArray.size() > 0) {
            notifyItemRemoved(indexToRemove);
            notifyItemRangeChanged(indexToRemove, filteredArray.size());
        } else {
            //there would be nothing under filtered items and therefore return remaining original array
            this.filteredArray.clear();
            this.filteredArray.addAll(this.data);
            notifyDataSetChanged();
        }
    }

    public void AddItem(U newItem) {
        this.data.add(newItem);
        this.filteredArray.add(newItem);
        notifyDataSetChanged();
    }

    //Updates item values
    public void UpdateItem(U updatedItem) {
        //change item under main model
        for (int iIndex = 0; iIndex < data.size(); iIndex++) {
            if (data.get(iIndex).getId() == updatedItem.getId()) {
                data.set(iIndex, updatedItem);
                break;
            }
        }

        //change item under filtered array
        for (int iIndex = 0; iIndex < filteredArray.size(); iIndex++) {
            if (filteredArray.get(iIndex).getId() == updatedItem.getId()) {
                filteredArray.set(iIndex, updatedItem);
                notifyItemChanged(iIndex);
                return;
            }
        }
    }

    //toggles item's selection under original array
    //this way we are keeping proper indexes of selected values no matter what filter value is
    public void toggleItemSelection(int position) {
        boolean selectedValue = !selectedItems.get(position, false);//setting opposite value as we want to toggle current value
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
            filteredArray.get(position).setSelected(selectedValue);
        } else {
            selectedItems.put(position, true);
            filteredArray.get(position).setSelected(selectedValue);
        }

        notifyItemChanged(position);
    }


    //retuns current number of selected items
    public int getSelectedItemsCount() {
        return selectedItems.size();
    }

    //clears selection of selected items
    public void clearSelectedItems() {
        //remove selected flag from original data first
        for (int iItem = 0; iItem < selectedItems.size(); iItem++) {
            filteredArray.get(selectedItems.keyAt(iItem)).setSelected(false);
            notifyItemChanged(iItem);
        }

        //clear selected arrays
        selectedItems.clear();
    }

    //returns ID's of all selected items
    public List<Integer> getSelectedItemsId() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int iItem = 0; iItem < selectedItems.size(); iItem++) {
            items.add(filteredArray.get(selectedItems.keyAt(iItem)).getId());
        }

        return items;
    }

    public void FilterArray(final String searchText) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filteredArray.clear();

                if (TextUtils.isEmpty(searchText)) {
                    filteredArray.addAll(data);
                } else {
                    for (U dataItem : data) {
                        if (dataItem.filterFunctionResult(searchText))
                            filteredArray.add(dataItem);
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}