package com.constaapps.constacalc.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.constaapps.constacalc.R
import com.constaapps.constacalc.db.historyTable.HistoryEntity
import com.constaapps.constacalc.recyclerview.HistoryRecyclerViewAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import org.koin.android.viewmodel.ext.android.getViewModel


class MainFragment : Fragment() {

    private lateinit var historyRecyclerViewAdapter: HistoryRecyclerViewAdapter
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.historyRecyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        enableButtons(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()
        viewModel.let { viewModel ->
            viewModel.displayFormula.observe(this, Observer {
                calculatorFormula.text = it.cleanListToString()
            })
            viewModel.currentAnswer.observe(this, Observer {
                calculatorAnswer.text = answerDisplayOutput(it)
            })

            viewModel.inverse.observe(this, Observer {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    inverseButton(it)
                }
            })

            viewModel.degree.observe(this, Observer {
                radDegButton(it)
            })

            historyRecyclerViewAdapter = HistoryRecyclerViewAdapter().also(historyRecyclerView::setAdapter)
            viewModel.getAllHistory().observe(viewLifecycleOwner, Observer {
                historyRecyclerViewAdapter.submitList(it)
                showNoHistoryLog(it.size)
            })
        }
    }

    private fun enableButtons(view: View) {
        val buttons =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                listOf<Button>(
                    view.button!!, view.button1, view.button4, view.button5,
                    view.button6, view.button7, view.button9, view.button10,
                    view.button11, view.button12, view.button13, view.button14, view.button15,
                    view.button16, view.button17, view.button18, view.button19, view.button21!!,
                    view.button23!!, view.button24!!, view.button25!!, view.button26!!,
                    view.button27!!, view.button28!!, view.button29!!, view.button30!!,
                    view.button32!!
                )
            } else {
                listOf<Button>(
                    view.button1, view.button4, view.button5,
                    view.button6, view.button7, view.button9, view.button10,
                    view.button11, view.button12, view.button13, view.button14, view.button15,
                    view.button16, view.button17, view.button18, view.button19
                )
            }


        buttons.forEach { button ->
            button.setOnClickListener {
                if (!button.text.isDigitsOnly()) viewModel.allowDecimal.value = true
                viewModel.currentFormula.update(buttonTextToGrammar(button.text.toString()))
                viewModel.displayFormula.update(buttonTextToDisplayText(button.text.toString()))
            }
        }

        view.button2.setOnClickListener {
            //This is the button for the decimal point.
            if (viewModel.allowDecimal.value!!) {
                viewModel.currentFormula.update(button2.text.toString())
                viewModel.displayFormula.update(button2.text.toString())
                viewModel.allowDecimal.value = false
            }

        }

        view.button8.let { button ->
            // This is the minus button
            button.setOnClickListener {
                viewModel.currentFormula.update(buttonTextToGrammar("-"))
                viewModel.displayFormula.update(buttonTextToDisplayText("-"))
            }

            button.setOnLongClickListener {
                viewModel.currentFormula.update(buttonTextToGrammar("neg"))
                viewModel.displayFormula.update(buttonTextToDisplayText("neg"))
                return@setOnLongClickListener true
            }

        }

        view.button20.let {
            // This is the clear button
            it.setOnClickListener {
                viewModel.currentFormula.delete()
                viewModel.displayFormula.delete()
                if (viewModel.currentFormula.value!!.isEmpty()) {
                    view.calculatorAnswer.text = "0"
                }
                viewModel.allowDecimal.value = true
            }

            it.setOnLongClickListener {
                viewModel.currentFormula.clear()
                viewModel.displayFormula.clear()
                view.calculatorAnswer.text = "0"
                viewModel.allowDecimal.value = true
                return@setOnLongClickListener true
            }
        }

        view.button3.setOnClickListener {
            // This is the equals button
            val formulaDisplay = viewModel.displayFormula.value?.convertAndClean()!!
            val answer = CalculatorBrain.calculate(viewModel.currentFormula.value?.convertAndClean())

            viewModel.let {
                it.currentAnswer.value = answer
                it.saveHistory(HistoryEntity(formulaDisplay, answerDisplayOutput(answer.toString())))
            }


        }

        view.historyImageView.setOnClickListener {
            // THis is the history button
            switchHistoryButtons()
        }

        view.clearBtn.setOnClickListener {
            viewModel.deleteAll()
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.button33?.setOnClickListener {
                //This is the rad button
                viewModel.degree.value = !viewModel.degree.value!!
            }

            view.button31?.setOnClickListener {
                //This is the INV button
                viewModel.inverse.value = !viewModel.inverse.value!!
            }

            view.button22?.setOnClickListener {
                //This is the ANS button

            }

            view.imageView_portrait.setOnClickListener {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.imageView_portrait.setOnClickListener {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    private fun answerDisplayOutput(it: String): String {
        return if (it.matches("-?(([0-9]*\\.[0-9]+)|[0-9]+)".toRegex())) {
            val numberDouble = String.format("%.11f", it.toDouble()).toDouble()

            if ((numberDouble == Math.floor(numberDouble))) {
                numberDouble.toInt().toString()
            } else {
                numberDouble.toString()
            }
        } else {
            it
        }
    }

    private fun buttonTextToGrammar(buttonText: String): String {
        return when (buttonText) {
            //This is for the non inverse
            "-" -> "minus"
            "×" -> "*"
            "÷" -> "/"
            "%" -> "percentage"
            "π" -> "PI"
            "sin" -> "sin("
            "cos" -> "cos("
            "tan" -> "tan("
            "EXP" -> "E"
            "x!" -> ")!"
            "ln" -> "ln("
            "log" -> "log("
            "√" -> "sqrt("
            "xⁿ" -> ")^("
            "abs" -> "abs("
            //This is for the inverse
            "sin⁻¹" -> "sin-1("
            "cos⁻¹" -> "cos-1("
            "tan⁻¹" -> "tan-1("
            "eⁿ" -> "e^("
            "10ⁿ" -> "10^("
            "x²" -> ")^2"
            "ⁿ√x" -> ")root("
            "neg" -> "neg("
            //This is the numbers and char that requires no change.
            else -> buttonText
        }
    }

    private fun buttonTextToDisplayText(buttonText: String): String {
        return when (buttonText) {
            //This is for the non inverse
            "sin" -> "sin("
            "cos" -> "cos("
            "tan" -> "tan("
            "EXP" -> "E"
            "x!" -> ")!"
            "ln" -> "ln("
            "log" -> "log("
            "√" -> "√("
            "xⁿ" -> ")^("
            "abs" -> "abs("
            //This is for the inverse
            "sin⁻¹" -> "sin⁻¹("
            "cos⁻¹" -> "cos⁻¹("
            "tan⁻¹" -> "tan⁻¹("
            "eⁿ" -> "e^("
            "10ⁿ" -> "10^("
            "x²" -> ")^2"
            "ⁿ√x" -> ")√("
            "neg" -> "-("
            //This is the numbers and char that requires no change.
            else -> buttonText
        }
    }

    @SuppressLint("SetTextI18n")
    private fun radDegButton(bool: Boolean) {
        if (bool) {
            button33?.let { button ->
                button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnAccent))
                button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorDarkBtn))
                button.text = "Deg"
            }

        } else {
            button33?.let { button ->
                button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnDark))
                button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorBtn))
                button.text = "Rad"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inverseButton(bool: Boolean) {
        if (bool) {
            button31?.let { button ->
                button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnAccent))
                button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorDarkBtn))
            }
            button30?.text = "sin⁻¹"
            button27?.text = "cos⁻¹"
            button24?.text = "tan⁻¹"
            button29?.text = "eⁿ"
            button26?.text = "10ⁿ"
            button25?.text = "x²"
            button?.text = "ⁿ√x"
        } else {
            button31?.let { button ->
                button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnDark))
                button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorBtn))
            }
            button30?.text = "sin"
            button27?.text = "cos"
            button24?.text = "tan"
            button29?.text = "ln"
            button26?.text = "log"
            button25?.text = "√"
            button?.text = "xⁿ"
        }
    }

    private fun List<String>.convertAndClean(): String {
        val radOrDegree =
            if (viewModel.degree.value!!) {
                this.toString()
                    .replace("sin(", "sdeg(")
                    .replace("cos(", "cdeg(")
                    .replace("tan(", "tdeg(")
                    .replace("sin-1(", "s-1deg(")
                    .replace("cos-1(", "c-1deg(")
                    .replace("tan-1(", "t-1deg(")
            } else {
                this.toString()
                    .replace("sdeg(", "sin(")
                    .replace("cdeg(", "cos(")
                    .replace("tdeg(", "tan(")
                    .replace("s-1deg(", "sin-1(")
                    .replace("c-1deg(", "cos-1(")
                    .replace("t-1deg(", "tan-1(")
            }
        return radOrDegree.replace(" ", "").replace(",", "").dropLast(1).drop(1)
    }


    private fun List<String>.cleanListToString(): String {
        return this.toString().replace(" ", "").replace(",", "").dropLast(1).drop(1)
    }

    private fun MutableLiveData<MutableList<String>>.update(newVal: String) {
        this.value?.add(newVal)
        this.value = this.value
    }

    private fun MutableLiveData<MutableList<String>>.delete() {
        if (!this.value.isNullOrEmpty()) {
            this.value?.removeAt(this.value!!.lastIndex)
            this.value = this.value
        }

    }

    private fun MutableLiveData<MutableList<String>>.clear() {
        if (!this.value.isNullOrEmpty()) {
            this.value?.clear()
            this.value = this.value
        }
    }

    private fun switchHistoryButtons () {
        if (!viewModel.historyDisplay.value!!){
            buttons.visibility = View.GONE
            history.visibility = View.VISIBLE

        }else{
            buttons.visibility = View.VISIBLE
            history.visibility = View.GONE
        }

        viewModel.historyDisplay.value = !viewModel.historyDisplay.value!!
    }


    private fun showNoHistoryLog(adapterSize: Int) {
        if (adapterSize == 0) textNoHistory.visibility = View.VISIBLE
        else textNoHistory.visibility = View.GONE
    }

}