package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.archit.calendardaterangepicker.customviews.printDate
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.controller.home.main_menu.presence.PresenceConfirmation
import kotlinx.android.synthetic.main.activity_detail_courses.*
import kotlinx.android.synthetic.main.activity_range_date_picker_create_eleave.*
import kotlinx.android.synthetic.main.lay_item_surattugas_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RangeDatePickerCreateEleave : AppCompatActivity(), View.OnClickListener {

    private val tags = RangeDatePickerCreateEleave::class.java.simpleName
    private lateinit var calendarCuti : DateRangeCalendarView
    private lateinit var calendarPresConf : DateRangeCalendarView
    private var codeTipeCuti : String? = null
    private var nameTipeCuti : String? = null
    private var fromAcvitity : String? = null

    val interimDates = arrayListOf<String>()
    val interimDatesShow = arrayListOf<String>()
    var currentYear : Int? = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_range_date_picker_create_eleave)

        // get current year
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Log.d(tags,"check current year di range date picker : $currentYear")

        // initialize
        // (CUTI)
        calendarCuti = findViewById(R.id.rangeCalendarCuti)
        calendarCuti.setCalendarListener(calendarListenerCuti)
        findViewById<View>(R.id.btnReset).setOnClickListener { v: View? -> calendarCuti.resetAllSelectedViews()
            finish()
        }
        // (PRESENCE CONFIRMATION)
        calendarPresConf = findViewById(R.id.rangeCalendarPresConf)
        calendarPresConf.setCalendarListener(calendarListenerPresConf)
        findViewById<View>(R.id.btnReset).setOnClickListener { v: View? -> calendarPresConf.resetAllSelectedViews()
            finish()
        }

        // getting data from intent
        fromAcvitity = intent.getStringExtra("from")
        if (fromAcvitity.equals("presence confirmation")){

            tv_judul_tipecuti.text = "Presence date"
            setVisiblePresConf()

            // set calendar range
            val startMonth = Calendar.getInstance()
            startMonth[currentYear!!, Calendar.JANUARY] = 0   // contoh lainnya
//            startMonth.add(Calendar.MONTH, 0)
            val endMonth = startMonth.clone() as Calendar
            endMonth.add(Calendar.MONTH, 24)
            Log.d(tags, "Start month: " + startMonth.time.toString() + " :: End month: " + endMonth.time.toString())
            calendarPresConf.setVisibleMonthRange(startMonth, endMonth)

            val current = startMonth.clone() as Calendar
//            current.add(Calendar.DAY_OF_MONTH, -1) // kalo 1 awal nampil datepickernya, ke bulan depan dari tanggal sekarang
            current.add(Calendar.DAY_OF_MONTH, -1) // kalo 1 awal nampil datepickernya, ke bulan depan dari tanggal sekarang
            calendarPresConf.setCurrentMonth(current)

        } else if (fromAcvitity.equals("create eleave")) {

            tv_judul_tipecuti.text = "E-leave date"
            setVisibleCuti()

            codeTipeCuti = intent.getStringExtra("codeTipeCuti")
            nameTipeCuti = intent.getStringExtra("nameTipeCuti")

            // set calendar range
            val startMonth = Calendar.getInstance()
            startMonth[currentYear!!, Calendar.JANUARY] = 0   // contoh lainnya
//            startMonth.add(Calendar.MONTH, 0)
            val endMonth = startMonth.clone() as Calendar
            endMonth.add(Calendar.MONTH, 24)
            Log.d(tags, "Start month: " + startMonth.time.toString() + " :: End month: " + endMonth.time.toString())
            calendarCuti.setVisibleMonthRange(startMonth, endMonth)

            val current = startMonth.clone() as Calendar
            current.add(Calendar.DAY_OF_MONTH, -1) // kalo 1 awal nampil datepickernya, ke bulan depan dari tanggal sekarang
            calendarCuti.setCurrentMonth(current)

            // set fix range selected date based on cuti type
            Log.d(tags,"check fix range date selection : $codeTipeCuti")
            if (codeTipeCuti.equals("CTBRN")){
                calendarCuti.setFixedDaysSelection(90)
            }
            else if (codeTipeCuti.equals("CTGGR")){
                calendarCuti.setFixedDaysSelection(44)
            }
            else if (codeTipeCuti.equals("CTHJI")){
                calendarCuti.setFixedDaysSelection(89)
            }
            else if (codeTipeCuti.equals("CTURH")){
                calendarCuti.setFixedDaysSelection(16)
            }
        }


        // on click
        btnSubmitDateRange.setOnClickListener(this)

    }

    private fun setVisibleCuti () {
        calendarCuti.visibility = View.VISIBLE
        calendarPresConf.visibility = View.GONE
    }

    private fun setVisiblePresConf () {
        calendarCuti.visibility = View.GONE
        calendarPresConf.visibility = View.VISIBLE
    }

    private val calendarListenerCuti : CalendarListener = object : CalendarListener {

        override fun onFirstDateSelected(startDate: Calendar) {
//            Toast.makeText(this@RangeDatePickerCreateEleave, "Start Date: " + startDate.time.toString(), Toast.LENGTH_SHORT).show()

            Log.d(tags, "Selected dates: Start: " + printDate(calendarCuti.startDate) + " End:" + printDate(calendarCuti.endDate))
        }

        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
//            startDateVariable = startDate
//            endDateVariable = endDate
            Toast.makeText(this@RangeDatePickerCreateEleave, "Start Date: " + startDate.time.toString() + " End date: " + endDate.time.toString(), Toast.LENGTH_SHORT).show()
            Log.d(tags, "Selected dates: Start: " + printDate(calendarCuti.startDate) + " End:" + printDate(calendarCuti.endDate))
        }

    }

    private val calendarListenerPresConf : CalendarListener = object : CalendarListener {

        override fun onFirstDateSelected(startDate: Calendar) {
//            Toast.makeText(this@RangeDatePickerCreateEleave, "Start Date: " + startDate.time.toString(), Toast.LENGTH_SHORT).show()

            Log.d(tags, "Selected dates: Start: " + printDate(calendarPresConf.startDate) + " End:" + printDate(calendarPresConf.endDate))
        }

        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
//            startDateVariable = startDate
//            endDateVariable = endDate
            Toast.makeText(this@RangeDatePickerCreateEleave, "Start Date: " + startDate.time.toString() + " End date: " + endDate.time.toString(), Toast.LENGTH_SHORT).show()
            Log.d(tags, "Selected dates: Start: " + printDate(calendarPresConf.startDate) + " End:" + printDate(calendarPresConf.endDate))
        }

    }

    private fun getBetweenDates(startDate: Calendar, endDate: Calendar){

        Log.d(tags, "check start date di range : $startDate \n dan endate di range : $endDate")

        var startDateVar = startDate
        var endDateVar = endDate

        val cal = startDateVar
        Log.d(tags,"cal cal cal : $cal")
        cal.add(Calendar.DATE, -1)
        val date = cal.time
        Log.d(tags,"date date date : $date")

        val cal1 = endDateVar
        Log.d(tags,"cal1 cal1 cal1 : $cal1")
        val date1 = cal1.time
        Log.d(tags,"date1 date1 date1 : $date1")

        var initial : Date = date
        var initial1 : Date = date1
        while (initial < initial1 ) {
            cal.add(Calendar.DATE, 1)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            interimDates.add("'${sdf.format(cal.time)}'")
            interimDatesShow.add(sdf.format(cal.time))
            Log.d(tags,"interim di looping : $interimDates")
            Log.d(tags,"interim di looping show : $interimDatesShow")
            initial = cal.time
        }

        Log.d(tags,"check arraynya list nya : $interimDates")
        Log.d(tags,"check show arraynya list nya : $interimDatesShow")


    }

    private fun backToCreateEleave(listDate: ArrayList<String>,listDateShow : ArrayList<String>){
        val intent = Intent(this, CreateEleave::class.java)
//        intent.putExtra("listDate", listDate)
        intent.putStringArrayListExtra("listDate",listDate)
        intent.putStringArrayListExtra("listDateShow",listDateShow)
        intent.putExtra("nameTipeCutiFromRangeDatePicker", nameTipeCuti)
        intent.putExtra("codeTipeCutiFromRangeDatePicker", codeTipeCuti)
        Log.d(tags,"check list date sebelum intent balik : $listDate")
        startActivity(intent)
        finish()
    }

    private fun backToCreatePresenceConfirmation(listDate: ArrayList<String>,listDateShow : ArrayList<String>){
        val intent = Intent(this, PresenceConfirmation::class.java)
//        intent.putExtra("listDate", listDate)
        intent.putStringArrayListExtra("listDate",listDate)
        intent.putStringArrayListExtra("listDateShow",listDateShow)
        Log.d(tags,"check list date sebelum intent balik : $listDate")
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSubmitDateRange -> {

                if (fromAcvitity.equals("presence confirmation")){
                    if (calendarPresConf.startDate == null || calendarPresConf.endDate == null) {
                        Toast.makeText(this, "Pick a date!", Toast.LENGTH_SHORT).show()
                    } else {
                        getBetweenDates(calendarPresConf.startDate!!, calendarPresConf.endDate!!)
                        backToCreatePresenceConfirmation(interimDates,interimDatesShow)
                    }
                } else if (fromAcvitity.equals("create eleave")) {
                    if (calendarCuti.startDate == null || calendarCuti.endDate == null) {
                        Toast.makeText(this, "Pick a date!", Toast.LENGTH_SHORT).show()
                    } else {
                        getBetweenDates(calendarCuti.startDate!!, calendarCuti.endDate!!)
                        backToCreateEleave(interimDates,interimDatesShow)
                    }
                }

            }
        }
    }
}


// set preselected date
//        val startDateSelectable = startMonth.clone() as Calendar
//        startDateSelectable.add(Calendar.DATE, 20)
//        val endDateSelectable = endMonth.clone() as Calendar
//        endDateSelectable.add(Calendar.DATE, -20)
//        Log.d(tags, "startDateSelectable: " + startDateSelectable.time.toString() + " :: endDateSelectable: " + endDateSelectable.time.toString())
//        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable)

// menentukan unselected date di awal dan akhir (10 di awal dan 10 di akhir)
//        val startSelectedDate = startDateSelectable.clone() as Calendar
//        startSelectedDate.add(Calendar.DATE, 10)
//        val endSelectedDate = endDateSelectable.clone() as Calendar
//        endSelectedDate.add(Calendar.DATE, -10)
//        Log.d(tags, "startSelectedDate: " + startSelectedDate.time.toString() + " :: endSelectedDate: " + endSelectedDate.time.toString())
//        calendar.setSelectedDateRange(startSelectedDate, endSelectedDate)