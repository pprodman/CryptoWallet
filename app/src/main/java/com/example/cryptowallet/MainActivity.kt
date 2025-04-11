package com.example.cryptowallet

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptowallet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

/**
 * Actividad principal de la aplicación que maneja la navegación entre fragmentos mediante el componente `NavController` y controla la visibilidad y animación del `NavigationDrawer` dependiendo del fragmento activo.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Configuración de la barra de acción y el `NavigationDrawer`.
     */
    private lateinit var appBarConfiguration: AppBarConfiguration

    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupSpinner()
    }

    /**
     * Configura la barra de herramientas.
     */
    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    /**
     * Configura el `NavigationDrawer` y el controlador de navegación.
     */
    private fun setupNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.marketFragment, R.id.holdingsFragment, R.id.transactionFragment),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Configura el `Spinner` para seleccionar el correo electrónico.
     */
    private fun setupSpinner() {
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val spinner = headerView.findViewById<Spinner>(R.id.spinner)

        val email = arrayOf("michael.saylor@gmail.com", "btc@microstrategy.com")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, email)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    /**
     * Maneja la navegación hacia atrás.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}