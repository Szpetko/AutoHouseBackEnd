package pl.autohouse.autohousebackend.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.autohouse.autohousebackend.room.Room;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "tbl_device",
        uniqueConstraints = @UniqueConstraint(name="pinAddress_unique", columnNames = "pin_address")
)
public class Device {

    @Transient
    private String type = "device";

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
    private Long deviceId;
    private String name;
    private Long iconId;
    @Transient
    private Boolean status = false;

    @Column(name ="pin_address")
    @NonNull
    private Integer pinAddress;

    @Column(name = "room_id")
    private Long roomId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "room_id",
            referencedColumnName = "roomId",
            insertable = false,
            updatable = false
    )
    private Room room;


    public Device(String name, Long iconId, Integer pinAddress, Room room) {
        this.name = name;
        this.iconId = iconId;
        this.pinAddress = pinAddress;
        this.room = room;
    }

    public String getType() {
        return "device";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getPinAddress() {
        return pinAddress;
    }
}
