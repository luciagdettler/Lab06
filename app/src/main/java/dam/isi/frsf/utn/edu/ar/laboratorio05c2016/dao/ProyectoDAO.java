package dam.isi.frsf.utn.edu.ar.laboratorio05c2016.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
        context=c;
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        cursorPry.close();
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public void nuevaTarea(Tarea t){

        ContentValues values = new ContentValues();
        values.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA,t.getDescripcion());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS,t.getHorasEstimadas());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,t.getMinutosTrabajados());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,t.getFinalizada());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD,""+t.getPrioridad().getId());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO,t.getProyecto().getId());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE,t.getResponsable().getId());
        db.insert(ProyectoDBMetadata.TABLA_TAREAS,ProyectoDBMetadata.TABLA_TAREAS,values);
    }

    public void actualizarTarea(Tarea t){
        open(true);
        ContentValues values = new ContentValues();
        values.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA,t.getDescripcion());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS,t.getHorasEstimadas());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,t.getMinutosTrabajados());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,t.getFinalizada());
        // values.put(ProyectoDBMetadata.TablaTareasMetadata.PROYECTO,t.getProyecto().getId());
        values.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD,t.getPrioridad().getId());
        // values.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE,t.getResponsable().getId());
        db.update(ProyectoDBMetadata.TABLA_TAREAS,values,"_ID="+t.getId(),null);
        db.close();
    }



    public void borrarTarea(Tarea t){
        open(true);
        db.delete(ProyectoDBMetadata.TABLA_TAREAS,"_ID="+t.getId(),null);
        close();
    }

    public List<Prioridad> listarPrioridades(){
        open(false);
        Cursor c = db.rawQuery("SELECT * FROM "+ProyectoDBMetadata.TABLA_PRIORIDAD,null);
        List<Prioridad> listaPrioridades = new ArrayList<Prioridad>();
        if(c.moveToFirst())
            do{
                Prioridad prioridadAux = new Prioridad();
                prioridadAux.setId(Integer.parseInt(c.getString(0)));
                prioridadAux.setPrioridad(c.getString(1));

                listaPrioridades.add(prioridadAux);
            }while(c.moveToNext());

        c.close();

        return listaPrioridades;
    }
    public  List<Usuario> addContactosALaBase(List<Usuario> listaContac){
        ContentValues values = new ContentValues();
        for(Usuario u: listaContac){
            values.put(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO,u.getNombre());
            values.put(ProyectoDBMetadata.TablaUsuariosMetadata.MAIL,u.getCorreoElectronico());
            db.insert(ProyectoDBMetadata.TABLA_USUARIOS,ProyectoDBMetadata.TABLA_USUARIOS,values);
        }
        return listaContac;
    }
    public List<Usuario> listarUsuarios(){
        open(false);
        Cursor c = db.rawQuery("SELECT * FROM "+ProyectoDBMetadata.TABLA_USUARIOS,null);
        List<Usuario> listaUsuarios = new ArrayList<Usuario>();

        if(c.moveToFirst())
            do{
                Usuario usuarioAux = new Usuario();
                usuarioAux.setId(Integer.parseInt(c.getString(0)));
                usuarioAux.setNombre(c.getString(1));
                usuarioAux.setCorreoElectronico(c.getString(2));
                listaUsuarios.add(usuarioAux);
            }while(c.moveToNext());

        c.close();

        return listaUsuarios;

    }




    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});
    }

    public List<Tarea> listarDesviosPlanificacion(Boolean soloTerminadas,Integer desvioMaximoMinutos){
        // retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
        // que el tiempo planificado.
        // si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.
        return null;
    }


    public Proyecto buscarProyecto(int ID) {
        List<Proyecto> listaProyecto = listarProyecto();
        for (Proyecto i : listaProyecto)
            if (i.getId() == ID)
                return i;
        return null;
    }

    public List<Proyecto> listarProyecto(){
        open(false);
        Cursor cursorListarDB = db.rawQuery("SELECT * FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
        if(cursorListarDB.moveToFirst())
            do{
                Proyecto nuevo = new Proyecto();
                nuevo.setId(Integer.parseInt(cursorListarDB.getString(0)));
                nuevo.setNombre(cursorListarDB.getString(1));

                listaProyecto.add(nuevo);
            }while(cursorListarDB.moveToNext());

        cursorListarDB.close();

        return listaProyecto;
    }

    public Tarea buscarTarea(int ID){
        List<Tarea> listaTarea = listarTareas(1);
        for(Tarea i: listaTarea)
            if(i.getId()==ID)
                return i;
        return null;
    }

    public Usuario buscarUsuario(int ID){
        List<Usuario> listaUsuario = listarUsuarios();
        for(Usuario i:listaUsuario)
            if(i.getId()==ID)
                return i;
        return null;
    }

    public Prioridad buscarPrioridad(int ID){
        List<Prioridad> listaPrioridad = listarPrioridades();
        for(Prioridad i: listaPrioridad)
            if(i.getId()==ID)
                return i;
        return null;
    }

    public List<Tarea> listarTareas(Integer idProyecto)
    {
        open(false);
        Cursor cursorListarDB = listaTareas(idProyecto);
        List<Tarea> listaTarea = new ArrayList<Tarea>();
        if(cursorListarDB.moveToFirst())
            do{
                /*
                Log.i("Prueba",cursorListarDB.getString(0)); //IDENTIFICADOR
                Log.i("Prueba",cursorListarDB.getString(1)); //DESCRIPCION
                Log.i("Prueba",cursorListarDB.getString(2)); //HORAS_PLANIFICADAS
                Log.i("Prueba",cursorListarDB.getString(3)); //MINUTOS_TRABAJDOS
                Log.i("Prueba",cursorListarDB.getString(4)); //FINALIZADA
                Log.i("Prueba",cursorListarDB.getString(5)); //ID_RESPONSABLE
                Log.i("Prueba",cursorListarDB.getString(6)); //ID_PRIORIDAD
                Log.i("Prueba",cursorListarDB.getString(7)); //ID_Proyecto*/
                /*Tarea nuevo = new Tarea(
                        Integer.parseInt(cursorListarDB.getString(0)),
                        Integer.parseInt(cursorListarDB.getString(2)),
                        Integer.parseInt(cursorListarDB.getString(3)),
                        Boolean.parseBoolean(cursorListarDB.getString(7)),
                        buscarProyecto(Integer.parseInt(cursorListarDB.getString(6))),
                        buscarPrioridad(Integer.parseInt(cursorListarDB.getString(4))),
                        buscarUsuario(Integer.parseInt(cursorListarDB.getString(5))));
                nuevo.setDescripcion(cursorListarDB.getString(1));*/
                Tarea nuevo = new Tarea();
                nuevo.setId(Integer.parseInt(cursorListarDB.getString(0)));
                nuevo.setDescripcion(cursorListarDB.getString(1));
                nuevo.setHorasEstimadas(Integer.parseInt(cursorListarDB.getString(2)));
                nuevo.setMinutosTrabajados(Integer.parseInt(cursorListarDB.getString(3)));
                nuevo.setResponsable(buscarUsuario(Integer.parseInt(cursorListarDB.getString(5))));
                switch(cursorListarDB.getString(6)){
                    case "Urgente":
                        nuevo.setPrioridad(buscarPrioridad(1));
                        break;
                    case "Alta":
                        nuevo.setPrioridad(buscarPrioridad(2));
                        break;
                    case "Media":
                        nuevo.setPrioridad(buscarPrioridad(3));
                        break;
                    case "Baja":
                        nuevo.setPrioridad(buscarPrioridad(4));
                        break;
                }
                nuevo.setProyecto(buscarProyecto(Integer.parseInt(cursorListarDB.getString(7))));
                nuevo.setFinalizada(Boolean.parseBoolean(cursorListarDB.getString(4)));

                listaTarea.add(nuevo);
            }while(cursorListarDB.moveToNext());

        cursorListarDB.close();

        return listaTarea;
    }


}

