package com.mzoubi.smartcity.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PaginationUtils {

    // Default values
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_DIR = "asc";
    private static final String DESC_SORT_DIR = "desc";
    private static final Set<String> ALLOWED_FIELDS = Set.of("id", "name"); // TODO: extend with more fields later
    private static final Set<String> ALLOWED_DIRECTION = Set.of(DEFAULT_SORT_DIR, DESC_SORT_DIR);

    private PaginationUtils() {

    }

    /**
     * Builds a PageRequest with dynamic paging and sorting.
     *
     * @param page    zero-based page index
     * @param size    page size
     * @param sortBy  field name to sort by
     * @param sortDir "asc" or "desc"
     * @return PageRequest object
     */
    public static PageRequest createPageRequest(
            final Integer page,
            final Integer size,
            final String sortBy,
            final String sortDir,
            final Set<String> allowedFields) {
        int pageNumber = (page == null || page < 0) ? DEFAULT_PAGE_NUMBER : page;
        int pageSize = (size == null || size < 1) ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);// Use Math.min to limit page size

        String safeSortBy = (sortBy == null || !allowedFields.contains(sortBy)) ? DEFAULT_SORT_FIELD : sortBy;
        String safeSortDir = (sortDir == null || !ALLOWED_DIRECTION.contains(sortDir)) ? DEFAULT_SORT_DIR : sortDir;

        Sort sort = DESC_SORT_DIR.equalsIgnoreCase(safeSortDir) ? Sort.by(safeSortBy).descending() : Sort.by(safeSortBy).ascending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
