package com.proyecto.SazonIA.DTO;

public class ReplyCommentRecipeDTO {

    private Integer comment_id;
    private String content;
    private String comment_time_stamp;
    private Integer user_id;
    private Integer recipe_id;

    public ReplyCommentRecipeDTO() {
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_time_stamp() {
        return comment_time_stamp;
    }

    public void setComment_time_stamp(String comment_time_stamp) {
        this.comment_time_stamp = comment_time_stamp;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    @Override
    public String toString() {
        return "ReplyCommentRecipeController [comment_id=" + comment_id + ", comment_time_stamp=" + comment_time_stamp + ", content="
                + content + ", recipe_id=" + recipe_id + ", user_id=" + user_id + "]";
    }
}
