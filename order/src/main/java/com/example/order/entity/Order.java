package com.example.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@Data
@Table(name = "order")
@MappedSuperclass
public class Order  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public Order() {
    }
}
