import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 * Created by sanek94cool on 28.09.2017.
 */

@ManagedBean (name = "javaBean")
@RequestScoped
public class Bean {

    private String text = "Vasily";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
