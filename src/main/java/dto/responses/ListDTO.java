package dto.responses;

import java.util.List;
import java.util.Map;

public class ListDTO {

    private Integer pageNumber;
    private Integer itemsOnPage;
    private Integer itemsTotalCount;
    private Map filteredFields;
    private List data;

    public ListDTO() {
    }

    public ListDTO(int pageNumber, int itemsOnPage, int itemsTotalCount, Map filteredFields, List data) {
        this.pageNumber = pageNumber;
        this.itemsOnPage = itemsOnPage;
        this.itemsTotalCount = itemsTotalCount;
        this.filteredFields = filteredFields;
        this.data = data;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(int itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public int getItemsTotalCount() {
        return itemsTotalCount;
    }

    public void setItemsTotalCount(int itemsTotalCount) {
        this.itemsTotalCount = itemsTotalCount;
    }

    public Map getFilteredFields() {
        return filteredFields;
    }

    public void setFilteredFields(Map filteredFields) {
        this.filteredFields = filteredFields;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
