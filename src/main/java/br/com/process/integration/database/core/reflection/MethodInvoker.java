package br.com.process.integration.database.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.exception.CheckedException;

@Service
public class MethodInvoker {

	@Autowired
	private ApplicationContext applicationContext;

	@Transactional
	public void invokeMethodWithParameters(String beanName, String methodName, Object... params) throws CheckedException {

		try {

			Object bean = applicationContext.getBean(beanName);
			Class<?>[] paramTypes = MethodReflection.transformParametersTypes(params);
			Method method = bean.getClass().getMethod(methodName, paramTypes);
			method.invoke(bean, params);

		} catch (InvocationTargetException e) {
			Throwable tex = e.getTargetException();
			throw new CheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			if (ex.getCause() instanceof CheckedException ccx) {
				throw ccx;
			}
			throw new CheckedException(ex.getLocalizedMessage(), ex);
		}
	}

	@Transactional
	public Object invokeMethodReturnObjectWithParameters(String beanName, String methodName, Object... params) throws CheckedException {

		try {

			Object bean = applicationContext.getBean(beanName);
			Class<?>[] paramTypes = MethodReflection.transformParametersTypes(params);
			Method method = bean.getClass().getMethod(methodName, paramTypes);
			return method.invoke(bean, params);

		} catch (InvocationTargetException e) {
			Throwable tex = e.getTargetException();
			throw new CheckedException(tex.getMessage(), tex);
		} catch (Exception ex) {
			if (ex.getCause() instanceof CheckedException ccx) {
				throw ccx;
			}
			throw new CheckedException(ex.getLocalizedMessage(), ex);
		}

	}
}
