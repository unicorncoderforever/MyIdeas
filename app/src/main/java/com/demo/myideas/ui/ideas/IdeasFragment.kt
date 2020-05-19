package com.demo.myideas.ui.ideas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.myideas.MainActivity
import com.demo.myideas.R
import com.demo.myideas.data.model.Idea
import com.demo.myideas.databinding.FragmentIdeasBinding
import com.demo.myideas.ui.ideas.PaginationListener.PAGE_START
import com.demo.myideas.utility.Utility
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [IdeasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IdeasFragment : DaggerFragment() {
    private var fragmentIdeasBinding: FragmentIdeasBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: IdeaAdapter? = null;
    val TAG = "IdeasFragment"

    private var currentPage: Int = PAGE_START
    private var lastPage = false
    private val totalPage = 10
    var loading = false
    var itemCount = 0

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
            inflater, R.layout.fragment_ideas, container, false
        )

        return fragmentIdeasBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        with(viewModel) {
            ideasList.observe(this@IdeasFragment.viewLifecycleOwner, Observer {
                    adapter?.removeLoading()
                    loading = false
                    if (ideasList.value?.size ?: -1 > 0) {
                        fragmentIdeasBinding!!.ideaDetailsRecyclerview.visibility = View.VISIBLE
                    }
                    else{
                        fragmentIdeasBinding!!.ideaDetailsRecyclerview.visibility = View.GONE
                    }
                    adapter?.removeLoading()
                    adapter?.notifyDataSetChanged()

            })
            error.observe(this@IdeasFragment.viewLifecycleOwner, Observer {
                Toast.makeText(context, it.getErrorMessage(resources), Toast.LENGTH_LONG).show()
            })
        }
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        fragmentIdeasBinding!!.ideaDetailsRecyclerview.layoutManager = mLayoutManager
        fragmentIdeasBinding!!.ideaDetailsRecyclerview.itemAnimator = DefaultItemAnimator()
        adapter = IdeaAdapter(viewModel.ideasList.value!!) { idea -> optionMenuClick(idea) }
        fragmentIdeasBinding!!.ideaDetailsRecyclerview.adapter = adapter
        viewModel.fetchIdeas(currentPage) {
            adapter?.removeLoading()
            loading = false

        }
        addPaginationListener(mLayoutManager)
        fragmentIdeasBinding!!.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ideasFragment_to_addIdeasFragment)
        }
    }

    fun logoutUser(){
        viewModel.logoutUser({
            findNavController().navigate(R.id.action_ideasFragment_to_splashFragment)
        }, {
            Utility.showToast(requireContext(),it.getErrorMessage(resources))
        }
        )
    }
    private fun addPaginationListener(layoutManager:RecyclerView.LayoutManager) {
        fragmentIdeasBinding!!.ideaDetailsRecyclerview.addOnScrollListener(object: PaginationListener(layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return lastPage;
            }
            override fun loadMoreItems() {
                Log.e(TAG,"load more items")
                adapter?.addLoading()
                this@IdeasFragment.loading = true;
                currentPage++
                viewModel.fetchIdeas(currentPage) {
                    loading = false;
                };
            }

            override fun isLoading(): Boolean {
                return loading
            }

        });
    }

    private fun setupToolbar() {
        (activity as MainActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity?)!!.supportActionBar!!.setIcon(
            R.drawable.light_bulb
        )
        (activity as MainActivity?)?.enableLogout(true)
        (activity as MainActivity?)!!.supportActionBar!!.title = resources.getString(
            R.string.app_name
        )
    }

    private fun showBottomSheetDialog(idea: Idea) {
        val view: View =
            layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)
        dialog.show()
        (dialog.findViewById<View>(R.id.delete_layout) as View).setOnClickListener {
//            deleteIdea(idea)
            onDeleteClick(idea)
            dialog.dismiss()
        }
        (dialog.findViewById<View>(R.id.edit_layout) as View).setOnClickListener {
            editIdea(idea)
            dialog.dismiss()
        }

    }

    private fun editIdea(idea: Idea) {
        val bundle = bundleOf("idea" to idea)
        findNavController().navigate(R.id.action_ideasFragment_to_addIdeasFragment, bundle)
    }

    private fun deleteIdea(idea: Idea) {
        viewModel.deleteIdea(idea, {
        },
        {
          Utility.showToast(requireContext(), it.getErrorMessage(resources))
        }
        )
    }


    private fun optionMenuClick(idea: Idea) {
        showBottomSheetDialog(idea)
    }

    private fun onDeleteClick(idea: Idea){
        Utility.showDialogue({

            deleteIdea(idea)
        },requireActivity(),R.string.title_diaogue,R.string.delete_dialogue)
    }




}
