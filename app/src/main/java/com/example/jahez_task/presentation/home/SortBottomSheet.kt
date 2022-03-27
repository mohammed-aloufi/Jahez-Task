package com.example.jahez_task.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jahez_task.databinding.SortBottomSheetBinding
import com.example.jahez_task.utils.Constants.AUTO_SORT
import com.example.jahez_task.utils.Constants.DISTANCE_SORT
import com.example.jahez_task.utils.Constants.RATING_SORT
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: SortBottomSheetBinding

    interface SortCallBack {
        fun onSortSelected(sortBy: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SortBottomSheetBinding.inflate(layoutInflater)

        setChosenRadio(this.tag)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setChosenRadio(chosenRadio: String?) {
        when (chosenRadio) {
            AUTO_SORT -> binding.autoSortRBtn.isChecked = true
            RATING_SORT -> binding.byRatingRBtn.isChecked = true
            DISTANCE_SORT -> binding.byDistanceRBtn.isChecked = true
        }
    }

    private fun setClickListeners() {
        binding.autoSortRBtn.setOnClickListener(this)
        binding.byRatingRBtn.setOnClickListener(this)
        binding.byDistanceRBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.cancelSortButton -> dismiss()
            binding.autoSortRBtn -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(AUTO_SORT)
                }
                dismiss()
            }
            binding.byRatingRBtn -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(RATING_SORT)
                }
                dismiss()
            }
            binding.byDistanceRBtn -> {
                targetFragment?.let {
                    (it as SortCallBack).onSortSelected(DISTANCE_SORT)
                }
                dismiss()
            }
        }
    }
}