
package projectcsi.reservoir;

public class Account {

    private final int accountId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String billingAddress;

    public Account(
            int accountId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String billingAddress) {

        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBillingAddress() {
        return billingAddress;
    }
}
