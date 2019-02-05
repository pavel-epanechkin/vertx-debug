package dto.requests;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class PaginationParams {
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer itemsOnPage;
    @NotNull
    private Map<String, Object> filter;

    private Long startDate;

    private Long endDate;

    public PaginationParams() {
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getItemsOnPage() {
        return itemsOnPage;
    }

    public void setItemsOnPage(Integer itemsOnPage) {
        this.itemsOnPage = itemsOnPage;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}
