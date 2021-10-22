package com.app.picture.entity;

import com.app.announcement.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
public class Picture {
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_Picture")
	@SequenceGenerator(name="seq_Picture",sequenceName="seq_Picture")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "announcement_id")
	private Announcement announcement;

	@NotBlank
	private String repositoryName;

    @NotBlank
    private String miniatureRepositoryName;

    private String fileName;
    private boolean mainPhotoInAnnouncement;

    @Transient
    @Builder.Default
    private boolean pictureToDelete = false;

    @SuppressWarnings("unused")
    public Picture() {
    }

}
