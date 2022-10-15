package pl.autohouse.autohousebackend.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.autohouse.autohousebackend.device.Device;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_room")
public class Room {
    @Transient
    private String type = "room";
    @Id
    @SequenceGenerator(
            name = "device_sequence",
            sequenceName = "device_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "device_sequence"
    )
    private Long roomId;
    private String name;
    private Long iconId;

    @JsonIgnore
    @OneToMany
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
