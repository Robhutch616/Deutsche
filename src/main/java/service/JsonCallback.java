package service;

public interface JsonCallback {
    void onFoundValue(String value);
    void onFailure(String reason);
}
