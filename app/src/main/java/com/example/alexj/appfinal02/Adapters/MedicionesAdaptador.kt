package com.example.alexj.appFinal02

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.alexj.appFinal02.fragments.ConsultarFragment
import com.example.alexj.appfinal02.R
import com.example.alexj.appfinal02.R.id.*
import kotlinx.android.synthetic.main.fragment_consultar.view.*
import kotlinx.android.synthetic.main.lista_item.*
import kotlinx.android.synthetic.main.lista_item.view.*
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.DateFormat.*
import java.text.Format.*
import java.util.*
import javax.sql.CommonDataSource


class MedicionesAdaptador(val mediciones: ArrayList<Medicion>, val context: ConsultarFragment): RecyclerView.Adapter<MedicionesAdaptador.MedicionesHolder>(),View.OnClickListener {

    // variable para hacer click en cada item del recycled
    private  var listener: View.OnClickListener? = null

    /**
        metodo necesario para cualquier adaptador:
    // creamos un holder con los componentes gráficos y se lo pasamos a la clase arriba
    */
    class MedicionesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFecha = view.tvFechaLista
        val tvGlucosa = view.tvGlucosaLista
        val tvKm = view.tvKmLista
        val tvPeso = view.tvPesoLista
        val tvCarbohidratos = view.tvCarbohidratosLista
    }


    /**
        metodo necesario para cualquier adaptador
     */
    override fun onBindViewHolder(p0: MedicionesHolder, position: Int) {

        // con SimpleDateFormat("dd,MM,yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(fecha) pasamos nuestra fecha al formato deseado
        p0.tvFecha?.text = SimpleDateFormat("dd-MM-yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(mediciones.get(position).fecha))
        p0.tvGlucosa?.text = mediciones.get(position).glucosa.toString()
        p0.tvKm?.text = mediciones.get(position).km.toString()
        p0.tvPeso?.text = mediciones.get(position).peso.toString()
        p0.tvCarbohidratos?.text = mediciones.get(position).carbohidratos.toString()


        // aqui comprobamos los valores de la glucosa que recibimos y cambiamos el color segun hemos establecido
        if (mediciones.get(position).glucosa >= 90 && mediciones.get(position).glucosa <= 160){
            p0.tvGlucosa.setTextColor(Color.GREEN)
        }
        if (mediciones.get(position).glucosa < 90 || mediciones.get(position).glucosa > 160){
            p0.tvGlucosa.setTextColor(Color.RED)
        }

    }


    /**
     metodo necesario para cualquier adaptador
     obtiene el numero de datos
     */
    override fun getItemCount(): Int {
        return mediciones.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    /**
    metodo necesario para cualquier adaptador
    Esto infla el layout lista_item
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MedicionesHolder {

        val layoutInflater = LayoutInflater.from(p0.context)
        val inflar = layoutInflater.inflate(R.layout.lista_item, p0, false)

        // referenciamos nuestro adantador con el evento onClick
        inflar.setOnClickListener(){

        }

        return MedicionesHolder(inflar)
    }

    /**
    // añadimos esta funcion para poder borrar luego en el recyclerview con un swipe,
    // cambiando coches por nuestro array
    */
    fun removeItem(position: Int) {

        mediciones.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mediciones.size)
    }

    /**
     * metodo para hacer click en cada item del recycledview
     */
    fun setOnClickListener(listener: View.OnClickListener){
        this.listener = listener
    }


    override fun onClick(v: View?) {
        if (listener != null){
            listener?.onClick(v)
        }
    }


}


