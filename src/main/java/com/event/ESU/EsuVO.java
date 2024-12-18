package com.event.ESU;


import javax.persistence.*;
import javax.persistence.Table;

import com.member.model.MemberVO;
import com.event.EveModel.EventVO;
/*
//用不到複合主鍵。可能會重複報名.....管他的
@Entity
@Table(name = "event_register")
public class EsuVO implements java.io.Serializable {
    private static final long serialVersionUID = 11L;

    public EsuVO() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ESU_ID")
    private Integer esuID;

    @ManyToOne(fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    @JoinColumn(name = "evt_id", referencedColumnName = "EVE_ID")
    private EventVO eveID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "MEM_ID")
    private MemberVO memID;
    
    
    private Integer esuStat;
    //報名狀態，更新或取消
    
    // Getters and Setters
    public Integer getEsuID() {
        return esuID;
    }

    public void setEsuID(Integer esuID) {
        this.esuID = esuID;
    }

    public EventVO getEveID() {
        return eveID;
    }

    public void setEveID(EventVO eveID) {
        this.eveID = eveID;
    }

    public MemberVO getMemID() {
        return memID;
    }

    public void setMemID(MemberVO memID) {
        this.memID = memID;
    }

	public Integer getEsuStat() {
		return esuStat;
	}

	public void setEsuStat(Integer esuStat) {
		this.esuStat = esuStat;
	}

	public EsuVO(Integer esuID, EventVO eveID, MemberVO memID, Integer esuStat) {
		this.esuID = esuID;
		this.eveID = eveID;
		this.memID = memID;
		this.esuStat = esuStat;
	}
    
	
    
}
*/