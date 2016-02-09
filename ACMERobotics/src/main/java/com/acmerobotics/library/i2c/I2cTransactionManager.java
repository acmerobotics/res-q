package com.acmerobotics.library.i2c;

import java.util.HashMap;
import java.util.LinkedList;

public class I2cTransactionManager<T> {

    private HashMap<T, I2cTransaction> transactionTable;
    private LinkedList<I2cTransaction> queue;

    private I2cTransaction currentTransaction;
    private int currentActionIndex;

    public class I2cTransaction {

        public LinkedList<I2cAction> actionSequence;

        public I2cTransaction() {
            this.actionSequence = new LinkedList<>();
        }

        public void addAction(I2cAction action) {
            actionSequence.add(action);
        }

        public void removeAction(I2cAction action) {
            actionSequence.remove(action);
        }

    }

    public interface I2cAction {
        void execute(I2cDeviceReaderAndWriter device);
    }

    public class I2cReadWindow {

        public final int startRegister;
        public final int length;

        public I2cReadWindow(int start, int len) {
            startRegister = start;
            length = len;
        }

    }

    public interface I2cReadCallback {
        public void onReadFinished(byte[] data);
    }

    public class I2cRead implements I2cAction {

        private final I2cReadWindow readWindow;
        private final I2cReadCallback callback;

        public I2cRead(I2cReadWindow readI2cReadWindow, I2cReadCallback callback) {
            this.readWindow = readI2cReadWindow;
            this.callback = callback;
        }

        @Override
        public void execute(I2cDeviceReaderAndWriter device) {
              callback.onReadFinished(
                  device.read(readWindow.startRegister, readWindow.length)
              );
        }
    }

    public I2cTransactionManager() {
        transactionTable = new HashMap<>();
        queue = new LinkedList<>();
        currentTransaction = null;
        currentActionIndex = 0;
    }
    
    public void addTransaction(T key, I2cTransaction transaction) {
        transactionTable.put(key, transaction);
    }

    public I2cTransaction getTransaction(T key) {
        return transactionTable.get(key);
    }

    public void queueTransaction(T key) {
        queue.addLast(getTransaction(key));
    }

}
