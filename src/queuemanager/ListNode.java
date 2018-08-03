/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queuemanager;

/**
 *
 * @author LUCIE
 */
public class ListNode<T> {

    // creation of the Node to creat a list of node
    private PriorityItem<T> item;
    private ListNode<T> next;

    ListNode(PriorityItem<T> item, ListNode<T> next) {
        this.item = item;
        this.next = next;
    }

    //retourn the value of the item insade the node
    PriorityItem<T> getItem() {
        return item;
    }

    //give you the next node usfull to travel in the list
    ListNode<T> getNext() {
        return next;
    }

    //put  you in the next node usefull to travel in the list
    void setNext(ListNode<T> next) {
        this.next = next;
    }
}
