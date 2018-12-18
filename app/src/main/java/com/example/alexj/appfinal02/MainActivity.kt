package com.example.alexj.appfinal02

import android.annotation.SuppressLint
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.alexj.appFinal02.Medicion
import com.example.alexj.appFinal02.fragments.BorrarFragment
import com.example.alexj.appFinal02.fragments.ConsultarFragment
import com.example.alexj.appfinal02.Interface.IComunicaFragments
import kotlinx.android.synthetic.main.activity_main.*
import com.example.alexj.appfinal02.fragments.Anadir1Fragment
import com.example.alexj.appfinal02.fragments.DetailFragment


class MainActivity : AppCompatActivity(), BorrarFragment.OnFragmentInteractionListener, Anadir1Fragment.OnFragmentInteractionListener, IComunicaFragments {
    override fun enviar(Medicion: Medicion) {
        var detalleFragments: DetailFragment = DetailFragment()

        var bundleEnvio = Bundle()

        bundleEnvio.putSerializable("objeto",Medicion)
        detalleFragments.arguments.apply { bundleEnvio }

        supportFragmentManager.beginTransaction().replace(R.id.tabItem,DetailFragment()).addToBackStack(null).commit()

    }


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {

            // añadimos esta estructura de control, devolviendo un fragment en cada opcion
            when (position) {
                0 -> {
                    return ConsultarFragment()
                }
                1 -> {
                    return Anadir1Fragment()
                }
                2 -> {
                    return BorrarFragment()
                }
                else -> return null
            }

        }

            override fun getCount(): Int {
                // Show 3 total pages.
                return 3
            }

        override fun getPageTitle(position: Int): CharSequence? {

            // añadimos esta estructura de control when
            when (position) {
                0 -> return "Consultar"
                1 -> return "Añadir"
                2 -> return "Borrar"
            }
            return null
        }
        }
}
