package com.demo.myideas.ui.ideas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.demo.myideas.MainActivity
import com.demo.myideas.R
import com.demo.myideas.data.model.Idea
import com.demo.myideas.databinding.FragmentAddIdeasBinding
import com.demo.myideas.ui.ideas.IdeaCalculator.IdeaCalculator
import com.demo.myideas.utility.Utility
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class AddIdeasFragment : DaggerFragment(), AdapterView.OnItemSelectedListener {
    private var fragmentIdeasBinding: FragmentAddIdeasBinding? = null
    val TAG = "AddIdeasFragment"
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var ideaCalculator: IdeaCalculator? = null

    private val viewModel: IdeasViewModel by lazy {
        ViewModelProviders
            .of(this, viewModelFactory)
            .get(IdeasViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentIdeasBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_ideas, container, false
        )
        setUpSpinner(fragmentIdeasBinding!!.impactSpinner)
        setUpSpinner(fragmentIdeasBinding!!.easeSpinner)
        setUpSpinner(fragmentIdeasBinding!!.confidenceSpinner)
        setAverage()
        return fragmentIdeasBinding?.root
    }

    private fun setAverage() {
        val impact = getRatingFromArray(fragmentIdeasBinding!!.impactSpinner)
        val ease = getRatingFromArray(fragmentIdeasBinding!!.easeSpinner)
        val confidence = getRatingFromArray(fragmentIdeasBinding!!.confidenceSpinner)
        ideaCalculator?.setNewData(impact, ease, confidence)
        fragmentIdeasBinding!!.avgText.text = ideaCalculator?.getAvg().toString()
    }

    private fun getRatingFromArray(spinner: Spinner): Int {
        return (spinner.count - spinner.selectedItemPosition)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)?.enableLogout(true)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        setAverage()
    }

    private fun setUpSpinner(spinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.rate_array
            , android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity?)!!.supportActionBar!!.title = ""
        (activity as MainActivity?)!!.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val idea: Idea? = arguments?.getParcelable("idea")
        populateFields(idea)
        this.fragmentIdeasBinding!!.save.setOnClickListener {
            if (fragmentIdeasBinding!!.content.text.isEmpty()) {
                Utility.showToast(this@AddIdeasFragment.requireContext(), R.string.content_error)
            } else {
                inserOrUpdate()
            }
        }

        this.fragmentIdeasBinding!!.cancel.setOnClickListener {
            if (isAdded) {
                requireActivity().onBackPressed()
            } else {
                findNavController().navigate(R.id.action_addIdeasFragment_to_ideasFragment)
            }
        }
    }


    fun logoutUser() {
        viewModel.logoutUser({
            findNavController().navigate(R.id.action_ideasFragment_to_splashFragment)
        }, {
            Utility.showToast(requireContext(), it.getErrorMessage(resources))
        }
        )

    }

    private fun inserOrUpdate() {
        ideaCalculator?.setContent(fragmentIdeasBinding!!.content.text.toString())
        Log.e(TAG, "id " + ideaCalculator?.idea?.id)
        if (!ideaCalculator?.isUpdate!!)
            viewModel.insertIdea(ideaCalculator?.idea, {
                if (isAdded) {
                    requireActivity().onBackPressed()
                } else {
                    findNavController().navigate(R.id.action_addIdeasFragment_to_ideasFragment)
                }
            }, {
                Utility.showToast(requireContext(), it.getErrorMessage(resources))
            }) else {
            viewModel.updateIdea(ideaCalculator?.idea, {
                if (isAdded) {
                    requireActivity().onBackPressed()
                } else {
                    findNavController().navigate(R.id.action_addIdeasFragment_to_ideasFragment)
                }
            }, {
                Utility.showToast(requireContext(), it.getErrorMessage(resources))
            })

        }
    }

    private fun populateFields(idea: Idea?) {
        ideaCalculator = if (idea == null) {
            IdeaCalculator(Idea("", "", 0, 0, 0, avg = 0.0f, createdAt = -1), false)
        } else {
            fragmentIdeasBinding?.content?.setText(idea.content, TextView.BufferType.EDITABLE)
            IdeaCalculator(idea, true)
        }
    }


}
