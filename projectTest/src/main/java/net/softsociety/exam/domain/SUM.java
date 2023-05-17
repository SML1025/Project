package net.softsociety.exam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SUM {
	double sum_avg;
	double avg_clean;
	double avg_access;
	double avg_faci;
	double avg_service;
	int avg_count;
}
