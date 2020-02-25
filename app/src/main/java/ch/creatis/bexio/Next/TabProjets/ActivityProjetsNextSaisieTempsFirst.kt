package ch.creatis.bexio.Next.TabProjets



import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.fragment_activity_projets_next_saisie_temps_first.*
import java.text.SimpleDateFormat
import java.util.*



class ActivityProjetsNextSaisieTempsFirst : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_activity_projets_next_saisie_temps_first, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Durée
        timePicker.setIs24HourView(true)
        val calendrierDuree = Calendar.getInstance()
        timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            calendrierDuree.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDuree.set(Calendar.MINUTE, minute)
            var time = SimpleDateFormat("HH:mm").format(calendrierDuree.time)
            println(time)

        }



        // Date
        var calendrierDate = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendrierDate.set(Calendar.YEAR, year)
            calendrierDate.set(Calendar.MONTH, monthOfYear)
            calendrierDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
           var date = sdf.format(calendrierDate.time)
           println(date)

        }
        datePicker.setOnClickListener {
            DatePickerDialog(context!!, dateSetListener,
                calendrierDate.get(Calendar.YEAR),
                calendrierDate.get(Calendar.MONTH),
                calendrierDate.get(Calendar.DAY_OF_MONTH)).show()
        }



    }



}