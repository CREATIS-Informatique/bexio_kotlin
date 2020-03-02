package ch.creatis.bexio.Next.All.TabTime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.creatis.bexio.R
import kotlinx.android.synthetic.main.fragment_activity_projets_next_saisie_temps_second.*
import java.text.SimpleDateFormat
import java.util.*


class ActivityProjetsNextSaisieTempsSecond : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_activity_projets_next_saisie_temps_second, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        // -----------------------------------------------------------------------------------------

        val calendrierDuree = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendrierDuree.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDuree.set(Calendar.MINUTE, minute)
            var dureeInfoDebut = SimpleDateFormat("HH:mm").format(calendrierDuree.time)
            jourDebutHeureRappel.text = dureeInfoDebut
        }



        jourDebutTimeButton.setOnClickListener { TimePickerDialog(context, timeSetListener, calendrierDuree.get(Calendar.HOUR_OF_DAY), calendrierDuree.get(Calendar.MINUTE), true).show()}







        // -----------------------------------------------------------------------------------------

        var calendrierDate = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendrierDate.set(Calendar.YEAR, year)
            calendrierDate.set(Calendar.MONTH, monthOfYear)
            calendrierDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var dateInfoDebut = sdf.format(calendrierDate.time)



            // Version suisse
            val myFormatTwo = "dd.MM.yyyy"
            val sdfTwo = SimpleDateFormat(myFormatTwo, Locale.US)
            jourDebutDateRappel.text = sdfTwo.format(calendrierDate.time)

        }



        jourDebutCalendarButton.setOnClickListener { DatePickerDialog(context!!, dateSetListener, calendrierDate.get(Calendar.YEAR), calendrierDate.get(Calendar.MONTH), calendrierDate.get(Calendar.DAY_OF_MONTH)).show() }







        // -----------------------------------------------------------------------------------------

        val calendrierDureeFin = Calendar.getInstance()
        val timeSetListenerFin = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendrierDureeFin.set(Calendar.HOUR_OF_DAY, hour)
            calendrierDureeFin.set(Calendar.MINUTE, minute)
            var dureeInfoFin = SimpleDateFormat("HH:mm").format(calendrierDureeFin.time)
            jourFinHeureRappel.text = dureeInfoFin
        }



        jourFinTimeButton.setOnClickListener { TimePickerDialog(context, timeSetListenerFin, calendrierDureeFin.get(Calendar.HOUR_OF_DAY), calendrierDureeFin.get(Calendar.MINUTE), true).show() }







        // -----------------------------------------------------------------------------------------




        jourFinCalendarButton.setOnClickListener {



        }



    }



}
