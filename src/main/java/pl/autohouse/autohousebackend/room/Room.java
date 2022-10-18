package pl.autohouse.autohousebackend.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.autohouse.autohousebackend.device.Device;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_room")
public class Room {
    @Transient
    private String type = "room";
    @Id
    @SequenceGenerator(
            name = "room_sequence",
            sequenceName = "room_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "room_sequence"
    )
    private Long roomId;
    private String name;
    private Long iconId;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "room_id",
            referencedColumnName = "roomId"
    )
    private Set<Device> deviceSet;

    public String getType() {
        return "room";
    }

    public void setType(String type) {
        this.type = type;
    }
}
