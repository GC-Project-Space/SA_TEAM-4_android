package com.chikorita.gamagochi.presentation.config.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.chikorita.gamagochi.presentation.utils.KeyboardVisibilityUtils
import dagger.hilt.android.AndroidEntryPoint
import android.view.LayoutInflater as LayoutInflater1

abstract class BaseBindingFragment<T: ViewDataBinding>(@LayoutRes private val layoutId: Int): Fragment() {
    protected lateinit var binding: T
    private lateinit var callback: OnBackPressedCallback
//    var disposables = CompositeDisposable()
    lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreateView(
        inflater: LayoutInflater1,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initListener()
        afterViewCreated()
        binding.root.isFocusableInTouchMode = true
        binding.root.isClickable = true
        binding.root.setOnTouchListener { view, motionEvent ->
            hideKeyboard(view)
            false
        }
    }

    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initListener() {}
    protected open fun afterViewCreated() {}

    override fun onStop() {
        super.onStop()
//        if (disposables.size() > 0) {
//            disposables.clear()
//        }
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = {
            },
            onHideKeyboard = {
                // binding.fragmentSnsSaveBtn.visibility = View.VISIBLE
            }
        )
    }
    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
