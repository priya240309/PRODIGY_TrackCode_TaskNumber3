import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

class Contact implements Serializable {
    private String name;
    private String phoneNumber;
    private String email;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone Number: " + phoneNumber + ", Email: " + email;
    }
}

public class ContactManager {
    private static final String FILE_NAME = "contacts.ser";
    private ArrayList<Contact> contacts;
    private Scanner scanner;

    public ContactManager() {
        contacts = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadContacts();
    }

    private void loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            contacts = (ArrayList<Contact>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing contacts found. Starting a new list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private void addContact() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        contacts.add(new Contact(name, phoneNumber, email));
        System.out.println("Contact added.");
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts to display.");
        } else {
            for (int i = 0; i < contacts.size(); i++) {
                System.out.println((i + 1) + ". " + contacts.get(i));
            }
        }
    }

    private void editContact() {
        viewContacts();
        System.out.print("Enter the contact number to edit: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < contacts.size()) {
            Contact contact = contacts.get(index);
            System.out.println("Editing contact: " + contact);

            System.out.print("Enter new name (leave empty to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                contact.setName(newName);
            }

            System.out.print("Enter new phone number (leave empty to keep current): ");
            String newPhoneNumber = scanner.nextLine();
            if (!newPhoneNumber.isEmpty()) {
                contact.setPhoneNumber(newPhoneNumber);
            }

            System.out.print("Enter new email (leave empty to keep current): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.isEmpty()) {
                contact.setEmail(newEmail);
            }

            System.out.println("Contact updated.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    private void deleteContact() {
        viewContacts();
        System.out.print("Enter the contact number to delete: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
            System.out.println("Contact deleted.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\nContact Manager");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addContact();
                    saveContacts();
                    break;
                case 2:
                    viewContacts();
                    break;
                case 3:
                    editContact();
                    saveContacts();
                    break;
                case 4:
                    deleteContact();
                    saveContacts();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    public static void main(String[] args) {
        ContactManager contactManager = new ContactManager();
        contactManager.showMenu();
    }
}
