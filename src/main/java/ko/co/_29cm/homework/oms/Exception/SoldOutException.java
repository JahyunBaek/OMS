package ko.co._29cm.homework.oms.Exception;

public class SoldOutException extends Exception{
    public SoldOutException() {
        super();
    }
    public SoldOutException(String message, Throwable cause) {
        super(message, cause);
    }
    public SoldOutException(String message) {
        super(message);
    }
    public SoldOutException(Throwable cause) {
        super(cause);
    }
}
