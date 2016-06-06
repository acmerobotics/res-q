package com.acmerobotics.library.tree;

import com.acmerobotics.library.inject.core.Binding;
import com.acmerobotics.library.inject.core.IdentityFilter;
import com.acmerobotics.library.inject.core.Injector;
import com.acmerobotics.library.inject.core.InstanceProvider;
import com.acmerobotics.library.inject.core.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Tree {

    public static class Builder {

        private Tree tree;
        private Node current;
        private Injector injector;

        public Builder() {

        }

        public Builder(Injector injector) {
            this.injector = injector;
        }

        public Builder add(Class<? extends Node> node) {
            Node construct = createNode(node);
            System.out.println("tree:\t" + current + "\n\t\tto " + construct);
            if (tree == null) {
                tree = new Tree(construct);
                current = tree.getRoot();
            } else {
                construct.setParentNode(current);
                current.addChild(construct);
                current = construct;
            }
            return this;
        }

        public Builder and(Class<? extends Node> node) {
            Node construct = createNode(node);
            Node parent = current.getParentNode();
            parent.addChild(construct);
            construct.setParentNode(parent);
            current = construct;
            return this;
        }

        public Builder parent() {
            current = current.getParentNode();
            return this;
        }

        public Builder select(int i) {
            current = (Node) current.getChildNodes().get(i);
            return this;
        }

        private Node createNode(Class<? extends Node> node) {
            Node construct = null;
            if (injector == null) {
                try {
                    construct = node.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                construct = injector.create(node);
            }
            return construct;
        }

        public Node getCurrent() {
            return current;
        }

        public Tree tree() {
            return tree;
        }

    }

    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void send(Object data) {
        sendHelper(data, root);
    }

    private void sendHelper(Object data, Node source) {
        System.out.println("node:\t" + source);
        Object output = source.process(data);
        List<Node> childNodes = source.getChildNodes();
        for (Node child : childNodes) {
            System.out.println("node:\tsending output " + output + "\n\t\tto " + child);
            sendHelper(output, child);
        }
    }

}
