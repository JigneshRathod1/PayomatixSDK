package com.pay.payomatix.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pay.payomatix.NetBankingModel
import com.pay.payomatix.PaymentMethodSubAdapter
import com.pay.payomatix.databinding.BottomSheetDialogBankBinding
import com.pay.payomatix.utils.hideKeyboard
import com.pay.payomatix.utils.onSafeClick

class BankBottomSheetDialog(
    var isCancel: Boolean = false,
    var isPreventBackButton: Boolean = false,
    private val isDraggable: Boolean = true,
    var selectButtonClick: ((NetBankingModel) -> Unit)? = null
) : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetDialogBankBinding
    lateinit var paymentMethodSubAdapter: PaymentMethodSubAdapter
    var bankList: ArrayList<NetBankingModel> = ArrayList()
    var bankListCopy: ArrayList<NetBankingModel> = ArrayList()
    var netBankingModel: NetBankingModel? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.behavior.skipCollapsed = true
            dialog.behavior.isDraggable = isDraggable
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

            /*Add For Cancelable false*/
            dialog.setCanceledOnTouchOutside(isCancel)
            dialog.setCancelable(isCancel)
            /*EO Add For Cancelable false*/
        }

        dialog.setOnShowListener {
            (view?.parent as? ViewGroup)?.background = ColorDrawable(Color.TRANSPARENT)
            // in 6.0.1 keyboard opens automatically
            view?.hideKeyboard()
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetup()
        setOnClick()
    }

    private fun initSetup() {
        binding.apply {
            bankList.apply {
                clear()
                add(NetBankingModel(0, "HDFC Bank"))
                add(NetBankingModel(1, "ICICI Bank"))
                add(NetBankingModel(2, "State Bank of India"))
                add(NetBankingModel(3, "AXIS Bank"))
                add(NetBankingModel(4, "Punjab National Bank"))
                add(NetBankingModel(5, "Kotak Mahindra Bank"))
                add(NetBankingModel(6, "Yes Bank"))
                add(NetBankingModel(7, "Bank of Baroda"))
                add(NetBankingModel(8, "Union Bank of India"))
                add(NetBankingModel(9, "Canara Bank"))
                add(NetBankingModel(10, "IndusInd Bank"))
                add(NetBankingModel(11, "IDBI Bank"))
                add(NetBankingModel(12, "Central Bank of India"))
                add(NetBankingModel(13, "Indian Bank"))
                add(NetBankingModel(14, "Bank of India"))
                add(NetBankingModel(15, "UCO Bank"))
                add(NetBankingModel(16, "Federal Bank"))
                add(NetBankingModel(17, "South Indian Bank"))
                add(NetBankingModel(18, "RBL Bank"))
                add(NetBankingModel(19, "Karnataka Bank"))
                add(NetBankingModel(20, "City Union Bank"))
                add(NetBankingModel(21, "Dhanlaxmi Bank"))
                add(NetBankingModel(22, "Saraswat Bank"))
                add(NetBankingModel(23, "Punjab & Sind Bank"))
                add(NetBankingModel(24, "Allahabad Bank"))
                add(NetBankingModel(25, "Oriental Bank of Commerce"))
                add(NetBankingModel(26, "Standard Chartered Bank"))
                add(NetBankingModel(27, "HSBC Bank"))
                add(NetBankingModel(28, "Deutsche Bank"))
                add(NetBankingModel(29, "JP Morgan Chase Bank"))
            }
            bankListCopy.addAll(bankList)
            paymentMethodSubAdapter = PaymentMethodSubAdapter(bankList) {
                netBankingModel = it
                btnSelect.isEnabled = true
            }
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvBank.layoutManager = layoutManager
            rvBank.adapter = paymentMethodSubAdapter
        }
    }

    private fun setOnClick() {
        binding.apply {

            imgClose onSafeClick {
                dismiss()
            }
            btnSelect onSafeClick {
                netBankingModel?.let { it1 -> selectButtonClick?.invoke(it1) }
                btnSelect.isEnabled = false
                dismiss()
            }
            edtSearch.doAfterTextChanged {
                if (it.toString() != "") {
                    mUpdateList(it.toString().trim())
                } else {
                    bankList.apply {
                        clear()
                        addAll(bankListCopy)
                    }
                    paymentMethodSubAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun mUpdateList(searchkey: String) {
        val temp: ArrayList<NetBankingModel> = ArrayList()

        for (d in bankListCopy) {
            if (d.title.lowercase().contains(searchkey.lowercase())) {
                temp.add(d)
            }
        }
        bankList.apply {
            clear()
            addAll(temp)
        }
        paymentMethodSubAdapter.notifyDataSetChanged()
    }
}