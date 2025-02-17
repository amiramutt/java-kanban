package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;
    ArrayList<Task> history = new ArrayList<>();

    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        nodeMap.put(task.getId(), node);
    }

    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
        }
        linkLast(task);
    }

    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    void removeNode(Node node) {

        if (first == node && last == node) {
            first = null;
            last = null;
        } else if (node.prev == null) { // в начале
            first = node.next;
            node.next.prev = null;
        } else if (node.prev != null && node.next != null) { // в середине
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else { // в конце
            last = node.prev;
            node.prev.next = null;
        }
        nodeMap.remove(node.task.getId());
    }

    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

}
