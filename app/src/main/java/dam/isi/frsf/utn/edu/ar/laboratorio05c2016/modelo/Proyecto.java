package dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.apiRest.ProyectoDBApiRestMetaData;

/**
 * Created by mdominguez on 06/10/16.
 */
public class Proyecto {

    private Integer id;
    private String nombre;

    public Proyecto() {

    }
    public Proyecto(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt(ProyectoDBApiRestMetaData.TablaProyectoMetadata.ID);
            nombre = jsonObject.getString(ProyectoDBApiRestMetaData.TablaProyectoMetadata.NOMBRE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Proyecto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ProyectoDBApiRestMetaData.TablaProyectoMetadata.NOMBRE,nombre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("JSON-PROYECTO: ",jsonObject.toString());
        return jsonObject;
    }
}
