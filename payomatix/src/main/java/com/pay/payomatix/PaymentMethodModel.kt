package com.pay.payomatix

data class PaymentMethodModel(
    var id: Int,
    var title: String,
    var paymentMethodType: String,
    var isOfferAvailable: Boolean = false,
    var isExpanded: Boolean = false,
    var netBanking: ArrayList<NetBankingModel>? = null
)

data class NetBankingModel(
    var id: Int,
    var title: String,
    var isSelected: Boolean = false
)