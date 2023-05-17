package net.softsociety.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSG {
	int msg_num;
	String msg_title;
	String sender_mem_id;
	String receiver_mem_id;
	String msg_contents;
	int suk_num;
	String msg_date;
}