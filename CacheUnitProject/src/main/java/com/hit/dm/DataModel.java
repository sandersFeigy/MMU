package main.java.com.hit.dm;
import java.util.Objects;


/**
 *This class implements memory pages structure.
 * @param <T> page value type
 */

public class DataModel<T> {
    private Long id;
    private T content;

    public DataModel(Long id, T content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataModel)) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return getId() == dataModel.getId() && getContent().equals(dataModel.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent());
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}

