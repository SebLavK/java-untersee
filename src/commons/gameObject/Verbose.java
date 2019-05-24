package commons.gameObject;

import java.util.Arrays;

public class Verbose {

	private String header;
	private String message;
	private String[] messageParams;
	private String acknowledgement;

	public Verbose() {
	}

	public Verbose(String header, String message) {
		this.header = header;
		this.message = message;
	}

	public Verbose(String header, String message, String[] messageParams) {
		this.header = header;
		this.message = message;
		this.messageParams = messageParams;
	}

	public Verbose(String header, String message, String[] messageParams, String acknowledgement) {
		this.header = header;
		this.message = message;
		this.messageParams = messageParams;
		this.acknowledgement = acknowledgement;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(String[] messageParams) {
		this.messageParams = messageParams;
	}

	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	@Override
	public String toString() {
		return "Verbose{" +
				"header='" + header + '\'' +
				", message='" + message + '\'' +
				", messageParams=" + Arrays.toString(messageParams) +
				", acknowledgement='" + acknowledgement + '\'' +
				'}';
	}
}
