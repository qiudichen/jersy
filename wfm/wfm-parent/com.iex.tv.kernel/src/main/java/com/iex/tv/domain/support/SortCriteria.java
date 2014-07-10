package com.iex.tv.domain.support;

public class SortCriteria {
	public SortCriteria.Order getOrder() { return null;}
	public boolean isFiltered() {
		return false;
	}
	public static class Order {
		public boolean isOrdered() {
			return false;
		}
		
		public boolean isAscending() {
			return false;
		}
	}
}
