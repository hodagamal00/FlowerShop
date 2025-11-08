package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.*;

// Class 1
// Helper Class (Generic LinkedList class for LinkedList)
class LinkedList<T> {

    // Data members
    // 1. Storing value of LinkedList
    T data;
    // 2. Storing address of next LinkedList
    LinkedList<T> next;

    // Parameterized constructor to assign value
    LinkedList(T data)
    {

        // This keyword refers to current object itself
        this.data = data;
        this.next = null;
    }
}

// Class 2
// Helper class ( Generic LinkedList class)
class list<T> {

    // Generic LinkedList instance
    LinkedList<T> head;
    // Data member to store length of list
    private int length = 0;

    // Default constructor
    list() { this.head = null; }
    // Method
    // To add LinkedList at the end of List
    void add(T data)
    {

        // Creating new LinkedList with given value
        LinkedList<T> temp = new LinkedList<>(data);

        // Checking if list is empty
        // and assigning new value to head LinkedList
        if (this.head == null) {
            head = temp;
        }

        // If list already exists
        else {

            // Temporary LinkedList for traversal
            LinkedList<T> X = head;

            // Iterating till end of the List
            while (X.next != null) {
                X = X.next;
            }

            // Adding new valued LinkedList at the end of the list
            X.next = temp;
        }

        // Increasing length after adding new LinkedList
        length++;
    }

    // Method
    // To add new LinkedList at any given position
    void add(int position, T data)
    {

        // Checking if position is valid
        if (position > length + 1) {

            // Display message only
            System.out.println(
                    "Position Unavailable in LinkedList");
            return;
        }

        // If new position is head then replace head LinkedList
        if (position == 1) {

            // Temporary LinkedList that stores previous head
            // value
            LinkedList<T> temp = head;

            // New valued LinkedList stored in head
            head = new LinkedList<T>(data);

            // New head LinkedList pointing to old head LinkedList
            head.next = temp;

            return;
        }

        // Temporary LinkedList for traversal
        LinkedList<T> temp = head;

        // Dummy LinkedList with null value that stores previous
        // LinkedList
        LinkedList<T> prev = new LinkedList<T>(null);
        // iterating to the given position
        while (position - 1 > 0) {
            // assigning previous LinkedList
            prev = temp;
            // incrementing next LinkedList
            temp = temp.next;
            // decreasing position counter
            position--;
        }
        // previous LinkedList now points to new value
        prev.next = new LinkedList<T>(data);
        // new value now points to former current LinkedList
        prev.next.next = temp;
    }
    // Method
    // To remove a LinkedList from list
    void remove(T key)
    {

        //  NOTE
        // dummy LinkedList is used to represent the LinkedList before
        // the current LinkedList Since in a Singly Linked-List we
        // cannot go backwards from a LinkedList, we use a dummy
        // LinkedList to represent the previous LinkedList. In case of
        // head LinkedList, since there is no previous LinkedList, the
        // previous LinkedList is assigned to null.

        // Dummy LinkedList with null value
        LinkedList<T> prev = new LinkedList<>(null);

        // Dummy LinkedList pointing to head LinkedList
        prev.next = head;

        // Next LinkedList that points ahead of current LinkedList
        LinkedList<T> next = head.next;

        // Temporary LinkedList for traversal
        LinkedList<T> temp = head;

        // Boolean value that checks whether value to be
        // deleted exists or not
        boolean exists = false;

        // If head LinkedList needs to be deleted
        if (head.data == key) {
            head = head.next;

            // LinkedList to be deleted exists
            exists = true;
        }

        // Iterating over LinkedList
        while (temp.next != null) {

            // We convert value to be compared into Strings
            // and then compare using
            // String1.equals(String2) method

            // Comparing value of key and current LinkedList
            if (String.valueOf(temp.data).equals(
                    String.valueOf(key))) {

                // If LinkedList to be deleted is found previous
                // LinkedList now points to next LinkedList skipping the
                // current LinkedList
                prev.next = next;
                // LinkedList to be deleted exists
                exists = true;

                // As soon as we find the LinkedList to be deleted
                // we exit the loop
                break;
            }

            // Previous LinkedList now points to current LinkedList
            prev = temp;

            // Current LinkedList now points to next LinkedList
            temp = temp.next;

            // Next LinkedList points the LinkedList ahead of current
            // LinkedList
            next = temp.next;
        }

        // Comparing the last LinkedList with the given key value
        if (exists == false
                && String.valueOf(temp.data).equals(
                String.valueOf(key))) {

            // If found , last LinkedList is skipped over
            prev.next = null;

            // LinkedList to be deleted exists
            exists = true;
        }

        // If LinkedList to be deleted exists
        if (exists) {

            // Length of LinkedList reduced
            length--;
        }

        // If LinkedList to be deleted does not exist
        else {

            // Print statement
            System.out.println(
                    "Given Value is not present in linked list");
        }
    }

    // Method
    // To clear the entire LinkedList
    void clear()
    {

        // Head now points to null
        head = null;
        // length is 0 again
        length = 0;
    }

    // Method
    // Returns whether List is empty or not
    boolean empty()
    {

        // Checking if head LinkedList points to null
        if (head == null) {
            return true;
        }
        return false;
    }
    // Method
    // Returning the length of LinkedList
    int length() { return this.length; }

    // Method
    // To display the LinkedList
    // @Override
    public String toString()
    {

        String S = "{ ";

        LinkedList<T> X = head;

        if (X == null)
            return S + " }";

        while (X.next != null) {
            S += String.valueOf(X.data) + " -> ";
            X = X.next;
        }

        S += String.valueOf(X.data);
        return S + " }";
    }
}

