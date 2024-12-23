package com.pay.payomatix

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pay.payomatix.databinding.EachRowPaymentMethodBinding
import com.pay.payomatix.utils.gone
import com.pay.payomatix.utils.goneIfOrVisible
import com.pay.payomatix.utils.hideKeyboard
import com.pay.payomatix.utils.onNoSafeGroupClick
import com.pay.payomatix.utils.onSafeClick
import com.pay.payomatix.utils.visibleIfOrGone
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentMethodAdapter(
    private var paymentMethodList: ArrayList<PaymentMethodModel>,
    var viewMoreClick: (PaymentMethodModel) -> Unit
) :
    RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder>() {
    private var paymentMethodSubAdapter: PaymentMethodSubAdapter? = null
    private lateinit var context: Context
    private var isCardNumberValid = false
    private var isExpiryDateValid = false
    private var isCvvValid = false
    private var isUPIIDValid = false

    class ViewHolder(val binding: EachRowPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            EachRowPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = paymentMethodList.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataBean = paymentMethodList[position]
        paymentMethodSubAdapter = dataBean.netBanking?.let {
            PaymentMethodSubAdapter(
                paymentMethodList = it,
                isShowSelectedBackGround = true
            ) {}
        }

        holder.binding.apply {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvSubPaymentMethod.layoutManager = layoutManager
            rvSubPaymentMethod.adapter = paymentMethodSubAdapter
            tvTitle.text = dataBean.title
            val backgroundDrawable = when (position) {
                0 -> AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_top_color_white_12
                )  // First item
                paymentMethodList.lastIndex -> AppCompatResources.getDrawable(
                    context,
                    R.drawable.bg_bottom_color_white_12
                )  // Last item
                else -> AppCompatResources.getDrawable(
                    context,
                    R.color.white
                )  // Default white background for other items
            }
            constraintMain.background = backgroundDrawable

            imgPaymentLogo.visibleIfOrGone(dataBean.paymentMethodType == "upi")
            tvOfferAvailable.visibleIfOrGone(dataBean.isOfferAvailable)

            tvViewMore.visibleIfOrGone(dataBean.paymentMethodType == "netBanking")
            tvViewMore.text =
                if (dataBean.paymentMethodType == "netBanking") "VIEW ALL BANKS" else ""

            // set divider visibility based on position
            dividerBottom.goneIfOrVisible(paymentMethodList.lastIndex == position)

            // Set RecyclerView visibility based on isExpanded
            when (dataBean.paymentMethodType) {
                "debitCard", "creditCard" -> {
                    layoutPayWithCard.root.visibleIfOrGone(dataBean.isExpanded)
                    constraintSubPaymentMethod.gone()
                    layoutPayWithUpi.root.gone()
                }
                "upi" -> {
                    layoutPayWithUpi.root.visibleIfOrGone(dataBean.isExpanded)
                    constraintSubPaymentMethod.gone()
                    layoutPayWithCard.root.gone()
                }
                else -> {
                    constraintSubPaymentMethod.visibleIfOrGone(dataBean.isExpanded)
                    layoutPayWithCard.root.gone()
                    layoutPayWithUpi.root.gone()
                }
            }
            // Set arrow rotation based on isExpanded
            ObjectAnimator.ofFloat(imgArrow, "rotation", if (dataBean.isExpanded) 180f else 0f)
                .apply {
                    duration = 100
                    start()
                }
            groupHeading onNoSafeGroupClick {
                groupHeading.hideKeyboard()
                paymentMethodList.forEachIndexed { index, bean ->
                    bean.isExpanded = if (index == position) !bean.isExpanded else false
                }
                notifyDataSetChanged()
            }

            tvViewMore onSafeClick {
                viewMoreClick.invoke(dataBean)
            }

            layoutPayWithCard.apply {
                btnPayNowCard onSafeClick {
                    btnPayNowCard.isEnabled = false
                    edtCardNumber.text?.clear()
                    edtExpiryDate.text?.clear()
                    edtCvv.text?.clear()
                }
                edtCardNumber.doAfterTextChanged {
                    val formattedCardNumber = formatCardNumber(it.toString())
                    if (it.toString() != formattedCardNumber) {
                        edtCardNumber.setText(formattedCardNumber)
                        edtCardNumber.setSelection(formattedCardNumber.length)
                    }
                    val cardNumber = it.toString().replace(" ", "")
                    if (cardNumber.isNotEmpty() && cardNumber.length == 16) {
                        if (isValidCardNumber(cardNumber)) {
                            // Clear the error if card number is valid
                            textInputCardNumber.error = null
                            edtExpiryDate.requestFocus()
                            isCardNumberValid = true
                        } else {
                            // Set error message if card number is invalid
                            textInputCardNumber.error = "Please enter a correct card number"
                            isCardNumberValid = false
                        }
                    } else {
                        // Reset error and validation result if input is empty
                        textInputCardNumber.error = null
                        isCardNumberValid = false
                    }
                    updatePayNowCardButton(holder.binding)
                }

                edtExpiryDate.doAfterTextChanged {
                    val formattedExpiryDate = formatExpiryDate(it.toString())
                    if (it.toString() != formattedExpiryDate) {
                        edtExpiryDate.setText(formattedExpiryDate)
                        edtExpiryDate.setSelection(formattedExpiryDate.length)
                    }

                    val expiryDate = it.toString()
                    if (expiryDate.length == 5) {
                        if (isValidExpiryDate(expiryDate)) {
                            // Clear error if expiry date is valid
                            textInputExpiryDate.error = null
                            edtCvv.requestFocus()
                            isExpiryDateValid = true
                        } else {
                            // Set error message if expiry date is invalid
                            textInputExpiryDate.error = "Invalid Expiry Date"
                            isExpiryDateValid = false
                        }
                    } else {
                        // Clear error if expiry date is not in the correct format
                        textInputExpiryDate.error = null
                        isExpiryDateValid = false
                    }
                    updatePayNowCardButton(holder.binding)
                }

                edtCvv.doAfterTextChanged {
                    val cvv = it.toString()
                    if (cvv.length == 3) {
                        isCvvValid = true
                        edtCvv.clearFocus()
                        edtCvv.hideKeyboard()
                    } else {
                        isCvvValid = false
                    }
                    updatePayNowCardButton(holder.binding) // Check if all fields are valid
                }
            }

            layoutPayWithUpi.apply {
                btnPayNowUPI onSafeClick {
                    btnPayNowUPI.isEnabled = false
                    edtUpiID.text?.clear()
                }

                edtUpiID.doAfterTextChanged {
                    val upiId = it.toString()
                    if (upiId.isNotEmpty()) {
                        if (isValidUpiId(upiId)) {
                            // Clear the error if card number is valid
                            textInputUpiID.error = null
                            edtUpiID.clearFocus()
                            isUPIIDValid = true
                        } else {
                            // Set error message if card number is invalid
                            textInputUpiID.error = "Please enter correct UPI ID"
                            isUPIIDValid = false
                        }
                    } else {
                        // Reset error and validation result if input is empty
                        textInputUpiID.error = null
                        isUPIIDValid = false
                    }
                    updatePayNowUPIButton(holder.binding)
                }
            }
        }
    }

    fun isValidUpiId(upiId: String): Boolean {
        // Corrected regular expression for UPI ID validation
        val upiPattern = "^[a-zA-Z0-9._-]{2,256}@[a-zA-Z]{2,64}$".toRegex()
        return upiPattern.matches(upiId)
    }

    fun formatCardNumber(cardNumber: String): String {
        val cleaned =
            cardNumber.replace("[^0-9]".toRegex(), "") // Remove any non-numeric characters
        val formatted = StringBuilder()
        for (i in cleaned.indices) {
            if (i != 0 && i % 4 == 0) {
                formatted.append(" ") // Add space after every 4 digits
            }
            formatted.append(cleaned[i])
        }
        return formatted.toString()
    }

    fun formatExpiryDate(expiryDate: String): String {
        val cleaned = expiryDate.replace("[^0-9]".toRegex(), "") // Remove non-numeric characters
        val formatted = StringBuilder()

        for (i in cleaned.indices) {
            if (i == 2) {
                formatted.append("/") // Add slash after the second digit (MM)
            }
            formatted.append(cleaned[i])
        }

        return formatted.toString()
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        var sum = 0
        var shouldDouble = false
        for (i in cardNumber.length - 1 downTo 0) {
            var digit = cardNumber[i].toString().toInt()
            if (shouldDouble) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            shouldDouble = !shouldDouble
        }
        return sum % 10 == 0
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        // Check if expiry date is in MM/YY format
        val dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
        dateFormat.isLenient = false

        try {
            val expiry = dateFormat.parse(expiryDate)
            val currentDate = Date()

            // Check if expiry date is in the future
            return expiry != null && expiry.after(currentDate)
        } catch (e: Exception) {
            // If parsing fails, the expiry date is invalid
            return false
        }
    }

    fun updatePayNowCardButton(binding: EachRowPaymentMethodBinding) {
        binding.apply {
            layoutPayWithCard.btnPayNowCard.isEnabled =
                isCardNumberValid && isExpiryDateValid && isCvvValid
        }
    }

    fun updatePayNowUPIButton(binding: EachRowPaymentMethodBinding) {
        binding.apply {
            layoutPayWithUpi.btnPayNowUPI.isEnabled = isUPIIDValid
        }
    }
}