package de.deloma.tools.sepa.pain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.model.pain.pain0100109.AccountIdentification4Choice;
import de.deloma.tools.sepa.model.pain.pain0100109.ActiveOrHistoricCurrencyAndAmount;
import de.deloma.tools.sepa.model.pain.pain0100109.AmountType4Choice;
import de.deloma.tools.sepa.model.pain.pain0100109.BranchAndFinancialInstitutionIdentification6;
import de.deloma.tools.sepa.model.pain.pain0100109.CashAccount38;
import de.deloma.tools.sepa.model.pain.pain0100109.ChargeBearerType1Code;
import de.deloma.tools.sepa.model.pain.pain0100109.CreditTransferTransaction34;
import de.deloma.tools.sepa.model.pain.pain0100109.CustomerCreditTransferInitiationV09;
import de.deloma.tools.sepa.model.pain.pain0100109.Document;
import de.deloma.tools.sepa.model.pain.pain0100109.FinancialInstitutionIdentification18;
import de.deloma.tools.sepa.model.pain.pain0100109.GroupHeader85;
import de.deloma.tools.sepa.model.pain.pain0100109.PartyIdentification135;
import de.deloma.tools.sepa.model.pain.pain0100109.PaymentIdentification6;
import de.deloma.tools.sepa.model.pain.pain0100109.PaymentInstruction30;
import de.deloma.tools.sepa.model.pain.pain0100109.PaymentMethod3Code;
import de.deloma.tools.sepa.model.pain.pain0100109.PaymentTypeInformation26;
import de.deloma.tools.sepa.model.pain.pain0100109.RemittanceInformation16;
import de.deloma.tools.sepa.model.pain.pain0100109.ServiceLevel8Choice;
import de.deloma.tools.sepa.pain.wrapper.CreditTransferPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.CreditTransferTransactionPain;
import de.deloma.tools.sepa.pain.wrapper.DebtorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;

/**
 * pain.001.001.09 document creator
 *
 */
public class PainDocument00100109
{
	/**
	 * creates a new document of given transfer infos
	 *
	 * @param groupHeaderInfo
	 * @param paymentInfos
	 *
	 * @return
	 *
	 * @throws PainParserException
	 */
	public static Document createDocument(final GroupHeaderInfo groupHeaderInfo,
		final List<CreditTransferPaymentInfoPain> paymentInfos) throws PainParserException
	{
		GroupHeaderInfo.validate(groupHeaderInfo);

		final List<PaymentInstruction30> paymentInstructionList = new ArrayList<>(paymentInfos.size());
		for (final CreditTransferPaymentInfoPain paymentInfo : paymentInfos)
			paymentInstructionList.add(PainDocument00100109.createPaymentInstruction(paymentInfo));

		final int numTxs = paymentInstructionList.stream()
			.collect(Collectors.summingInt(p -> Integer.parseInt(p.getNbOfTxs())));
		if (numTxs < 0)
			throw new PainParserException(ParserExceptionType.GENERAL, "invalid number of transactions!");

		final PartyIdentification135 initiatingParty = new PartyIdentification135();
		initiatingParty.setNm(groupHeaderInfo.getInitiator());

		final GroupHeader85 groupHeader = new GroupHeader85();
		groupHeader.setMsgId(groupHeaderInfo.getMsgId());
		groupHeader.setNbOfTxs(String.valueOf(numTxs));
		groupHeader.setCreDtTm(groupHeaderInfo.getCreationDateTime());
		groupHeader.setInitgPty(initiatingParty);

		final BigDecimal totalAmount = paymentInfos.stream().map(pi -> pi.getTotalAmount())
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		groupHeader.setCtrlSum(totalAmount);

		final CustomerCreditTransferInitiationV09 creditTransferInitiation = new CustomerCreditTransferInitiationV09();
		creditTransferInitiation.setGrpHdr(groupHeader);
		creditTransferInitiation.getPmtInves().addAll(paymentInstructionList);

		final Document document = new Document();
		document.setCstmrCdtTrfInitn(creditTransferInitiation);
		return document;
	}

