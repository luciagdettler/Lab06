package dam.isi.frsf.utn.edu.ar.laboratorio05c2016;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.laboratorio05c2016.modelo.Usuario;


/**
 * Created by Usuario on 15/1/2017.
 */
public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horasEstimadas;
    private SeekBar prioridadSeek;
    private Spinner responsabilidad;
    private TextView prioridadView;
    private List<Prioridad> listaDePrioridades;
    private Button btGuardar;
    private Button btCancelar;
    private ProyectoDAO consultaAlaBase;
    private Tarea nuevaTarea;
    private boolean tareaEditada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);

        consultaAlaBase = new ProyectoDAO(AltaTareaActivity.this);

        descripcion = (EditText) findViewById(R.id.editText);
        horasEstimadas = (EditText) findViewById(R.id.editText2);
        prioridadView = (TextView) findViewById(R.id.textView4);
        prioridadSeek = (SeekBar) findViewById(R.id.seekBar);
        responsabilidad = (Spinner) findViewById(R.id.spinner);
        btGuardar = (Button) findViewById(R.id.btnGuardar);
        btCancelar = (Button) findViewById(R.id.btnCanelar);


        prioridadSeek.setMax(3);

        ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Usuario>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item);
        adaptador.add(new Usuario(-1, "[Seleccione el usuario]", "Juan Perez"));
        Context c=getApplicationContext();
        List<Usuario> listaContac=consultaAlaBase.listarUsuarios();
        consultaAlaBase.addContactosALaBase(listaContac);
        adaptador.addAll(consultaAlaBase.listarUsuarios());
        responsabilidad.setAdapter(adaptador);
        responsabilidad.setBackgroundColor(Color.BLACK);

        listaDePrioridades = consultaAlaBase.listarPrioridades();
        prioridadView.setText("La Prioridad:" + listaDePrioridades.get(1).toString());
        prioridadSeek.setProgress(0);
        prioridadSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                prioridadView.setText("Prioridad: " + listaDePrioridades.get(prioridadSeek.getProgress()).toString());

            }
        });

        //vamos si se presiono el boton editar o el boton para crear una nueva tarea
        nuevaTarea = new Tarea();
        tareaEditada=false;
        if(getIntent().getExtras().getInt("ID_TAREA")!=0){

            nuevaTarea = consultaAlaBase.buscarTarea(getIntent().getExtras().getInt("ID_TAREA"));
            prioridadSeek.setProgress(nuevaTarea.getPrioridad().getId());
            // prioridadView.setText("Prioridad: "+listaDePrioridades.get(nuevaTarea.getPrioridad().getId()).toString());
            descripcion.setText(nuevaTarea.getDescripcion());
            horasEstimadas.setText(" "+nuevaTarea.getHorasEstimadas());
            tareaEditada=true;

        }


        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descripcion.getText().toString().isEmpty()){
                    Toast.makeText(AltaTareaActivity.this,"Debe ingresar una descripción para poder guardar la tarea",Toast.LENGTH_LONG).show();
                }
                else if(horasEstimadas.getText().toString().isEmpty()){
                    Toast.makeText(AltaTareaActivity.this,"Debe ingresar una hora estimada para poder guardar la tarea",Toast.LENGTH_LONG).show();
                }
                else if(((Usuario) responsabilidad.getSelectedItem()).getId()==-1) {
                    Toast.makeText(AltaTareaActivity.this, "Debe seleccionar un responsable para poder guardar la tarea", Toast.LENGTH_SHORT).show();
                }
                else {

                    nuevaTarea.setDescripcion(descripcion.getText().toString());
                    nuevaTarea.setHorasEstimadas(Integer.parseInt(horasEstimadas.getText().toString()));
                    nuevaTarea.setPrioridad(listaDePrioridades.get(prioridadSeek.getProgress()));
                    nuevaTarea.setResponsable((Usuario)responsabilidad.getSelectedItem());

                    if(tareaEditada){
                        consultaAlaBase.actualizarTarea(nuevaTarea);
                        Toast.makeText(AltaTareaActivity.this, "Se edito la tarea con exito!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        nuevaTarea.setMinutosTrabajados(0);
                        nuevaTarea.setFinalizada(false);
                        nuevaTarea.setProyecto(consultaAlaBase.buscarProyecto(1)); // Proyecto 1 temporal
                        consultaAlaBase.nuevaTarea(nuevaTarea);
                        Toast.makeText(AltaTareaActivity.this, "Se creo la tarea con exito!", Toast.LENGTH_SHORT).show();
                    }
                    Intent actividad = new Intent();
                    setResult(RESULT_OK,actividad);

                    finish();
                }
            }

        });

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividad = new Intent();
                setResult(RESULT_CANCELED,actividad);
                finish();
            }
        });

    }

}


