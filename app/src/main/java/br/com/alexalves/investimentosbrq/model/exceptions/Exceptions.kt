package br.com.alexalves.investimentosbrq.model.exceptions

class UserNotFoundException(override val message: String = "User not found"): Exception()

class PurchaseNotApprovalException(override val message: String = "Purchase not approval"): Exception()

class SaleNotApprovalException(override val message: String = "Sale not approval"): Exception()