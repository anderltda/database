package br.com.process.integration.database.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.process.integration.database.core.domain.BeanEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends RepresentationModel<Product> implements BeanEntity<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Column(name = "type", nullable = true, length = 100)
	private String type;

	@Column(name = "brand", nullable = true, length = 45)
	private String brand;

	@Column(name = "description", nullable = true, length = 100)
	private String description;

	@Column(name = "regular", nullable = false, precision = 10, scale = 0)
	private Double regular;

	@Column(name = "sale", nullable = false, precision = 10, scale = 0)
	private Double sale;

	@Column(name = "schedule_date", nullable = false)
	private LocalDate scheduleDate;

	@Column(name = "stock", nullable = false)
	private Integer stock;

	@Column(name = "discout", nullable = true, precision = 10, scale = 0)
	private Double discout;

	@Column(name = "available_date", nullable = false)
	private LocalDateTime availableDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_category_id", nullable = false, referencedColumnName = "product_category_id")
	private ProductCategory category;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_option_id", nullable = false, referencedColumnName = "product_option_id")
	private ProductOption option;

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

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public ProductOption getOption() {
		return option;
	}

	public void setOption(ProductOption option) {
		this.option = option;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(id, other.id);
	}

}
