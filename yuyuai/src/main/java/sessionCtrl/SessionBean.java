package sessionCtrl;

import eventHandler.ILine;

public class SessionBean {

	private String status;
	private String address;

	public SessionBean() {
		status = ILine.DEFAULT;
		address = "";
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String tmpAddress) {
		this.address = tmpAddress;
	}
}
