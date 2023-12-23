package com.eeluproject.phone.book.Classes;

import com.eeluproject.phone.book.Utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;

public class BookedContact {

  public static ArrayList<Contact> contacts = new ArrayList<>();

  public BookedContact() {}

  public BookedContact(
    String name,
    String phone,
    String address,
    String email,
    int id
  ) {
    contacts.add(new Contact(name, phone, address, email, id));
  }

  public BookedContact(Contact contact) {
    contacts.add(contact);
  }

  public static Contact findById(int id) {
    return contacts
      .stream()
      .filter(contact -> contact.getId() == id)
      .findFirst()
      .orElse(null);
  }

  public static Contact findByEmail(String email) {
    return contacts
      .stream()
      .filter(contact -> contact.getEmail().equals(email))
      .findFirst()
      .orElse(null);
  }

  public static Contact findByPhone(String phone) {
    return contacts
      .stream()
      .filter(contact -> contact.getPhone().equals(phone))
      .findFirst()
      .orElse(null);
  }

  public static ArrayList<Contact> getBookedContactsByEmail(String email) {
    ArrayList<Contact> bookedContact = new ArrayList<>();
    for (Contact contact : getValidContacts()) {
      if (contact.getEmail().equals(email)) {
        bookedContact.add(contact);
      }
    }

    return bookedContact;
  }

  public static ArrayList<Contact> getValidContacts() {
    Iterator<Contact> contacts = BookedContact.contacts.iterator();
    ArrayList<Contact> validContacts = new ArrayList<>();

    while (contacts.hasNext()) {
      Contact contact = contacts.next();

      if (Utils.validatePhoneNumber(contact.getPhone())) {
        validContacts.add(contact);
      }
    }

    return validContacts;
  }

  public static ArrayList<Contact> getValidContacts(User user) {
    Iterator<Contact> contacts = BookedContact.contacts.iterator();
    ArrayList<Contact> validContacts = new ArrayList<>();

    while (contacts.hasNext()) {
      Contact contact = contacts.next();

      // Check if the id of the user matches the id of the contact
      if (
        user.getId() == (contact.getUserId()) &&
        Utils.validatePhoneNumber(contact.getPhone())
        ) {
        System.out.println(contact.getPhone());
        validContacts.add(contact);
      }
    }

    return validContacts;
  }

  public static boolean isDuplicated(String phone) {
    return contacts.stream().anyMatch(cont -> cont.getPhone().equals(phone));
  }

  public static boolean idExists(int id) {
    return contacts.stream().anyMatch(contact -> contact.getUserId() == id);
  }

  public static boolean isEmailConnected(int id, String email) {
    return BookedContact.contacts
      .stream()
      .anyMatch(contact ->
        contact.getEmail().equals(email) && id == contact.getUserId()
      );
  }

  public static void deleteByPhone(String phnoe) {
    contacts.removeIf(contact -> contact.getPhone().equals(phnoe));
  }

  public static void deleteByEmail(String email) {
    contacts.removeIf(contact -> contact.getEmail().equals(email));
  }

  public static ArrayList<Contact> filterhBooked(String s) {
    ArrayList<Contact> contacts1 = new ArrayList<>();
    for (Contact contact : contacts) {
      if (contact.getEmail().toLowerCase().contains(s) || contact.getPhone().toLowerCase().contains(s)  ||  contact.getName().toLowerCase().contains(s) || contact.getAddress().toLowerCase().contains(s) ) {
        contacts1.add(contact);
      }
    }
    return contacts1;
  }

  public static ArrayList<User> filterhEmails(String s) {
    ArrayList<User> users = new ArrayList<>();
    for (User user : User.users) {
      // String contactId = String.valueOf(user.getId());
      if (user.getEmail().toLowerCase().contains(s) ) {
        users.add(user);
      }
    }
    return users;
  }

  public static void display() {
    System.out.println(contacts.size());
    contacts.forEach(contact -> {
      System.out.println(contact);
    });
  }
}
