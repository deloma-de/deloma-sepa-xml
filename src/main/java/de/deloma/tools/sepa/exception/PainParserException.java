package de.deloma.tools.sepa.exception;

import java.text.MessageFormat;

public class PainParserException extends Exception {

	private static final long serialVersionUID = 8406608512223304384L;

	public enum ParserExceptionType {
		GENERAL("An error occured while while parsing! "), DOCUMENT(
				"Unable to create xml document!"), PAYMENT_INFO_ERROR(
						"An Error in Payment info level!"), TRANSACTION_ERROR("An Error in Transaction level!");

		String msg;

		private ParserExceptionType(String msg) {
			this.msg = msg;
		}

		public String getMsg() {

			return this.msg;
		}
	}

	private ParserExceptionType exceptionType;

	private String detailMsg;

	public PainParserException() {

	}

	public PainParserException(ParserExceptionType exceptionType) {

		this(exceptionType, null);
	}

	public PainParserException(ParserExceptionType exceptionType, String detailMsg) {
		this(exceptionType, detailMsg, new Throwable());
	}

	public PainParserException(ParserExceptionType exceptionType, String detailMsg, Throwable e) {

		this.exceptionType = exceptionType;
		this.detailMsg = detailMsg;

		this.addSuppressed(new Throwable(getExeptionMsg(), e));
	}

	private String getExeptionMsg() {
		return MessageFormat.format("{0} \n Details: {1} " + this.exceptionType.msg, this.detailMsg);

	}

}
