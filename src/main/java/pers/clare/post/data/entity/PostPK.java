package pers.clare.post.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long time;

    @Id
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPK postPK = (PostPK) o;
        return Objects.equals(time, postPK.time) && Objects.equals(userId, postPK.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, userId);
    }
}
