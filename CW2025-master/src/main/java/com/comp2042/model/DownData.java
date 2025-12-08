package com.comp2042.model;

/**
 * Result of a downward move event.
 * Bundles any cleared row data with the updated view data for rendering.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Creates a new DownData result.
     *
     * @param clearRow the cleared row information, or {@code null} if no rows
     *                 cleared
     * @param viewData the updated view data for rendering
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the cleared row information.
     *
     * @return the ClearRow data, or {@code null} if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data.
     *
     * @return the view data for rendering the current state
     */
    public ViewData getViewData() {
        return viewData;
    }
}
