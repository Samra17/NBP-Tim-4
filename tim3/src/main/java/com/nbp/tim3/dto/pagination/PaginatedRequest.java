package com.nbp.tim3.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedRequest {
    private int page;
    private int recordsPerPage;
}
