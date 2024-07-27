package br.com.process.integration.database.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.infrastructure.AbstractModel;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductModel extends AbstractModel<ProductModel> {

	private Long id;

	private String title;

	private String type;

	private String brand;

	private String description;

	private Double regular;

	private Double sale;

	private LocalDate scheduleDate;

	private Integer stock;

	private Double discout;

	private LocalDateTime availableDate;

	private LocalDateTime endDate;

	// Category
	private Long product_category_id;
	private String name;
	private String vendor;
	private String message;
	private String tags;

	// Option
	private Long product_option_id;
	private String toppings;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getRegular() {
		return regular;
	}

	public void setRegular(Double regular) {
		this.regular = regular;
	}

	public Double getSale() {
		return sale;
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	public LocalDate getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(LocalDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getDiscout() {
		return discout;
	}

	public void setDiscout(Double discout) {
		this.discout = discout;
	}

	public LocalDateTime getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(LocalDateTime availableDate) {
		this.availableDate = availableDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getProduct_category_id() {
		return product_category_id;
	}

	public void setProduct_category_id(Long product_category_id) {
		this.product_category_id = product_category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Long getProduct_option_id() {
		return product_option_id;
	}

	public void setProduct_option_id(Long product_option_id) {
		this.product_option_id = product_option_id;
	}

	public String getToppings() {
		return toppings;
	}

	public void setToppings(String toppings) {
		this.toppings = toppings;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
