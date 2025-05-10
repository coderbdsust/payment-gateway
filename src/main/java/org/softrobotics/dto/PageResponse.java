package org.softrobotics.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PageResponse<T>{
    private List<T> content;
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private int totalCount;
}