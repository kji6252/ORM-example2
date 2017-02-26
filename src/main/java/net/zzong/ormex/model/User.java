package net.zzong.ormex.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
	@Id
	private long id;
	private String name;
	private String email;
	private Date regdt;
}
