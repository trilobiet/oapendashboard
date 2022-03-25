package org.oapen.dashboard.management;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "ip_range")
public class IpRange implements Serializable, Comparable<IpRange> {

	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	String ipStart;
	String ipEnd;
	
	
	// Define a natural ordering
	@Override
	public int compareTo(IpRange o) {
		
		if (ipStart.equals(o.ipStart))
			return ipEnd.compareTo(o.ipEnd);
		else 
			return ipStart.compareTo(o.ipStart);
	}
		
	
}
