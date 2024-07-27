package br.com.process.integration.database.core.reflection;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.exception.ServiceException;

@Service
public class MethodInvoker {

	@Autowired
	private ApplicationContext applicationContext;

	@Transactional
	public void invokeMethodWithParameters(String beanName, String methodName, Object... params) throws ServiceException {
		try {
			Object bean = applicationContext.getBean(beanName);
			Class<?>[] paramTypes = MethodReflection.transformParametersTypes(params);
			Method method = bean.getClass().getMethod(methodName, paramTypes);
			method.invoke(bean, params);
		} catch (Exception ex) {
			throw new ServiceException("Ocorreu um erro executar o metodo: (" + methodName + ") via reflection, veja se o metodo existe, ou os parametros estao incorretos!", ex);
		}
	}

	@Transactional
	public Object invokeMethodReturnObjectWithParameters(String beanName, String methodName, Object... params) throws ServiceException {
		try {
			Object bean = applicationContext.getBean(beanName);
			Class<?>[] paramTypes = MethodReflection.transformParametersTypes(params);
			Method method = bean.getClass().getMethod(methodName, paramTypes);
			return method.invoke(bean, params);
		} catch (Exception ex) {
			throw new ServiceException("Ocorreu um erro executar o metodo: (" + methodName + ") via reflection, veja se o metodo existe, ou os parametros estao incorretos!", ex);
		}
	}
}
