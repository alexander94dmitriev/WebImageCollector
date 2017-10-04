package main.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * Created by sanek94cool on 28.09.2017.
 */

@ManagedBean (name = "javaBean")
@RequestScoped
public class Bean {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void handleText()
    {
        this.text = text.toUpperCase();
    }

}
