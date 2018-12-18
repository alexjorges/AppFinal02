package com.example.alexj.appfinal02.fragments

import android.animation.Animator
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.example.alexj.appfinal02.R
import kotlinx.android.synthetic.main.fragment_anadir1.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Anadir1Fragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Anadir1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Anadir1Fragment : Fragment(), com.android.volley.Response.Listener<String>,com.android.volley.Response.ErrorListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    // variables necesarias para Volley
    private lateinit var request: RequestQueue
    private lateinit var stringRequest: StringRequest
    lateinit var dateAñadir : String

    override fun onErrorResponse(error: VolleyError?) {

        var progressDialog: ProgressDialog = ProgressDialog(context)
        progressDialog.setTitle("Añadiendo")
        progressDialog.setMessage("Añadiendo a la base de datos...")
        progressDialog.show()

        Handler().postDelayed({progressDialog.dismiss()},3000)

        Handler().postDelayed(
            { Snackbar.make(layoutAñadir,"ERROR, no se ha podido añadir", Toast.LENGTH_LONG).show()},2000)
    }


    /*
    metodo onResponse de Volley
     */
    override fun onResponse(response: String?) {
        // comprobamos que lo que nos devuelve el string es correcto segun nuestro PHP
        if (response.equals("Existe")) {

            // creamos una nueva ventana
            var progressDialog: ProgressDialog
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Añadiendo")
            progressDialog.setMessage("Añadiendo a la base de datos...")
            progressDialog.show()

            // Creamos un Handler utilizando el metodo postDelayed() para cerrar la ventana antes creada cuando queramos
            Handler().postDelayed({ progressDialog.dismiss() }, 3000)

            // creamos otro Handler y le pasamos un Toast para que lo muestre cuando queramos
            Handler().postDelayed(
                { Snackbar.make(layoutAñadir,"Ya existe un registro con esta fecha, pruebe con otra", Snackbar.LENGTH_LONG).show()}, 2000
            )
        }else{

            // creamos una nueva ventana de informacion
            var progressDialog: ProgressDialog
            progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Añadiendo")
            progressDialog.setMessage("Añadiendo a la base de datos...")
            progressDialog.show()

            // creamos un Handler utilizando el metodo .postDelayed y le pasamos la ventana anterior para cerrarla cuando queramos
            Handler().postDelayed({ progressDialog.dismiss() }, 3000)

            // creamos un Handler y le pasamos un Toast para que se muestre cuando queramos
            Handler().postDelayed(
                { Snackbar.make(layoutAñadir,"Se ha registrado la medicion correctamente", Snackbar.LENGTH_LONG).show()}, 2000
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // metodo para animar el botón
        animation()

        /**
         * comprobamos que los campos no esten vacios antes de llamar a nuestra funcion para añadir
         */
        botonAñadir.setOnClickListener() {
            if (etDia.text.isEmpty() || etGlucosa.text.isEmpty() || etKilometros.text.isEmpty()) {
                Toast.makeText(context, "No puede haber ningún campo vacio", Toast.LENGTH_LONG).show()
            } else {
                cargarWebServiceAñadir()
            }
        }


        /**
        con esta variable, conseguimos una instancia con el Calendario del sistema,
        escogiendo el dato que queremos en cada caso
         */
        val calendario = Calendar.getInstance()
        var dia = calendario.get(Calendar.DAY_OF_MONTH)
        var mes = calendario.get(Calendar.MONTH)
        var ano = calendario.get(Calendar.YEAR)


        /**
        Metodo para cuando pulsemos en el boton, crear un nuevo DatePickerDialig
        y establecer el año, dia y mes seleccionado
         */
        btFecha.setOnClickListener(){
            // creamos un nuevo data pick, con un listener, pasandole como parametros año, mes y dia, primero el año.
            val dpd = DatePickerDialog(getContext(), DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
                // establecemos en el textview
                etDia.setText(""+dia+"/"+(mes + 1)+"/"+ano)
                // variable para pasarsale a la url
                dateAñadir = ""+ano+"-"+(mes + 1)+"-"+dia
            }, dia, mes, ano) // aqui devolvemos el año, mes y dia antes almacenados desde el metodo get
            dpd.show()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // se crea un view y ese view se infla con la vista correspondiente
        var view: View
        view = inflater.inflate(R.layout.fragment_anadir1, container, false)

        // a request se le pasa en contexto
        request = Volley.newRequestQueue(context)


        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Anadir1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Anadir1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


   /**
    metodo con un objeto StringRequest y un objeto request, que nos permite
    enviar una url por metodo get y conectar con Volley
     */
    fun cargarWebServiceAñadir()
    {
        var urlAñadir: String = "http://iesayala.ddns.net/alexjorges/insertMediciones.php?fecha="+dateAñadir+
                "&glucosa="+etGlucosa.text+
                "&km="+etKilometros.text+
                "&peso="+etPeso.text+
                "&carbohidratos="+etCarbohidratos.text

        // con esto mandamos la informacion a Volley para que la lea y procece la informacion que queremos enviar
        stringRequest = StringRequest(com.android.volley.Request.Method.GET,urlAñadir,this,this)


        // Mandandole al objeto request el objeto jsonObjectRequest
        //nos permite establecer la conexion entre los métodos onErrorResponse y onResponse
        request?.add(stringRequest)

    }


    /**
     * funcion para animar el floatingActionButton
     */
    fun animation(){

    botonAñadir.setScaleX(0f)
    botonAñadir.setScaleY(0f)

    val interpolador = AnimationUtils.loadInterpolator(
        getContext(),
        android.R.interpolator.fast_out_slow_in
    )

    botonAñadir.animate()
        .scaleX(1f)
        .scaleY(1f)
        .setInterpolator(interpolador)
        .setDuration(600)
        .setStartDelay(1000)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {
                    animation.start()

            }
        })
    }

}
