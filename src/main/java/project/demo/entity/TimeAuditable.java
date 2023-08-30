package project.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //ghi nhận thời gian
public abstract class TimeAuditable {
	@CreatedDate //auto gen to date
	@Column(updatable = false)
	private Date createdAt;
	
	@LastModifiedDate
	private Date updateAt;
}
