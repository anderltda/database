package br.com.process.integration.database.domain.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductView extends RepresentationModel<ProductView> {

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
	private Long productCategoryId;
	private String name;
	private String vendor;
	private String message;
	private String tags;

	// Option
	private Long productOptionId;
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

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Long getProductOptionId() {
		return productOptionId;
	}

	public void setProductOptionId(Long productOptionId) {
		this.productOptionId = productOptionId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(availableDate, brand, description, discout, email, endDate, id, message,
				name, productCategoryId, productOptionId, regular, sale, scheduleDate, stock, tags, title, toppings,
				type, vendor);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductView other = (ProductView) obj;
		return Objects.equals(availableDate, other.availableDate) && Objects.equals(brand, other.brand)
				&& Objects.equals(description, other.description) && Objects.equals(discout, other.discout)
				&& Objects.equals(email, other.email) && Objects.equals(endDate, other.endDate)
				&& Objects.equals(id, other.id) && Objects.equals(message, other.message)
				&& Objects.equals(name, other.name) && Objects.equals(productCategoryId, other.productCategoryId)
				&& Objects.equals(productOptionId, other.productOptionId) && Objects.equals(regular, other.regular)
				&& Objects.equals(sale, other.sale) && Objects.equals(scheduleDate, other.scheduleDate)
				&& Objects.equals(stock, other.stock) && Objects.equals(tags, other.tags)
				&& Objects.equals(title, other.title) && Objects.equals(toppings, other.toppings)
				&& Objects.equals(type, other.type) && Objects.equals(vendor, other.vendor);
	}
}
