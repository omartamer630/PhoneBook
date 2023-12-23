/**
 * The {@code Utils} class provides utility methods for various functionalities in a phone book application.
 * It includes methods for error handling, validation of user inputs, and other miscellaneous functions.
 * <p>
 * This class is part of the phone book application and offers a set of static methods to perform
 * common tasks such as input validation, error handling, and data manipulation.
 *
 * Special Thanks:
 * <ul>
 *     <li>Abdalhalim/li>
 * </ul>
 *
 * @authors Mohamed Elsayed , Eyad Hesham , Youssef Diaa, Fireman , Mohamed Gamal ,Mina Hany, Omar tamer Aka 'spectre', Khemu ☥, Abdalhalim
 * @version 2.0
 * @since 2023-12-18
 */
package com.eeluproject.phone.book.Utils;

import com.eeluproject.phone.book.Classes.Admin;
import com.eeluproject.phone.book.Classes.BookedContact;
import com.eeluproject.phone.book.Classes.Contact;
import com.eeluproject.phone.book.Classes.User;
import com.eeluproject.phone.book.Classes.Validator;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * The {@code Utils} class provides a collection of utility methods for common tasks in a phone book application.
 * These tasks include input validation, error handling, and data manipulation.
 */

public class Utils {

  /**
   * A static set that keeps track of allocated contact IDs to ensure uniqueness.
   */
  public static Set<Integer> allocatedIds = new HashSet<>();

  /**
   * Displays an error message using {@code JOptionPane} if the provided {@code Validator} object indicates an error.
   *
   * @param validator The {@code Validator} object to check for errors.
   * @return {@code true} if validation is successful, otherwise {@code false}.
   */

  public static boolean displayError(Validator validator) {
    boolean isValid = true;

    if (validator.getState()) return isValid;

    isValid = false;
    JOptionPane.showMessageDialog(
      null,
      validator.getError(),
      validator.getErrorType(),
      JOptionPane.WARNING_MESSAGE
    );

    return isValid;
  }

  /**
   * Iterates through a list of {@code Validator} objects and displays error messages for each using {@code displayError} method.
   *
   * @param validators An {@code ArrayList} of {@code Validator} objects to check for errors.
   * @return {@code true} if all validations are successful, otherwise {@code false}.
   */

  public static boolean displayErrors(ArrayList<Validator> validators) {
    boolean isValid = true;

    for (Validator validator : validators) {
      boolean result = Utils.displayError(validator);
      if (!result) isValid = false;
    }

    return isValid;
  }

  /**
   * Validates a phone number using Google's {@code libphonenumber} library.
   *
   * @param phone The phone number to validate.
   * @return {@code true} if the phone number is valid, otherwise {@code false}.
   */

  public static boolean validatePhoneNumber(String phone) {
    if (phone.matches("[0-9]+")) {
      PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
      try {
        PhoneNumber parsedPhone = phoneUtil.parse(
          phone,
          CountryUtils.getCountry()
        );
        return phoneUtil.isValidNumber(parsedPhone);
      } catch (NumberParseException e) {
        System.out.println("Error parsing: " + e.getMessage());
      }
    }
    return false;
  }

  /**
   * Provides an additional method for phone number validation based on a specific format.
   *
   * @param phone The phone number to validate.
   * @return {@code true} if the phone number matches the format, otherwise {@code false}.
   */

  public static boolean _validPhone(String phone) {
    phone = phone.trim();
    if (phone.matches("^(01[0-2]|015)\\d{8}$")) {
      return true;
    }
    if (phone.length() != 11) {
      System.err.println("Phone number length must be 11");
    } else {
      System.err.println("Invalid Phone Number Format");
    }
    return false;
  }

  /**
   * Validates a name, allowing only alphabets and spaces.
   *
   * @param name The name to validate.
   * @return {@code true} if the name is valid, otherwise {@code false}.
   */

