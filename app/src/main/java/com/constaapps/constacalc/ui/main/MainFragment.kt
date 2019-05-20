package com.constaapps.constacalc.ui.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.constaapps.constacalc.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableButtons(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.currentFormula.observe(this, Observer {
            calculatorFormula.text = it
        })

        viewModel.currentAnswer.observe(this, Observer {
            calculatorAnswer.text = it
        })

        viewModel.inverse.observe(this, Observer {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (it) {
                    button31!!.let{ button ->
                        button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnAccent))
                        button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorAccent))
                    }
                    button30!!.text = "sin⁻¹"
                    button27!!.text = "cos⁻¹"
                    button24!!.text = "tan⁻¹"
                    button29!!.text = "eⁿ"
                    button26!!.text = "10ⁿ"
                    button25!!.text = "x²"
                    button!!.text = "ⁿ√x"
                } else {
                    button31!!.let{ button ->
                        button.background = ColorDrawable(ContextCompat.getColor(context!!, R.color.btnDark))
                        button.setTextColor(ContextCompat.getColor(context!!, R.color.textColorBtn))
                    }
                    button30!!.text = "sin"
                    button27!!.text = "cos"
                    button24!!.text = "tan"
                    button29!!.text = "ln"
                    button26!!.text = "log"
                    button25!!.text = "√"
                    button!!.text = "xⁿ"
                }
            }
        })
    }


    private fun enableButtons(view: View) {


        val buttons =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                listOf<Button>(
                    view.button!!, view.button1, view.button2, view.button4, view.button5,
                    view.button6, view.button7, view.button8, view.button9, view.button10,
                    view.button11, view.button12, view.button13, view.button14, view.button15,
                    view.button16, view.button17, view.button18, view.button19, view.button21!!,
                    view.button23!!, view.button24!!, view.button25!!, view.button26!!,
                    view.button27!!, view.button28!!, view.button29!!, view.button30!!,
                    view.button32!!
                )
            } else {
                listOf<Button>(
                    view.button1, view.button2, view.button4, view.button5,
                    view.button6, view.button7, view.button8, view.button9, view.button10,
                    view.button11, view.button12, view.button13, view.button14, view.button15,
                    view.button16, view.button17, view.button18, view.button19
                )
            }


        buttons.forEach { button ->
            button.setOnClickListener {

                viewModel.currentFormula.value = viewModel.currentFormula.value.orEmpty() +
                        when (button.text.toString()) {
                            //This is for the non inverse
                            "-" -> "-"
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
                            //This is for the inverse
                            "sin⁻¹" -> "sin-1("
                            "cos⁻¹" -> "cos-1("
                            "tan⁻¹" -> "tan-1("
                            "eⁿ" -> "e^("
                            "10ⁿ" -> "10^("
                            "x²" -> ")^2"
                            "ⁿ√x" -> ")root("
                            //This is the numbers and char that requires no change.
                            else -> button.text
                        }
            }
        }

        view.button20.let {
            // This is the clear button
            it.setOnClickListener {
                viewModel.currentFormula.value = viewModel.currentFormula.value!!.dropLast(1)
                if (viewModel.currentFormula.value.toString().isBlank()) {
                    view.calculatorAnswer.text = "0"
                }
            }

            it.setOnLongClickListener {
                viewModel.currentFormula.value = ""
                view.calculatorAnswer.text = "0"
                return@setOnLongClickListener true
            }
        }

        view.button3.setOnClickListener {
            // This is the equals button
            view.calculatorAnswer.text = CalculatorBrain.calculate(viewModel.currentFormula.value)
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.button33!!.setOnClickListener {
                //This is the rad button
            }

            view.button31!!.setOnClickListener {
                //This is the INV button
                viewModel.inverse.value =
                    if (viewModel.inverse.value != null) {
                        !viewModel.inverse.value!!
                    } else {
                        true
                    }
            }

            view.button22!!.setOnClickListener {
                //This is the ANS button
            }
        }
    }
}