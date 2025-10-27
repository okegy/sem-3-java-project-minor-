package com.cyberlearn.app.model;

import java.util.List;

public class Material {
    private String id;
    private String title;
    private String level; // e.g., "L1 Basics"
    private String description;
    private java.util.List<String> links;

    public Material(){}

    public Material(String id, String title, String level, String description, java.util.List<String> links){
        this.id = id; this.title = title; this.level = level; this.description = description; this.links = links;
    }
    public String getId(){ return id; }
    public void setId(String v){ id=v; }
    public String getTitle(){ return title; }
    public void setTitle(String v){ title=v; }
    public String getLevel(){ return level; }
    public void setLevel(String v){ level=v; }
    public String getDescription(){ return description; }
    public void setDescription(String v){ description=v; }
    public java.util.List<String> getLinks(){ return links; }
    public void setLinks(java.util.List<String> v){ links=v; }
}
