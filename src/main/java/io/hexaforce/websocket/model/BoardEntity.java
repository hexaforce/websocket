package io.hexaforce.websocket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "t_board")
public class BoardEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "order_id")
    private long orderId;

    @Column(name = "client")
    private int client;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "order_type")
    private int orderType;

    @Column(name = "time_in_force")
    private int timeInForce;

    @Column(name = "side")
    private int side;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "order_datetime")
    private Date orderTime;

    @Column(name = "update_datetime")
    private Date updateTime;
    
}
