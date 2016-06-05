package com.acmerobotics.library.tree;

import com.acmerobotics.library.module.core.Binding;
import com.acmerobotics.library.module.core.Filter;
import com.acmerobotics.library.module.core.IdentityFilter;
import com.acmerobotics.library.module.core.Injector;
import com.acmerobotics.library.module.core.InstanceProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
            if (tree == null) {
                tree = new Tree(construct);
                current = tree.getRoot();
            } else {
                current.addChild(construct);
                current = construct;
            }
            return this;
        }

        public Builder and(Class<? extends Node> node) {
            Node construct = createNode(node);
            current.getParentNode().addChild(construct);
            current = construct;
            return this;
        }

        private Node createNode(Class<? extends Node> node) {
            Node construct;
            if (injector == null) {
                Constructor constructor = null;
                construct = null;
                try {
                    constructor = node.getConstructor(Node.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    construct = (Node) constructor.newInstance(current);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
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

        public Tree finish() {
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
        Object output = source.process(data);
        List<Node> childNodes = source.getChildNodes();
        for (Node child : childNodes) {
            sendHelper(output, source);
        }
    }

}
