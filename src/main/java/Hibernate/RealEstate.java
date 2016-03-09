package Hibernate;

/**
 * Created by kapiton on 06.03.16.
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "real_estates")
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Integer id;
    @Column(name = "address")
    private String address;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "cost")
    private int cost;

    public RealEstate(final String address, final Integer ownerId, final int cost) {
        this.address = address;
        this.ownerId = ownerId;
        this.cost = cost;
    }

    @Deprecated
    RealEstate() {}

    public Integer getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwner(final int ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final RealEstate thatUser = (RealEstate) that;
        if (address != null ? !address.equals(thatUser.getAddress()) : thatUser.getAddress() != null) return false;
        if (id != null ? !id.equals(thatUser.getId()) : thatUser.getId() != null) return false;
        if (!ownerId.equals(thatUser.getOwnerId())) return false;
        if (cost != thatUser.getCost()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0) + cost;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, address='%s', ownerId='%s', cost='%d'}",
                getClass().getSimpleName(), id, address, ownerId, cost);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
