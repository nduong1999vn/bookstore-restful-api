package com.duonghn.springrolejwt.model;

public class CommentDto {
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment getCmtFromDto(){
        Comment cmt = new Comment();
        cmt.setComment(comment);

        return cmt;
    }
}
