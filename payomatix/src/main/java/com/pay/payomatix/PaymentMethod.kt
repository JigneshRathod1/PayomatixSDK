package com.pay.payomatix

import android.content.Context
import android.graphics.Outline
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pay.payomatix.bottomsheet.BankBottomSheetDialog
import com.pay.payomatix.databinding.PaymentMethodLayoutBinding
import com.pay.payomatix.utils.Utils

class PaymentMethod @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var paymentMethodAdapter: PaymentMethodAdapter
    private var paymentMethodList: ArrayList<PaymentMethodModel> = ArrayList()
    private lateinit var bankBottomSheetDialog: BankBottomSheetDialog
    private val imageList = listOf(
        R.drawable.ic_bhim,
        R.drawable.ic_google_pay,
        R.drawable.ic_paytm,
        R.drawable.ic_phonepe,
    )

    // Inflate the layout and set up View Binding
    private var binding: PaymentMethodLayoutBinding =
        PaymentMethodLayoutBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        binding.root.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binding.root)
        paymentMethodList.apply {
            clear()
            add(PaymentMethodModel(0, "Pay by any UPI App", "upi", isOfferAvailable = true))
            add(PaymentMethodModel(1, "Credit Card", "creditCard"))
            add(PaymentMethodModel(2, "Debit Card", "debitCard"))
            add(
                PaymentMethodModel(
                    3,
                    "Net Banking",
                    "netBanking",
                    netBanking = ArrayList<NetBankingModel>().apply {
                        clear()
                        add(NetBankingModel(0, "HDFC Bank"))
                        add(NetBankingModel(1, "ICICI Bank"))
                        add(NetBankingModel(2, "State Bank of India"))
                        add(NetBankingModel(3, "AXIS Bank"))
                    })
            )
        }
        paymentMethodAdapter = PaymentMethodAdapter(paymentMethodList, ::viewMoreClick)

        binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvPaymentMethod.layoutManager = layoutManager
            rvPaymentMethod.adapter = paymentMethodAdapter

            val viewPagerAdapter = AutoImageSliderAdapter(context,imageList)
            viewPager.adapter = viewPagerAdapter
            viewPagerAdapter.autoSlide(viewPager)
            Utils.setCustomScroller(viewPager)
            // set the viewpage circle outline
            viewPager.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
            viewPager.clipToOutline = true
        }
    }


    private fun viewMoreClick(paymentMethodModel: PaymentMethodModel) {
        bankBottomSheetDialog = BankBottomSheetDialog(selectButtonClick = ::selectButtonClick)
        bankBottomSheetDialog.show(
            (context as FragmentActivity).supportFragmentManager,
            "bottomsheet"
        )
    }

    private fun selectButtonClick(netBankingModel: NetBankingModel) {
        // Find the payment method entry with the type "netBanking"
        val netBankingPaymentMethod = paymentMethodList.find { it.paymentMethodType == "netBanking" }

        // If we find a matching type, proceed to update the list
        netBankingPaymentMethod?.netBanking?.forEach { it.isSelected = false }
        val existingMethod = netBankingPaymentMethod?.netBanking?.find { it.id == netBankingModel.id }
        if (existingMethod != null) {
            // If already exists, set the isSelected flag to true
            existingMethod.isSelected = true
        } else {
            // If not found, add the new model to the list
            netBankingPaymentMethod?.netBanking?.add(netBankingModel)
        }
        paymentMethodAdapter.notifyDataSetChanged()
    }

    fun setOriginalPrice(price: String) {
        val originalPrice = "₹$price"
        val spannableString = SpannableString(originalPrice)
        spannableString.setSpan(StrikethroughSpan(), 0, originalPrice.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvOriginalPrice.text = spannableString
    }

    fun setDiscountedPrice(price: String) {
        binding.tvDiscountedPrice.text = "₹$price"
    }

    fun setSavedPrice(price: String) {
        binding.tvSavedPrice.text = "Save ₹$price"
    }
}