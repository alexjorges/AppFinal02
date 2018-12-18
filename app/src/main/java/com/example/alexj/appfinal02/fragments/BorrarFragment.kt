package com.example.alexj.appFinal02.fragments

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
import kotlinx.android.synthetic.main.fragment_borrar.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BorrarFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BorrarFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

// añadimos com.android.volley.Response.Listener<String> y com.android.volley.Response.ErrorListener al fragment
class BorrarFragment() : Fragment(), com.android.volley.Response.Listener<String>,com.android.volley.Response.ErrorListener{

    private lateinit var request: RequestQueue
    private lateinit var stringRequest: StringRequest
    private lateinit var date : String



    // metodo de Volley que nos obliga a implementar
    override fun onResponse(response: String?) {
        // comprobamos que lo devuelto por Volley(un string), es correcto y lo tratamos
        if (response.equals("eliminado")) {
            var progressDialog: ProgressDialog = ProgressDialog(context)
            progressDialog.setTitle("Borrando")
            progressDialog.setMessage("Borrando...")
            progressDialog.show()

            Handler().postDelayed({ progressDialog.dismiss() }, 3000)

            Handler().postDelayed({Toast.makeText(context,"Eliminado correctamente",Toast.LENGTH_LONG).show()},2000)

        }
        else{
            var progressDialog: ProgressDialog = ProgressDialog(context)
            progressDialog.setTitle("Borrando")
            progressDialog.setMessage("Borrando...")
            progressDialog.show()

            Handler().postDelayed({progressDialog.dismiss()},3000)

            Toast.makeText(context, "Error, la fecha no existe", Toast.LENGTH_LONG).show()
        }
    }

    // metodo de Volley que nos obliga a implementar
    override fun onErrorResponse(error: VolleyError?) {
        Toast.makeText(context, "Error, no hay conexion con la base de datos", Toast.LENGTH_LONG).show()
    }



    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // creamos la vista y la inflamos con el fragment correspondiente
        var vista: View

        vista = inflater.inflate(R.layout.fragment_borrar,container, false)

        // creamos un objeto newRequestQueue al que luego le pasaremos con add el StringRequest
        request = Volley.newRequestQueue(context)



        return vista
    }

    // en los fragments, necesitamos este método, donde pondremos toda nuestra lógica
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // animacion del floatingActionButton
        animation()


        /**
         * con esta variale comprobamos que el campo no este vacio. si lo está lanzamos un mensaje
         * y en caso contrario llamamos al metodo
         */
        botonBorrar.setOnClickListener(){
            if (etDiaBorrar.text.isEmpty()){

                Snackbar.make(root_layout,"El campo 'Dia a Borrar' no puede estar vacio", Snackbar.LENGTH_LONG).show()
            }else{
                cargarWebServiceEliminar()
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
        btFechaBorrar.setOnClickListener(){
            // creamos un nuevo data pick, con un listener, pasandole como parametros año, mes y dia, primero el año.
            val dpd = DatePickerDialog(getContext(), DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
                // establecemos en el textview
                etDiaBorrar.setText(""+dia+"/"+(mes + 1)+"/"+ano)
                date = "'"+ano+"-"+(mes + 1)+"-"+dia+"'"
            }, dia, mes, ano) // aqui devolvemos el año, mes y dia antes almacenados desde el metodo get
            dpd.show()
        }
    }


    fun animation(){

        botonBorrar.setScaleX(0f)
        botonBorrar.setScaleY(0f)

        val interpolador = AnimationUtils.loadInterpolator(
            getContext(),
            android.R.interpolator.fast_out_slow_in
        )

        botonBorrar.animate()
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


     fun cargarWebServiceEliminar() {



        // creamos la variable con nuestra url, pasandole como parámetro el texto del EditText
        val urlEliminar: String = "http://iesayala.ddns.net/alexjorges/deleteMediciones.php?fecha="+ date

        stringRequest = StringRequest(com.android.volley.Request.Method.GET,urlEliminar,this,this)
        // con esto mandamos la informacion a Volley para que la lea y procece la informacion que queremos envia

        // Mandandole al objeto request el objeto StringRequest
        //nos permite establecer la conexion entre los métodos onErrorResponse y onResponse
        request?.add(stringRequest)
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
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
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
         * @return A new instance of fragment BorrarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BorrarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
