package com.event.Participate;

import javax.persistence.*;
import javax.persistence.Table;

import com.member.model.MemberVO;
import com.event.EveModel.EventVO;

//用不到複合主鍵。可能會重複報名.....管他的
@Entity
@Table(name = "event_register") // app-properties, ciag07, table = event
public class ParticipateVO implements java.io.Serializable {
	private static final long serialVersionUID = 11L;

	public ParticipateVO() { // 基本建構子
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ESU_ID")
	private Integer esuID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "evt_id", referencedColumnName = "EVE_ID")
	private EventVO eveID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", referencedColumnName = "MEM_ID")
	private MemberVO memID;

}
