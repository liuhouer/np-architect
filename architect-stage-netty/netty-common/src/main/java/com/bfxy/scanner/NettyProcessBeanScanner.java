package com.bfxy.scanner;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.bfxy.annotation.Cmd;
import com.bfxy.annotation.Module;

/**
 * 	$NettyProcessBeanScanner
 *	使用BeanPostProcessor 在bean初始化之后加载所有的bean
 *	然后找到带有@Module 的bean对象
 *	接下来进行扫描bean对象下的方法中带有 @Cmd, 注解的方法
 *	最后创建对应的Invoker, 并把他们加入到InvokerTable中
 */
@Component
public class NettyProcessBeanScanner implements BeanPostProcessor {
	
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		
		//	1.首先获取当前bean的class类型
		Class<?> clazz = bean.getClass();
		
		boolean isPresent = clazz.isAnnotationPresent(Module.class);
	
		if(isPresent) {
			Method[] methods = clazz.getMethods();
			if(methods != null && methods.length > 0) {
				for(Method m : methods) {
					Module module = clazz.getAnnotation(Module.class);
					Cmd cmd = m.getAnnotation(Cmd.class);
					if(cmd == null) {
						continue;
					}
					String moduleValue = module.module();
					String cmdValue = cmd.cmd();
					
					//	只需要把moduleValue + cmdValue的值与对应的反射对象(invoker) 管理起来(map)
					if(InvokerTable.getInvoker(moduleValue, cmdValue) == null) {
						InvokerTable.addInvoker(moduleValue, cmdValue, Invoker.createInvoker(m, bean));
					} else {
						System.err.println("模块下的命令对应的程序缓存已经存在, module: " + moduleValue + " ,cmd: " + cmdValue);
					}
				}
			}
		}
		
		return bean;
	}
}
