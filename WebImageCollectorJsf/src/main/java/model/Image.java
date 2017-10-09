package model;

import java.io.Serializable;
import java.util.List;

/**
 * Basic Image class that contains information about
 * image name, its path and tags
 */
public class Image implements Serializable {

    private String name;
    private String path;
    private List<String> tags;

    public Image(String name, String path, List<String> tags)
    {
        this.setName(name);
        this.setPath(path);
        this.setTags(tags);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
