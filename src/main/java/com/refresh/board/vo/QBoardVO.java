package com.refresh.board.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class QBoardVO {
	private Long requestId;             // 상담 요청 ID
    private String consultantName;      // 이름
    private String consultantEmail;     // 이메일
    private String consultationType;    // 문의사항 유형
    private String consultantPassword;   // 비밀번호
    private String consultantMessage;    // 메시지
    private Timestamp requestDate;       // 요청 날짜
    private String answer;
    private String memberId;

    // Getter 및 Setter 메소드
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getConsultantEmail() {
        return consultantEmail;
    }

    public void setConsultantEmail(String consultantEmail) {
        this.consultantEmail = consultantEmail;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getConsultantPassword() {
        return consultantPassword;
    }

    public void setConsultantPassword(String consultantPassword) {
        this.consultantPassword = consultantPassword;
    }

    public String getConsultantMessage() {
        return consultantMessage;
    }

    public void setConsultantMessage(String consultantMessage) {
        this.consultantMessage = consultantMessage;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
    
}