	/**
	 * creates a single payment instruction with given informations
	 *
	 * @param paymentInfo
	 *            <code>payment informations like paymentInfoId, execution date,
	 *            debtor info and transactions</code>
	 *
	 * @return
	 *
	 * @throws PainParserException
	 */
	private static PaymentInstruction30 createPaymentInstruction(final CreditTransferPaymentInfoPain paymentInfo)
		throws PainParserException
	{
		CreditTransferPaymentInfoPain.validate(paymentInfo);

		final DebtorInfo debtorInfo = paymentInfo.getDebtorInfo();

		final List<CreditTransferTransaction34> transactions = paymentInfo.getTransactions().stream()
			.map(t -> PainDocument00100109.createTransaction(t)).collect(Collectors.toList());

		final PartyIdentification135 debtor = new PartyIdentification135();
		debtor.setNm(debtorInfo.getName());

		final PaymentTypeInformation26 paymentTypeInformation = new PaymentTypeInformation26();

		final ServiceLevel8Choice serviceLevel = new ServiceLevel8Choice();
		serviceLevel.setCd("SEPA");
		paymentTypeInformation.getSvcLvls().add(serviceLevel);

		final PaymentInstruction30 paymentInstruction = new PaymentInstruction30();
		paymentInstruction.setPmtInfId(paymentInfo.getPaymentInfoId());
		paymentInstruction.setPmtMtd(PaymentMethod3Code.TRF);
		paymentInstruction.setNbOfTxs(String.valueOf(transactions.size()));
		paymentInstruction.setCtrlSum(paymentInfo.getTotalAmount());
		paymentInstruction.setPmtTpInf(paymentTypeInformation);
		paymentInstruction.setDbtr(debtor);

		// Debtor Bank Account
		final CashAccount38 debtorAccount = new CashAccount38();
		final AccountIdentification4Choice debtorAccountId = new AccountIdentification4Choice();
		debtorAccountId.setIBAN(debtorInfo.getIban());
		debtorAccount.setId(debtorAccountId);
		paymentInstruction.setDbtrAcct(debtorAccount);

		// Debtor Bank Info
		final BranchAndFinancialInstitutionIdentification6 debtorAgent =
			new BranchAndFinancialInstitutionIdentification6();
		final FinancialInstitutionIdentification18 debtorBank = new FinancialInstitutionIdentification18();
		debtorBank.setBICFI(debtorInfo.getBic());
		debtorAgent.setFinInstnId(debtorBank);
		paymentInstruction.setDbtrAgt(debtorAgent);

		// Constant charge bearer: SLEV
		paymentInstruction.setChrgBr(ChargeBearerType1Code.SLEV);

		// Transactions
		paymentInstruction.getCdtTrfTxInves().addAll(transactions);

		return paymentInstruction;
	}

	/**
	 * creates a single credit transfer transaction
	 *
	 * @param transactionPain
	 *
	 * @return
	 */
	private static CreditTransferTransaction34 createTransaction(final CreditTransferTransactionPain transactionPain)
	{
		final CreditTransferTransaction34 transaction = new CreditTransferTransaction34();

		// pmtId: EndtoEnd
		final PaymentIdentification6 paymentIdentification = new PaymentIdentification6();
		paymentIdentification.setEndToEndId(transactionPain.getEndToEndId());
		transaction.setPmtId(paymentIdentification);

		// Amount
		final ActiveOrHistoricCurrencyAndAmount amount = new ActiveOrHistoricCurrencyAndAmount();
		amount.setValue(transactionPain.getAmount());
		amount.setCcy("EUR");

		final AmountType4Choice amountChoice = new AmountType4Choice();
		amountChoice.setInstdAmt(amount);
		transaction.setAmt(amountChoice);

		// Creditor name
		final PartyIdentification135 creditor = new PartyIdentification135();
		creditor.setNm(transactionPain.getCreditorName());
		transaction.setCdtr(creditor);

		// Creditor Bank Account
		final CashAccount38 creditorAccount = new CashAccount38();
		final AccountIdentification4Choice creditorAccountId = new AccountIdentification4Choice();
		creditorAccountId.setIBAN(transactionPain.getCreditorIban());
		creditorAccount.setId(creditorAccountId);
		transaction.setCdtrAcct(creditorAccount);

		// Creditor Bank Info
		final BranchAndFinancialInstitutionIdentification6 creditorAgent =
			new BranchAndFinancialInstitutionIdentification6();
		final FinancialInstitutionIdentification18 creditorBank = new FinancialInstitutionIdentification18();
		creditorBank.setBICFI(transactionPain.getCreditorBic());
		creditorAgent.setFinInstnId(creditorBank);
		transaction.setCdtrAgt(creditorAgent);

		// Verwendungszweck
		final RemittanceInformation16 remittanceInformation = new RemittanceInformation16();
		remittanceInformation.getUstrds().add(transactionPain.getUstrdRemInf());
		transaction.setRmtInf(remittanceInformation);

		return transaction;
	}
}