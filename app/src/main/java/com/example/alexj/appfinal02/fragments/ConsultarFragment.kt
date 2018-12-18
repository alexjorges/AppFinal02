package com.example.alexj.appFinal02.fragments

import android.app.Activity
import android.app.ListActivity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.example.alexj.appFinal02.Medicion
import com.example.alexj.appfinal02.R
import com.example.alexj.appFinal02.MedicionesAdaptador
import kotlinx.android.synthetic.main.fragment_consultar.*

class ConsultarFragment : Fragment(), Response.Listener<JSONObject>, Response.ErrorListener, SwipeRefreshLayout.OnRefreshListener{


    // variables para recyclerView
    private lateinit var rvLista: RecyclerView
    private var mediciones: ArrayList<Medicion> = ArrayList()
    private lateinit var medicion: Medicion
    private var url: String = "http://iesayala.ddns.net/alexjorges/mediciones.php"
    private var adaptador : MedicionesAdaptador?=null

    // variable para mensaje en pantalla
    private lateinit var progressBar: ProgressDialog


    // variables para el SwipeRefreshLayout
    private lateinit var hilo:Runnable

    // variables de Volley para json
    lateinit var request: RequestQueue
    lateinit var jsonObjectRequest: JsonObjectRequest

    // variable para swipe
    private val p = Paint()


    /**
    // metodos que hay que sobreescribir al implementar Response de Volley
    */
    override fun onResponse(jsonObj: JSONObject) {

        // creamos un JSONArray
        var jsonArray: JSONArray = jsonObj.getJSONArray("mediciones")//en vez de opt get

        // limpiamos el arrayList para que no duplique datos
        mediciones.clear()
        /**
         * encerramos todo en un try/catch para controlar las excepciones
         */
        try {
            // hacemos un bucle y recorremos el tamaño del jsonArray
            for (i in 0..(jsonArray.length() -1)){

                // se crea un objeto tipo JSONObject y le pasamos la posicion i
                var jsonObject: JSONObject = jsonArray.getJSONObject(i)


                //crear directamente el medicion y pasarle los parametros
                    medicion = Medicion(
                        jsonObject.optString("fecha"),
                        jsonObject.optInt("glucosa"),
                        jsonObject.optInt("km"),
                        jsonObject.optInt("peso"),
                        jsonObject.optInt("carbohidratos")
                    )

                // añadimos el objeto medicion al arraylist mediciones
                    mediciones.add (medicion)
            }

            // Creamos el adaptador
            adaptador = MedicionesAdaptador(mediciones, this)

            //asignamos el adaptador a la lista
            rvLista.adapter= adaptador



        } catch (e: JSONException ){
                e.printStackTrace()
                Toast.makeText(this.context,"No se puedo establecer la conexión",Toast.LENGTH_LONG).show()
        }

    }


    /**
    // metodos que hay que sobreescribir al implementar Response de Volley
     */
    override fun onErrorResponse(error: VolleyError?) {
        Toast.makeText(context,"Error al conectar, - NO HAY CONEXION CON EL SERVIDOR -",Toast.LENGTH_LONG).show()
        Log.e("Volley Error:", error.toString())
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // se crea un view y ese view se infla con el fragment correspondiente
        var vista: View
        vista = inflater?.inflate(R.layout.fragment_consultar, container, false)


        // Enlazamos el reciclerView
        rvLista = vista.findViewById(R.id.rvLista)

        // esto es dar la forma del linearLayout
        rvLista.layoutManager = LinearLayoutManager(context)

        // este método lanza la consulta al servidor y devuelve un json
        cargarWebServiceConsultar()

        // llamamos al metodo para poder borrar haciendo swipe en el recycled view
        initSwipe()

        return vista
    }


    /**
        Metodo para crear un progressBar
     */
    private fun cargarWebServiceConsultar() {

       /* var progressBar = ProgressDialog(context)
        progressBar.setTitle("Cargando...")
        progressBar.setMessage("cargando datos...espere por favor")
        progressBar.show()

        Handler().postDelayed({progressBar.dismiss()},3000)*/

        // creamos un nuevo newRequesQueue y le pasamos el contexto
        request = Volley.newRequestQueue(context)

        // creamos un nuevo jsonObjectRequest, con el metodo GET, la url
        jsonObjectRequest =  JsonObjectRequest(Request.Method.GET,url,null,this,this)

        // añadimos el jsonObjectRequest al request con el metodo .add
        request.add(jsonObjectRequest)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // hacemos al objeto swipeRefresh un onRefresListener para ver cuando es llamado
         // y le decimos que llame al metodo cargarWebServiceConsultar y que pare con un handler a los 3 segundos
        swipeRefresh.setOnRefreshListener{
            cargarWebServiceConsultar()

            Handler().postDelayed(
                {swipeRefresh.isRefreshing = false},3000)
        }
    }





    /**
    // metodos para borrar con un swipe en el recyclerview
    // Todo esto es siempre igual excepto la variable adaptador(adaptador) y rvLista(recycledview)
     // La variable adaptador es declarada en onCreateView
     la funcion removeItem() se implementa en el adaptador
    */
    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    adaptador!!.removeItem(position)
                } else {

                    adaptador!!.removeItem(position)

                }
            }
            private fun removeView() {
                if (view!!.parent != null) {
                    (view!!.parent as ViewGroup).removeView(view)
                }
            }


            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        p.color = Color.parseColor("#bcbebc")
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.delete_w_50px)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p.color = Color.parseColor("#bcbebc")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.delete_w_50px)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        // rvLista es el nombre de nuestro recyclerView arriba declarado
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rvLista)
    }



    override fun onRefresh() {
    }



}
