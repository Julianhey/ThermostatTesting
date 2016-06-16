package nl.tue.thermostattesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s157570 on 16-6-2016.
 */
public class Group {

    public String string;
    public final List<String> children = new ArrayList<String>();

    public Group(String string) {
        this.string = string;
    }
}
