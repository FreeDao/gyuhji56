package rays.web.rays.form;
public class Contact {
    private String firstname ="fName";
    private String lastname = "lName";
    private String email = "x@y.z";
    private String telephone = "086-12345678901";
	@Override
	public String toString() {
		return "Contact [firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + ", telephone=" + telephone + "]";
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
 
}
