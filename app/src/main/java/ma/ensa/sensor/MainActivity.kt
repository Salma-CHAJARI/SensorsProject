package ma.ensa.sensor

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            sendNotification("New notification about your sensor", "This is an alert notification.")
            Snackbar.make(view, "Notification sent", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_share,
                R.id.nav_list,
                R.id.nav_temp,
                R.id.nav_humd,
                R.id.nav_compass,
                R.id.nav_acceleration,
                R.id.nav_proximity,
                R.id.nav_magnetic,
                R.id.nav_gravity_rotation,
                R.id.nav_step_counter

            ),
            drawer
        )

        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            when (id) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                }

                R.id.nav_share -> {
                    navController.navigate(R.id.nav_share)
                }

                R.id.nav_list -> {
                    navController.navigate(R.id.nav_list)
                }

                R.id.nav_temp -> {
                    navController.navigate(R.id.nav_temp)
                }

                R.id.nav_humd -> {
                    navController.navigate(R.id.nav_humd)
                }

                R.id.nav_compass -> {
                    navController.navigate(R.id.nav_compass)
                }

                R.id.nav_acceleration -> {
                    navController.navigate(R.id.nav_acceleration)
                }

                R.id.nav_proximity -> {
                    navController.navigate(R.id.nav_proximity)
                }

                R.id.nav_magnetic -> {
                    navController.navigate(R.id.nav_magnetic)
                }

                R.id.nav_gravity_rotation -> {
                    navController.navigate(R.id.nav_gravity_rotation)
                }

                R.id.nav_step_counter -> {
                    navController.navigate(R.id.nav_step_counter)
                }

                else -> {
                    drawer.closeDrawers()
                    return@setNavigationItemSelectedListener false
                }
            }
            drawer.closeDrawers()
            true
        }
    }

        private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val channelDescription = "Channel for notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, "my_channel_id")
            .setSmallIcon(R.drawable.img)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return
        }
        notificationManager.notify(1, builder.build()) // ID de notification
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
