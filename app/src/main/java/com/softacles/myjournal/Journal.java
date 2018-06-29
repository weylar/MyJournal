package com.softacles.myjournal;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Journal {
    private String title;
    private String content;

    @PrimaryKey(autoGenerate = true)
    private int id;

    public Journal(int id, String title, String content){
        this.title = title;
        this.content = content;
        this.id = id;
    }

    @Ignore
    public Journal(String title, String content) {
        this.title = title;
        this.content = content;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
