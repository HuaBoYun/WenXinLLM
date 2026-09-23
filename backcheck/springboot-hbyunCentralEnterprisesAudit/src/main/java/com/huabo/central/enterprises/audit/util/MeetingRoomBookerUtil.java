package com.huabo.central.enterprises.audit.util;

import com.huabo.central.enterprises.audit.vo.result.ConferencePlaceTimeResult;

import java.util.List;
import java.util.TreeSet;

/**
 * 会议地址 是否范围内是否冲突
 */
public class MeetingRoomBookerUtil {

	public boolean book(int start, int end, String meetingRoom, List<ConferencePlaceTimeResult> list) {
		TreeSet<Interval> bookedIntervals = new TreeSet<>();
		list.forEach(item -> bookedIntervals
				.add(new Interval((int) item.getConferenceTimeStart().getTime(), (int) item.getConferenceTimeEnd().getTime(),
						item.getConferencePlace())));
		Interval newInterval = new Interval(start, end, meetingRoom);
		// 找到第一个开始时间大于等于新会议开始时间的会议
		Interval floor = bookedIntervals.floor(newInterval);
		if (floor != null && floor.end > start) {
			// 如果上一个会议的结束时间大于新会议的开始时间，则表示重复
			return false;
		}
		// 找到第一个开始时间大于新会议结束时间的会议
		Interval ceiling = bookedIntervals.ceiling(newInterval);
		if (ceiling != null && ceiling.start < end) {
			// 如果下一个会议的开始时间小于新会议的结束时间，则表示重复
			return false;
		}
		return true;
	}

	private static class Interval implements Comparable<Interval> {
		int start;
		int end;
		String meetingRoom;

		public Interval(int start, int end, String meetingRoom) {
			this.start = start;
			this.end = end;
			this.meetingRoom = meetingRoom;
		}

		@Override
		public int compareTo(Interval other) {
			return Integer.compare(this.start, other.start);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Interval interval = (Interval) o;

			if (start != interval.start)
				return false;
			if (end != interval.end)
				return false;
			return meetingRoom != null ? meetingRoom.equals(interval.meetingRoom) : interval.meetingRoom == null;
		}

		@Override
		public int hashCode() {
			int result = start;
			result = 31 * result + end;
			result = 31 * result + (meetingRoom != null ? meetingRoom.hashCode() : 0);
			return result;
		}
	}
}
