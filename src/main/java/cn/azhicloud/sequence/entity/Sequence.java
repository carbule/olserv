package cn.azhicloud.sequence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 09:17
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sequence {

    @Id
    private Long id;
}
