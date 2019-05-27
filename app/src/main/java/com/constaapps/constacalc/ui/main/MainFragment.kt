package com.constaapps.constacalc.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.constaapps.constacalc.R
import com.constaapps.constacalc.db.historyTable.HistoryEntity
import com.constaapps.constacalc.recyclerview.HistoryRecyclerViewAdapter
import com.constaapps.constacalc.util.*
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
        switchHistoryButtons(false)
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

            historyRecyclerViewAdapter = HistoryRecyclerViewAdapter(viewModel).also(historyRecyclerView::setAdapter)
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
                viewModel.grammarFormula.update(buttonTextToGrammar(button.text.toString()))
                viewModel.displayFormula.update(buttonTextToDisplayText(button.text.toString()))
            }
        }

        view.button2.setOnClickListener {
            //This is the button for the decimal point.
            if (viewModel.allowDecimal.value!!) {
                viewModel.grammarFormula.update(button2.text.toString())
                viewModel.displayFormula.update(button2.text.toString())
                viewModel.allowDecimal.value = false
            }

        }

        view.button8.let { button ->
            // This is the minus button
            button.setOnClickListener {
                viewModel.grammarFormula.update(buttonTextToGrammar("-"))
                viewModel.displayFormula.update(buttonTextToDisplayText("-"))
            }

            button.setOnLongClickListener {
                viewModel.grammarFormula.update(buttonTextToGrammar("neg"))
                viewModel.displayFormula.update(buttonTextToDisplayText("neg"))
                return@setOnLongClickListener true
            }

        }

        view.button20.let {
            // This is the clear button
            it.setOnClickListener {
                viewModel.grammarFormula.delete()
                viewModel.displayFormula.delete()
                if (viewModel.grammarFormula.value!!.isEmpty()) {
                    view.calculatorAnswer.text = "0"
                }
                viewModel.allowDecimal.value = true
            }

            it.setOnLongClickListener {
                viewModel.grammarFormula.clear()
                viewModel.displayFormula.clear()
                view.calculatorAnswer.text = "0"
                viewModel.allowDecimal.value = true
                return@setOnLongClickListener true
            }
        }

        view.button3.setOnClickListener {
            // This is the equals button
            val displayFormula = viewModel.displayFormula.value
            if(!displayFormula.isNullOrEmpty()){
                val formulaDisplay = displayFormula.convertAndClean(viewModel)
                val answer = CalculatorBrain.calculate(viewModel.grammarFormula.value?.convertAndClean(viewModel))

                viewModel.let {
                    it.currentAnswer.value = answer
                    it.saveHistory(
                        HistoryEntity(
                            formulaDisplay,
                            answerDisplayOutput(answer.toString()),
                            answer.matches("-?((\\d*\\.\\d+)|\\d+\\.?)(E\\d+)?".toRegex())
                        )
                    )
                }
            }
        }

        view.historyImageView.setOnClickListener {
            // THis is the history button
            switchHistoryButtons(true)
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
                val validHistory = viewModel.getANS()

                if (!validHistory.isNullOrEmpty()) {
                    val latest = validHistory.first().answer
                    viewModel.grammarFormula.update(latest)
                    viewModel.displayFormula.update(latest)
                }


            }

            view.imageView_portrait.setOnClickListener {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.imageView_portrait.setOnClickListener {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    private fun answerDisplayOutput(it: String): String {
        return if (it.matches("-?((\\d*\\.\\d+)|\\d+\\.?)".toRegex())) {
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
            "EXP" -> "E"
            "xⁿ" -> "^"

            "√" -> smartRoot("root(", "sqrt(")
            // These are the functions
            "sin" -> smartFunction("sin(")
            "cos" -> smartFunction("cos(")
            "tan" -> smartFunction("tan(")
            "sin⁻¹" -> smartFunction("sin-1(")
            "cos⁻¹" -> smartFunction("cos-1(")
            "tan⁻¹" -> smartFunction("tan-1(")
            "eⁿ" -> smartFunction("e^(")
            "10ⁿ" -> smartFunction("10^(")
            "ln" -> smartFunction("ln(")
            "log" -> smartFunction("log(")
            "neg" -> smartFunction("neg(")
            "abs" -> smartFunction("abs(")
            //This is the numbers and char that requires no change.
            "e" -> smartNumber("e")
            "π" -> smartNumber("PI")
            else -> smartNumber(buttonText)
        }
    }

    private fun smartRoot(root: String, sqrt: String): String {
        val formula = if (root == "√(") {
            viewModel.displayFormula.value!!
        } else {
            viewModel.grammarFormula.value!!
        }
        return if (formula.isNotEmpty()) {
            val last = formula.last()
            when {
                isNumber(last) -> root
                last == ")" -> "*$sqrt"
                else -> sqrt
            }
        } else {
            sqrt
        }
    }

    private fun smartNumber(string: String): String {
        val grammarFormula = viewModel.grammarFormula.value!!
        return if (grammarFormula.isNotEmpty()) {
            val last = grammarFormula.last()
            if (string == "PI" || string == "e") {
                if (isNumber(last)) {
                    "*$string"
                } else {
                    string
                }
            } else if (isNumber(string) || string == ".") {
                if (last == ")" || last == "percentage" || last == "PI" || last == "e") {
                    "*$string"
                } else {
                    string
                }
            } else {
                string
            }
        } else {
            string
        }
    }

    private fun smartFunction(string: String): String {
        val grammarFormula = viewModel.grammarFormula.value!!
        return if (grammarFormula.isNotEmpty()) {
            val last = grammarFormula.last()
            if (isNumber(last)) "*$string" else string
        } else {
            string
        }

    }

    private fun isNumber(string: String): Boolean {
        return string.matches("-?((\\d*\\.\\d+)|\\d+\\.?)(E\\d+)?".toRegex()) || string == "PI" || string == "e" || string == "*PI" || string == "*e"
    }

    private fun buttonTextToDisplayText(buttonText: String): String {
        return when (buttonText) {
            //This is for the non inverse
            "sin" -> "sin("
            "cos" -> "cos("
            "tan" -> "tan("
            "EXP" -> "E"
            "ln" -> "ln("
            "log" -> "log("
            "√" -> smartRoot("√(", "√(")
            "xⁿ" -> "^"
            "abs" -> "abs("
            //This is for the inverse
            "sin⁻¹" -> "sin⁻¹("
            "cos⁻¹" -> "cos⁻¹("
            "tan⁻¹" -> "tan⁻¹("
            "eⁿ" -> "e^("
            "10ⁿ" -> "10^("
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
        }
    }

    private fun switchHistoryButtons(switch: Boolean) {
        if (switch) viewModel.historyDisplay.value = !viewModel.historyDisplay.value!!
        if (viewModel.historyDisplay.value!!) {
            buttons.visibility = View.GONE
            history.visibility = View.VISIBLE

        } else {
            buttons.visibility = View.VISIBLE
            history.visibility = View.GONE
        }
    }

    private fun showNoHistoryLog(adapterSize: Int) {
        if (adapterSize == 0) textNoHistory.visibility = View.VISIBLE
        else textNoHistory.visibility = View.GONE
    }

}