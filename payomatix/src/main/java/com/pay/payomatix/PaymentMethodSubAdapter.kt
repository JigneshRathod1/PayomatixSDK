package com.pay.payomatix

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pay.payomatix.databinding.EachRowSubPaymentMethodBinding
import com.pay.payomatix.utils.onNoSafeClick

class PaymentMethodSubAdapter(
    private var paymentMethodList: ArrayList<NetBankingModel>,
    var isShowSelectedBackGround: Boolean = false,
    var onRadioButtonClick: (NetBankingModel) -> Unit
) :
    RecyclerView.Adapter<PaymentMethodSubAdapter.ViewHolder>() {
    private var oldPosition: Int = 0
    private var newPosition: Int = -1
    private lateinit var context: Context

    class ViewHolder(val binding: EachRowSubPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            EachRowSubPaymentMethodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = paymentMethodList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataBean = paymentMethodList[position]


        holder.binding.apply {
            tvTitle.text = dataBean.title

            rbPaymentMethod.isSelected = dataBean.isSelected == true
            if (isShowSelectedBackGround) {
                constraintMain.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        if (dataBean.isSelected) R.color.light_green else R.color.white
                    )
                )
            }
            if (dataBean.isSelected) {
                oldPosition = holder.absoluteAdapterPosition
            }
            root onNoSafeClick  {
                newPosition = holder.absoluteAdapterPosition
                ((paymentMethodList[oldPosition])).isSelected = false
                ((paymentMethodList[newPosition])).isSelected = true
                notifyItemChanged(oldPosition)
                notifyItemChanged(newPosition)
                onRadioButtonClick.invoke(dataBean)
            }
        }
    }
}