package tc.wo.joyfui.shuttersoundcontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    companion object {
        const val KEY_NAME = "csc_pref_camera_forced_shuttersound_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateUI()

        main_sw.setOnCheckedChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        writeShuttersound(p1)
        Toast.makeText(this, R.string.apply_message, Toast.LENGTH_SHORT).show()
    }

    private fun checkShuttersound(): Boolean? {
        return try {
            Settings.System.getInt(contentResolver, KEY_NAME) != 0
        } catch (e: Settings.SettingNotFoundException) {
            null
        }
    }

    private fun writeShuttersound(state: Boolean): Boolean {
        return Settings.System.putInt(contentResolver, KEY_NAME, if (state) 1 else 0)
    }

    private fun updateUI() {
        when (checkShuttersound()) {
            true -> {
                unsupported_txt.visibility = View.INVISIBLE
                main_sw.apply {
                    visibility = View.VISIBLE
                    isChecked = true
                }
                on_txt.visibility = View.VISIBLE
                off_txt.visibility = View.VISIBLE
            }

            false -> {
                unsupported_txt.visibility = View.INVISIBLE
                main_sw.apply {
                    visibility = View.VISIBLE
                    isChecked = false
                }
                on_txt.visibility = View.VISIBLE
                off_txt.visibility = View.VISIBLE
            }

            null -> {
                unsupported_txt.visibility = View.VISIBLE
                main_sw.visibility = View.INVISIBLE
                on_txt.visibility = View.INVISIBLE
                off_txt.visibility = View.INVISIBLE
            }
        }
    }

    fun onClick(item: MenuItem) {
        when (item.itemId) {
            R.id.create_menu -> writeShuttersound(false)
        }
        updateUI()
    }
}
