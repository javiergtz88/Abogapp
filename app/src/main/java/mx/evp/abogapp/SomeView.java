package mx.evp.abogapp;

import java.util.HashMap;

import Models.ContentModel;

/**
 * Created by saul on 8/18/15.
 */
public interface SomeView {

    HashMap get_data();
    void set_data(HashMap a);
    void alert(String s);

    void dismis_progress();

    void add(HashMap params);

    void add(ContentModel content);
}
