package com.constaapps.constacalc.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.constaapps.constacalc.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.io.StringReader


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.currentFormula.observe(this, Observer {
            calculatorFormula.text = it
        })

        viewModel.currentAnswer.observe(this, Observer {
            calculatorAnswer.text = it
        })
    }


    private fun enableButtons(view: View) {
        val buttons = listOf<Button>(
            view.button1, view.button2, view.button4, view.button5,
            view.button6, view.button7, view.button8, view.button9, view.button10,
            view.button11, view.button12, view.button13, view.button14, view.button15,
            view.button16, view.button17, view.button18, view.button19
        )



        view.button20.let {
            it.setOnClickListener {
                viewModel.currentFormula.value = viewModel.currentFormula.value!!.dropLast(1)
            }

            it.setOnLongClickListener {
                viewModel.currentFormula.value = ""
                return@setOnLongClickListener true
            }

        }
        view.button3.setOnClickListener {
            view.calculatorAnswer.text = CalculatorBrain.calculate(viewModel.currentFormula.value)

        }

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.currentFormula.value = viewModel.currentFormula.value.orEmpty() + button.text
            }
        }
    }
}

