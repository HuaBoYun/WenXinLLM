package com.huabo.etl.repo;

public class RepositoryTree {
    private String id;
    private String parent;
    private String text;
    private String icon;
    private Object state;
    private String type;
    private boolean isLasted;
    private String path;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLasted() {
        return this.isLasted;
    }

    public void setLasted(boolean isLasted) {
        this.isLasted = isLasted;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Object getState() {
        return this.state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public RepositoryTree(String id, String parent, String text, String icon, Object state, String type, boolean isLasted, String path) {
        this.id = id;
        this.parent = parent;
        this.text = text;
        this.icon = icon;
        this.state = state;
        this.type = type;
        this.isLasted = isLasted;
        this.path = path;
    }

    public RepositoryTree() {
    }

    public String toString() {
        return "RepositoryTree [id=" + this.id + ", parent=" + this.parent + ", text=" + this.text + ", icon=" + this.icon + ", state=" + this.state + ", type=" + this.type + ", isLasted=" + this.isLasted + ", path=" + this.path + "]";
    }
}