  public static boolean validateName(String name) {
    name = name.trim();
    if (name.matches("[a-zA-Z ]*")) {
      return true;
    }
    System.err.println("Invalid Name, please enter a valid one");
    return false;
  }

  /**
   * Validates an address, allowing alphanumeric characters, commas, periods, and spaces.
   *
   * @param address The address to validate.
   * @return {@code true} if the address is valid, otherwise {@code false}.
   */

  public static boolean validateAddress(String address) {
    // System.out.println("Invalid address, please Enter a valid one");
    return address.trim().matches(("^[a-zA-Z0-9.,\\s ]+$"));
  }

  public static boolean validateContact(String email) {
    return BookedContact.contacts
      .stream()
      .anyMatch(contact -> contact.getEmail().equals(email));
  }

  /**
   * Validates an email address using a regular expression.
   *
   * @param email The email address to validate.
   * @return {@code true} if the email is valid, otherwise {@code false}.
   */

  public static boolean validateEmail(String email) {
    return email.trim().matches(("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
  }

  /**
   * Validates a password based on specific criteria.
   *
   * @param password The password to validate.
   * @return {@code true} if the password is valid, otherwise {@code false}.
   */

  public static boolean validatePassword(String password) {
    return password.matches(
      "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
    );
  }

  /**
   * Checks if a phone number is duplicated in the system.
   *
   * @param phone The phone number to check for duplication.
   * @return {@code true} if the phone number is duplicated, otherwise {@code false}.
   */

  public static boolean isDuplicated(String phone) {
    return BookedContact.contacts
      .stream()
      .anyMatch(contact -> contact.getPhone().equals(phone));
  }

  /**
   * Checks if a phone number is duplicated in the system, excluding a contact with a specific ID.
   *
   * @param id    The ID of the contact to exclude from duplication check.
   * @param phone The phone number to check for duplication.
   * @return {@code true} if the phone number is duplicated, otherwise {@code false}.
   */

  public static boolean isDuplicated(int id, String phone) {
    return BookedContact.contacts
      .stream()
      .anyMatch(cont -> cont.getId() != id && cont.getPhone().equals(phone));
  }

  /**
   * Generates a random number for contact ID, ensuring its uniqueness.
   *
   * @return The generated random number.
   */

  public static int generateRandomNumber() {
    int id = 0;
    do {
      id = (int) (Math.random() * Math.pow(10, 4));
    } while (Admin.findById(id) != null);

    return id;
  }

  /**
   * Sets values in a {@code DefaultTableModel} based on the provided data and selected row.
   *
   * @param model       The {@code DefaultTableModel} to update.
   * @param data        An {@code ArrayList} of data to set in the model.
   * @param selectedRow The selected row index.
   */

  public static void setValues(
    DefaultTableModel model,
    ArrayList<Object> data,
    int selectedRow
  ) {
    for (int i = 1; i < data.size(); i++) {
      model.setValueAt(data.get(i), selectedRow, i + 1);
    }
  }

  /**
   * Performs quicksort on an ArrayList of Contact objects based on their names.
   *
   * @param contacts The ArrayList of Contact objects to be sorted.
   * @return A sorted ArrayList of Contact objects.
   */

  public static ArrayList<Contact> quickSortNames(ArrayList<Contact> contacts) {
    if (contacts.size() < 2) {
      return contacts;
    }

    Contact pivot = contacts.get(contacts.size() - 1);
    ArrayList<Contact> left = new ArrayList<>();
    ArrayList<Contact> right = new ArrayList<>();

    for (int i = 0; i < contacts.size() - 1; i++) {
      if (contacts.get(i).getName().compareTo(pivot.getName()) > 0) {
        right.add(contacts.get(i));
        continue;
      }
      left.add(contacts.get(i));
    }

    ArrayList<Contact> sorted = new ArrayList<>();
    sorted.addAll(quickSortNames(left));
    sorted.add(pivot);
    sorted.addAll(quickSortNames(right));

    return sorted;
  }

  /**
   * Performs quicksort on an ArrayList of User objects based on their email addresses.
   *
   * @param users The ArrayList of User objects to be sorted.
   * @return A sorted ArrayList of User objects.
   */

  public static ArrayList<User> quickSortEmail(ArrayList<User> users) {
    if (users.size() < 2) {
      return users;
    }

    User pivot = users.get(users.size() - 1);
    ArrayList<User> left = new ArrayList<>();
    ArrayList<User> right = new ArrayList<>();

    for (int i = 0; i < users.size() - 1; i++) {
      if (users.get(i).getEmail().compareTo(pivot.getEmail()) > 0) {
        right.add(users.get(i));
        continue;
      }
      left.add(users.get(i));
    }

    ArrayList<User> sorted = new ArrayList<>();
    sorted.addAll(quickSortEmail(left));
    sorted.add(pivot);
    sorted.addAll(quickSortEmail(right));

    return sorted;
  }

  /**
   * Performs quicksort on an ArrayList of Contact objects based on their email addresses.
   *
   * @param contacts The ArrayList of Contact objects to be sorted.
   * @return A sorted ArrayList of Contact objects.
   */

  public static ArrayList<Contact> quickSortContactEmails(
    ArrayList<Contact> contacts
  ) {
    if (contacts.size() < 2) {
      return contacts;
    }

    Contact pivot = contacts.get(contacts.size() - 1);
    ArrayList<Contact> left = new ArrayList<>();
    ArrayList<Contact> right = new ArrayList<>();

    for (int i = 0; i < contacts.size() - 1; i++) {
      if (contacts.get(i).getEmail().compareTo(pivot.getEmail()) > 0) {
        right.add(contacts.get(i));
        continue;
      }
      left.add(contacts.get(i));
    }

    ArrayList<Contact> sorted = new ArrayList<>();
    sorted.addAll(quickSortContactEmails(left));
    sorted.add(pivot);
    sorted.addAll(quickSortContactEmails(right));

    return sorted;
  }

  /**
   * Performs quicksort on an ArrayList of User objects based on their IDs.
   *
   * @param users The ArrayList of User objects to be sorted.
   * @return A sorted ArrayList of User objects.
   */

  public static ArrayList<User> quickSortIds(ArrayList<User> users) {
    if (users.size() < 2) {
      return users;
    }

    User pivot = users.get(users.size() - 1);
    ArrayList<User> left = new ArrayList<>();
    ArrayList<User> right = new ArrayList<>();

    for (int i = 0; i < users.size() - 1; i++) {
      if (users.get(i).getId() > (pivot.getId())) {
        right.add(users.get(i));
        continue;
      }
      left.add(users.get(i));
    }

    ArrayList<User> sorted = new ArrayList<>();
    sorted.addAll(quickSortIds(left));
    sorted.add(pivot);
    sorted.addAll(quickSortIds(right));

    return sorted;
  }

  /**
   * Performs quicksort on an ArrayList of Contact objects based on their IDs.
   *
   * @param contacts The ArrayList of Contact objects to be sorted.
   * @return A sorted ArrayList of Contact objects.
   */
  public static ArrayList<Contact> quickSortContactIds(
    ArrayList<Contact> contacts
  ) {
    if (contacts.size() < 2) {
      return contacts;
    }

    Contact pivot = contacts.get(contacts.size() - 1);
    ArrayList<Contact> left = new ArrayList<>();
    ArrayList<Contact> right = new ArrayList<>();

    for (int i = 0; i < contacts.size() - 1; i++) {
      if (contacts.get(i).getId() > (pivot.getId())) {
        right.add(contacts.get(i));
        continue;
      }

      left.add(contacts.get(i));
    }

    ArrayList<Contact> sorted = new ArrayList<>();
    sorted.addAll(quickSortContactIds(left));
    sorted.add(pivot);
    sorted.addAll(quickSortContactIds(right));

    return sorted;
  }
}
