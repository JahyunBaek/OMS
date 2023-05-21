package ko.co._29cm.homework.oms.Exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException() {
        super();
    }
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
