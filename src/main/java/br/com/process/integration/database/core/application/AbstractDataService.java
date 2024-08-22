package br.com.process.integration.database.core.application;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;

public abstract class AbstractDataService<D> implements DataMapper<D> {

	protected D data;

	protected DataMapper<D> mapper;

	@Autowired
	protected MethodInvoker methodInvoker;

	public abstract void setData(D data);

	@Override
	public int executeCount(Map<String, Object> filter, String method) {
		try {
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			return (int) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public D executeSingle(Map<String, Object> filter, String method) {
		try {
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			return (D) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<D> executeAll(Map<String, Object> filter, String method) {
		try {
			Object[] args = MethodReflection.getMethodArgs(mapper.getClass(), method, filter);
			return (List<D>) methodInvoker.invokeMethodReturnObjectWithParameters(
					MethodReflection.getNameMapper(data.getClass().getSimpleName()), method, args);
		} catch (Exception ex) {
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
}
