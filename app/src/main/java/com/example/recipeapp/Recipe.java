package com.example.recipeapp;

public class Recipe {
    private String title;
    private String category;
    private boolean isFavorite;

    public Recipe(String title, String category, boolean isFavorite) {
        this.title = title;
        this.category = category;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return (isFavorite ? "★ " : "") + title + " (" + category + ")";
    }
}
