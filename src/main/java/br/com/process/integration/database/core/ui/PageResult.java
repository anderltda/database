package br.com.process.integration.database.core.ui;

import java.util.List;

/**
 * @param <T>
 */
public class PageResult<T> {

	private PageMetadata page;
	private List<T> content;

	/**
	 * 
	 */
	public PageResult() { }

	/**
	 * @param content
	 * @param page
	 */
	public PageResult(List<T> content, PageMetadata page) {
		this.content = content;
		this.page = page;
	}

	public PageMetadata getPage() {
		return page;
	}

	public void setPage(PageMetadata page) {
		this.page = page;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	/**
	 * 
	 */
	public static class PageMetadata {
		
		private int size;
		private long totalElements;
		private int totalPages;
		private int number;

		/**
		 * 
		 */
		public PageMetadata() { }

		/**
		 * @param size
		 * @param totalElements
		 * @param totalPages
		 * @param number
		 */
		public PageMetadata(int size, long totalElements, int totalPages, int number) {
			this.size = size;
			this.totalElements = totalElements;
			this.totalPages = totalPages;
			this.number = number;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public long getTotalElements() {
			return totalElements;
		}

		public void setTotalElements(long totalElements) {
			this.totalElements = totalElements;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(int totalPages) {
			this.totalPages = totalPages;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
	}
}
