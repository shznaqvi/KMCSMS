package edu.aku.hassannaqvi.kmc_sms;

public interface MessageListener {
    /**
     * To call this method when new message received and send back
     *
     * @param message Message
     */
    void messageReceived(String message);
}
