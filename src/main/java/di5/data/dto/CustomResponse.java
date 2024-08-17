package di5.data.dto;

import jakarta.ws.rs.core.Response;

import java.io.Serializable;

public class CustomResponse<T> implements Serializable  {
    public String message;
    public T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
